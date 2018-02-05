package com.mqunar.qutui;

import com.mqunar.qutui.OnIMMessage.Stub;
import com.mqunar.qutui.PushManager.OnImMessageListener;
import com.mqunar.tools.log.QLog;
import java.io.UnsupportedEncodingException;
import java.util.Iterator;

class h extends Stub {
    final /* synthetic */ PushManager a;

    h(PushManager pushManager) {
        this.a = pushManager;
    }

    public void onImMessage(byte[] bArr) {
        Iterator it = this.a.f.iterator();
        while (it.hasNext()) {
            ((OnImMessageListener) it.next()).onImMessage(bArr);
        }
        try {
            QLog.d("QunarImApp " + new String(bArr, "utf-8"), new Object[0]);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    public void onConnected() {
        QLog.d("PushManager", "onConnected", new Object[0]);
        Iterator it = this.a.f.iterator();
        while (it.hasNext()) {
            ((OnImMessageListener) it.next()).onConnected();
        }
    }

    public void onClosed() {
        QLog.d("PushManager", "onClosed", new Object[0]);
        Iterator it = this.a.f.iterator();
        while (it.hasNext()) {
            ((OnImMessageListener) it.next()).onClosed();
        }
    }
}
