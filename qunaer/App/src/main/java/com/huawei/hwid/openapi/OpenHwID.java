package com.huawei.hwid.openapi;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import com.huawei.hwid.openapi.b.c;
import com.huawei.hwid.openapi.b.d;
import com.huawei.hwid.openapi.c.a;
import com.huawei.hwid.openapi.e.b;
import com.huawei.hwid.openapi.out.IHwIDCallBack;
import com.huawei.hwid.openapi.out.ResReqHandler;
import java.util.HashMap;

public class OpenHwID {
    private static a a = null;

    public static String getAccessToken(Context context, String str, String str2, Bundle bundle) {
        return d.a(context, str, str2, bundle);
    }

    public static Boolean storeAccessToken(Context context, String str, String str2, String str3, Bundle bundle) {
        return Boolean.valueOf(d.a(context, str, str2, str3, bundle));
    }

    public static void logOut(Context context, String str, String str2, Bundle bundle) {
        d.b(context, str, str2, bundle);
        d.b(context, str, "default", null);
        try {
            b.a(context, str + "userName");
            b.a(context, "userInfo_Default");
            b.a(context, "loginUserName");
        } catch (Throwable e) {
            com.huawei.hwid.openapi.quicklogin.d.b.d.b("OpenHwID", e.getMessage(), e);
        } catch (Throwable e2) {
            com.huawei.hwid.openapi.quicklogin.d.b.d.b("OpenHwID", e2.getMessage(), e2);
        }
    }

    public static void setLoginProxy(Activity activity, String str, IHwIDCallBack iHwIDCallBack, Bundle bundle) {
        com.huawei.hwid.openapi.quicklogin.d.b.d.b("OpenHwID", "setLoginProxy");
        a = new a(activity, str, iHwIDCallBack, bundle);
    }

    public static void releaseResouce() {
        com.huawei.hwid.openapi.quicklogin.d.b.d.b("OpenHwID", "releaseResouce");
        if (a != null) {
            a.a();
            a = null;
        }
    }

    public static void initial(Context context, Bundle bundle) {
        com.huawei.hwid.openapi.quicklogin.d.b.d.b("OpenHwID", "initial");
        if (a == null) {
            com.huawei.hwid.openapi.quicklogin.d.b.d.d("OpenHwID", "when call initial, mHwIDAdapter is null");
        } else {
            a.a(context, bundle);
        }
    }

    public static void login(Bundle bundle) {
        com.huawei.hwid.openapi.quicklogin.d.b.d.b("OpenHwID", "login");
        if (a == null) {
            com.huawei.hwid.openapi.quicklogin.d.b.d.d("OpenHwID", "when call login, mHwIDAdapter is null");
        } else {
            a.a(bundle);
        }
    }

    public static void logout() {
        com.huawei.hwid.openapi.quicklogin.d.b.d.b("OpenHwID", "logout");
        if (a == null) {
            com.huawei.hwid.openapi.quicklogin.d.b.d.d("OpenHwID", "when call logout, mHwIDAdapter is null");
        } else {
            a.c();
        }
    }

    public static HashMap getUserInfo() {
        if (a != null) {
            return a.b();
        }
        com.huawei.hwid.openapi.quicklogin.d.b.d.d("OpenHwID", "when call getUserInfo, mHwIDAdapter is null");
        return new HashMap();
    }

    public static HashMap getDefaultUserInfo() {
        if (a != null) {
            return a.d();
        }
        com.huawei.hwid.openapi.quicklogin.d.b.d.d("OpenHwID", "when call getUserInfo, mHwIDAdapter is null");
        return new HashMap();
    }

    public static boolean hasLoginAccount(Context context) {
        return a.a(context);
    }

    public static void authorize(Activity activity, ResReqHandler resReqHandler, String str, Bundle bundle) {
        authorize(activity, null, resReqHandler, str, bundle);
    }

    public static void authorize(Activity activity, String str, ResReqHandler resReqHandler, String str2, Bundle bundle) {
        authorize(activity, str, resReqHandler, str2, null, bundle);
    }

    public static void authorize(Activity activity, String str, ResReqHandler resReqHandler, String str2, String str3, Bundle bundle) {
        authorize(activity, str, null, null, resReqHandler, str2, str3, bundle);
    }

    public static void authorize(Activity activity, String str, String str2, String str3, ResReqHandler resReqHandler, String str4, String str5, Bundle bundle) {
        com.huawei.hwid.openapi.b.a.a(new com.huawei.hwid.openapi.a.a(activity, resReqHandler, str4, null, str2, str, null, str3, str5, null, null, 0, null, bundle));
    }

    public static void userInfoRequest(Context context, ResReqHandler resReqHandler, String str) {
        userInfoRequest(context, resReqHandler, str, -1, null);
    }

    public static void userInfoRequest(Context context, ResReqHandler resReqHandler, String str, int i, Bundle bundle) {
        c.a(context, new com.huawei.hwid.openapi.d.a.b(str, i, bundle), resReqHandler);
    }

    public static void changSTToAT(Activity activity, String str, String str2, ResReqHandler resReqHandler, String str3, String str4, Bundle bundle) {
        c.a(activity, new com.huawei.hwid.openapi.d.a.a(activity, str, str2, str3, str4, bundle), resReqHandler);
    }
}
