package org.acra.collector;

import android.content.Context;
import android.os.Environment;
import android.support.annotation.NonNull;
import com.mqunar.contacts.basis.model.Contact;
import java.io.BufferedReader;
import java.io.Closeable;
import java.io.File;
import java.io.FileReader;
import org.acra.ACRA;
import org.acra.util.g;

final class a {
    static boolean a(@NonNull Context context) {
        Closeable bufferedReader;
        Exception e;
        Throwable th;
        File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/qunar/cloudTest.txt");
        if (file.exists() && file.isFile()) {
            try {
                bufferedReader = new BufferedReader(new FileReader(file));
                try {
                    String[] split = bufferedReader.readLine().trim().split(Contact.NUMBER);
                    if (split.length == 2) {
                        String[] split2 = split[1].split("=");
                        split = split[0].split("=");
                        if (split.length == 2 && split[0] != null && split[0].contains("isCloudTest") && split2.length == 2 && "testPackage".equals(split2[0])) {
                            boolean equals = context.getPackageName().equals(split2[1]);
                            Object obj = split[1];
                            if (equals && "true".equals(obj)) {
                                g.a(bufferedReader);
                                return true;
                            }
                        }
                    }
                    g.a(bufferedReader);
                } catch (Exception e2) {
                    e = e2;
                    try {
                        ACRA.f.e(ACRA.e, "get cloudTestFile data error" + e.toString());
                        g.a(bufferedReader);
                        return false;
                    } catch (Throwable th2) {
                        th = th2;
                        g.a(bufferedReader);
                        throw th;
                    }
                }
            } catch (Exception e3) {
                e = e3;
                bufferedReader = null;
                ACRA.f.e(ACRA.e, "get cloudTestFile data error" + e.toString());
                g.a(bufferedReader);
                return false;
            } catch (Throwable th3) {
                th = th3;
                bufferedReader = null;
                g.a(bufferedReader);
                throw th;
            }
        }
        ACRA.f.b(ACRA.e, "can not find cloudTestFile");
        return false;
    }
}
