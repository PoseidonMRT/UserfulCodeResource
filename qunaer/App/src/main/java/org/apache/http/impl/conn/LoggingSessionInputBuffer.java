package org.apache.http.impl.conn;

import org.apache.http.io.HttpTransportMetrics;
import org.apache.http.io.SessionInputBuffer;
import org.apache.http.util.CharArrayBuffer;

@Deprecated
public class LoggingSessionInputBuffer implements SessionInputBuffer {
    public LoggingSessionInputBuffer(SessionInputBuffer sessionInputBuffer, Wire wire) {
        throw new RuntimeException("Stub!");
    }

    public boolean isDataAvailable(int i) {
        throw new RuntimeException("Stub!");
    }

    public int read(byte[] bArr, int i, int i2) {
        throw new RuntimeException("Stub!");
    }

    public int read() {
        throw new RuntimeException("Stub!");
    }

    public int read(byte[] bArr) {
        throw new RuntimeException("Stub!");
    }

    public String readLine() {
        throw new RuntimeException("Stub!");
    }

    public int readLine(CharArrayBuffer charArrayBuffer) {
        throw new RuntimeException("Stub!");
    }

    public HttpTransportMetrics getMetrics() {
        throw new RuntimeException("Stub!");
    }
}
