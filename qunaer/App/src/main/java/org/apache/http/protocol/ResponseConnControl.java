package org.apache.http.protocol;

import org.apache.http.HttpResponse;
import org.apache.http.HttpResponseInterceptor;

@Deprecated
public class ResponseConnControl implements HttpResponseInterceptor {
    public ResponseConnControl() {
        throw new RuntimeException("Stub!");
    }

    public void process(HttpResponse httpResponse, HttpContext httpContext) {
        throw new RuntimeException("Stub!");
    }
}
