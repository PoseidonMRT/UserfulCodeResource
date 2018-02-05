package com.mqunar.cock.network.state;

import com.mqunar.cock.model.BaseParam;
import com.mqunar.cock.network.OneKeyCremation;
import com.mqunar.tools.log.QLog;

public class YaccaListUnavailableState implements ChannelState {
    String a = "OnekeyCremation";

    public boolean oneKeyCremation() {
        QLog.d(this.a, "YaccaListUnavailableState", new Object[0]);
        OneKeyCremation.getInstance().loadYacaAsync(new BaseParam());
        OneKeyCremation.getInstance().setState(OneKeyCremation.getInstance().yaccaListGettingState);
        return false;
    }
}
