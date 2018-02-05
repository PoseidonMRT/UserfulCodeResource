package ctrip.android.oauth.component;

import android.app.Activity;
import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.Bundle;
import android.webkit.SslErrorHandler;
import android.webkit.WebView;
import ctrip.android.oauth.RequestProcessor;
import ctrip.android.oauth.RequestProcessor.Request;
import ctrip.android.oauth.utils.CtripApiUtils;

class CtripAuthWebViewClient extends CtripWebViewClient {
    private boolean isCallBacked = false;
    private Activity mAct;
    private CtripAuthRequestParam mAuthRequestParam;

    public CtripAuthWebViewClient(Activity activity, CtripAuthRequestParam ctripAuthRequestParam) {
        this.mAct = activity;
        this.mAuthRequestParam = ctripAuthRequestParam;
    }

    public void onPageStarted(WebView webView, String str, Bitmap bitmap) {
        if (this.mCallBack != null) {
            this.mCallBack.onPageStartedCallBack(webView, str, bitmap);
        }
        if (!str.startsWith(this.mAuthRequestParam.getAuthRequest().getRedirectUrl()) || this.isCallBacked) {
            super.onPageStarted(webView, str, bitmap);
            return;
        }
        this.isCallBacked = true;
        handleRedirectUrl(str);
        webView.stopLoading();
    }

    public boolean shouldOverrideUrlLoading(WebView webView, String str) {
        if (this.mCallBack != null) {
            this.mCallBack.shouldOverrideUrlLoadingCallBack(webView, str);
        }
        return super.shouldOverrideUrlLoading(webView, str);
    }

    public void onPageFinished(WebView webView, String str) {
        if (this.mCallBack != null) {
            this.mCallBack.onPageFinishedCallBack(webView, str);
        }
        super.onPageFinished(webView, str);
    }

    public void onReceivedError(WebView webView, int i, String str, String str2) {
        if (this.mCallBack != null) {
            this.mCallBack.onReceivedErrorCallBack(webView, i, str, str2);
        }
        super.onReceivedError(webView, i, str, str2);
    }

    public void onReceivedSslError(WebView webView, SslErrorHandler sslErrorHandler, SslError sslError) {
        if (this.mCallBack != null) {
            this.mCallBack.onReceivedSslErrorCallBack(webView, sslErrorHandler, sslError);
        }
        super.onReceivedSslError(webView, sslErrorHandler, sslError);
    }

    public void handleRedirectUrl(String str) {
        Bundle parseUrl = CtripApiUtils.parseUrl(str);
        String string = parseUrl.getString("state");
        String string2 = parseUrl.getString("code");
        Request request = new Request();
        Bundle bundle = new Bundle();
        bundle.putString("_ctripapi_sendauth_response_state", string);
        bundle.putString("_ctripapi_sendauth_response_code", string2);
        bundle.putInt("_ctripapi_command_type", this.mAuthRequestParam.getAuthRequest().getType());
        string = "_ctripapi_baseresponse_errcode";
        int i = (string2 == null || string2.isEmpty()) ? -1 : 0;
        bundle.putInt(string, i);
        bundle.putString("_ctripapi_baseresponse_errstr", "");
        request.bundle = bundle;
        request.messageContent = "";
        request.flags = 67108864;
        request.targetPackageName = this.mAuthRequestParam.getPackageName();
        request.targetClassName = this.mAuthRequestParam.getPackageName() + ".ctripapi.CtripEntryActivity";
        RequestProcessor.startActivity(this.mAct, request);
        this.mAct.finish();
    }
}
