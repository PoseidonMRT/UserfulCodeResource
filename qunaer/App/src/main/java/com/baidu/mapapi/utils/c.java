package com.baidu.mapapi.utils;

import android.content.ComponentName;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.util.Log;
import com.baidu.mapframework.open.aidl.a.a;

final class c implements ServiceConnection {
    c() {
    }

    public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
        if (a.v != null) {
            a.v.interrupt();
        }
        Log.d(a.c, "onServiceConnected " + componentName);
        try {
            if (a.d != null) {
                a.d = null;
            }
            a.d = a.a(iBinder);
            a.d.a(new d(this));
        } catch (Throwable e) {
            Log.d(a.c, "getComOpenClient ", e);
            if (a.d != null) {
                a.d = null;
            }
        }
    }

    public void onServiceDisconnected(ComponentName componentName) {
        Log.d(a.c, "onServiceDisconnected " + componentName);
        if (a.d != null) {
            a.d = null;
            a.u = false;
        }
    }
}
