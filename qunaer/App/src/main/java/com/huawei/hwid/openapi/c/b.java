package com.huawei.hwid.openapi.c;

import android.os.Bundle;
import com.huawei.hwid.openapi.OpenHwID;
import com.huawei.hwid.openapi.out.OutReturn;
import com.huawei.hwid.openapi.out.ResReqHandler;
import com.huawei.hwid.openapi.quicklogin.a;
import com.huawei.hwid.openapi.quicklogin.d.b.d;
import com.huawei.hwid.openapi.quicklogin.d.b.k;

class b extends ResReqHandler {
    final /* synthetic */ boolean a;
    final /* synthetic */ boolean b;
    final /* synthetic */ a c;

    b(a aVar, boolean z, boolean z2) {
        this.c = aVar;
        this.a = z;
        this.b = z2;
    }

    public void onComplete(Bundle bundle) {
        d.b(a.a, "auth onComplete");
        if (bundle == null) {
            try {
                this.c.g.a(new NullPointerException("null return"));
            } catch (Exception e) {
                d.b(a.a, e.getMessage(), e);
                this.c.j();
                this.c.g.a(e);
            }
        } else if (OutReturn.isRequestSuccess(bundle)) {
            String accessToken = OutReturn.getAccessToken(bundle);
            String expireIn = OutReturn.getExpireIn(bundle);
            OpenHwID.storeAccessToken(this.c.e, a.a().b(), null, accessToken, null);
            OpenHwID.storeAccessToken(this.c.e, a.a().b(), "default", accessToken, null);
            com.huawei.hwid.openapi.b.d.b(this.c.e, a.a().b(), "default_expire", expireIn);
            d.b(a.a, "authorize, onComplete, token:" + k.a(accessToken));
            if (this.a) {
                this.c.a(accessToken, expireIn, this.b);
            } else {
                this.c.a(accessToken, expireIn, false);
            }
        } else {
            this.c.j();
            this.c.g.a(bundle, 1006, null);
        }
    }
}
