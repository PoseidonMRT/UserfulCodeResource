package com.huawei.hwid.openapi.update.ui;

import com.huawei.hwid.openapi.quicklogin.d.a.c;
import com.huawei.hwid.openapi.update.d;

public class o extends d {
    final /* synthetic */ OtaDownloadActivity a;

    public o(OtaDownloadActivity otaDownloadActivity) {
        this.a = otaDownloadActivity;
    }

    public void a(int i, int i2) {
        this.a.a(i, i2);
    }

    public void a(int i) {
        c.b("OtaDownloadActivity", "handleDownloadFailed");
        this.a.e();
    }

    public void a() {
        this.a.f();
        this.a.g();
        this.a.j();
    }
}
