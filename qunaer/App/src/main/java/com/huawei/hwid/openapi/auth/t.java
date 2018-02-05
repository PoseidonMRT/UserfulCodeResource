package com.huawei.hwid.openapi.auth;

import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;

class t implements OnClickListener {
    final /* synthetic */ WebAuthorizationActivity a;

    t(WebAuthorizationActivity webAuthorizationActivity) {
        this.a = webAuthorizationActivity;
    }

    public void onClick(View view) {
        this.a.startActivity(new Intent("android.settings.SETTINGS"));
    }
}
