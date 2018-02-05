package com.huawei.hwid.openapi.update;

import android.os.Handler;
import android.os.Message;
import com.huawei.hwid.openapi.quicklogin.d.a.c;
import java.util.Map;

public abstract class a extends Handler {
    public abstract void a(int i, Map map);

    public abstract void a(Map map);

    public void handleMessage(Message message) {
        super.handleMessage(message);
        Map map = (Map) message.obj;
        switch (message.what) {
            case 1:
                c.b("OtaCheckVersionHandler", "entry CHECK_VERSION_FINISHED");
                a(map);
                return;
            case 2:
                c.b("OtaCheckVersionHandler", "entry not hasNewVersion");
                a(2, map);
                return;
            case 7:
                c.b("OtaCheckVersionHandler", "entry check version error");
                a(7, map);
                return;
            default:
                c.b("OtaCheckVersionHandler", "entry check version default");
                return;
        }
    }
}
