package com.huawei.hwid.openapi.update.ui;

import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Build.VERSION;
import com.huawei.hwid.openapi.quicklogin.d.a.c;

class d implements OnClickListener {
    final /* synthetic */ OtaDownloadActivity a;

    d(OtaDownloadActivity otaDownloadActivity) {
        this.a = otaDownloadActivity;
    }

    public void onClick(DialogInterface dialogInterface, int i) {
        this.a.d.a(false);
        if (VERSION.SDK_INT <= 22 || this.a.checkSelfPermission("android.permission.READ_PHONE_STATE") == 0) {
            c.b("OtaDownloadActivity", "startCheckVersion");
            this.a.a(new n(this.a));
            return;
        }
        c.b("OtaDownloadActivity", "have not permission READ_PHONE_STATE");
        this.a.requestPermissions(new String[]{"android.permission.READ_PHONE_STATE"}, 10002);
    }
}
