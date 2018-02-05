package com.mqunar.necro.agent.bean;

import java.util.List;

public class UIData implements BaseData {
    private static final long serialVersionUID = 1;
    public String appearTime;
    public String applicationTime;
    public List<CpuData> cpuUsage;
    public String disposeTime;
    public String exitTime;
    public String fps;
    public List<KeyMethod> keyMethods;
    public List<MemData> memoryUsage;
    public String name;
    public String startTime;
    public String visibleTime;

    public String toString() {
        return "name=" + this.name + ",startTime=" + this.startTime + ",exitTime=" + this.exitTime + ",appearTime=" + this.appearTime + ",disposeTime=" + this.disposeTime + ",visibleTime=" + this.visibleTime + ",fps=" + this.fps + ",memoryUsage=[" + this.memoryUsage.toString() + "],cpuUsage=[" + this.cpuUsage.toString() + "],applicationTime=[" + this.applicationTime + "]";
    }
}
