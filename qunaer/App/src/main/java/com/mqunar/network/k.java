package com.mqunar.network;

import java.io.OutputStream;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;

class k extends MultipartEntity {
    final /* synthetic */ OutputStreamWarp a;
    final /* synthetic */ NetRequestManager b;

    k(NetRequestManager netRequestManager, HttpMultipartMode httpMultipartMode, OutputStreamWarp outputStreamWarp) {
        this.b = netRequestManager;
        this.a = outputStreamWarp;
        super(httpMultipartMode);
    }

    public void writeTo(OutputStream outputStream) {
        this.a.setOutputStream(outputStream);
        super.writeTo(this.a);
    }
}
