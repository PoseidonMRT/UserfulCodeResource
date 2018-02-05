package com.mqunar.core.basectx.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Process;
import android.support.v4.app.Fragment;
import android.support.v4.internal.view.SupportMenu;
import com.mqunar.core.basectx.activity.Util;
import com.mqunar.core.basectx.launcherfragment.DialogFragmentActivity;
import com.mqunar.core.basectx.launcherfragment.LauncherFragmentActivityBase;
import com.mqunar.core.basectx.launcherfragment.LauncherFragmentUtils;
import com.mqunar.core.basectx.launcherfragment.QFragmentManager;
import com.mqunar.core.basectx.launcherfragment.TransparentFragmentActivity;

public class QFragment extends Fragment {
    public static final String EXTRA_FROM_ACTIVITY = "__FROM_ACTIVITY__";
    public static final String EXTRA_TO_ACTIVITY = "__TO_ACTIVITY__";
    public static final String key = "_fragmentName_key";
    private Fragment mCalledFragment;
    protected Bundle myBundle;
    private int pidOnDie;

    private void startActivityFromChildFragment(QFragment qFragment, Intent intent, int i) {
        this.mCalledFragment = qFragment;
        startActivityForResult(intent, SupportMenu.USER_MASK & i);
    }

    public void onNewIntent(Intent intent) {
    }

    public boolean isCanFlip() {
        try {
            LauncherFragmentActivityBase geLauncherFragmentActivityBase = geLauncherFragmentActivityBase();
            if (geLauncherFragmentActivityBase != null) {
                return geLauncherFragmentActivityBase.isCanFlip();
            }
        } catch (Throwable th) {
        }
        return true;
    }

    private LauncherFragmentActivityBase geLauncherFragmentActivityBase() {
        try {
            if (LauncherFragmentActivityBase.class.isAssignableFrom(getActivity().getClass())) {
                return (LauncherFragmentActivityBase) getActivity();
            }
        } catch (Throwable th) {
        }
        return null;
    }

    public void setCanFlip(boolean z) {
        try {
            LauncherFragmentActivityBase geLauncherFragmentActivityBase = geLauncherFragmentActivityBase();
            if (geLauncherFragmentActivityBase != null) {
                geLauncherFragmentActivityBase.setCanFlip(z);
            }
        } catch (Throwable th) {
        }
    }

    public boolean isTouchOnHScroll() {
        try {
            LauncherFragmentActivityBase geLauncherFragmentActivityBase = geLauncherFragmentActivityBase();
            if (geLauncherFragmentActivityBase != null) {
                return geLauncherFragmentActivityBase.isTouchOnHScroll();
            }
        } catch (Throwable th) {
        }
        return true;
    }

    public void setTouchOnHScroll(boolean z) {
        try {
            LauncherFragmentActivityBase geLauncherFragmentActivityBase = geLauncherFragmentActivityBase();
            if (geLauncherFragmentActivityBase != null) {
                geLauncherFragmentActivityBase.setTouchOnHScroll(z);
            }
        } catch (Throwable th) {
        }
    }

    public void startActivityForResult(Intent intent, int i) {
        if (getActivity() == null) {
            throw new IllegalStateException("Fragment " + this + " not attached to Activity");
        }
        Fragment parentFragment = getParentFragment();
        if (parentFragment == null) {
            getActivity().startActivityFromFragment(this, intent, i);
        } else if (parentFragment instanceof QFragment) {
            ((QFragment) parentFragment).startActivityFromChildFragment(this, intent, i);
        } else {
            getActivity().startActivityFromFragment(getParentFragment(), intent, i);
        }
    }

    public final void startFragmentForResult(Class<? extends QFragment> cls, Bundle bundle, int i) {
        Class launcherFragmentActivity = QFragmentManager.getLauncherFragmentActivity(cls);
        Intent intent = new Intent();
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        intent.putExtra(key, cls.getName());
        intent.setClass(getActivity(), launcherFragmentActivity);
        if (getActivity() == null) {
            throw new IllegalStateException("Fragment " + this + " not attached to Activity");
        }
        Fragment parentFragment = getParentFragment();
        if (parentFragment == null) {
            getActivity().startActivityFromFragment(this, intent, i);
        } else if (parentFragment instanceof QFragment) {
            ((QFragment) parentFragment).startActivityFromChildFragment(this, intent, i);
        } else {
            getActivity().startActivityFromFragment(getParentFragment(), intent, i);
        }
    }

    public final void startFragmentForResult(Class<? extends QFragment> cls, Bundle bundle, int i, boolean z) {
        if (bundle == null) {
            bundle = new Bundle();
        }
        bundle.putBoolean("_key_isFlip", z);
        startFragmentForResult((Class) cls, bundle, i);
    }

    public final void startFragmentForResult(Class<? extends QFragment> cls, int i, boolean z) {
        Bundle bundle = new Bundle();
        bundle.putBoolean("_key_isFlip", z);
        startFragmentForResult((Class) cls, bundle, i);
    }

