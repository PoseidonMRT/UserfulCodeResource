package com.mqunar.splash;

import android.app.Activity;
import android.os.Handler;
import android.webkit.JavascriptInterface;
import com.mqunar.tools.log.QLog;

public class SplashJavascriptObject {
    private Activity activity;
    private Handler handler;

    public SplashJavascriptObject(Activity activity, Handler handler) {
        this.activity = activity;
        this.handler = handler;
    }

    @JavascriptInterface
    public void finish() {
        this.activity.finish();
    }

    @JavascriptInterface
    public void removeToHomeAction() {
        QLog.e("SplashJavascriptObject", "removeToHomeAction", new Object[0]);
        this.handler.removeMessages(1);
    }
}
