package org.apache.http.impl.conn;

import org.apache.http.HttpHost;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.OperatedClientConnection;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HttpContext;

@Deprecated
public abstract class AbstractPooledConnAdapter extends AbstractClientConnAdapter {
    protected volatile AbstractPoolEntry poolEntry;

    protected AbstractPooledConnAdapter(ClientConnectionManager clientConnectionManager, AbstractPoolEntry abstractPoolEntry) {
        super((ClientConnectionManager) null, (OperatedClientConnection) null);
        throw new RuntimeException("Stub!");
    }

    protected final void assertAttached() {
        throw new RuntimeException("Stub!");
    }

    protected void detach() {
        throw new RuntimeException("Stub!");
    }

    public HttpRoute getRoute() {
        throw new RuntimeException("Stub!");
    }

    public void open(HttpRoute httpRoute, HttpContext httpContext, HttpParams httpParams) {
        throw new RuntimeException("Stub!");
    }

    public void tunnelTarget(boolean z, HttpParams httpParams) {
        throw new RuntimeException("Stub!");
    }

    public void tunnelProxy(HttpHost httpHost, boolean z, HttpParams httpParams) {
        throw new RuntimeException("Stub!");
    }

    public void layerProtocol(HttpContext httpContext, HttpParams httpParams) {
        throw new RuntimeException("Stub!");
    }

    public void close() {
        throw new RuntimeException("Stub!");
    }

    public void shutdown() {
        throw new RuntimeException("Stub!");
    }

    public Object getState() {
        throw new RuntimeException("Stub!");
    }

    public void setState(Object obj) {
        throw new RuntimeException("Stub!");
    }
}
