package com.mqunar.qutui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import com.mqunar.cock.network.OneKeyCremation;
import com.mqunar.core.basectx.application.QApplication;
import com.mqunar.tools.log.QLog;

public class NotificationReceiver extends BroadcastReceiver {
    public static final String tag = "NotificationReceiver";
    private Handler a = new Handler();
    private long b = 0;

    private boolean a() {
        try {
            NetworkInfo activeNetworkInfo = ((ConnectivityManager) QApplication.getContext().getSystemService("connectivity")).getActiveNetworkInfo();
            if (activeNetworkInfo != null) {
                return activeNetworkInfo.isAvailable();
            }
        } catch (Exception e) {
            QLog.e("CONNECTIVITY_SERVICE ERROR", new Object[0]);
        }
        return false;
    }

    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("android.intent.action.TIME_TICK")) {
            QLog.d(tag, "ACTION_TIME_TICK", new Object[0]);
            OneKeyCremation.getInstance().oneKeyCremation();
        } else if (intent.getAction().equals("com.mqunar.dispatcher.MESSAGE_CONNECT_STATE")) {
            QLog.d(tag, "ACTION_MESSAGE_CONNECT_STATE", new Object[0]);
            long currentTimeMillis = System.currentTimeMillis();
            long j = currentTimeMillis - this.b;
            this.b = currentTimeMillis;
            this.a.postDelayed(new e(this, j), 1500);
        }
    }
}
