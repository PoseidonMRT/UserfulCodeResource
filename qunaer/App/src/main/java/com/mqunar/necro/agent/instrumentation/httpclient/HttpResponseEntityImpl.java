package com.mqunar.necro.agent.instrumentation.httpclient;

import com.mqunar.necro.agent.instrumentation.TransactionState;
import com.mqunar.necro.agent.instrumentation.TransactionStateUtil;
import com.mqunar.necro.agent.instrumentation.io.CountingInputStream;
import com.mqunar.necro.agent.instrumentation.io.CountingOutputStream;
import com.mqunar.necro.agent.instrumentation.io.StreamCompleteEvent;
import com.mqunar.necro.agent.instrumentation.io.StreamCompleteListener;
import com.mqunar.necro.agent.instrumentation.io.StreamCompleteListenerSource;
import com.mqunar.necro.agent.logging.AgentLog;
import com.mqunar.necro.agent.logging.AgentLogManager;
import java.io.InputStream;
import java.io.OutputStream;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.entity.HttpEntityWrapper;
import org.apache.http.message.AbstractHttpMessage;

public class HttpResponseEntityImpl implements StreamCompleteListener, HttpEntity {
    private static final String ENCODING_CHUNKED = "chunked";
    private static final String TRANSFER_ENCODING_HEADER = "Transfer-Encoding";
    private static final AgentLog log = AgentLogManager.getAgentLog();
    private final long contentLengthFromHeader;
    private CountingInputStream contentStream;
    private final HttpEntity impl;
    private final TransactionState transactionState;

    public HttpResponseEntityImpl(HttpEntity httpEntity, TransactionState transactionState, long j) {
        this.impl = httpEntity;
        this.transactionState = transactionState;
        this.contentLengthFromHeader = j;
    }

    public void consumeContent() {
        try {
            this.impl.consumeContent();
        } catch (Exception e) {
            handleException(e);
            throw e;
        }
    }

    public InputStream getContent() {
        boolean z = true;
        if (this.contentStream != null) {
            return this.contentStream;
        }
        try {
            if (this.impl instanceof AbstractHttpMessage) {
                boolean z2;
                Header lastHeader = ((AbstractHttpMessage) this.impl).getLastHeader("Transfer-Encoding");
                if (lastHeader == null || !"chunked".equalsIgnoreCase(lastHeader.getValue())) {
                    z2 = true;
                } else {
                    z2 = false;
                }
                z = z2;
            } else if ((this.impl instanceof HttpEntityWrapper) && ((HttpEntityWrapper) this.impl).isChunked()) {
                z = false;
            }
            this.contentStream = new CountingInputStream(this.impl.getContent(), z);
            this.contentStream.addStreamCompleteListener(this);
            return this.contentStream;
        } catch (Exception e) {
            handleException(e);
            throw e;
        }
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
        if (this.transactionState.isComplete()) {
            this.impl.writeTo(outputStream);
            return;
        }
        OutputStream countingOutputStream = new CountingOutputStream(outputStream);
        try {
            this.impl.writeTo(countingOutputStream);
            if (!this.transactionState.isComplete()) {
                if (this.contentLengthFromHeader >= 0) {
                    this.transactionState.setBytesReceived(this.contentLengthFromHeader);
                } else {
                    this.transactionState.setBytesReceived(countingOutputStream.getCount());
                }
                addTransactionAndErrorData(this.transactionState);
            }
        } catch (Exception e) {
            handleException(e, Long.valueOf(countingOutputStream.getCount()));
            e.printStackTrace();
            throw e;
        }
    }

    public void streamComplete(StreamCompleteEvent streamCompleteEvent) {
        ((StreamCompleteListenerSource) streamCompleteEvent.getSource()).removeStreamCompleteListener(this);
        if (!this.transactionState.isComplete()) {
            if (this.contentLengthFromHeader >= 0) {
                this.transactionState.setBytesReceived(this.contentLengthFromHeader);
            } else {
                this.transactionState.setBytesReceived(streamCompleteEvent.getBytes());
            }
            addTransactionAndErrorData(this.transactionState);
        }
    }

    public void streamError(StreamCompleteEvent streamCompleteEvent) {
        ((StreamCompleteListenerSource) streamCompleteEvent.getSource()).removeStreamCompleteListener(this);
        TransactionStateUtil.setErrorCodeFromException(this.transactionState, streamCompleteEvent.getException());
        if (!this.transactionState.isComplete()) {
            this.transactionState.setBytesReceived(streamCompleteEvent.getBytes());
        }
    }

    private void addTransactionAndErrorData(TransactionState transactionState) {
        TransactionStateUtil.end(transactionState);
    }

    private void handleException(Exception exception) {
        handleException(exception, (Long) null);
    }

    private void handleException(Exception exception, Long l) {
        TransactionStateUtil.setErrorCodeFromException(this.transactionState, exception);
        if (!this.transactionState.isComplete()) {
            if (l != null) {
                this.transactionState.setBytesReceived(l.longValue());
            }
            TransactionStateUtil.end(this.transactionState);
        }
    }
}
