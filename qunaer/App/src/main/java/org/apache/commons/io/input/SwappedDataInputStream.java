package org.apache.commons.io.input;

import java.io.DataInput;
import java.io.EOFException;
import java.io.InputStream;
import org.apache.commons.io.EndianUtils;

public class SwappedDataInputStream extends ProxyInputStream implements DataInput {
    public SwappedDataInputStream(InputStream inputStream) {
        super(inputStream);
    }

    public boolean readBoolean() {
        return readByte() != (byte) 0;
    }

    public byte readByte() {
        return (byte) this.in.read();
    }

    public char readChar() {
        return (char) readShort();
    }

    public double readDouble() {
        return EndianUtils.readSwappedDouble(this.in);
    }

    public float readFloat() {
        return EndianUtils.readSwappedFloat(this.in);
    }

    public void readFully(byte[] bArr) {
        readFully(bArr, 0, bArr.length);
    }

    public void readFully(byte[] bArr, int i, int i2) {
        int i3 = i2;
        while (i3 > 0) {
            int read = read(bArr, (i + i2) - i3, i3);
            if (-1 == read) {
                throw new EOFException();
            }
            i3 -= read;
        }
    }

    public int readInt() {
        return EndianUtils.readSwappedInteger(this.in);
    }

    public String readLine() {
        throw new UnsupportedOperationException("Operation not supported: readLine()");
    }

    public long readLong() {
        return EndianUtils.readSwappedLong(this.in);
    }

    public short readShort() {
        return EndianUtils.readSwappedShort(this.in);
    }

    public int readUnsignedByte() {
        return this.in.read();
    }

    public int readUnsignedShort() {
        return EndianUtils.readSwappedUnsignedShort(this.in);
    }

    public String readUTF() {
        throw new UnsupportedOperationException("Operation not supported: readUTF()");
    }

    public int skipBytes(int i) {
        return (int) this.in.skip((long) i);
    }
}
