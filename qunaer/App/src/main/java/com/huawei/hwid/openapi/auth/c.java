package com.huawei.hwid.openapi.auth;

import android.os.Handler;
import android.os.Message;
import com.huawei.hwid.openapi.a.a;

final class c extends Handler {
    c() {
    }

    public void dispatchMessage(Message message) {
        a aVar = (a) message.obj;
        switch (message.what) {
            case 1:
                a.a(aVar);
                break;
            case 2:
                b.g(aVar);
                b.b(aVar, true);
                break;
        }
        super.dispatchMessage(message);
    }
}
