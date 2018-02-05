package com.baidu.location.d;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Location;
import android.support.v4.widget.AutoScrollHelper;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.BDNotifyListener;
import com.baidu.location.Jni;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.platform.comapi.location.CoordinateType;
import java.util.ArrayList;
import java.util.Iterator;

public class a {
    private ArrayList<BDNotifyListener> a = null;
    private float b = AutoScrollHelper.NO_MAX;
    private BDLocation c = null;
    private long d = 0;
    private LocationClient e = null;
    private Context f = null;
    private int g = 0;
    private long h = 0;
    private boolean i = false;
    private PendingIntent j = null;
    private AlarmManager k = null;
    private a l = null;
    private b m = new b(this);
    private boolean n = false;

    public class a extends BroadcastReceiver {
        final /* synthetic */ a a;

        public a(a aVar) {
            this.a = aVar;
        }

        public void onReceive(Context context, Intent intent) {
            if (this.a.a != null && !this.a.a.isEmpty()) {
                this.a.e.requestNotifyLocation();
            }
        }
    }

    public class b implements BDLocationListener {
        final /* synthetic */ a a;

        public b(a aVar) {
            this.a = aVar;
        }

        public void onReceiveLocation(BDLocation bDLocation) {
            if (this.a.a != null && this.a.a.size() > 0) {
                this.a.a(bDLocation);
            }
        }
    }

    public a(Context context, LocationClient locationClient) {
        this.f = context;
        this.e = locationClient;
        this.e.registerNotifyLocationListener(this.m);
        this.k = (AlarmManager) this.f.getSystemService("alarm");
        this.l = new a(this);
        this.n = false;
    }

    private void a(long j) {
        try {
            if (this.j != null) {
                this.k.cancel(this.j);
            }
            this.j = PendingIntent.getBroadcast(this.f, 0, new Intent("android.com.baidu.location.TIMER.NOTIFY"), 134217728);
            if (this.j != null) {
                this.k.set(0, System.currentTimeMillis() + j, this.j);
            }
        } catch (Exception e) {
        }
    }

    private void a(BDLocation bDLocation) {
        if (bDLocation.getLocType() != 61 && bDLocation.getLocType() != BDLocation.TypeNetWorkLocation && bDLocation.getLocType() != 65) {
            a(120000);
        } else if (System.currentTimeMillis() - this.d >= 5000 && this.a != null) {
            this.c = bDLocation;
            this.d = System.currentTimeMillis();
            float[] fArr = new float[1];
            Iterator it = this.a.iterator();
            float f = AutoScrollHelper.NO_MAX;
            while (it.hasNext()) {
                BDNotifyListener bDNotifyListener = (BDNotifyListener) it.next();
                Location.distanceBetween(bDLocation.getLatitude(), bDLocation.getLongitude(), bDNotifyListener.mLatitudeC, bDNotifyListener.mLongitudeC, fArr);
                float radius = (fArr[0] - bDNotifyListener.mRadius) - bDLocation.getRadius();
                if (radius > 0.0f) {
                    if (radius < f) {
                    }
                    radius = f;
                } else {
                    if (bDNotifyListener.Notified < 3) {
                        bDNotifyListener.Notified++;
                        bDNotifyListener.onNotify(bDLocation, fArr[0]);
                        if (bDNotifyListener.Notified < 3) {
                            this.i = true;
                        }
                    }
                    radius = f;
                }
                f = radius;
            }
            if (f < this.b) {
                this.b = f;
            }
            this.g = 0;
            c();
        }
    }

    private boolean b() {
        if (this.a == null || this.a.isEmpty()) {
            return false;
        }
        Iterator it = this.a.iterator();
        boolean z = false;
        while (it.hasNext()) {
            z = ((BDNotifyListener) it.next()).Notified < 3 ? true : z;
        }
        return z;
    }

