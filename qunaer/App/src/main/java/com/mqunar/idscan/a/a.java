package com.mqunar.idscan.a;

import android.hardware.Camera;
import android.hardware.Camera.AutoFocusCallback;
import android.os.AsyncTask;
import com.mqunar.tools.log.QLog;

final class a implements AutoFocusCallback {
    private static final String a = a.class.getSimpleName();
    private boolean b;
    private final boolean c = true;
    private final Camera d;
    private AsyncTask e;

    a(Camera camera) {
        this.d = camera;
        a();
    }

    final synchronized void a() {
        if (this.c) {
            this.b = true;
            try {
                this.d.autoFocus(this);
            } catch (Throwable e) {
                QLog.e(e);
            }
        }
    }

    final synchronized void b() {
        if (this.c) {
            try {
                this.d.cancelAutoFocus();
            } catch (RuntimeException e) {
                QLog.e(a, "Unexpected exception while cancelling focusing", e);
            }
        }
        if (this.e != null) {
            this.e.cancel(true);
            this.e = null;
        }
        this.b = false;
    }

    public final synchronized void onAutoFocus(boolean z, Camera camera) {
        if (z) {
            QLog.e("autofocus", "auto focus success", new Object[0]);
        } else {
            QLog.e("autofocus", "auto focus fail", new Object[0]);
        }
        if (this.b) {
            this.e = new b();
            this.e.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new Object[0]);
        }
    }
}
