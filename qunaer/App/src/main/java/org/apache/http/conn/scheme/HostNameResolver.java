package org.apache.http.conn.scheme;

import java.net.InetAddress;

public interface HostNameResolver {
    InetAddress resolve(String str);
}
