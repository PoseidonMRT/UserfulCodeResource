package com.megvii.livenessdeteciton.obf;

import android.util.Log;

public final class d {
    private static boolean a = false;
    private static String b = "MegviiSDK";

    public static void a() {
        a = true;
    }

    public static void b() {
        a = false;
    }

    public static void a(String str) {
        b(b, str);
    }

    public static void b(String str) {
        a(b, str);
    }

    public static void a(String str, String str2) {
        if (a) {
            if (str == null) {
                str = b;
            }
            if (str2 == null) {
                str2 = "";
            }
            Log.e(str, str2);
        }
    }

    public static void b(String str, String str2) {
        if (a) {
            if (str == null) {
                str = b;
            }
            if (str2 == null) {
                str2 = "";
            }
            Log.d(str, str2);
        }
    }
}
