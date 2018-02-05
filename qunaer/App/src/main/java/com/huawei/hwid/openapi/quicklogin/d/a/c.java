package com.huawei.hwid.openapi.quicklogin.d.a;

import android.util.Log;

public class c {
    private static String a = "hwid";
    private static String b = "";

    public static void a(String str, String str2) {
        a(3, str, str2, null, 2);
    }

    public static void b(String str, String str2) {
        a(4, str, str2, null, 2);
    }

    public static void c(String str, String str2) {
        a(5, str, str2, null, 2);
    }

    public static void a(String str, String str2, Throwable th) {
        a(5, str, str2, th, 2);
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

    public static void c(String str, String str2, Throwable th) {
        a(2, str, str2, th, 2);
    }

    private static synchronized void a(int i, String str, String str2, Throwable th, int i2) {
        synchronized (c.class) {
            if (a(i)) {
                if (str2 == null) {
                    try {
                        str2 = "";
                    } catch (Throwable e) {
                        Log.e(a, "call writeLog cause:" + e.getMessage(), e);
                    }
                }
                Log.println(i, b + str, str2);
            }
        }
    }

    private static boolean a(int i) {
        return Log.isLoggable(a, i);
    }
}
