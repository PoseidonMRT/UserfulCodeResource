package com.mqunar.qutui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

class b extends BroadcastReceiver {
    final /* synthetic */ KActivity a;

    b(KActivity kActivity) {
        this.a = kActivity;
    }

    public void onReceive(Context context, Intent intent) {
        this.a.finish();
    }
}
