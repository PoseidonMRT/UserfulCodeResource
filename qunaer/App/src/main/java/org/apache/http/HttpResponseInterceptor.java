package org.apache.http;

import org.apache.http.protocol.HttpContext;

@Deprecated
public interface HttpResponseInterceptor {
    void process(HttpResponse httpResponse, HttpContext httpContext);
}
