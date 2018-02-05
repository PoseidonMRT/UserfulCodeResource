package org.apache.http.conn;

@Deprecated
public interface ConnectionReleaseTrigger {
    void abortConnection();

    void releaseConnection();
}
