package com.mqunar.qutui;

import android.content.ComponentName;
import android.content.ServiceConnection;
import android.os.IBinder;
import com.mqunar.qutui.IQutuiInterface.Stub;
import com.mqunar.tools.log.QLog;

class g implements ServiceConnection {
    final /* synthetic */ PushManager a;

    g(PushManager pushManager) {
        this.a = pushManager;
    }

    public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
        QLog.d("PushManager", "onServiceConnected", new Object[0]);
        this.a.d = Stub.asInterface(iBinder);
        try {
            this.a.d.registerCallBack(this.a.i);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void onServiceDisconnected(ComponentName componentName) {
        QLog.d("PushManager", "onServiceDisconnected", new Object[0]);
        try {
            this.a.d.unregisterCallBack(this.a.i);
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.a.d = null;
    }
}
