package com.mqunar.necro.agent.tracing;

public interface ITraceLifecycleAware {
    void onEnterMethod();

    void onExitMethod();

    void onTraceComplete(ActivityTrace activityTrace);

    void onTraceStart(ActivityTrace activityTrace);
}
