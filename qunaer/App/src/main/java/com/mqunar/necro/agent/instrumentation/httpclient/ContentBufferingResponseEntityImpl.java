package com.mqunar.necro.agent.instrumentation.httpclient;

import com.mqunar.necro.agent.instrumentation.io.CountingInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import org.apache.http.Header;
import org.apache.http.HttpEntity;

public class ContentBufferingResponseEntityImpl implements HttpEntity {
    private CountingInputStream contentStream;
    final HttpEntity impl;

    public ContentBufferingResponseEntityImpl(HttpEntity httpEntity) {
        if (httpEntity == null) {
            throw new IllegalArgumentException("Missing wrapped entity");
        }
        this.impl = httpEntity;
    }

    public void consumeContent() {
        this.impl.consumeContent();
    }

    public InputStream getContent() {
        if (this.contentStream != null) {
            return this.contentStream;
        }
        this.contentStream = new CountingInputStream(this.impl.getContent(), true);
        return this.contentStream;
    }

    public Header getContentEncoding() {
        return this.impl.getContentEncoding();
    }

    public long getContentLength() {
        return this.impl.getContentLength();
    }

    public Header getContentType() {
        return this.impl.getContentType();
    }

    public boolean isChunked() {
        return this.impl.isChunked();
    }

    public boolean isRepeatable() {
        return this.impl.isRepeatable();
    }

    public boolean isStreaming() {
        return this.impl.isStreaming();
    }

    public void writeTo(OutputStream outputStream) {
        this.impl.writeTo(outputStream);
    }
}
