package com.huawei.hwid.openapi.update.ui;

import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;

class f implements OnClickListener {
    final /* synthetic */ OtaDownloadActivity a;

    f(OtaDownloadActivity otaDownloadActivity) {
        this.a = otaDownloadActivity;
    }

    public void onClick(DialogInterface dialogInterface, int i) {
        dialogInterface.dismiss();
        this.a.finish();
    }
}
