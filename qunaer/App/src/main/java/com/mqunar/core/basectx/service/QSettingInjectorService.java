package com.mqunar.core.basectx.service;

import android.annotation.TargetApi;
import android.content.Context;
import android.location.SettingInjectorService;

@TargetApi(19)
public abstract class QSettingInjectorService extends SettingInjectorService {
    public QSettingInjectorService(String str) {
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
