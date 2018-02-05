package com.baidu.location.a;

import android.location.Location;
import android.os.Build.VERSION;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import com.baidu.location.Address;
import com.baidu.location.BDLocation;
import com.baidu.location.Poi;
import com.baidu.location.c.g;
import com.baidu.location.e.d;
import com.baidu.location.f.c;
import com.baidu.location.f.f;
import com.baidu.location.f.i;
import com.baidu.location.f.j;
import com.baidu.location.f.k;
import com.iflytek.cloud.SpeechConstant;
import com.mqunar.tools.DateTimeUtils;
import java.util.List;

public class e extends d {
    public static boolean h = false;
    private static e i = null;
    private double A;
    private boolean B;
    private long C;
    private long D;
    private a E;
    private boolean F;
    private boolean G;
    private boolean H;
    private boolean I;
    private b J;
    private boolean K;
    final int e;
    public b f;
    public final Handler g;
    private boolean j;
    private String k;
    private BDLocation l;
    private BDLocation m;
    private i n;
    private com.baidu.location.f.a o;
    private i p;
    private com.baidu.location.f.a q;
    private boolean r;
    private volatile boolean s;
    private boolean t;
    private long u;
    private long v;
    private Address w;
    private String x;
    private List<Poi> y;
    private double z;

    class a implements Runnable {
        final /* synthetic */ e a;

        private a(e eVar) {
            this.a = eVar;
        }

        public void run() {
            if (this.a.F) {
                this.a.F = false;
                if (!this.a.G) {
                    this.a.a(true);
                }
            }
        }
    }

    class b implements Runnable {
        final /* synthetic */ e a;

        private b(e eVar) {
            this.a = eVar;
        }

        public void run() {
            if (this.a.K) {
                this.a.K = false;
            }
            if (this.a.t) {
                this.a.t = false;
                this.a.g(null);
            }
        }
    }

    private e() {
        this.e = 1000;
        this.j = true;
        this.f = null;
        this.k = null;
        this.l = null;
        this.m = null;
        this.n = null;
        this.o = null;
        this.p = null;
        this.q = null;
        this.r = true;
        this.s = false;
        this.t = false;
        this.u = 0;
        this.v = 0;
        this.w = null;
        this.x = null;
        this.y = null;
        this.B = false;
        this.C = 0;
        this.D = 0;
        this.E = null;
        this.F = false;
        this.G = false;
        this.H = true;
        this.g = new com.baidu.location.a.d.a(this);
        this.I = false;
        this.J = null;
        this.K = false;
        this.f = new b(this);
    }

    private boolean a(com.baidu.location.f.a aVar) {
        this.b = c.a().f();
        return this.b == aVar ? false : this.b == null || aVar == null || !aVar.a(this.b);
    }

    private boolean a(i iVar) {
        this.a = k.a().k();
        return iVar == this.a ? false : this.a == null || iVar == null || !iVar.c(this.a);
    }

    public static synchronized e b() {
        e eVar;
        synchronized (e.class) {
            if (i == null) {
                i = new e();
            }
            eVar = i;
        }
        return eVar;
    }

    private boolean b(com.baidu.location.f.a aVar) {
        return aVar == null ? false : this.q == null || !aVar.a(this.q);
    }

    private void c(Message message) {
        int d;
        boolean z = message.getData().getBoolean("isWaitingLocTag", false);
        if (z) {
            h = true;
        }
        if (z) {
            d = a.a().d(message);
            f.a().d();
        } else {
            d = a.a().d(message);
            f.a().d();
        }
        switch (d) {
            case 1:
                d(message);
                return;
            case 2:
                f(message);
                return;
            case 3:
                if (f.a().i()) {
                    e(message);
                    return;
                }
                return;
            default:
                throw new IllegalArgumentException(String.format("this type %d is illegal", new Object[]{Integer.valueOf(d)}));
        }
    }

    private void d(Message message) {
        if (f.a().i()) {
            e(message);
            f.a().c();
            return;
        }
        f(message);
        f.a().b();
    }

