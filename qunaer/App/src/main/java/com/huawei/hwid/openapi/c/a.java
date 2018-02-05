package com.huawei.hwid.openapi.c;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import com.huawei.hwid.openapi.OpenHwID;
import com.huawei.hwid.openapi.a.b;
import com.huawei.hwid.openapi.auth.f;
import com.huawei.hwid.openapi.e.h;
import com.huawei.hwid.openapi.out.IHwIDCallBack;
import com.huawei.hwid.openapi.out.OutReturn;
import com.huawei.hwid.openapi.out.OutReturn.ParamStr;
import com.huawei.hwid.openapi.quicklogin.d.a.c;
import com.huawei.hwid.openapi.quicklogin.d.b.d;
import com.huawei.hwid.openapi.quicklogin.d.b.k;
import com.huawei.hwid.openapi.quicklogin.datatype.e;
import com.huawei.hwid.openapi.update.j;
import com.huawei.hwid.openapi.update.ui.OtaDownloadActivity;
import java.io.File;
import java.util.HashMap;

public class a {
    private static final String a = b.a;
    private static final Object b = new Object();
    private static int l = 0;
    private static int m = 1;
    private com.huawei.hwid.openapi.quicklogin.c.a.a c = null;
    private IHwIDCallBack d = null;
    private Activity e = null;
    private Handler f = null;
    private f g = null;
    private boolean h = true;
    private boolean i = false;
    private boolean j = true;
    private int k = 2;

    static /* synthetic */ int h() {
        int i = l;
        l = i + 1;
        return i;
    }

    public void a() {
        if (this.g != null) {
            this.g.a();
        }
    }

    public a(Activity activity, String str, IHwIDCallBack iHwIDCallBack, Bundle bundle) {
        d.b(a, "HwIDProxy bundle is " + k.a(bundle));
        this.e = activity;
        this.g = new f(iHwIDCallBack, activity);
        this.d = iHwIDCallBack;
        com.huawei.hwid.openapi.quicklogin.a.a().a(activity.getApplicationContext());
        com.huawei.hwid.openapi.quicklogin.a.a().a(str);
        com.huawei.hwid.openapi.quicklogin.a.a().a(bundle);
        this.j = bundle.getBoolean("showLogin", true);
        this.k = bundle.getInt("forceLogin", 2);
        this.g.a(bundle.getBoolean("isErrCallback", false));
        this.c = new com.huawei.hwid.openapi.quicklogin.c.a.a(activity);
        this.c.requestWindowFeature(1);
        if (this.j) {
            this.c.setCanceledOnTouchOutside(false);
        }
        this.f = new Handler();
    }

    private void a(Bundle bundle, String str) {
        e eVar = new e(this.e.getApplicationContext(), "106");
        eVar.a(System.currentTimeMillis());
        if (bundle == null) {
            eVar.b("0123456789");
            eVar.a("no_user");
            eVar.c(ParamStr.RET_RES_ERROR);
        } else if (OutReturn.isRequestSuccess(bundle)) {
            eVar.b("");
            eVar.a(str);
            eVar.c("success");
        } else {
            eVar.b(String.valueOf(OutReturn.getRetCode(bundle)));
            eVar.a("no_user");
            eVar.c(bundle.getString(ParamStr.Err_Info));
        }
        com.huawei.hwid.openapi.quicklogin.d.a.d.a().b(this.e.getApplicationContext(), eVar);
    }

    private void a(HashMap hashMap) {
        synchronized (b) {
            hashMap.put("loginStatus", "1");
            if (this.d != null) {
                this.d.onUserInfo(hashMap);
            } else {
                d.b(a, "handleUserinfoMap, mCallback is null");
            }
        }
    }

