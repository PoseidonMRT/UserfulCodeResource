package com.mqunar.storage;

import android.content.Context;
import android.util.Base64;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.json.JSONException;
import org.json.JSONObject;

public class FileStorage implements IStorage {
    private File a;
    private JSONObject b;

    private FileStorage(Context context, File file) {
        if (file == null) {
            RuntimeException runtimeException = new RuntimeException("file is null!");
        }
        this.a = file;
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
            this.b = new JSONObject();
        } else if (file.exists() && file.isDirectory()) {
            throw new RuntimeException("无法创建文件!已存在同名的文件夹!");
        } else {
            this.b = a();
        }
    }

    public static IStorage newInstance(Context context, File file) {
        return new FileStorage(context, file);
    }

    private JSONObject a() {
        Throwable th;
        JSONObject jSONObject;
        FileInputStream fileInputStream = null;
        if (this.a.exists()) {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            FileInputStream fileInputStream2;
            try {
                byte[] bArr = new byte[4096];
                fileInputStream2 = new FileInputStream(this.a);
                while (true) {
                    try {
                        int read = fileInputStream2.read(bArr);
                        if (read == -1) {
                            break;
                        }
                        byteArrayOutputStream.write(bArr, 0, read);
                    } catch (Exception e) {
                    } catch (Throwable th2) {
                        Throwable th3 = th2;
                        fileInputStream = fileInputStream2;
                        th = th3;
                    }
                }
                JSONObject jSONObject2 = new JSONObject(new String(byteArrayOutputStream.toByteArray(), "UTF-8"));
                if (fileInputStream2 != null) {
                    try {
                        fileInputStream2.close();
                        jSONObject = jSONObject2;
                    } catch (IOException e2) {
                        jSONObject = jSONObject2;
                    }
                } else {
                    jSONObject = jSONObject2;
                }
            } catch (Exception e3) {
                fileInputStream2 = null;
                if (fileInputStream2 != null) {
                    try {
                        fileInputStream2.close();
                        jSONObject = null;
                    } catch (IOException e4) {
                        jSONObject = null;
                    }
                    if (jSONObject != null) {
                        return new JSONObject();
                    }
                    return jSONObject;
                }
                jSONObject = null;
                if (jSONObject != null) {
                    return jSONObject;
                }
                return new JSONObject();
            } catch (Throwable th4) {
                th = th4;
                if (fileInputStream != null) {
                    try {
                        fileInputStream.close();
                    } catch (IOException e5) {
                    }
                }
                throw th;
            }
            if (jSONObject != null) {
                return new JSONObject();
            }
            return jSONObject;
        }
        jSONObject = null;
        if (jSONObject != null) {
            return jSONObject;
        }
        return new JSONObject();
    }

    private void a(JSONObject jSONObject) {
        FileOutputStream fileOutputStream;
        Throwable th;
        FileOutputStream fileOutputStream2 = null;
        try {
            fileOutputStream = new FileOutputStream(this.a);
            try {
                fileOutputStream.write(jSONObject.toString().getBytes("UTF-8"));
                if (fileOutputStream != null) {
                    try {
                        fileOutputStream.close();
                    } catch (IOException e) {
                    }
                }
            } catch (Exception e2) {
                if (fileOutputStream != null) {
                    try {
                        fileOutputStream.close();
                    } catch (IOException e3) {
                    }
                }
            } catch (Throwable th2) {
                Throwable th3 = th2;
                fileOutputStream2 = fileOutputStream;
                th = th3;
                if (fileOutputStream2 != null) {
                    try {
                        fileOutputStream2.close();
                    } catch (IOException e4) {
                    }
                }
                throw th;
            }
        } catch (Exception e5) {
            fileOutputStream = null;
            if (fileOutputStream != null) {
                fileOutputStream.close();
            }
        } catch (Throwable th4) {
            th = th4;
            if (fileOutputStream2 != null) {
                fileOutputStream2.close();
            }
            throw th;
        }
    }

    private void a(String str, String str2) {
        synchronized (this.a) {
            this.b.put(str, str2);
            a(this.b);
        }
    }

    private String a(String str) {
        String optString;
        synchronized (this.a) {
            optString = this.b.optString(str, null);
        }
        return optString;
    }

    public static byte[] i2b(int i) {
        return new byte[]{(byte) ((i >> 24) & 255), (byte) ((i >> 16) & 255), (byte) ((i >> 8) & 255), (byte) (i & 255)};
    }

    private static byte[] a(long j) {
        return new byte[]{(byte) ((int) ((j >> 56) & 255)), (byte) ((int) ((j >> 48) & 255)), (byte) ((int) ((j >> 40) & 255)), (byte) ((int) ((j >> 32) & 255)), (byte) ((int) ((j >> 24) & 255)), (byte) ((int) ((j >> 16) & 255)), (byte) ((int) ((j >> 8) & 255)), (byte) ((int) (j & 255))};
    }

    public static int b2i(byte[] bArr) {
        int i = 0;
        int i2 = 0;
        while (i < 4) {
            i2 += (bArr[i] & 255) << ((3 - i) * 8);
            i++;
        }
        return i2;
    }

    private static long a(byte[] bArr) {
        long j = 0;
        for (int i = 0; i < 8; i++) {
            j += (((long) bArr[i]) & 255) << ((7 - i) * 8);
        }
        return j;
    }

    private boolean a(int i, String str, byte[] bArr) {
        if (bArr == null || bArr.length == 0) {
            try {
                a(str, "");
                return true;
            } catch (JSONException e) {
                return false;
            }
        }
        try {
            Object obj = new byte[(bArr.length + 1)];
            obj[0] = (byte) i;
            System.arraycopy(bArr, 0, obj, 1, bArr.length);
            a(str, Base64.encodeToString(EggRoll.ea(obj), 2));
            return true;
        } catch (Throwable th) {
            return false;
        }
    }

    private byte[] b(String str) {
        if (str != null) {
            return EggRoll.da(Base64.decode(str, 2));
        }
        return null;
    }

    private byte[] c(String str) {
        return b(a(str));
    }

    private byte[] a(int i, String str) {
        Object c = c(str);
        if (c == null || c.length <= 0) {
            return c;
        }
        if (c[0] != ((byte) i)) {
            throw new RuntimeException("类型不匹配");
        }
        Object obj = new byte[(c.length - 1)];
        System.arraycopy(c, 1, obj, 0, obj.length);
        return obj;
    }

    public boolean putBytes(String str, byte[] bArr) {
        return a(0, str, bArr);
    }

    public boolean putSmoothBytes(String str, byte[] bArr) {
        return putBytes(str, bArr);
    }

    public byte[] getBytes(String str, byte[] bArr) {
        try {
            byte[] a = a(0, str);
            if (a == null || a.length == 0) {
                return bArr;
            }
            return a;
        } catch (Throwable th) {
            return bArr;
        }
    }

    public boolean putInt(String str, int i) {
        return a(2, str, i2b(i));
    }

    public boolean putSmoothInt(String str, int i) {
        return putInt(str, i);
    }

    public boolean putShort(String str, short s) {
        return a(1, str, new byte[]{(byte) ((s >> 8) & 255), (byte) (s & 255)});
    }

    public boolean putSmoothShort(String str, short s) {
        return putShort(str, s);
    }

    public boolean putLong(String str, long j) {
        return a(3, str, a(j));
    }

    public boolean putSmoothLong(String str, long j) {
        return putLong(str, j);
    }

    public boolean putFloat(String str, float f) {
        return a(4, str, i2b(Float.floatToIntBits(f)));
    }

    public boolean putSmoothFloat(String str, float f) {
        return putFloat(str, f);
    }

    public boolean putDouble(String str, double d) {
        return a(5, str, a(Double.doubleToLongBits(d)));
    }

    public boolean putSmoothDouble(String str, double d) {
        return putDouble(str, d);
    }

    public boolean putString(String str, String str2) {
        try {
            return a(7, str, str2 == null ? null : str2.getBytes("UTF-8"));
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public boolean putSmoothString(String str, String str2) {
        return putString(str, str2);
    }

    public boolean putBoolean(String str, boolean z) {
        int i;
        if (z) {
            i = 1;
        } else {
            i = 0;
        }
        return a(6, str, new byte[]{(byte) i});
    }

    public boolean putSmoothBoolean(String str, boolean z) {
        return putBoolean(str, z);
    }

    public boolean putSerializable(String str, Serializable serializable) {
        ByteArrayOutputStream byteArrayOutputStream;
        ObjectOutputStream objectOutputStream;
        Throwable th;
        byte[] bArr = null;
        if (serializable != null) {
            try {
                byteArrayOutputStream = new ByteArrayOutputStream();
                try {
                    objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
                    try {
                        objectOutputStream.writeObject(serializable);
                        objectOutputStream.flush();
                        bArr = byteArrayOutputStream.toByteArray();
                        if (objectOutputStream != null) {
                            try {
                                objectOutputStream.close();
                            } catch (IOException e) {
                            }
                        }
                        if (byteArrayOutputStream != null) {
                            try {
                                byteArrayOutputStream.close();
                            } catch (IOException e2) {
                            }
                        }
                    } catch (IOException e3) {
                        if (objectOutputStream != null) {
                            try {
                                objectOutputStream.close();
                            } catch (IOException e4) {
                            }
                        }
                        if (byteArrayOutputStream != null) {
                            return false;
                        }
                        try {
                            byteArrayOutputStream.close();
                            return false;
                        } catch (IOException e5) {
                            return false;
                        }
                    } catch (Throwable th2) {
                        th = th2;
                        if (objectOutputStream != null) {
                            try {
                                objectOutputStream.close();
                            } catch (IOException e6) {
                            }
                        }
                        if (byteArrayOutputStream != null) {
                            try {
                                byteArrayOutputStream.close();
                            } catch (IOException e7) {
                            }
                        }
                        throw th;
                    }
                } catch (IOException e8) {
                    objectOutputStream = null;
                    if (objectOutputStream != null) {
                        objectOutputStream.close();
                    }
                    if (byteArrayOutputStream != null) {
                        return false;
                    }
                    byteArrayOutputStream.close();
                    return false;
                } catch (Throwable th3) {
                    Throwable th4 = th3;
                    objectOutputStream = null;
                    th = th4;
                    if (objectOutputStream != null) {
                        objectOutputStream.close();
                    }
                    if (byteArrayOutputStream != null) {
                        byteArrayOutputStream.close();
                    }
                    throw th;
                }
            } catch (IOException e9) {
                objectOutputStream = null;
                byteArrayOutputStream = null;
                if (objectOutputStream != null) {
                    objectOutputStream.close();
                }
                if (byteArrayOutputStream != null) {
                    return false;
                }
                byteArrayOutputStream.close();
                return false;
            } catch (Throwable th32) {
                byteArrayOutputStream = null;
                th = th32;
                objectOutputStream = null;
                if (objectOutputStream != null) {
                    objectOutputStream.close();
                }
                if (byteArrayOutputStream != null) {
                    byteArrayOutputStream.close();
                }
                throw th;
            }
        }
        return a(8, str, bArr);
    }

    public boolean putSmoothSerializable(String str, Serializable serializable) {
        return putSerializable(str, serializable);
    }

    public short getShort(String str, short s) {
        int i = 0;
        try {
            byte[] a = a(1, str);
            if (a != null) {
                s = (short) 0;
                while (i < 2) {
                    s = (short) (((a[i] & 255) << ((1 - i) * 8)) + s);
                    i++;
                }
            }
        } catch (Throwable th) {
        }
        return s;
    }

    public int getInt(String str, int i) {
        try {
            byte[] a = a(2, str);
            if (a != null) {
                i = b2i(a);
            }
        } catch (Throwable th) {
        }
        return i;
    }

    public long getLong(String str, long j) {
        try {
            byte[] a = a(3, str);
            if (a != null) {
                j = a(a);
            }
        } catch (Throwable th) {
        }
        return j;
    }

    public float getFloat(String str, float f) {
        try {
            byte[] a = a(4, str);
            if (a != null) {
                f = Float.intBitsToFloat(b2i(a));
            }
        } catch (Throwable th) {
        }
        return f;
    }

    public double getDouble(String str, double d) {
        try {
            byte[] a = a(5, str);
            if (a != null) {
                d = Double.longBitsToDouble(a(a));
            }
        } catch (Throwable th) {
        }
        return d;
    }

    public String getString(String str, String str2) {
        try {
            byte[] a = a(7, str);
            if (a != null) {
                return new String(a, "UTF-8");
            }
            return str2;
        } catch (Throwable th) {
            return str2;
        }
    }

    public boolean getBoolean(String str, boolean z) {
        try {
            byte[] a = a(6, str);
            if (a == null) {
                return z;
            }
            if (a[0] == (byte) 1) {
                return true;
            }
            return false;
        } catch (Throwable th) {
            return z;
        }
    }

    public <T extends Serializable> T getSerializable(String str, Class<T> cls, T t) {
        ObjectInputStream objectInputStream;
        Throwable th;
        ByteArrayInputStream byteArrayInputStream = null;
        ObjectInputStream objectInputStream2;
        try {
            byte[] a = a(8, str);
            if (a != null) {
                objectInputStream2 = new ObjectInputStream(new ByteArrayInputStream(a));
                try {
                    t = (Serializable) objectInputStream2.readObject();
                } catch (Throwable th2) {
                    th = th2;
                    if (objectInputStream2 != null) {
                        try {
                            objectInputStream2.close();
                        } catch (IOException e) {
                        }
                    }
                    if (null != null) {
                        try {
                            byteArrayInputStream.close();
                        } catch (IOException e2) {
                        }
                    }
                    throw th;
                }
            }
            objectInputStream2 = null;
            if (objectInputStream2 != null) {
                try {
                    objectInputStream2.close();
                } catch (IOException e3) {
                }
            }
            if (null != null) {
                try {
                    byteArrayInputStream.close();
                } catch (IOException e4) {
                }
            }
        } catch (Throwable th3) {
            th = th3;
            objectInputStream2 = null;
            if (objectInputStream2 != null) {
                objectInputStream2.close();
            }
            if (null != null) {
                byteArrayInputStream.close();
            }
            throw th;
        }
        return t;
    }

    public boolean remove(String str) {
        try {
            boolean z;
            synchronized (this.a) {
                Object remove = this.b.remove(str);
                if (remove != null) {
                    a(this.b);
                }
                if (remove != null) {
                    z = true;
                } else {
                    z = false;
                }
            }
            return z;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean contains(String str) {
        boolean has;
        synchronized (this.a) {
            try {
                has = this.b.has(str);
            } catch (Exception e) {
                return false;
            }
        }
        return has;
    }

    public List<String> getKeys() {
        List arrayList = new ArrayList();
        synchronized (this.a) {
            Iterator keys = this.b.keys();
            while (keys.hasNext()) {
                arrayList.add((String) keys.next());
            }
        }
        return arrayList;
    }

    public Map<String, Object> getAll() {
        Map<String, Object> hashMap;
        Throwable th;
        ObjectInputStream objectInputStream;
        synchronized (this.a) {
            hashMap = new HashMap();
            Iterator keys = this.b.keys();
            while (keys.hasNext()) {
                ByteArrayInputStream byteArrayInputStream;
                String str = (String) keys.next();
                Object b = b(String.valueOf(this.b.get(str)));
                if (b != null && b.length > 0) {
                    byte b2 = b[0];
                    byte[] bArr = new byte[(b.length - 1)];
                    System.arraycopy(b, 1, bArr, 0, bArr.length);
                    switch (b2) {
                        case (byte) 0:
                            b = bArr;
                            break;
                        case (byte) 1:
                            int i = 0;
                            short s = (short) 0;
                            while (i < 2) {
                                try {
                                    s = (short) (s + ((bArr[i] & 255) << ((1 - i) * 8)));
                                    i++;
                                } catch (Throwable th2) {
                                    th2.printStackTrace();
                                    break;
                                }
                            }
                            b = Short.valueOf(s);
                            break;
                        case (byte) 2:
                            b = Integer.valueOf(b2i(bArr));
                            break;
                        case (byte) 3:
                            b = Long.valueOf(a(bArr));
                            break;
                        case (byte) 4:
                            b = Float.valueOf(Float.intBitsToFloat(b2i(bArr)));
                            break;
                        case (byte) 5:
                            b = Double.valueOf(Double.longBitsToDouble(a(bArr)));
                            break;
                        case (byte) 6:
                            b = Boolean.valueOf(bArr[0] == (byte) 1);
                            break;
                        case (byte) 7:
                            try {
                                b = new String(bArr, "UTF-8");
                                break;
                            } catch (UnsupportedEncodingException e) {
                                b = null;
                                break;
                            }
                        case (byte) 8:
                            byteArrayInputStream = null;
                            try {
                                objectInputStream = new ObjectInputStream(new ByteArrayInputStream(bArr));
                            } catch (Throwable th3) {
                                th2 = th3;
                                objectInputStream = null;
                                break;
                            }
                            try {
                                Object readObject = objectInputStream.readObject();
                                if (objectInputStream != null) {
                                    try {
                                        objectInputStream.close();
                                    } catch (IOException e2) {
                                    }
                                }
                                if (null != null) {
                                    try {
                                        byteArrayInputStream.close();
                                        b = readObject;
                                    } catch (IOException e3) {
                                        b = readObject;
                                    }
                                } else {
                                    b = readObject;
                                }
                            } catch (Throwable th4) {
                                th2 = th4;
                                break;
                            }
                    }
                }
                b = null;
                hashMap.put(str, b);
            }
        }
        return hashMap;
        if (objectInputStream != null) {
            try {
                objectInputStream.close();
            } catch (IOException e4) {
            }
        }
        if (null != null) {
            try {
                byteArrayInputStream.close();
            } catch (IOException e5) {
            }
        }
        throw th2;
        if (null != null) {
            byteArrayInputStream.close();
        }
        throw th2;
        throw th2;
    }

    public boolean cleanAllStorage() {
        synchronized (this.a) {
            a(this.b);
        }
        return true;
    }
}
