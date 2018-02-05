package com.mqunar.core.basectx.application;

import android.app.Application;
import android.content.Context;
import android.content.res.Resources;
import com.mqunar.core.basectx.application.ActivityLifecycleDispatcher.QActivityLifecycleCallbacks;

public class QApplication extends Application {
    private static String inStackName;
    private static Context mContext;
    private static int stackCount = 0;
    private static String versionInfo;

    protected void attachBaseContext(Context context) {
        super.attachBaseContext(context);
        if (mContext == null) {
            mContext = this;
        }
    }

    public static Context getContext() {
        if (mContext != null) {
            return mContext;
        }
        throw new RuntimeException("WTF! you must extends QApplication !!! ");
    }

    public Resources getResources() {
        if (mContext == null || mContext == this) {
            return super.getResources();
        }
        return mContext.getResources();
    }

    public static void setVersionInfo(String str) {
        versionInfo = str;
    }

    public static String getVersionInfo() {
        return versionInfo;
    }

    public static void setInStackName(String str) {
        inStackName = str;
    }

    public static String getInStackName() {
        return inStackName;
    }

    public static int getStackCount() {
        return stackCount;
    }

    public static void setStackCount(int i) {
        stackCount = i;
    }

    public void qRegisterActivityLifecycleCallbacks(QActivityLifecycleCallbacks qActivityLifecycleCallbacks) {
        ActivityLifecycleDispatcher.getInstance().registerActivityLifecycleCallbacks(qActivityLifecycleCallbacks);
    }

    public void qUnregisterActivityLifecycleCallbacks(QActivityLifecycleCallbacks qActivityLifecycleCallbacks) {
        ActivityLifecycleDispatcher.getInstance().unregisterActivityLifecycleCallbacks(qActivityLifecycleCallbacks);
    }
}
