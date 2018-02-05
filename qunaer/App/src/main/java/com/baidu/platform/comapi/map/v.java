package com.baidu.platform.comapi.map;

import android.os.Handler;
import android.os.Message;

class v extends Handler {
    final /* synthetic */ u a;

    v(u uVar) {
        this.a = uVar;
    }

    public void handleMessage(Message message) {
        super.handleMessage(message);
        if (u.c != null) {
            this.a.d.a(message);
        }
    }
}
