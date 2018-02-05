package com.baidu.platform.core.b;

import com.baidu.mapapi.search.geocode.GeoCodeOption;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.platform.base.SearchType;
import com.baidu.platform.base.g;

public class a extends com.baidu.platform.base.a implements f {
    OnGetGeoCoderResultListener b;

    public void a() {
        this.b = null;
    }

    public void a(OnGetGeoCoderResultListener onGetGeoCoderResultListener) {
        this.b = onGetGeoCoderResultListener;
    }

    public boolean a(GeoCodeOption geoCodeOption) {
        this.a = new d();
        g eVar = new e(geoCodeOption);
        this.a.a(new b(this));
        this.a.a(SearchType.GEO_CODER);
        return a(eVar);
    }

    public boolean a(ReverseGeoCodeOption reverseGeoCodeOption) {
        this.a = new g();
        g hVar = new h(reverseGeoCodeOption);
        this.a.a(new c(this));
        this.a.a(SearchType.REVERSE_GEO_CODER);
        return a(hVar);
    }
}
