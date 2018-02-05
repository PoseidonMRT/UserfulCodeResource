package org.apache.commons.io.input;

import java.io.FilterReader;
import java.io.IOException;
import java.io.Reader;
import java.nio.CharBuffer;

public abstract class ProxyReader extends FilterReader {
    public ProxyReader(Reader reader) {
        super(reader);
    }

    public int read() {
        int i = 1;
        try {
            beforeRead(1);
            int read = this.in.read();
            if (read == -1) {
                i = -1;
            }
            afterRead(i);
            return read;
        } catch (IOException e) {
            handleIOException(e);
            return -1;
        }
    }

    public int read(char[] cArr) {
        int length;
        if (cArr != null) {
            try {
                length = cArr.length;
            } catch (IOException e) {
                handleIOException(e);
                return -1;
            }
        }
        length = 0;
        beforeRead(length);
        length = this.in.read(cArr);
        afterRead(length);
        return length;
    }

    public int read(char[] cArr, int i, int i2) {
        try {
            beforeRead(i2);
            int read = this.in.read(cArr, i, i2);
            afterRead(read);
            return read;
        } catch (IOException e) {
            handleIOException(e);
            return -1;
        }
    }

    public int read(CharBuffer charBuffer) {
        int length;
        if (charBuffer != null) {
            try {
                length = charBuffer.length();
            } catch (IOException e) {
                handleIOException(e);
                return -1;
            }
        }
        length = 0;
        beforeRead(length);
        length = this.in.read(charBuffer);
        afterRead(length);
        return length;
    }

    public long skip(long j) {
        try {
            return this.in.skip(j);
        } catch (IOException e) {
            handleIOException(e);
            return 0;
        }
    }

    public boolean ready() {
        try {
            return this.in.ready();
        } catch (IOException e) {
            handleIOException(e);
            return false;
        }
    }

    public void close() {
        try {
            this.in.close();
        } catch (IOException e) {
            handleIOException(e);
        }
    }

    public synchronized void mark(int i) {
        try {
            this.in.mark(i);
        } catch (IOException e) {
            handleIOException(e);
        }
    }

    public synchronized void reset() {
        try {
            this.in.reset();
        } catch (IOException e) {
            handleIOException(e);
        }
    }

    public boolean markSupported() {
        return this.in.markSupported();
    }

    protected void beforeRead(int i) {
    }

    protected void afterRead(int i) {
    }

    protected void handleIOException(IOException iOException) {
        throw iOException;
    }
}
