package com.baidu.mapapi.utils;

import android.os.IBinder;
import android.util.Log;
import com.baidu.mapframework.open.aidl.IComOpenClient;
import com.baidu.mapframework.open.aidl.b.a;

final class b extends a {
    final /* synthetic */ int a;

    b(int i) {
        this.a = i;
    }

    public void a(IBinder iBinder) {
        Log.d(a.c, "onClientReady");
        if (a.e != null) {
            a.e = null;
        }
        a.e = IComOpenClient.a.a(iBinder);
        a.a(this.a);
        a.t = true;
    }
}
