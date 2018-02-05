package com.huawei.hwid.openapi.update.b;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;

final class e implements OnCancelListener {
    final /* synthetic */ Context a;

    e(Context context) {
        this.a = context;
    }

    public void onCancel(DialogInterface dialogInterface) {
        ((Activity) this.a).finish();
    }
}
