package com.megvii.livenessdeteciton.obf;

import android.content.Context;
import android.os.Build;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileFilter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Locale;

public class c {
    private static Context a;
    private static c b;

    final class a implements FileFilter {
        private String a = "";

        public a(c cVar, String str) {
            this.a = str;
        }

        public final boolean accept(File file) {
            return file.getName().startsWith(this.a);
        }
    }

    public static synchronized c a(Context context) {
        c cVar;
        synchronized (c.class) {
            if (b == null) {
                b = new c(context);
            }
            cVar = b;
        }
        return cVar;
    }

    private c(Context context) {
        a = context;
    }

    public final boolean a(String str, String str2) {
        boolean z;
        try {
            System.loadLibrary("livenessdetection_v2.4.2");
            z = true;
        } catch (Exception e) {
            z = false;
        }
        if (z) {
            return true;
        }
        String str3 = str + "_bak";
        File filesDir = a.getFilesDir();
        if (a(filesDir.toString(), str3, str, str2)) {
            File file = new File(filesDir.toString() + File.separator + (str3 + File.separator + ("lib" + str + "_" + str2 + ".so")));
            d.a("copy lib to " + file.toString());
            if (file.exists()) {
                try {
                    System.load(file.toString());
                    return true;
                } catch (UnsatisfiedLinkError e2) {
                    d.a("SoProtect", e2.toString());
                    return z;
                }
            }
            String str4 = "SoProtect";
            d.b(str4, String.format(Locale.ENGLISH, "error can't find %1$s lib in plugins_lib", new Object[]{str}));
            return z;
        }
        str4 = "SoProtect";
        d.a(str4, String.format(Locale.ENGLISH, "error copy %1$s lib fail", new Object[]{str}));
        return z;
    }

    private void a(File file, String str) {
        try {
            for (File a : file.listFiles(new a(this, str))) {
                a(a);
            }
        } catch (Exception e) {
            d.a("SoProtect", e.toString());
        }
    }

    private void a(File file) {
        if (!file.exists()) {
            d.b("SoProtect", "所删除的文件不存在！\n");
        } else if (file.isFile()) {
            file.delete();
        } else if (file.isDirectory()) {
            for (File a : file.listFiles()) {
                a(a);
            }
            file.delete();
        }
    }

    private boolean a(String str, String str2, String str3, String str4) {
        String str5;
        String str6 = Build.CPU_ABI;
        String str7 = "lib" + str3 + "_" + str4 + ".so";
        if ("x86".equals(str6)) {
            str5 = "lib/x86/" + str7;
        } else if ("armeabi-v7a".equals(str6)) {
            str5 = "lib/armeabi-v7a/" + str7;
        } else {
            d.a("SoProtect", "apse is not support for this mode");
            return false;
        }
        try {
            File file = new File(str + File.separator + str2);
            File file2 = new File(file.toString() + File.separator + str7);
            if (file2.exists()) {
                d.b("SoProtect", "file " + file2.toString() + " is exist");
                return true;
            }
            a(file, "lib" + str3);
            file.mkdirs();
            boolean a = a(str, str5, str7, file2);
            if (a || !str6.equals("armeabi-v7a")) {
                return a;
            }
            d.b("SoProtect", String.format("%s arch copy failed, try to copy %s arch", new Object[]{"armeabi-v7a", "armeabi"}));
            return a(str, "lib/armeabi/" + str7, str7, file2);
        } catch (Exception e) {
            d.a("SoProtect", e.toString());
            return false;
        }
    }

    private boolean a(String str, String str2, String str3, File file) {
        boolean z = false;
        InputStream resourceAsStream = c.class.getClassLoader().getResourceAsStream(str2);
        if (resourceAsStream != null) {
            if (str == null) {
                d.a("SoProtect", "apse file cann't be null...");
            }
            z = a(resourceAsStream, file);
            try {
                resourceAsStream.close();
            } catch (IOException e) {
                d.a("SoProtect", e.toString());
            }
        } else {
            d.b("SoProtect", "error: can't find " + str3 + " in apk");
        }
        return z;
    }

