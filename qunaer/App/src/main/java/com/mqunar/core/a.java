package com.mqunar.core;

import android.content.Context;
import android.content.IntentFilter;
import com.mqunar.atomenv.SwitchEnv;
import com.mqunar.dispatcher.GlobalConnectChangedReceiver;
import com.mqunar.module.ModuleInfoController;
import com.mqunar.network.NetRequestManager;
import com.mqunar.spider.GPushReceiver;
import com.mqunar.splash.SplashUtils;
import com.mqunar.tools.QPreExecuteTaskUtils;
import com.mqunar.tools.log.QLog;

final class a extends Thread {
    final /* synthetic */ Context a;

    a(Context context) {
        this.a = context;
    }

    public void run() {
        try {
            long currentTimeMillis = System.currentTimeMillis();
            ModuleInfoController.loadApk(this.a);
            QLog.i("initSpider", "loadApk:" + (System.currentTimeMillis() - currentTimeMillis) + "ms", new Object[0]);
            QPreExecuteTaskUtils.runAllTask();
            try {
                if (!SwitchEnv.getInstance().isShowNetTips()) {
                    NetRequestManager.getInstance().requestIpList(this.a);
                }
            } catch (Throwable th) {
            }
            Class.forName("com.mqunar.imagecache.ImageLoader").getDeclaredMethod("getInstance", new Class[]{Context.class}).invoke(null, new Object[]{this.a});
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
            this.a.registerReceiver(new GlobalConnectChangedReceiver(), intentFilter);
        } catch (Throwable th2) {
            QSpider.b(th2, this.a);
        } finally {
            QSpider.loadDone = true;
            SplashUtils.setLoadDone(true);
            GPushReceiver.check(this.a);
        }
    }
}
