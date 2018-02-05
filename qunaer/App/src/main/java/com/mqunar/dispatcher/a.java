package com.mqunar.dispatcher;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import com.mqunar.core.QSpider;

class a extends Handler {
    final /* synthetic */ DispatcherActivity a;

    a(DispatcherActivity dispatcherActivity, Looper looper) {
        this.a = dispatcherActivity;
        super(looper);
    }

    public void handleMessage(Message message) {
        super.handleMessage(message);
        if (QSpider.loadDone) {
            this.a.a();
        } else {
            sendEmptyMessageDelayed(0, 500);
        }
    }
}
