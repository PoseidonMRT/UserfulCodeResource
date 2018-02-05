package com.huawei.hwid.openapi.update.ui;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.MenuItem;
import com.huawei.hwid.openapi.quicklogin.d.a.c;
import com.huawei.hwid.openapi.quicklogin.d.b;
import java.util.ArrayList;

public abstract class BaseActivity extends Activity {
    private int a = 0;
    private int b = 0;
    private boolean c = false;
    private ProgressDialog d;
    private ArrayList e = new ArrayList();
    private boolean f = false;
    private boolean g = true;

    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
    }

    public void a(Dialog dialog) {
        if (dialog != null) {
            synchronized (this.e) {
                c.b("BaseActivity", "mManagedDialogList.size = " + this.e.size());
                this.e.add(dialog);
            }
        }
    }

    protected void onDestroy() {
        a();
        super.onDestroy();
    }

    public void a() {
        synchronized (this.e) {
            int size = this.e.size();
            for (int i = 0; i < size; i++) {
                Dialog dialog = (Dialog) this.e.get(i);
                if (dialog.isShowing()) {
                    dialog.dismiss();
                }
            }
            this.e.clear();
        }
    }

    public synchronized void a(String str) {
        if (TextUtils.isEmpty(str)) {
            str = getString(b.a(this, "CS_waiting_progress_message"));
        }
        c.a("BaseActivity", "oobe Login, showRequestProgressDialog theme id is " + com.huawei.hwid.openapi.update.b.c.b(this));
        if (this.d == null) {
            this.d = new a(this, this);
            this.d.setCanceledOnTouchOutside(false);
            this.d.setMessage(str);
            a(this.d);
        }
        c.a("BaseActivity", "this.isFinishing():" + isFinishing());
        if (!(this.d.isShowing() || isFinishing())) {
            this.d.setMessage(str);
            this.d.show();
        }
    }

    private boolean a(int i, KeyEvent keyEvent) {
        if ((4 == i && !this.c) || 84 == i) {
            return true;
        }
        if (this.c) {
            finish();
        }
        return false;
    }

    public synchronized void b() {
        c.b("BaseActivity", "dismissRequestProgressDialog");
        if (this.d != null && this.d.isShowing()) {
            this.d.dismiss();
        }
    }

    public void setContentView(int i) {
        int b = com.huawei.hwid.openapi.update.b.c.b(this);
        if (b != 0) {
            setTheme(b);
        }
        if (com.huawei.hwid.openapi.update.b.c.a || this.a == 0 || this.b == 0) {
            try {
                super.setContentView(i);
            } catch (IllegalStateException e) {
                c.d("BaseActivity", e.getMessage());
            } catch (Exception e2) {
                c.d("BaseActivity", e2.getMessage());
            }
            if (com.huawei.hwid.openapi.update.b.c.a && this.g) {
                try {
                    ActionBar actionBar = getActionBar();
                    if (actionBar != null) {
                        actionBar.setDisplayHomeAsUpEnabled(true);
                        if (this.a != 0) {
                            actionBar.setTitle(this.a);
                            return;
                        }
                        return;
                    }
                    return;
                } catch (Throwable e3) {
                    c.a("BaseActivity", e3.getMessage(), e3);
                    return;
                }
            }
            return;
        }
        super.setContentView(i);
    }

    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() != 16908332) {
            return super.onOptionsItemSelected(menuItem);
        }
        if (this.g) {
            onBackPressed();
        }
        return true;
    }

    protected void onActivityResult(int i, int i2, Intent intent) {
        synchronized (this) {
            this.f = false;
            super.onActivityResult(i, i2, intent);
        }
    }

    public synchronized void startActivityForResult(Intent intent, int i) {
        if ((intent.getFlags() & 268435456) == 0) {
            if (!this.f) {
                this.f = true;
            }
        }
        super.startActivityForResult(intent, i);
    }

    public void startActivity(Intent intent) {
        startActivityForResult(intent, 0);
    }

    public void onBackPressed() {
        try {
            super.onBackPressed();
        } catch (Throwable e) {
            c.b("BaseActivity", "catch Exception throw by FragmentManager!", e);
        }
    }
}
