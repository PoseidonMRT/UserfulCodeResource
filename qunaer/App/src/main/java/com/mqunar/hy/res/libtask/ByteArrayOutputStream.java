package com.mqunar.hy.res.libtask;

import java.io.OutputStream;

class ByteArrayOutputStream extends OutputStream {
    protected byte[] buf;
    protected int count;

    public ByteArrayOutputStream() {
        this.buf = new byte[2048];
    }

    public ByteArrayOutputStream(int i) {
        if (i >= 0) {
            this.buf = new byte[i];
            return;
        }
        throw new IllegalArgumentException("size < 0");
    }

    public void close() {
        super.close();
    }

    private void expand(int i) {
        if (this.count + i > this.buf.length) {
            Object obj = new byte[((this.count + i) * 2)];
            System.arraycopy(this.buf, 0, obj, 0, this.count);
            this.buf = obj;
        }
    }

    public synchronized void reset() {
        this.count = 0;
    }

    public int size() {
        return this.count;
    }

    public synchronized byte[] toByteArray() {
        Object obj;
        obj = new byte[this.count];
        System.arraycopy(this.buf, 0, obj, 0, this.count);
        return obj;
    }

    public synchronized byte[] rawByteArray() {
        byte[] bArr;
        if (this.count == this.buf.length) {
            bArr = this.buf;
        } else {
            bArr = toByteArray();
        }
        return bArr;
    }

    public String toString() {
        return new String(this.buf, 0, this.count);
    }

    @Deprecated
    public String toString(int i) {
        char[] cArr = new char[size()];
        for (int i2 = 0; i2 < cArr.length; i2++) {
            cArr[i2] = (char) (((i & 255) << 8) | (this.buf[i2] & 255));
        }
        return new String(cArr);
    }

    public String toString(String str) {
        return new String(this.buf, 0, this.count, str);
    }

    private static void checkOffsetAndCount(int i, int i2, int i3) {
        if ((i2 | i3) < 0 || i2 > i || i - i2 < i3) {
            throw new ArrayIndexOutOfBoundsException("length=" + i + "; regionStart=" + i2 + "; regionLength=" + i3);
        }
    }

    public synchronized void write(byte[] bArr, int i, int i2) {
        checkOffsetAndCount(bArr.length, i, i2);
        if (i2 != 0) {
            expand(i2);
            System.arraycopy(bArr, i, this.buf, this.count, i2);
            this.count += i2;
        }
    }

    public synchronized void write(int i) {
        if (this.count == this.buf.length) {
            expand(1);
        }
        byte[] bArr = this.buf;
        int i2 = this.count;
        this.count = i2 + 1;
        bArr[i2] = (byte) i;
    }

    public synchronized void writeTo(OutputStream outputStream) {
        outputStream.write(this.buf, 0, this.count);
    }
}
