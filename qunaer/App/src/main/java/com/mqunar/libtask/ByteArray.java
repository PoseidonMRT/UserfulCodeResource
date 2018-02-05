package com.mqunar.libtask;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import com.mqunar.tools.ArrayUtils;
import java.io.ByteArrayInputStream;

public class ByteArray {
    private byte[] a;
    private int b;

    public ByteArray(byte[] bArr, int i, int i2) {
        if (bArr == null) {
            throw new IllegalArgumentException("bytes must not bu null");
        }
        this.b = i;
        if (i == 0 && i2 == bArr.length) {
            this.a = bArr;
            return;
        }
        this.a = new byte[i2];
        System.arraycopy(bArr, i, this.a, 0, i2);
    }

    public ByteArray(byte[] bArr) {
        this(bArr, 0, bArr.length);
    }

    public ByteArray(int i) {
        this(new byte[i], 0, i);
    }

    public byte[] rawBytes() {
        return this.a;
    }

    public byte[] copyBytes(int i) {
        if (i > this.a.length) {
            throw new IllegalArgumentException("length too long");
        }
        Object obj = new byte[i];
        System.arraycopy(this.a, this.b, obj, 0, i);
        return obj;
    }

    public byte[] copyBytes() {
        return copyBytes(this.a.length - this.b);
    }

    public void setCursor(int i) {
        this.b = i;
    }

    public int getCursor() {
        return this.b;
    }

    public void inc(int i) {
        this.b += i;
    }

    public void inc() {
        inc(1);
    }

    public int getLength() {
        return this.a.length;
    }

    public int getSize() {
        return this.a.length - this.b;
    }

    public ByteArrayInputStream toByteArrayInputStream(int i) {
        return new ByteArrayInputStream(this.a, this.b, i);
    }

    public ByteArrayInputStream toByteArrayInputStream() {
        return new ByteArrayInputStream(this.a, this.b, this.a.length - this.b);
    }

    public void add(byte... bArr) {
        if (bArr != null && bArr.length != 0) {
            Object obj = new byte[(this.a.length + bArr.length)];
            System.arraycopy(this.a, 0, obj, 0, this.a.length);
            System.arraycopy(bArr, 0, obj, this.a.length, bArr.length);
            this.a = obj;
        }
    }

    public void add(ByteArray byteArray) {
        if (byteArray != null) {
            Object obj = new byte[(this.a.length + byteArray.getSize())];
            System.arraycopy(this.a, 0, obj, 0, this.a.length);
            System.arraycopy(byteArray.rawBytes(), byteArray.getCursor(), obj, this.a.length, byteArray.getSize());
            this.a = obj;
        }
    }

    public byte get(int i) {
        return this.a[i];
    }

    public void set(int i, byte b) {
        this.a[i] = b;
    }

    public boolean valid() {
        return this.b < this.a.length && this.b > -1;
    }

    public int getIntOfByte() {
        return getByte() & 255;
    }

    protected byte getByte() {
        byte[] bArr = this.a;
        int i = this.b;
        this.b = i + 1;
        return bArr[i];
    }

    public int getInt() {
        int i = ((((this.a[this.b + 3] & 255) << 24) | ((this.a[this.b + 2] & 255) << 16)) | ((this.a[this.b + 1] & 255) << 8)) | ((this.a[this.b + 0] & 255) << 0);
        this.b += 4;
        return i;
    }

    public String getString(int i) {
        String str;
        try {
            str = new String(this.a, this.b, i, "utf-8");
        } catch (Exception e) {
            str = null;
        }
        this.b += i;
        return str;
    }

    public byte[] getByteArray(int i) {
        byte[] subarray = ArrayUtils.subarray(this.a, this.b, this.b + i);
        this.b += i;
        return subarray;
    }

    public Bitmap getBitmap(int i) {
        Bitmap decodeByteArray = BitmapFactory.decodeByteArray(this.a, this.b, i);
        this.b += i;
        return decodeByteArray;
    }
}
