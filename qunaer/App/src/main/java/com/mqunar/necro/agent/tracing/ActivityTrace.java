package com.mqunar.necro.agent.tracing;

import com.mqunar.contacts.basis.model.Contact;
import com.mqunar.necro.agent.Agent;
import com.mqunar.necro.agent.bean.CpuData;
import com.mqunar.necro.agent.bean.MemData;
import com.mqunar.necro.agent.bean.UIData;
import com.mqunar.necro.agent.logging.AgentLog;
import com.mqunar.necro.agent.logging.AgentLogManager;
import com.mqunar.necro.agent.sample.Sample;
import com.mqunar.necro.agent.sample.Sample.SampleType;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class ActivityTrace implements Harvestable {
    private boolean complete = false;
    private long lastUpdatedAt;
    private final AgentLog log = AgentLogManager.getAgentLog();
    private final Set<UUID> missingChildren = Collections.synchronizedSet(new HashSet());
    private final HashMap<String, String> params = new HashMap();
    public Trace rootTrace;
    private long startedAt;
    private final ConcurrentHashMap<UUID, Trace> traces = new ConcurrentHashMap();
    private Map<SampleType, Collection<Sample>> vitals;

    public ActivityTrace(Trace trace) {
        this.rootTrace = trace;
        this.lastUpdatedAt = trace.entryTimestamp;
        this.startedAt = this.lastUpdatedAt;
        this.params.put("type", "ACTIVITY");
    }

    public String getId() {
        return this.rootTrace == null ? null : this.rootTrace.myUUID.toString();
    }

    public void addTrace(Trace trace) {
        this.missingChildren.add(trace.myUUID);
        this.lastUpdatedAt = System.currentTimeMillis();
    }

    public void addCompletedTrace(Trace trace) {
        trace.traceMachine = null;
        this.missingChildren.remove(trace.myUUID);
        this.traces.put(trace.myUUID, trace);
        if (trace.exitTimestamp > this.rootTrace.exitTimestamp) {
            this.rootTrace.exitTimestamp = trace.exitTimestamp;
        }
        this.log.debug("Added trace " + trace.myUUID.toString() + " missing children: " + this.missingChildren.size());
        this.lastUpdatedAt = System.currentTimeMillis();
    }

    public boolean hasMissingChildren() {
        return !this.missingChildren.isEmpty();
    }

    public boolean isComplete() {
        return this.complete;
    }

    public void discard() {
        this.log.debug("Discarding trace of " + this.rootTrace.displayName + ":" + this.rootTrace.myUUID.toString() + "(" + this.traces.size() + " traces)");
        this.rootTrace.traceMachine = null;
        this.complete = true;
    }

    public void complete() {
        this.log.debug("ActivityTrace Completing trace of " + this.rootTrace.displayName + ":" + this.rootTrace.myUUID.toString() + "(" + this.traces.size() + " traces)");
        if (this.rootTrace.exitTimestamp == 0) {
            this.rootTrace.exitTimestamp = System.currentTimeMillis();
        }
        if (this.traces.isEmpty()) {
            this.rootTrace.traceMachine = null;
            this.complete = true;
            return;
        }
        this.rootTrace.traceMachine = null;
        this.complete = true;
        Agent.dealData(toSaveData());
    }

    public Map<UUID, Trace> getTraces() {
        return this.traces;
    }

    public void setVitals(Map<SampleType, Collection<Sample>> map) {
        this.vitals = map;
    }

    public void setLastUpdatedAt(long j) {
        this.lastUpdatedAt = j;
    }

    public long getLastUpdatedAt() {
        return this.lastUpdatedAt;
    }

    public long getStartedAt() {
        return this.startedAt;
    }

    public String getActivityName() {
        String str = "<activity>";
        if (this.rootTrace == null) {
            return str;
        }
        str = this.rootTrace.displayName;
        if (str == null) {
            return str;
        }
        int indexOf = str.indexOf(Contact.NUMBER);
        if (indexOf > 0) {
            return str.substring(0, indexOf);
        }
        return str;
    }

    private UIData toSaveData() {
        UIData uIData = new UIData();
        uIData.name = this.rootTrace.displayName;
        uIData.startTime = String.valueOf(this.rootTrace.entryTimestamp);
        uIData.exitTime = String.valueOf(this.rootTrace.exitTimestamp);
        uIData.appearTime = "";
        uIData.disposeTime = "";
        uIData.visibleTime = "";
        uIData.fps = "";
        uIData.applicationTime = String.valueOf(this.rootTrace.applicationTime);
        if (this.vitals != null) {
            List arrayList = new ArrayList();
            List arrayList2 = new ArrayList();
            for (Entry entry : this.vitals.entrySet()) {
                List<Sample> list = (List) entry.getValue();
                if (list != null) {
                    for (Sample sample : list) {
                        if (entry.getKey() == SampleType.CPU) {
                            CpuData cpuData = new CpuData();
                            cpuData.time = String.valueOf(sample.getTimestamp());
                            cpuData.cpu = sample.getSampleValue().getValue().toString();
                            arrayList.add(cpuData);
                        } else if (entry.getKey() == SampleType.MEMORY) {
                            MemData memData = new MemData();
                            memData.time = String.valueOf(sample.getTimestamp());
                            memData.mem = sample.getSampleValue().getValue().toString();
                            arrayList2.add(memData);
                        }
                    }
                }
            }
            uIData.cpuUsage = arrayList;
            uIData.memoryUsage = arrayList2;
        }
        return uIData;
    }
}
