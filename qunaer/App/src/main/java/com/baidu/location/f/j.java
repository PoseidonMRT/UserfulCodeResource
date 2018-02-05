package com.baidu.location.f;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.NetworkInfo.State;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build.VERSION;
import android.os.Handler;
import com.baidu.location.a.e;
import com.baidu.location.a.h;
import com.baidu.location.f;
import java.lang.reflect.Field;
import java.util.List;

public class j extends l {
    public static long a = 0;
    private static j b = null;
    private WifiManager c = null;
    private a d = null;
    private i e = null;
    private long f = 0;
    private long g = 0;
    private boolean h = false;
    private Object i = null;
    private boolean j = true;
    private Handler k = new Handler();

    class a extends BroadcastReceiver {
        final /* synthetic */ j a;
        private long b;
        private boolean c;

        private a(j jVar) {
            this.a = jVar;
            this.b = 0;
            this.c = false;
        }

        public void onReceive(Context context, Intent intent) {
            if (context != null) {
                String action = intent.getAction();
                if (action.equals("android.net.wifi.SCAN_RESULTS")) {
                    j.a = System.currentTimeMillis() / 1000;
                    this.a.n();
                    e.b().h();
                    if (System.currentTimeMillis() - h.b() <= 5000) {
                        com.baidu.location.a.j.a(h.c(), this.a.j(), h.d(), h.a());
                    }
                } else if (action.equals("android.net.wifi.STATE_CHANGE") && ((NetworkInfo) intent.getParcelableExtra("networkInfo")).getState().equals(State.CONNECTED) && System.currentTimeMillis() - this.b >= 5000) {
                    this.b = System.currentTimeMillis();
                    if (!this.c) {
                        this.c = true;
                    }
                }
            }
        }
    }

    private j() {
    }

    public static synchronized j a() {
        j jVar;
        synchronized (j.class) {
            if (b == null) {
                b = new j();
            }
            jVar = b;
        }
        return jVar;
    }

    public static boolean a(i iVar, i iVar2, float f) {
        if (iVar == null || iVar2 == null) {
            return false;
        }
        List list = iVar.a;
        List list2 = iVar2.a;
        if (list == list2) {
            return true;
        }
        if (list == null || list2 == null) {
            return false;
        }
        int size = list.size();
        int size2 = list2.size();
        float f2 = (float) (size + size2);
        if (size == 0 && size2 == 0) {
            return true;
        }
        if (size == 0 || size2 == 0) {
            return false;
        }
        int i = 0;
        int i2 = 0;
        while (i < size) {
            int i3;
            String str = ((ScanResult) list.get(i)).BSSID;
            if (str == null) {
                i3 = i2;
            } else {
                for (int i4 = 0; i4 < size2; i4++) {
                    if (str.equals(((ScanResult) list2.get(i4)).BSSID)) {
                        i3 = i2 + 1;
                        break;
                    }
                }
                i3 = i2;
            }
            i++;
            i2 = i3;
        }
        return ((float) (i2 * 2)) > f2 * f;
    }

    private void n() {
        if (this.c != null) {
            try {
                i iVar = new i(this.c.getScanResults(), System.currentTimeMillis());
                if (this.e == null || !iVar.a(this.e)) {
                    this.e = iVar;
                }
            } catch (Exception e) {
            }
        }
    }

    public synchronized void b() {
        if (!this.h) {
            if (f.isServing) {
                this.c = (WifiManager) f.getServiceContext().getSystemService("wifi");
                this.d = new a();
                try {
                    f.getServiceContext().registerReceiver(this.d, new IntentFilter("android.net.wifi.SCAN_RESULTS"));
                } catch (Exception e) {
                }
                this.h = true;
                try {
                    Field declaredField = Class.forName("android.net.wifi.WifiManager").getDeclaredField("mService");
                    if (declaredField != null) {
                        declaredField.setAccessible(true);
                        this.i = declaredField.get(this.c);
                        this.i.getClass();
                    }
                } catch (Exception e2) {
                }
            }
        }
    }

    public synchronized void c() {
        if (this.h) {
            try {
                f.getServiceContext().unregisterReceiver(this.d);
                a = 0;
            } catch (Exception e) {
            }
            this.d = null;
            this.c = null;
            this.h = false;
        }
    }

    public boolean d() {
        long currentTimeMillis = System.currentTimeMillis();
        if (currentTimeMillis - this.g > 0 && currentTimeMillis - this.g <= 5000) {
            return false;
        }
        this.g = currentTimeMillis;
        return e();
    }

    public boolean e() {
        if (this.c == null) {
            return false;
        }
        long currentTimeMillis = System.currentTimeMillis();
        if (currentTimeMillis - this.f > 0) {
            if (currentTimeMillis - this.f <= 5000 || currentTimeMillis - (a * 1000) <= 5000) {
                return false;
            }
            if (h() && currentTimeMillis - this.f <= 10000) {
                return false;
            }
        }
        return g();
    }

    public String f() {
        String str = "";
        if (this.c == null) {
            return str;
        }
        try {
            return (this.c.isWifiEnabled() || (VERSION.SDK_INT > 17 && this.c.isScanAlwaysAvailable())) ? "&wifio=1" : str;
        } catch (NoSuchMethodError e) {
            return str;
        } catch (Exception e2) {
            return str;
        }
    }

    public boolean g() {
        try {
            if (!this.c.isWifiEnabled() && (VERSION.SDK_INT <= 17 || !this.c.isScanAlwaysAvailable())) {
                return false;
            }
            this.c.startScan();
            this.f = System.currentTimeMillis();
            return true;
        } catch (NoSuchMethodError e) {
            return false;
        } catch (Exception e2) {
            return false;
        }
    }

    public boolean h() {
        try {
            NetworkInfo activeNetworkInfo = ((ConnectivityManager) f.getServiceContext().getSystemService("connectivity")).getActiveNetworkInfo();
            return activeNetworkInfo != null && activeNetworkInfo.getType() == 1;
        } catch (Exception e) {
            return false;
        }
    }

    public WifiInfo i() {
        if (this.c == null) {
            return null;
        }
        try {
            WifiInfo connectionInfo = this.c.getConnectionInfo();
            if (connectionInfo == null || connectionInfo.getBSSID() == null) {
                return null;
            }
            String bssid = connectionInfo.getBSSID();
            if (bssid != null) {
                bssid = bssid.replace(":", "");
                if ("000000000000".equals(bssid) || "".equals(bssid)) {
                    return null;
                }
            }
            return connectionInfo;
        } catch (Exception e) {
            return null;
        }
    }

    public i j() {
        return (this.e == null || !this.e.f()) ? l() : this.e;
    }

    public i k() {
        return (this.e == null || !this.e.g()) ? l() : this.e;
    }

    public i l() {
        if (this.c != null) {
            try {
                return new i(this.c.getScanResults(), this.f);
            } catch (Exception e) {
            }
        }
        return new i(null, 0);
    }

    public String m() {
        String str = null;
        try {
            WifiInfo connectionInfo = this.c.getConnectionInfo();
            if (connectionInfo != null) {
                str = connectionInfo.getMacAddress();
            }
        } catch (Exception e) {
        }
        return str;
    }
}
