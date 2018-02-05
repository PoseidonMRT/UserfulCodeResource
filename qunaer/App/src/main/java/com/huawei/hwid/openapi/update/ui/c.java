package com.huawei.hwid.openapi.update.ui;

import java.lang.reflect.Field;
import java.security.PrivilegedAction;

class c implements PrivilegedAction {
    final /* synthetic */ boolean a;
    final /* synthetic */ b b;

    c(b bVar, boolean z) {
        this.b = bVar;
        this.a = z;
    }

    public Object run() {
        try {
            Field declaredField = this.b.getClass().getSuperclass().getSuperclass().getDeclaredField("mShowing");
            declaredField.setAccessible(true);
            declaredField.set(this.b, Boolean.valueOf(this.a));
            if (this.a) {
                this.b.dismiss();
            }
        } catch (RuntimeException e) {
            com.huawei.hwid.openapi.quicklogin.d.a.c.d("CustomAlertDialog", "RuntimeException: " + e.getMessage());
        } catch (Exception e2) {
            com.huawei.hwid.openapi.quicklogin.d.a.c.d("CustomAlertDialog", "Exception: " + e2.getMessage());
        }
        return null;
    }
}
