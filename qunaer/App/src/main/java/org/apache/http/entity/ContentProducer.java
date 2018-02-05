package org.apache.http.entity;

import java.io.OutputStream;

@Deprecated
public interface ContentProducer {
    void writeTo(OutputStream outputStream);
}
