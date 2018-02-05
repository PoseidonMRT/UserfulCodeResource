package com.huawei.hwid.openapi.e;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.text.TextUtils;
import com.huawei.hwid.openapi.a.b;
import com.huawei.hwid.openapi.quicklogin.d.b.d;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import org.json.JSONObject;

public class h {
    private static String a = b.a;

    public static HashMap a(String str) {
        HashMap hashMap = new HashMap();
        try {
            if (!TextUtils.isEmpty(str)) {
                JSONObject jSONObject = new JSONObject(str);
                Iterator keys = jSONObject.keys();
                while (keys.hasNext()) {
                    String valueOf = String.valueOf(keys.next());
                    hashMap.put(valueOf, jSONObject.get(valueOf));
                }
            }
        } catch (Exception e) {
            d.d(a, e.getMessage());
        }
        return hashMap;
    }

    public static byte[] b(String str) {
        try {
            return str.getBytes("UTF-8");
        } catch (Throwable e) {
            d.b("DESEncryptor", "getBytes error:" + str, e);
            return new byte[0];
        }
    }

    public static boolean a(Context context) {
        if (context.checkCallingOrSelfPermission("android.permission.ACCESS_NETWORK_STATE") != 0) {
            d.c(a, "App miss permission android.permission.ACCESS_NETWORK_STATE! Some mobile's WebView don't display page!");
            return false;
        } else if (context.checkCallingOrSelfPermission("android.permission.INTERNET") != 0) {
            d.c(a, "App miss permission android.permission.INTERNET! Some mobile's WebView don't display page!");
            return false;
        } else if (-100 != g.a(context)) {
            return true;
        } else {
            d.c(a, "no net work, Some mobile's WebView don't display page!");
            return false;
        }
    }

    public static String a() {
        String toLowerCase = Locale.getDefault().getLanguage().toLowerCase(Locale.US);
        return toLowerCase + "-" + Locale.getDefault().getCountry().toLowerCase(Locale.US);
    }

    public static boolean a(Context context, String str) {
        return a(context, str, "com.huawei.hwid");
    }

    private static boolean a(Context context, String str, String str2) {
        PackageManager packageManager = context.getPackageManager();
        Intent intent = new Intent(str);
        intent.setPackage(str2);
        List list = null;
        if (packageManager != null) {
            list = packageManager.queryIntentActivities(intent, 0);
        }
        if (list == null) {
            d.b(a, "action " + str + " in HwID is no exist");
            return false;
        } else if (list.isEmpty()) {
            return false;
        } else {
            return true;
        }
    }
}
