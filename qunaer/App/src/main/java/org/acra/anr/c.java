package org.acra.anr;

import android.content.Context;
import android.os.FileObserver;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import java.io.File;
import org.acra.ACRA;

class c {
    private static FileObserver c;
    private final int a = 5000;
    private boolean b = true;

    final class a extends Thread {
        final /* synthetic */ c a;
        private int b;

        a(c cVar, int i) {
            this.a = cVar;
            this.b = i;
        }

        public void run() {
            SystemClock.sleep((long) this.b);
            this.a.b = true;
        }
    }

    c() {
    }

    public final void a(@NonNull final Context context) {
        ACRA.f.b(ACRA.e, "startANRMonitor FileObserver");
        c = new FileObserver(this, "/data/anr/", 8) {
            final /* synthetic */ c b;

            public void onEvent(int i, String str) {
                if (str != null && str.contains("trace") && this.b.b && new File("/data/anr/" + str).length() != 0) {
                    this.b.b = false;
                    a.a(context, 50, 1, "/data/anr/" + str);
                    new a(this.b, 5000).start();
                }
            }
        };
        c.startWatching();
    }
}