    private static boolean a(InputStream inputStream, File file) {
        BufferedInputStream bufferedInputStream;
        FileOutputStream fileOutputStream;
        IOException e;
        FileNotFoundException e2;
        BufferedInputStream bufferedInputStream2;
        BufferedOutputStream bufferedOutputStream;
        FileOutputStream fileOutputStream2;
        Throwable th;
        BufferedOutputStream bufferedOutputStream2 = null;
        try {
            if (file.exists()) {
                file.delete();
            }
            file.createNewFile();
            bufferedInputStream = new BufferedInputStream(inputStream);
            try {
                fileOutputStream = new FileOutputStream(file);
                try {
                    BufferedOutputStream bufferedOutputStream3 = new BufferedOutputStream(fileOutputStream);
                    try {
                        byte[] bArr = new byte[1024];
                        while (true) {
                            int read = bufferedInputStream.read(bArr);
                            if (read != -1) {
                                bufferedOutputStream3.write(bArr, 0, read);
                            } else {
                                bufferedOutputStream3.flush();
                                fileOutputStream.flush();
                                try {
                                    fileOutputStream.close();
                                    bufferedInputStream.close();
                                    bufferedOutputStream3.close();
                                    return true;
                                } catch (IOException e3) {
                                    d.a("SoProtect", e3.toString());
                                    return false;
                                }
                            }
                        }
                    } catch (FileNotFoundException e4) {
                        e2 = e4;
                        bufferedInputStream2 = bufferedInputStream;
                        bufferedOutputStream = bufferedOutputStream3;
                        fileOutputStream2 = fileOutputStream;
                        try {
                            d.a("SoProtect", e2.toString());
                            if (fileOutputStream2 != null) {
                                try {
                                    fileOutputStream2.close();
                                } catch (IOException e32) {
                                    d.a("SoProtect", e32.toString());
                                    return false;
                                }
                            }
                            if (bufferedInputStream2 != null) {
                                bufferedInputStream2.close();
                            }
                            if (bufferedOutputStream != null) {
                                bufferedOutputStream.close();
                            }
                            return false;
                        } catch (Throwable th2) {
                            th = th2;
                            fileOutputStream = fileOutputStream2;
                            BufferedOutputStream bufferedOutputStream4 = bufferedOutputStream;
                            bufferedInputStream = bufferedInputStream2;
                            bufferedOutputStream2 = bufferedOutputStream4;
                            if (fileOutputStream != null) {
                                try {
                                    fileOutputStream.close();
                                } catch (IOException e5) {
                                    d.a("SoProtect", e5.toString());
                                    throw th;
                                }
                            }
                            if (bufferedInputStream != null) {
                                bufferedInputStream.close();
                            }
                            if (bufferedOutputStream2 != null) {
                                bufferedOutputStream2.close();
                            }
                            throw th;
                        }
                    } catch (IOException e6) {
                        e32 = e6;
                        bufferedOutputStream2 = bufferedOutputStream3;
                        try {
                            d.a("SoProtect", e32.toString());
                            if (fileOutputStream != null) {
                                try {
                                    fileOutputStream.close();
                                } catch (IOException e322) {
                                    d.a("SoProtect", e322.toString());
                                    return false;
                                }
                            }
                            if (bufferedInputStream != null) {
                                bufferedInputStream.close();
                            }
                            if (bufferedOutputStream2 != null) {
                                bufferedOutputStream2.close();
                            }
                            return false;
                        } catch (Throwable th3) {
                            th = th3;
                            if (fileOutputStream != null) {
                                fileOutputStream.close();
                            }
                            if (bufferedInputStream != null) {
                                bufferedInputStream.close();
                            }
                            if (bufferedOutputStream2 != null) {
                                bufferedOutputStream2.close();
                            }
                            throw th;
                        }
                    } catch (Throwable th4) {
                        th = th4;
                        bufferedOutputStream2 = bufferedOutputStream3;
                        if (fileOutputStream != null) {
                            fileOutputStream.close();
                        }
                        if (bufferedInputStream != null) {
                            bufferedInputStream.close();
                        }
                        if (bufferedOutputStream2 != null) {
                            bufferedOutputStream2.close();
                        }
                        throw th;
                    }
                } catch (FileNotFoundException e7) {
                    e2 = e7;
                    fileOutputStream2 = fileOutputStream;
                    bufferedInputStream2 = bufferedInputStream;
                    bufferedOutputStream = null;
                    d.a("SoProtect", e2.toString());
                    if (fileOutputStream2 != null) {
                        fileOutputStream2.close();
                    }
                    if (bufferedInputStream2 != null) {
                        bufferedInputStream2.close();
                    }
                    if (bufferedOutputStream != null) {
                        bufferedOutputStream.close();
                    }
                    return false;
                } catch (IOException e8) {
                    e322 = e8;
                    d.a("SoProtect", e322.toString());
                    if (fileOutputStream != null) {
                        fileOutputStream.close();
                    }
                    if (bufferedInputStream != null) {
                        bufferedInputStream.close();
                    }
                    if (bufferedOutputStream2 != null) {
                        bufferedOutputStream2.close();
                    }
                    return false;
                }
            } catch (FileNotFoundException e9) {
                e2 = e9;
                fileOutputStream2 = null;
                bufferedInputStream2 = bufferedInputStream;
                bufferedOutputStream = null;
                d.a("SoProtect", e2.toString());
                if (fileOutputStream2 != null) {
                    fileOutputStream2.close();
                }
                if (bufferedInputStream2 != null) {
                    bufferedInputStream2.close();
                }
                if (bufferedOutputStream != null) {
                    bufferedOutputStream.close();
                }
                return false;
            } catch (IOException e10) {
                e322 = e10;
                fileOutputStream = null;
                d.a("SoProtect", e322.toString());
                if (fileOutputStream != null) {
                    fileOutputStream.close();
                }
                if (bufferedInputStream != null) {
                    bufferedInputStream.close();
                }
                if (bufferedOutputStream2 != null) {
                    bufferedOutputStream2.close();
                }
                return false;
            } catch (Throwable th5) {
                th = th5;
                fileOutputStream = null;
                if (fileOutputStream != null) {
                    fileOutputStream.close();
                }
                if (bufferedInputStream != null) {
                    bufferedInputStream.close();
                }
                if (bufferedOutputStream2 != null) {
                    bufferedOutputStream2.close();
                }
                throw th;
            }
        } catch (FileNotFoundException e11) {
            e2 = e11;
            bufferedOutputStream = null;
            fileOutputStream2 = null;
            d.a("SoProtect", e2.toString());
            if (fileOutputStream2 != null) {
                fileOutputStream2.close();
            }
            if (bufferedInputStream2 != null) {
                bufferedInputStream2.close();
            }
            if (bufferedOutputStream != null) {
                bufferedOutputStream.close();
            }
            return false;
        } catch (IOException e12) {
            e322 = e12;
            bufferedInputStream = null;
            fileOutputStream = null;
            d.a("SoProtect", e322.toString());
            if (fileOutputStream != null) {
                fileOutputStream.close();
            }
            if (bufferedInputStream != null) {
                bufferedInputStream.close();
            }
            if (bufferedOutputStream2 != null) {
                bufferedOutputStream2.close();
            }
            return false;
        } catch (Throwable th6) {
            th = th6;
            bufferedInputStream = null;
            fileOutputStream = null;
            if (fileOutputStream != null) {
                fileOutputStream.close();
            }
            if (bufferedInputStream != null) {
                bufferedInputStream.close();
            }
            if (bufferedOutputStream2 != null) {
                bufferedOutputStream2.close();
            }
            throw th;
        }
    }
}
