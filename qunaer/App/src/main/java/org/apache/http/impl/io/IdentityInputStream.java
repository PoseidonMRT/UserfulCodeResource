package org.apache.http.impl.io;

import java.io.InputStream;
import org.apache.http.io.SessionInputBuffer;

@Deprecated
public class IdentityInputStream extends InputStream {
    public IdentityInputStream(SessionInputBuffer sessionInputBuffer) {
        throw new RuntimeException("Stub!");
    }

    public int available() {
        throw new RuntimeException("Stub!");
    }

    public void close() {
        throw new RuntimeException("Stub!");
    }

    public int read() {
        throw new RuntimeException("Stub!");
    }

    public int read(byte[] bArr, int i, int i2) {
        throw new RuntimeException("Stub!");
    }
}
