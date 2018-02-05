package com.baidu.location.c;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo.State;
import android.os.Handler;
import com.baidu.location.e.d;
import com.baidu.location.f;
import com.baidu.location.h.i;

public class h {
    private static h b = null;
    final Handler a = new Handler();
    private a c = null;
    private boolean d = false;
    private boolean e = false;
    private boolean f = false;
    private boolean g = true;
    private boolean h = false;

    class a extends BroadcastReceiver {
        final /* synthetic */ h a;

        private a(h hVar) {
            this.a = hVar;
        }

        public void onReceive(Context context, Intent intent) {
            if (context != null && this.a.a != null) {
                this.a.f();
            }
        }
    }

    class b implements Runnable {
        final /* synthetic */ h a;

        private b(h hVar) {
            this.a = hVar;
        }

        public void run() {
            if (this.a.d && b.a().e() && d.a().d()) {
                new Thread(this) {
                    final /* synthetic */ b a;

                    {
                        this.a = r1;
                    }

                    public void run() {
                        super.run();
                        d.a().m();
                        d.a().i();
                    }
                }.start();
            }
            if (this.a.d && b.a().e()) {
                f.a().d();
            }
            if (this.a.d && this.a.g) {
                this.a.a.postDelayed(this, (long) i.Q);
                this.a.f = true;
                return;
            }
            this.a.f = false;
        }
    }

    private h() {
    }

    public static synchronized h a() {
        h hVar;
        synchronized (h.class) {
            if (b == null) {
                b = new h();
            }
            hVar = b;
        }
        return hVar;
    }

    private void f() {
        State state;
        State state2 = State.UNKNOWN;
        try {
            state = ((ConnectivityManager) f.getServiceContext().getSystemService("connectivity")).getNetworkInfo(1).getState();
        } catch (Exception e) {
            state = state2;
        }
        if (State.CONNECTED != state) {
            this.d = false;
        } else if (!this.d) {
            this.d = true;
            this.a.postDelayed(new b(), (long) i.Q);
            this.f = true;
        }
    }

    public synchronized void b() {
        if (f.isServing) {
            if (!this.h) {
                try {
                    this.c = new a();
                    IntentFilter intentFilter = new IntentFilter();
                    intentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
                    f.getServiceContext().registerReceiver(this.c, intentFilter);
                    this.e = true;
                    f();
                } catch (Exception e) {
                }
                this.g = true;
                this.h = true;
            }
        }
    }

    public synchronized void c() {
        if (this.h) {
            try {
                f.getServiceContext().unregisterReceiver(this.c);
            } catch (Exception e) {
            }
            this.g = false;
            this.h = false;
            this.f = false;
            this.c = null;
        }
    }

    public void d() {
        if (this.h) {
            this.g = true;
            if (!this.f && this.g) {
                this.a.postDelayed(new b(), (long) i.Q);
                this.f = true;
            }
        }
    }

    public void e() {
        this.g = false;
    }
}
