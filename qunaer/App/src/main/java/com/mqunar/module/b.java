package com.mqunar.module;

import android.content.Context;
import android.content.Intent;

class b extends Thread {
    final /* synthetic */ Context a;
    final /* synthetic */ Intent b;
    final /* synthetic */ a c;

    b(a aVar, Context context, Intent intent) {
        this.c = aVar;
        this.a = context;
        this.b = intent;
    }

    public void run() {
        this.c.a(this.a, this.b);
    }
}
