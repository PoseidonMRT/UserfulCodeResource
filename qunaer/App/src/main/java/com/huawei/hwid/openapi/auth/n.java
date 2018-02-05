package com.huawei.hwid.openapi.auth;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.webkit.SslErrorHandler;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import com.huawei.hwid.openapi.out.OutReturn;
import com.huawei.hwid.openapi.out.OutReturn.ParamStr;
import com.huawei.hwid.openapi.quicklogin.b.b.b.a;
import com.huawei.hwid.openapi.quicklogin.d.a.b;
import com.huawei.hwid.openapi.quicklogin.d.b.d;
import com.huawei.hwid.openapi.quicklogin.d.b.k;
import java.net.URL;

public class n extends WebViewClient {
    private Context a;
    private String b;
    private Handler c;
    private boolean d;
    private o e = new o(this);

    public n(Context context, String str, Handler handler) {
        this.a = context;
        this.b = str;
        this.c = handler;
    }

    public boolean shouldOverrideUrlLoading(WebView webView, String str) {
        d.b("OpenSDKWebViewClient", "shouldOverrideUrlLoading");
        if (a(str, this.b)) {
            Bundle d = a.d(str);
            Message message = new Message();
            message.setData(d);
            if (!d.containsKey(ParamStr.ACCESS_TOKEN) || d.containsKey(ParamStr.RET_RES_ERROR)) {
                message.what = 3;
            } else {
                message.what = 0;
            }
            this.d = true;
            this.c.sendMessage(message);
            return true;
        }
        this.c.sendEmptyMessage(-2);
        webView.loadUrl(str);
        return false;
    }

    public void onReceivedSslError(WebView webView, SslErrorHandler sslErrorHandler, SslError sslError) {
        d.b("OpenSDKWebViewClient", "onReceivedSslError and not test version");
        super.onReceivedSslError(webView, sslErrorHandler, sslError);
        Message message = new Message();
        message.what = 2;
        message.setData(OutReturn.createOpenGwSSLErr());
        this.c.sendMessage(message);
    }

    public void onPageStarted(WebView webView, String str, Bitmap bitmap) {
        if (!b.a(this.a)) {
            this.c.sendEmptyMessage(-1);
            webView.stopLoading();
        }
        if (webView != null && !TextUtils.isEmpty(webView.getUrl()) && !webView.getUrl().equalsIgnoreCase("null")) {
            d.b("OpenSDKWebViewClient", "onPageStarted:" + k.b(str));
            super.onPageStarted(webView, str, bitmap);
            this.e.a(webView);
        }
    }

    public void onPageFinished(WebView webView, String str) {
        d.b("OpenSDKWebViewClient", "onPageFinished:" + k.b(str));
        super.onPageFinished(webView, str);
        this.e.b(webView);
    }

    private boolean a(String str, String str2) {
        if (TextUtils.isEmpty(str) || TextUtils.isEmpty(str2)) {
            return false;
        }
        try {
            String string = a.d(str2).getString("redirect_uri");
            d.a("OpenSDKWebViewClient", "reUrl:" + k.b(string) + " tokenRspUrl:" + k.b(str), null);
            if ("oob".equals(string)) {
                return str.startsWith("https://login.vmall.com/oauth2/oob#");
            }
            URL url = new URL(string);
            string = url.getHost();
            d.b("OpenSDKWebViewClient", "tokenRspUrl:" + k.b(str) + " compareUrl:" + url.getProtocol() + "://" + string, null);
            return str.startsWith(url.getProtocol() + "://" + string);
        } catch (Throwable e) {
            d.b("OpenSDKWebViewClient", e.getMessage(), e);
            return false;
        }
    }
}
