package com.mqunar.necro.agent.bean;

public class CpuData implements BaseData {
    private static final long serialVersionUID = 1;
    public String cpu;
    public String time;

    public String toString() {
        return " time=" + this.time + ",cpu=" + this.cpu;
    }
}
