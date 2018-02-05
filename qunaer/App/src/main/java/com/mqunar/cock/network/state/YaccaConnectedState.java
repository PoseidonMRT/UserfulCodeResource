package com.mqunar.cock.network.state;

import com.mqunar.tools.log.QLog;

public class YaccaConnectedState implements ChannelState {
    String a = "OnekeyCremation";

    public boolean oneKeyCremation() {
        QLog.d(this.a, "YaccaConnectedState", new Object[0]);
        return true;
    }
}
