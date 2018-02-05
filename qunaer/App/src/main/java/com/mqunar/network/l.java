package com.mqunar.network;

import org.apache.http.conn.ClientConnectionOperator;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.HttpParams;

class l extends ThreadSafeClientConnManager {
    final /* synthetic */ NetRequestManager a;

    public l(NetRequestManager netRequestManager, HttpParams httpParams, SchemeRegistry schemeRegistry) {
        this.a = netRequestManager;
        super(httpParams, schemeRegistry);
    }

    protected ClientConnectionOperator createConnectionOperator(SchemeRegistry schemeRegistry) {
        return new m(this, schemeRegistry);
    }

    public void shutdown() {
    }
}
