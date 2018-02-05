package com.mqunar.necro.agent.instrumentation.io;

import java.io.OutputStream;

public final class CountingOutputStream extends OutputStream implements StreamCompleteListenerSource {
    private long count = 0;
    private final OutputStream impl;
    private final StreamCompleteListenerManager listenerManager = new StreamCompleteListenerManager();

    public CountingOutputStream(OutputStream outputStream) {
        this.impl = outputStream;
    }

    public void addStreamCompleteListener(StreamCompleteListener streamCompleteListener) {
        this.listenerManager.addStreamCompleteListener(streamCompleteListener);
    }

    public void removeStreamCompleteListener(StreamCompleteListener streamCompleteListener) {
        this.listenerManager.removeStreamCompleteListener(streamCompleteListener);
    }

    public long getCount() {
        return this.count;
    }

    public void write(int i) {
        try {
            this.impl.write(i);
            this.count++;
        } catch (Exception e) {
            notifyStreamError(e);
            throw e;
        }
    }

    public void write(byte[] bArr) {
        try {
            this.impl.write(bArr);
            this.count += (long) bArr.length;
        } catch (Exception e) {
            notifyStreamError(e);
            throw e;
        }
    }

    public void write(byte[] bArr, int i, int i2) {
        try {
            this.impl.write(bArr, i, i2);
            this.count += (long) i2;
        } catch (Exception e) {
            notifyStreamError(e);
            throw e;
        }
    }

    public void flush() {
        try {
            this.impl.flush();
        } catch (Exception e) {
            notifyStreamError(e);
            throw e;
        }
    }

    public void close() {
        try {
            this.impl.close();
            notifyStreamComplete();
        } catch (Exception e) {
            notifyStreamError(e);
            throw e;
        }
    }

    private void notifyStreamComplete() {
        if (!this.listenerManager.isComplete()) {
            this.listenerManager.notifyStreamComplete(new StreamCompleteEvent(this, this.count));
        }
    }

    private void notifyStreamError(Exception exception) {
        if (!this.listenerManager.isComplete()) {
            this.listenerManager.notifyStreamError(new StreamCompleteEvent(this, this.count, exception));
        }
    }
}
