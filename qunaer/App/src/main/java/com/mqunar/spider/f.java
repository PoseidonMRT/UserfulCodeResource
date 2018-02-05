package com.mqunar.spider;

import android.content.Intent;
import android.net.Uri;
import android.os.Build.VERSION;
import android.provider.Settings;
import android.widget.Toast;
import com.mqunar.core.basectx.application.QApplication;
import com.mqunar.dlogin.login.ILogin.LoginCallback;
import com.mqunar.dlogin.login.PlatformLogin;

class f implements Runnable {
    final /* synthetic */ PlatformLogin a;
    final /* synthetic */ LoginCallback b;
    final /* synthetic */ QAcraBetaSender c;

    f(QAcraBetaSender qAcraBetaSender, PlatformLogin platformLogin, LoginCallback loginCallback) {
        this.c = qAcraBetaSender;
        this.a = platformLogin;
        this.b = loginCallback;
    }

    public void run() {
        if (VERSION.SDK_INT < 23) {
            if (!this.a.internalWindow().isShowing()) {
                this.a.addCallback(this.b).show();
            }
            if (VERSION.SDK_INT >= 19 && !this.c.a(QApplication.getContext())) {
                Toast.makeText(QApplication.getContext(), "请开启 显示悬浮框 进行 qtalk登录哈！！！", 0).show();
            }
        } else if (!Settings.canDrawOverlays(this.c.a)) {
            Intent intent = new Intent("android.settings.action.MANAGE_OVERLAY_PERMISSION");
            intent.setData(Uri.parse("package:" + this.c.a.getPackageName()));
            this.c.a.startActivity(intent);
        } else if (!this.a.internalWindow().isShowing()) {
            this.a.addCallback(this.b).show();
        }
    }
}
