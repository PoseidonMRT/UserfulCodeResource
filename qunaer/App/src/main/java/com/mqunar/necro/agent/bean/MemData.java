package com.mqunar.necro.agent.bean;

public class MemData implements BaseData {
    private static final long serialVersionUID = 1;
    public String mem;
    public String time;

    public String toString() {
        return " time=" + this.time + ",mem=" + this.mem;
    }
}
