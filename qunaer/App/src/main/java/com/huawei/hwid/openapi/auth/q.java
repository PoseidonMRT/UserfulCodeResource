package com.huawei.hwid.openapi.auth;

import com.huawei.hwid.openapi.quicklogin.d.b.d;

class q implements Runnable {
    final /* synthetic */ p a;

    q(p pVar) {
        this.a = pVar;
    }

    public void run() {
        if (this.a.a.b.getProgress() < 100) {
            d.a("OpenSDKWebViewClient", "timeout...........");
            this.a.a.b();
        }
    }
}
