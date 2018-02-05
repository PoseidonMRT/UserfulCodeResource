package org.apache.commons.io.output;

import java.io.OutputStream;

public class NullOutputStream extends OutputStream {
    public static final NullOutputStream NULL_OUTPUT_STREAM = new NullOutputStream();

    public void write(byte[] bArr, int i, int i2) {
    }

    public void write(int i) {
    }

    public void write(byte[] bArr) {
    }
}
