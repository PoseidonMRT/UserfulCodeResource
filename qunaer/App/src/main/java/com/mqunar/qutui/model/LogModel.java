package com.mqunar.qutui.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class LogModel implements Serializable {
    public List<CallData> callDatas = new ArrayList();
    public List<CallData> calledDatas = new ArrayList();
    public long endTime;
    public HashMap<String, Boolean> installMap = new HashMap();
    public long startTime;

    public class CallData implements Serializable {
        public int effectiveCount;
        public int invalidCount;
        public String key;

        public CallData(String str, boolean z) {
            this.key = str;
            if (z) {
                this.effectiveCount++;
            } else {
                this.invalidCount++;
            }
        }
    }
}
