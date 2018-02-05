package com.huawei.hwid.openapi.update.ui;

import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import com.huawei.hwid.openapi.quicklogin.d.a.b;
import com.huawei.hwid.openapi.update.b.c;

class k implements OnClickListener {
    final /* synthetic */ OtaDownloadActivity a;

    k(OtaDownloadActivity otaDownloadActivity) {
        this.a = otaDownloadActivity;
    }

    public void onClick(DialogInterface dialogInterface, int i) {
        this.a.f.a(false);
        if (b.a(this.a)) {
            this.a.a(new o(this.a));
            this.a.f.a(true);
            this.a.f.dismiss();
            return;
        }
        this.a.a(c.a(this.a, com.huawei.hwid.openapi.quicklogin.d.b.a(this.a, "CS_network_connect_error"), com.huawei.hwid.openapi.quicklogin.d.b.a(this.a, "CS_server_unavailable_title"), false).show());
    }
}