    private void e(Message message) {
        BDLocation bDLocation = new BDLocation(f.a().f());
        if (com.baidu.location.h.i.f.equals(SpeechConstant.PLUS_LOCAL_ALL) || com.baidu.location.h.i.j || com.baidu.location.h.i.k) {
            float[] fArr = new float[2];
            Location.distanceBetween(this.A, this.z, bDLocation.getLatitude(), bDLocation.getLongitude(), fArr);
            if (fArr[0] < 100.0f) {
                if (this.w != null) {
                    bDLocation.setAddr(this.w);
                }
                if (this.x != null) {
                    bDLocation.setLocationDescribe(this.x);
                }
                if (this.y != null) {
                    bDLocation.setPoiList(this.y);
                }
            } else {
                this.B = true;
                f(null);
            }
        }
        this.l = bDLocation;
        this.m = null;
        a.a().a(bDLocation);
    }

    private void f(Message message) {
        if (this.r) {
            this.D = SystemClock.uptimeMillis();
            g(message);
        } else if (!this.s) {
            this.D = SystemClock.uptimeMillis();
            if (k.a().e()) {
                this.t = true;
                if (this.J == null) {
                    this.J = new b();
                }
                if (this.K && this.J != null) {
                    this.g.removeCallbacks(this.J);
                }
                this.g.postDelayed(this.J, 3500);
                this.K = true;
                return;
            }
            g(message);
        }
    }

    private void g(Message message) {
        if (!this.s) {
            if (System.currentTimeMillis() - this.u <= 0 || System.currentTimeMillis() - this.u >= 1000 || this.l == null) {
                if (this.D > 0) {
                    com.baidu.location.c.f.a().b().a(this.D);
                } else {
                    com.baidu.location.c.f.a().b().a(SystemClock.uptimeMillis());
                }
                this.s = true;
                this.j = a(this.o);
                if (a(this.n) || this.j || this.l == null || this.B) {
                    this.u = System.currentTimeMillis();
                    String a = a(null);
                    if (a != null) {
                        if (this.k != null) {
                            a = a + this.k;
                            this.k = null;
                        }
                        com.baidu.location.c.f.a().b().b(SystemClock.uptimeMillis());
                        this.f.a(a);
                        this.o = this.b;
                        this.n = this.a;
                        if (j()) {
                            this.o = this.b;
                            this.n = this.a;
                        }
                        if (d.a().h()) {
                            if (this.E == null) {
                                this.E = new a();
                            }
                            this.g.postDelayed(this.E, d.a().a(c.a(c.a().e())));
                            this.F = true;
                        }
                        if (this.r) {
                            this.r = false;
                            if (k.a().h() && message != null && a.a().e(message) < 1000 && d.a().d()) {
                                d.a().i();
                            }
                            com.baidu.location.c.a.a().b();
                            return;
                        }
                        return;
                    } else if (this.l != null) {
                        a.a().a(this.l);
                        l();
                        return;
                    } else {
                        String[] k = k();
                        BDLocation bDLocation = new BDLocation();
                        bDLocation.setLocType(62);
                        bDLocation.setLocTypeDescription(k[1]);
                        a.a().a(bDLocation);
                        l();
                        long currentTimeMillis = System.currentTimeMillis();
                        if (currentTimeMillis - this.C > DateTimeUtils.ONE_MINUTE) {
                            this.C = currentTimeMillis;
                            com.baidu.location.c.f.a().a("Criteria" + k[0]);
                            return;
                        }
                        return;
                    }
                }
                if (this.m != null && System.currentTimeMillis() - this.v > 30000) {
                    this.l = this.m;
                    this.m = null;
                }
                if (f.a().f()) {
                    this.l.setDirection(f.a().h());
                }
                a.a().a(this.l);
                l();
                return;
            }
            a.a().a(this.l);
            l();
        }
    }

    private boolean j() {
        BDLocation bDLocation = null;
        double random = Math.random();
        long uptimeMillis = SystemClock.uptimeMillis();
        com.baidu.location.f.a f = c.a().f();
        i j = k.a().j();
        boolean z = f != null && f.e() && (j == null || j.a() == 0);
        if (d.a().d() && d.a().f() && (z || (0.0d < random && random < d.a().o()))) {
            bDLocation = d.a().a(c.a().f(), k.a().j(), null, com.baidu.location.e.d.b.IS_MIX_MODE, com.baidu.location.e.d.a.NEED_TO_LOG);
        }
        if (bDLocation == null || bDLocation.getLocType() != 66 || !this.s) {
            return false;
        }
        BDLocation bDLocation2 = new BDLocation(bDLocation);
        bDLocation2.setLocType(BDLocation.TypeNetWorkLocation);
        if (!this.s) {
            return false;
        }
        g gVar = new g();
        gVar.a(this.D);
        gVar.b(uptimeMillis);
        gVar.c(uptimeMillis);
        gVar.d(SystemClock.uptimeMillis());
        gVar.a("ofs");
        if (this.o != null) {
            gVar.b(this.o.h());
            gVar.b("&offtag=1");
        }
        com.baidu.location.c.f.a().a(gVar);
        this.G = true;
        a.a().a(bDLocation2);
        this.l = bDLocation2;
        return true;
    }

