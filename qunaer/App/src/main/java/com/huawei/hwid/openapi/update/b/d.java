package com.huawei.hwid.openapi.update.b;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;

final class d implements OnClickListener {
    final /* synthetic */ Context a;

    d(Context context) {
        this.a = context;
    }

    public void onClick(DialogInterface dialogInterface, int i) {
        ((Activity) this.a).finish();
    }
}
