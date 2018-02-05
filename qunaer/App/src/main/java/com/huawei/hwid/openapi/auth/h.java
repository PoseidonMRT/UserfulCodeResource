package com.huawei.hwid.openapi.auth;

import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import java.util.HashMap;

class h implements OnClickListener {
    final /* synthetic */ f a;

    private h(f fVar) {
        this.a = fVar;
    }

    public void onClick(DialogInterface dialogInterface, int i) {
        this.a.b.dismiss();
        HashMap hashMap = new HashMap();
        hashMap.put("loginStatus", "0");
        this.a.a(hashMap);
    }
}