    private String[] k() {
        String[] strArr = new String[]{"", "Location failed beacuse we can not get any loc information!"};
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("&apl=");
        int b = com.baidu.location.h.i.b(com.baidu.location.f.getServiceContext());
        if (b == 1) {
            strArr[1] = "Location failed beacuse we can not get any loc information in airplane mode, you can turn it off and try again!!";
        }
        stringBuffer.append(b);
        String d = com.baidu.location.h.i.d(com.baidu.location.f.getServiceContext());
        if (d.contains("0|0|")) {
            strArr[1] = "Location failed beacuse we can not get any loc information without any location permission!";
        }
        stringBuffer.append(d);
        if (VERSION.SDK_INT >= 23) {
            stringBuffer.append("&loc=");
            if (com.baidu.location.h.i.c(com.baidu.location.f.getServiceContext()) == 0) {
                strArr[1] = "Location failed beacuse we can not get any loc information with the phone loc mode is off, you can turn it on and try again!";
            }
            stringBuffer.append(com.baidu.location.h.i.c(com.baidu.location.f.getServiceContext()));
        }
        stringBuffer.append(j.a().f());
        stringBuffer.append(com.baidu.location.f.b.a().g());
        stringBuffer.append(com.baidu.location.h.i.e(com.baidu.location.f.getServiceContext()));
        strArr[0] = stringBuffer.toString();
        return strArr;
    }

    private void l() {
        this.s = false;
        this.G = false;
        this.H = false;
        this.B = false;
        m();
    }

    private void m() {
        if (this.l != null) {
            j.a().c();
        }
    }

    public Address a(BDLocation bDLocation) {
        if (com.baidu.location.h.i.f.equals(SpeechConstant.PLUS_LOCAL_ALL) || com.baidu.location.h.i.j || com.baidu.location.h.i.k) {
            float[] fArr = new float[2];
            Location.distanceBetween(this.A, this.z, bDLocation.getLatitude(), bDLocation.getLongitude(), fArr);
            if (fArr[0] >= 100.0f) {
                this.x = null;
                this.y = null;
                this.B = true;
                f(null);
            } else if (this.w != null) {
                return this.w;
            }
        }
        return null;
    }

