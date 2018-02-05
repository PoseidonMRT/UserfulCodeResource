package com.mqunar.cock.network;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import com.mqunar.cock.model.TcpRequest;
import com.mqunar.cock.model.TcpResult;
import com.mqunar.cock.network.callback.TcpMessageCallBack;
import com.mqunar.tools.ArrayUtils;

class g extends Handler {
    final /* synthetic */ OneKeyCremation a;

    g(OneKeyCremation oneKeyCremation, Looper looper) {
        this.a = oneKeyCremation;
        super(looper);
    }

    public void handleMessage(Message message) {
        if (message.what != 257) {
            if (message.what == 258) {
                this.a.a(1);
                if (ArrayUtils.isEmpty(this.a.h)) {
                    this.a.setState(this.a.yaccaListUnavailableState);
                } else {
                    this.a.setState(this.a.yaccaDisconnectState);
                }
            } else if (message.what == 259 && message.obj != null && (message.obj instanceof TcpResult)) {
                int i = ((TcpRequest) message.obj).reqid;
                if (this.a.g.containsKey(Integer.valueOf(i))) {
                    ((TcpMessageCallBack) this.a.g.get(Integer.valueOf(i))).onTimeOut((byte[]) message.obj);
                }
                this.a.f.remove(message.obj);
            }
        }
    }
}
