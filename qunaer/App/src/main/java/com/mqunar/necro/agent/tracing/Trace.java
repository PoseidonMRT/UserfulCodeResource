package com.mqunar.necro.agent.tracing;

import com.mqunar.necro.agent.instrumentation.MetricCategory;
import com.mqunar.necro.agent.logging.AgentLog;
import com.mqunar.necro.agent.logging.AgentLogManager;
import com.mqunar.necro.agent.util.Util;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class Trace {
    private static final String CATEGORY_PARAMETER = "category";
    private static final AgentLog log = AgentLogManager.getAgentLog();
    public long applicationTime;
    public long childExclusiveTime;
    private volatile Set<UUID> children;
    public String displayName;
    public long entryTimestamp;
    public long exclusiveTime;
    public long exitTimestamp;
    private boolean isComplete;
    public String metricBackgroundName;
    public String metricName;
    public final UUID myUUID;
    private volatile Map<String, Object> params;
    public final UUID parentUUID;
    private List<String> rawAnnotationParams;
    public String scope;
    public long threadId;
    public String threadName;
    public TraceMachine traceMachine;
    private TraceType type;

    public Trace() {
        this.myUUID = new UUID(Util.getRandom().nextLong(), Util.getRandom().nextLong());
        this.entryTimestamp = 0;
        this.exitTimestamp = 0;
        this.exclusiveTime = 0;
        this.childExclusiveTime = 0;
        this.threadId = 0;
        this.threadName = "main";
        this.applicationTime = 0;
        this.type = TraceType.TRACE;
        this.isComplete = false;
        this.parentUUID = null;
    }

    public Trace(String str, UUID uuid, TraceMachine traceMachine) {
        this.myUUID = new UUID(Util.getRandom().nextLong(), Util.getRandom().nextLong());
        this.entryTimestamp = 0;
        this.exitTimestamp = 0;
        this.exclusiveTime = 0;
        this.childExclusiveTime = 0;
        this.threadId = 0;
        this.threadName = "main";
        this.applicationTime = 0;
        this.type = TraceType.TRACE;
        this.isComplete = false;
        this.displayName = str;
        this.parentUUID = uuid;
        this.traceMachine = traceMachine;
    }

    public void addChild(Trace trace) {
        if (this.children == null) {
            synchronized (this) {
                if (this.children == null) {
                    this.children = Collections.synchronizedSet(new HashSet());
                }
            }
        }
        this.children.add(trace.myUUID);
    }

    public Set<UUID> getChildren() {
        if (this.children == null) {
            synchronized (this) {
                if (this.children == null) {
                    this.children = Collections.synchronizedSet(new HashSet());
                }
            }
        }
        return this.children;
    }

    public Map<String, Object> getParams() {
        if (this.params == null) {
            synchronized (this) {
                if (this.params == null) {
                    this.params = new ConcurrentHashMap();
                }
            }
        }
        this.params.put("type", this.type.toString());
        return this.params;
    }

    public void setAnnotationParams(List<String> list) {
        this.rawAnnotationParams = list;
    }

    public Map<String, Object> getAnnotationParams() {
        Map hashMap = new HashMap();
        if (this.rawAnnotationParams != null && this.rawAnnotationParams.size() > 0) {
            Iterator it = this.rawAnnotationParams.iterator();
            while (it.hasNext()) {
                String str = (String) it.next();
                Object createParameter = createParameter(str, (String) it.next(), (String) it.next());
                if (createParameter != null) {
                    hashMap.put(str, createParameter);
                }
            }
        }
        return hashMap;
    }

    public boolean isComplete() {
        return this.isComplete;
    }

    public void complete() {
        log.debug("Trace complete getDurationAsMilliseconds " + getDurationAsMilliseconds() + " childExclusiveTime " + this.childExclusiveTime);
        if (this.isComplete) {
            log.warning("Attempted to double complete trace " + this.myUUID.toString());
            return;
        }
        if (this.exitTimestamp == 0) {
            this.exitTimestamp = System.currentTimeMillis();
        }
        this.exclusiveTime = getDurationAsMilliseconds() - this.childExclusiveTime;
        this.isComplete = true;
        try {
            this.traceMachine.storeCompletedTrace(this);
        } catch (NullPointerException e) {
            throw new TracingInactiveException();
        }
    }

    public TraceType getType() {
        return this.type;
    }

    public void setType(TraceType traceType) {
        this.type = traceType;
    }

    public long getDurationAsMilliseconds() {
        return this.exitTimestamp - this.entryTimestamp;
    }

    public float getDurationAsSeconds() {
        return ((float) (this.exitTimestamp - this.entryTimestamp)) / 1000.0f;
    }

    public MetricCategory getCategory() {
        if (!getAnnotationParams().containsKey("category")) {
            return null;
        }
        Object obj = getAnnotationParams().get("category");
        if (obj instanceof MetricCategory) {
            return (MetricCategory) obj;
        }
        log.error("Category annotation parameter is not of type MetricCategory");
        return null;
    }

    private static Object createParameter(String str, String str2, String str3) {
        try {
            Class cls = Class.forName(str2);
            if (MetricCategory.class == cls) {
                return MetricCategory.valueOf(str3);
            }
            if (String.class != cls) {
                return null;
            }
            return str3;
        } catch (Throwable e) {
            log.error("Unable to resolve parameter class in enterMethod: " + e.getMessage(), e);
            return null;
        }
    }
}
