package com.mqunar.cock.network.tcpmodel.rec;

import com.mqunar.cock.network.tcpmodel.BaseMessage;

public class GroupAdmChangedNotify extends BaseMessage {
    public String message;
    public String owner;
    public String reqid;
    public int ret;
    public String sId;
}
