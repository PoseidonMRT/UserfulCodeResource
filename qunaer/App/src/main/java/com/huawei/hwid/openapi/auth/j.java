package com.huawei.hwid.openapi.auth;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import com.huawei.hwid.openapi.OpenHwID;
import com.huawei.hwid.openapi.out.OutReturn;
import com.huawei.hwid.openapi.quicklogin.d.b;
import com.huawei.hwid.openapi.quicklogin.d.b.d;
import com.iflytek.cloud.SpeechConstant;
import java.util.HashMap;

class j extends BroadcastReceiver {
    final /* synthetic */ f a;

    private j(f fVar) {
        this.a = fVar;
    }

    public void onReceive(Context context, Intent intent) {
        if (intent == null) {
            d.b("AuthHelper", "MyReceiver null == intent");
            return;
        }
        CharSequence action = intent.getAction();
        int intExtra = intent.getIntExtra(SpeechConstant.ISV_CMD, 0);
        Bundle bundle = intent.getExtras().getBundle("response");
        String nspstatus = OutReturn.getNSPSTATUS(bundle);
        int retCode = OutReturn.getRetCode(bundle);
        if ("6".equals(nspstatus) || "102".equals(nspstatus)) {
            OpenHwID.logout();
            OpenHwID.login(null);
        } else if (!TextUtils.isEmpty(action) && "com.huawei.hwid.opensdk.game.request.result".equals(action)) {
            d.b("AuthHelper", "MyReceiver:" + intExtra);
            switch (intExtra) {
                case 1000:
                    HashMap userInfo = OpenHwID.getUserInfo();
                    userInfo.put("loginStatus", "1");
                    this.a.a(userInfo);
                    return;
                case 1002:
                    if (103 == retCode) {
                        this.a.a(b.a(this.a.e, "xh_server_cer_error"));
                        return;
                    } else {
                        this.a.a(b.a(this.a.e, "xh_game_acct_fail"));
                        return;
                    }
                default:
                    return;
            }
        }
    }
}
