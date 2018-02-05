package com.mqunar.qutui;

import android.content.Context;
import android.content.pm.PackageInfo;
import com.mqunar.tools.log.QLog;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

final class i {
    public static PackageInfo a(Context context, String str) {
        try {
            return context.getPackageManager().getPackageInfo(str, 1);
        } catch (Throwable th) {
            QLog.e(th);
            return null;
        }
    }

    public static List<String> a() {
        File[] listFiles = new File("/proc").listFiles(new j());
        List arrayList = new ArrayList();
        if (listFiles != null) {
            for (File name : listFiles) {
                try {
                    String a = a("/proc/" + name.getName() + "/cmdline");
                    if (!(a.equals("null") || a.contains("com.android.systemui") || a.contains("/system/") || a.contains("dirac"))) {
                        arrayList.add(a);
                    }
                } catch (Throwable e) {
                    QLog.e(e);
                }
            }
        }
        return arrayList;
    }

    private static String a(String str) {
        StringBuilder stringBuilder = new StringBuilder();
        BufferedReader bufferedReader = new BufferedReader(new FileReader(str));
        stringBuilder.append(bufferedReader.readLine());
        for (String readLine = bufferedReader.readLine(); readLine != null; readLine = bufferedReader.readLine()) {
            stringBuilder.append('\n').append(readLine);
        }
        bufferedReader.close();
        return stringBuilder.toString().trim();
    }
}
