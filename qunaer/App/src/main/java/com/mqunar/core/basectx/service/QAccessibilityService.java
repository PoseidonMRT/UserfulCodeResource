package com.mqunar.core.basectx.service;

import android.accessibilityservice.AccessibilityService;
import android.content.Context;

public abstract class QAccessibilityService extends AccessibilityService {
    public void onCreate() {
        super.onCreate();
        try {
            Class.forName("com.mqunar.core.QunarApkLoader").getDeclaredMethod("loadResource", new Class[]{Context.class}).invoke(null, new Object[]{this});
        } catch (Throwable th) {
        }
    }
}
