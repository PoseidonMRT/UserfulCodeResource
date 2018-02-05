package com.mqunar.necro.agent;

import android.support.v4.os.EnvironmentCompat;
import com.mqunar.necro.agent.bean.BaseData;

class NullAgentImpl implements AgentImpl {
    public void start() {
    }

    public void stop() {
    }

    public void disable() {
    }

    public boolean isDisabled() {
        return true;
    }

    public String getNetworkCarrier() {
        return EnvironmentCompat.MEDIA_UNKNOWN;
    }

    public String getNetworkWanType() {
        return EnvironmentCompat.MEDIA_UNKNOWN;
    }

    public String getCparam() {
        return "";
    }

    public void dealData(BaseData baseData) {
    }

    public void sendDataNow() {
    }

    public void insertFailDataToFile(String str) {
    }
}
