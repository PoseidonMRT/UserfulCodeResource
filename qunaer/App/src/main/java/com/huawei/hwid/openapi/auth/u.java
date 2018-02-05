package com.huawei.hwid.openapi.auth;

import android.view.View;
import android.view.View.OnClickListener;
import com.huawei.hwid.openapi.quicklogin.d.a.b;

class u implements OnClickListener {
    final /* synthetic */ WebAuthorizationActivity a;

    u(WebAuthorizationActivity webAuthorizationActivity) {
        this.a = webAuthorizationActivity;
    }

    public void onClick(View view) {
        if (b.a(this.a.getApplicationContext())) {
            this.a.g();
            this.a.c.loadUrl(this.a.b);
        }
    }
}
