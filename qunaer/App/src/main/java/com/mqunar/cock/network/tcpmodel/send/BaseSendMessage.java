package com.mqunar.cock.network.tcpmodel.send;

import com.mqunar.cock.network.tcpmodel.BaseMessage;
import java.io.Serializable;

public class BaseSendMessage extends BaseMessage {
    public Platform c = new Platform();

    public class Platform implements Serializable {
    }

    public void setC(Platform platform) {
        this.c = platform;
    }
}
