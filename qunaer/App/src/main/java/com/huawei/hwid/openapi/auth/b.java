package com.huawei.hwid.openapi.auth;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import com.huawei.hwid.openapi.a.a;
import com.huawei.hwid.openapi.auth.dump.OpenDumpActivity;
import com.huawei.hwid.openapi.e.h;
import com.huawei.hwid.openapi.out.OutReturn;
import com.huawei.hwid.openapi.quicklogin.d.a.c;
import com.huawei.hwid.openapi.quicklogin.d.b.d;
import com.huawei.hwid.openapi.quicklogin.d.b.k;

public class b extends a {
    private static final String a = com.huawei.hwid.openapi.a.b.a;
    private static Handler b = new c();

    private static boolean a(int i) {
        return i > 1;
    }

    public static void b(a aVar) {
        g(aVar);
        if (aVar.c != null && aVar.c.getBoolean("AIDL", false) && com.huawei.hwid.openapi.e.a.a(aVar.a, "com.huawei.hwid.ICloudService")) {
            c(aVar);
        } else {
            b(aVar, false);
        }
    }

    public static void c(a aVar) {
        c.a(a, "authorize");
        if (h.a(aVar.a)) {
            k a = k.a(aVar.a, aVar.a.getPackageName(), aVar.c);
            a.a(aVar.b);
            a.a();
            return;
        }
        d.b(a, "no network");
        aVar.b.finish(OutReturn.creatRunTimeErrRet("check env failed!"));
    }

    private static void b(a aVar, boolean z) {
        Intent intent = new Intent();
        intent.setClassName(aVar.a, OpenDumpActivity.class.getName());
        intent.putExtra("tokenType", aVar.a.getPackageName());
        intent.putExtra("loginChannel", 90000100);
        c.b(a, "default_channel_id =90000100");
        if (aVar.c != null) {
            c.b(a, "authTokenParam.bd  has value");
            intent.putExtra("needAuth", aVar.c.getBoolean("needAuth"));
            int i = aVar.c.getInt("loginChannel", 90000100);
            c.b(a, "channel_id =" + i);
            intent.putExtra("loginChannel", i);
        }
        if (z) {
            intent.putExtra("needAuth", true);
        }
        if (com.huawei.hwid.openapi.quicklogin.d.a.b.a(aVar.a)) {
            intent.putExtra("FULLSCREEN", true);
        }
        intent.putExtra("chooseAccount", a(aVar.n));
        c.b(a, "start auth in APK" + k.a(intent));
        aVar.a.startActivity(intent);
    }

    private static void g(a aVar) {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("com.huawei.cloudserive.getSTSuccess");
        intentFilter.addAction("com.huawei.cloudserive.getSTCancel");
        com.huawei.hwid.openapi.e.c.a(aVar.a).a(new e(aVar), intentFilter);
    }

    private static void b(Context context, a aVar, int i) {
        d.b(a, "come into getTmpSTbyST");
        com.huawei.hwid.openapi.quicklogin.b.b.d.a(context, new com.huawei.hwid.openapi.quicklogin.b.a(context, aVar.j, i), new d(aVar));
    }

    private static boolean h(a aVar) {
        Bundle bundle = aVar.c;
        if (bundle == null || 2 != bundle.getInt("forceLogin", -1)) {
            return false;
        }
        return true;
    }

    private static boolean b(a aVar, String str) {
        try {
            String str2 = (String) h.a(com.huawei.hwid.openapi.e.b.b(aVar.a, "userInfo_Default", "")).get("userID");
            if (!(str2 == null || str == null || !str2.equals(str))) {
                return true;
            }
        } catch (Throwable th) {
            d.b(a, th.getMessage(), th);
        }
        return false;
    }

    private static boolean i(a aVar) {
        CharSequence a = com.huawei.hwid.openapi.b.d.a(aVar.a, com.huawei.hwid.openapi.quicklogin.a.a().b(), null, null);
        long a2 = com.huawei.hwid.openapi.b.d.a(aVar.a, "expire", com.huawei.hwid.openapi.quicklogin.a.a().b());
        if (TextUtils.isEmpty(a) || a2 <= System.currentTimeMillis()) {
            return false;
        }
        return true;
    }
}
