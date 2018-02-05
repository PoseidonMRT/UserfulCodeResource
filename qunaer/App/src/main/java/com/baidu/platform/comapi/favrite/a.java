package com.baidu.platform.comapi.favrite;

import android.os.Bundle;
import android.text.TextUtils;
import com.baidu.mapapi.common.SysOSUtil;
import com.baidu.mapapi.model.inner.Point;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Vector;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class a {
    private static a b = null;
    private com.baidu.platform.comjni.map.favorite.a a = null;
    private boolean c = false;
    private boolean d = false;
    private Vector<String> e = null;
    private Vector<String> f = null;
    private boolean g = false;
    private c h = new c();
    private b i = new b();

    class a implements Comparator<String> {
        final /* synthetic */ a a;

        a(a aVar) {
            this.a = aVar;
        }

        public int a(String str, String str2) {
            return str2.compareTo(str);
        }

        public /* synthetic */ int compare(Object obj, Object obj2) {
            return a((String) obj, (String) obj2);
        }
    }

    class b {
        final /* synthetic */ a a;
        private long b;
        private long c;

        private b(a aVar) {
            this.a = aVar;
        }

        private void a() {
            this.b = System.currentTimeMillis();
        }

        private void b() {
            this.c = System.currentTimeMillis();
        }

        private boolean c() {
            return this.c - this.b > 1000;
        }
    }

    class c {
        final /* synthetic */ a a;
        private String b;
        private long c;
        private long d;

        private c(a aVar) {
            this.a = aVar;
            this.c = 5000;
            this.d = 0;
        }

        private String a() {
            return this.b;
        }

        private void a(String str) {
            this.b = str;
            this.d = System.currentTimeMillis();
        }

        private boolean b() {
            return TextUtils.isEmpty(this.b);
        }

        private boolean c() {
            return true;
        }
    }

    private a() {
    }

    public static a a() {
        if (b == null) {
            synchronized (a.class) {
                if (b == null) {
                    b = new a();
                    b.h();
                }
            }
        }
        return b;
    }

    public static boolean g() {
        return (b == null || b.a == null || !b.a.d()) ? false : true;
    }

    private boolean h() {
        if (this.a != null) {
            return true;
        }
        this.a = new com.baidu.platform.comjni.map.favorite.a();
        if (this.a.a() == 0) {
            this.a = null;
            return false;
        }
        j();
        i();
        return true;
    }

    private boolean i() {
        if (this.a == null) {
            return false;
        }
        String str = "fav_poi";
        this.a.a(1);
        return this.a.a(SysOSUtil.getModuleFileName() + "/", str, "fifo", 10, 501, -1);
    }

    private void j() {
        this.c = false;
        this.d = false;
    }

    public synchronized int a(String str, FavSyncPoi favSyncPoi) {
        int i;
        if (this.a == null) {
            i = 0;
        } else if (str == null || str.equals("") || favSyncPoi == null) {
            i = -1;
        } else {
            j();
            ArrayList e = e();
            if ((e != null ? e.size() : 0) + 1 > 500) {
                i = -2;
            } else {
                if (e != null && e.size() > 0) {
                    Iterator it = e.iterator();
                    while (it.hasNext()) {
                        FavSyncPoi b = b((String) it.next());
                        if (b != null && str.equals(b.b)) {
                            i = -1;
                            break;
                        }
                    }
                }
                String str2 = "";
                try {
                    JSONObject jSONObject = new JSONObject();
                    favSyncPoi.b = str;
                    String valueOf = String.valueOf(System.currentTimeMillis());
                    String str3 = valueOf + "_" + favSyncPoi.hashCode();
                    favSyncPoi.h = valueOf;
                    favSyncPoi.a = str3;
                    jSONObject.put("bdetail", favSyncPoi.i);
                    jSONObject.put("uspoiname", favSyncPoi.b);
                    JSONObject jSONObject2 = new JSONObject();
                    jSONObject2.put(MapViewConstants.ATTR_X, favSyncPoi.c.getmPtx());
                    jSONObject2.put(MapViewConstants.ATTR_Y, favSyncPoi.c.getmPty());
                    jSONObject.put("pt", jSONObject2);
                    jSONObject.put("ncityid", favSyncPoi.e);
                    jSONObject.put("npoitype", favSyncPoi.g);
                    jSONObject.put("uspoiuid", favSyncPoi.f);
                    jSONObject.put("addr", favSyncPoi.d);
                    jSONObject.put("addtimesec", favSyncPoi.h);
                    jSONObject2 = new JSONObject();
                    jSONObject2.put("Fav_Sync", jSONObject);
                    jSONObject2.put("Fav_Content", favSyncPoi.j);
                    if (this.a.a(str3, jSONObject2.toString())) {
                        j();
                        i = 1;
                    } else {
                        g();
                        i = 0;
                    }
                } catch (JSONException e2) {
                    i = e2;
                    i = 0;
                    return i;
                } finally {
                    g();
                }
            }
        }
        return i;
    }

    public synchronized boolean a(String str) {
        boolean z = false;
        synchronized (this) {
            if (!(this.a == null || str == null)) {
                if (!str.equals("") && c(str)) {
                    j();
                    z = this.a.a(str);
                }
            }
        }
        return z;
    }

    public FavSyncPoi b(String str) {
        if (this.a == null || str == null || str.equals("")) {
            return null;
        }
        try {
            if (!c(str)) {
                return null;
            }
            FavSyncPoi favSyncPoi = new FavSyncPoi();
            String b = this.a.b(str);
            if (b == null || b.equals("")) {
                return null;
            }
            JSONObject jSONObject = new JSONObject(b);
            JSONObject optJSONObject = jSONObject.optJSONObject("Fav_Sync");
            String optString = jSONObject.optString("Fav_Content");
            favSyncPoi.b = optJSONObject.optString("uspoiname");
            JSONObject optJSONObject2 = optJSONObject.optJSONObject("pt");
            favSyncPoi.c = new Point(optJSONObject2.optInt(MapViewConstants.ATTR_X), optJSONObject2.optInt(MapViewConstants.ATTR_Y));
            favSyncPoi.e = optJSONObject.optString("ncityid");
            favSyncPoi.f = optJSONObject.optString("uspoiuid");
            favSyncPoi.g = optJSONObject.optInt("npoitype");
            favSyncPoi.d = optJSONObject.optString("addr");
            favSyncPoi.h = optJSONObject.optString("addtimesec");
            favSyncPoi.i = optJSONObject.optBoolean("bdetail");
            favSyncPoi.j = optString;
            favSyncPoi.a = str;
            return favSyncPoi;
        } catch (NullPointerException e) {
            e.printStackTrace();
            return null;
        } catch (JSONException e2) {
            e2.printStackTrace();
            return null;
        }
    }

    public void b() {
        if (b != null) {
            if (b.a != null) {
                b.a.b();
                b.a = null;
            }
            b = null;
        }
    }

    public synchronized boolean b(String str, FavSyncPoi favSyncPoi) {
        boolean z = false;
        synchronized (this) {
            if (!(this.a == null || str == null || str.equals("") || favSyncPoi == null)) {
                if (c(str)) {
                    try {
                        JSONObject jSONObject = new JSONObject();
                        jSONObject.put("uspoiname", favSyncPoi.b);
                        JSONObject jSONObject2 = new JSONObject();
                        jSONObject2.put(MapViewConstants.ATTR_X, favSyncPoi.c.getmPtx());
                        jSONObject2.put(MapViewConstants.ATTR_Y, favSyncPoi.c.getmPty());
                        jSONObject.put("pt", jSONObject2);
                        jSONObject.put("ncityid", favSyncPoi.e);
                        jSONObject.put("npoitype", favSyncPoi.g);
                        jSONObject.put("uspoiuid", favSyncPoi.f);
                        jSONObject.put("addr", favSyncPoi.d);
                        favSyncPoi.h = String.valueOf(System.currentTimeMillis());
                        jSONObject.put("addtimesec", favSyncPoi.h);
                        jSONObject.put("bdetail", false);
                        jSONObject2 = new JSONObject();
                        jSONObject2.put("Fav_Sync", jSONObject);
                        jSONObject2.put("Fav_Content", favSyncPoi.j);
                        j();
                        if (this.a != null && this.a.b(str, jSONObject2.toString())) {
                            z = true;
                        }
                    } catch (JSONException e) {
                    }
                }
            }
        }
        return z;
    }

    public synchronized boolean c() {
        boolean z;
        if (this.a == null) {
            z = false;
        } else {
            j();
            z = this.a.c();
            g();
        }
        return z;
    }

    public boolean c(String str) {
        return (this.a == null || str == null || str.equals("") || !this.a.c(str)) ? false : true;
    }

    public ArrayList<String> d() {
        if (this.a == null) {
            return null;
        }
        if (this.d && this.f != null) {
            return new ArrayList(this.f);
        }
        try {
            Bundle bundle = new Bundle();
            this.a.a(bundle);
            String[] stringArray = bundle.getStringArray("rstString");
            if (stringArray != null) {
                if (this.f == null) {
                    this.f = new Vector();
                } else {
                    this.f.clear();
                }
                for (int i = 0; i < stringArray.length; i++) {
                    if (!stringArray[i].equals("data_version")) {
                        String b = this.a.b(stringArray[i]);
                        if (!(b == null || b.equals(""))) {
                            this.f.add(stringArray[i]);
                        }
                    }
                }
                if (this.f.size() > 0) {
                    try {
                        Collections.sort(this.f, new a(this));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    this.d = true;
                }
            } else if (this.f != null) {
                this.f.clear();
                this.f = null;
            }
            ArrayList<String> arrayList = (this.f == null || this.f.isEmpty()) ? null : new ArrayList(this.f);
            return arrayList;
        } catch (Exception e2) {
            return null;
        }
    }

    public ArrayList<String> e() {
        if (this.a == null) {
            return null;
        }
        if (this.c && this.e != null) {
            return new ArrayList(this.e);
        }
        try {
            Bundle bundle = new Bundle();
            this.a.a(bundle);
            String[] stringArray = bundle.getStringArray("rstString");
            if (stringArray != null) {
                if (this.e == null) {
                    this.e = new Vector();
                } else {
                    this.e.clear();
                }
                for (String str : stringArray) {
                    if (!str.equals("data_version")) {
                        this.e.add(str);
                    }
                }
                if (this.e.size() > 0) {
                    try {
                        Collections.sort(this.e, new a(this));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    this.c = true;
                }
            } else if (this.e != null) {
                this.e.clear();
                this.e = null;
            }
            return (this.e == null || this.e.size() == 0) ? null : new ArrayList(this.e);
        } catch (Exception e2) {
            return null;
        }
    }

    public String f() {
        if (this.i.c() && !this.h.c() && !this.h.b()) {
            return this.h.a();
        }
        this.i.a();
        if (this.a == null) {
            return null;
        }
        ArrayList d = d();
        JSONObject jSONObject = new JSONObject();
        if (d != null) {
            try {
                JSONArray jSONArray = new JSONArray();
                int i = 0;
                Iterator it = d.iterator();
                while (it.hasNext()) {
                    int i2;
                    String str = (String) it.next();
                    if (!(str == null || str.equals("data_version"))) {
                        String b = this.a.b(str);
                        if (!(b == null || b.equals(""))) {
                            JSONObject optJSONObject = new JSONObject(b).optJSONObject("Fav_Sync");
                            optJSONObject.put("key", str);
                            jSONArray.put(i, optJSONObject);
                            i2 = i + 1;
                            i = i2;
                        }
                    }
                    i2 = i;
                    i = i2;
                }
                if (i > 0) {
                    jSONObject.put("favcontents", jSONArray);
                    jSONObject.put("favpoinum", i);
                }
            } catch (JSONException e) {
                return null;
            }
        }
        this.i.b();
        this.h.a(jSONObject.toString());
        return this.h.a();
    }
}
