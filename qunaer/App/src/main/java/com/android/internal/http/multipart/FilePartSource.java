package com.android.internal.http.multipart;

import java.io.File;
import java.io.InputStream;

public class FilePartSource implements PartSource {
    public FilePartSource(File file) {
        throw new RuntimeException("Stub!");
    }

    public FilePartSource(String str, File file) {
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
