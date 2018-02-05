package org.acra.util;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import java.io.ByteArrayInputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.zip.GZIPOutputStream;
import org.acra.ACRA;

public class a {
    @Nullable
    public static File a(@NonNull File file, boolean z) {
        Closeable fileInputStream;
        Closeable gZIPOutputStream;
        Exception e;
        Throwable th;
        String str = file.getAbsolutePath() + ".gz";
        try {
            fileInputStream = new FileInputStream(file);
            try {
                gZIPOutputStream = new GZIPOutputStream(new FileOutputStream(str));
            } catch (Exception e2) {
                e = e2;
                gZIPOutputStream = null;
                try {
                    ACRA.f.e(ACRA.e, "FileInputStream error when gzip " + e.toString());
                    g.a(fileInputStream);
                    g.a(gZIPOutputStream);
                    return null;
                } catch (Throwable th2) {
                    th = th2;
                    g.a(fileInputStream);
                    g.a(gZIPOutputStream);
                    throw th;
                }
            } catch (Throwable th3) {
                th = th3;
                gZIPOutputStream = null;
                g.a(fileInputStream);
                g.a(gZIPOutputStream);
                throw th;
            }
            try {
                byte[] bArr = new byte[1024];
                while (true) {
                    int read = fileInputStream.read(bArr);
                    if (read <= 0) {
                        break;
                    }
                    gZIPOutputStream.write(bArr, 0, read);
                }
                gZIPOutputStream.finish();
                if (z && file.exists() && file.delete()) {
                    ACRA.f.b(ACRA.e, "delete gzip origin file");
                }
                File file2 = new File(str);
                g.a(fileInputStream);
                g.a(gZIPOutputStream);
                return file2;
            } catch (Exception e3) {
                e = e3;
                ACRA.f.e(ACRA.e, "FileInputStream error when gzip " + e.toString());
                g.a(fileInputStream);
                g.a(gZIPOutputStream);
                return null;
            }
        } catch (Exception e4) {
            e = e4;
            fileInputStream = null;
            gZIPOutputStream = null;
            ACRA.f.e(ACRA.e, "FileInputStream error when gzip " + e.toString());
            g.a(fileInputStream);
            g.a(gZIPOutputStream);
            return null;
        } catch (Throwable th4) {
            th = th4;
            fileInputStream = null;
            gZIPOutputStream = null;
            g.a(fileInputStream);
            g.a(gZIPOutputStream);
            throw th;
        }
    }

    @Nullable
    public static File a(@NonNull File file, @NonNull String str, boolean z) {
        Closeable fileInputStream;
        Closeable gZIPOutputStream;
        Exception e;
        Throwable th;
        String str2 = file.getAbsolutePath() + str;
        try {
            fileInputStream = new FileInputStream(file);
            try {
                gZIPOutputStream = new GZIPOutputStream(new FileOutputStream(str2));
                try {
                    byte[] bArr = new byte[1024];
                    while (true) {
                        int read = fileInputStream.read(bArr);
                        if (read <= 0) {
                            break;
                        }
                        gZIPOutputStream.write(bArr, 0, read);
                    }
                    gZIPOutputStream.finish();
                    if (z && file.exists() && file.delete()) {
                        ACRA.f.b(ACRA.e, "delete gzip origin file");
                    }
                    File file2 = new File(str2);
                    g.a(fileInputStream);
                    g.a(gZIPOutputStream);
                    return file2;
                } catch (Exception e2) {
                    e = e2;
                    try {
                        ACRA.f.e(ACRA.e, "FileInputStream error when gzip " + e.toString());
                        g.a(fileInputStream);
                        g.a(gZIPOutputStream);
                        return null;
                    } catch (Throwable th2) {
                        th = th2;
                        g.a(fileInputStream);
                        g.a(gZIPOutputStream);
                        throw th;
                    }
                }
            } catch (Exception e3) {
                e = e3;
                gZIPOutputStream = null;
                ACRA.f.e(ACRA.e, "FileInputStream error when gzip " + e.toString());
                g.a(fileInputStream);
                g.a(gZIPOutputStream);
                return null;
            } catch (Throwable th3) {
                th = th3;
                gZIPOutputStream = null;
                g.a(fileInputStream);
                g.a(gZIPOutputStream);
                throw th;
            }
        } catch (Exception e4) {
            e = e4;
            fileInputStream = null;
            gZIPOutputStream = null;
            ACRA.f.e(ACRA.e, "FileInputStream error when gzip " + e.toString());
            g.a(fileInputStream);
            g.a(gZIPOutputStream);
            return null;
        } catch (Throwable th4) {
            th = th4;
            fileInputStream = null;
            gZIPOutputStream = null;
            g.a(fileInputStream);
            g.a(gZIPOutputStream);
            throw th;
        }
    }

    @Nullable
    public static File a(@NonNull String str, @NonNull String str2) {
        Closeable byteArrayInputStream;
        Closeable gZIPOutputStream;
        Exception e;
        Throwable th;
        try {
            byteArrayInputStream = new ByteArrayInputStream(str.getBytes());
            try {
                gZIPOutputStream = new GZIPOutputStream(new FileOutputStream(str2));
                try {
                    byte[] bArr = new byte[1024];
                    while (true) {
                        int read = byteArrayInputStream.read(bArr);
                        if (read > 0) {
                            gZIPOutputStream.write(bArr, 0, read);
                        } else {
                            gZIPOutputStream.finish();
                            File file = new File(str2);
                            g.a(byteArrayInputStream);
                            g.a(gZIPOutputStream);
                            return file;
                        }
                    }
                } catch (Exception e2) {
                    e = e2;
                    try {
                        ACRA.f.e(ACRA.e, "FileInputStream error when gzip " + e.toString());
                        g.a(byteArrayInputStream);
                        g.a(gZIPOutputStream);
                        return null;
                    } catch (Throwable th2) {
                        th = th2;
                        g.a(byteArrayInputStream);
                        g.a(gZIPOutputStream);
                        throw th;
                    }
                }
            } catch (Exception e3) {
                e = e3;
                gZIPOutputStream = null;
                ACRA.f.e(ACRA.e, "FileInputStream error when gzip " + e.toString());
                g.a(byteArrayInputStream);
                g.a(gZIPOutputStream);
                return null;
            } catch (Throwable th3) {
                th = th3;
                gZIPOutputStream = null;
                g.a(byteArrayInputStream);
                g.a(gZIPOutputStream);
                throw th;
            }
        } catch (Exception e4) {
            e = e4;
            byteArrayInputStream = null;
            gZIPOutputStream = null;
            ACRA.f.e(ACRA.e, "FileInputStream error when gzip " + e.toString());
            g.a(byteArrayInputStream);
            g.a(gZIPOutputStream);
            return null;
        } catch (Throwable th4) {
            th = th4;
            byteArrayInputStream = null;
            gZIPOutputStream = null;
            g.a(byteArrayInputStream);
            g.a(gZIPOutputStream);
            throw th;
        }
    }
}
