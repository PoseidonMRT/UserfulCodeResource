package com.mqunar.idscan.activity;

import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;

final class d implements OnClickListener {
    final /* synthetic */ QrScanPassportActivity a;

    d(QrScanPassportActivity qrScanPassportActivity) {
        this.a = qrScanPassportActivity;
    }

    public final void onClick(DialogInterface dialogInterface, int i) {
        dialogInterface.dismiss();
        this.a.finish();
    }
}
