package com.mqunar.necro.agent.tracing;

import android.os.Handler;
import com.mqunar.necro.agent.Agent;
import com.mqunar.necro.agent.logging.AgentLog;
import com.mqunar.necro.agent.logging.AgentLogManager;
import com.mqunar.necro.agent.util.Util;
import com.mqunar.tools.DateTimeUtils;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.Stack;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

public class TraceMachine {
    private static final int HEALTHY_TRACE_TIMEOUT = 500;
    private static final int UNHEALTHY_TRACE_TIMEOUT = 60000;
    private static final AtomicBoolean disabled = new AtomicBoolean(false);
    private static final AgentLog log = AgentLogManager.getAgentLog();
    private static final ThreadLocal<Trace> threadLocalTrace = new ThreadLocal();
    private static final ThreadLocal<TraceStack> threadLocalTraceStack = new ThreadLocal();
    private static final Collection<ITraceLifecycleAware> traceListeners = new CopyOnWriteArrayList();
    private static TraceMachine traceMachine = null;
    private static ITraceMachineThread traceMachineInterface;
    private ActivityTrace activityTrace;

    class TraceStack extends Stack<Trace> {
        private TraceStack() {
        }
    }

    protected TraceMachine(Trace trace) {
        this.activityTrace = new ActivityTrace(trace);
    }

    public static TraceMachine getTraceMachine() {
        return traceMachine;
    }

    public static void addTraceListener(ITraceLifecycleAware iTraceLifecycleAware) {
        traceListeners.add(iTraceLifecycleAware);
    }

    public static void removeTraceListener(ITraceLifecycleAware iTraceLifecycleAware) {
        traceListeners.remove(iTraceLifecycleAware);
    }

    public static void setTraceMachineInterface(ITraceMachineThread iTraceMachineThread) {
        traceMachineInterface = iTraceMachineThread;
    }

    public static void startTracing(String str) {
        startTracing(str, false);
    }

    public static void startTracing(String str, boolean z) {
        log.debug("TraceMachine startTracing begin name " + str);
        try {
            if (!disabled.get()) {
                if (isTracingActive()) {
                    traceMachine.completeActivityTrace();
                }
                threadLocalTrace.remove();
                threadLocalTraceStack.set(new TraceStack());
                Trace trace = new Trace();
                if (z) {
                    trace.displayName = str;
                } else {
                    trace.displayName = Util.formatActivityDisplayName(str);
                }
                trace.metricName = Util.formatActivityMetricName(trace.displayName);
                trace.metricBackgroundName = Util.formatActivityBackgroundMetricName(trace.displayName);
                trace.entryTimestamp = System.currentTimeMillis();
                trace.applicationTime = getAppTime();
                if (log.getLevel() == 5) {
                    log.debug("Started trace of " + str + ":" + trace.myUUID.toString());
                }
                traceMachine = new TraceMachine(trace);
                trace.traceMachine = traceMachine;
                pushTraceContext(trace);
                for (ITraceLifecycleAware onTraceStart : traceListeners) {
                    onTraceStart.onTraceStart(traceMachine.activityTrace);
                }
                log.debug("TraceMachine startTracing end name " + str);
            }
        } catch (Throwable e) {
            log.error("Caught error while initializing TraceMachine, shutting it down", e);
            traceMachine = null;
            threadLocalTrace.remove();
            threadLocalTraceStack.remove();
        }
    }

    public static void enterMethod(String str) {
        enterMethod(null, str, null);
    }

    public static void enterMethod(String str, ArrayList<String> arrayList) {
        enterMethod(null, str, arrayList);
    }

    public static void enterMethod(Trace trace, String str, ArrayList<String> arrayList) {
        log.debug("TraceMachine enterMethod begin trace " + trace + " name " + str);
        try {
            if (!isTracingInactive()) {
                long currentTimeMillis = System.currentTimeMillis();
                if (traceMachine.activityTrace.getLastUpdatedAt() + 500 < currentTimeMillis && !traceMachine.activityTrace.hasMissingChildren()) {
                    log.debug("Completing activity trace after hitting healthy timeout (500ms)");
                    traceMachine.completeActivityTrace();
                } else if (traceMachine.activityTrace.getStartedAt() + DateTimeUtils.ONE_MINUTE < currentTimeMillis) {
                    log.debug("Completing activity trace after hitting unhealthy timeout (60000ms)");
                    traceMachine.completeActivityTrace();
                } else {
                    loadTraceContext(trace);
                    Trace registerNewTrace = registerNewTrace(str);
                    pushTraceContext(registerNewTrace);
                    registerNewTrace.scope = getCurrentScope();
                    registerNewTrace.setAnnotationParams(arrayList);
                    for (ITraceLifecycleAware onEnterMethod : traceListeners) {
                        onEnterMethod.onEnterMethod();
                    }
                    registerNewTrace.entryTimestamp = System.currentTimeMillis();
                }
            }
        } catch (Throwable e) {
            log.error("Caught error while calling enterMethod()", e);
        } catch (Throwable e2) {
            log.error("Caught error while calling enterMethod()", e2);
        }
    }

