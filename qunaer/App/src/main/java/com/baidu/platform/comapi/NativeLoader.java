package com.baidu.platform.comapi;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.os.Build.VERSION;
import android.util.Log;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class NativeLoader {
    private static Context a;
    private static final Set<String> b = new HashSet();
    private static final Set<String> c = new HashSet();
    private static NativeLoader d;
    private static a e = a.ARMEABI;

    enum a {
        ARMEABI("armeabi"),
        ARMV7("armeabi-v7a"),
        ARM64("arm64-v8a"),
        X86("x86"),
        X86_64("x86_64");
        
        private String f;

        private a(String str) {
            this.f = str;
        }

        public String a() {
            return this.f;
        }
    }

    private NativeLoader() {
    }

    @TargetApi(21)
    private static a a() {
        String str = VERSION.SDK_INT < 21 ? Build.CPU_ABI : Build.SUPPORTED_ABIS[0];
        if (str == null) {
            return a.ARMEABI;
        }
        if (str.contains("arm") && str.contains("v7")) {
            e = a.ARMV7;
        }
        if (str.contains("arm") && str.contains("64")) {
            e = a.ARM64;
        }
        if (str.contains("x86")) {
            if (str.contains("64")) {
                e = a.X86_64;
            } else {
                e = a.X86;
            }
        }
        return e;
    }

    private String a(a aVar) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("lib/").append(aVar.a()).append("/");
        return stringBuilder.toString();
    }

    private void a(Throwable th) {
        Log.e(NativeLoader.class.getSimpleName(), "loadException", th);
        for (String str : c) {
            Log.e(NativeLoader.class.getSimpleName(), str + " Failed to load.");
        }
    }

    private boolean a(String str, String str2) {
        return !copyNativeLibrary(str2, a.ARMV7) ? b(str, str2) : f(str2, str);
    }

    private boolean b(String str, String str2) {
        if (copyNativeLibrary(str2, a.ARMEABI)) {
            return f(str2, str);
        }
        Log.e(NativeLoader.class.getSimpleName(), "found lib" + str + ".so error");
        return false;
    }

    private boolean c(String str, String str2) {
        return !copyNativeLibrary(str2, a.ARM64) ? a(str, str2) : f(str2, str);
    }

    private boolean d(String str, String str2) {
        return !copyNativeLibrary(str2, a.X86) ? a(str, str2) : f(str2, str);
    }

    private boolean e(String str, String str2) {
        return !copyNativeLibrary(str2, a.X86_64) ? d(str, str2) : f(str2, str);
    }

    private boolean f(String str, String str2) {
        try {
            System.load(new File(getCustomizeNativePath(), str).getAbsolutePath());
            synchronized (b) {
                b.add(str2);
            }
            return true;
        } catch (Throwable th) {
            synchronized (c) {
                c.add(str2);
                a(th);
                return false;
            }
        }
    }

    public static synchronized NativeLoader getInstance() {
        NativeLoader nativeLoader;
        synchronized (NativeLoader.class) {
            if (d == null) {
                d = new NativeLoader();
                e = a();
            }
            nativeLoader = d;
        }
        return nativeLoader;
    }

    public static void setContext(Context context) {
        a = context;
    }

    protected boolean copyNativeLibrary(String str, a aVar) {
        ZipFile zipFile;
        Throwable e;
        String str2 = a(aVar) + str;
        try {
            zipFile = new ZipFile(getCodePath());
            try {
                File file = new File(getCustomizeNativePath(), str);
                ZipEntry entry = zipFile.getEntry(str2);
                if (entry != null) {
                    copyStream(zipFile.getInputStream(entry), new FileOutputStream(file));
                    if (zipFile != null) {
                        try {
                            zipFile.close();
                        } catch (IOException e2) {
                            return false;
                        }
                    }
                    return true;
                } else if (zipFile == null) {
                    return false;
                } else {
                    try {
                        zipFile.close();
                        return false;
                    } catch (IOException e3) {
                        return false;
                    }
                }
            } catch (Exception e4) {
                e = e4;
                try {
                    Log.e(NativeLoader.class.getSimpleName(), "copyError", e);
                    if (zipFile != null) {
                        return false;
                    }
                    try {
                        zipFile.close();
                        return false;
                    } catch (IOException e5) {
                        return false;
                    }
                } catch (Throwable th) {
                    e = th;
                    if (zipFile != null) {
                        try {
                            zipFile.close();
                        } catch (IOException e6) {
                            return false;
                        }
                    }
                    throw e;
                }
            }
        } catch (Exception e7) {
            e = e7;
            zipFile = null;
            Log.e(NativeLoader.class.getSimpleName(), "copyError", e);
            if (zipFile != null) {
                return false;
            }
            zipFile.close();
            return false;
        } catch (Throwable th2) {
            e = th2;
            zipFile = null;
            if (zipFile != null) {
                zipFile.close();
            }
            throw e;
        }
    }

    protected final void copyStream(InputStream inputStream, FileOutputStream fileOutputStream) {
        byte[] bArr = new byte[4096];
        while (true) {
            try {
                int read = inputStream.read(bArr);
                if (read == -1) {
                    break;
                }
                fileOutputStream.write(bArr, 0, read);
            } finally {
                try {
                    inputStream.close();
                    try {
                        fileOutputStream.close();
                    } catch (IOException e) {
                        return;
                    }
                } catch (IOException e2) {
                    return;
                }
            }
        }
        fileOutputStream.flush();
        try {
            fileOutputStream.close();
        } catch (IOException e3) {
        }
    }

    @TargetApi(8)
    protected String getCodePath() {
        return 8 <= VERSION.SDK_INT ? a.getPackageCodePath() : "";
    }

    protected String getCustomizeNativePath() {
        File file = new File(a.getFilesDir(), "libs");
        file.mkdirs();
        return file.getAbsolutePath();
    }

    protected boolean loadCustomizeNativeLibrary(String str) {
        String mapLibraryName = System.mapLibraryName(str);
        switch (e) {
            case ARM64:
                return c(str, mapLibraryName);
            case ARMV7:
                return a(str, mapLibraryName);
            case ARMEABI:
                return b(str, mapLibraryName);
            case X86_64:
                return e(str, mapLibraryName);
            case X86:
                return d(str, mapLibraryName);
            default:
                return false;
        }
    }

    public synchronized boolean loadLibrary(String str) {
        boolean z = true;
        synchronized (this) {
            try {
                synchronized (b) {
                    if (b.contains(str)) {
                    } else {
                        System.loadLibrary(str);
                        synchronized (b) {
                            b.add(str);
                        }
                    }
                }
            } catch (Throwable th) {
                z = loadCustomizeNativeLibrary(str);
            }
        }
        return z;
    }
}
