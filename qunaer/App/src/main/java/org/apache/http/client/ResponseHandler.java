package org.apache.http.client;

import org.apache.http.HttpResponse;

@Deprecated
public interface ResponseHandler<T> {
    T handleResponse(HttpResponse httpResponse);
}
