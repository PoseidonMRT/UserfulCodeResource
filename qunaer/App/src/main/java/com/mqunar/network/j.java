package com.mqunar.network;

import java.io.InputStream;
import java.io.OutputStream;
import org.apache.http.entity.InputStreamEntity;

class j extends InputStreamEntity {
    final /* synthetic */ OutputStreamWarp a;
    final /* synthetic */ NetRequestManager b;

    j(NetRequestManager netRequestManager, InputStream inputStream, long j, OutputStreamWarp outputStreamWarp) {
        this.b = netRequestManager;
        this.a = outputStreamWarp;
        super(inputStream, j);
    }

    public void writeTo(OutputStream outputStream) {
        this.a.setOutputStream(outputStream);
        super.writeTo(this.a);
    }
}
