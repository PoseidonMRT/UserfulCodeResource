package com.mqunar.cock.network;

import com.mqunar.libtask.AbsConductor;

class f implements Runnable {
    final /* synthetic */ AbsConductor a;
    final /* synthetic */ GenericNetworkTaskCallback b;

    f(GenericNetworkTaskCallback genericNetworkTaskCallback, AbsConductor absConductor) {
        this.b = genericNetworkTaskCallback;
        this.a = absConductor;
    }

    public void run() {
        this.b.onNetError(this.a);
    }
}
