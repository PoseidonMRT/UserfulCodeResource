package com.mqunar.cock.model;

import java.io.Serializable;
import java.util.List;

public class YaccaListResult extends BaseResult implements Serializable {
    public YaccaListResultData data;

    public class YaccaInfo implements Serializable {
        public int cnt;
        public String ip;
        public int tPort;
        public int wPort;
    }

    public class YaccaListResultData implements Serializable {
        public int delay;
        public List<YaccaInfo> servers;
    }
}
