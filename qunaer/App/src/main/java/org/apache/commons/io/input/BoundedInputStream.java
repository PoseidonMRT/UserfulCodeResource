package org.apache.commons.io.input;

import java.io.InputStream;

public class BoundedInputStream extends InputStream {
    private final InputStream in;
    private long mark;
    private final long max;
    private long pos;
    private boolean propagateClose;

    public BoundedInputStream(InputStream inputStream, long j) {
        this.pos = 0;
        this.mark = -1;
        this.propagateClose = true;
        this.max = j;
        this.in = inputStream;
    }

    public BoundedInputStream(InputStream inputStream) {
        this(inputStream, -1);
    }

    public int read() {
        if (this.max >= 0 && this.pos >= this.max) {
            return -1;
        }
        int read = this.in.read();
        this.pos++;
        return read;
    }

    public int read(byte[] bArr) {
        return read(bArr, 0, bArr.length);
    }

    public int read(byte[] bArr, int i, int i2) {
        if (this.max >= 0 && this.pos >= this.max) {
            return -1;
        }
        int read = this.in.read(bArr, i, (int) (this.max >= 0 ? Math.min((long) i2, this.max - this.pos) : (long) i2));
        if (read == -1) {
            return -1;
        }
        this.pos += (long) read;
        return read;
    }

    public long skip(long j) {
        if (this.max >= 0) {
            j = Math.min(j, this.max - this.pos);
        }
        long skip = this.in.skip(j);
        this.pos += skip;
        return skip;
    }

    public int available() {
        if (this.max < 0 || this.pos < this.max) {
            return this.in.available();
        }
        return 0;
    }

    public String toString() {
        return this.in.toString();
    }

    public void close() {
        if (this.propagateClose) {
            this.in.close();
        }
    }

    public synchronized void reset() {
        this.in.reset();
        this.pos = this.mark;
    }

    public synchronized void mark(int i) {
        this.in.mark(i);
        this.mark = this.pos;
    }

    public boolean markSupported() {
        return this.in.markSupported();
    }

    public boolean isPropagateClose() {
        return this.propagateClose;
    }

    public void setPropagateClose(boolean z) {
        this.propagateClose = z;
    }
}
