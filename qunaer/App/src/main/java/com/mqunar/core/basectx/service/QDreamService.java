package com.mqunar.core.basectx.service;

import android.annotation.TargetApi;
import android.content.Context;
import android.service.dreams.DreamService;

@TargetApi(17)
public abstract class QDreamService extends DreamService {
    public void onCreate() {
        super.onCreate();
        try {
            Class.forName("com.mqunar.core.QunarApkLoader").getDeclaredMethod("loadResource", new Class[]{Context.class}).invoke(null, new Object[]{this});
        } catch (Throwable th) {
        }
    }
}
