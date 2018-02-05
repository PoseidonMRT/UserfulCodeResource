package com.mqunar.necro.agent.bean;

import java.util.HashMap;

public class NetworkData implements BaseData {
    private static final long serialVersionUID = 1;
    public String connTime;
    public String endTime;
    public HashMap<String, String> headers = new HashMap();
    public String hf;
    public String httpCode;
    public String loc;
    public String mno;
    public String netType;
    public String reqSize;
    public String reqTime;
    public String reqUrl;
    public String resSize;
    public String startTime;

    public String toString() {
        return "reqUrl=" + this.reqUrl + ",startTime=" + this.startTime + ",endTime=" + this.endTime + ",connTime=" + this.connTime + ",reqSize=" + this.reqSize + ",resSize=" + this.resSize + ",httpCode=" + this.httpCode + ",hf=" + this.hf + ",netType=" + this.netType + ",loc=" + this.loc + ",mno=" + this.mno;
    }
}