    public void a() {
        boolean z;
        boolean z2 = true;
        if (this.E == null || !this.F) {
            z = false;
        } else {
            this.F = false;
            this.g.removeCallbacks(this.E);
            z = true;
        }
        if (f.a().i()) {
            BDLocation bDLocation = new BDLocation(f.a().f());
            if (com.baidu.location.h.i.f.equals(SpeechConstant.PLUS_LOCAL_ALL) || com.baidu.location.h.i.j || com.baidu.location.h.i.k) {
                float[] fArr = new float[2];
                Location.distanceBetween(this.A, this.z, bDLocation.getLatitude(), bDLocation.getLongitude(), fArr);
                if (fArr[0] < 100.0f) {
                    if (this.w != null) {
                        bDLocation.setAddr(this.w);
                    }
                    if (this.x != null) {
                        bDLocation.setLocationDescribe(this.x);
                    }
                    if (this.y != null) {
                        bDLocation.setPoiList(this.y);
                    }
                }
            }
            a.a().a(bDLocation);
            l();
        } else if (this.G) {
            l();
        } else {
            com.baidu.location.c.f.a().b().c(SystemClock.uptimeMillis());
            BDLocation a;
            if (z) {
                if (d.a().d() && d.a().e()) {
                    a = d.a().a(c.a().f(), k.a().j(), null, com.baidu.location.e.d.b.IS_NOT_MIX_MODE, com.baidu.location.e.d.a.NEED_TO_LOG);
                    if (a != null && a.getLocType() == 66) {
                        a.a().a(a);
                    }
                } else {
                    a = null;
                }
                if (a == null || a.getLocType() == 67) {
                    if (this.j || this.l == null) {
                        a = com.baidu.location.e.a.a().a(false);
                        a.a().a(a);
                        if (com.baidu.location.h.i.f.equals(SpeechConstant.PLUS_LOCAL_ALL) && a.getAddrStr() == null) {
                            z2 = false;
                        }
                        if (com.baidu.location.h.i.j && a.getLocationDescribe() == null) {
                            z2 = false;
                        }
                        if (com.baidu.location.h.i.k && a.getPoiList() == null) {
                            z2 = false;
                        }
                        if (!z2) {
                            a.setLocType(67);
                        }
                    } else {
                        a.a().a(this.l);
                    }
                }
                com.baidu.location.c.f.a().b().d(SystemClock.uptimeMillis());
                if (a == null || a.getLocType() == 67) {
                    this.l = null;
                    com.baidu.location.c.f.a().b().a("off");
                    if (this.o != null) {
                        com.baidu.location.c.f.a().b().b(this.o.h());
                    }
                    com.baidu.location.c.f.a().c();
                } else {
                    this.l = a;
                    com.baidu.location.c.f.a().b().a("ofs");
                    if (this.o != null) {
                        com.baidu.location.c.f.a().b().b(this.o.h());
                    }
                    com.baidu.location.c.f.a().c();
                }
            } else {
                a = new BDLocation();
                a.setLocType(63);
                this.l = null;
                a.a().a(a);
            }
            this.m = null;
            l();
        }
    }

    public void a(Message message) {
        if (this.E != null && this.F) {
            this.F = false;
            this.g.removeCallbacks(this.E);
        }
        BDLocation bDLocation = (BDLocation) message.obj;
        BDLocation bDLocation2 = new BDLocation(bDLocation);
        if (bDLocation.hasAddr()) {
            this.w = bDLocation.getAddress();
            this.z = bDLocation.getLongitude();
            this.A = bDLocation.getLatitude();
        }
        if (bDLocation.getLocationDescribe() != null) {
            this.x = bDLocation.getLocationDescribe();
            this.z = bDLocation.getLongitude();
            this.A = bDLocation.getLatitude();
        }
        if (bDLocation.getPoiList() != null) {
            this.y = bDLocation.getPoiList();
            this.z = bDLocation.getLongitude();
            this.A = bDLocation.getLatitude();
        }
        float[] fArr;
        if (f.a().i()) {
            bDLocation = new BDLocation(f.a().f());
            if (com.baidu.location.h.i.f.equals(SpeechConstant.PLUS_LOCAL_ALL) || com.baidu.location.h.i.j || com.baidu.location.h.i.k) {
                fArr = new float[2];
                Location.distanceBetween(this.A, this.z, bDLocation.getLatitude(), bDLocation.getLongitude(), fArr);
                if (fArr[0] < 100.0f) {
                    if (this.w != null) {
                        bDLocation.setAddr(this.w);
                    }
                    if (this.x != null) {
                        bDLocation.setLocationDescribe(this.x);
                    }
                    if (this.y != null) {
                        bDLocation.setPoiList(this.y);
                    }
                }
            }
            a.a().a(bDLocation);
            l();
        } else if (this.G) {
            fArr = new float[2];
            if (this.l != null) {
                Location.distanceBetween(this.l.getLatitude(), this.l.getLongitude(), bDLocation.getLatitude(), bDLocation.getLongitude(), fArr);
            }
            if (fArr[0] > 10.0f) {
                this.l = bDLocation;
                if (!this.H) {
                    this.H = false;
                    a.a().a(bDLocation);
                }
            } else if (bDLocation.getUserIndoorState() > -1) {
                this.l = bDLocation;
                a.a().a(bDLocation);
            }
            l();
        } else {
            boolean z;
            com.baidu.location.c.f.a().b().c(SystemClock.uptimeMillis());
            this.m = null;
            if (bDLocation.getLocType() == BDLocation.TypeNetWorkLocation && "cl".equals(bDLocation.getNetworkLocationType()) && this.l != null && this.l.getLocType() == BDLocation.TypeNetWorkLocation && "wf".equals(this.l.getNetworkLocationType()) && System.currentTimeMillis() - this.v < 30000) {
                z = true;
                this.m = bDLocation;
            } else {
                z = false;
            }
            if (z) {
                a.a().a(this.l);
            } else {
                a.a().a(bDLocation);
                this.v = System.currentTimeMillis();
                com.baidu.location.c.f.a().b().d(SystemClock.uptimeMillis());
                if (bDLocation.getLocType() == BDLocation.TypeNetWorkLocation) {
                    com.baidu.location.c.f.a().b().a("ons");
                    if (this.o != null) {
                        com.baidu.location.c.f.a().b().b(this.o.h());
                    }
                } else {
                    com.baidu.location.c.f.a().b().a("onf");
                    if (this.o != null) {
                        com.baidu.location.c.f.a().b().b(this.o.h());
                    }
                    com.baidu.location.c.f.a().c();
                }
            }
            if (!com.baidu.location.h.i.a(bDLocation)) {
                this.l = null;
            } else if (!z) {
                this.l = bDLocation;
            }
            int a = com.baidu.location.h.i.a(c, "ssid\":\"", "\"");
            if (a == Integer.MIN_VALUE || this.n == null) {
                this.k = null;
            } else {
                this.k = this.n.c(a);
            }
            if (d.a().d() && bDLocation.getLocType() == BDLocation.TypeNetWorkLocation && "cl".equals(bDLocation.getNetworkLocationType()) && b(this.o)) {
                d.a().a(this.o, null, bDLocation2, com.baidu.location.e.d.b.IS_NOT_MIX_MODE, com.baidu.location.e.d.a.NO_NEED_TO_LOG);
                this.q = this.o;
            }
            if (d.a().d() && bDLocation.getLocType() == BDLocation.TypeNetWorkLocation && "wf".equals(bDLocation.getNetworkLocationType())) {
                d.a().a(null, this.n, bDLocation2, com.baidu.location.e.d.b.IS_NOT_MIX_MODE, com.baidu.location.e.d.a.NO_NEED_TO_LOG);
                this.p = this.n;
            }
            if (this.o != null) {
                com.baidu.location.e.a.a().a(c, this.o, this.n, bDLocation2);
            }
            if (k.a().h()) {
                d.a().i();
                d.a().m();
            }
            l();
        }
    }

