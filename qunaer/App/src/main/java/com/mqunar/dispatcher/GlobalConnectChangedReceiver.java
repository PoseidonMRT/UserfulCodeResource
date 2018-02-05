package com.mqunar.dispatcher;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;

public class GlobalConnectChangedReceiver extends BroadcastReceiver {
    public static final String ACTION_MESSAGE_CONNECT_STATE = "com.mqunar.dispatcher.MESSAGE_CONNECT_STATE";

    public void onReceive(Context context, Intent intent) {
        if ("android.net.conn.CONNECTIVITY_CHANGE".equals(intent.getAction())) {
            Intent intent2 = new Intent(intent);
            intent2.setAction("com.mqunar.dispatcher.MESSAGE_CONNECT_STATE");
            LocalBroadcastManager.getInstance(context).sendBroadcast(intent2);
        }
    }
}
