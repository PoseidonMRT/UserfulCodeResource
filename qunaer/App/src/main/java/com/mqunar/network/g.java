package com.mqunar.network;

import android.content.Context;

class g extends Thread {
    final /* synthetic */ Context a;
    final /* synthetic */ NetRequestManager b;

    g(NetRequestManager netRequestManager, Context context) {
        this.b = netRequestManager;
        this.a = context;
    }

    public void run() {
        if (!this.b.a(this.a, "http://211.151.112.148/v1/client.qunar.com/a", "http://httpdns1.opsapp.com/v1/client.qunar.com/a")) {
            this.b.a(this.a, "http://120.132.34.90/v1/client.qunar.com/a", "http://httpdns2.opsapp.com/v1/client.qunar.com/a");
        }
    }
}