    public final void a(Bundle bundle) {
        if (com.huawei.hwid.openapi.update.b.f.c(this.e)) {
            b(this.e);
            c.b(a, "The " + (l + 1) + " times to try to login");
            String a = com.huawei.hwid.openapi.b.d.a(this.e, com.huawei.hwid.openapi.quicklogin.a.a().b(), null, null);
            long a2 = com.huawei.hwid.openapi.b.d.a(this.e, "expire", com.huawei.hwid.openapi.quicklogin.a.a().b());
            this.h = true;
            if (TextUtils.isEmpty(a) || a2 < System.currentTimeMillis() || this.k != -1) {
                this.i = true;
                a(this.j);
                return;
            }
            this.i = false;
            a(a, null, this.j);
            return;
        }
        c.b(a, "hwid is not exit");
        HashMap hashMap = new HashMap();
        hashMap.put("loginStatus", "9");
        hashMap.put("loginResult", "hwid is not exit");
        if (this.d != null) {
            this.d.onUserInfo(hashMap);
        }
    }

    public final void a(Context context, Bundle bundle) {
        c.b(a, "The initial");
        if (bundle == null) {
            c.b(a, "initial is null");
        } else if (!com.huawei.hwid.openapi.quicklogin.d.a.b.a(this.e)) {
            r0 = new HashMap();
            r0.put("loginStatus", "7");
            r0.put("loginResult", "Network Error");
            if (this.d != null) {
                this.d.onUserInfo(r0);
                return;
            }
            d.b(a, "mCallback is null");
            c.b(a, "error: have no network");
        } else if (!com.huawei.hwid.openapi.update.b.f.c(this.e)) {
            c.b(a, "hwid is not exit");
            r0 = new HashMap();
            r0.put("loginStatus", "9");
            r0.put("loginResult", "hwid is not exit");
            if (this.d != null) {
                this.d.onUserInfo(r0);
            }
            bundle.putBoolean("updateApk", false);
            b(this.e, bundle);
        } else if (com.huawei.hwid.openapi.update.b.f.e(this.e) < 20300000) {
            r0 = new HashMap();
            r0.put("loginStatus", "10");
            r0.put("loginResult", "hwid is low version");
            if (this.d != null) {
                this.d.onUserInfo(r0);
            }
            d.b(a, "apk version is low");
            bundle.putBoolean("updateApk", true);
            b(this.e, bundle);
        } else {
            String d = com.huawei.hwid.openapi.update.b.f.d(this.e);
            HashMap hashMap = new HashMap();
            hashMap.put("loginStatus", "11");
            hashMap.put("loginResult", "hwid is initial ok" + d);
            if (this.d != null) {
                this.d.onUserInfo(hashMap);
            }
        }
    }

    private final void a(boolean z) {
        try {
            d.b(a, "come into auth");
            c.b(a, k.a(com.huawei.hwid.openapi.quicklogin.a.a().c()));
            boolean c = com.huawei.hwid.openapi.quicklogin.d.a.b.c(this.e);
            if (c && z) {
                this.c.a(this.e.getResources().getString(com.huawei.hwid.openapi.quicklogin.d.b.a(this.e, "xh_logining")));
                i();
            }
            OpenHwID.authorize(this.e, "https://www.huawei.com/auth/account", "oob", "mobile", new b(this, c, z), com.huawei.hwid.openapi.quicklogin.a.a().b(), null, com.huawei.hwid.openapi.quicklogin.a.a().c());
        } catch (Exception e) {
            d.b(a, e.getMessage(), e);
            j();
            this.g.a(e);
        }
    }

    private final void a(String str, String str2, boolean z) {
        d.b(a, "come into getUserInfo:showDialog=" + z);
        if (z) {
            try {
                if (!this.c.isShowing()) {
                    this.c.a(this.e.getResources().getString(com.huawei.hwid.openapi.quicklogin.d.b.a(this.e, "xh_logining")));
                    i();
                }
            } catch (Throwable e) {
                d.b(a, e.toString(), e);
                return;
            } catch (Throwable e2) {
                d.b(a, e2.getMessage(), e2);
                return;
            }
        }
        int i = 0;
        if (!this.i) {
            i = 2000;
        }
        Runnable cVar = new c(this, str, str2, this.i);
        if (this.f != null) {
            this.f.postDelayed(cVar, (long) i);
        }
    }

