package org.apache.http.impl.io;

import java.io.OutputStream;
import org.apache.http.io.HttpTransportMetrics;
import org.apache.http.io.SessionOutputBuffer;
import org.apache.http.params.HttpParams;
import org.apache.http.util.CharArrayBuffer;

@Deprecated
public abstract class AbstractSessionOutputBuffer implements SessionOutputBuffer {
    public AbstractSessionOutputBuffer() {
        throw new RuntimeException("Stub!");
    }

    protected void init(OutputStream outputStream, int i, HttpParams httpParams) {
        throw new RuntimeException("Stub!");
    }

    protected void flushBuffer() {
        throw new RuntimeException("Stub!");
    }

    public void flush() {
        throw new RuntimeException("Stub!");
    }

    public void write(byte[] bArr, int i, int i2) {
        throw new RuntimeException("Stub!");
    }

    public void write(byte[] bArr) {
        throw new RuntimeException("Stub!");
    }

    public void write(int i) {
        throw new RuntimeException("Stub!");
    }

    public void writeLine(String str) {
        throw new RuntimeException("Stub!");
    }

    public void writeLine(CharArrayBuffer charArrayBuffer) {
        throw new RuntimeException("Stub!");
    }

    public HttpTransportMetrics getMetrics() {
        throw new RuntimeException("Stub!");
    }
}
