package org.acra.builder;

import android.app.Activity;
import android.app.Application;
import android.app.Application.ActivityLifecycleCallbacks;
import android.content.SharedPreferences;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import java.lang.ref.WeakReference;
import org.acra.ACRA;
import org.acra.config.ACRAConfiguration;
import org.acra.dialog.a;
import org.acra.prefs.b;

public final class LastActivityManager {
    public static boolean a = false;
    @NonNull
    private WeakReference<Activity> b = new WeakReference(null);

    public LastActivityManager(@NonNull final Application application, @NonNull final ACRAConfiguration aCRAConfiguration) {
        if (VERSION.SDK_INT >= 14) {
            application.registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks(this) {
                final /* synthetic */ LastActivityManager c;

                public void onActivityCreated(@NonNull Activity activity, Bundle bundle) {
                    ACRA.f.b(ACRA.e, "onActivityCreated " + activity.getClass());
                    if (!(activity instanceof a)) {
                        this.c.b = new WeakReference(activity);
                    }
                }

                public void onActivityStarted(@NonNull Activity activity) {
                    ACRA.f.b(ACRA.e, "onActivityStarted " + activity.getClass());
                }

                public void onActivityResumed(@NonNull Activity activity) {
                    ACRA.f.b(ACRA.e, "onActivityResumed " + activity.getClass());
                    if (!LastActivityManager.a) {
                        LastActivityManager.a = true;
                        SharedPreferences a = new b(application, aCRAConfiguration).a();
                        a.edit().putInt("acra.backgroundCrashCount", 0).apply();
                        a.edit().putInt("acra.silentBackgroundCrashCount", 0).apply();
                    }
                }

                public void onActivityPaused(@NonNull Activity activity) {
                    ACRA.f.b(ACRA.e, "onActivityPaused " + activity.getClass());
                }

                public void onActivityStopped(@NonNull Activity activity) {
                    ACRA.f.b(ACRA.e, "onActivityStopped " + activity.getClass());
                    synchronized (this) {
                        notify();
                    }
                }

                public void onActivitySaveInstanceState(@NonNull Activity activity, Bundle bundle) {
                    ACRA.f.b(ACRA.e, "onActivitySaveInstanceState " + activity.getClass());
                }

                public void onActivityDestroyed(@NonNull Activity activity) {
                    ACRA.f.b(ACRA.e, "onActivityDestroyed " + activity.getClass());
                }
            });
        }
    }

    @Nullable
    public Activity getLastActivity() {
        return (Activity) this.b.get();
    }

    public void a() {
        this.b.clear();
    }

    public void a(int i) {
        synchronized (this) {
            try {
                wait((long) i);
            } catch (InterruptedException e) {
            }
        }
    }
}
