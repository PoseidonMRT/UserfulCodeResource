package com.android.internal.http.multipart;

import java.io.InputStream;

public interface PartSource {
    InputStream createInputStream();

    String getFileName();

    long getLength();
}
