package com.mqunar.splash;

import android.annotation.TargetApi;
import android.graphics.Bitmap;
import android.net.http.SslError;
import android.webkit.SslErrorHandler;
import android.webkit.WebView;
import com.mqunar.core.basectx.webview.QWebViewClient;

@TargetApi(8)
public class WebViewClientSDK8 extends QWebViewClient {
    private IWebViewObserver mCallback;

    public WebViewClientSDK8() {
        this.mCallback = null;
    }

    public WebViewClientSDK8(IWebViewObserver iWebViewObserver) {
        this.mCallback = iWebViewObserver;
    }

    public boolean shouldOverrideUrlLoading(WebView webView, String str) {
        if (this.mCallback != null) {
            return this.mCallback.shouldOverrideUrlLoading(webView, str);
        }
        return super.shouldOverrideUrlLoading(webView, str);
    }

    public void onPageStarted(WebView webView, String str, Bitmap bitmap) {
        if (this.mCallback != null) {
            this.mCallback.onPageStarted(webView, str, bitmap);
        } else {
            super.onPageStarted(webView, str, bitmap);
        }
    }

    public void onPageFinished(WebView webView, String str) {
        if (this.mCallback != null) {
            this.mCallback.onPageFinished(webView, str);
        } else {
            super.onPageFinished(webView, str);
        }
    }

    public void onReceivedError(WebView webView, int i, String str, String str2) {
        if (this.mCallback != null) {
            this.mCallback.onReceivedError(webView, i, str, str2);
        } else {
            super.onReceivedError(webView, i, str, str2);
        }
    }

    public void onReceivedSslError(WebView webView, SslErrorHandler sslErrorHandler, SslError sslError) {
        sslErrorHandler.proceed();
        if (this.mCallback != null) {
            this.mCallback.onReceivedSslError(webView, sslErrorHandler, sslError);
        }
    }
}
