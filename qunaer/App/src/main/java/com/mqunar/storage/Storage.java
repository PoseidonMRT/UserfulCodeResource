package com.mqunar.storage;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build.VERSION;
import android.os.Environment;
import android.text.TextUtils;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class Storage {
    public static final String DEAFAULT_USER = "visitor";
    public static final String DEFAULT_SANDBOX = "hen";
    private static int a = 0;
    private String b;
    private IStorage c;

    public static Storage newStorage(Context context) {
        return newStorage(context, DEAFAULT_USER);
    }

    public static Storage newStorage(Context context, String str) {
        return new Storage(context, str);
    }

    public static boolean hasFroyo() {
        return VERSION.SDK_INT >= 8;
    }

    @TargetApi(8)
    private static File b(Context context) {
        if (hasFroyo()) {
            File externalFilesDir = context.getExternalFilesDir(null);
            if (externalFilesDir != null) {
                return externalFilesDir;
            }
            return context.getFilesDir();
        }
        return new File(Environment.getExternalStorageDirectory(), "/Android/data/" + context.getPackageName() + "/files");
    }

    public static File getAppFileDir(Context context) {
        FileOutputStream fileOutputStream;
        FileOutputStream fileOutputStream2;
        Throwable th;
        File file = null;
        try {
            if ("mounted".equals(Environment.getExternalStorageState())) {
                File b = b(context);
                if (a == 0) {
                    try {
                        String uuid = UUID.randomUUID().toString();
                        File file2 = new File(b, uuid);
                        if (!file2.exists()) {
                            file2.mkdirs();
                        }
                        File file3 = new File(file2, uuid);
                        try {
                            fileOutputStream = new FileOutputStream(file3);
                            try {
                                fileOutputStream.write(0);
                                fileOutputStream.flush();
                                a = 1;
                                if (fileOutputStream != null) {
                                    fileOutputStream.close();
                                    file3.delete();
                                    file3.getParentFile().delete();
                                }
                            } catch (Throwable th2) {
                                th = th2;
                                file = file3;
                                if (fileOutputStream != null) {
                                    fileOutputStream.close();
                                    file.delete();
                                    file.getParentFile().delete();
                                }
                                throw th;
                            }
                        } catch (Throwable th3) {
                            th = th3;
                            fileOutputStream = null;
                            file = file3;
                            if (fileOutputStream != null) {
                                fileOutputStream.close();
                                file.delete();
                                file.getParentFile().delete();
                            }
                            throw th;
                        }
                    } catch (Throwable th4) {
                        th = th4;
                        fileOutputStream = null;
                        if (fileOutputStream != null) {
                            fileOutputStream.close();
                            file.delete();
                            file.getParentFile().delete();
                        }
                        throw th;
                    }
                }
                if (a == 1) {
                    return b;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Throwable th5) {
            th5.printStackTrace();
        }
        return context.getFilesDir();
    }

    public static File getAppDir(Context context) {
        return getAppFileDir(context).getParentFile();
    }

    public static File getFileDir(Context context) {
        File file = new File(getAppFileDir(context), a(context));
        if (!file.exists()) {
            file.mkdirs();
        }
        return file;
    }

    private Storage(Context context, String str, String str2) {
        Object obj = null;
        this.b = str;
        if (TextUtils.isEmpty(str2)) {
            str2 = DEAFAULT_USER;
        }
        try {
            if (Class.forName("com.mqunar.core.QunarApkLoader") != null) {
                obj = 1;
            }
        } catch (Throwable th) {
        }
        if (obj != null) {
            this.c = SpStorage.newInstance(context, this.b, str2);
        } else if ("mounted".equals(Environment.getExternalStorageState())) {
            this.c = FileStorage.newInstance(context, new File(new File(new File(Environment.getExternalStorageDirectory(), "qunar_file"), this.b), str2));
        } else {
            throw new RuntimeException("不在spider运行时,手机必须得安装sdcard!");
        }
    }

    private Storage(Context context, String str) {
        this(context, a(context), str);
    }

    static String a(Context context) {
        String obj;
        try {
            StackTraceElement stackTraceElement;
            Class cls = Class.forName("com.mqunar.core.QunarApkLoader");
            Method declaredMethod = cls.getDeclaredMethod("isSpiderClass", new Class[]{String.class});
            Method declaredMethod2 = cls.getDeclaredMethod("getPackageName", new Class[]{String.class});
            StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
            StackTraceElement stackTraceElement2 = stackTrace[2];
            String name = Storage.class.getPackage().getName();
            for (StackTraceElement stackTraceElement3 : stackTrace) {
                if (((Boolean) declaredMethod.invoke(null, new Object[]{stackTraceElement3.getClassName()})).booleanValue() && !stackTraceElement3.getClassName().startsWith(name + ".") && !stackTraceElement3.getClassName().startsWith(name + "$")) {
                    stackTraceElement = stackTraceElement3;
                    break;
                }
            }
            stackTraceElement = stackTraceElement2;
            Object invoke = declaredMethod2.invoke(null, new Object[]{stackTraceElement.getClassName()});
            obj = invoke != null ? invoke.toString() : null;
        } catch (ClassNotFoundException e) {
            obj = context.getPackageName();
        } catch (Throwable th) {
            obj = null;
        }
        if (TextUtils.isEmpty(obj)) {
            return DEFAULT_SANDBOX;
        }
        return obj;
    }

    public <T extends Serializable> boolean append(String str, T t) {
        Object string = getString(str, null);
        if (!TextUtils.isEmpty(string)) {
            return putString(str, string + t);
        }
        if (t != null) {
            return putString(str, String.valueOf(t));
        }
        return false;
    }

    public <T extends Serializable> boolean smoothAppend(String str, T t) {
        Object string = getString(str, null);
        if (!TextUtils.isEmpty(string)) {
            return putSmoothString(str, string + t);
        }
        if (t != null) {
            return putSmoothString(str, String.valueOf(t));
        }
        return false;
    }

    public boolean putString(String str, String str2) {
        return this.c.putString(str, str2);
    }

    public boolean putSmoothString(String str, String str2) {
        return this.c.putSmoothString(str, str2);
    }

    public boolean putBoolean(String str, boolean z) {
        return this.c.putBoolean(str, z);
    }

    public boolean putSmoothBoolean(String str, boolean z) {
        return this.c.putSmoothBoolean(str, z);
    }

    public boolean putBytes(String str, byte[] bArr) {
        return this.c.putBytes(str, bArr);
    }

    public boolean putSmoothBytes(String str, byte[] bArr) {
        return this.c.putSmoothBytes(str, bArr);
    }

    public boolean putInt(String str, int i) {
        return this.c.putInt(str, i);
    }

    public boolean putSmoothInt(String str, int i) {
        return this.c.putSmoothInt(str, i);
    }

    public boolean putShort(String str, short s) {
        return this.c.putShort(str, s);
    }

    public boolean putSmoothShort(String str, short s) {
        return this.c.putSmoothShort(str, s);
    }

    public boolean putLong(String str, long j) {
        return this.c.putLong(str, j);
    }

    public boolean putSmoothLong(String str, long j) {
        return this.c.putSmoothLong(str, j);
    }

    public boolean putFloat(String str, float f) {
        return this.c.putFloat(str, f);
    }

    public boolean putSmoothFloat(String str, float f) {
        return this.c.putSmoothFloat(str, f);
    }

    public boolean putDouble(String str, double d) {
        return this.c.putDouble(str, d);
    }

    public boolean putSmoothDouble(String str, double d) {
        return this.c.putSmoothDouble(str, d);
    }

    public boolean putSerializable(String str, Serializable serializable) {
        return this.c.putSerializable(str, serializable);
    }

    public boolean putSmoothSerializable(String str, Serializable serializable) {
        return this.c.putSmoothSerializable(str, serializable);
    }

    public boolean getBoolean(String str, boolean z) {
        return this.c.getBoolean(str, z);
    }

    public <T extends Serializable> T getSerializable(String str, Class<T> cls, T t) {
        return this.c.getSerializable(str, cls, t);
    }

    public <T extends Serializable> T getSerializable(String str, T t) {
        return this.c.getSerializable(str, null, t);
    }

    public <T extends Serializable> T getSerializable(String str) {
        return this.c.getSerializable(str, null, null);
    }

    public byte[] getBytes(String str, byte[] bArr) {
        return this.c.getBytes(str, bArr);
    }

    public int getInt(String str, int i) {
        return this.c.getInt(str, i);
    }

    public short getShort(String str, short s) {
        return this.c.getShort(str, s);
    }

    public long getLong(String str, long j) {
        return this.c.getLong(str, j);
    }

    public float getFloat(String str, float f) {
        return this.c.getFloat(str, f);
    }

    public double getDouble(String str, double d) {
        return this.c.getDouble(str, d);
    }

    public String getString(String str, String str2) {
        return this.c.getString(str, str2);
    }

    public boolean remove(String str) {
        return this.c.remove(str);
    }

    public boolean contains(String str) {
        return this.c.contains(str);
    }

    public Map<String, Object> getAll() {
        return this.c.getAll();
    }

    public List<String> getKeys() {
        return this.c.getKeys();
    }

    public static byte[] openAsset(Context context, String str, boolean z) {
        ByteArrayOutputStream byteArrayOutputStream;
        byte[] bArr;
        Throwable th;
        InputStream inputStream = null;
        InputStream open;
        try {
            byteArrayOutputStream = new ByteArrayOutputStream();
            try {
                open = context.getAssets().open(str);
            } catch (Throwable th2) {
                th = th2;
                if (inputStream != null) {
                    try {
                        inputStream.close();
                    } catch (IOException e) {
                    }
                }
                if (byteArrayOutputStream != null) {
                    try {
                        byteArrayOutputStream.close();
                    } catch (IOException e2) {
                    }
                }
                throw th;
            }
            try {
                byte[] bArr2 = new byte[1024];
                while (true) {
                    int read = open.read(bArr2);
                    if (read == -1) {
                        break;
                    }
                    byteArrayOutputStream.write(bArr2, 0, read);
                }
                byteArrayOutputStream.flush();
                byte[] toByteArray = byteArrayOutputStream.toByteArray();
                if (open != null) {
                    try {
                        open.close();
                    } catch (IOException e3) {
                    }
                }
                if (byteArrayOutputStream != null) {
                    try {
                        byteArrayOutputStream.close();
                        bArr = toByteArray;
                    } catch (IOException e4) {
                        bArr = toByteArray;
                    }
                } else {
                    bArr = toByteArray;
                }
            } catch (Throwable th3) {
                Throwable th4 = th3;
                inputStream = open;
                th = th4;
                if (inputStream != null) {
                    inputStream.close();
                }
                if (byteArrayOutputStream != null) {
                    byteArrayOutputStream.close();
                }
                throw th;
            }
        } catch (Throwable th5) {
            th = th5;
            byteArrayOutputStream = inputStream;
            if (inputStream != null) {
                inputStream.close();
            }
            if (byteArrayOutputStream != null) {
                byteArrayOutputStream.close();
            }
            throw th;
        }
        if (bArr == null && z) {
            return EggRoll.da(bArr);
        }
    }

    public static byte[] openAsset(Context context, String str) {
        return openAsset(context, str, true);
    }

    public String getOwner() {
        return this.b;
    }

    public boolean clean() {
        return this.c.cleanAllStorage();
    }
}
