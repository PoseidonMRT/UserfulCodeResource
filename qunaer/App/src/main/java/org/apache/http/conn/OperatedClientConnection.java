package org.apache.http.conn;

import java.net.Socket;
import org.apache.http.HttpClientConnection;
import org.apache.http.HttpHost;
import org.apache.http.HttpInetConnection;
import org.apache.http.params.HttpParams;

@Deprecated
public interface OperatedClientConnection extends HttpClientConnection, HttpInetConnection {
    Socket getSocket();

    HttpHost getTargetHost();

    boolean isSecure();

    void openCompleted(boolean z, HttpParams httpParams);

    void opening(Socket socket, HttpHost httpHost);

    void update(Socket socket, HttpHost httpHost, boolean z, HttpParams httpParams);
}
