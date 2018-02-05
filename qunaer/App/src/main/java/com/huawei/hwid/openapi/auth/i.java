package com.huawei.hwid.openapi.auth;

import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import com.huawei.hwid.openapi.OpenHwID;
import com.huawei.hwid.openapi.quicklogin.d.b.d;

class i implements OnClickListener {
    final /* synthetic */ f a;
    private int b = -1;

    public i(f fVar, int i) {
        this.a = fVar;
        this.b = i;
    }

    public void onClick(DialogInterface dialogInterface, int i) {
        this.a.b.dismiss();
        switch (this.b) {
            case 1006:
            case 1007:
                d.b("AuthHelper", "AUTH_TYPE_FROM:" + this.b);
                OpenHwID.login(null);
                return;
            default:
                return;
        }
    }
}
