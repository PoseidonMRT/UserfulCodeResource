package com.baidu.location.c;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.Message;
import com.baidu.location.LocationClientOption;
import com.baidu.location.a.e;
import com.baidu.location.f;
import com.baidu.location.h.i;

public class a {
    private static a a = null;
    private boolean b = false;
    private Handler c = null;
    private AlarmManager d = null;
    private a e = null;
    private PendingIntent f = null;
    private long g = 0;

    class a extends BroadcastReceiver {
        final /* synthetic */ a a;

        private a(a aVar) {
            this.a = aVar;
        }

        public void onReceive(Context context, Intent intent) {
            if (this.a.b && intent.getAction().equals("com.baidu.location.autonotifyloc_6.3.3") && this.a.c != null) {
                this.a.f = null;
                this.a.c.sendEmptyMessage(1);
            }
        }
    }

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

    private void f() {
        if (System.currentTimeMillis() - this.g >= 1000) {
            if (this.f != null) {
                this.d.cancel(this.f);
                this.f = null;
            }
            if (this.f == null) {
                this.f = PendingIntent.getBroadcast(f.getServiceContext(), 0, new Intent("com.baidu.location.autonotifyloc_6.3.3"), 134217728);
                this.d.set(0, System.currentTimeMillis() + ((long) i.W), this.f);
            }
            Message message = new Message();
            message.what = 22;
            if (System.currentTimeMillis() - this.g >= ((long) i.X)) {
                this.g = System.currentTimeMillis();
                if (!com.baidu.location.f.f.a().i()) {
                    e.b().b(message);
                }
            }
        }
    }

    private void g() {
        if (this.b) {
            try {
                if (this.f != null) {
                    this.d.cancel(this.f);
                    this.f = null;
                }
                f.getServiceContext().unregisterReceiver(this.e);
            } catch (Exception e) {
            }
            this.d = null;
            this.e = null;
            this.c = null;
            this.b = false;
        }
    }

    public void b() {
        if (!this.b && i.W >= LocationClientOption.MIN_AUTO_NOTIFY_INTERVAL) {
            if (this.c == null) {
                this.c = new Handler(this) {
                    final /* synthetic */ a a;

                    {
                        this.a = r1;
                    }

                    public void handleMessage(Message message) {
                        switch (message.what) {
                            case 1:
                                try {
                                    this.a.f();
                                    return;
                                } catch (Exception e) {
                                    return;
                                }
                            case 2:
                                try {
                                    this.a.g();
                                    return;
                                } catch (Exception e2) {
                                    return;
                                }
                            default:
                                return;
                        }
                    }
                };
            }
            this.d = (AlarmManager) f.getServiceContext().getSystemService("alarm");
            this.e = new a();
            f.getServiceContext().registerReceiver(this.e, new IntentFilter("com.baidu.location.autonotifyloc_6.3.3"));
            this.f = PendingIntent.getBroadcast(f.getServiceContext(), 0, new Intent("com.baidu.location.autonotifyloc_6.3.3"), 134217728);
            this.d.set(0, System.currentTimeMillis() + ((long) i.W), this.f);
            this.b = true;
            this.g = System.currentTimeMillis();
        }
    }

    public void c() {
        if (this.b && this.c != null) {
            this.c.sendEmptyMessage(2);
        }
    }

    public void d() {
        if (this.b && this.c != null) {
            this.c.sendEmptyMessage(1);
        }
    }

    public void e() {
        if (this.b && this.c != null) {
            this.c.sendEmptyMessage(1);
        }
    }
}
