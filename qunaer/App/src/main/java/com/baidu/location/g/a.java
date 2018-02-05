package com.baidu.location.g;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.Messenger;
import android.os.Process;
import android.util.Log;
import com.baidu.location.LLSInterface;
import com.baidu.location.a.e;
import com.baidu.location.a.g;
import com.baidu.location.a.i;
import com.baidu.location.a.j;
import com.baidu.location.c.b;
import com.baidu.location.c.c;
import com.baidu.location.c.h;
import com.baidu.location.e.d;
import com.baidu.location.f;
import com.baidu.location.f.k;
import org.apache.http.HttpStatus;

public class a extends Service implements LLSInterface {
    static a a = null;
    private static long f = 0;
    Messenger b = null;
    private Looper c;
    private HandlerThread d;
    private boolean e = false;

    public class a extends Handler {
        final /* synthetic */ a a;

        public a(a aVar, Looper looper) {
            this.a = aVar;
            super(looper);
        }

        public void handleMessage(Message message) {
            if (f.isServing) {
                switch (message.what) {
                    case 11:
                        this.a.a(message);
                        break;
                    case 12:
                        this.a.b(message);
                        break;
                    case 15:
                        this.a.c(message);
                        break;
                    case 22:
                        e.b().b(message);
                        break;
                    case 28:
                        e.b().a(true);
                        break;
                    case 41:
                        e.b().h();
                        break;
                    case HttpStatus.SC_UNAUTHORIZED /*401*/:
                        try {
                            message.getData();
                            break;
                        } catch (Exception e) {
                            break;
                        }
                    case HttpStatus.SC_METHOD_NOT_ALLOWED /*405*/:
                        message.getData().getString("errorid", "");
                        break;
                }
            }
            if (message.what == 1) {
                this.a.d();
            }
            if (message.what == 0) {
                this.a.c();
            }
            super.handleMessage(message);
        }
    }

    public static Handler a() {
        return a;
    }

    private void a(Message message) {
        Log.d("baidu_location_service", "baidu location service register ...");
        com.baidu.location.a.a.a().a(message);
        d.a();
        c.a().d();
        g.b().c();
    }

    public static long b() {
        return f;
    }

    private void b(Message message) {
        com.baidu.location.a.a.a().b(message);
    }

    private void c() {
        com.baidu.location.f.c.a().b();
        k.a().b();
        com.baidu.location.h.c.a();
        e.b().c();
        com.baidu.location.e.a.a().b();
        b.a().b();
        c.a().b();
        com.baidu.location.c.d.a().b();
    }

    private void c(Message message) {
        com.baidu.location.a.a.a().c(message);
    }

    private void d() {
        k.a().c();
        d.a().n();
        com.baidu.location.f.f.a().e();
        h.a().c();
        c.a().c();
        b.a().c();
        com.baidu.location.c.a.a().c();
        com.baidu.location.f.c.a().c();
        e.b().d();
        j.e();
        com.baidu.location.a.a.a().b();
        com.baidu.location.c.e.a().b();
        Log.d("baidu_location_service", "baidu location service has stoped ...");
        if (!this.e) {
            Process.killProcess(Process.myPid());
        }
    }

    public double getVersion() {
        return 6.329999923706055d;
    }

    public IBinder onBind(Intent intent) {
        Bundle extras = intent.getExtras();
        boolean z = false;
        if (extras != null) {
            com.baidu.location.h.c.g = extras.getString("key");
            com.baidu.location.h.c.f = extras.getString("sign");
            this.e = extras.getBoolean("kill_process");
            z = extras.getBoolean("cache_exception");
        }
        if (!z) {
            Thread.setDefaultUncaughtExceptionHandler(com.baidu.location.c.d.a());
        }
        return this.b.getBinder();
    }

    public void onCreate(Context context) {
        f = System.currentTimeMillis();
        this.d = i.a();
        this.c = this.d.getLooper();
        a = new a(this, this.c);
        this.b = new Messenger(a);
        a.sendEmptyMessage(0);
        Log.d("baidu_location_service", "baidu location service start1 ..." + Process.myPid());
    }

    public void onDestroy() {
        a.sendEmptyMessage(1);
        Log.d("baidu_location_service", "baidu location service stop ...");
    }

    public int onStartCommand(Intent intent, int i, int i2) {
        return 1;
    }

    public boolean onUnBind(Intent intent) {
        return false;
    }
}
