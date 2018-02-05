package org.apache.commons.io.output;

import java.io.OutputStream;

public class TeeOutputStream extends ProxyOutputStream {
    protected OutputStream branch;

    public TeeOutputStream(OutputStream outputStream, OutputStream outputStream2) {
        super(outputStream);
        this.branch = outputStream2;
    }

    public synchronized void write(byte[] bArr) {
        super.write(bArr);
        this.branch.write(bArr);
    }

    public synchronized void write(byte[] bArr, int i, int i2) {
        super.write(bArr, i, i2);
        this.branch.write(bArr, i, i2);
    }

    public synchronized void write(int i) {
        super.write(i);
        this.branch.write(i);
    }

    public void flush() {
        super.flush();
        this.branch.flush();
    }

    public void close() {
        try {
            super.close();
        } finally {
            this.branch.close();
        }
    }
}
