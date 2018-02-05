package com.mqunar.core.basectx.activity;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.TabActivity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.media.TransportMediator;
import com.mqunar.core.basectx.fragment.QFragment;
import com.mqunar.core.basectx.launcherfragment.LauncherFragmentUtils;

public abstract class QTabActivity extends TabActivity {
    private long preStartTime;

    public Resources getResources() {
        try {
            return (Resources) Class.forName("com.mqunar.core.QunarApkLoader").getDeclaredMethod("checkResources", new Class[]{Resources.class, Context.class}).invoke(null, new Object[]{r1, this});
        } catch (Throwable th) {
            return super.getResources();
        }
    }

    protected void onStart() {
        try {
            super.onStart();
            Proxy.onStart(this);
        } catch (Exception e) {
            onException(e);
        }
    }

    protected void onResume() {
        try {
            super.onResume();
            Proxy.onResume(this, getIntent() == null ? null : getIntent().getExtras());
        } catch (Exception e) {
            onException(e);
        }
    }

    protected void onPostResume() {
        try {
            super.onPostResume();
        } catch (Exception e) {
            onException(e);
        }
    }

    protected void onPause() {
        try {
            super.onPause();
            Proxy.onPause(this);
        } catch (Exception e) {
            onException(e);
        }
    }

    protected void onStop() {
        try {
            super.onStop();
            Proxy.onStop(this);
        } catch (Exception e) {
            onException(e);
        }
    }

    protected void onRestoreInstanceState(Bundle bundle) {
        try {
            super.onRestoreInstanceState(bundle);
        } catch (Exception e) {
            onException(e);
        }
    }

    protected void onDestroy() {
        try {
            super.onDestroy();
            Proxy.onDestroy(this);
        } catch (Exception e) {
            onException(e);
        }
    }

    public void setContentView(int i) {
        try {
            super.setContentView(i);
        } catch (Exception e) {
            onException(e);
        }
    }

    protected void onSaveInstanceState(Bundle bundle) {
        long spiderStartTime = Util.getSpiderStartTime();
        bundle.putLong("_spider_starttime_", spiderStartTime);
        super.onSaveInstanceState(bundle);
        bundle.putLong("_spider_starttime_", spiderStartTime);
        Proxy.onSaveInstanceState(this, bundle);
    }

    public void finish() {
        super.finish();
        Proxy.finish(this);
    }

    private void onException(Exception exception) {
        Util.onException(this, this.preStartTime, exception);
    }

    protected void onCreate(Bundle bundle) {
        RuntimeException runtimeException;
        RuntimeException runtimeException2 = null;
        try {
            super.onCreate(bundle);
            runtimeException = null;
        } catch (RuntimeException e) {
            runtimeException = e;
        }
        if (bundle != null) {
            this.preStartTime = bundle.getLong("_spider_starttime_", 0);
            long spiderStartTime = Util.getSpiderStartTime();
            if (!(this.preStartTime == 0 || spiderStartTime == this.preStartTime)) {
                Util.restart2(this);
                if (runtimeException2 == null) {
                    throw runtimeException2;
                }
                Proxy.onCreate(this, bundle);
                return;
            }
        }
        runtimeException2 = runtimeException;
        if (runtimeException2 == null) {
            Proxy.onCreate(this, bundle);
            return;
        }
        throw runtimeException2;
    }

    protected boolean needOnCreateSendBroadcast() {
        return true;
    }

    public void startActivityForResult(Intent intent, int i) {
        Proxy.startActivityForResult(this, intent, i);
        super.startActivityForResult(intent, i);
    }

    @TargetApi(16)
    public void startActivityForResult(Intent intent, int i, Bundle bundle) {
        Proxy.startActivityForResult(this, intent, i);
        super.startActivityForResult(intent, i, bundle);
    }

    public void overridePendingTransition(int i, int i2) {
        int i3 = (i >> 24) & 255;
        int i4 = (i2 >> 24) & 255;
        if (!(i3 == 1 || i3 == TransportMediator.KEYCODE_MEDIA_PAUSE || i == 0)) {
            i = SpiderR.getResId("anim", "spider_slide_in_right");
        }
        if (!(i4 == 1 || i4 == TransportMediator.KEYCODE_MEDIA_PAUSE || i2 == 0)) {
            i2 = SpiderR.getResId("anim", "spider_slide_out_right");
        }
        super.overridePendingTransition(i, i2);
    }

    public final void startFragmentForResult(Class<? extends QFragment> cls, Bundle bundle, int i) {
        LauncherFragmentUtils.startFragmentForResult((Activity) this, (Class) cls, bundle, i);
    }

    public final void startFragmentForResult(Class<? extends QFragment> cls, int i) {
        startFragmentForResult((Class) cls, null, i);
    }

    public final void startFragment(Class<? extends QFragment> cls, Bundle bundle) {
        LauncherFragmentUtils.startFragment((Activity) this, (Class) cls, bundle);
    }

    public final void startTransparentFragmentForResult(Class<? extends QFragment> cls, int i) {
        startTransparentFragmentForResult(cls, null, i);
    }

    public final void startTransparentFragmentForResult(Class<? extends QFragment> cls, Bundle bundle, int i) {
        LauncherFragmentUtils.startTransparentFragmentForResult(this, cls, bundle, i);
    }

    public final void startTransparentFragment(Class<? extends QFragment> cls, Bundle bundle) {
        LauncherFragmentUtils.startTransparentFragment(this, cls, bundle);
    }

    public final void startDialogFragmentForResult(Class<? extends QFragment> cls, int i) {
        startDialogFragmentForResult(cls, null, i);
    }

    public final void startDialogFragmentForResult(Class<? extends QFragment> cls, Bundle bundle, int i) {
        LauncherFragmentUtils.startDialogFragmentForResult(this, cls, bundle, i);
    }

    public final void startDialogFragment(Class<? extends QFragment> cls, Bundle bundle) {
        LauncherFragmentUtils.startDialogFragment(this, cls, bundle);
    }

    public final void backToFragment(Class<? extends QFragment> cls, Bundle bundle) {
        LauncherFragmentUtils.backToFragment(this, cls, bundle);
    }

    public final void backToActivity(Class<? extends Activity> cls, Bundle bundle) {
        LauncherFragmentUtils.backToActivity(this, cls, bundle);
    }

    public final void backToActivity(Class<? extends Activity> cls) {
        backToActivity(cls, null);
    }

    public final void startFragment(Class<? extends QFragment> cls) {
        startFragment((Class) cls, null);
    }

    public final void backToFragment(Class<? extends QFragment> cls) {
        backToFragment(cls, null);
    }

    public final void startDialogFragment(Class<? extends QFragment> cls) {
        startDialogFragment(cls, null);
    }

    public final void startTransparentFragment(Class<? extends QFragment> cls) {
        startTransparentFragment(cls, null);
    }

    public final void startFragment(Class<? extends QFragment> cls, boolean z) {
        LauncherFragmentUtils.startFragment((Activity) this, (Class) cls, z);
    }

    public final void startFragment(Class<? extends QFragment> cls, Bundle bundle, boolean z) {
        LauncherFragmentUtils.startFragment(this, cls, bundle, z);
    }

    public final void startFragmentForResult(Class<? extends QFragment> cls, Bundle bundle, int i, boolean z) {
        LauncherFragmentUtils.startFragmentForResult(this, cls, bundle, i, z);
    }

    public final void startFragmentForResult(Class<? extends QFragment> cls, int i, boolean z) {
        LauncherFragmentUtils.startFragmentForResult((Activity) this, (Class) cls, i, z);
    }
}
