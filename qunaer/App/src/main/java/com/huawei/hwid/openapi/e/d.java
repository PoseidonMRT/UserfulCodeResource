package com.huawei.hwid.openapi.e;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

class d extends Handler {
    final /* synthetic */ c a;

    d(c cVar, Looper looper) {
        this.a = cVar;
        super(looper);
    }

    public void handleMessage(Message message) {
        if (message.what == 1) {
            this.a.a();
        }
        super.handleMessage(message);
    }
}
