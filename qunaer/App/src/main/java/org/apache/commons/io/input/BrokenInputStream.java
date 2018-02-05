package org.apache.commons.io.input;

import java.io.IOException;
import java.io.InputStream;

public class BrokenInputStream extends InputStream {
    private final IOException exception;

    public BrokenInputStream(IOException iOException) {
        this.exception = iOException;
    }

    public BrokenInputStream() {
        this(new IOException("Broken input stream"));
    }

    public int read() {
        throw this.exception;
    }

    public int available() {
        throw this.exception;
    }

    public long skip(long j) {
        throw this.exception;
    }

    public void reset() {
        throw this.exception;
    }

    public void close() {
        throw this.exception;
    }
}
