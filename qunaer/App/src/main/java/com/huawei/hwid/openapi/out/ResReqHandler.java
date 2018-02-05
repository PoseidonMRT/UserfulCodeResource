package com.huawei.hwid.openapi.out;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import com.huawei.hwid.openapi.a.b;
import com.huawei.hwid.openapi.quicklogin.d.b.d;

public abstract class ResReqHandler implements Runnable {
    private static final String TAG = b.a;
    Bundle bd = null;
    Handler handler = null;
    boolean isNeedRun = true;

    public abstract void onComplete(Bundle bundle);

    public ResReqHandler() {
        if (Looper.myLooper() != null) {
            this.handler = new Handler();
        }
    }

    public final void setStop() {
        this.isNeedRun = false;
    }

    public final void finish(Bundle bundle) {
        try {
            this.bd = bundle;
            if (!this.isNeedRun) {
                return;
            }
            if (this.handler != null) {
                this.handler.post(this);
            } else {
                onComplete(bundle);
            }
        } catch (Throwable th) {
            d.b(TAG, th.toString(), th);
        }
    }

    public final void run() {
        onComplete(this.bd);
    }
}
