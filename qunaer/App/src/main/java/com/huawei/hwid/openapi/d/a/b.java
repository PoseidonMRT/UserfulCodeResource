package com.huawei.hwid.openapi.d.a;

import android.os.Bundle;
import com.huawei.hwid.openapi.d.a;
import com.huawei.hwid.openapi.out.OutReturn.ParamStr;
import com.huawei.hwid.openapi.quicklogin.d.b.d;
import com.huawei.hwid.openapi.quicklogin.d.b.k;
import java.util.HashMap;
import java.util.Map;
import org.apache.http.HttpEntity;

public class b extends a {
    private String b = "https://api.vmall.com/rest.php";
    private String c = "001110";
    private String d = "OpenUP.User.getInfo";
    private String e;
    private int f = -1;
    private int g = 0;

    public b(String str, int i, Bundle bundle) {
        this.e = str;
        this.f = i;
        if (bundle != null) {
            d.b(a, "bundle ====" + k.a(bundle));
        }
    }

    public HttpEntity a() {
        HashMap hashMap = new HashMap();
        hashMap.put("queryRangeFlag", this.c);
        hashMap.put("nsp_svc", this.d);
        hashMap.put("nsp_ts", String.valueOf(System.currentTimeMillis()));
        hashMap.put(ParamStr.ACCESS_TOKEN, this.e);
        hashMap.put("getNickName", String.valueOf(this.g));
        if (-1 != this.f) {
            hashMap.put("reqClientType", String.valueOf(this.f));
        }
        d.a(a, "GetUserInfoReq params " + k.a((Map) hashMap));
        return com.huawei.hwid.openapi.e.b.a.a(hashMap);
    }

    public com.huawei.hwid.openapi.d.b c() {
        return com.huawei.hwid.openapi.d.b.JSONType;
    }

    public String b() {
        return this.b;
    }
}
