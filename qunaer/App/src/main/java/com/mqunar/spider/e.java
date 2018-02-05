package com.mqunar.spider;

import qunar.lego.utils.Pitcher;

class e extends Thread {
    final /* synthetic */ Pitcher a;
    final /* synthetic */ d b;

    e(d dVar, Pitcher pitcher) {
        this.b = dVar;
        this.a = pitcher;
    }

    public void run() {
        this.a.request();
    }
}
