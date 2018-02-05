package com.mqunar.splash;

import android.graphics.Bitmap;
import android.net.Uri;
import android.net.http.SslError;
import android.webkit.SslErrorHandler;
import android.webkit.ValueCallback;
import android.webkit.WebView;

public interface IWebViewObserver {
    void onPageFinished(WebView webView, String str);

    void onPageStarted(WebView webView, String str, Bitmap bitmap);

    void onProgressChanged(WebView webView, int i);

    void onReceivedError(WebView webView, int i, String str, String str2);

    void onReceivedSslError(WebView webView, SslErrorHandler sslErrorHandler, SslError sslError);

    void onReceivedTitle(WebView webView, String str);

    void openFileChooser(ValueCallback<Uri> valueCallback);

    void openFileChooser(ValueCallback<Uri> valueCallback, String str);

    void openFileChooser(ValueCallback<Uri> valueCallback, String str, String str2);

    boolean shouldOverrideUrlLoading(WebView webView, String str);
}
