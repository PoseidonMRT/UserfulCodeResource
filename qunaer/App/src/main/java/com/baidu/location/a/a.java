package com.baidu.location.a;

import android.location.Location;
import android.os.Bundle;
import android.os.DeadObjectException;
import android.os.Message;
import android.os.Messenger;
import com.baidu.location.Address;
import com.baidu.location.BDLocation;
import com.baidu.location.Jni;
import com.baidu.location.LocationClientOption;
import com.baidu.location.c.h;
import com.baidu.location.f.f;
import com.baidu.location.h.c;
import com.baidu.location.h.i;
import com.baidu.platform.comapi.location.CoordinateType;
import com.iflytek.cloud.SpeechConstant;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.apache.commons.logging.LogFactory;

public class a {
    private static a b = null;
    public boolean a;
    private ArrayList<a> c;
    private boolean d;
    private BDLocation e;

    class a {
        public String a = null;
        public Messenger b = null;
        public LocationClientOption c = new LocationClientOption();
        public int d = 0;
        final /* synthetic */ a e;

        public a(a aVar, Message message) {
            boolean z = true;
            this.e = aVar;
            this.b = message.replyTo;
            this.a = message.getData().getString("packName");
            this.c.prodName = message.getData().getString("prodName");
            c.a().a(this.c.prodName, this.a);
            this.c.coorType = message.getData().getString("coorType");
            this.c.addrType = message.getData().getString("addrType");
            this.c.enableSimulateGps = message.getData().getBoolean("enableSimulateGps", false);
            boolean z2 = i.n || this.c.enableSimulateGps;
            i.n = z2;
            if (!i.f.equals(SpeechConstant.PLUS_LOCAL_ALL)) {
                i.f = this.c.addrType;
            }
            i.g = message.getData().getString("host", "loc.map.baidu.com");
            i.h = message.getData().getString("proxyhost", "");
            i.i = message.getData().getInt("proxyport", 0);
            this.c.openGps = message.getData().getBoolean("openGPS");
            this.c.scanSpan = message.getData().getInt("scanSpan");
            this.c.timeOut = message.getData().getInt("timeOut");
            this.c.priority = message.getData().getInt(LogFactory.PRIORITY_KEY);
            this.c.location_change_notify = message.getData().getBoolean("location_change_notify");
            this.c.mIsNeedDeviceDirect = message.getData().getBoolean("needDirect", false);
            this.c.isNeedAltitude = message.getData().getBoolean("isneedaltitude", false);
            z2 = i.j || message.getData().getBoolean("isneedaptag", false);
            i.j = z2;
            if (!(i.k || message.getData().getBoolean("isneedaptagd", false))) {
                z = false;
            }
            i.k = z;
            i.R = message.getData().getFloat("autoNotifyLocSensitivity", 0.5f);
            int i = message.getData().getInt("autoNotifyMaxInterval", 0);
            if (i >= i.W) {
                i.W = i;
            }
            i = message.getData().getInt("autoNotifyMinDistance", 0);
            if (i >= i.Y) {
                i.Y = i;
            }
            i = message.getData().getInt("autoNotifyMinTimeInterval", 0);
            if (i >= i.X) {
                i.X = i;
            }
            if (this.c.scanSpan >= 1000) {
                h.a().b();
            }
            if (this.c.mIsNeedDeviceDirect || this.c.isNeedAltitude) {
                f.a().a(this.c.mIsNeedDeviceDirect);
                f.a().b(this.c.isNeedAltitude);
                f.a().b();
            }
        }

        private void a(int i) {
            Message obtain = Message.obtain(null, i);
            try {
                if (this.b != null) {
                    this.b.send(obtain);
                }
                this.d = 0;
            } catch (Exception e) {
                if (e instanceof DeadObjectException) {
                    this.d++;
                }
            }
        }

        private void a(int i, String str, BDLocation bDLocation) {
            Bundle bundle = new Bundle();
            bundle.putParcelable(str, bDLocation);
            bundle.setClassLoader(BDLocation.class.getClassLoader());
            Message obtain = Message.obtain(null, i);
            obtain.setData(bundle);
            try {
                if (this.b != null) {
                    this.b.send(obtain);
                }
                this.d = 0;
            } catch (Exception e) {
                if (e instanceof DeadObjectException) {
                    this.d++;
                }
            }
        }

