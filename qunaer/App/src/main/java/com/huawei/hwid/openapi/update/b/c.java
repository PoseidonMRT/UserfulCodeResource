package com.huawei.hwid.openapi.update.b;

import android.app.AlertDialog.Builder;
import android.content.Context;
import android.os.Build.VERSION;

public class c {
    public static final boolean a = (VERSION.SDK_INT >= 11);

    public static Builder a(Context context, int i, int i2, boolean z) {
        return a(context, i2, context.getString(i), z);
    }

    public static Builder a(Context context, int i, String str, boolean z) {
        if (context == null) {
            com.huawei.hwid.openapi.quicklogin.d.a.c.b("UIUtil", "activity is null");
            return null;
        }
        Builder builder = new Builder(context, a(context));
        builder.setMessage(str);
        builder.setTitle(i);
        if (z) {
            builder.setPositiveButton(17039370, new d(context));
            builder.setOnCancelListener(new e(context));
        } else {
            builder.setPositiveButton(17039370, null);
        }
        return builder;
    }

    public static int a(Context context) {
        if (context == null) {
            com.huawei.hwid.openapi.quicklogin.d.a.c.b("UIUtil", "getDialogThemeId, context is null");
            return 3;
        }
        int b = b(context);
        if (VERSION.SDK_INT < 16 || b == 0) {
            return 3;
        }
        return 0;
    }

    public static int b(Context context) {
        return context.getResources().getIdentifier("androidhwext:style/Theme.Emui", null, null);
    }
}
