package org.apache.http;

import org.apache.http.protocol.HttpContext;

@Deprecated
public interface HttpRequestInterceptor {
    void process(HttpRequest httpRequest, HttpContext httpContext);
}
