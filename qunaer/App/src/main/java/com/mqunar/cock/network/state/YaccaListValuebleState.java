package com.mqunar.cock.network.state;

import com.mqunar.cock.network.OneKeyCremation;
import com.mqunar.tools.log.QLog;

public class YaccaListValuebleState implements ChannelState {
    String a = "OnekeyCremation";

    public boolean oneKeyCremation() {
        QLog.d(this.a, "YaccaListValuebleState", new Object[0]);
        OneKeyCremation.getInstance().connect();
        OneKeyCremation.getInstance().setState(OneKeyCremation.getInstance().yaccaConnectingState);
        return false;
    }
}
