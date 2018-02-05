package org.apache.http.conn;

import java.io.InputStream;

@Deprecated
public class BasicEofSensorWatcher implements EofSensorWatcher {
    protected boolean attemptReuse;
    protected ManagedClientConnection managedConn;

    public BasicEofSensorWatcher(ManagedClientConnection managedClientConnection, boolean z) {
        throw new RuntimeException("Stub!");
    }

    public boolean eofDetected(InputStream inputStream) {
        throw new RuntimeException("Stub!");
    }

    public boolean streamClosed(InputStream inputStream) {
        throw new RuntimeException("Stub!");
    }

    public boolean streamAbort(InputStream inputStream) {
        throw new RuntimeException("Stub!");
    }
}
