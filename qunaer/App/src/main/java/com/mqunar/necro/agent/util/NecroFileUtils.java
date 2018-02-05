package com.mqunar.necro.agent.util;

import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;

public class NecroFileUtils {
    public static boolean str2File(String str, String str2) {
        Throwable th;
        Closeable closeable = null;
        try {
            Closeable fileWriter = new FileWriter(new File(str2));
            try {
                fileWriter.write(str);
                safeClose(fileWriter);
                return true;
            } catch (Throwable th2) {
                Throwable th3 = th2;
                closeable = fileWriter;
                th = th3;
                safeClose(closeable);
                throw th;
            }
        } catch (Throwable th4) {
            th = th4;
            safeClose(closeable);
            throw th;
        }
    }

    public static String file2Str(String str) {
        Closeable closeable;
        Throwable th;
        Closeable fileInputStream;
        try {
            fileInputStream = new FileInputStream(new File(str));
            try {
                byte[] bArr = new byte[fileInputStream.available()];
                fileInputStream.read(bArr);
                String str2 = new String(bArr, "UTF-8");
                safeClose(fileInputStream);
                return str2;
            } catch (IOException e) {
                closeable = fileInputStream;
                safeClose(closeable);
                return null;
            } catch (Throwable th2) {
                th = th2;
                safeClose(fileInputStream);
                throw th;
            }
        } catch (IOException e2) {
            closeable = null;
            safeClose(closeable);
            return null;
        } catch (Throwable th3) {
            th = th3;
            fileInputStream = null;
            safeClose(fileInputStream);
            throw th;
        }
    }

    public static void safeClose(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (IOException e) {
            }
        }
    }
}
