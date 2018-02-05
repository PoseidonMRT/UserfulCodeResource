package com.huawei.hwid.openapi.quicklogin;

import android.content.Context;
import android.os.Bundle;
import java.util.HashMap;
import java.util.Map;

public final class a {
    private static a a;
    private String b;
    private Bundle c = null;
    private int d = 50;
    private Map e = new HashMap();
    private Context f = null;
    private com.huawei.hwid.openapi.a.a g;

    private a() {
    }

    public static synchronized a a() {
        a aVar;
        synchronized (a.class) {
            if (a == null) {
                a = new a();
            }
            aVar = a;
        }
        return aVar;
    }

    public String b() {
        return this.b;
    }

    public void a(String str) {
        this.b = str;
    }

    public Bundle c() {
        return this.c;
    }

    public void a(Bundle bundle) {
        this.c = bundle;
    }

    public void a(Context context) {
        this.f = context;
    }

    public void a(com.huawei.hwid.openapi.a.a aVar) {
        this.g = aVar;
    }

    public com.huawei.hwid.openapi.a.a d() {
        return this.g;
    }
}
