package org.apache.http.impl.io;

import java.net.Socket;
import org.apache.http.params.HttpParams;

@Deprecated
public class SocketOutputBuffer extends AbstractSessionOutputBuffer {
    public SocketOutputBuffer(Socket socket, int i, HttpParams httpParams) {
        throw new RuntimeException("Stub!");
    }
}
