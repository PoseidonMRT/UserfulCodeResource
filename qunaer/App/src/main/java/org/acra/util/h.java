package org.acra.util;

import android.content.Context;
import android.support.annotation.NonNull;
import java.io.Closeable;
import java.io.File;
import java.io.FileOutputStream;
import java.io.RandomAccessFile;
import java.util.UUID;
import org.acra.ACRA;

public final class h {
    private static String a;

    @NonNull
    public static synchronized String a(@NonNull Context context) {
        String str;
        synchronized (h.class) {
            if (a == null) {
                File file = new File(context.getFilesDir(), "ACRA-INSTALLATION");
                try {
                    if (!file.exists()) {
                        b(file);
                    }
                    a = a(file);
                } catch (Throwable e) {
                    ACRA.f.b(ACRA.e, "Couldn't retrieve InstallationId for " + context.getPackageName(), e);
                    str = "Couldn't retrieve InstallationId";
                } catch (Throwable e2) {
                    ACRA.f.b(ACRA.e, "Couldn't retrieve InstallationId for " + context.getPackageName(), e2);
                    str = "Couldn't retrieve InstallationId";
                }
            }
            str = a;
        }
        return str;
    }

    @NonNull
    private static String a(@NonNull File file) {
        Closeable randomAccessFile = new RandomAccessFile(file, "r");
        byte[] bArr = new byte[((int) randomAccessFile.length())];
        try {
            randomAccessFile.readFully(bArr);
            return new String(bArr);
        } finally {
            g.a(randomAccessFile);
        }
    }

    private static void b(@NonNull File file) {
        FileOutputStream fileOutputStream = new FileOutputStream(file);
        try {
            fileOutputStream.write(UUID.randomUUID().toString().getBytes());
        } finally {
            fileOutputStream.close();
        }
    }
}
