package com.huawei.hwid.openapi.auth;

import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.RemoteException;
import com.huawei.cloudservice.ICloudAccount;
import com.huawei.cloudservice.IHwIDCallback;
import com.huawei.hwid.openapi.out.OutReturn;
import com.huawei.hwid.openapi.out.ResReqHandler;
import com.huawei.hwid.openapi.quicklogin.d.a.b;
import com.huawei.hwid.openapi.quicklogin.d.a.c;

public final class k {
    private static final Object[] b = new Object[0];
    private static k c;
    private ICloudAccount a;
    private Context d;
    private String e;
    private Bundle f;
    private ResReqHandler g = null;
    private IHwIDCallback h = null;
    private ServiceConnection i = new m(this);

    public static k a(Context context, String str, Bundle bundle) {
        synchronized (b) {
            if (c == null) {
                c = new k(context, str, bundle);
            }
        }
        return c;
    }

    private k(Context context, String str, Bundle bundle) {
        this.d = context;
        this.e = str;
        this.f = bundle;
    }

    public void a() {
        c.a("CloudAccountServiceHandle", "bindService");
        if (this.g == null) {
            c.d("CloudAccountServiceHandle", "has not set LoginHandle");
        } else if (!b.c(this.d)) {
            Bundle bundle = new Bundle();
            OutReturn.addFailCode(bundle, 4);
            com.huawei.hwid.openapi.e.c.a(this.d).a(this.d, bundle);
        } else if (this.h == null) {
            c.d("CloudAccountServiceHandle", "mCallback is null, cannot bind service");
        } else if (this.a == null) {
            b();
        } else {
            a(this.e, this.f);
        }
    }

    private void b() {
        Intent intent = new Intent();
        intent.setAction("com.huawei.hwid.ICloudService");
        intent.setPackage("com.huawei.hwid");
        c.a("CloudAccountServiceHandle", "begin to bindService");
        this.d.getApplicationContext().bindService(intent, this.i, 1);
    }

    private void a(String str, Bundle bundle) {
        c.a("CloudAccountServiceHandle", "login");
        try {
            if (this.h != null && this.a != null) {
                this.a.a(str, bundle, this.h);
            }
        } catch (RemoteException e) {
            c.d("CloudAccountServiceHandle", "Call Remote Exception and try again");
            b();
        }
    }

    public void a(ResReqHandler resReqHandler) {
        this.g = resReqHandler;
        this.h = new l(this);
    }
}
