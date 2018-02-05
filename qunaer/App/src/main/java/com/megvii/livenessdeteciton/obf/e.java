package com.megvii.livenessdeteciton.obf;

import android.content.Context;
import android.content.SharedPreferences;
import java.security.InvalidParameterException;

public final class e {
    private String a;
    private SharedPreferences b;

    private e(Context context, String str, String str2) {
        this.a = "";
        if (context == null) {
            throw new InvalidParameterException();
        }
        this.a = str2;
        this.b = context.getApplicationContext().getSharedPreferences(str, 0);
    }

    public e(Context context) {
        this(context, "MegviiSDKPreference", "");
    }

    public final synchronized void a(String str, String str2) {
        this.b.edit().putString(str + this.a, str2).apply();
    }

    public final synchronized String a(String str) {
        return this.b.getString(str + this.a, null);
    }

    public final synchronized String b(String str) {
        String string;
        string = this.b.getString(str + this.a, null);
        this.b.edit().remove(str + this.a).apply();
        return string;
    }
}
