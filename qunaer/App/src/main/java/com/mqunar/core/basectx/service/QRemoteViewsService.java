package com.mqunar.core.basectx.service;

import android.annotation.TargetApi;
import android.content.Context;
import android.widget.RemoteViewsService;

@TargetApi(11)
public abstract class QRemoteViewsService extends RemoteViewsService {
    public void onCreate() {
        super.onCreate();
        try {
            Class.forName("com.mqunar.core.QunarApkLoader").getDeclaredMethod("loadResource", new Class[]{Context.class}).invoke(null, new Object[]{this});
        } catch (Throwable th) {
        }
    }
}
