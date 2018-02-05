package com.mqunar.necro.agent.tracing;

public interface ITraceMachineThread {
    long getCurrentThreadId();

    String getCurrentThreadName();

    boolean isUIThread();
}