    public void a(boolean z) {
        BDLocation bDLocation = null;
        if (d.a().d() && d.a().g()) {
            bDLocation = d.a().a(c.a().f(), k.a().j(), null, com.baidu.location.e.d.b.IS_NOT_MIX_MODE, com.baidu.location.e.d.a.NEED_TO_LOG);
            if ((bDLocation == null || bDLocation.getLocType() == 67) && z) {
                bDLocation = com.baidu.location.e.a.a().a(false);
            }
        } else if (z) {
            bDLocation = com.baidu.location.e.a.a().a(false);
        }
        if (bDLocation != null && bDLocation.getLocType() == 66) {
            boolean z2 = true;
            if (com.baidu.location.h.i.f.equals(SpeechConstant.PLUS_LOCAL_ALL) && bDLocation.getAddrStr() == null) {
                z2 = false;
            }
            if (com.baidu.location.h.i.j && bDLocation.getLocationDescribe() == null) {
                z2 = false;
            }
            if (com.baidu.location.h.i.k && bDLocation.getPoiList() == null) {
                z2 = false;
            }
            if (z2) {
                a.a().a(bDLocation);
            }
        }
    }

    public void b(Message message) {
        if (this.I) {
            c(message);
        }
    }

    public void b(BDLocation bDLocation) {
        this.l = new BDLocation(bDLocation);
    }

    public void c() {
        this.r = true;
        this.s = false;
        this.I = true;
    }

    public void d() {
        this.s = false;
        this.t = false;
        this.G = false;
        this.H = true;
        i();
        this.I = false;
    }

    public String e() {
        return this.x;
    }

    public List<Poi> f() {
        return this.y;
    }

    public boolean g() {
        return this.j;
    }

    public void h() {
        if (this.t) {
            g(null);
            this.t = false;
            return;
        }
        com.baidu.location.c.a.a().d();
    }

    public void i() {
        this.l = null;
    }
}
