package com.mqunar.core.basectx.service;

import android.annotation.TargetApi;
import android.content.Context;
import android.nfc.cardemulation.HostApduService;

@TargetApi(19)
public abstract class QHostApduService extends HostApduService {
    public void onCreate() {
        super.onCreate();
        try {
            Class.forName("com.mqunar.core.QunarApkLoader").getDeclaredMethod("loadResource", new Class[]{Context.class}).invoke(null, new Object[]{this});
        } catch (Throwable th) {
        }
    }
}
