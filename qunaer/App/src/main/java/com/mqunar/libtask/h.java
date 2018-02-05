package com.mqunar.libtask;

class h implements Runnable {
    final /* synthetic */ Runnable a;
    final /* synthetic */ g b;

    h(g gVar, Runnable runnable) {
        this.b = gVar;
        this.a = runnable;
    }

    public void run() {
        try {
            this.a.run();
        } finally {
            this.b.a();
        }
    }
}
