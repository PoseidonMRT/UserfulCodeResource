package com.mqunar.core.basectx.application;

import android.app.Activity;
import android.os.Bundle;
import java.util.ArrayList;

public class ActivityLifecycleDispatcher {
    private static ActivityLifecycleDispatcher INSTANCE;
    private ArrayList<QActivityLifecycleCallbacks> mActivityLifecycleCallbacks = new ArrayList();

    public interface QActivityLifecycleCallbacks {
        void onActivityCreated(Activity activity, Bundle bundle);

        void onActivityDestroyed(Activity activity);

        void onActivityFinished(Activity activity);

        void onActivityPaused(Activity activity);

        void onActivityResumed(Activity activity);

        void onActivitySaveInstanceState(Activity activity, Bundle bundle);

        void onActivityStarted(Activity activity);

        void onActivityStopped(Activity activity);
    }

    public static ActivityLifecycleDispatcher getInstance() {
        if (INSTANCE == null) {
            synchronized (ActivityLifecycleDispatcher.class) {
                if (INSTANCE == null) {
                    INSTANCE = new ActivityLifecycleDispatcher();
                }
            }
        }
        return INSTANCE;
    }

    public void registerActivityLifecycleCallbacks(QActivityLifecycleCallbacks qActivityLifecycleCallbacks) {
        synchronized (this.mActivityLifecycleCallbacks) {
            this.mActivityLifecycleCallbacks.add(qActivityLifecycleCallbacks);
        }
    }

    public void unregisterActivityLifecycleCallbacks(QActivityLifecycleCallbacks qActivityLifecycleCallbacks) {
        synchronized (this.mActivityLifecycleCallbacks) {
            this.mActivityLifecycleCallbacks.remove(qActivityLifecycleCallbacks);
        }
    }

    public void dispatchActivityCreated(Activity activity, Bundle bundle) {
        Object[] collectActivityLifecycleCallbacks = collectActivityLifecycleCallbacks();
        if (collectActivityLifecycleCallbacks != null) {
            for (Object obj : collectActivityLifecycleCallbacks) {
                ((QActivityLifecycleCallbacks) obj).onActivityCreated(activity, bundle);
            }
        }
    }

    public void dispatchActivityStarted(Activity activity) {
        Object[] collectActivityLifecycleCallbacks = collectActivityLifecycleCallbacks();
        if (collectActivityLifecycleCallbacks != null) {
            for (Object obj : collectActivityLifecycleCallbacks) {
                ((QActivityLifecycleCallbacks) obj).onActivityStarted(activity);
            }
        }
    }

    public void dispatchActivityResumed(Activity activity) {
        Object[] collectActivityLifecycleCallbacks = collectActivityLifecycleCallbacks();
        if (collectActivityLifecycleCallbacks != null) {
            for (Object obj : collectActivityLifecycleCallbacks) {
                ((QActivityLifecycleCallbacks) obj).onActivityResumed(activity);
            }
        }
    }

    public void dispatchActivityPaused(Activity activity) {
        Object[] collectActivityLifecycleCallbacks = collectActivityLifecycleCallbacks();
        if (collectActivityLifecycleCallbacks != null) {
            for (Object obj : collectActivityLifecycleCallbacks) {
                ((QActivityLifecycleCallbacks) obj).onActivityPaused(activity);
            }
        }
    }

    public void dispatchActivityStopped(Activity activity) {
        Object[] collectActivityLifecycleCallbacks = collectActivityLifecycleCallbacks();
        if (collectActivityLifecycleCallbacks != null) {
            for (Object obj : collectActivityLifecycleCallbacks) {
                ((QActivityLifecycleCallbacks) obj).onActivityStopped(activity);
            }
        }
    }

    public void dispatchActivitySaveInstanceState(Activity activity, Bundle bundle) {
        Object[] collectActivityLifecycleCallbacks = collectActivityLifecycleCallbacks();
        if (collectActivityLifecycleCallbacks != null) {
            for (Object obj : collectActivityLifecycleCallbacks) {
                ((QActivityLifecycleCallbacks) obj).onActivitySaveInstanceState(activity, bundle);
            }
        }
    }

    public void dispatchActivityDestroyed(Activity activity) {
        Object[] collectActivityLifecycleCallbacks = collectActivityLifecycleCallbacks();
        if (collectActivityLifecycleCallbacks != null) {
            for (Object obj : collectActivityLifecycleCallbacks) {
                ((QActivityLifecycleCallbacks) obj).onActivityDestroyed(activity);
            }
        }
    }

    public void dispatchActivityFinished(Activity activity) {
        Object[] collectActivityLifecycleCallbacks = collectActivityLifecycleCallbacks();
        if (collectActivityLifecycleCallbacks != null) {
            for (Object obj : collectActivityLifecycleCallbacks) {
                ((QActivityLifecycleCallbacks) obj).onActivityFinished(activity);
            }
        }
    }

    private Object[] collectActivityLifecycleCallbacks() {
        Object[] objArr = null;
        synchronized (this.mActivityLifecycleCallbacks) {
            if (this.mActivityLifecycleCallbacks.size() > 0) {
                objArr = this.mActivityLifecycleCallbacks.toArray();
            }
        }
        return objArr;
    }
}
