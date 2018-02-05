package com.huawei.hwid.openapi.auth;

import android.content.ComponentName;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import com.huawei.cloudservice.ICloudAccount.Stub;
import com.huawei.hwid.openapi.quicklogin.d.a.c;

class m implements ServiceConnection {
    final /* synthetic */ k a;

    m(k kVar) {
        this.a = kVar;
    }

    public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
        c.b("CloudAccountServiceHandle", "onServiceConnected---");
        int i = 0;
        while (i < 20) {
            this.a.a = Stub.a(iBinder);
            try {
                if (this.a.a == null) {
                    Thread.sleep(200);
                    i++;
                }
            } catch (InterruptedException e) {
                c.b("CloudAccountServiceHandle", "service cannot connected");
            }
        }
        try {
            this.a.a.a(this.a.e, this.a.f, this.a.h);
        } catch (RemoteException e2) {
            c.d("CloudAccountServiceHandle", "remote exception");
        }
    }

    public void onServiceDisconnected(ComponentName componentName) {
        c.b("CloudAccountServiceHandle", "onServiceDisconnected");
        this.a.a = null;
    }
}
