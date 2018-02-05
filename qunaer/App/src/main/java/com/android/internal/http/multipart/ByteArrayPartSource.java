package com.android.internal.http.multipart;

import java.io.InputStream;

public class ByteArrayPartSource implements PartSource {
    public ByteArrayPartSource(String str, byte[] bArr) {
        throw new RuntimeException("Stub!");
    }

    public long getLength() {
        throw new RuntimeException("Stub!");
    }

    public String getFileName() {
        throw new RuntimeException("Stub!");
    }

    public InputStream createInputStream() {
        throw new RuntimeException("Stub!");
    }
}
