package com.mqunar.network;

import java.io.OutputStream;

class a extends OutputStream {
    protected byte[] a;
    protected int b;

    public a() {
        this.a = new byte[2048];
    }

    public a(int i) {
        if (i >= 0) {
            this.a = new byte[i];
            return;
        }
        throw new IllegalArgumentException("size < 0");
    }

    public void close() {
        super.close();
    }

    private void a(int i) {
        if (this.b + i > this.a.length) {
            Object obj = new byte[((this.b + i) * 2)];
            System.arraycopy(this.a, 0, obj, 0, this.b);
            this.a = obj;
        }
    }

    public synchronized byte[] a() {
        Object obj;
        obj = new byte[this.b];
        System.arraycopy(this.a, 0, obj, 0, this.b);
        return obj;
    }

    public synchronized byte[] b() {
        byte[] bArr;
        if (this.b == this.a.length) {
            bArr = this.a;
        } else {
            bArr = a();
        }
        return bArr;
    }

    public String toString() {
        return new String(this.a, 0, this.b);
    }

    private static void a(int i, int i2, int i3) {
        if ((i2 | i3) < 0 || i2 > i || i - i2 < i3) {
            throw new ArrayIndexOutOfBoundsException(i, i2, i3);
        }
    }

    public synchronized void write(byte[] bArr, int i, int i2) {
        a(bArr.length, i, i2);
        if (i2 != 0) {
            a(i2);
            System.arraycopy(bArr, i, this.a, this.b, i2);
            this.b += i2;
        }
    }

    public synchronized void write(int i) {
        if (this.b == this.a.length) {
            a(1);
        }
        byte[] bArr = this.a;
        int i2 = this.b;
        this.b = i2 + 1;
        bArr[i2] = (byte) i;
    }
}
