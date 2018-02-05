package com.mqunar.splash;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Build.VERSION;
import android.util.AttributeSet;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.DownloadListener;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import com.iflytek.aiui.AIUIConstant;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

public class QWebView extends WebView {
    private static int appCacheMaxSize = 8388608;
    private QWebViewConfigure configure;
    private boolean isDestroy = false;

    public class QCookie {
        public String domain;
        public String key;
        public String value;
    }

    public class QWebViewConfigure {
        public boolean isAllowFileAccess = true;
        public boolean isAppCacheEnabled = true;
        public boolean isBuiltInZoomControls = false;
        public boolean isDomStorageEnable = true;
        public boolean isJavaScriptEnable = true;
        public boolean isSavePassword = false;
        public boolean isSupportZoom = true;
        public WebChromeClient mWebChromeClient;
        public WebViewClient mWebViewClient;

        public QWebViewConfigure(IWebViewObserver iWebViewObserver) {
            if (iWebViewObserver != null) {
                this.mWebViewClient = new WebViewClientSDK8(iWebViewObserver);
                this.mWebChromeClient = new WebChromeClientSDK8(iWebViewObserver);
                return;
            }
            this.mWebViewClient = new WebViewClientSDK8();
            this.mWebChromeClient = new WebChromeClientSDK8();
        }
    }

    public QWebView(Context context) {
        super(context);
        init();
    }

    public QWebView(Context context, QWebViewConfigure qWebViewConfigure) {
        super(context);
        this.configure = qWebViewConfigure;
        init();
    }

    public QWebView(Context context, IWebViewObserver iWebViewObserver) {
        super(context);
        this.configure = new QWebViewConfigure(iWebViewObserver);
        init();
    }

    public QWebView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        init();
    }

    private void init() {
        if (this.configure == null) {
            this.configure = new QWebViewConfigure(null);
        }
        getSettings().setSavePassword(this.configure.isSavePassword);
        getSettings().setSupportZoom(this.configure.isSupportZoom);
        getSettings().setJavaScriptEnabled(this.configure.isJavaScriptEnable);
        getSettings().setDomStorageEnabled(this.configure.isDomStorageEnable);
        getSettings().setAppCacheMaxSize((long) appCacheMaxSize);
        getSettings().setAllowFileAccess(this.configure.isAllowFileAccess);
        getSettings().setAppCacheEnabled(this.configure.isAppCacheEnabled);
        getSettings().setBuiltInZoomControls(this.configure.isBuiltInZoomControls);
        if (getContext().getCacheDir() != null) {
            getSettings().setAppCachePath(getContext().getCacheDir().getAbsolutePath());
        }
        setDownloadListener(new DownloadListener() {
            public void onDownloadStart(String str, String str2, String str3, String str4, long j) {
                QWebView.this.getContext().startActivity(new Intent("android.intent.action.VIEW", Uri.parse(str)));
            }
        });
        setWebViewClient(this.configure.mWebViewClient);
        setWebChromeClient(this.configure.mWebChromeClient);
        setDownloadListener(new DownloadListener() {
            public void onDownloadStart(String str, String str2, String str3, String str4, long j) {
                Intent intent = new Intent("android.intent.action.VIEW", Uri.parse(str));
                String str5 = null;
                boolean z = false;
                for (ResolveInfo resolveInfo : QWebView.this.getContext().getPackageManager().queryIntentActivities(intent, 0)) {
                    String str6 = resolveInfo.activityInfo.packageName;
                    try {
                        z = resolveInfo.getClass().getDeclaredField(AIUIConstant.AUDIO_CAPTOR_SYSTEM).getBoolean(resolveInfo);
                    } catch (Exception e) {
                    }
                    if (z) {
                        intent.setPackage(str6);
                        QWebView.this.getContext().startActivity(intent);
                        ((Activity) QWebView.this.getContext()).overridePendingTransition(0, 0);
                        ((Activity) QWebView.this.getContext()).finish();
                        ((Activity) QWebView.this.getContext()).overridePendingTransition(0, 0);
                        return;
                    }
                    String str7 = resolveInfo.activityInfo.name;
                    String str8 = "browser";
                    str8 = "chrome";
                    if (str7.contains("browser") || str7.contains("chrome") || str6.contains("browser") || str6.contains("chrome")) {
                        str7 = str6;
                    } else {
                        str7 = str5;
                    }
                    str5 = str7;
                }
                if (str5 != null) {
                    intent.setPackage(str5);
                }
                QWebView.this.getContext().startActivity(intent);
                ((Activity) QWebView.this.getContext()).overridePendingTransition(0, 0);
                ((Activity) QWebView.this.getContext()).finish();
                ((Activity) QWebView.this.getContext()).overridePendingTransition(0, 0);
            }
        });
    }

    public void setObserver(IWebViewObserver iWebViewObserver) {
        QWebViewConfigure qWebViewConfigure = new QWebViewConfigure(iWebViewObserver);
        setWebViewClient(qWebViewConfigure.mWebViewClient);
        setWebChromeClient(qWebViewConfigure.mWebChromeClient);
    }

    public void loadUrl(String str) {
        if (!this.isDestroy) {
            super.loadUrl(str);
        }
    }

    public void postUrl(String str, byte[] bArr) {
        super.postUrl(str, bArr);
    }

    protected void onDetachedFromWindow() {
        if (!this.isDestroy) {
            super.onDetachedFromWindow();
        }
    }

    @TargetApi(8)
    public void loadUrl(String str, Map<String, String> map) {
        if (!this.isDestroy) {
            super.loadUrl(str, map);
        }
    }

    public void destroy() {
        this.isDestroy = true;
        super.destroy();
    }

    public void addCookie(String str, String str2, String str3) {
        CookieSyncManager.createInstance(getContext());
        CookieManager instance = CookieManager.getInstance();
        instance.setAcceptCookie(true);
        instance.setCookie(str, str2 + "=" + str3 + "; domain=" + str);
        CookieSyncManager.getInstance().sync();
    }

    public void removeCookie(String str, String str2) {
        CookieSyncManager.createInstance(getContext());
        CookieManager instance = CookieManager.getInstance();
        instance.setAcceptCookie(true);
        instance.setCookie(str, str2 + "=; domain=" + str);
        CookieSyncManager.getInstance().sync();
    }

    public void setCookie(String str, String str2, String str3) {
        addCookie(str, str2, str3);
    }

    public void setCookieList(ArrayList<QCookie> arrayList) {
        if (arrayList != null) {
            CookieSyncManager.createInstance(getContext());
            CookieManager instance = CookieManager.getInstance();
            instance.setAcceptCookie(true);
            Iterator it = arrayList.iterator();
            while (it.hasNext()) {
                QCookie qCookie = (QCookie) it.next();
                instance.setCookie(qCookie.domain, qCookie.key + "=" + qCookie.value + "; domain=" + qCookie.domain);
            }
            CookieSyncManager.getInstance().sync();
        }
    }

    @TargetApi(11)
    public void onWebViewResume() {
        if (VERSION.SDK_INT < 11) {
            try {
                QWebView.class.getMethod("onResume", new Class[0]).invoke(this, (Object[]) null);
                return;
            } catch (Throwable th) {
                return;
            }
        }
        super.onResume();
    }

    @TargetApi(11)
    public void onWebViewPause() {
        if (VERSION.SDK_INT < 11) {
            try {
                QWebView.class.getMethod("onPause", new Class[0]).invoke(this, (Object[]) null);
                return;
            } catch (Throwable th) {
                return;
            }
        }
        super.onPause();
    }
}
