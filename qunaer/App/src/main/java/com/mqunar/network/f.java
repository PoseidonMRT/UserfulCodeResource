package com.mqunar.network;

import android.content.Context;
import java.util.concurrent.atomic.AtomicInteger;

class f extends Thread {
    final /* synthetic */ Context a;
    final /* synthetic */ String b;
    final /* synthetic */ boolean[] c;
    final /* synthetic */ AtomicInteger d;
    final /* synthetic */ NetRequestManager e;

    f(NetRequestManager netRequestManager, Context context, String str, boolean[] zArr, AtomicInteger atomicInteger) {
        this.e = netRequestManager;
        this.a = context;
        this.b = str;
        this.c = zArr;
        this.d = atomicInteger;
    }

    public void run() {
        boolean a = this.e.a(this.a, this.b);
        synchronized (this.c) {
            if (a) {
                this.c[0] = true;
                this.c.notify();
            } else {
                this.d.decrementAndGet();
                if (this.d.get() <= 0) {
                    this.c.notify();
                }
            }
        }
    }
}
