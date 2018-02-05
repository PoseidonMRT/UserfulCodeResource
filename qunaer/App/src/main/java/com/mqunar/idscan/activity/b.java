package com.mqunar.idscan.activity;

import android.view.SurfaceHolder;

final class b implements Runnable {
    final /* synthetic */ SurfaceHolder a;
    final /* synthetic */ a b;

    b(a aVar, SurfaceHolder surfaceHolder) {
        this.b = aVar;
        this.a = surfaceHolder;
    }

    public final void run() {
        this.b.a(this.a);
    }
}
