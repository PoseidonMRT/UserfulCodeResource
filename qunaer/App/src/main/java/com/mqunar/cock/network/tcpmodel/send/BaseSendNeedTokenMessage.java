package com.mqunar.cock.network.tcpmodel.send;

import com.mqunar.cock.network.tcpmodel.send.BaseSendMessage.Platform;

public class BaseSendNeedTokenMessage extends BaseSendMessage {

    class PlatformNew extends Platform {
        private PlatformNew() {
        }
    }

    BaseSendNeedTokenMessage() {
        setC(new PlatformNew());
    }
}
