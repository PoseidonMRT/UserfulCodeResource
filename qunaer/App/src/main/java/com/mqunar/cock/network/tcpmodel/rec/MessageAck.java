package com.mqunar.cock.network.tcpmodel.rec;

import com.mqunar.cock.network.tcpmodel.BaseMessage;

public class MessageAck extends BaseMessage {
    public String ctnt;
    public long dId;
    public String hint;
    public long mId;
    public int reqid;
    public int ret;
    public String retMsg;
    public long st;
    public long tm;
    public String to;
}
