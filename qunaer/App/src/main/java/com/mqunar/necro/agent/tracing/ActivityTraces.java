package com.mqunar.necro.agent.tracing;

import java.util.ArrayList;
import java.util.Collection;

public class ActivityTraces {
    private final Collection<ActivityTrace> activityTraces = new ArrayList();

    public synchronized void add(ActivityTrace activityTrace) {
        this.activityTraces.add(activityTrace);
    }

    public synchronized void remove(ActivityTrace activityTrace) {
        this.activityTraces.remove(activityTrace);
    }

    public void clear() {
        this.activityTraces.clear();
    }

    public int count() {
        return this.activityTraces.size();
    }

    public Collection<ActivityTrace> getActivityTraces() {
        return this.activityTraces;
    }
}
