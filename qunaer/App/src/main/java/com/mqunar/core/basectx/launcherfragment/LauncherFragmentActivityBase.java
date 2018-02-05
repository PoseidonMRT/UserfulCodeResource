package com.mqunar.core.basectx.launcherfragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import com.mqunar.core.basectx.IStackName;
import com.mqunar.core.basectx.fragment.QFragment;
import com.mqunar.core.basectx.launcherfragment.app.FlipActivityHelper;
import com.mqunar.core.basectx.launcherfragment.app.InnerFlipActivity;

public abstract class LauncherFragmentActivityBase extends InnerFlipActivity implements IStackName {
    protected String _fragmentName;
    protected int _id = -1;
    private QFragment fragment;
    protected boolean isFlip = true;
    protected boolean isPause = false;
    public FlipActivityHelper mFlipHelper = new FlipActivityHelper(this);
    protected Bundle myBundle;

    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        this.fragment.onNewIntent(intent);
    }

    public void finish() {
        super.finish();
    }

    protected void onResume() {
        super.onResume();
        this.isPause = false;
    }

    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        getWindow().requestFeature(1);
        View frameLayout = new FrameLayout(this);
        frameLayout.setId(16908292);
        setContentView(frameLayout);
        this.myBundle = bundle == null ? getIntent().getExtras() : bundle;
        if (this.myBundle == null) {
            this.myBundle = new Bundle();
        }
        if (this.myBundle.containsKey("_id")) {
            this._id = this.myBundle.getInt("_id");
        } else {
            this._id = QFragmentManager.getId();
        }
        if (this.myBundle.containsKey("_key_isFlip")) {
            this.isFlip = this.myBundle.getBoolean("_key_isFlip");
        }
        this.mFlipHelper.onCreate(this.myBundle);
        this.mFlipHelper.setCanFlip(this.isFlip);
        if (this.myBundle.containsKey(QFragment.key)) {
            this._fragmentName = this.myBundle.getString(QFragment.key);
        }
        if (bundle == null) {
            try {
                Class cls = Class.forName(this._fragmentName);
                if (this.myBundle.containsKey("_flag_clear_top") && this.myBundle.getBoolean("_flag_clear_top")) {
                    QFragmentManager.addSingleTopFragmentActivity(this, cls);
                }
                this.fragment = (QFragment) cls.newInstance();
                this.fragment.setArguments(this.myBundle);
                QFragmentManager.addFragment(this, this.fragment, getFragmentTag());
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (this.myBundle.containsKey("_isPause") && this.myBundle.getBoolean("_isPause")) {
            QFragmentManager.onResumeLauncherFragmentActivity(this);
            this.fragment = (QFragment) QFragmentManager.findFragmentByTag(this, getFragmentTag());
        }
    }

    private String getFragmentTag() {
        return this._fragmentName + this._id;
    }

    protected void onSaveInstanceState(Bundle bundle) {
        if (this.myBundle != null) {
            bundle.putAll(this.myBundle);
        }
        this.isPause = true;
        bundle.putBoolean("_isPause", this.isPause);
        bundle.putBoolean("_key_isFlip", this.isFlip);
        bundle.putInt("_id", this._id);
        bundle.putString(QFragment.key, this._fragmentName);
        this.mFlipHelper.onSaveInstanceState(bundle);
        super.onSaveInstanceState(bundle);
    }

    protected void onDestroy() {
        super.onDestroy();
        if (this.isPause) {
            QFragmentManager.onPauseLauncherFragmentActivity(this);
        } else {
            QFragmentManager.finishLauncherFragmentActivity(this);
        }
    }

    public boolean isCanFlip() {
        return this.mFlipHelper.isCanFlip();
    }

    public void setCanFlip(boolean z) {
        this.mFlipHelper.setCanFlip(z);
    }

    public boolean isTouchOnHScroll() {
        return this.mFlipHelper.isTouchOnHScroll();
    }

    public void setTouchOnHScroll(boolean z) {
        this.mFlipHelper.setTouchOnHScroll(z);
    }

    public boolean dispatchTouchEvent(MotionEvent motionEvent) {
        return this.mFlipHelper.dispatchTouchEvent(motionEvent);
    }

    public void onBackPressed() {
        if (this.fragment == null || !this.fragment.onBackPressed()) {
            if (this.fragment == null && tryDoBack()) {
                finish();
                if (this.isFlip) {
                    super.overridePendingTransition(0, getResId("anim", "spider_slide_out_right"));
                }
            }
        } else if (tryDoBack()) {
            finish();
            this.fragment = null;
            if (this.isFlip) {
                super.overridePendingTransition(0, getResId("anim", "spider_slide_out_right"));
            }
        }
    }

    private int getResId(String str, String str2) {
        return getResources().getIdentifier(str2, str, getPackageName());
    }

    public boolean tryDoBack() {
        try {
            FragmentManager supportFragmentManager = getSupportFragmentManager();
            if (supportFragmentManager.getBackStackEntryCount() <= 0 || !supportFragmentManager.popBackStackImmediate()) {
                return true;
            }
            return false;
        } catch (IllegalStateException e) {
            return true;
        }
    }

    public String getInStackName() {
        if (this.fragment == null) {
            return getClass().getName();
        }
        if (this.fragment instanceof IStackName) {
            return ((IStackName) this.fragment).getInStackName();
        }
        return this.fragment.getClass().getName();
    }

    protected void onActivityResult(int i, int i2, Intent intent) {
        super.onActivityResult(i, i2, intent);
    }
}
