package com.huawei.hwid.openapi.e;

import android.content.Context;
import android.content.SharedPreferences;
import com.huawei.hwid.openapi.e.a.c;

public class b {
    public static SharedPreferences a(Context context) {
        return context.getSharedPreferences("hwid_opensdk_config", 0);
    }

    public static void a(Context context, String str, String str2) {
        a(context).edit().putString(c.a(context, str), c.a(context, str2)).commit();
    }

    public static void a(Context context, String str) {
        a(context).edit().remove(c.a(context, str)).commit();
    }

    public static String b(Context context, String str, String str2) {
        return c.b(context, a(context).getString(c.a(context, str), str2));
    }
}
