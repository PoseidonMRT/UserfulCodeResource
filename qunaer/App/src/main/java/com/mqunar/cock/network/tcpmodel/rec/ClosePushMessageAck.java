package com.mqunar.cock.network.tcpmodel.rec;

import com.mqunar.cock.network.tcpmodel.BaseMessage;

public class ClosePushMessageAck extends BaseMessage {
    public int push;
    public int ret;
    public String retMsg;
    public String sid;
    public String uid;
}
