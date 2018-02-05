package com.mqunar.cock.network;

import com.mqunar.tools.log.QLog;
import com.mqunar.yacca.YaccaBridge;
import com.mqunar.yacca.YaccaCallback;
import java.util.Iterator;

class c implements YaccaCallback {
    final /* synthetic */ CockHandler a;

    c(CockHandler cockHandler) {
        this.a = cockHandler;
    }

    public void onOpen(YaccaBridge yaccaBridge) {
        QLog.d("CockHandler", "ONOPEN", new Object[0]);
    }

    public void onClose(YaccaBridge yaccaBridge) {
        Iterator it = this.a.i.iterator();
        while (it.hasNext()) {
            ((ImListener) it.next()).onClosed();
        }
        QLog.d("CockHandler", "onClose", new Object[0]);
        if (this.a.r != null) {
            this.a.r.onNetEnd(((d) yaccaBridge).a);
        }
        this.a.u = false;
    }
}
