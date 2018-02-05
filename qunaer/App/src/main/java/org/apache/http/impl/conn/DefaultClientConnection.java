package org.apache.http.impl.conn;

import java.net.Socket;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.HttpResponseFactory;
import org.apache.http.conn.OperatedClientConnection;
import org.apache.http.impl.SocketHttpClientConnection;
import org.apache.http.io.HttpMessageParser;
import org.apache.http.io.SessionInputBuffer;
import org.apache.http.io.SessionOutputBuffer;
import org.apache.http.params.HttpParams;

@Deprecated
public class DefaultClientConnection extends SocketHttpClientConnection implements OperatedClientConnection {
    public DefaultClientConnection() {
        throw new RuntimeException("Stub!");
    }

    public final HttpHost getTargetHost() {
        throw new RuntimeException("Stub!");
    }

    public final boolean isSecure() {
        throw new RuntimeException("Stub!");
    }

    public final Socket getSocket() {
        throw new RuntimeException("Stub!");
    }

    public void opening(Socket socket, HttpHost httpHost) {
        throw new RuntimeException("Stub!");
    }

    public void openCompleted(boolean z, HttpParams httpParams) {
        throw new RuntimeException("Stub!");
    }

    public void shutdown() {
        throw new RuntimeException("Stub!");
    }

    public void close() {
        throw new RuntimeException("Stub!");
    }

    protected SessionInputBuffer createSessionInputBuffer(Socket socket, int i, HttpParams httpParams) {
        throw new RuntimeException("Stub!");
    }

    protected SessionOutputBuffer createSessionOutputBuffer(Socket socket, int i, HttpParams httpParams) {
        throw new RuntimeException("Stub!");
    }

    protected HttpMessageParser createResponseParser(SessionInputBuffer sessionInputBuffer, HttpResponseFactory httpResponseFactory, HttpParams httpParams) {
        throw new RuntimeException("Stub!");
    }

    public void update(Socket socket, HttpHost httpHost, boolean z, HttpParams httpParams) {
        throw new RuntimeException("Stub!");
    }

    public HttpResponse receiveResponseHeader() {
        throw new RuntimeException("Stub!");
    }

    public void sendRequestHeader(HttpRequest httpRequest) {
        throw new RuntimeException("Stub!");
    }
}
