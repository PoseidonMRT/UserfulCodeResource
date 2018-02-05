package com.mqunar.libtask;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

class f extends Handler {
    public f() {
        super(Looper.getMainLooper());
    }

    public void handleMessage(Message message) {
        e eVar = (e) message.obj;
        switch (message.what) {
            case 1:
                eVar.a.finish(eVar.b[0]);
                return;
            case 2:
                eVar.a.onProgressUpdate(eVar.b);
                return;
            default:
                return;
        }
    }
}
