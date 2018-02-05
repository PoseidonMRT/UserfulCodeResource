package com.huawei.hwid.openapi.auth;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import com.huawei.hwid.openapi.a.a;
import com.huawei.hwid.openapi.e.b;
import com.huawei.hwid.openapi.e.c;
import com.huawei.hwid.openapi.out.OutReturn.ParamStr;
import com.huawei.hwid.openapi.quicklogin.d.b.d;

class e extends BroadcastReceiver {
    private a a;

    e(a aVar) {
        this.a = aVar;
    }

    public synchronized void onReceive(Context context, Intent intent) {
        d.b(b.a, "onReceive");
        if (intent != null) {
            try {
                CharSequence action = intent.getAction();
                c.a(context).a((BroadcastReceiver) this);
                if (!TextUtils.isEmpty(action)) {
                    if ("com.huawei.cloudserive.getSTCancel".equals(action)) {
                        d.a(b.a, "ACTION_GET_ST_FAILED:" + intent.getBundleExtra("bundle"));
                        this.a.b.finish(intent.getBundleExtra("bundle"));
                    } else if ("com.huawei.cloudserive.getSTSuccess".equals(action)) {
                        Bundle bundleExtra = intent.getBundleExtra("bundle");
                        if (bundleExtra != null) {
                            this.a.j = bundleExtra.getString("authToken");
                            int i = bundleExtra.getInt("siteId");
                            String string = bundleExtra.getString("userId");
                            b.a(this.a.a, "loginUserName", bundleExtra.getString("loginUserName"));
                            if (b.h(this.a) && b.b(this.a, string)) {
                                String a = com.huawei.hwid.openapi.b.d.a(this.a.a, com.huawei.hwid.openapi.quicklogin.a.a().b(), null, null);
                                if (b.i(this.a)) {
                                    Bundle bundle = new Bundle();
                                    bundle.putInt(ParamStr.RET_CODE, 1);
                                    bundle.putString(ParamStr.ACCESS_TOKEN, a);
                                    this.a.b.finish(bundle);
                                }
                            }
                            b.b(context, this.a, i);
                        }
                    }
                }
            } catch (Throwable th) {
                d.b(b.a, th.getMessage(), th);
            }
        }
    }
}
