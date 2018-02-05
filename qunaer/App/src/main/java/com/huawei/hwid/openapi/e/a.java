package com.huawei.hwid.openapi.e;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import com.huawei.hwid.openapi.quicklogin.d.a.c;
import com.huawei.hwid.openapi.quicklogin.d.b.d;
import java.util.List;

public class a {
    public static boolean a(Context context) {
        try {
            if (context.getPackageManager().getApplicationInfo("com.huawei.hwid", 128) != null) {
                return true;
            }
            return false;
        } catch (NameNotFoundException e) {
            d.d("CloudAccountTools", e.getMessage());
            return false;
        }
    }

    public static boolean a(Context context, String str) {
        PackageManager packageManager = context.getPackageManager();
        Intent intent = new Intent(str);
        intent.setPackage("com.huawei.hwid");
        List list = null;
        if (packageManager != null) {
            list = packageManager.queryIntentServices(intent, 0);
        }
        if (list == null) {
            c.b("CloudAccountTools", "action " + str + "in HwID is no exist");
            return false;
        } else if (list.isEmpty()) {
            return false;
        } else {
            return true;
        }
    }
}
