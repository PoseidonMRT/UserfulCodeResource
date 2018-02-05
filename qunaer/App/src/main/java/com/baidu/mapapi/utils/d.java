package com.baidu.mapapi.utils;

import android.os.IBinder;
import android.util.Log;
import com.baidu.mapframework.open.aidl.IComOpenClient;
import com.baidu.mapframework.open.aidl.b.a;

class d extends a {
    final /* synthetic */ c a;

    d(c cVar) {
        this.a = cVar;
    }

    public void a(IBinder iBinder) {
        Log.d(a.c, "onClientReady");
        if (a.e != null) {
            a.e = null;
        }
        a.e = IComOpenClient.a.a(iBinder);
        if (!a.t) {
            a.a(a.a);
        }
        a.t = true;
    }
}
