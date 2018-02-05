package org.apache.http.impl;

import java.net.Socket;
import org.apache.http.params.HttpParams;

@Deprecated
public class DefaultHttpServerConnection extends SocketHttpServerConnection {
    public DefaultHttpServerConnection() {
        throw new RuntimeException("Stub!");
    }

    public void bind(Socket socket, HttpParams httpParams) {
        throw new RuntimeException("Stub!");
    }

    public String toString() {
        throw new RuntimeException("Stub!");
    }
}
