package com.baidu.platform.comapi.map;

import android.os.Handler;
import com.baidu.mapapi.BMapManager;
import com.baidu.mapapi.UIMsg.m_AppUI;
import com.baidu.mapapi.common.EnvironmentUtilities;
import com.baidu.mapapi.common.SysOSUtil;
import com.baidu.mapapi.model.inner.GeoPoint;
import com.baidu.platform.comjni.map.basemap.a;
import com.iflytek.aiui.AIUIConstant;
import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class u {
    private static final String a = u.class.getSimpleName();
    private static u c;
    private a b;
    private z d;
    private Handler e;

    private u() {
    }

    public static u a() {
        if (c == null) {
            c = new u();
            c.g();
        }
        return c;
    }

    private void g() {
        h();
        this.d = new z();
        this.e = new v(this);
        MessageCenter.registMessage(m_AppUI.V_WM_VDATAENGINE, this.e);
    }

    private void h() {
        EnvironmentUtilities.initAppDirectory(BMapManager.getContext());
        this.b = new a();
        this.b.a();
        String moduleFileName = SysOSUtil.getModuleFileName();
        String appSDCardPath = EnvironmentUtilities.getAppSDCardPath();
        String appCachePath = EnvironmentUtilities.getAppCachePath();
        String appSecondCachePath = EnvironmentUtilities.getAppSecondCachePath();
        int mapTmpStgMax = EnvironmentUtilities.getMapTmpStgMax();
        int domTmpStgMax = EnvironmentUtilities.getDomTmpStgMax();
        int itsTmpStgMax = EnvironmentUtilities.getItsTmpStgMax();
        String str = SysOSUtil.getDensityDpi() >= 180 ? "/h/" : "/l/";
        String str2 = moduleFileName + "/cfg";
        String str3 = appSDCardPath + "/vmp";
        moduleFileName = str2 + "/a/";
        String str4 = str2 + "/a/";
        String str5 = str2 + "/idrres/";
        this.b.a(moduleFileName, str3 + str, appCachePath + "/tmp/", appSecondCachePath + "/tmp/", str3 + str, str4, null, str5, SysOSUtil.getScreenSizeX(), SysOSUtil.getScreenSizeY(), SysOSUtil.getDensityDpi(), mapTmpStgMax, domTmpStgMax, itsTmpStgMax, 0);
        this.b.f();
    }

    public ArrayList<t> a(String str) {
        if (str.equals("") || this.b == null) {
            return null;
        }
        String str2 = "";
        String a = this.b.a(str);
        if (a == null || a.equals("")) {
            return null;
        }
        ArrayList<t> arrayList = new ArrayList();
        try {
            JSONObject jSONObject = new JSONObject(a);
            if (jSONObject == null || jSONObject.length() == 0) {
                return null;
            }
            JSONArray optJSONArray = jSONObject.optJSONArray("dataset");
            if (optJSONArray == null) {
                return null;
            }
            for (int i = 0; i < optJSONArray.length(); i++) {
                t tVar = new t();
                JSONObject jSONObject2 = optJSONArray.getJSONObject(i);
                tVar.a = jSONObject2.optInt("id");
                tVar.b = jSONObject2.optString(AIUIConstant.KEY_NAME);
                tVar.c = jSONObject2.optInt("mapsize");
                tVar.d = jSONObject2.optInt("cty");
                if (jSONObject2.has("child")) {
                    JSONArray optJSONArray2 = jSONObject2.optJSONArray("child");
                    ArrayList arrayList2 = new ArrayList();
                    for (int i2 = 0; i2 < optJSONArray2.length(); i2++) {
                        t tVar2 = new t();
                        JSONObject optJSONObject = optJSONArray2.optJSONObject(i2);
                        tVar2.a = optJSONObject.optInt("id");
                        tVar2.b = optJSONObject.optString(AIUIConstant.KEY_NAME);
                        tVar2.c = optJSONObject.optInt("mapsize");
                        tVar2.d = optJSONObject.optInt("cty");
                        arrayList2.add(tVar2);
                    }
                    tVar.a(arrayList2);
                }
                arrayList.add(tVar);
            }
            return arrayList;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void a(y yVar) {
        if (this.d != null) {
            this.d.a(yVar);
        }
    }

    public boolean a(int i) {
        return (this.b == null || i < 0) ? false : this.b.b(i);
    }

    public boolean a(boolean z, boolean z2) {
        return this.b == null ? false : this.b.a(z, z2);
    }

    public void b() {
        MessageCenter.unregistMessage(m_AppUI.V_WM_VDATAENGINE, this.e);
        this.b.b();
        c = null;
    }

    public void b(y yVar) {
        if (this.d != null) {
            this.d.b(yVar);
        }
    }

    public boolean b(int i) {
        return (this.b == null || i < 0) ? false : this.b.a(i, false, 0);
    }

    public ArrayList<t> c() {
        if (this.b == null) {
            return null;
        }
        String str = "";
        String o = this.b.o();
        ArrayList<t> arrayList = new ArrayList();
        try {
            JSONArray optJSONArray = new JSONObject(o).optJSONArray("dataset");
            for (int i = 0; i < optJSONArray.length(); i++) {
                t tVar = new t();
                JSONObject optJSONObject = optJSONArray.optJSONObject(i);
                tVar.a = optJSONObject.optInt("id");
                tVar.b = optJSONObject.optString(AIUIConstant.KEY_NAME);
                tVar.c = optJSONObject.optInt("mapsize");
                tVar.d = optJSONObject.optInt("cty");
                if (optJSONObject.has("child")) {
                    JSONArray optJSONArray2 = optJSONObject.optJSONArray("child");
                    ArrayList arrayList2 = new ArrayList();
                    for (int i2 = 0; i2 < optJSONArray2.length(); i2++) {
                        t tVar2 = new t();
                        JSONObject optJSONObject2 = optJSONArray2.optJSONObject(i2);
                        tVar2.a = optJSONObject2.optInt("id");
                        tVar2.b = optJSONObject2.optString(AIUIConstant.KEY_NAME);
                        tVar2.c = optJSONObject2.optInt("mapsize");
                        tVar2.d = optJSONObject2.optInt("cty");
                        arrayList2.add(tVar2);
                    }
                    tVar.a(arrayList2);
                }
                arrayList.add(tVar);
            }
            return arrayList;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean c(int i) {
        return (this.b == null || i < 0) ? false : this.b.b(i, false, 0);
    }

    public ArrayList<t> d() {
        String str = "";
        if (this.b == null) {
            return null;
        }
        String a = this.b.a("");
        ArrayList<t> arrayList = new ArrayList();
        try {
            JSONArray optJSONArray = new JSONObject(a).optJSONArray("dataset");
            for (int i = 0; i < optJSONArray.length(); i++) {
                t tVar = new t();
                JSONObject optJSONObject = optJSONArray.optJSONObject(i);
                tVar.a = optJSONObject.optInt("id");
                tVar.b = optJSONObject.optString(AIUIConstant.KEY_NAME);
                tVar.c = optJSONObject.optInt("mapsize");
                tVar.d = optJSONObject.optInt("cty");
                if (optJSONObject.has("child")) {
                    JSONArray optJSONArray2 = optJSONObject.optJSONArray("child");
                    ArrayList arrayList2 = new ArrayList();
                    for (int i2 = 0; i2 < optJSONArray2.length(); i2++) {
                        t tVar2 = new t();
                        JSONObject optJSONObject2 = optJSONArray2.optJSONObject(i2);
                        tVar2.a = optJSONObject2.optInt("id");
                        tVar2.b = optJSONObject2.optString(AIUIConstant.KEY_NAME);
                        tVar2.c = optJSONObject2.optInt("mapsize");
                        tVar2.d = optJSONObject2.optInt("cty");
                        arrayList2.add(tVar2);
                    }
                    tVar.a(arrayList2);
                }
                arrayList.add(tVar);
            }
            return arrayList;
        } catch (JSONException e) {
            return null;
        } catch (Exception e2) {
            return null;
        }
    }

    public boolean d(int i) {
        return this.b == null ? false : this.b.b(0, true, i);
    }

    public ArrayList<x> e() {
        if (this.b == null) {
            return null;
        }
        String str = "";
        String n = this.b.n();
        if (n == null || n.equals("")) {
            return null;
        }
        ArrayList<x> arrayList = new ArrayList();
        try {
            JSONObject jSONObject = new JSONObject(n);
            if (jSONObject.length() == 0) {
                return null;
            }
            JSONArray optJSONArray = jSONObject.optJSONArray("dataset");
            for (int i = 0; i < optJSONArray.length(); i++) {
                x xVar = new x();
                w wVar = new w();
                JSONObject optJSONObject = optJSONArray.optJSONObject(i);
                wVar.a = optJSONObject.optInt("id");
                wVar.b = optJSONObject.optString(AIUIConstant.KEY_NAME);
                wVar.c = optJSONObject.optString("pinyin");
                wVar.h = optJSONObject.optInt("mapoldsize");
                wVar.i = optJSONObject.optInt("ratio");
                wVar.l = optJSONObject.optInt("status");
                wVar.g = new GeoPoint((double) optJSONObject.optInt(MapViewConstants.ATTR_Y), (double) optJSONObject.optInt(MapViewConstants.ATTR_X));
                if (optJSONObject.optInt("up") == 1) {
                    wVar.j = true;
                } else {
                    wVar.j = false;
                }
                wVar.e = optJSONObject.optInt("lev");
                if (wVar.j) {
                    wVar.k = optJSONObject.optInt("mapsize");
                } else {
                    wVar.k = 0;
                }
                xVar.a(wVar);
                arrayList.add(xVar);
            }
            return arrayList;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean e(int i) {
        return (this.b == null || i < 0) ? false : this.b.b(i, false);
    }

    public boolean f(int i) {
        return (this.b == null || i < 0) ? false : this.b.a(i, false);
    }

    public x g(int i) {
        if (this.b == null || i < 0) {
            return null;
        }
        String str = "";
        String c = this.b.c(i);
        if (c == null || c.equals("")) {
            return null;
        }
        x xVar = new x();
        w wVar = new w();
        try {
            JSONObject jSONObject = new JSONObject(c);
            if (jSONObject.length() == 0) {
                return null;
            }
            wVar.a = jSONObject.optInt("id");
            wVar.b = jSONObject.optString(AIUIConstant.KEY_NAME);
            wVar.c = jSONObject.optString("pinyin");
            wVar.d = jSONObject.optString("headchar");
            wVar.h = jSONObject.optInt("mapoldsize");
            wVar.i = jSONObject.optInt("ratio");
            wVar.l = jSONObject.optInt("status");
            wVar.g = new GeoPoint((double) jSONObject.optInt(MapViewConstants.ATTR_Y), (double) jSONObject.optInt(MapViewConstants.ATTR_X));
            if (jSONObject.optInt("up") == 1) {
                wVar.j = true;
            } else {
                wVar.j = false;
            }
            wVar.e = jSONObject.optInt("lev");
            if (wVar.j) {
                wVar.k = jSONObject.optInt("mapsize");
            } else {
                wVar.k = 0;
            }
            wVar.f = jSONObject.optInt("ver");
            xVar.a(wVar);
            return xVar;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }
}
