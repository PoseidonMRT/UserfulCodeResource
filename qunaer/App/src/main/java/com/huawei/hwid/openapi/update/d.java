package com.huawei.hwid.openapi.update;

import android.os.Handler;
import android.os.Message;
import com.huawei.hwid.openapi.quicklogin.d.a.c;

public abstract class d extends Handler {
    public abstract void a();

    public abstract void a(int i);

    public abstract void a(int i, int i2);

    public void handleMessage(Message message) {
        super.handleMessage(message);
        switch (message.what) {
            case 3:
                c.b("OtaDownloadHandler", "entry DOWNLOAD_SHOW_PROGRESS");
                a(message.arg1, message.arg2);
                return;
            case 4:
                c.b("OtaDownloadHandler", "entry DOWNLOAD_VERSION_FAILURE");
                a(2);
                return;
            case 5:
                c.b("OtaDownloadHandler", "entry APK_DOWNLOAD_COMPLETED");
                a();
                return;
            case 11:
                return;
            default:
                c.b("OtaDownloadHandler", "entry download default");
                return;
        }
    }
}
