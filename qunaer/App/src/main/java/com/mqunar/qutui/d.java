package com.mqunar.qutui;

import com.mqunar.cock.network.ImListener;
import com.mqunar.tools.log.QLog;

class d implements ImListener {
    final /* synthetic */ NetService a;

    d(NetService netService) {
        this.a = netService;
    }

    public void onMessage(byte[] bArr) {
        QLog.d("NetService", "ImListener onMessage", new Object[0]);
        this.a.a(bArr);
    }

    public void onConnected() {
        QLog.d("NetService", "ImListener onConnected", new Object[0]);
        this.a.a();
    }

    public void onClosed() {
        QLog.d("NetService", "ImListener onClosed", new Object[0]);
        this.a.b();
    }
}
