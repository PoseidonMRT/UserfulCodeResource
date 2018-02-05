package com.mqunar.network;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import org.apache.http.HttpHost;
import org.apache.http.conn.OperatedClientConnection;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.impl.conn.DefaultClientConnectionOperator;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HttpContext;

class m extends DefaultClientConnectionOperator {
    final /* synthetic */ l a;

    m(l lVar, SchemeRegistry schemeRegistry) {
        this.a = lVar;
        super(schemeRegistry);
    }

    public void openConnection(OperatedClientConnection operatedClientConnection, HttpHost httpHost, InetAddress inetAddress, HttpContext httpContext, HttpParams httpParams) {
        String hostName = httpHost.getHostName();
        try {
            super.openConnection(operatedClientConnection, new HttpHost(this.a.a.a(hostName), httpHost.getPort(), httpHost.getSchemeName()), inetAddress, httpContext, httpParams);
        } catch (IOException e) {
            this.a.a.b(hostName);
            throw e;
        }
    }

    public void updateSecureConnection(OperatedClientConnection operatedClientConnection, HttpHost httpHost, HttpContext httpContext, HttpParams httpParams) {
        String hostName = httpHost.getHostName();
        try {
            super.updateSecureConnection(operatedClientConnection, new HttpHost(this.a.a.a(hostName), httpHost.getPort(), httpHost.getSchemeName()), httpContext, httpParams);
        } catch (IOException e) {
            this.a.a.b(hostName);
            throw e;
        }
    }

    protected void prepareSocket(Socket socket, HttpContext httpContext, HttpParams httpParams) {
        super.prepareSocket(socket, httpContext, httpParams);
    }
}
