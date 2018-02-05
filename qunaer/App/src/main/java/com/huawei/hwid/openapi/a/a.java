package com.huawei.hwid.openapi.a;

import android.app.Activity;
import android.os.Bundle;
import com.huawei.hwid.openapi.out.ResReqHandler;
import com.huawei.hwid.openapi.quicklogin.d.b.k;

public class a {
    public Activity a;
    public ResReqHandler b;
    public Bundle c;
    public String d;
    public String e;
    public String f;
    public String g;
    public String h;
    public String i;
    public String j;
    public Integer k;
    public String l;
    public String m;
    public int n = 0;

    public a(Activity activity, ResReqHandler resReqHandler, String str, String str2, String str3, String str4, String str5, String str6, String str7, Integer num, String str8, int i, String str9, Bundle bundle) {
        this.a = activity;
        this.g = str4;
        this.b = resReqHandler;
        this.f = str3;
        if (str3 == null) {
            this.f = "oob";
        }
        this.e = str2;
        if (str2 == null) {
            this.e = "token";
        }
        this.d = str;
        this.h = str5;
        this.k = num;
        this.i = str6;
        if (str6 == null) {
            this.i = "mobile";
        }
        this.j = str7;
        this.l = str8;
        this.n = i;
        this.m = str9;
        this.c = bundle;
    }

    public String a() {
        StringBuffer stringBuffer = new StringBuffer();
        String str = "=";
        String str2 = " ";
        stringBuffer.append("clientId").append(str).append(k.a(this.d)).append(str2).append("responseType").append(str).append(this.e).append(str2).append("redirectUrl").append(str).append(this.f).append(str2).append("scope").append(str).append(this.g).append(str2).append("state").append(str).append(this.h).append(str2).append("display").append(str).append(this.i).append(str2).append("sso_st").append(str).append(k.a(this.j)).append(str2).append("deviceType").append(str).append(this.k).append(str2).append("device_id").append(str).append(k.a(this.l)).append(str2).append("authTimes").append(str).append(this.n).append(str2).append("userAccount").append(str).append(k.a(this.m)).append(str2).append("max_authTimes").append(str).append(2);
        stringBuffer.append(k.a(this.c));
        return stringBuffer.toString();
    }
}
