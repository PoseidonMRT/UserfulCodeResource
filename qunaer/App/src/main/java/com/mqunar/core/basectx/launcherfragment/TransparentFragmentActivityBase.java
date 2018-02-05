package com.mqunar.core.basectx.launcherfragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.widget.FrameLayout;
import com.mqunar.core.basectx.IStackName;
import com.mqunar.core.basectx.activity.QFragmentActivity;
import com.mqunar.core.basectx.fragment.QFragment;

public abstract class TransparentFragmentActivityBase extends QFragmentActivity implements IStackName {
    protected String _fragmentName;
    private QFragment fragment;
    protected Bundle myBundle;

    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (this.fragment != null) {
            this.fragment.onNewIntent(intent);
        }
    }

    public void setQFragment(QFragment qFragment) {
        this.fragment = qFragment;
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
        if (this.myBundle.containsKey(QFragment.key)) {
            this._fragmentName = this.myBundle.getString(QFragment.key);
        }
        if (bundle == null) {
            try {
                this.fragment = (QFragment) Class.forName(this._fragmentName).newInstance();
                this.fragment.setArguments(this.myBundle);
                QFragmentManager.addFragment(this, this.fragment, getFragmentTag());
                return;
            } catch (Exception e) {
                e.printStackTrace();
                return;
            }
        }
        this.fragment = (QFragment) QFragmentManager.findFragmentByTag(this, getFragmentTag());
    }

    private String getFragmentTag() {
        return this._fragmentName;
    }

    protected void onSaveInstanceState(Bundle bundle) {
        if (this.myBundle != null) {
            bundle.putAll(this.myBundle);
        }
        bundle.putString(QFragment.key, this._fragmentName);
        super.onSaveInstanceState(bundle);
    }

    public void onBackPressed() {
        if (this.fragment == null || !this.fragment.onBackPressed()) {
            if (this.fragment == null && tryDoBack()) {
                finish();
            }
        } else if (tryDoBack()) {
            finish();
            this.fragment = null;
        }
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
