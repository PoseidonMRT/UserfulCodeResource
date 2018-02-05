package com.huawei.hwid.openapi.auth;

import android.content.Intent;
import android.os.Bundle;
import com.huawei.cloudservice.IHwIDCallback.Stub;
import com.huawei.hwid.openapi.out.OutReturn;
import com.huawei.hwid.openapi.quicklogin.d.a.c;

class l extends Stub {
    final /* synthetic */ k a;

    l(k kVar) {
        this.a = kVar;
    }

    public void a(int i, Bundle bundle) {
        c.b("CloudAccountServiceHandle", "loginResult:retCode=" + i);
        if (i == -1) {
            Intent intent = new Intent();
            intent.setAction("com.huawei.cloudserive.getSTSuccess");
            Bundle bundle2 = new Bundle();
            int i2 = bundle.getInt("siteId", 0);
            String string = bundle.getString("userId");
            String string2 = bundle.getString("serviceToken");
            String string3 = bundle.getString("loginUserName");
            bundle2.putString("authToken", string2);
            bundle2.putInt("siteId", i2);
            bundle2.putString("userId", string);
            bundle2.putString("loginUserName", string3);
            intent.putExtra("bundle", bundle2);
            c.a("CloudAccountServiceHandle", "finish login, send local broadcast");
            com.huawei.hwid.openapi.e.c.a(this.a.d).a(this.a.d, intent);
        } else if (i == 0) {
            r0 = new Bundle();
            OutReturn.addFailCode(r0, 4);
            com.huawei.hwid.openapi.e.c.a(this.a.d).a(this.a.d, r0);
        } else if (i == 1) {
            r0 = new Bundle();
            OutReturn.addFailCode(r0, 5);
            com.huawei.hwid.openapi.e.c.a(this.a.d).a(this.a.d, r0);
        } else if (i == 2) {
            r0 = new Bundle();
            OutReturn.addFailCode(r0, 6);
            com.huawei.hwid.openapi.e.c.a(this.a.d).a(this.a.d, r0);
        } else {
            c.b("CloudAccountServiceHandle", "DONT KNOW RET_CODE:" + i);
            r0 = new Bundle();
            OutReturn.addFailCode(r0, 100);
            com.huawei.hwid.openapi.e.c.a(this.a.d).a(this.a.d, r0);
        }
    }
}
