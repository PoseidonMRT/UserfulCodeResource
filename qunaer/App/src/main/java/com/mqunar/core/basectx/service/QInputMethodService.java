package com.mqunar.core.basectx.service;

import android.content.Context;
import android.inputmethodservice.InputMethodService;

public abstract class QInputMethodService extends InputMethodService {
    public void onCreate() {
        super.onCreate();
        try {
            Class.forName("com.mqunar.core.QunarApkLoader").getDeclaredMethod("loadResource", new Class[]{Context.class}).invoke(null, new Object[]{this});
        } catch (Throwable th) {
        }
    }
}
