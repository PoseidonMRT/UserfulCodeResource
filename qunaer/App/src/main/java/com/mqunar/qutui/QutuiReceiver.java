package com.mqunar.qutui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

public class QutuiReceiver extends BroadcastReceiver {
    private final String a = "com.mqunar.atom.qutui.ACTION_MESSAGE_TRANSPARENT";
    private final String b = "com.mqunar.atom.qutui.OBJ_MESSAGE";

    public void onReceive(Context context, Intent intent) {
        if ("com.mqunar.atom.qutui.ACTION_MESSAGE_TRANSPARENT".equals(intent.getAction())) {
            Bundle extras = intent.getExtras();
            if (extras != null) {
                Object string = extras.getString("com.mqunar.atom.qutui.OBJ_MESSAGE");
                if (!TextUtils.isEmpty(string)) {
                    PushManager.getInstance().dispatchMessage(string);
                }
            }
        }
    }
}
