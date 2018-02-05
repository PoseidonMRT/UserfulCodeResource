package com.huawei.hwid.openapi.update.ui;

import android.app.ProgressDialog;
import android.content.Context;
import android.view.KeyEvent;

class a extends ProgressDialog {
    final /* synthetic */ BaseActivity a;

    a(BaseActivity baseActivity, Context context) {
        this.a = baseActivity;
        super(context);
    }

    public boolean onKeyDown(int i, KeyEvent keyEvent) {
        if (this.a.a(i, keyEvent)) {
            return true;
        }
        return super.onKeyDown(i, keyEvent);
    }
}
