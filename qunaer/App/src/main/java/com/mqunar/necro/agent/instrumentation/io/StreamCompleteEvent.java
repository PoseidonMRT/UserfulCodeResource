package com.mqunar.necro.agent.instrumentation.io;

import java.util.EventObject;

public class StreamCompleteEvent extends EventObject {
    private static final long serialVersionUID = 1;
    private final long bytes;
    private final Exception exception;

    public StreamCompleteEvent(Object obj, long j, Exception exception) {
        super(obj);
        this.bytes = j;
        this.exception = exception;
    }

    public StreamCompleteEvent(Object obj, long j) {
        this(obj, j, (Exception) null);
    }

    public long getBytes() {
        return this.bytes;
    }

    public Exception getException() {
        return this.exception;
    }

    public boolean isError() {
        return this.exception != null;
    }
}
