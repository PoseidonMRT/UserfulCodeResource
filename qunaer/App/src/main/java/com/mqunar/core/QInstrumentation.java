package com.mqunar.core;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Application;
import android.app.Instrumentation;
import android.app.Instrumentation.ActivityMonitor;
import android.app.Instrumentation.ActivityResult;
import android.app.UiAutomation;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Process;
import android.view.KeyEvent;
import android.view.MotionEvent;
import com.mqunar.dispatcher.DispatcherActivity;
import com.mqunar.dispatcher.DispatcherProxyActivity;
import com.mqunar.dispatcher.DispatcherProxyHomeActivity;
import com.mqunar.dispatcher.WebActivity;
import com.mqunar.splash.SplashActivity;

public class QInstrumentation extends Instrumentation {
    public static String firstActivityName = null;
    private Instrumentation a;

    public QInstrumentation(Instrumentation instrumentation) {
        this.a = instrumentation;
    }

    public void onCreate(Bundle bundle) {
        this.a.onCreate(bundle);
    }

    public void start() {
        this.a.start();
    }

    public void onStart() {
        this.a.onStart();
    }

    public boolean onException(Object obj, Throwable th) {
        return this.a.onException(obj, th);
    }

    public void sendStatus(int i, Bundle bundle) {
        this.a.sendStatus(i, bundle);
    }

    public void finish(int i, Bundle bundle) {
        this.a.finish(i, bundle);
    }

    public void setAutomaticPerformanceSnapshots() {
        this.a.setAutomaticPerformanceSnapshots();
    }

    public void startPerformanceSnapshot() {
        this.a.startPerformanceSnapshot();
    }

    public void endPerformanceSnapshot() {
        this.a.endPerformanceSnapshot();
    }

    public void onDestroy() {
        this.a.onDestroy();
    }

    public Context getContext() {
        return this.a.getContext();
    }

    public ComponentName getComponentName() {
        return this.a.getComponentName();
    }

    public Context getTargetContext() {
        return this.a.getTargetContext();
    }

    public boolean isProfiling() {
        return this.a.isProfiling();
    }

    public void startProfiling() {
        this.a.startProfiling();
    }

    public void stopProfiling() {
        this.a.stopProfiling();
    }

    public void setInTouchMode(boolean z) {
        this.a.setInTouchMode(z);
    }

    public void waitForIdle(Runnable runnable) {
        this.a.waitForIdle(runnable);
    }

    public void waitForIdleSync() {
        this.a.waitForIdleSync();
    }

    public void runOnMainSync(Runnable runnable) {
        this.a.runOnMainSync(runnable);
    }

    public Activity startActivitySync(Intent intent) {
        return this.a.startActivitySync(intent);
    }

    public void addMonitor(ActivityMonitor activityMonitor) {
        this.a.addMonitor(activityMonitor);
    }

    public ActivityMonitor addMonitor(IntentFilter intentFilter, ActivityResult activityResult, boolean z) {
        return this.a.addMonitor(intentFilter, activityResult, z);
    }

    public ActivityMonitor addMonitor(String str, ActivityResult activityResult, boolean z) {
        return this.a.addMonitor(str, activityResult, z);
    }

    public boolean checkMonitorHit(ActivityMonitor activityMonitor, int i) {
        return this.a.checkMonitorHit(activityMonitor, i);
    }

    public Activity waitForMonitor(ActivityMonitor activityMonitor) {
        return this.a.waitForMonitor(activityMonitor);
    }

    public Activity waitForMonitorWithTimeout(ActivityMonitor activityMonitor, long j) {
        return this.a.waitForMonitorWithTimeout(activityMonitor, j);
    }

    public void removeMonitor(ActivityMonitor activityMonitor) {
        this.a.removeMonitor(activityMonitor);
    }

    public boolean invokeMenuActionSync(Activity activity, int i, int i2) {
        return this.a.invokeMenuActionSync(activity, i, i2);
    }

    public boolean invokeContextMenuAction(Activity activity, int i, int i2) {
        return this.a.invokeContextMenuAction(activity, i, i2);
    }

    public void sendStringSync(String str) {
        this.a.sendStringSync(str);
    }