    public static void exitMethod() {
        log.debug("TraceMachine exitMethod begin threadLocalTrace.get() " + threadLocalTrace.get());
        try {
            if (!isTracingInactive()) {
                Trace trace = (Trace) threadLocalTrace.get();
                if (trace == null) {
                    log.debug("threadLocalTrace is null");
                    return;
                }
                trace.exitTimestamp = System.currentTimeMillis();
                if (trace.threadId == 0 && traceMachineInterface != null) {
                    trace.threadId = traceMachineInterface.getCurrentThreadId();
                    trace.threadName = traceMachineInterface.getCurrentThreadName();
                }
                for (ITraceLifecycleAware onExitMethod : traceListeners) {
                    onExitMethod.onExitMethod();
                }
                try {
                    trace.complete();
                    ((TraceStack) threadLocalTraceStack.get()).pop();
                    if (((TraceStack) threadLocalTraceStack.get()).empty()) {
                        threadLocalTrace.set(null);
                    } else {
                        Trace trace2 = (Trace) ((TraceStack) threadLocalTraceStack.get()).peek();
                        threadLocalTrace.set(trace2);
                        trace2.childExclusiveTime += trace.getDurationAsMilliseconds();
                        log.debug("TraceMachine exitMethod parentTrace1.childExclusiveTime " + trace2.childExclusiveTime);
                    }
                    if (trace.getType() != TraceType.TRACE) {
                    }
                } catch (TracingInactiveException e) {
                    threadLocalTrace.remove();
                    threadLocalTraceStack.remove();
                    if (trace.getType() != TraceType.TRACE) {
                    }
                }
            }
        } catch (Throwable e2) {
            log.error("Caught error while calling exitMethod()", e2);
        }
    }

    public static void postMethod() {
        new Handler().post(new Runnable() {
            public void run() {
                TraceMachine.exitMethod();
            }
        });
    }

    public static void unloadTraceContext(Object obj) {
        try {
            if (!isTracingInactive()) {
                if (traceMachineInterface == null || !traceMachineInterface.isUIThread()) {
                    if (threadLocalTrace.get() != null) {
                        log.debug("Trace " + ((Trace) threadLocalTrace.get()).myUUID.toString() + " is now inactive");
                    }
                    threadLocalTrace.remove();
                    threadLocalTraceStack.remove();
                    try {
                        ((TraceFieldInterface) obj)._nr_setTrace(null);
                    } catch (ClassCastException e) {
                        log.error("Not a TraceFieldInterface: " + e.getMessage());
                    }
                }
            }
        } catch (Throwable e2) {
            log.error("Caught error while calling unloadTraceContext()", e2);
        }
    }

    private static Trace registerNewTrace(String str) {
        if (isTracingInactive()) {
            log.debug("Tried to register a new trace but tracing is inactive!");
            throw new TracingInactiveException();
        }
        Trace currentTrace = getCurrentTrace();
        Trace trace = new Trace(str, currentTrace.myUUID, traceMachine);
        try {
            traceMachine.activityTrace.addTrace(trace);
            if (log.getLevel() == 5) {
                log.debug("Registering trace of " + str + " with parent " + currentTrace.displayName);
            }
            currentTrace.addChild(trace);
            return trace;
        } catch (Exception e) {
            throw new TracingInactiveException();
        }
    }

    private static void pushTraceContext(Trace trace) {
        log.debug("TraceMachine pushTraceContext trace " + trace + " threadLocalTraceStack.get() " + threadLocalTraceStack.get());
        if (!isTracingInactive() && trace != null) {
            TraceStack traceStack = (TraceStack) threadLocalTraceStack.get();
            if (traceStack.empty()) {
                traceStack.push(trace);
            } else if (traceStack.peek() != trace) {
                traceStack.push(trace);
            }
            threadLocalTrace.set(trace);
        }
    }

    private static void loadTraceContext(Trace trace) {
        log.debug("TraceMachine loadTraceContext trace " + trace + " threadLocalTrace.get() " + threadLocalTrace.get());
        if (!isTracingInactive()) {
            if (threadLocalTrace.get() == null) {
                threadLocalTrace.set(trace);
                threadLocalTraceStack.set(new TraceStack());
                if (trace != null) {
                    ((TraceStack) threadLocalTraceStack.get()).push(trace);
                } else {
                    return;
                }
            } else if (trace == null) {
                if (((TraceStack) threadLocalTraceStack.get()).isEmpty()) {
                    if (log.getLevel() == 5) {
                        log.debug("No context to load!");
                    }
                    threadLocalTrace.set(null);
                    return;
                }
                Trace trace2 = (Trace) ((TraceStack) threadLocalTraceStack.get()).peek();
                threadLocalTrace.set(trace2);
                trace = trace2;
            }
            if (log.getLevel() == 5) {
                log.debug("Trace " + trace.myUUID.toString() + " is now active");
            }
        }
    }

