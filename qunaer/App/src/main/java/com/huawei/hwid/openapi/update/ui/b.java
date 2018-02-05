package com.huawei.hwid.openapi.update.ui;

import android.app.AlertDialog;
import android.content.Context;
import com.huawei.hwid.openapi.update.b.c;
import java.security.AccessController;

public class b extends AlertDialog {
    public b(Context context) {
        super(context, c.a(context));
    }

    public void onBackPressed() {
        a(true);
        try {
            super.onBackPressed();
        } catch (Throwable e) {
            com.huawei.hwid.openapi.quicklogin.d.a.c.b("CustomAlertDialog", "catch Exception", e);
        }
    }

    public boolean onSearchRequested() {
        a(true);
        return super.onSearchRequested();
    }

    public void a(boolean z) {
        try {
            AccessController.doPrivileged(new c(this, z));
        } catch (Exception e) {
            com.huawei.hwid.openapi.quicklogin.d.a.c.d("CustomAlertDialog", "Exception: " + e.getMessage());
        }
    }
}
