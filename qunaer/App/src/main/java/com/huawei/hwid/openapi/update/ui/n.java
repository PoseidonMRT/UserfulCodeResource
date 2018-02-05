package com.huawei.hwid.openapi.update.ui;

import com.huawei.hwid.openapi.quicklogin.d.a.c;
import com.huawei.hwid.openapi.update.a;
import com.huawei.hwid.openapi.update.a.b;
import com.huawei.hwid.openapi.update.e;
import java.util.Map;

public class n extends a {
    final /* synthetic */ OtaDownloadActivity a;

    public n(OtaDownloadActivity otaDownloadActivity) {
        this.a = otaDownloadActivity;
    }

    public void a(int i, Map map) {
        c.b("OtaDownloadActivity", "handleCheckFailed");
        this.a.b();
        e.a().a(false);
        e.a().c();
        this.a.i();
    }

    public void a(Map map) {
        this.a.b();
        o oVar = new o(this.a);
        e.a().a(false);
        e.a().a(map);
        if (map == null) {
            c.b("OtaDownloadActivity", "versionInfo is null");
            this.a.finish();
        } else if (this.a.a((b) map.get(Integer.valueOf(this.a.g)))) {
            this.a.j();
        } else {
            this.a.a(oVar);
        }
    }
}
