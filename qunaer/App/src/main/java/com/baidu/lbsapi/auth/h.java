package com.baidu.lbsapi.auth;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

class h extends Handler {
    final /* synthetic */ LBSAuthManager a;

    h(LBSAuthManager lBSAuthManager, Looper looper) {
        this.a = lBSAuthManager;
        super(looper);
    }

    public void handleMessage(Message message) {
        if (a.a) {
            a.a("handleMessage !!");
        }
        LBSAuthManagerListener lBSAuthManagerListener = (LBSAuthManagerListener) LBSAuthManager.f.get(message.getData().getString("listenerKey"));
        if (a.a) {
            a.a("handleMessage listener = " + lBSAuthManagerListener);
        }
        if (lBSAuthManagerListener != null) {
            lBSAuthManagerListener.onAuthResult(message.what, message.obj.toString());
        }
    }
}
