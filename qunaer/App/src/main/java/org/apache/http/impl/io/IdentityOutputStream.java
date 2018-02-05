package org.apache.http.impl.io;

import java.io.OutputStream;
import org.apache.http.io.SessionOutputBuffer;

@Deprecated
public class IdentityOutputStream extends OutputStream {
    public IdentityOutputStream(SessionOutputBuffer sessionOutputBuffer) {
        throw new RuntimeException("Stub!");
    }

    public void close() {
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
}
