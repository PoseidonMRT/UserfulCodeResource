package com.mqunar.core.basectx.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import com.mqunar.core.basectx.SchemeDispatcher;
import com.mqunar.core.basectx.application.ActivityLifecycleDispatcher;
import com.mqunar.core.basectx.application.QApplication;

class Proxy {
    Proxy() {
    }

    static void onCreate(Activity activity, Bundle bundle) {
        try {
            Class.forName("com.mqunar.core.QunarApkLoader").getDeclaredMethod("loadResource", new Class[]{Context.class}).invoke(null, new Object[]{activity});
        } catch (Throwable th) {
        }
        if (bundle == null) {
            QApplication.setStackCount(QApplication.getStackCount() + 1);
        }
        ActivityLifecycleDispatcher.getInstance().dispatchActivityCreated(activity, bundle);
    }

    static void onStart(Activity activity) {
        ActivityLifecycleDispatcher.getInstance().dispatchActivityStarted(activity);
    }

    static void onStop(Activity activity) {
        ActivityLifecycleDispatcher.getInstance().dispatchActivityStopped(activity);
    }

    static void startActivityForResult(Activity activity, Intent intent, int i) {
        intent.putExtra(SchemeDispatcher.SPIDER_REQUESTCODE, i);
    }

    static void onPause(Activity activity) {
        QApplication.setInStackName(null);
        ActivityLifecycleDispatcher.getInstance().dispatchActivityPaused(activity);
    }

    static void onResume(Activity activity, Bundle bundle) {
        QApplication.setInStackName(Util.getInStackName(activity));
        ActivityLifecycleDispatcher.getInstance().dispatchActivityResumed(activity);
    }

    static void onDestroy(Activity activity) {
        ActivityLifecycleDispatcher.getInstance().dispatchActivityDestroyed(activity);
    }

    static void finish(Activity activity) {
        ActivityLifecycleDispatcher.getInstance().dispatchActivityFinished(activity);
        QApplication.setStackCount(QApplication.getStackCount() - 1);
    }

    static void onSaveInstanceState(Activity activity, Bundle bundle) {
        ActivityLifecycleDispatcher.getInstance().dispatchActivitySaveInstanceState(activity, bundle);
    }
}
