package com.mqunar.libtask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.DataFormatException;
import java.util.zip.Inflater;

class t {
    public static byte[] a(byte[] bArr, int i, int i2) {
        Inflater inflater = new Inflater();
        inflater.setInput(bArr, i, i2);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(bArr.length);
        byte[] bArr2 = new byte[1024];
        while (!inflater.finished()) {
            try {
                byteArrayOutputStream.write(bArr2, 0, inflater.inflate(bArr2));
            } catch (DataFormatException e) {
            }
        }
        inflater.end();
        try {
            byteArrayOutputStream.close();
        } catch (IOException e2) {
        }
        return byteArrayOutputStream.toByteArray();
    }
}
