package com.mqunar.cock.network.tcpmodel.rec;

import com.mqunar.cock.network.tcpmodel.BaseMessage;
import com.mqunar.cock.network.tcpmodel.MemberInfo;
import java.io.Serializable;
import java.util.ArrayList;

public class GroupChangedInfo extends BaseMessage {
    public Alter alter;
    public String ctnt;
    public int ginfo;
    public int hide;
    public int left;
    public int nr;
    public int reqid;
    public String sId;
    public long st;
    public int swNotify;
    public String uId;

    public class Alter implements Serializable {
        private static final long serialVersionUID = 1;
        public ArrayList<MemberInfo> addDetail;
        public String name;
        public String notice;
        public String[] rmv;
    }
}
