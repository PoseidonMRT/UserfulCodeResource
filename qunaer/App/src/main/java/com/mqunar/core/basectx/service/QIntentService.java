package com.mqunar.core.basectx.service;

import android.app.IntentService;
import android.content.Context;

public abstract class QIntentService extends IntentService {
    public QIntentService(String str) {
        super(str);
    }

    public void onCreate() {
        super.onCreate();
        try {
            Class.forName("com.mqunar.core.QunarApkLoader").getDeclaredMethod("loadResource", new Class[]{Context.class}).invoke(null, new Object[]{this});
        } catch (Throwable th) {
        }
    }
}
