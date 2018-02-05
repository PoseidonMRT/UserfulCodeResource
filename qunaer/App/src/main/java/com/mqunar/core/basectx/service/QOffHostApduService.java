package com.mqunar.core.basectx.service;

import android.annotation.TargetApi;
import android.content.Context;
import android.nfc.cardemulation.OffHostApduService;

@TargetApi(19)
public abstract class QOffHostApduService extends OffHostApduService {
    public void onCreate() {
        super.onCreate();
        try {
            Class.forName("com.mqunar.core.QunarApkLoader").getDeclaredMethod("loadResource", new Class[]{Context.class}).invoke(null, new Object[]{this});
        } catch (Throwable th) {
        }
    }
}
