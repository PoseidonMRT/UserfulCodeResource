package com.mqunar.hy.res.model;

import java.io.Serializable;
import java.util.ArrayList;

public class HybridResult implements Serializable {
    private static final long serialVersionUID = 1;
    public MgData data;
    public String message;
    public int status = -1;

    public class MgData implements Serializable {
        private static final long serialVersionUID = 1;
        public ArrayList<HybridInfo> hlist;
        public ArrayList<HybridInfo> offline_hlist;
    }
}
