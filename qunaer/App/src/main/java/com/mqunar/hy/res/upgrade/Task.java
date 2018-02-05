package com.mqunar.hy.res.upgrade;

import com.mqunar.hy.res.TaskResult;
import java.io.Serializable;
import java.util.List;

abstract class Task<T extends Serializable, V> {
    public static final byte TASK_FAILED = (byte) 3;
    public static final byte TASK_NORUN = (byte) 0;
    public static final byte TASK_RUNING = (byte) 1;
    public static final byte TASK_SUCCESS = (byte) 2;
    private boolean isValid = true;
    private byte status = (byte) 0;

    public abstract void run(List<T> list, TaskResult<V> taskResult, T... tArr);

    Task() {
    }

    public synchronized boolean necessary() {
        boolean z;
        z = this.status == (byte) 0 || this.status == (byte) 3;
        return z;
    }

    public synchronized void setStatus(byte b) {
        this.status = b;
    }

    public synchronized void setValid(boolean z) {
        this.isValid = z;
    }

    public synchronized boolean valid() {
        return this.isValid;
    }
}
