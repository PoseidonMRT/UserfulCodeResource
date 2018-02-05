package com.mqunar.cock.network.tcpmodel.rec;

import com.mqunar.cock.network.tcpmodel.BaseMessage;

public class FetchDeleteMsgAck extends BaseMessage {
    public String msg_list;
    public int new_version;
    public int ret;
    public String sId;
}
