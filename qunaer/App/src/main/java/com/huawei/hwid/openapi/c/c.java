package com.huawei.hwid.openapi.c;

import com.huawei.hwid.openapi.OpenHwID;
import com.huawei.hwid.openapi.quicklogin.a;
import com.huawei.hwid.openapi.quicklogin.d.a.d;
import com.huawei.hwid.openapi.quicklogin.datatype.e;
import java.util.HashMap;

class c implements Runnable {
    String a = null;
    final /* synthetic */ a b;
    private String c = null;
    private boolean d = false;
    private HashMap e = null;

    public c(a aVar, String str, String str2, boolean z) {
        this.b = aVar;
        this.c = str;
        this.a = str2;
        this.d = z;
    }

    public void run() {
        if (this.b.h) {
            d.a().a(this.b.e.getApplicationContext(), new e(this.b.e.getApplicationContext(), "106"));
            OpenHwID.userInfoRequest(this.b.e, new d(this), this.c, -1, a.a().c());
        }
    }

    private void a() {
        com.huawei.hwid.openapi.quicklogin.d.b.d.a(a.a, "come in check*********Fail:" + this.d);
        if (this.d) {
            this.b.c();
        }
    }
}
