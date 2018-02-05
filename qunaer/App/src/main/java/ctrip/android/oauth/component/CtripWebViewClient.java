package ctrip.android.oauth.component;

import android.webkit.WebViewClient;

abstract class CtripWebViewClient extends WebViewClient {
    protected CtripWebBrowserRequestCallBack mCallBack;

    CtripWebViewClient() {
    }

    public void setBrowserRequestCallBack(CtripWebBrowserRequestCallBack ctripWebBrowserRequestCallBack) {
        this.mCallBack = ctripWebBrowserRequestCallBack;
    }
}
