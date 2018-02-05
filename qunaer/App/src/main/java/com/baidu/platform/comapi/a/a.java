package com.baidu.platform.comapi.a;

import android.content.Context;

public class a {
    private static int a = 621133959;

    public static boolean a(Context context) {
        return c(context);
    }

    private static int b(Context context) {
        int i = 0;
        try {
            i = context.getPackageManager().getPackageInfo("com.baidu.BaiduMap", 64).signatures[0].hashCode();
        } catch (Exception e) {
        }
        return i;
    }

    private static boolean c(Context context) {
        return b(context) == a;
    }
}