        public void a() {
            if (!this.c.location_change_notify) {
                return;
            }
            if (i.b) {
                a(54);
            } else {
                a(55);
            }
        }

        public void a(BDLocation bDLocation) {
            a(bDLocation, 21);
        }

        public void a(BDLocation bDLocation, int i) {
            BDLocation bDLocation2 = new BDLocation(bDLocation);
            if (f.a().g() && (bDLocation2.getLocType() == BDLocation.TypeNetWorkLocation || bDLocation2.getLocType() == 66)) {
                bDLocation2.setAltitude(f.a().i());
            }
            if (i == 21) {
                a(27, "locStr", bDLocation2);
            }
            if (!(this.c.coorType == null || this.c.coorType.equals(CoordinateType.GCJ02))) {
                double longitude = bDLocation2.getLongitude();
                double latitude = bDLocation2.getLatitude();
                if (!(longitude == Double.MIN_VALUE || latitude == Double.MIN_VALUE)) {
                    double[] coorEncrypt;
                    if ((bDLocation2.getCoorType() != null && bDLocation2.getCoorType().equals(CoordinateType.GCJ02)) || bDLocation2.getCoorType() == null) {
                        coorEncrypt = Jni.coorEncrypt(longitude, latitude, this.c.coorType);
                        bDLocation2.setLongitude(coorEncrypt[0]);
                        bDLocation2.setLatitude(coorEncrypt[1]);
                        bDLocation2.setCoorType(this.c.coorType);
                    } else if (!(bDLocation2.getCoorType() == null || !bDLocation2.getCoorType().equals(CoordinateType.WGS84) || this.c.coorType.equals("bd09ll"))) {
                        coorEncrypt = Jni.coorEncrypt(longitude, latitude, "wgs842mc");
                        bDLocation2.setLongitude(coorEncrypt[0]);
                        bDLocation2.setLatitude(coorEncrypt[1]);
                        bDLocation2.setCoorType("wgs84mc");
                    }
                }
            }
            a(i, "locStr", bDLocation2);
        }
    }

    private a() {
        this.c = null;
        this.d = false;
        this.a = false;
        this.e = null;
        this.c = new ArrayList();
    }

    private a a(Messenger messenger) {
        if (this.c == null) {
            return null;
        }
        Iterator it = this.c.iterator();
        while (it.hasNext()) {
            a aVar = (a) it.next();
            if (aVar.b.equals(messenger)) {
                return aVar;
            }
        }
        return null;
    }

    public static a a() {
        if (b == null) {
            b = new a();
        }
        return b;
    }

    private void a(a aVar) {
        if (aVar != null) {
            if (a(aVar.b) != null) {
                aVar.a(14);
                return;
            }
            this.c.add(aVar);
            aVar.a(13);
        }
    }

    private void e() {
        f();
        d();
    }

    private void f() {
        Iterator it = this.c.iterator();
        boolean z = false;
        boolean z2 = false;
        while (it.hasNext()) {
            a aVar = (a) it.next();
            if (aVar.c.openGps) {
                z2 = true;
            }
            z = aVar.c.location_change_notify ? true : z;
        }
        i.a = z;
        if (this.d != z2) {
            this.d = z2;
            f.a().a(this.d);
        }
    }

    public void a(Message message) {
        if (message != null && message.replyTo != null) {
            this.a = true;
            a(new a(this, message));
            e();
        }
    }

    public void a(BDLocation bDLocation) {
        boolean z = e.h;
        if (z) {
            e.h = false;
        }
        if (i.W >= LocationClientOption.MIN_AUTO_NOTIFY_INTERVAL && (bDLocation.getLocType() == 61 || bDLocation.getLocType() == BDLocation.TypeNetWorkLocation || bDLocation.getLocType() == 66)) {
            if (this.e != null) {
                float[] fArr = new float[1];
                Location.distanceBetween(this.e.getLatitude(), this.e.getLongitude(), bDLocation.getLatitude(), bDLocation.getLongitude(), fArr);
                if (fArr[0] > ((float) i.Y) || z) {
                    this.e = null;
                    this.e = new BDLocation(bDLocation);
                } else {
                    return;
                }
            }
            this.e = new BDLocation(bDLocation);
        }
        Iterator it = this.c.iterator();
        while (it.hasNext()) {
            try {
                a aVar = (a) it.next();
                aVar.a(bDLocation);
                if (aVar.d > 4) {
                    it.remove();
                }
            } catch (Exception e) {
                return;
            }
        }
    }

