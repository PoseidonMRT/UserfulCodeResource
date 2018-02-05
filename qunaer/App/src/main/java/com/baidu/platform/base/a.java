package com.baidu.platform.base;

import android.os.Handler;
import android.os.Looper;
import com.baidu.mapapi.http.AsyncHttpClient;

public abstract class a {
    protected f a;
    private AsyncHttpClient b = new AsyncHttpClient();
    private Handler c = new Handler(Looper.getMainLooper());
    private String d;

    private boolean a() {
        return Looper.myLooper() == Looper.getMainLooper();
    }

    protected boolean a(g gVar) {
        String a = gVar.a();
        if (a == null) {
            this.d = "{SDK_InnerError:{PermissionCheckError:Error}}";
            this.a.a(this.d);
            return false;
        }
        this.b.get(a, new b(this));
        return true;
    }
}
