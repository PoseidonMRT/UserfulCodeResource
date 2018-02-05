package com.mqunar.idscan.decode;

import android.app.Activity;
import android.os.AsyncTask;

public final class e {
    private static final String a = e.class.getSimpleName();
    private final Activity b;
    private AsyncTask c;

    public e(Activity activity) {
        this.b = activity;
    }

    private synchronized void f() {
        AsyncTask asyncTask = this.c;
        if (asyncTask != null) {
            asyncTask.cancel(true);
            this.c = null;
        }
    }

    public final synchronized void a() {
        f();
        this.c = new f();
        this.c.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new Object[0]);
    }

    public final synchronized void b() {
        f();
    }

    public final synchronized void c() {
        a();
    }

    public final void d() {
        f();
    }
}
