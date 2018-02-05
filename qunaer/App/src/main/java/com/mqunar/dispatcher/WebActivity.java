package com.mqunar.dispatcher;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import com.mqunar.core.basectx.activity.QActivity;
import com.mqunar.splash.QWebView;

public class WebActivity extends QActivity {
    protected boolean needOnCreateSendBroadcast() {
        return false;
    }

    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        if (bundle == null) {
            bundle = getIntent().getExtras();
        }
        if (bundle == null || bundle.isEmpty()) {
            finish();
            return;
        }
        Object string = bundle.getString("url");
        if (TextUtils.isEmpty(string)) {
            finish();
            return;
        }
        View qWebView = new QWebView(this);
        setContentView(qWebView);
        qWebView.loadUrl(string);
    }
}
