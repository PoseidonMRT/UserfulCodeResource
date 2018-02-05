package com.huawei.hwid.openapi.auth;

import android.os.Message;
import android.webkit.WebView;
import com.huawei.hwid.openapi.out.OutReturn;
import com.huawei.hwid.openapi.quicklogin.d.b.d;
import java.util.Timer;

class o {
    Timer a = null;
    WebView b = null;
    final /* synthetic */ n c;

    public o(n nVar) {
        this.c = nVar;
    }

    public void a(WebView webView) {
        d.a("WebViewMonitor", "onPageStart");
        try {
            a();
            this.a = new Timer();
            this.b = webView;
            this.a.schedule(new p(this), 15000, 1);
        } catch (Throwable e) {
            d.b("WebViewMonitor", e.getMessage(), e);
            a();
        }
    }

    public void b(WebView webView) {
        d.a("WebViewMonitor", "onPageFinished");
        a();
    }

    public void a() {
        d.a("WebViewMonitor", "cancel");
        if (this.a != null) {
            this.a.cancel();
            this.a.purge();
            this.a = null;
        }
    }

    private void b() {
        try {
            a();
            if (!this.c.d) {
                Message message = new Message();
                message.what = 1;
                message.setData(OutReturn.createOpenGwTimeout());
                this.c.c.sendMessage(message);
            }
        } catch (Throwable th) {
            d.b("WebViewMonitor", th.getMessage(), th);
        }
    }
}
