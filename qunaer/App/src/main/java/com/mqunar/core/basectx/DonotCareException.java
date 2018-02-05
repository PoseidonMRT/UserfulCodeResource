package com.mqunar.core.basectx;

public class DonotCareException extends RuntimeException {
    private boolean killProcess;

    public DonotCareException(boolean z) {
        this.killProcess = z;
    }

    public DonotCareException(String str) {
        super(str);
    }

    public DonotCareException(String str, Throwable th) {
        super(str, th);
    }

    public DonotCareException(Throwable th) {
        super(th);
    }

    public void setKillProcess(boolean z) {
        this.killProcess = z;
    }

    public boolean isKillProcess() {
        return this.killProcess;
    }
}
