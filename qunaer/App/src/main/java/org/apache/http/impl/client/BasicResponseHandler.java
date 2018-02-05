package org.apache.http.impl.client;

import org.apache.http.HttpResponse;
import org.apache.http.client.ResponseHandler;

@Deprecated
public class BasicResponseHandler implements ResponseHandler<String> {
    public BasicResponseHandler() {
        throw new RuntimeException("Stub!");
    }

    public String handleResponse(HttpResponse httpResponse) {
        throw new RuntimeException("Stub!");
    }
}
