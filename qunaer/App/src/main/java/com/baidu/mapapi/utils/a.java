package com.baidu.mapapi.utils;

import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import com.baidu.mapapi.CoordType;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.common.AppTools;
import com.baidu.mapapi.model.CoordUtil;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.model.inner.GeoPoint;
import com.baidu.mapapi.navi.NaviParaOption;
import com.baidu.mapapi.utils.poi.DispathcPoiData;
import com.baidu.mapapi.utils.poi.PoiParaOption;
import com.baidu.mapapi.utils.route.RouteParaOption;
import com.baidu.mapapi.utils.route.RouteParaOption.EBusStrategyType;
import com.baidu.mapframework.open.aidl.IComOpenClient;
import com.baidu.platform.comapi.util.CoordTrans;
import com.iflytek.aiui.AIUIConstant;
import com.iflytek.cloud.SpeechEvent;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class a {
    public static int a = -1;
    static ServiceConnection b = new c();
    private static final String c = a.class.getName();
    private static com.baidu.mapframework.open.aidl.a d;
    private static IComOpenClient e;
    private static int f;
    private static String g = null;
    private static String h = null;
    private static String i = null;
    private static List<DispathcPoiData> j = new ArrayList();
    private static LatLng k = null;
    private static LatLng l = null;
    private static String m = null;
    private static String n = null;
    private static EBusStrategyType o;
    private static String p = null;
    private static String q = null;
    private static LatLng r = null;
    private static int s = 0;
    private static boolean t = false;
    private static boolean u = false;
    private static Thread v;

    public static String a() {
        return AppTools.getBaiduMapToken();
    }

    public static void a(int i, Context context) {
        switch (i) {
            case 0:
            case 1:
            case 2:
                c(context, i);
                return;
            case 3:
                c(context);
                return;
            case 4:
                d(context);
                return;
            case 5:
                e(context);
                return;
            case 7:
                f(context);
                return;
            case 8:
                g(context);
                return;
            case 9:
                h(context);
                return;
            default:
                return;
        }
    }

    public static void a(Context context) {
        if (u) {
            context.unbindService(b);
            u = false;
        }
    }

    private static void a(List<DispathcPoiData> list, Context context) {
        g = context.getPackageName();
        h = b(context);
        i = "";
        if (j != null) {
            j.clear();
        }
        for (DispathcPoiData add : list) {
            j.add(add);
        }
    }

    public static boolean a(int i) {
        switch (i) {
            case 0:
            case 1:
            case 2:
                return g();
            case 3:
                return h();
            case 4:
                return m();
            case 5:
                return j();
            case 6:
                return i();
            case 7:
                return k();
            case 8:
                return l();
            default:
                return false;
        }
    }

    public static boolean a(Context context, int i) {
        try {
            if (com.baidu.platform.comapi.a.a.a(context)) {
                t = false;
                switch (i) {
                    case 0:
                        a = 0;
                        break;
                    case 1:
                        a = 1;
                        break;
                    case 2:
                        a = 2;
                        break;
                    case 3:
                        a = 3;
                        break;
                    case 4:
                        a = 4;
                        break;
                    case 5:
                        a = 5;
                        break;
                    case 6:
                        a = 6;
                        break;
                    case 7:
                        a = 7;
                        break;
                    case 8:
                        a = 8;
                        break;
                    case 9:
                        a = 9;
                        break;
                }
                if (i == 9) {
                    u = false;
                }
                if (d == null || !u) {
                    b(context, i);
                    return true;
                } else if (e != null) {
                    t = true;
                    return a(i);
                } else {
                    d.a(new b(i));
                    return true;
                }
            }
            Log.d(c, "package sign verify failed");
            return false;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static boolean a(NaviParaOption naviParaOption, Context context, int i) {
        b(naviParaOption, context, i);
        return a(context, i);
    }

    public static boolean a(PoiParaOption poiParaOption, Context context, int i) {
        b(poiParaOption, context, i);
        return a(context, i);
    }

    public static boolean a(RouteParaOption routeParaOption, Context context, int i) {
        b(routeParaOption, context, i);
        return a(context, i);
    }

    public static boolean a(List<DispathcPoiData> list, Context context, int i) {
        a((List) list, context);
        return a(context, i);
    }

    public static String b(Context context) {
        PackageManager packageManager;
        ApplicationInfo applicationInfo = null;
        try {
            packageManager = context.getPackageManager();
            try {
                applicationInfo = packageManager.getApplicationInfo(context.getPackageName(), 0);
            } catch (NameNotFoundException e) {
            }
        } catch (NameNotFoundException e2) {
            Object obj = applicationInfo;
        }
        return (String) packageManager.getApplicationLabel(applicationInfo);
    }

    private static void b(Context context, int i) {
        Intent intent = new Intent();
        String a = a();
        if (a != null) {
            intent.putExtra("api_token", a);
            intent.setAction("com.baidu.map.action.OPEN_SERVICE");
            intent.setPackage("com.baidu.BaiduMap");
            if (i != 9) {
                u = context.bindService(intent, b, 1);
            }
            if (u) {
                v = new Thread(new e(context, i));
                v.setDaemon(true);
                v.start();
                return;
            }
            Log.e("baidumapsdk", "bind service failed，call openapi");
            a(i, context);
        }
    }

    private static void b(NaviParaOption naviParaOption, Context context, int i) {
        g = context.getPackageName();
        m = null;
        k = null;
        n = null;
        l = null;
        if (naviParaOption.getStartPoint() != null) {
            k = naviParaOption.getStartPoint();
        }
        if (naviParaOption.getEndPoint() != null) {
            l = naviParaOption.getEndPoint();
        }
        if (naviParaOption.getStartName() != null) {
            m = naviParaOption.getStartName();
        }
        if (naviParaOption.getEndName() != null) {
            n = naviParaOption.getEndName();
        }
    }

    private static void b(PoiParaOption poiParaOption, Context context, int i) {
        p = null;
        q = null;
        r = null;
        s = 0;
        g = context.getPackageName();
        if (poiParaOption.getUid() != null) {
            p = poiParaOption.getUid();
        }
        if (poiParaOption.getKey() != null) {
            q = poiParaOption.getKey();
        }
        if (poiParaOption.getCenter() != null) {
            r = poiParaOption.getCenter();
        }
        if (poiParaOption.getRadius() != 0) {
            s = poiParaOption.getRadius();
        }
    }

    private static void b(RouteParaOption routeParaOption, Context context, int i) {
        m = null;
        k = null;
        n = null;
        l = null;
        g = context.getPackageName();
        if (routeParaOption.getStartPoint() != null) {
            k = routeParaOption.getStartPoint();
        }
        if (routeParaOption.getEndPoint() != null) {
            l = routeParaOption.getEndPoint();
        }
        if (routeParaOption.getStartName() != null) {
            m = routeParaOption.getStartName();
        }
        if (routeParaOption.getEndName() != null) {
            n = routeParaOption.getEndName();
        }
        if (routeParaOption.getBusStrategyType() != null) {
            o = routeParaOption.getBusStrategyType();
        }
        switch (i) {
            case 0:
                f = 0;
                return;
            case 1:
                f = 1;
                return;
            case 2:
                f = 2;
                return;
            default:
                return;
        }
    }

    private static void c(Context context) {
        if (v != null) {
            v.interrupt();
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("baidumap://map/place/detail?");
        stringBuilder.append("uid=").append(p);
        stringBuilder.append("&show_type=").append("detail_page");
        stringBuilder.append("&src=").append("sdk_[" + g + "]");
        Intent intent = new Intent("android.intent.action.VIEW", Uri.parse(stringBuilder.toString()));
        intent.setFlags(268435456);
        context.startActivity(intent);
    }

    private static void c(Context context, int i) {
        if (v != null) {
            v.interrupt();
        }
        String[] strArr = new String[]{"driving", "transit", "walking"};
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("baidumap://map/direction?");
        stringBuilder.append("origin=");
        if (k != null && SDKInitializer.getCoordType() == CoordType.GCJ02) {
            k = CoordTrans.gcjToBaidu(k);
        }
        if (!TextUtils.isEmpty(m) && k != null) {
            stringBuilder.append("name:").append(m).append("|latlng:").append(k.latitude).append(",").append(k.longitude);
        } else if (!TextUtils.isEmpty(m)) {
            stringBuilder.append(m);
        } else if (k != null) {
            stringBuilder.append(k.latitude).append(",").append(k.longitude);
        }
        stringBuilder.append("&destination=");
        if (l != null && SDKInitializer.getCoordType() == CoordType.GCJ02) {
            l = CoordTrans.gcjToBaidu(l);
        }
        if (!TextUtils.isEmpty(n) && l != null) {
            stringBuilder.append("name:").append(n).append("|latlng:").append(l.latitude).append(",").append(l.longitude);
        } else if (!TextUtils.isEmpty(n)) {
            stringBuilder.append(n);
        } else if (l != null) {
            stringBuilder.append(l.latitude).append(",").append(l.longitude);
        }
        stringBuilder.append("&mode=").append(strArr[i]);
        stringBuilder.append("&target=").append("1");
        stringBuilder.append("&src=").append("sdk_[" + g + "]");
        Intent intent = new Intent("android.intent.action.VIEW", Uri.parse(stringBuilder.toString()));
        intent.setFlags(268435456);
        context.startActivity(intent);
    }

    private static void d(Context context) {
        if (v != null) {
            v.interrupt();
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("baidumap://map/nearbysearch?");
        if (SDKInitializer.getCoordType() == CoordType.GCJ02) {
            r = CoordTrans.gcjToBaidu(r);
        }
        stringBuilder.append("center=").append(r.latitude).append(",").append(r.longitude);
        stringBuilder.append("&query=").append(q).append("&radius=").append(s);
        stringBuilder.append("&src=").append("sdk_[" + g + "]");
        Intent intent = new Intent("android.intent.action.VIEW", Uri.parse(stringBuilder.toString()));
        intent.setFlags(268435456);
        context.startActivity(intent);
    }

    private static void e(Context context) {
        if (v != null) {
            v.interrupt();
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("baidumap://map/navi?");
        if (SDKInitializer.getCoordType() == CoordType.GCJ02) {
            k = CoordTrans.gcjToBaidu(k);
            l = CoordTrans.gcjToBaidu(l);
        }
        stringBuilder.append("origin=").append(k.latitude).append(",").append(k.longitude);
        stringBuilder.append("&location=").append(l.latitude).append(",").append(l.longitude);
        stringBuilder.append("&src=").append("sdk_[" + g + "]");
        Intent intent = new Intent("android.intent.action.VIEW", Uri.parse(stringBuilder.toString()));
        intent.setFlags(268435456);
        context.startActivity(intent);
    }

    private static void f(Context context) {
        if (v != null) {
            v.interrupt();
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("baidumap://map/walknavi?");
        if (SDKInitializer.getCoordType() == CoordType.GCJ02) {
            k = CoordTrans.gcjToBaidu(k);
            l = CoordTrans.gcjToBaidu(l);
        }
        stringBuilder.append("origin=").append(k.latitude).append(",").append(k.longitude);
        stringBuilder.append("&destination=").append(l.latitude).append(",").append(l.longitude);
        stringBuilder.append("&src=").append("sdk_[" + g + "]");
        Intent intent = new Intent("android.intent.action.VIEW", Uri.parse(stringBuilder.toString()));
        intent.setFlags(268435456);
        context.startActivity(intent);
    }

    private static void g(Context context) {
        if (v != null) {
            v.interrupt();
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("baidumap://map/bikenavi?");
        if (SDKInitializer.getCoordType() == CoordType.GCJ02) {
            k = CoordTrans.gcjToBaidu(k);
            l = CoordTrans.gcjToBaidu(l);
        }
        stringBuilder.append("origin=").append(k.latitude).append(",").append(k.longitude);
        stringBuilder.append("&destination=").append(l.latitude).append(",").append(l.longitude);
        stringBuilder.append("&src=").append("sdk_[" + g + "]");
        Intent intent = new Intent("android.intent.action.VIEW", Uri.parse(stringBuilder.toString()));
        intent.setFlags(268435456);
        context.startActivity(intent);
    }

    private static boolean g() {
        try {
            Log.d(c, "callDispatchTakeOutRoute");
            String a = e.a("map.android.baidu.mainmap");
            if (a != null) {
                Bundle bundle = new Bundle();
                bundle.putString("target", "route_search_page");
                Bundle bundle2 = new Bundle();
                bundle2.putInt("route_type", f);
                bundle2.putInt("bus_strategy", o.ordinal());
                bundle2.putInt("cross_city_bus_strategy", 5);
                if (k != null) {
                    bundle2.putInt("start_type", 1);
                    bundle2.putInt("start_longitude", (int) CoordUtil.ll2mc(k).getLongitudeE6());
                    bundle2.putInt("start_latitude", (int) CoordUtil.ll2mc(k).getLatitudeE6());
                } else {
                    bundle2.putInt("start_type", 2);
                    bundle2.putInt("start_longitude", 0);
                    bundle2.putInt("start_latitude", 0);
                }
                if (m != null) {
                    bundle2.putString("start_keyword", m);
                } else {
                    bundle2.putString("start_keyword", "地图上的点");
                }
                bundle2.putString("start_uid", "");
                if (l != null) {
                    bundle2.putInt("end_type", 1);
                    bundle2.putInt("end_longitude", (int) CoordUtil.ll2mc(l).getLongitudeE6());
                    bundle2.putInt("end_latitude", (int) CoordUtil.ll2mc(l).getLatitudeE6());
                } else {
                    bundle2.putInt("end_type", 2);
                    bundle2.putInt("end_longitude", 0);
                    bundle2.putInt("end_latitude", 0);
                }
                if (n != null) {
                    bundle2.putString("end_keyword", n);
                } else {
                    bundle2.putString("end_keyword", "地图上的点");
                }
                bundle2.putString("end_uid", "");
                bundle.putBundle("base_params", bundle2);
                bundle2 = new Bundle();
                bundle2.putString("launch_from", "sdk_[" + g + "]");
                bundle.putBundle("ext_params", bundle2);
                return e.a("map.android.baidu.mainmap", a, bundle);
            }
            Log.d(c, "callDispatchTakeOut com not found");
            return false;
        } catch (Throwable e) {
            Log.d(c, "callDispatchTakeOut exception", e);
            return false;
        }
    }

    private static void h(Context context) {
        if (v != null) {
            v.interrupt();
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("baidumap://map/walknavi?");
        if (SDKInitializer.getCoordType() == CoordType.GCJ02) {
            k = CoordTrans.gcjToBaidu(k);
            l = CoordTrans.gcjToBaidu(l);
        }
        stringBuilder.append("origin=").append(k.latitude).append(",").append(k.longitude);
        stringBuilder.append("&destination=").append(l.latitude).append(",").append(l.longitude);
        stringBuilder.append("&mode=").append("walking_ar");
        stringBuilder.append("&src=").append("sdk_[" + g + "]");
        Log.e("test", stringBuilder.toString());
        Intent intent = new Intent("android.intent.action.VIEW", Uri.parse(stringBuilder.toString()));
        intent.setFlags(268435456);
        context.startActivity(intent);
    }

    private static boolean h() {
        try {
            Log.d(c, "callDispatchTakeOutPoiDetials");
            String a = e.a("map.android.baidu.mainmap");
            if (a != null) {
                Bundle bundle = new Bundle();
                bundle.putString("target", "request_poi_detail_page");
                Bundle bundle2 = new Bundle();
                if (p != null) {
                    bundle2.putString(AIUIConstant.KEY_UID, p);
                } else {
                    bundle2.putString(AIUIConstant.KEY_UID, "");
                }
                bundle.putBundle("base_params", bundle2);
                bundle2 = new Bundle();
                bundle2.putString("launch_from", "sdk_[" + g + "]");
                bundle.putBundle("ext_params", bundle2);
                return e.a("map.android.baidu.mainmap", a, bundle);
            }
            Log.d(c, "callDispatchTakeOut com not found");
            return false;
        } catch (Throwable e) {
            Log.d(c, "callDispatchTakeOut exception", e);
        }
    }

    private static boolean i() {
        JSONException e;
        if (j == null || j.size() <= 0) {
            return false;
        }
        try {
            Log.d(c, "callDispatchPoiToBaiduMap");
            String a = e.a("map.android.baidu.mainmap");
            if (a != null) {
                Bundle bundle = new Bundle();
                bundle.putString("target", "favorite_page");
                Bundle bundle2 = new Bundle();
                JSONArray jSONArray = new JSONArray();
                int i = 0;
                int i2 = 0;
                while (i < j.size()) {
                    int i3;
                    if (((DispathcPoiData) j.get(i)).name == null || ((DispathcPoiData) j.get(i)).name.equals("")) {
                        i3 = i2;
                    } else if (((DispathcPoiData) j.get(i)).pt == null) {
                        i3 = i2;
                    } else {
                        JSONObject jSONObject = new JSONObject();
                        try {
                            jSONObject.put(AIUIConstant.KEY_NAME, ((DispathcPoiData) j.get(i)).name);
                            GeoPoint ll2mc = CoordUtil.ll2mc(((DispathcPoiData) j.get(i)).pt);
                            jSONObject.put("ptx", ll2mc.getLongitudeE6());
                            jSONObject.put("pty", ll2mc.getLatitudeE6());
                            jSONObject.put("addr", ((DispathcPoiData) j.get(i)).addr);
                            jSONObject.put(AIUIConstant.KEY_UID, ((DispathcPoiData) j.get(i)).uid);
                            i3 = i2 + 1;
                            try {
                                jSONArray.put(jSONObject);
                            } catch (JSONException e2) {
                                e = e2;
                                e.printStackTrace();
                                i++;
                                i2 = i3;
                            }
                        } catch (JSONException e3) {
                            JSONException jSONException = e3;
                            i3 = i2;
                            e = jSONException;
                            e.printStackTrace();
                            i++;
                            i2 = i3;
                        }
                    }
                    i++;
                    i2 = i3;
                }
                if (i2 == 0) {
                    return false;
                }
                bundle2.putString(SpeechEvent.KEY_EVENT_RECORD_DATA, jSONArray.toString());
                bundle2.putString("from", h);
                bundle2.putString("pkg", g);
                bundle2.putString("cls", i);
                bundle2.putInt("count", i2);
                bundle.putBundle("base_params", bundle2);
                Bundle bundle3 = new Bundle();
                bundle3.putString("launch_from", "sdk_[" + g + "]");
                bundle.putBundle("ext_params", bundle3);
                return e.a("map.android.baidu.mainmap", a, bundle);
            }
            Log.d(c, "callDispatchPoiToBaiduMap com not found");
            return false;
        } catch (Throwable e4) {
            Log.d(c, "callDispatchPoiToBaiduMap exception", e4);
        }
    }

    private static boolean j() {
        try {
            Log.d(c, "callDispatchTakeOutRouteNavi");
            String a = e.a("map.android.baidu.mainmap");
            if (a != null) {
                Bundle bundle = new Bundle();
                bundle.putString("target", "navigation_page");
                Bundle bundle2 = new Bundle();
                bundle2.putString("coord_type", "bd09ll");
                bundle2.putString("type", "DIS");
                StringBuffer stringBuffer = new StringBuffer();
                if (m != null) {
                    stringBuffer.append("name:" + m + "|");
                }
                if (SDKInitializer.getCoordType() == CoordType.GCJ02) {
                    k = CoordTrans.gcjToBaidu(k);
                }
                stringBuffer.append(String.format("latlng:%f,%f", new Object[]{Double.valueOf(k.latitude), Double.valueOf(k.longitude)}));
                StringBuffer stringBuffer2 = new StringBuffer();
                if (n != null) {
                    stringBuffer2.append("name:" + n + "|");
                }
                if (SDKInitializer.getCoordType() == CoordType.GCJ02) {
                    l = CoordTrans.gcjToBaidu(l);
                }
                stringBuffer2.append(String.format("latlng:%f,%f", new Object[]{Double.valueOf(l.latitude), Double.valueOf(l.longitude)}));
                bundle2.putString("origin", stringBuffer.toString());
                bundle2.putString("destination", stringBuffer2.toString());
                bundle.putBundle("base_params", bundle2);
                bundle2 = new Bundle();
                bundle2.putString("launch_from", "sdk_[" + g + "]");
                bundle.putBundle("ext_params", bundle2);
                return e.a("map.android.baidu.mainmap", a, bundle);
            }
            Log.d(c, "callDispatchTakeOut com not found");
            return false;
        } catch (Throwable e) {
            Log.d(c, "callDispatchTakeOut exception", e);
            return false;
        }
    }

    private static boolean k() {
        try {
            Log.d(c, "callDispatchTakeOutRouteNavi");
            String a = e.a("map.android.baidu.mainmap");
            if (a != null) {
                Bundle bundle = new Bundle();
                bundle.putString("target", "walknavi_page");
                Bundle bundle2 = new Bundle();
                bundle2.putString("coord_type", "bd09ll");
                StringBuffer stringBuffer = new StringBuffer();
                if (m != null) {
                    stringBuffer.append("name:" + m + "|");
                }
                if (SDKInitializer.getCoordType() == CoordType.GCJ02) {
                    k = CoordTrans.gcjToBaidu(k);
                }
                stringBuffer.append(String.format("latlng:%f,%f", new Object[]{Double.valueOf(k.latitude), Double.valueOf(k.longitude)}));
                StringBuffer stringBuffer2 = new StringBuffer();
                if (n != null) {
                    stringBuffer2.append("name:" + n + "|");
                }
                if (SDKInitializer.getCoordType() == CoordType.GCJ02) {
                    l = CoordTrans.gcjToBaidu(l);
                }
                stringBuffer2.append(String.format("latlng:%f,%f", new Object[]{Double.valueOf(l.latitude), Double.valueOf(l.longitude)}));
                bundle2.putString("origin", stringBuffer.toString());
                bundle2.putString("destination", stringBuffer2.toString());
                bundle.putBundle("base_params", bundle2);
                bundle2 = new Bundle();
                bundle2.putString("launch_from", "sdk_[" + g + "]");
                bundle.putBundle("ext_params", bundle2);
                return e.a("map.android.baidu.mainmap", a, bundle);
            }
            Log.d(c, "callDispatchTakeOut com not found");
            return false;
        } catch (Throwable e) {
            Log.d(c, "callDispatchTakeOut exception", e);
            return false;
        }
    }

    private static boolean l() {
        try {
            Log.d(c, "callDispatchTakeOutRouteRidingNavi");
            String a = e.a("map.android.baidu.mainmap");
            if (a != null) {
                Bundle bundle = new Bundle();
                bundle.putString("target", "bikenavi_page");
                Bundle bundle2 = new Bundle();
                bundle2.putString("coord_type", "bd09ll");
                StringBuffer stringBuffer = new StringBuffer();
                if (m != null) {
                    stringBuffer.append("name:" + m + "|");
                }
                if (SDKInitializer.getCoordType() == CoordType.GCJ02) {
                    k = CoordTrans.gcjToBaidu(k);
                }
                stringBuffer.append(String.format("latlng:%f,%f", new Object[]{Double.valueOf(k.latitude), Double.valueOf(k.longitude)}));
                StringBuffer stringBuffer2 = new StringBuffer();
                if (n != null) {
                    stringBuffer2.append("name:" + n + "|");
                }
                if (SDKInitializer.getCoordType() == CoordType.GCJ02) {
                    l = CoordTrans.gcjToBaidu(l);
                }
                stringBuffer2.append(String.format("latlng:%f,%f", new Object[]{Double.valueOf(l.latitude), Double.valueOf(l.longitude)}));
                bundle2.putString("origin", stringBuffer.toString());
                bundle2.putString("destination", stringBuffer2.toString());
                bundle.putBundle("base_params", bundle2);
                bundle2 = new Bundle();
                bundle2.putString("launch_from", "sdk_[" + g + "]");
                bundle.putBundle("ext_params", bundle2);
                return e.a("map.android.baidu.mainmap", a, bundle);
            }
            Log.d(c, "callDispatchTakeOut com not found");
            return false;
        } catch (Throwable e) {
            Log.d(c, "callDispatchTakeOut exception", e);
            return false;
        }
    }

    private static boolean m() {
        try {
            Log.d(c, "callDispatchTakeOutPoiNearbySearch");
            String a = e.a("map.android.baidu.mainmap");
            if (a != null) {
                Bundle bundle = new Bundle();
                bundle.putString("target", "poi_search_page");
                Bundle bundle2 = new Bundle();
                if (q != null) {
                    bundle2.putString("search_key", q);
                } else {
                    bundle2.putString("search_key", "");
                }
                if (r != null) {
                    bundle2.putInt("center_pt_x", (int) CoordUtil.ll2mc(r).getLongitudeE6());
                    bundle2.putInt("center_pt_y", (int) CoordUtil.ll2mc(r).getLatitudeE6());
                } else {
                    bundle2.putString("search_key", "");
                }
                if (s != 0) {
                    bundle2.putInt("search_radius", s);
                } else {
                    bundle2.putInt("search_radius", 1000);
                }
                bundle2.putBoolean("is_direct_search", true);
                bundle2.putBoolean("is_direct_area_search", true);
                bundle.putBundle("base_params", bundle2);
                bundle2 = new Bundle();
                bundle2.putString("launch_from", "sdk_[" + g + "]");
                bundle.putBundle("ext_params", bundle2);
                return e.a("map.android.baidu.mainmap", a, bundle);
            }
            Log.d(c, "callDispatchTakeOut com not found");
            return false;
        } catch (Throwable e) {
            Log.d(c, "callDispatchTakeOut exception", e);
        }
    }
}
