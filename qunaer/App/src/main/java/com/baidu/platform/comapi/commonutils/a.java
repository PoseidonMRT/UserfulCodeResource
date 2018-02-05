package com.baidu.platform.comapi.commonutils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build.VERSION;
import android.util.Log;
import com.iflytek.aiui.AIUIConstant;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class a {
    private static final boolean a = (VERSION.SDK_INT >= 8);

    public static Bitmap a(String str, Context context) {
        try {
            InputStream open = context.getAssets().open(str);
            return open != null ? BitmapFactory.decodeStream(open) : null;
        } catch (Exception e) {
            return BitmapFactory.decodeFile(b("assets/" + str, str, context));
        }
    }

    private static void a(InputStream inputStream, FileOutputStream fileOutputStream) {
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

    public static void a(String str, String str2, Context context) {
        InputStream open;
        FileOutputStream fileOutputStream;
        InputStream inputStream;
        Throwable th;
        Throwable th2;
        FileOutputStream fileOutputStream2 = null;
        try {
            open = context.getAssets().open(str);
            if (open != null) {
                byte[] bArr;
                try {
                    bArr = new byte[open.available()];
                    open.read(bArr);
                    File file = new File(context.getFilesDir().getAbsolutePath() + "/" + str2);
                    if (file.exists()) {
                        file.delete();
                    }
                    file.createNewFile();
                    fileOutputStream = new FileOutputStream(file);
                } catch (Exception e) {
                    inputStream = open;
                    try {
                        b("assets/" + str, str2, context);
                        if (inputStream != null) {
                            try {
                                inputStream.close();
                            } catch (IOException e2) {
                                e2.printStackTrace();
                                return;
                            }
                        }
                        if (fileOutputStream2 == null) {
                            fileOutputStream2.close();
                        }
                    } catch (Throwable th3) {
                        th = th3;
                        open = inputStream;
                        fileOutputStream = fileOutputStream2;
                        th2 = th;
                        if (open != null) {
                            try {
                                open.close();
                            } catch (IOException e3) {
                                e3.printStackTrace();
                                throw th2;
                            }
                        }
                        if (fileOutputStream != null) {
                            fileOutputStream.close();
                        }
                        throw th2;
                    }
                } catch (Throwable th4) {
                    th = th4;
                    fileOutputStream = null;
                    th2 = th;
                    if (open != null) {
                        open.close();
                    }
                    if (fileOutputStream != null) {
                        fileOutputStream.close();
                    }
                    throw th2;
                }
                try {
                    fileOutputStream.write(bArr);
                    fileOutputStream.close();
                } catch (Exception e4) {
                    fileOutputStream2 = fileOutputStream;
                    inputStream = open;
                    b("assets/" + str, str2, context);
                    if (inputStream != null) {
                        inputStream.close();
                    }
                    if (fileOutputStream2 == null) {
                        fileOutputStream2.close();
                    }
                } catch (Throwable th5) {
                    th2 = th5;
                    if (open != null) {
                        open.close();
                    }
                    if (fileOutputStream != null) {
                        fileOutputStream.close();
                    }
                    throw th2;
                }
            }
            fileOutputStream = null;
            if (open != null) {
                try {
                    open.close();
                } catch (IOException e22) {
                    e22.printStackTrace();
                    return;
                }
            }
            if (fileOutputStream != null) {
                fileOutputStream.close();
            }
        } catch (Exception e5) {
            inputStream = null;
            b("assets/" + str, str2, context);
            if (inputStream != null) {
                inputStream.close();
            }
            if (fileOutputStream2 == null) {
                fileOutputStream2.close();
            }
        } catch (Throwable th42) {
            open = null;
            th2 = th42;
            fileOutputStream = null;
            if (open != null) {
                open.close();
            }
            if (fileOutputStream != null) {
                fileOutputStream.close();
            }
            throw th2;
        }
    }

    private static String b(String str, String str2, Context context) {
        Throwable e;
        ZipFile zipFile = null;
        String str3 = "";
        StringBuilder stringBuilder = new StringBuilder(context.getFilesDir().getAbsolutePath());
        if (a) {
            str3 = context.getPackageCodePath();
        }
        ZipFile zipFile2;
        try {
            zipFile2 = new ZipFile(str3);
            try {
                File file;
                File file2;
                int lastIndexOf = str2.lastIndexOf("/");
                if (lastIndexOf > 0) {
                    file = new File(context.getFilesDir().getAbsolutePath());
                    String substring = str2.substring(0, lastIndexOf);
                    file2 = new File(file.getAbsolutePath() + "/" + substring, str2.substring(lastIndexOf + 1, str2.length()));
                } else {
                    file = new File(context.getFilesDir(), AIUIConstant.RES_TYPE_ASSETS);
                    file2 = new File(file.getAbsolutePath(), str2);
                }
                file.mkdirs();
                ZipEntry entry = zipFile2.getEntry(str);
                if (entry == null) {
                    if (zipFile2 != null) {
                        try {
                            zipFile2.close();
                        } catch (IOException e2) {
                        }
                    }
                    return null;
                }
                a(zipFile2.getInputStream(entry), new FileOutputStream(file2));
                stringBuilder.append("/").append(str);
                if (zipFile2 != null) {
                    try {
                        zipFile2.close();
                    } catch (IOException e3) {
                    }
                }
                return stringBuilder.toString();
            } catch (Exception e4) {
                e = e4;
                zipFile = zipFile2;
                try {
                    Log.e(a.class.getSimpleName(), "copyAssetsError", e);
                    if (zipFile != null) {
                        try {
                            zipFile.close();
                        } catch (IOException e5) {
                        }
                    }
                    return stringBuilder.toString();
                } catch (Throwable th) {
                    e = th;
                    zipFile2 = zipFile;
                    if (zipFile2 != null) {
                        try {
                            zipFile2.close();
                        } catch (IOException e6) {
                        }
                    }
                    throw e;
                }
            } catch (Throwable th2) {
                e = th2;
                if (zipFile2 != null) {
                    zipFile2.close();
                }
                throw e;
            }
        } catch (Exception e7) {
            e = e7;
            Log.e(a.class.getSimpleName(), "copyAssetsError", e);
            if (zipFile != null) {
                zipFile.close();
            }
            return stringBuilder.toString();
        } catch (Throwable th3) {
            e = th3;
            zipFile2 = null;
            if (zipFile2 != null) {
                zipFile2.close();
            }
            throw e;
        }
    }
}
