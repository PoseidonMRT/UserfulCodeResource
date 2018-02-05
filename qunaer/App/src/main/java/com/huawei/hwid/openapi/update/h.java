package com.huawei.hwid.openapi.update;

import android.content.Context;

public final class h {
    private static h a;

    private h() {
    }

    public static synchronized h a() {
        h hVar;
        synchronized (h.class) {
            if (a == null) {
                a = new h();
            }
            hVar = a;
        }
        return hVar;
    }

    public i a(Context context, String str) {
        Object iVar = new i(context, str);
        new Thread(iVar, "InstallUpdateThread").start();
        return iVar;
    }
}
