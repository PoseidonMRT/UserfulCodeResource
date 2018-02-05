package com.mqunar.network;

import org.apache.http.Header;
import org.apache.http.HeaderElement;
import org.apache.http.HttpResponse;
import org.apache.http.HttpResponseInterceptor;
import org.apache.http.protocol.HttpContext;

class h implements HttpResponseInterceptor {
    final /* synthetic */ NetRequestManager a;

    h(NetRequestManager netRequestManager) {
        this.a = netRequestManager;
    }

    public void process(HttpResponse httpResponse, HttpContext httpContext) {
        Header contentEncoding = httpResponse.getEntity().getContentEncoding();
        if (contentEncoding != null) {
            for (HeaderElement name : contentEncoding.getElements()) {
                if (name.getName().equalsIgnoreCase("gzip")) {
                    httpResponse.setEntity(new GzipDecompressingEntity(httpResponse.getEntity()));
                    return;
                }
            }
        }
    }
}
