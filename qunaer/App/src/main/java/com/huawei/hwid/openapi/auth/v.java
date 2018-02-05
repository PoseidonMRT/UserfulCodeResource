package com.huawei.hwid.openapi.auth;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import com.huawei.hwid.openapi.out.OutReturn;
import com.huawei.hwid.openapi.quicklogin.d.b.d;
import com.huawei.hwid.openapi.quicklogin.d.b.k;

class v extends Handler {
    final /* synthetic */ WebAuthorizationActivity a;

    v(WebAuthorizationActivity webAuthorizationActivity) {
        this.a = webAuthorizationActivity;
    }

    public void handleMessage(Message message) {
        super.handleMessage(message);
        if (message.what == 0) {
            Bundle data = message.getData();
            this.a.g.b.finish(OutReturn.addSuccessCode(data));
            this.a.a(OutReturn.addSuccessCode(data), false);
            this.a.finish();
        } else if (message.what == 1 || message.what == 3 || message.what == 2) {
            this.a.c.stopLoading();
            this.a.c.setVisibility(4);
            this.a.f.setVisibility(4);
            this.a.h.setVisibility(0);
            this.a.i.setVisibility(4);
            d.d(WebAuthorizationActivity.a, "server failed:" + k.a(message.getData()));
            this.a.j.setText(this.a.a(message));
            if (message.what == 1 || message.what == 2) {
                this.a.m = true;
            } else {
                this.a.m = false;
            }
        } else if (message.what == -1) {
            d.d(WebAuthorizationActivity.a, "no network");
            this.a.c.setVisibility(4);
            this.a.f.setVisibility(4);
            this.a.i.setVisibility(0);
            this.a.e.setVisibility(0);
            this.a.h.setVisibility(4);
            this.a.l = true;
        } else if (message.what != -2) {
        } else {
            if (this.a.l || this.a.m) {
                this.a.l = false;
                this.a.m = false;
                this.a.i.setVisibility(4);
                this.a.e.setVisibility(4);
                this.a.h.setVisibility(4);
                this.a.c.setVisibility(0);
                this.a.f.setVisibility(0);
            }
        }
    }
}
