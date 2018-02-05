package org.apache.http.impl.io;

import java.io.InputStream;
import org.apache.http.io.HttpTransportMetrics;
import org.apache.http.io.SessionInputBuffer;
import org.apache.http.params.HttpParams;
import org.apache.http.util.CharArrayBuffer;

@Deprecated
public abstract class AbstractSessionInputBuffer implements SessionInputBuffer {
    public AbstractSessionInputBuffer() {
        throw new RuntimeException("Stub!");
    }

    protected void init(InputStream inputStream, int i, HttpParams httpParams) {
        throw new RuntimeException("Stub!");
    }

    protected int fillBuffer() {
        throw new RuntimeException("Stub!");
    }

    protected boolean hasBufferedData() {
        throw new RuntimeException("Stub!");
    }

    public int read() {
        throw new RuntimeException("Stub!");
    }

    public int read(byte[] bArr, int i, int i2) {
        throw new RuntimeException("Stub!");
    }

    public int read(byte[] bArr) {
        throw new RuntimeException("Stub!");
    }

    public int readLine(CharArrayBuffer charArrayBuffer) {
        throw new RuntimeException("Stub!");
    }

    public String readLine() {
        throw new RuntimeException("Stub!");
    }

    public HttpTransportMetrics getMetrics() {
        throw new RuntimeException("Stub!");
    }
}
