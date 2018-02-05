package com.huawei.hwid.openapi.d.a;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import com.huawei.hwid.openapi.d.b;
import com.huawei.hwid.openapi.quicklogin.d.d;
import org.apache.http.HttpEntity;

public class a extends com.huawei.hwid.openapi.d.a {
    private String b;

    public a(String str) {
        this.b = str;
    }

    public a(Context context, String str, String str2, String str3, String str4, Bundle bundle) {
        Bundle bundle2 = new Bundle();
        bundle2.putString("client_id", str3);
        bundle2.putString("redirect_uri", str2);
        bundle2.putString("response_type", "token");
        bundle2.putString("scope", str);
        if (!(bundle == null || bundle.isEmpty())) {
            bundle2.putAll(bundle);
        }
        if (!TextUtils.isEmpty(str4)) {
            bundle2.putString("device_id", d.e(context));
            bundle2.putString("device_type", d.a(context, d.e(context)));
            bundle2.putString("sso_st", str4);
        }
        bundle2.putString("packageName", context.getPackageName());
        this.b = "https://login.vmall.com/oauth2/authorize?" + com.huawei.hwid.openapi.e.b.a.a(bundle2);
    }

    public HttpEntity a() {
        return null;
    }

    public String b() {
        return this.b;
    }

    public b c() {
        return b.URLType;
    }
}
