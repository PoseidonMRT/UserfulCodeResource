package com.baidu.location.c;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import com.baidu.location.f;

public class b {
    private static b d = null;
    private boolean a = false;
    private String b = null;
    private a c = null;
    private int e = -1;

    public class a extends BroadcastReceiver {
        final /* synthetic */ b a;

        public a(b bVar) {
            this.a = bVar;
        }

        public void onReceive(Context context, Intent intent) {
            try {
                if (intent.getAction().equals("android.intent.action.BATTERY_CHANGED")) {
                    this.a.a = false;
                    int intExtra = intent.getIntExtra("status", 0);
                    int intExtra2 = intent.getIntExtra("plugged", 0);
                    int intExtra3 = intent.getIntExtra("level", -1);
                    int intExtra4 = intent.getIntExtra("scale", -1);
                    if (intExtra3 <= 0 || intExtra4 <= 0) {
                        this.a.e = -1;
                    } else {
                        this.a.e = (intExtra3 * 100) / intExtra4;
                    }
                    switch (intExtra) {
                        case 2:
                            this.a.b = "4";
                            break;
                        case 3:
                        case 4:
                            this.a.b = "3";
                            break;
                        default:
                            this.a.b = null;
                            break;
                    }
                    switch (intExtra2) {
                        case 1:
                            this.a.b = "6";
                            this.a.a = true;
                            return;
                        case 2:
                            this.a.b = "5";
                            this.a.a = true;
                            return;
                        default:
                            return;
                    }
                }
            } catch (Exception e) {
                this.a.b = null;
            }
        }
    }

    private b() {
    }

    public static synchronized b a() {
        b bVar;
        synchronized (b.class) {
            if (d == null) {
                d = new b();
            }
            bVar = d;
        }
        return bVar;
    }

    public void b() {
        this.c = new a(this);
        f.getServiceContext().registerReceiver(this.c, new IntentFilter("android.intent.action.BATTERY_CHANGED"));
    }

    public void c() {
        if (this.c != null) {
            try {
                f.getServiceContext().unregisterReceiver(this.c);
            } catch (Exception e) {
            }
        }
        this.c = null;
    }

    public String d() {
        return this.b;
    }

    public boolean e() {
        return this.a;
    }

    public int f() {
        return this.e;
    }
}
