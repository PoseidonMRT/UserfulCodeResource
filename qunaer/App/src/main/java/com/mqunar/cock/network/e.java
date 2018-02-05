package com.mqunar.cock.network;

import com.mqunar.cock.model.BaseResult;

class e implements Runnable {
    final /* synthetic */ BaseResult a;
    final /* synthetic */ GenericNetworkTaskCallback b;

    e(GenericNetworkTaskCallback genericNetworkTaskCallback, BaseResult baseResult) {
        this.b = genericNetworkTaskCallback;
        this.a = baseResult;
    }

    public void run() {
        this.b.onDataArrive(this.a);
    }
}
