package com.baidu.platform.core.e;

import com.baidu.mapapi.search.share.LocationShareURLOption;
import com.baidu.mapapi.search.share.OnGetShareUrlResultListener;
import com.baidu.mapapi.search.share.PoiDetailShareURLOption;
import com.baidu.mapapi.search.share.RouteShareURLOption;
import com.baidu.platform.base.SearchType;
import com.baidu.platform.base.a;

public class g extends a implements a {
    OnGetShareUrlResultListener b = null;

    public void a() {
        this.b = null;
    }

    public void a(OnGetShareUrlResultListener onGetShareUrlResultListener) {
        this.b = onGetShareUrlResultListener;
    }

    public boolean a(LocationShareURLOption locationShareURLOption) {
        this.a = new f();
        this.a.a(new i(this));
        this.a.a(SearchType.LOCATION_SEARCH_SHARE);
        return a(new b(locationShareURLOption));
    }

    public boolean a(PoiDetailShareURLOption poiDetailShareURLOption) {
        this.a = new f();
        this.a.a(new h(this));
        this.a.a(SearchType.POI_DETAIL_SHARE);
        return a(new c(poiDetailShareURLOption));
    }

    public boolean a(RouteShareURLOption routeShareURLOption) {
        this.a = new d();
        this.a.a(new j(this));
        this.a.a(SearchType.ROUTE_PLAN_SHARE);
        return a(new e(routeShareURLOption));
    }
}
