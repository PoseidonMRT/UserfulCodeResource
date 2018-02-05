package com.huawei.hwid.openapi.update.ui;

import android.content.DialogInterface;
import android.content.DialogInterface.OnKeyListener;
import android.view.KeyEvent;
import com.huawei.hwid.openapi.quicklogin.d.a.c;

class i implements OnKeyListener {
    final /* synthetic */ OtaDownloadActivity a;

    i(OtaDownloadActivity otaDownloadActivity) {
        this.a = otaDownloadActivity;
    }

    public boolean onKey(DialogInterface dialogInterface, int i, KeyEvent keyEvent) {
        if (i == 4 && keyEvent.getRepeatCount() == 0 && keyEvent.getAction() == 0) {
            c.b("OtaDownloadActivity", "onKey keycode_back");
            this.a.d();
        }
        return false;
    }
}
