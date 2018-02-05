package com.mqunar.network;

public class ArrayIndexOutOfBoundsException extends IndexOutOfBoundsException {
    private static final long serialVersionUID = -5116101128118950844L;

    public ArrayIndexOutOfBoundsException(int i) {
        super("index=" + i);
    }

    public ArrayIndexOutOfBoundsException(String str) {
        super(str);
    }

    public ArrayIndexOutOfBoundsException(int i, int i2) {
        super("length=" + i + "; index=" + i2);
    }

    public ArrayIndexOutOfBoundsException(int i, int i2, int i3) {
        super("length=" + i + "; regionStart=" + i2 + "; regionLength=" + i3);
    }
}
