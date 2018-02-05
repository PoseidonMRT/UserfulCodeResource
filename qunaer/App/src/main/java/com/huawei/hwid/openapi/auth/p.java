package com.huawei.hwid.openapi.auth;

import java.util.TimerTask;

class p extends TimerTask {
    final /* synthetic */ o a;

    p(o oVar) {
        this.a = oVar;
    }

    public void run() {
        this.a.c.c.post(new q(this));
    }
}