    private void c() {
        int i = LocationClientOption.MIN_AUTO_NOTIFY_INTERVAL;
        if (b()) {
            boolean z;
            int i2 = this.b > 5000.0f ? 600000 : this.b > 1000.0f ? 120000 : this.b > 500.0f ? 60000 : LocationClientOption.MIN_AUTO_NOTIFY_INTERVAL;
            if (this.i) {
                this.i = false;
            } else {
                i = i2;
            }
            if (this.g != 0) {
                if (((long) i) > (this.h + ((long) this.g)) - System.currentTimeMillis()) {
                    z = false;
                    if (z) {
                        this.g = i;
                        this.h = System.currentTimeMillis();
                        a((long) this.g);
                    }
                }
            }
            z = true;
            if (z) {
                this.g = i;
                this.h = System.currentTimeMillis();
                a((long) this.g);
            }
        }
    }

    public int a(BDNotifyListener bDNotifyListener) {
        if (this.a == null) {
            this.a = new ArrayList();
        }
        this.a.add(bDNotifyListener);
        bDNotifyListener.isAdded = true;
        bDNotifyListener.mNotifyCache = this;
        if (!this.n) {
            this.f.registerReceiver(this.l, new IntentFilter("android.com.baidu.location.TIMER.NOTIFY"));
            this.n = true;
        }
        if (bDNotifyListener.mCoorType != null) {
            if (!bDNotifyListener.mCoorType.equals(CoordinateType.GCJ02)) {
                double[] coorEncrypt = Jni.coorEncrypt(bDNotifyListener.mLongitude, bDNotifyListener.mLatitude, bDNotifyListener.mCoorType + "2gcj");
                bDNotifyListener.mLongitudeC = coorEncrypt[0];
                bDNotifyListener.mLatitudeC = coorEncrypt[1];
            }
            if (this.c == null || System.currentTimeMillis() - this.d > 30000) {
                this.e.requestNotifyLocation();
            } else {
                float[] fArr = new float[1];
                Location.distanceBetween(this.c.getLatitude(), this.c.getLongitude(), bDNotifyListener.mLatitudeC, bDNotifyListener.mLongitudeC, fArr);
                float radius = (fArr[0] - bDNotifyListener.mRadius) - this.c.getRadius();
                if (radius > 0.0f) {
                    if (radius < this.b) {
                        this.b = radius;
                    }
                } else if (bDNotifyListener.Notified < 3) {
                    bDNotifyListener.Notified++;
                    bDNotifyListener.onNotify(this.c, fArr[0]);
                    if (bDNotifyListener.Notified < 3) {
                        this.i = true;
                    }
                }
            }
            c();
        }
        return 1;
    }

    public void a() {
        if (this.j != null) {
            this.k.cancel(this.j);
        }
        this.c = null;
        this.d = 0;
        if (this.n) {
            this.f.unregisterReceiver(this.l);
        }
        this.n = false;
    }

    public void b(BDNotifyListener bDNotifyListener) {
        if (bDNotifyListener.mCoorType != null) {
            if (!bDNotifyListener.mCoorType.equals(CoordinateType.GCJ02)) {
                double[] coorEncrypt = Jni.coorEncrypt(bDNotifyListener.mLongitude, bDNotifyListener.mLatitude, bDNotifyListener.mCoorType + "2gcj");
                bDNotifyListener.mLongitudeC = coorEncrypt[0];
                bDNotifyListener.mLatitudeC = coorEncrypt[1];
            }
            if (this.c == null || System.currentTimeMillis() - this.d > 300000) {
                this.e.requestNotifyLocation();
            } else {
                float[] fArr = new float[1];
                Location.distanceBetween(this.c.getLatitude(), this.c.getLongitude(), bDNotifyListener.mLatitudeC, bDNotifyListener.mLongitudeC, fArr);
                float radius = (fArr[0] - bDNotifyListener.mRadius) - this.c.getRadius();
                if (radius > 0.0f) {
                    if (radius < this.b) {
                        this.b = radius;
                    }
                } else if (bDNotifyListener.Notified < 3) {
                    bDNotifyListener.Notified++;
                    bDNotifyListener.onNotify(this.c, fArr[0]);
                    if (bDNotifyListener.Notified < 3) {
                        this.i = true;
                    }
                }
            }
            c();
        }
    }

    public int c(BDNotifyListener bDNotifyListener) {
        if (this.a == null) {
            return 0;
        }
        if (this.a.contains(bDNotifyListener)) {
            this.a.remove(bDNotifyListener);
        }
        if (this.a.size() == 0 && this.j != null) {
            this.k.cancel(this.j);
        }
        return 1;
    }
}
