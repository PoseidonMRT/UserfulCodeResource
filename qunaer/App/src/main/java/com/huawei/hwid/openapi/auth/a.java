package com.huawei.hwid.openapi.auth;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.webkit.CookieSyncManager;
import com.huawei.hwid.openapi.OpenHwID;
import com.huawei.hwid.openapi.a.b;
import com.huawei.hwid.openapi.e.g;
import com.huawei.hwid.openapi.e.h;
import com.huawei.hwid.openapi.quicklogin.d.b.d;

public class a {
    private static String a = b.a;
    private static com.huawei.hwid.openapi.f.a b = new com.huawei.hwid.openapi.f.b();

    private static void a(Activity activity) {
        g.b(activity);
    }

    public static void a(com.huawei.hwid.openapi.a.a aVar) {
        d.b(a, "enter authorize, redirectUrl:" + aVar.f);
        a(aVar.a);
        CookieSyncManager.createInstance(aVar.a);
        Bundle bundle = new Bundle();
        bundle.putString("client_id", aVar.d);
        bundle.putString("redirect_uri", aVar.f);
        bundle.putString("response_type", aVar.e);
        bundle.putString("display", aVar.i);
        bundle.putString("scope", aVar.g);
        bundle.putBoolean("download", true);
        bundle.putString("lang", h.a());
        if (aVar.k != null) {
            bundle.putInt("deviceType", aVar.k.intValue());
        }
        if (TextUtils.isEmpty(aVar.l)) {
            bundle.putString("device_id", aVar.l);
        }
        if (!(aVar.c == null || aVar.c.isEmpty())) {
            bundle.putAll(aVar.c);
        }
        if (!TextUtils.isEmpty(aVar.j)) {
            bundle.putString("device_id", com.huawei.hwid.openapi.quicklogin.d.d.e(aVar.a));
            bundle.putString("device_type", com.huawei.hwid.openapi.quicklogin.d.d.a(aVar.a, com.huawei.hwid.openapi.quicklogin.d.d.e(aVar.a)));
            if (!TextUtils.isEmpty(aVar.h)) {
                bundle.putString("device_type", com.huawei.hwid.openapi.quicklogin.d.d.a(aVar.a, com.huawei.hwid.openapi.quicklogin.d.d.e(aVar.a)));
            }
            bundle.putString("sso_st", aVar.j);
        }
        if (!b.a()) {
            bundle.putString("prompt", "consent");
        }
        bundle.putString("packageName", aVar.a.getPackageName());
        String str = "https://login.vmall.com/oauth2/authorize?" + com.huawei.hwid.openapi.e.b.a.a(bundle);
        if (b.a()) {
            OpenHwID.changSTToAT(aVar.a, "https://www.huawei.com/auth/account", "oob", aVar.b, aVar.d, aVar.j, aVar.c);
            return;
        }
        Intent intent = new Intent(aVar.a, WebAuthorizationActivity.class);
        intent.putExtra("AUTH_URL", str);
        aVar.a.startActivity(intent);
    }
}
