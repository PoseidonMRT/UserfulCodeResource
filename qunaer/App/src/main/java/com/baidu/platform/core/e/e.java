package com.baidu.platform.core.e;

import com.baidu.mapapi.model.CoordUtil;
import com.baidu.mapapi.model.inner.Point;
import com.baidu.mapapi.search.share.RouteShareURLOption;
import com.baidu.platform.base.g;
import com.baidu.platform.comjni.util.AppMD5;
import com.baidu.platform.domain.b;
import com.baidu.platform.util.a;

public class e extends g {
    public e(RouteShareURLOption routeShareURLOption) {
        a(routeShareURLOption);
    }

    private int a(String str) {
        try {
            return Integer.parseInt(str);
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    private void a(RouteShareURLOption routeShareURLOption) {
        a aVar = new a();
        Point ll2point = CoordUtil.ll2point(routeShareURLOption.mFrom.getLocation());
        Point ll2point2 = CoordUtil.ll2point(routeShareURLOption.mTo.getLocation());
        String str = ll2point != null ? "1$$$$" + ll2point.x + "," + ll2point.y + "$$" : "2$$$$$$";
        String name = routeShareURLOption.mFrom.getName();
        if (name == null || name.equals("")) {
            name = "起点";
        }
        String str2 = str + name + "$$0$$$$";
        str = ll2point2 != null ? "1$$$$" + ll2point2.x + "," + ll2point2.y + "$$" : "2$$$$$$";
        String name2 = routeShareURLOption.mTo.getName();
        if (name2 == null || name2.equals("")) {
            name2 = "终点";
        }
        String str3 = str + name2 + "$$0$$$$";
        String str4 = "";
        str = "";
        switch (routeShareURLOption.mMode.ordinal()) {
            case 0:
                str = "&sharecallbackflag=carRoute";
                str4 = "nav";
                aVar.a("sc", a(routeShareURLOption.mFrom.getCity()) + "");
                aVar.a("ec", a(routeShareURLOption.mTo.getCity()) + "");
                break;
            case 1:
                str = "&sharecallbackflag=footRoute";
                str4 = "walk";
                aVar.a("sc", a(routeShareURLOption.mFrom.getCity()) + "");
                aVar.a("ec", a(routeShareURLOption.mTo.getCity()) + "");
                break;
            case 2:
                str = "&sharecallbackflag=cycleRoute";
                str4 = "cycle";
                aVar.a("sc", a(routeShareURLOption.mFrom.getCity()) + "");
                aVar.a("ec", a(routeShareURLOption.mTo.getCity()) + "");
                break;
            case 3:
                str = "&i=" + routeShareURLOption.mPn + ",1,1&sharecallbackflag=busRoute";
                aVar.a("c", routeShareURLOption.mCityCode + "");
                str4 = "bt";
                break;
        }
        aVar.a("sn", str2);
        aVar.a("en", str3);
        this.a.a("url", "http://map.baidu.com/?newmap=1&s=" + str4 + (AppMD5.encodeUrlParamsValue("&" + aVar.a() + ("&start=" + name + "&end=" + name2)) + str));
        this.a.a("from", "android_map_sdk");
    }

    public String a(b bVar) {
        return bVar.r();
    }
}
