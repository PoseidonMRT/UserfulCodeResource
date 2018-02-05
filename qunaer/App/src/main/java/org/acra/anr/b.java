package org.acra.anr;

import android.content.Context;
import android.os.Build.VERSION;
import android.os.FileObserver;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import org.acra.ACRA;

public class b {
    private FileObserver a;
    private boolean b = false;

    public void a(@NonNull Context context) {
        switch (2) {
            case 0:
                new c().a(context);
                return;
            case 1:
                new d().a(context);
                return;
            case 2:
                if (VERSION.SDK_INT < 21) {
                    new c().a(context);
                    return;
                } else {
                    b(context);
                    return;
                }
            default:
                return;
        }
    }

    private void b(@NonNull final Context context) {
        new Thread(this) {
            final /* synthetic */ b b;

            public void run() {
                this.b.d(context);
                this.b.c(context);
            }
        }.start();
    }

    private void c(@NonNull Context context) {
        SystemClock.sleep((long) 1000);
        if (!this.b) {
            new d().a(context);
        }
    }

    private void d(@NonNull final Context context) {
        this.a = new FileObserver(this, "/data/anr/", 4095) {
            final /* synthetic */ b b;

            public void onEvent(int i, String str) {
                this.b.a.stopWatching();
                if (!this.b.b) {
                    this.b.b = true;
                    new c().a(context);
                }
            }
        };
        this.a.startWatching();
        try {
            Runtime.getRuntime().exec("ls /data/anr/");
        } catch (Exception e) {
            ACRA.f.b(ACRA.e, "exec ls /data/anr/ failed ");
        }
    }
}