    public HashMap b() {
        if (com.huawei.hwid.openapi.update.b.f.c(this.e)) {
            HashMap b = b("userInfo");
            b.put("accesstoken", OpenHwID.getAccessToken(this.e, com.huawei.hwid.openapi.quicklogin.a.a().b(), null, null));
            return b;
        }
        c.b(a, "hwid is not exit");
        b = new HashMap();
        b.put("loginStatus", "9");
        b.put("loginResult", "hwid is not exit");
        if (this.d != null) {
            this.d.onUserInfo(b);
        }
        return new HashMap();
    }

    private void a(String str) {
        if (str == null) {
            str = "";
        }
        try {
            com.huawei.hwid.openapi.e.b.a(this.e, "userInfo", str);
        } catch (Throwable e) {
            d.b(a, e.getMessage(), e);
        }
    }

    public void c() {
        a(null);
        OpenHwID.logOut(this.e, com.huawei.hwid.openapi.quicklogin.a.a().b(), null, null);
    }

    public HashMap d() {
        Object accessToken = OpenHwID.getAccessToken(this.e, com.huawei.hwid.openapi.quicklogin.a.a().b(), "default", null);
        if (TextUtils.isEmpty(accessToken) || TextUtils.isEmpty(accessToken.trim())) {
            return b();
        }
        HashMap b = b("default");
        b.put("accesstoken", accessToken);
        return b;
    }

    private HashMap b(String str) {
        try {
            HashMap a = h.a(com.huawei.hwid.openapi.e.b.b(this.e, str, ""));
            if (TextUtils.isEmpty(a.toString())) {
                com.huawei.hwid.openapi.e.b.a(this.e, "loginUserName");
            } else {
                Object b = com.huawei.hwid.openapi.e.b.b(this.e, "loginUserName", "");
                if (TextUtils.isEmpty(b) && a.get("userName") != null) {
                    b = (String) a.get("userName");
                }
                a.put("loginUserName", b);
            }
            return a;
        } catch (Throwable e) {
            d.b(a, e.toString(), e);
            return new HashMap();
        } catch (Throwable e2) {
            d.b(a, e2.getMessage(), e2);
            return new HashMap();
        }
    }

    private void i() {
        try {
            d.b(a, "showDialog");
            this.c.show();
        } catch (Throwable e) {
            d.b(a, e.getMessage(), e);
        }
    }

    private void j() {
        k();
        try {
            this.c.dismiss();
        } catch (Throwable e) {
            d.b(a, e.getMessage(), e);
        }
    }

    private void k() {
    }

    public static boolean a(Context context) {
        if (com.huawei.hwid.openapi.update.b.f.c(context)) {
            return com.huawei.hwid.openapi.quicklogin.d.a.b.c(context);
        }
        c.b(a, "hwid is not exit");
        return false;
    }

    public static void b(Context context, Bundle bundle) {
        c.b(a, "downloadHwIDAPK");
        Intent intent = new Intent();
        intent.setClass(context, OtaDownloadActivity.class);
        intent.putExtras(bundle);
        com.huawei.hwid.openapi.update.b.f.a(context, intent, 0);
    }

    private static void b(Context context) {
        if (j.a(context).a() != null) {
            Object c = j.a(context).c(context);
            if (!TextUtils.isEmpty(c)) {
                File file = new File(c);
                if (file.exists()) {
                    try {
                        if (!file.delete()) {
                            c.d(a, "delete old apk error");
                        }
                    } catch (Exception e) {
                        c.d(a, "delete old apk error,error is " + e.getMessage());
                    }
                }
            }
            j.a(context).b();
        }
    }
}