    public void a(String str) {
        BDLocation bDLocation = new BDLocation(str);
        Address a = e.b().a(bDLocation);
        String e = e.b().e();
        List f = e.b().f();
        if (a != null) {
            bDLocation.setAddr(a);
        }
        if (e != null) {
            bDLocation.setLocationDescribe(e);
        }
        if (f != null) {
            bDLocation.setPoiList(f);
        }
        e.b().b(bDLocation);
        a(bDLocation);
    }

    public void b() {
        this.c.clear();
        this.e = null;
        e();
    }

    public void b(Message message) {
        a a = a(message.replyTo);
        if (a != null) {
            this.c.remove(a);
        }
        h.a().c();
        f.a().c();
        e();
    }

    public String c() {
        StringBuffer stringBuffer = new StringBuffer(256);
        if (this.c.isEmpty()) {
            return "&prod=" + c.e + ":" + c.d;
        }
        a aVar = (a) this.c.get(0);
        if (aVar.c.prodName != null) {
            stringBuffer.append(aVar.c.prodName);
        }
        if (aVar.a != null) {
            stringBuffer.append(":");
            stringBuffer.append(aVar.a);
            stringBuffer.append("|");
        }
        String stringBuffer2 = stringBuffer.toString();
        return (stringBuffer2 == null || stringBuffer2.equals("")) ? null : "&prod=" + stringBuffer2;
    }

    public boolean c(Message message) {
        boolean z = true;
        a a = a(message.replyTo);
        if (a == null) {
            return false;
        }
        int i = a.c.scanSpan;
        a.c.scanSpan = message.getData().getInt("scanSpan", a.c.scanSpan);
        if (a.c.scanSpan < 1000) {
            h.a().e();
            f.a().c();
            this.a = false;
        } else {
            h.a().d();
            this.a = true;
        }
        if (a.c.scanSpan <= 999 || i >= 1000) {
            z = false;
        } else if (a.c.mIsNeedDeviceDirect || a.c.isNeedAltitude) {
            f.a().a(a.c.mIsNeedDeviceDirect);
            f.a().b(a.c.isNeedAltitude);
            f.a().b();
        }
        a.c.openGps = message.getData().getBoolean("openGPS", a.c.openGps);
        String string = message.getData().getString("coorType");
        LocationClientOption locationClientOption = a.c;
        if (string == null || string.equals("")) {
            string = a.c.coorType;
        }
        locationClientOption.coorType = string;
        string = message.getData().getString("addrType");
        locationClientOption = a.c;
        if (string == null || string.equals("")) {
            string = a.c.addrType;
        }
        locationClientOption.addrType = string;
        if (!i.f.equals(a.c.addrType)) {
            e.b().i();
        }
        a.c.timeOut = message.getData().getInt("timeOut", a.c.timeOut);
        a.c.location_change_notify = message.getData().getBoolean("location_change_notify", a.c.location_change_notify);
        a.c.priority = message.getData().getInt(LogFactory.PRIORITY_KEY, a.c.priority);
        e();
        return z;
    }

    public int d(Message message) {
        if (message == null || message.replyTo == null) {
            return 1;
        }
        a a = a(message.replyTo);
        return (a == null || a.c == null) ? 1 : a.c.priority;
    }

    public void d() {
        Iterator it = this.c.iterator();
        while (it.hasNext()) {
            ((a) it.next()).a();
        }
    }

    public int e(Message message) {
        if (message == null || message.replyTo == null) {
            return 1000;
        }
        a a = a(message.replyTo);
        return (a == null || a.c == null) ? 1000 : a.c.scanSpan;
    }
}
