package com.huawei.hwid.openapi.quicklogin.d.b;

import android.text.TextUtils;
import android.util.Log;
import com.huawei.hwid.openapi.quicklogin.a.a;

public class d {
    private static String a = "hwid_opensdk";

    public static void a(String str, String str2) {
        a(3, str, str2, null, 2);
    }

    public static void a(String str, String str2, Throwable th) {
        a(3, str, str2, th, 2);
    }

    public static void b(String str, String str2) {
        a(4, str, str2, null, 2);
    }

    public static void c(String str, String str2) {
        a(5, str, str2, null, 2);
    }

    public static void d(String str, String str2) {
        a(6, str, str2, null, 2);
    }

    public static void b(String str, String str2, Throwable th) {
        a(6, str, str2, th, 2);
    }

    public static void e(String str, String str2) {
        a(2, str, str2, null, 2);
    }

    private static synchronized void a(int i, String str, String str2, Throwable th, int i2) {
        synchronized (d.class) {
            if (Log.isLoggable(a, i)) {
                String stringBuffer = new StringBuffer().append("[").append(30301300 + a.a).append("|").append(Thread.currentThread().getName()).append("-").append(Thread.currentThread().getId()).append("]:").append(str2).toString();
                if (TextUtils.isEmpty(stringBuffer)) {
                    stringBuffer = "";
                }
                Log.println(i, str, stringBuffer);
            }
        }
    }
}
