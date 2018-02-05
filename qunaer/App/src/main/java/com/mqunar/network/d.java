package com.mqunar.network;

class d extends Thread {
    final /* synthetic */ NetRequest a;

    d(NetRequest netRequest) {
        this.a = netRequest;
    }

    public void run() {
        super.run();
        try {
            this.a.c.abort();
        } catch (Throwable th) {
        }
    }
}
