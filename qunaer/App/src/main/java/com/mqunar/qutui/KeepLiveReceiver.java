package com.mqunar.qutui;

import android.content.Context;
import android.content.Intent;
import com.mqunar.core.basectx.receiver.QBroadcastReceiver;
import com.mqunar.tools.log.QLog;

public class KeepLiveReceiver extends QBroadcastReceiver {
    private final String a = "KeepLiveReceiver";

    public void onReceive(Context context, Intent intent) {
        QLog.d("KeepLiveReceiver", "onreceive" + intent.getAction(), new Object[0]);
        String action = intent.getAction();
        if (action.equals("android.intent.action.SCREEN_OFF")) {
            Intent intent2 = new Intent(context, KActivity.class);
            intent2.addFlags(268435456);
            context.startActivity(intent2);
            QLog.d("KeepLiveReceiver", "-------screen off", new Object[0]);
        } else if (action.equals("android.intent.action.SCREEN_ON")) {
            context.sendBroadcast(new Intent("finish activity"));
            QLog.d("KeepLiveReceiver", "------screen on", new Object[0]);
        }
    }
}
