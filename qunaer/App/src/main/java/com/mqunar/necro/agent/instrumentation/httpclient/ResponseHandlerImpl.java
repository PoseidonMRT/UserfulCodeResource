package com.mqunar.necro.agent.instrumentation.httpclient;

import com.mqunar.necro.agent.instrumentation.TransactionState;
import com.mqunar.necro.agent.instrumentation.TransactionStateUtil;
import org.apache.http.HttpResponse;
import org.apache.http.client.ResponseHandler;

public class ResponseHandlerImpl<T> implements ResponseHandler<T> {
    private final ResponseHandler<T> impl;
    private final TransactionState transactionState;

    private ResponseHandlerImpl(ResponseHandler<T> responseHandler, TransactionState transactionState) {
        this.impl = responseHandler;
        this.transactionState = transactionState;
    }

    public T handleResponse(HttpResponse httpResponse) {
        TransactionStateUtil.inspectAndInstrument(this.transactionState, httpResponse);
        return this.impl.handleResponse(httpResponse);
    }

    public static <T> ResponseHandler<? extends T> wrap(ResponseHandler<? extends T> responseHandler, TransactionState transactionState) {
        return new ResponseHandlerImpl(responseHandler, transactionState);
    }
}
