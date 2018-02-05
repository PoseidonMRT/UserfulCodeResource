package com.mqunar.qutui;

import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.os.RemoteCallbackList;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import com.mqunar.atomenv.GlobalEnv;
import com.mqunar.cock.network.CockHandler;
import com.mqunar.cock.network.ImListener;
import com.mqunar.cock.network.OneKeyCremation;
import com.mqunar.qutui.IQutuiInterface.Stub;
import com.mqunar.tools.log.QLog;

public class NetService extends Service {
    public static final String ACTION_MESSAGE_CONNECT_STATE = "com.mqunar.dispatcher.MESSAGE_CONNECT_STATE";
    private static boolean h = false;
    NotificationReceiver a = null;
    KeepLiveReceiver b = null;
    private final String c = "NetService";
    private ImListener d;
    private String e;
    private boolean f;
    private RemoteCallbackList<OnIMMessage> g = new RemoteCallbackList();
    private final Stub i = new c(this);

    @Nullable
    public IBinder onBind(Intent intent) {
        h = false;
        return this.i;
    }

    public void onCreate() {
        super.onCreate();
        h = true;
        this.d = new d(this);
        CockHandler.getInstance().registerImListener(this.d);
    }

    public int onStartCommand(Intent intent, int i, int i2) {
        if (intent != null) {
            try {
                if (NetService.class.getName().equals(intent.getAction()) && intent.hasExtra("source")) {
                    String stringExtra = intent.getStringExtra("source");
                    boolean z = h;
                    QLog.i("startCommand form source:" + stringExtra + ", effectiveWakeUp = " + z, new Object[0]);
                    QutuiLog.getInstance(getApplicationContext()).a(stringExtra, z);
                }
            } catch (Throwable e) {
                QLog.e(e);
            }
        }
        h = false;
        QutuiLog.getInstance(getApplicationContext()).a();
        IntentFilter intentFilter = new IntentFilter("android.intent.action.TIME_TICK");
        this.a = new NotificationReceiver();
        registerReceiver(this.a, intentFilter);
        this.b = new KeepLiveReceiver();
        if (intent != null) {
            this.e = intent.getStringExtra("yid");
            this.f = intent.getBooleanExtra("isRelease", true);
        }
        if (TextUtils.isEmpty(this.e)) {
            try {
                this.e = GlobalEnv.getInstance().getGid();
            } catch (Throwable e2) {
                QLog.e(e2);
            }
        }
        try {
            CockHandler.getInstance().setRelease(GlobalEnv.getInstance().isRelease());
        } catch (Throwable e22) {
            CockHandler.getInstance().setRelease(this.f);
            QLog.e(e22);
        }
        CockHandler.getInstance().setYid(this.e);
        intentFilter = new IntentFilter();
        intentFilter.addAction("android.intent.action.SCREEN_OFF");
        intentFilter.addAction("android.intent.action.SCREEN_ON");
        intentFilter.addAction("android.intent.action.USER_PRESENT");
        intentFilter.addAction("com.mqunar.dispatcher.MESSAGE_CONNECT_STATE");
        QLog.d("NetService", "onStartCommand", new Object[0]);
        OneKeyCremation.getInstance().oneKeyCremation();
        return 1;
    }

    public void onDestroy() {
        super.onDestroy();
        QLog.d("SERVICE DESTROY", new Object[0]);
        try {
            unregisterReceiver(this.a);
            unregisterReceiver(this.b);
            CockHandler.getInstance().unregisterImListener(this.d);
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.d = null;
    }

    private void a(byte[] bArr) {
        try {
            int beginBroadcast = this.g.beginBroadcast();
            for (int i = 0; i < beginBroadcast; i++) {
                try {
                    ((OnIMMessage) this.g.getBroadcastItem(i)).onImMessage(bArr);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            this.g.finishBroadcast();
        } catch (Exception e2) {
            e2.printStackTrace();
        }
    }

    private void a() {
        int beginBroadcast = this.g.beginBroadcast();
        for (int i = 0; i < beginBroadcast; i++) {
            try {
                ((OnIMMessage) this.g.getBroadcastItem(i)).onConnected();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        this.g.finishBroadcast();
    }

    private void b() {
        int beginBroadcast = this.g.beginBroadcast();
        for (int i = 0; i < beginBroadcast; i++) {
            try {
                ((OnIMMessage) this.g.getBroadcastItem(i)).onClosed();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        this.g.finishBroadcast();
    }
}
