package com.mqunar.cock.network.state;

import com.mqunar.cock.network.OneKeyCremation;
import com.mqunar.tools.log.QLog;

public class YaccaDisconnectState implements ChannelState {
    String a = "OnekeyCremation";

    public boolean oneKeyCremation() {
        QLog.d(this.a, "YaccaDisconnectState", new Object[0]);
        OneKeyCremation.getInstance().connect();
        return false;
    }
}
