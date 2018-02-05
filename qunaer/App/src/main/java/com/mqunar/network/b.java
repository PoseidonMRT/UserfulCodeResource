package com.mqunar.network;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import org.apache.http.HttpEntity;
import org.apache.http.entity.HttpEntityWrapper;

abstract class b extends HttpEntityWrapper {
    private InputStream a;

    abstract InputStream a(InputStream inputStream);

    public b(HttpEntity httpEntity) {
        super(httpEntity);
    }

    private InputStream a() {
        InputStream content = this.wrappedEntity.getContent();
        try {
            return a(content);
        } catch (IOException e) {
            content.close();
            throw e;
        }
    }

    public InputStream getContent() {
        if (!this.wrappedEntity.isStreaming()) {
            return a();
        }
        if (this.a == null) {
            this.a = a();
        }
        return this.a;
    }

    public void writeTo(OutputStream outputStream) {
        if (outputStream == null) {
            throw new IllegalArgumentException("Output stream may not be null");
        }
        InputStream content = getContent();
        try {
            byte[] bArr = new byte[2048];
            while (true) {
                int read = content.read(bArr);
                if (read == -1) {
                    break;
                }
                outputStream.write(bArr, 0, read);
            }
        } finally {
            content.close();
        }
    }
}
