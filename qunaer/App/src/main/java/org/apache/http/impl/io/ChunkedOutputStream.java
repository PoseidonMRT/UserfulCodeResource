package org.apache.http.impl.io;

import java.io.OutputStream;
import org.apache.http.io.SessionOutputBuffer;

@Deprecated
public class ChunkedOutputStream extends OutputStream {
    public ChunkedOutputStream(SessionOutputBuffer sessionOutputBuffer, int i) {
        throw new RuntimeException("Stub!");
    }

    public ChunkedOutputStream(SessionOutputBuffer sessionOutputBuffer) {
        throw new RuntimeException("Stub!");
    }

    protected void flushCache() {
        throw new RuntimeException("Stub!");
    }

    protected void flushCacheWithAppend(byte[] bArr, int i, int i2) {
        throw new RuntimeException("Stub!");
    }

    protected void writeClosingChunk() {
        throw new RuntimeException("Stub!");
    }

    public void finish() {
        throw new RuntimeException("Stub!");
    }

    public void write(int i) {
        throw new RuntimeException("Stub!");
    }

    public void write(byte[] bArr) {
        throw new RuntimeException("Stub!");
    }

    public void write(byte[] bArr, int i, int i2) {
        throw new RuntimeException("Stub!");
    }

    public void flush() {
        throw new RuntimeException("Stub!");
    }

    public void close() {
        throw new RuntimeException("Stub!");
    }
}
