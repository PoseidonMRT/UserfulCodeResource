package com.huawei.hwid.openapi.auth;

import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.huawei.android.app.ActionBarEx;
import com.huawei.hwid.openapi.a.a;
import com.huawei.hwid.openapi.a.b;
import com.huawei.hwid.openapi.out.OutReturn;
import com.huawei.hwid.openapi.out.OutReturn.ParamStr;
import com.huawei.hwid.openapi.quicklogin.d.a.c;
import com.huawei.hwid.openapi.quicklogin.d.b.d;
import com.huawei.hwid.openapi.quicklogin.datatype.e;

public class WebAuthorizationActivity extends Activity {
    private static final String a = b.a;
    private String b = null;
    private WebView c;
    private ImageView d;
    private Button e;
    private ProgressBar f;
    private a g;
    private RelativeLayout h;
    private RelativeLayout i;
    private TextView j;
    private boolean k = false;
    private boolean l = false;
    private boolean m = false;
    private ImageView n;
    private ImageView o;
    private Handler p = new v(this);

    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        this.b = getIntent().getStringExtra("AUTH_URL");
        this.g = com.huawei.hwid.openapi.quicklogin.a.a().d();
        b();
        a(this.b);
        c();
        d();
    }

    private void b() {
        if (com.huawei.hwid.openapi.quicklogin.d.a.b.a("com.huawei.android.app.ActionBarEx")) {
            setContentView(com.huawei.hwid.openapi.quicklogin.d.b.c(this, "default_webview"));
            ((LinearLayout) findViewById(com.huawei.hwid.openapi.quicklogin.d.b.d(this, "top_view"))).setVisibility(8);
            ActionBar actionBar = getActionBar();
            actionBar.setDisplayHomeAsUpEnabled(false);
            actionBar.setTitle(com.huawei.hwid.openapi.quicklogin.d.b.a(this, "default_hwid_login"));
            ActionBarEx.setStartIcon(actionBar, true, null, new r(this));
            return;
        }
        requestWindowFeature(1);
        setContentView(com.huawei.hwid.openapi.quicklogin.d.b.c(this, "default_webview"));
    }

    private void c() {
        this.f = (ProgressBar) findViewById(com.huawei.hwid.openapi.quicklogin.d.b.d(this, "area_webview_progress_bar"));
        this.d = (ImageView) findViewById(com.huawei.hwid.openapi.quicklogin.d.b.d(this, "close_imageview"));
        this.h = (RelativeLayout) findViewById(com.huawei.hwid.openapi.quicklogin.d.b.d(this, "default_requset_err_layout"));
        this.i = (RelativeLayout) findViewById(com.huawei.hwid.openapi.quicklogin.d.b.d(this, "default_network_failed_layout"));
        this.j = (TextView) findViewById(com.huawei.hwid.openapi.quicklogin.d.b.d(this, "default_err_description"));
        this.o = (ImageView) findViewById(com.huawei.hwid.openapi.quicklogin.d.b.d(this, "default_err_network_failed_image"));
        this.n = (ImageView) findViewById(com.huawei.hwid.openapi.quicklogin.d.b.d(this, "default_err_image"));
        e();
        this.e = (Button) findViewById(com.huawei.hwid.openapi.quicklogin.d.b.d(this, "default_err_btn_network_setting"));
        if (this.d != null) {
            this.d.setOnClickListener(new s(this));
        }
        if (this.e != null) {
            this.e.setOnClickListener(new t(this));
        }
        if (this.i != null) {
            this.i.setOnClickListener(new u(this));
        }
    }

    private void a(String str) {
        boolean z = str != null && str.contains("sso_st");
        this.k = z;
    }

    private void d() {
        c.b(a, "startWebAuth");
        h();
        this.c.loadUrl(this.b);
    }

    public void onBackPressed() {
        try {
            if (this.c != null) {
                this.c.stopLoading();
            }
            if (this.c == null || !this.c.canGoBack()) {
                f();
                super.onBackPressed();
                return;
            }
            this.c.goBack();
        } catch (Throwable e) {
            d.b(a, e.getMessage(), e);
            this.g.b.finish(OutReturn.creatRunTimeErrRet(e.toString()));
            a(null, true);
        }
    }

    private void a(Bundle bundle, boolean z) {
        e eVar = new e(this.g.a.getApplicationContext(), this.k ? "101" : "103");
        eVar.a(System.currentTimeMillis());
        if (z) {
            eVar.b("cancel");
            eVar.a("no_user");
            eVar.c("cancel");
        } else if (OutReturn.isRequestSuccess(bundle)) {
            eVar.b("");
            eVar.a("no_user");
            eVar.c("success");
        } else if (bundle == null) {
            eVar.b("0123456789");
            eVar.a("no_user");
            eVar.c("0123456789");
        } else {
            eVar.b(String.valueOf(OutReturn.getRetCode(bundle)));
            eVar.a(bundle.getString(ParamStr.Err_Info));
            eVar.c(ParamStr.RET_RES_ERROR);
        }
        com.huawei.hwid.openapi.quicklogin.d.a.d.a().b(this.g.a.getApplicationContext(), eVar);
    }

    private void e() {
        if (com.huawei.hwid.openapi.quicklogin.d.a.b.e()) {
            if (this.o != null) {
                this.o.setBackgroundResource(com.huawei.hwid.openapi.quicklogin.d.b.f(this, "ql_network_fail"));
            }
            if (this.n != null) {
                this.n.setBackgroundResource(com.huawei.hwid.openapi.quicklogin.d.b.f(this, "ql_default_error"));
                return;
            }
            return;
        }
        if (this.o != null) {
            this.o.setBackgroundResource(com.huawei.hwid.openapi.quicklogin.d.b.f(this, "default_err_network_failed"));
        }
        if (this.n != null) {
            this.n.setBackgroundResource(com.huawei.hwid.openapi.quicklogin.d.b.f(this, "default_err_request_failed"));
        }
    }

    private String a(Message message) {
        int i = message.what;
        String str = "";
        d.a(a, "retCode" + OutReturn.getRetResErrCode(message.getData()));
        if (i == 2 || i == 1) {
            return getString(com.huawei.hwid.openapi.quicklogin.d.b.a(this, "default_err_conn_server_failed"));
        }
        if (i == 3) {
            return getString(com.huawei.hwid.openapi.quicklogin.d.b.a(this, "default_err_webview_req_failed"));
        }
        return str;
    }

    private void f() {
        if (this.c != null) {
            this.c.stopLoading();
        }
        this.g.b.finish(OutReturn.creatReturn(2, "user cancel the operation"));
        a(null, true);
        finish();
    }

    private void g() {
        d.b(a, "retryLoading");
        this.i.setVisibility(4);
        this.e.setVisibility(4);
        this.h.setVisibility(4);
    }

    private void h() {
        c.b(a, "setUpWebView");
        this.c = (WebView) findViewById(com.huawei.hwid.openapi.quicklogin.d.b.d(this, "activity_area_webview"));
        this.c.setVerticalScrollBarEnabled(false);
        this.c.setHorizontalScrollBarEnabled(false);
        this.c.getSettings().setJavaScriptEnabled(true);
        this.c.getSettings().setSavePassword(false);
        this.c.removeJavascriptInterface("searchBoxJavaBridge_");
        this.c.removeJavascriptInterface("accessibility");
        this.c.removeJavascriptInterface("accessibilityTraversal");
        this.c.setWebViewClient(new n(getApplicationContext(), this.b, this.p));
        this.c.setWebChromeClient(new w(this));
    }
}