    public final void startFragmentForResult(Class<? extends QFragment> cls, int i) {
        startFragmentForResult((Class) cls, null, i);
    }

    public void onActivityResult(int i, int i2, Intent intent) {
        if (this.mCalledFragment != null) {
            this.mCalledFragment.onActivityResult(SupportMenu.USER_MASK & i, i2, intent);
            this.mCalledFragment = null;
            return;
        }
        super.onActivityResult(i, i2, intent);
    }

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        this.myBundle = bundle == null ? getArguments() : bundle;
        if (this.myBundle == null) {
            this.myBundle = new Bundle();
        }
        if (bundle != null) {
            this.pidOnDie = bundle.getInt("_spider_mypid_", 0);
            int myPid = Process.myPid();
            if (this.pidOnDie != 0 && myPid != this.pidOnDie) {
                Util.restart(getActivity());
            }
        }
    }

    public void onSaveInstanceState(Bundle bundle) {
        if (this.myBundle != null) {
            bundle.putAll(this.myBundle);
        }
        int myPid = Process.myPid();
        bundle.putInt("_spider_mypid_", myPid);
        super.onSaveInstanceState(bundle);
        bundle.putInt("_spider_mypid_", myPid);
    }

    public final void startFragment(Class<? extends QFragment> cls, Bundle bundle) {
        LauncherFragmentUtils.startFragment(getActivity(), (Class) cls, setFragmentName(bundle));
    }

    private Bundle setFragmentName(Bundle bundle) {
        if (bundle == null) {
            bundle = new Bundle();
        }
        if (!bundle.containsKey(key)) {
            bundle.putString(key, getClass().getSimpleName());
        }
        return bundle;
    }

    public final void startTransparentFragmentForResult(Class<? extends QFragment> cls, int i) {
        startTransparentFragmentForResult(cls, null, i);
    }

    public final void startTransparentFragmentForResult(Class<? extends QFragment> cls, Bundle bundle, int i) {
        Class cls2 = TransparentFragmentActivity.class;
        Intent intent = new Intent();
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        intent.putExtra(key, cls.getName());
        intent.setClass(getActivity(), cls2);
        if (getActivity() == null) {
            throw new IllegalStateException("Fragment " + this + " not attached to Activity");
        }
        Fragment parentFragment = getParentFragment();
        if (parentFragment == null) {
            getActivity().startActivityFromFragment(this, intent, i);
        } else if (parentFragment instanceof QFragment) {
            ((QFragment) parentFragment).startActivityFromChildFragment(this, intent, i);
        } else {
            getActivity().startActivityFromFragment(getParentFragment(), intent, i);
        }
    }

    public final void startTransparentFragment(Class<? extends QFragment> cls, Bundle bundle) {
        LauncherFragmentUtils.startTransparentFragment(getActivity(), cls, bundle);
    }

    public final void startDialogFragmentForResult(Class<? extends QFragment> cls, int i) {
        startDialogFragmentForResult(cls, null, i);
    }

    public final void startDialogFragmentForResult(Class<? extends QFragment> cls, Bundle bundle, int i) {
        Class cls2 = DialogFragmentActivity.class;
        Intent intent = new Intent();
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        intent.putExtra(key, cls.getName());
        intent.setClass(getActivity(), cls2);
        if (getActivity() == null) {
            throw new IllegalStateException("Fragment " + this + " not attached to Activity");
        }
        Fragment parentFragment = getParentFragment();
        if (parentFragment == null) {
            getActivity().startActivityFromFragment(this, intent, i);
        } else if (parentFragment instanceof QFragment) {
            ((QFragment) parentFragment).startActivityFromChildFragment(this, intent, i);
        } else {
            getActivity().startActivityFromFragment(getParentFragment(), intent, i);
        }
    }

    public final void startDialogFragment(Class<? extends QFragment> cls, Bundle bundle) {
        LauncherFragmentUtils.startDialogFragment(getActivity(), cls, setFragmentName(bundle));
    }

    public final void backToFragment(Class<? extends QFragment> cls, Bundle bundle) {
        LauncherFragmentUtils.backToFragment(getActivity(), cls, bundle);
    }

    public final void backToActivity(Class<? extends Activity> cls, Bundle bundle) {
        LauncherFragmentUtils.backToActivity(getActivity(), cls, bundle);
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
        startDialogFragment(cls, setFragmentName(null));
    }

    public final void startTransparentFragment(Class<? extends QFragment> cls) {
        startTransparentFragment(cls, setFragmentName(null));
    }

    public final void startFragment(Class<? extends QFragment> cls, boolean z) {
        LauncherFragmentUtils.startFragment(getActivity(), cls, setFragmentName(null), z);
    }

    public final void startFragment(Class<? extends QFragment> cls, Bundle bundle, boolean z) {
        LauncherFragmentUtils.startFragment(getActivity(), cls, setFragmentName(bundle), z);
    }

    public boolean onBackPressed() {
        return true;
    }
}
