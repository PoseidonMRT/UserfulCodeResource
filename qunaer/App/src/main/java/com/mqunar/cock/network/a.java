package com.mqunar.cock.network;

import com.mqunar.tools.log.QLog;
import com.mqunar.yacca.Msg.OnMsg;
import java.util.Iterator;

class a implements OnMsg {
    final /* synthetic */ CockHandler a;

    a(CockHandler cockHandler) {
        this.a = cockHandler;
    }

    public void onMessage(byte[] bArr, int i, byte[] bArr2) {
        OneKeyCremation.getInstance().getHandler().post(new b(this, bArr, bArr2));
    }

    public void onConnected() {
        Iterator it = this.a.i.iterator();
        while (it.hasNext()) {
            ((ImListener) it.next()).onConnected();
        }
        QLog.d("CockHandler", "onconnected", new Object[0]);
        this.a.r.onConnected();
    }
}
