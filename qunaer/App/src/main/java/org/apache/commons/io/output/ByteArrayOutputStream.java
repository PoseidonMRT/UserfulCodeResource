package org.apache.commons.io.output;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.SequenceInputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import org.apache.commons.io.input.ClosedInputStream;

public class ByteArrayOutputStream extends OutputStream {
    private static final byte[] EMPTY_BYTE_ARRAY = new byte[0];
    private final List<byte[]> buffers;
    private int count;
    private byte[] currentBuffer;
    private int currentBufferIndex;
    private int filledBufferSum;

    public ByteArrayOutputStream() {
        this(1024);
    }

    public ByteArrayOutputStream(int i) {
        this.buffers = new ArrayList();
        if (i < 0) {
            throw new IllegalArgumentException("Negative initial size: " + i);
        }
        synchronized (this) {
            needNewBuffer(i);
        }
    }

    private void needNewBuffer(int i) {
        if (this.currentBufferIndex < this.buffers.size() - 1) {
            this.filledBufferSum += this.currentBuffer.length;
            this.currentBufferIndex++;
            this.currentBuffer = (byte[]) this.buffers.get(this.currentBufferIndex);
            return;
        }
        if (this.currentBuffer == null) {
            this.filledBufferSum = 0;
        } else {
            i = Math.max(this.currentBuffer.length << 1, i - this.filledBufferSum);
            this.filledBufferSum += this.currentBuffer.length;
        }
        this.currentBufferIndex++;
        this.currentBuffer = new byte[i];
        this.buffers.add(this.currentBuffer);
    }

    public void write(byte[] bArr, int i, int i2) {
        if (i < 0 || i > bArr.length || i2 < 0 || i + i2 > bArr.length || i + i2 < 0) {
            throw new IndexOutOfBoundsException();
        } else if (i2 != 0) {
            synchronized (this) {
                int i3 = this.count + i2;
                int i4 = this.count - this.filledBufferSum;
                int i5 = i2;
                while (i5 > 0) {
                    int min = Math.min(i5, this.currentBuffer.length - i4);
                    System.arraycopy(bArr, (i + i2) - i5, this.currentBuffer, i4, min);
                    i5 -= min;
                    if (i5 > 0) {
                        needNewBuffer(i3);
                        i4 = 0;
                    }
                }
                this.count = i3;
            }
        }
    }

    public synchronized void write(int i) {
        int i2 = this.count - this.filledBufferSum;
        if (i2 == this.currentBuffer.length) {
            needNewBuffer(this.count + 1);
            i2 = 0;
        }
        this.currentBuffer[i2] = (byte) i;
        this.count++;
    }

    public synchronized int write(InputStream inputStream) {
        int i;
        int i2 = this.count - this.filledBufferSum;
        i = 0;
        int i3 = i2;
        i2 = inputStream.read(this.currentBuffer, i2, this.currentBuffer.length - i2);
        int i4 = i3;
        while (i2 != -1) {
            i += i2;
            i4 += i2;
            this.count = i2 + this.count;
            if (i4 == this.currentBuffer.length) {
                needNewBuffer(this.currentBuffer.length);
                i4 = 0;
            }
            i2 = inputStream.read(this.currentBuffer, i4, this.currentBuffer.length - i4);
        }
        return i;
    }

    public synchronized int size() {
        return this.count;
    }

    public void close() {
    }

    public synchronized void reset() {
        this.count = 0;
        this.filledBufferSum = 0;
        this.currentBufferIndex = 0;
        this.currentBuffer = (byte[]) this.buffers.get(this.currentBufferIndex);
    }

    public synchronized void writeTo(OutputStream outputStream) {
        int i = this.count;
        int i2 = i;
        for (byte[] bArr : this.buffers) {
            int min = Math.min(bArr.length, i2);
            outputStream.write(bArr, 0, min);
            i = i2 - min;
            if (i == 0) {
                break;
            }
            i2 = i;
        }
    }

    public static InputStream toBufferedInputStream(InputStream inputStream) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byteArrayOutputStream.write(inputStream);
        return byteArrayOutputStream.toBufferedInputStream();
    }

    private InputStream toBufferedInputStream() {
        int i = this.count;
        if (i == 0) {
            return new ClosedInputStream();
        }
        Collection arrayList = new ArrayList(this.buffers.size());
        int i2 = i;
        for (byte[] bArr : this.buffers) {
            int min = Math.min(bArr.length, i2);
            arrayList.add(new ByteArrayInputStream(bArr, 0, min));
            i = i2 - min;
            if (i == 0) {
                break;
            }
            i2 = i;
        }
        return new SequenceInputStream(Collections.enumeration(arrayList));
    }

    public synchronized byte[] toByteArray() {
        byte[] bArr;
        int i = this.count;
        if (i == 0) {
            bArr = EMPTY_BYTE_ARRAY;
        } else {
            Object obj = new byte[i];
            int i2 = i;
            i = 0;
            for (byte[] bArr2 : this.buffers) {
                int min = Math.min(bArr2.length, i2);
                System.arraycopy(bArr2, 0, obj, i, min);
                int i3 = i + min;
                i = i2 - min;
                if (i == 0) {
                    break;
                }
                i2 = i;
                i = i3;
            }
            Object obj2 = obj;
        }
        return bArr2;
    }

    public String toString() {
        return new String(toByteArray());
    }

    public String toString(String str) {
        return new String(toByteArray(), str);
    }
}
