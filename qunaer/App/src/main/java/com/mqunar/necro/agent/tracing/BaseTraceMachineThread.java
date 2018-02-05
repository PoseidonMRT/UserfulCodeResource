package com.mqunar.necro.agent.tracing;

import android.os.Looper;

public class BaseTraceMachineThread implements ITraceMachineThread {
    public long getCurrentThreadId() {
        return Thread.currentThread().getId();
    }

    public boolean isUIThread() {
        return Looper.myLooper() == Looper.getMainLooper();
    }

    public String getCurrentThreadName() {
        return Thread.currentThread().getName();
    }
}
