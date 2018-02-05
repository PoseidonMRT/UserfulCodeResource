package com.mqunar.cock.network;

class j implements Runnable {
    final /* synthetic */ i a;

    j(i iVar) {
        this.a = iVar;
    }

    public void run() {
        this.a.a.c.compareAndSet(true, false);
        OneKeyCremation.getInstance().oneKeyCremation();
    }
}
