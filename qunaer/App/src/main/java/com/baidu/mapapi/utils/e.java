package com.baidu.mapapi.utils;

import android.content.Context;

final class e implements Runnable {
    final /* synthetic */ Context a;
    final /* synthetic */ int b;

    e(Context context, int i) {
        this.a = context;
        this.b = i;
    }

    public void run() {
        long currentTimeMillis = System.currentTimeMillis();
        do {
            if (System.currentTimeMillis() - currentTimeMillis > 3000) {
                a.a(this.a);
                a.a(this.b, this.a);
            }
        } while (!a.v.isInterrupted());
    }
}
