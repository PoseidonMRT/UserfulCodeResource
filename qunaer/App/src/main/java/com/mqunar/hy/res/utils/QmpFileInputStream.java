package com.mqunar.hy.res.utils;

import java.io.File;
import java.io.InputStream;
import java.io.RandomAccessFile;

public class QmpFileInputStream extends InputStream {
    private int cursor = 0;
    private int length;
    private RandomAccessFile randomAccessFile;

    public QmpFileInputStream(String str, int i, int i2) {
        this.randomAccessFile = new RandomAccessFile(new File(str), "r");
        this.length = i2;
        this.randomAccessFile.seek((long) i);
    }

    public int read() {
        int i = this.cursor;
        this.cursor = i + 1;
        if (i < this.length) {
            return this.randomAccessFile.read();
        }
        return -1;
    }

    public int read(byte[] bArr) {
        if (this.cursor >= this.length) {
            return -1;
        }
        int read;
        if (bArr.length + this.cursor <= this.length) {
            read = this.randomAccessFile.read(bArr, 0, bArr.length);
        } else {
            read = this.randomAccessFile.read(bArr, 0, this.length - this.cursor);
        }
        if (read != -1) {
            this.cursor += read;
        }
        return read;
    }

    public int read(byte[] bArr, int i, int i2) {
        if (this.cursor >= this.length) {
            return -1;
        }
        int read;
        if (this.cursor + i2 <= this.length) {
            read = this.randomAccessFile.read(bArr, i, i2);
        } else {
            read = this.randomAccessFile.read(bArr, i, this.length - this.cursor);
        }
        if (read != -1) {
            this.cursor += read;
        }
        return read;
    }

    public int available() {
        return this.length - this.cursor;
    }
}
