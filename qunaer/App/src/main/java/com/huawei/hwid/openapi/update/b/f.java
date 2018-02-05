package com.huawei.hwid.openapi.update.b;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.text.TextUtils;
import com.huawei.hwid.openapi.quicklogin.d.a.c;
import com.huawei.hwid.openapi.quicklogin.d.b.k;
import java.util.Locale;

public class f {
    public static boolean a() {
        String str;
        String str2;
        String str3 = "";
        String str4 = "";
        try {
            Object a = b.a("android.os.SystemProperties", "get", new Class[]{String.class}, new Object[]{"ro.product.locale.language"});
            Object a2 = b.a("android.os.SystemProperties", "get", new Class[]{String.class}, new Object[]{"ro.product.locale.region"});
            if (a != null) {
                str3 = (String) a;
            }
            if (a2 != null) {
                str = (String) a2;
            } else {
                str = str4;
            }
            str2 = str3;
        } catch (Exception e) {
            Exception exception = e;
            str = str3;
            c.c("UpdateBaseUtil", exception.getMessage());
            str2 = str;
            str = str4;
        }
        if ("zh".equalsIgnoreCase(str2) && "cn".equalsIgnoreCase(r1)) {
            return true;
        }
        return false;
    }

    public static String a(Context context) {
        String toLowerCase = context.getResources().getConfiguration().locale.getCountry().toLowerCase(Locale.getDefault());
        c.b("UpdateBaseUtil", "countryStr:" + toLowerCase);
        return toLowerCase;
    }

    public static boolean b(Context context) {
        int intValue;
        Object b = b();
        try {
            Object a = b.a("android.os.SystemProperties", "getInt", new Class[]{String.class, Integer.TYPE}, new Object[]{"ro.build.hw_emui_api_level", Integer.valueOf(0)});
            if (a != null) {
                intValue = ((Integer) a).intValue();
                if (!TextUtils.isEmpty(b)) {
                    b = b.toLowerCase(Locale.ENGLISH);
                }
                if (TextUtils.isEmpty(b) && b.contains("3.0") && r0 == 0) {
                    return true;
                }
                return false;
            }
        } catch (Exception e) {
            c.c("UpdateBaseUtil", e.getMessage());
        }
        intValue = 0;
        if (TextUtils.isEmpty(b)) {
            b = b.toLowerCase(Locale.ENGLISH);
        }
        return TextUtils.isEmpty(b) ? false : false;
    }

    public static String b() {
        String str = "";
        try {
            Object a = b.a("android.os.SystemProperties", "get", new Class[]{String.class, String.class}, new Object[]{"ro.build.version.emui", ""});
            if (a != null) {
                return (String) a;
            }
        } catch (Exception e) {
            c.c("UpdateBaseUtil", e.getMessage());
        }
        return str;
    }

    public static void a(Context context, Intent intent, int i) {
        if (context == null || intent == null) {
            c.d("UpdateBaseUtil", "context or intent is null.");
            return;
        }
        if (!(context instanceof Activity)) {
            intent.setFlags((268435456 | i) | 67108864);
            c.e("UpdateBaseUtil", "not send Activity");
        } else if (i != 0) {
            intent.setFlags(i);
        }
        try {
            c.e("UpdateBaseUtil", "startActivity-->context = " + context.getClass().getName() + ", intent = " + k.a(intent));
            context.startActivity(intent);
        } catch (Exception e) {
            c.d("UpdateBaseUtil", "can not start activity:" + e.getMessage());
        }
    }

    public static boolean c(Context context) {
        if (context == null) {
            c.b("UpdateBaseUtil", "checkIsInstallHuaweiAccount context is null");
            return false;
        }
        try {
            if (context.getPackageManager().getApplicationInfo("com.huawei.hwid", 128) != null) {
                return true;
            }
            return false;
        } catch (NameNotFoundException e) {
            return false;
        }
    }

    public static String d(Context context) {
        if (context == null) {
            c.b("UpdateBaseUtil", "getHwIDVersionName context is null");
            return "";
        }
        try {
            String str = context.getPackageManager().getPackageInfo("com.huawei.hwid", 0).versionName;
            c.b("BaseUtil", "versionName " + str);
            return str;
        } catch (Throwable e) {
            c.b("BaseUtil", "getVersionTag error", e);
            return "";
        }
    }

    public static int e(Context context) {
        try {
            int i = context.getPackageManager().getPackageInfo("com.huawei.hwid", 0).versionCode;
            c.b("BaseUtil", "versionCode " + i);
            return i;
        } catch (Throwable e) {
            c.b("BaseUtil", "getVersionTag error", e);
            return 0;
        }
    }
}
