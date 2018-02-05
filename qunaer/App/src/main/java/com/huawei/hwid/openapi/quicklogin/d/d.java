package com.huawei.hwid.openapi.quicklogin.d;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Build;
import android.os.Build.VERSION;
import android.support.v4.os.EnvironmentCompat;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import com.huawei.hwid.openapi.quicklogin.d.a.b;
import com.huawei.hwid.openapi.quicklogin.d.b.e;
import com.huawei.hwid.openapi.quicklogin.d.b.g;
import java.util.UUID;

public class d {
    private static int a = -1;
    private static String b = "";
    private static String c = "";

    public static String a(Context context) {
        if (-1 == a && (VERSION.SDK_INT <= 22 || context.checkSelfPermission("android.permission.READ_PHONE_STATE") == 0)) {
            TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService("phone");
            if (telephonyManager != null) {
                a = telephonyManager.getPhoneType();
            }
        }
        if (2 == a) {
            return "2";
        }
        return "0";
    }

    public static String a() {
        return Build.MODEL;
    }

    public static String b(Context context) {
        String c = c(context);
        if (c == null || "NULL".equals(c)) {
            return e(context);
        }
        return c;
    }

    public static String c(Context context) {
        String d = d(context);
        if (!b.d() || b.c() || "NULL".equals(d)) {
            return d;
        }
        return d + "_" + b.b();
    }

    public static String d(Context context) {
        if (TextUtils.isEmpty(b) || !f(context).contains("encrypt")) {
            b = f(context).getString("DDID", "");
            boolean z = f(context).getBoolean("encrypt", false);
            if (TextUtils.isEmpty(b)) {
                if (VERSION.SDK_INT <= 22 || context.checkSelfPermission("android.permission.READ_PHONE_STATE") == 0) {
                    TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService("phone");
                    if (telephonyManager != null) {
                        b = telephonyManager.getDeviceId();
                    }
                }
                if (TextUtils.isEmpty(b) || EnvironmentCompat.MEDIA_UNKNOWN.equalsIgnoreCase(b)) {
                    return "NULL";
                }
                a(context, "DDID", a.a(context, b));
                a(context, "encrypt", true);
            } else if (z) {
                b = a.b(context, b);
            } else {
                a(context, "DDID", a.a(context, b));
                a(context, "encrypt", true);
            }
        }
        return b;
    }

    public static String e(Context context) {
        if (TextUtils.isEmpty(c)) {
            c = f(context).getString("UUID", "");
            if (TextUtils.isEmpty(c)) {
                c = UUID.randomUUID().toString();
                if (TextUtils.isEmpty(c)) {
                    return "NULL";
                }
                a(context, "UUID", c);
            }
        }
        return c;
    }

    public static String a(Context context, int i) {
        String c;
        String str = "";
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService("phone");
        if (g.b()) {
            e a = g.a();
            if (i == -999) {
                i = a.a();
            }
            if (5 == a.b(i)) {
                c = a.c(i);
                if (TextUtils.isEmpty(c)) {
                    c = a.a(i);
                    if (!TextUtils.isEmpty(c)) {
                        c = c.substring(0, 5);
                    }
                }
            }
            c = str;
        } else {
            if (5 == telephonyManager.getSimState()) {
                str = telephonyManager.getSimOperator();
                if (TextUtils.isEmpty(str)) {
                    c = telephonyManager.getSubscriberId();
                    if (!TextUtils.isEmpty(c)) {
                        c = c.substring(0, 5);
                    }
                }
            }
            c = str;
        }
        if (TextUtils.isEmpty(c)) {
            return "00000";
        }
        return c;
    }

    public static String a(Context context, String str) {
        if (e(context).equals(str)) {
            return "6";
        }
        return a(context);
    }

    public static String b() {
        return VERSION.RELEASE;
    }

    public static void a(Context context, String str, String str2) {
        Editor edit = f(context).edit();
        edit.putString(str, str2);
        edit.commit();
    }

    public static void a(Context context, String str, boolean z) {
        Editor edit = f(context).edit();
        edit.putBoolean(str, z);
        edit.commit();
    }

    public static SharedPreferences f(Context context) {
        return context.getSharedPreferences("TerminalInfo", 0);
    }
}
