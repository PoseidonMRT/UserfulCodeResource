package com.mqunar.spider;

class g extends Thread {
    final /* synthetic */ QunarApp a;

    g(QunarApp qunarApp) {
        this.a = qunarApp;
    }

    public void run() {
        super.run();
        this.a.clearStack();
    }
}