    public void sendKeySync(KeyEvent keyEvent) {
        this.a.sendKeySync(keyEvent);
    }

    public void sendKeyDownUpSync(int i) {
        this.a.sendKeyDownUpSync(i);
    }

    public void sendCharacterSync(int i) {
        this.a.sendCharacterSync(i);
    }

    public void sendPointerSync(MotionEvent motionEvent) {
        this.a.sendPointerSync(motionEvent);
    }

    public void sendTrackballEventSync(MotionEvent motionEvent) {
        this.a.sendTrackballEventSync(motionEvent);
    }

    public Application newApplication(ClassLoader classLoader, String str, Context context) {
        return this.a.newApplication(classLoader, str, context);
    }

    public void callApplicationOnCreate(Application application) {
        this.a.callApplicationOnCreate(application);
    }

    public Activity newActivity(Class<?> cls, Context context, IBinder iBinder, Application application, Intent intent, ActivityInfo activityInfo, CharSequence charSequence, Activity activity, String str, Object obj) {
        return this.a.newActivity(cls, context, iBinder, application, intent, activityInfo, charSequence, activity, str, obj);
    }

    public Activity newActivity(ClassLoader classLoader, String str, Intent intent) {
        if (firstActivityName == null) {
            firstActivityName = str;
        }
        try {
            QunarApkLoader.a(classLoader);
            QunarApkLoader.checkClassLoader();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        if (!(QSpider.loadDone || str.equals(DispatcherActivity.class.getName()) || str.equals(SplashActivity.class.getName()) || str.equals(DispatcherProxyActivity.class.getName()) || str.equals(WebActivity.class.getName()) || str.equals(DispatcherProxyHomeActivity.class.getName()))) {
            intent = new Intent();
            intent.setClass(QunarApkLoader.getAppContext(), SplashActivity.class);
            intent.addFlags(32768);
            intent.addFlags(268435456);
            intent.addFlags(67108864);
            intent.addFlags(65536);
            intent.addFlags(8388608);
            QunarApkLoader.getAppContext().startActivity(intent);
            Process.killProcess(Process.myPid());
            System.exit(0);
        }
        return this.a.newActivity(classLoader, str, intent);
    }

    public void callActivityOnCreate(Activity activity, Bundle bundle) {
        try {
            QunarApkLoader.a(activity.getClassLoader());
            QunarApkLoader.checkClassLoader();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        this.a.callActivityOnCreate(activity, bundle);
    }

    public void callActivityOnDestroy(Activity activity) {
        this.a.callActivityOnDestroy(activity);
    }

    public void callActivityOnRestoreInstanceState(Activity activity, Bundle bundle) {
        this.a.callActivityOnRestoreInstanceState(activity, bundle);
    }

    public void callActivityOnPostCreate(Activity activity, Bundle bundle) {
        this.a.callActivityOnPostCreate(activity, bundle);
    }

    public void callActivityOnNewIntent(Activity activity, Intent intent) {
        this.a.callActivityOnNewIntent(activity, intent);
    }

    public void callActivityOnStart(Activity activity) {
        this.a.callActivityOnStart(activity);
    }

    public void callActivityOnRestart(Activity activity) {
        this.a.callActivityOnRestart(activity);
    }

    public void callActivityOnResume(Activity activity) {
        this.a.callActivityOnResume(activity);
    }

    public void callActivityOnStop(Activity activity) {
        this.a.callActivityOnStop(activity);
    }

    public void callActivityOnSaveInstanceState(Activity activity, Bundle bundle) {
        this.a.callActivityOnSaveInstanceState(activity, bundle);
    }

    public void callActivityOnPause(Activity activity) {
        this.a.callActivityOnPause(activity);
    }

    public void callActivityOnUserLeaving(Activity activity) {
        this.a.callActivityOnUserLeaving(activity);
    }

    public void startAllocCounting() {
        this.a.startAllocCounting();
    }

    public void stopAllocCounting() {
        this.a.stopAllocCounting();
    }

    public Bundle getAllocCounts() {
        return this.a.getAllocCounts();
    }

    public Bundle getBinderCounts() {
        return this.a.getBinderCounts();
    }

    @TargetApi(18)
    public UiAutomation getUiAutomation() {
        return this.a.getUiAutomation();
    }
}
