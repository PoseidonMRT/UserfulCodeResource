package com.huawei.hwid.openapi.auth;

import android.webkit.WebChromeClient;
import android.webkit.WebView;

class w extends WebChromeClient {
    final /* synthetic */ WebAuthorizationActivity a;

    w(WebAuthorizationActivity webAuthorizationActivity) {
        this.a = webAuthorizationActivity;
    }

    public void onProgressChanged(WebView webView, int i) {
        if (i == 100) {
            if (this.a.f != null) {
                this.a.f.setVisibility(8);
            }
        } else if (this.a.f != null) {
            this.a.f.setVisibility(0);
            this.a.f.setProgress(i);
        }
    }
}
