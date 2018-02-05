package com.mqunar.network;

import java.io.OutputStream;
import org.apache.http.entity.ByteArrayEntity;

class i extends ByteArrayEntity {
    final /* synthetic */ OutputStreamWarp a;
    final /* synthetic */ NetRequestManager b;

    i(NetRequestManager netRequestManager, byte[] bArr, OutputStreamWarp outputStreamWarp) {
        this.b = netRequestManager;
        this.a = outputStreamWarp;
        super(bArr);
    }

    public void writeTo(OutputStream outputStream) {
        this.a.setOutputStream(outputStream);
        super.writeTo(this.a);
    }
}
