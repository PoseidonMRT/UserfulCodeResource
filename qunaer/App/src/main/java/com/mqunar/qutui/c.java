package com.mqunar.qutui;

import android.os.IBinder;
import com.mqunar.cock.network.CockHandler;
import com.mqunar.cock.network.OneKeyCremation;
import com.mqunar.qutui.IQutuiInterface.Stub;
import com.mqunar.tools.log.QLog;

class c extends Stub {
    final /* synthetic */ NetService a;

    c(NetService netService) {
        this.a = netService;
    }

    public IBinder asBinder() {
        return null;
    }

    public void registerCallBack(OnIMMessage onIMMessage) {
        QLog.d("NetService", "IQutuiInterface registerCallBack", new Object[0]);
        this.a.g.register(onIMMessage);
    }

    public void unregisterCallBack(OnIMMessage onIMMessage) {
        QLog.d("NetService", "IQutuiInterface unregisterCallBack", new Object[0]);
        this.a.g.unregister(onIMMessage);
    }

    public void sendImMessage(byte[] bArr) {
        QLog.d("NetService", "IQutuiInterface sendImMessage", new Object[0]);
        CockHandler.getInstance().sendRequest(bArr, "OChat");
    }

    public boolean oneKeyCremation() {
        QLog.d("NetService", "IQutuiInterface oneKeyCremation", new Object[0]);
        return OneKeyCremation.getInstance().oneKeyCremation();
    }
}
