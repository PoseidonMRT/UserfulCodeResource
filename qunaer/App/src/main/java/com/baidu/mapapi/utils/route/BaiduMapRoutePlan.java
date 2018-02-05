package com.baidu.mapapi.utils.route;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import com.baidu.mapapi.CoordType;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.navi.IllegalNaviArgumentException;
import com.baidu.mapapi.utils.OpenClientUtil;
import com.baidu.mapapi.utils.a;
import com.baidu.mapapi.utils.poi.IllegalPoiSearchArgumentException;
import com.baidu.mapapi.utils.route.RouteParaOption.EBusStrategyType;
import com.baidu.platform.comapi.util.CoordTrans;

public class BaiduMapRoutePlan {
    private static boolean a = true;

    private static void a(RouteParaOption routeParaOption, Context context, int i) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("http://api.map.baidu.com/direction?");
        stringBuilder.append("origin=");
        LatLng latLng = routeParaOption.a;
        if (SDKInitializer.getCoordType() == CoordType.GCJ02 && latLng != null) {
            latLng = CoordTrans.gcjToBaidu(latLng);
        }
        if (routeParaOption.a != null && routeParaOption.c != null && !routeParaOption.c.equals("")) {
            stringBuilder.append("latlng:");
            stringBuilder.append(latLng.latitude);
            stringBuilder.append(",");
            stringBuilder.append(latLng.longitude);
            stringBuilder.append("|");
            stringBuilder.append("name:");
            stringBuilder.append(routeParaOption.c);
        } else if (routeParaOption.a != null) {
            stringBuilder.append(latLng.latitude);
            stringBuilder.append(",");
            stringBuilder.append(latLng.longitude);
        } else {
            stringBuilder.append(routeParaOption.c);
        }
        latLng = routeParaOption.b;
        if (SDKInitializer.getCoordType() == CoordType.GCJ02 && latLng != null) {
            latLng = CoordTrans.gcjToBaidu(latLng);
        }
        stringBuilder.append("&destination=");
        if (routeParaOption.b != null && routeParaOption.d != null && !routeParaOption.d.equals("")) {
            stringBuilder.append("latlng:");
            stringBuilder.append(latLng.latitude);
            stringBuilder.append(",");
            stringBuilder.append(latLng.longitude);
            stringBuilder.append("|");
            stringBuilder.append("name:");
            stringBuilder.append(routeParaOption.d);
        } else if (routeParaOption.b != null) {
            stringBuilder.append(latLng.latitude);
            stringBuilder.append(",");
            stringBuilder.append(latLng.longitude);
        } else {
            stringBuilder.append(routeParaOption.d);
        }
        String str = "";
        switch (i) {
            case 0:
                str = "driving";
                break;
            case 1:
                str = "transit";
                break;
            case 2:
                str = "walking";
                break;
        }
        stringBuilder.append("&mode=");
        stringBuilder.append(str);
        stringBuilder.append("&region=");
        if (routeParaOption.getCityName() == null || routeParaOption.getCityName().equals("")) {
            stringBuilder.append("全国");
        } else {
            stringBuilder.append(routeParaOption.getCityName());
        }
        stringBuilder.append("&output=html");
        stringBuilder.append("&src=");
        stringBuilder.append(context.getPackageName());
        Uri parse = Uri.parse(stringBuilder.toString());
        Intent intent = new Intent();
        intent.setAction("android.intent.action.VIEW");
        intent.setFlags(268435456);
        intent.setData(parse);
        context.startActivity(intent);
    }

    public static void finish(Context context) {
        if (context != null) {
            a.a(context);
        }
    }

    public static boolean openBaiduMapDrivingRoute(RouteParaOption routeParaOption, Context context) {
        if (routeParaOption == null || context == null) {
            throw new IllegalPoiSearchArgumentException("para or context can not be null.");
        } else if (routeParaOption.b == null && routeParaOption.a == null && routeParaOption.d == null && routeParaOption.c == null) {
            throw new IllegalNaviArgumentException("startPoint and endPoint and endName and startName not all null.");
        } else if (routeParaOption.c == null && routeParaOption.a == null) {
            throw new IllegalNaviArgumentException("startPoint and startName not all null.");
        } else if (routeParaOption.d == null && routeParaOption.b == null) {
            throw new IllegalNaviArgumentException("endPoint and endName not all null.");
        } else if (((routeParaOption.c == null || routeParaOption.c.equals("")) && routeParaOption.a == null) || ((routeParaOption.d == null || routeParaOption.d.equals("")) && routeParaOption.b == null)) {
            Log.e(BaiduMapRoutePlan.class.getName(), "poi startName or endName can not be empty string while pt is null");
            return false;
        } else {
            if (routeParaOption.f == null) {
                routeParaOption.f = EBusStrategyType.bus_recommend_way;
            }
            int baiduMapVersion = OpenClientUtil.getBaiduMapVersion(context);
            if (baiduMapVersion == 0) {
                Log.e("baidumapsdk", "BaiduMap app is not installed.");
                if (a) {
                    a(routeParaOption, context, 0);
                    return true;
                }
                throw new IllegalPoiSearchArgumentException("BaiduMap app is not installed.");
            } else if (baiduMapVersion >= 810) {
                return a.a(routeParaOption, context, 0);
            } else {
                Log.e("baidumapsdk", "Baidumap app version is too lowl.Version is greater than 8.1");
                if (a) {
                    a(routeParaOption, context, 0);
                    return true;
                }
                throw new IllegalPoiSearchArgumentException("Baidumap app version is too lowl.Version is greater than 8.1");
            }
        }
    }

    public static boolean openBaiduMapTransitRoute(RouteParaOption routeParaOption, Context context) {
        if (routeParaOption == null || context == null) {
            throw new IllegalPoiSearchArgumentException("para or context can not be null.");
        } else if (routeParaOption.b == null && routeParaOption.a == null && routeParaOption.d == null && routeParaOption.c == null) {
            throw new IllegalNaviArgumentException("startPoint and endPoint and endName and startName not all null.");
        } else if (routeParaOption.c == null && routeParaOption.a == null) {
            throw new IllegalNaviArgumentException("startPoint and startName not all null.");
        } else if (routeParaOption.d == null && routeParaOption.b == null) {
            throw new IllegalNaviArgumentException("endPoint and endName not all null.");
        } else if (((routeParaOption.c == null || routeParaOption.c.equals("")) && routeParaOption.a == null) || ((routeParaOption.d == null || routeParaOption.d.equals("")) && routeParaOption.b == null)) {
            Log.e(BaiduMapRoutePlan.class.getName(), "poi startName or endName can not be empty string while pt is null");
            return false;
        } else {
            if (routeParaOption.f == null) {
                routeParaOption.f = EBusStrategyType.bus_recommend_way;
            }
            int baiduMapVersion = OpenClientUtil.getBaiduMapVersion(context);
            if (baiduMapVersion == 0) {
                Log.e("baidumapsdk", "BaiduMap app is not installed.");
                if (a) {
                    a(routeParaOption, context, 1);
                    return true;
                }
                throw new IllegalPoiSearchArgumentException("BaiduMap app is not installed.");
            } else if (baiduMapVersion >= 810) {
                return a.a(routeParaOption, context, 1);
            } else {
                Log.e("baidumapsdk", "Baidumap app version is too lowl.Version is greater than 8.1");
                if (a) {
                    a(routeParaOption, context, 1);
                    return true;
                }
                throw new IllegalPoiSearchArgumentException("Baidumap app version is too lowl.Version is greater than 8.1");
            }
        }
    }

    public static boolean openBaiduMapWalkingRoute(RouteParaOption routeParaOption, Context context) {
        if (routeParaOption == null || context == null) {
            throw new IllegalPoiSearchArgumentException("para or context can not be null.");
        } else if (routeParaOption.b == null && routeParaOption.a == null && routeParaOption.d == null && routeParaOption.c == null) {
            throw new IllegalNaviArgumentException("startPoint and endPoint and endName and startName not all null.");
        } else if (routeParaOption.c == null && routeParaOption.a == null) {
            throw new IllegalNaviArgumentException("startPoint and startName not all null.");
        } else if (routeParaOption.d == null && routeParaOption.b == null) {
            throw new IllegalNaviArgumentException("endPoint and endName not all null.");
        } else if (((routeParaOption.c == null || routeParaOption.c.equals("")) && routeParaOption.a == null) || ((routeParaOption.d == null || routeParaOption.d.equals("")) && routeParaOption.b == null)) {
            Log.e(BaiduMapRoutePlan.class.getName(), "poi startName or endName can not be empty string while pt is null");
            return false;
        } else {
            if (routeParaOption.f == null) {
                routeParaOption.f = EBusStrategyType.bus_recommend_way;
            }
            int baiduMapVersion = OpenClientUtil.getBaiduMapVersion(context);
            if (baiduMapVersion == 0) {
                Log.e("baidumapsdk", "BaiduMap app is not installed.");
                if (a) {
                    a(routeParaOption, context, 2);
                    return true;
                }
                throw new IllegalPoiSearchArgumentException("BaiduMap app is not installed.");
            } else if (baiduMapVersion >= 810) {
                return a.a(routeParaOption, context, 2);
            } else {
                Log.e("baidumapsdk", "Baidumap app version is too lowl.Version is greater than 8.1");
                if (a) {
                    a(routeParaOption, context, 2);
                    return true;
                }
                throw new IllegalPoiSearchArgumentException("Baidumap app version is too lowl.Version is greater than 8.1");
            }
        }
    }

    public static void setSupportWebRoute(boolean z) {
        a = z;
    }
}