    private void completeActivityTrace() {
        log.debug("TraceMachine completeActivityTrace begin");
        if (!isTracingInactive()) {
            TraceMachine traceMachine = traceMachine;
            traceMachine = null;
            for (ITraceLifecycleAware iTraceLifecycleAware : traceListeners) {
                log.debug("TraceMachine completeActivityTrace TraceLifecycleAware " + iTraceLifecycleAware.getClass());
                iTraceLifecycleAware.onTraceComplete(traceMachine.activityTrace);
            }
            traceMachine.activityTrace.complete();
        }
    }

    private static Trace getCurrentTrace() {
        if (isTracingInactive()) {
            throw new TracingInactiveException();
        }
        Trace trace = (Trace) threadLocalTrace.get();
        return trace != null ? trace : getRootTrace();
    }

    private static Trace getRootTrace() {
        try {
            return traceMachine.activityTrace.rootTrace;
        } catch (NullPointerException e) {
            throw new TracingInactiveException();
        }
    }

    public static boolean isTracingActive() {
        return traceMachine != null;
    }

    public static boolean isTracingInactive() {
        return traceMachine == null;
    }

    public void storeCompletedTrace(Trace trace) {
        try {
            if (isTracingInactive()) {
                log.debug("Attempted to store a completed trace with no trace machine!");
            } else {
                this.activityTrace.addCompletedTrace(trace);
            }
        } catch (Throwable e) {
            log.error("Caught error while calling storeCompletedTrace()", e);
        }
    }

    public static ActivityTrace getActivityTrace() {
        try {
            return traceMachine.activityTrace;
        } catch (NullPointerException e) {
            throw new TracingInactiveException();
        }
    }

    public static Map<String, Object> getCurrentTraceParams() {
        return getCurrentTrace().getParams();
    }

    public static void setCurrentTraceParam(String str, Object obj) {
        if (!isTracingInactive()) {
            try {
                getCurrentTrace().getParams().put(str, obj);
            } catch (Throwable e) {
                log.error("setCurrentTraceParam", e);
            }
        }
    }

    public static void setCurrentDisplayName(String str) {
        if (!isTracingInactive()) {
            try {
                getCurrentTrace().displayName = str;
            } catch (Throwable e) {
                log.error("setCurrentDisplayName", e);
            }
        }
    }

    public static void setRootDisplayName(String str) {
        if (!isTracingInactive()) {
            try {
                Trace rootTrace = getRootTrace();
                rootTrace.metricName = Util.formatActivityMetricName(str);
                rootTrace.metricBackgroundName = Util.formatActivityBackgroundMetricName(str);
                rootTrace.displayName = str;
                getCurrentTrace().scope = getCurrentScope();
            } catch (Throwable e) {
                log.error("setRootDisplayName", e);
            }
        }
    }

    public static String getCurrentScope() {
        try {
            if (isTracingInactive()) {
                return null;
            }
            return (traceMachineInterface == null || traceMachineInterface.isUIThread()) ? traceMachine.activityTrace.rootTrace.metricName : traceMachine.activityTrace.rootTrace.metricBackgroundName;
        } catch (Throwable e) {
            log.error("Caught error while calling getCurrentScope()", e);
            return null;
        }
    }

    public void onHarvestBefore() {
        log.debug("TraceMachine ------- onHarvestBefore");
        if (isTracingActive()) {
            long currentTimeMillis = System.currentTimeMillis();
            long lastUpdatedAt = traceMachine.activityTrace.getLastUpdatedAt();
            long startedAt = traceMachine.activityTrace.getStartedAt();
            if (lastUpdatedAt + 500 < currentTimeMillis && !traceMachine.activityTrace.hasMissingChildren()) {
                log.debug("Completing activity trace after hitting healthy timeout (500ms)");
                completeActivityTrace();
                return;
            } else if (DateTimeUtils.ONE_MINUTE + startedAt < currentTimeMillis) {
                log.debug("Completing activity trace after hitting unhealthy timeout (60000ms)");
                completeActivityTrace();
                return;
            } else {
                return;
            }
        }
        log.debug("TraceMachine is inactive");
    }

    public static void haltTracing() {
        if (!isTracingInactive()) {
            TraceMachine traceMachine = traceMachine;
            traceMachine = null;
            traceMachine.activityTrace.discard();
            threadLocalTrace.remove();
            threadLocalTraceStack.remove();
        }
    }

    public static void endTrace() {
        traceMachine.completeActivityTrace();
    }

    public static void endTrace(String str) {
        try {
            if (getActivityTrace().rootTrace.myUUID.toString().equals(str)) {
                traceMachine.completeActivityTrace();
            }
        } catch (TracingInactiveException e) {
        }
    }

    public static void enterNetworkSegment(String str) {
        try {
            if (!isTracingInactive()) {
                if (getCurrentTrace().getType() == TraceType.NETWORK) {
                    exitMethod();
                }
                enterMethod(null, str, null);
                getCurrentTrace().setType(TraceType.NETWORK);
            }
        } catch (Throwable e) {
            log.error("Caught error while calling enterNetworkSegment()", e);
        } catch (Throwable e2) {
            log.error("Caught error while calling enterNetworkSegment()", e2);
        }
    }

    private static long getAppTime() {
        long j = Agent.applicationStart;
        if (j > 0) {
            Agent.applicationStart = -1;
        }
        return j;
    }
}
