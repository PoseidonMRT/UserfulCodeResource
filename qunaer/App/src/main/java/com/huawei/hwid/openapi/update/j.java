package com.huawei.hwid.openapi.update;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import com.huawei.hwid.openapi.e.a.c;

public class j {
    private static j a = null;
    private SharedPreferences b;

    public j(Context context) {
        this.b = context.getSharedPreferences("HwIDVersionInfo", 0);
    }

    public static synchronized j a(Context context) {
        j jVar;
        synchronized (j.class) {
            if (a == null) {
                a = new j(context);
            }
            jVar = a;
        }
        return jVar;
    }

    public void a(Context context, String str, String str2, String str3) {
        if (this.b != null) {
            Editor edit = this.b.edit();
            try {
                edit.putString("component_id", c.a(context, str));
                edit.putString("version_name", c.a(context, str2));
                edit.putString("download_path", c.a(context, str3));
                edit.commit();
            } catch (RuntimeException e) {
                com.huawei.hwid.openapi.quicklogin.d.a.c.b("OtaSharedPreferences", "e = " + e.getMessage());
            } catch (Exception e2) {
                com.huawei.hwid.openapi.quicklogin.d.a.c.b("OtaSharedPreferences", "e = " + e2.getMessage());
            }
        }
    }

    public SharedPreferences a() {
        return this.b;
    }

    public String b(Context context) {
        if (this.b != null) {
            return c.b(context, this.b.getString("version_name", ""));
        }
        return "";
    }

    public String c(Context context) {
        if (this.b != null) {
            return c.b(context, this.b.getString("download_path", ""));
        }
        return "";
    }

    public void b() {
        if (this.b != null) {
            Editor edit = this.b.edit();
            edit.clear();
            edit.commit();
        }
    }
}
