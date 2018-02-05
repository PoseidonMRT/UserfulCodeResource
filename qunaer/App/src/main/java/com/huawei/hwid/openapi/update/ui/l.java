package com.huawei.hwid.openapi.update.ui;

import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;

class l implements OnClickListener {
    final /* synthetic */ OtaDownloadActivity a;

    l(OtaDownloadActivity otaDownloadActivity) {
        this.a = otaDownloadActivity;
    }

    public void onClick(DialogInterface dialogInterface, int i) {
        this.a.f.a(true);
        this.a.f.dismiss();
        this.a.finish();
    }
}
