package com.mqunar.atomenv;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Looper;
import android.os.Message;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import com.mqunar.atomenv.version.VersionUtils;
import java.io.File;
import java.util.concurrent.TimeoutException;

public class AtomUpdateListener extends BroadcastReceiver implements Callback {
    private IntentFilter a;
    private AtomFileCallback b;
    private Context c;
    private String d;
    private Handler e;
    private long f;

    public interface AtomFileCallback {
        void onError(Throwable th);

        void onSuccess(File file);
    }

    public AtomUpdateListener(Context context, String str, AtomFileCallback atomFileCallback) {
        this(context, str, atomFileCallback, 0);
    }

    public AtomUpdateListener(Context context, String str, AtomFileCallback atomFileCallback, long j) {
        this.a = new IntentFilter();
        this.a.addAction("com.mqunar.spider.module.download");
        this.c = context;
        this.b = atomFileCallback;
        this.d = str;
        this.f = j;
        this.e = new Handler(Looper.getMainLooper(), this);
        a();
    }

    void a() {
        if (this.f < 0) {
            throw new IllegalArgumentException("timeout must be >= 0");
        } else if (TextUtils.isEmpty(this.d)) {
            throw new IllegalArgumentException("packageName must be not null");
        }
    }

    public void start() {
        File atomFileWithUpdate = VersionUtils.getAtomFileWithUpdate(this.c, this.d);
        if (atomFileWithUpdate == null || !atomFileWithUpdate.exists()) {
            b();
            LocalBroadcastManager.getInstance(this.c).registerReceiver(this, this.a);
            return;
        }
        this.b.onSuccess(atomFileWithUpdate);
    }

    public void stop() {
        c();
        LocalBroadcastManager.getInstance(this.c).unregisterReceiver(this);
    }

    public void onReceive(Context context, Intent intent) {
        this.e.sendEmptyMessageDelayed(1, 100);
    }

    public boolean handleMessage(Message message) {
        if (message.what == 0) {
            stop();
            this.b.onError(new TimeoutException());
        } else if (message.what == 1) {
            File atomFileWithUpdate = VersionUtils.getAtomFileWithUpdate(this.c, this.d);
            if (atomFileWithUpdate != null && atomFileWithUpdate.exists()) {
                c();
                this.b.onSuccess(atomFileWithUpdate);
            }
        }
        return true;
    }

    void b() {
        if (this.f != 0) {
            this.e.sendEmptyMessageDelayed(0, this.f);
        }
    }

    void c() {
        if (this.f != 0) {
            this.e.removeMessages(0);
        }
    }
}
