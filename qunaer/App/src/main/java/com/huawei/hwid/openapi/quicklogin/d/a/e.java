package com.huawei.hwid.openapi.quicklogin.d.a;

import android.content.Context;
import android.os.Bundle;
import com.huawei.hwid.openapi.quicklogin.b.b.a.a;
import com.huawei.hwid.openapi.quicklogin.b.c;
import com.huawei.hwid.openapi.quicklogin.d.b.d;

class e implements a {
    final /* synthetic */ d a;
    private com.huawei.hwid.openapi.quicklogin.datatype.e b;

    public e(d dVar, Context context, com.huawei.hwid.openapi.quicklogin.datatype.e eVar) {
        this.a = dVar;
        this.b = eVar;
    }

    public void a(c cVar, Bundle bundle) {
        d.a("OpLogUtil", "onSuccess");
        this.a.a(1002, this.b);
    }

    public void b(c cVar, Bundle bundle) {
        d.a("OpLogUtil", "onFail");
        this.a.a(1002, this.b);
    }
}
