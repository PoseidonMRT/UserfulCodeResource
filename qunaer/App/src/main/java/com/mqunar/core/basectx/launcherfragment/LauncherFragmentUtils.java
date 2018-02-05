package com.mqunar.core.basectx.launcherfragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import com.mqunar.core.basectx.fragment.QFragment;

public class LauncherFragmentUtils {
    public static void startFragmentForResult(Activity activity, Class<? extends QFragment> cls, Bundle bundle, int i) {
        Class launcherFragmentActivity = QFragmentManager.getLauncherFragmentActivity(cls);
        Intent intent = new Intent();
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        intent.putExtra(QFragment.key, cls.getName());
        intent.setClass(activity, launcherFragmentActivity);
        activity.startActivityForResult(intent, i);
    }

    public static void startFragment(Activity activity, Class<? extends QFragment> cls, Bundle bundle) {
        Intent intent = new Intent();
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        if (intent.getFlags() == 603979776) {
            backToFragment(activity, cls, bundle);
            return;
        }
        Class launcherFragmentActivity = QFragmentManager.getLauncherFragmentActivity(cls);
        intent.putExtra(QFragment.key, cls.getName());
        intent.setClass(activity, launcherFragmentActivity);
        activity.startActivity(intent);
    }

    public static void startTransparentFragmentForResult(Activity activity, Class<? extends QFragment> cls, Bundle bundle, int i) {
        Class cls2 = TransparentFragmentActivity.class;
        Intent intent = new Intent();
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        intent.putExtra(QFragment.key, cls.getName());
        intent.setClass(activity, cls2);
        activity.startActivityForResult(intent, i);
    }

    public static void startTransparentFragment(Activity activity, Class<? extends QFragment> cls, Bundle bundle) {
        Intent intent = new Intent();
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        intent.putExtra(QFragment.key, cls.getName());
        intent.setClass(activity, TransparentFragmentActivity.class);
        activity.startActivity(intent);
    }

    public static void startDialogFragmentForResult(Activity activity, Class<? extends QFragment> cls, Bundle bundle, int i) {
        Class cls2 = DialogFragmentActivity.class;
        Intent intent = new Intent();
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        intent.putExtra(QFragment.key, cls.getName());
        intent.setClass(activity, cls2);
        activity.startActivityForResult(intent, i);
    }

    public static void startDialogFragment(Activity activity, Class<? extends QFragment> cls, Bundle bundle) {
        Intent intent = new Intent();
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        intent.putExtra(QFragment.key, cls.getName());
        intent.setClass(activity, DialogFragmentActivity.class);
        activity.startActivity(intent);
    }

    public static void backToFragment(Activity activity, Class<? extends QFragment> cls, Bundle bundle) {
        Class freeLauncherFragmentActivity = QFragmentManager.getFreeLauncherFragmentActivity(cls);
        Intent intent = new Intent();
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        if (freeLauncherFragmentActivity == null) {
            freeLauncherFragmentActivity = QFragmentManager.getLauncherFragmentActivity(cls);
        } else {
            intent.addFlags(603979776);
            intent.putExtra("_flag_clear_top", true);
        }
        intent.putExtra(QFragment.key, cls.getName());
        intent.setClass(activity, freeLauncherFragmentActivity);
        activity.startActivity(intent);
    }

    public static void backToActivity(Activity activity, Class<? extends Activity> cls, Bundle bundle) {
        Intent intent = new Intent();
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        intent.addFlags(603979776);
        intent.setClass(activity, cls);
        activity.startActivity(intent);
    }

    public static void startFragment(Activity activity, Class<? extends QFragment> cls, boolean z) {
        Bundle bundle = new Bundle();
        bundle.putBoolean("_key_isFlip", z);
        startFragment(activity, (Class) cls, bundle);
    }

    public static void startFragment(Activity activity, Class<? extends QFragment> cls, Bundle bundle, boolean z) {
        if (bundle == null) {
            bundle = new Bundle();
        }
        bundle.putBoolean("_key_isFlip", z);
        startFragment(activity, (Class) cls, bundle);
    }

    public static void startFragmentForResult(Activity activity, Class<? extends QFragment> cls, Bundle bundle, int i, boolean z) {
        if (bundle == null) {
            bundle = new Bundle();
        }
        bundle.putBoolean("_key_isFlip", z);
        startFragmentForResult(activity, (Class) cls, bundle, i);
    }

    public static void startFragmentForResult(Activity activity, Class<? extends QFragment> cls, int i, boolean z) {
        Bundle bundle = new Bundle();
        bundle.putBoolean("_key_isFlip", z);
        startFragmentForResult(activity, (Class) cls, bundle, i);
    }
}
