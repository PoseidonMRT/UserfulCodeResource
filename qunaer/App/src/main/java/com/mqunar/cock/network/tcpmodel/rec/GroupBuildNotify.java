package com.mqunar.cock.network.tcpmodel.rec;

import com.mqunar.cock.network.tcpmodel.BaseMessage;
import com.mqunar.cock.network.tcpmodel.MemberInfo;
import java.util.ArrayList;

public class GroupBuildNotify extends BaseMessage {
    public String frm;
    public int ginfo;
    public int hide;
    public String img;
    public ArrayList<MemberInfo> members;
    public String name;
    public int reqid;
    public String sId;
}
