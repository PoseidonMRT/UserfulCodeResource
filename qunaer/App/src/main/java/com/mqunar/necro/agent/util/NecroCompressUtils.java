package com.mqunar.necro.agent.util;

import java.io.ByteArrayInputStream;
import java.io.Closeable;
import java.io.FileOutputStream;
import java.util.zip.GZIPOutputStream;

public class NecroCompressUtils {
    public static void doCompressString(String str, String str2) {
        Closeable byteArrayInputStream;
        Closeable gZIPOutputStream;
        Exception e;
        Throwable th;
        Closeable closeable = null;
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
                            NecroFileUtils.safeClose(byteArrayInputStream);
                            NecroFileUtils.safeClose(gZIPOutputStream);
                            return;
                        }
                    }
                } catch (Exception e2) {
                    e = e2;
                    closeable = byteArrayInputStream;
                    try {
                        e.printStackTrace();
                        NecroFileUtils.safeClose(closeable);
                        NecroFileUtils.safeClose(gZIPOutputStream);
                    } catch (Throwable th2) {
                        th = th2;
                        byteArrayInputStream = closeable;
                        closeable = gZIPOutputStream;
                        NecroFileUtils.safeClose(byteArrayInputStream);
                        NecroFileUtils.safeClose(closeable);
                        throw th;
                    }
                } catch (Throwable th3) {
                    th = th3;
                    closeable = gZIPOutputStream;
                    NecroFileUtils.safeClose(byteArrayInputStream);
                    NecroFileUtils.safeClose(closeable);
                    throw th;
                }
            } catch (Exception e3) {
                e = e3;
                gZIPOutputStream = null;
                closeable = byteArrayInputStream;
                e.printStackTrace();
                NecroFileUtils.safeClose(closeable);
                NecroFileUtils.safeClose(gZIPOutputStream);
            } catch (Throwable th4) {
                th = th4;
                NecroFileUtils.safeClose(byteArrayInputStream);
                NecroFileUtils.safeClose(closeable);
                throw th;
            }
        } catch (Exception e4) {
            e = e4;
            gZIPOutputStream = null;
            e.printStackTrace();
            NecroFileUtils.safeClose(closeable);
            NecroFileUtils.safeClose(gZIPOutputStream);
        } catch (Throwable th5) {
            th = th5;
            byteArrayInputStream = null;
            NecroFileUtils.safeClose(byteArrayInputStream);
            NecroFileUtils.safeClose(closeable);
            throw th;
        }
    }
}
