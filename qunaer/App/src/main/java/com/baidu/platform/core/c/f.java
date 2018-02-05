package com.baidu.platform.core.c;

import com.baidu.mapapi.search.poi.OnGetPoiSearchResultListener;
import com.baidu.mapapi.search.poi.PoiBoundSearchOption;
import com.baidu.mapapi.search.poi.PoiCitySearchOption;
import com.baidu.mapapi.search.poi.PoiDetailSearchOption;
import com.baidu.mapapi.search.poi.PoiIndoorOption;
import com.baidu.mapapi.search.poi.PoiNearbySearchOption;
import com.baidu.platform.base.SearchType;
import com.baidu.platform.base.a;

public class f extends a implements a {
    private OnGetPoiSearchResultListener b = null;

    public void a() {
        this.b = null;
    }

    public void a(OnGetPoiSearchResultListener onGetPoiSearchResultListener) {
        this.b = onGetPoiSearchResultListener;
    }

    public boolean a(PoiBoundSearchOption poiBoundSearchOption) {
        this.a = new l(poiBoundSearchOption.mPageNum, poiBoundSearchOption.mPageCapacity);
        this.a.a(new i(this));
        this.a.a(SearchType.POI_IN_BOUND_SEARCH);
        return a(new m(poiBoundSearchOption));
    }

    public boolean a(PoiCitySearchOption poiCitySearchOption) {
        this.a = new l(poiCitySearchOption.mPageNum, poiCitySearchOption.mPageCapacity);
        this.a.a(new h(this));
        this.a.a(SearchType.POI_IN_CITY_SEARCH);
        return a(new m(poiCitySearchOption));
    }

    public boolean a(PoiDetailSearchOption poiDetailSearchOption) {
        this.a = new d();
        this.a.a(new j(this));
        this.a.a(SearchType.POI_DETAIL_SEARCH);
        return a(new e(poiDetailSearchOption));
    }

    public boolean a(PoiIndoorOption poiIndoorOption) {
        this.a = new b();
        this.a.a(new k(this));
        this.a.a(SearchType.INDOOR_POI_SEARCH);
        return a(new c(poiIndoorOption));
    }

    public boolean a(PoiNearbySearchOption poiNearbySearchOption) {
        this.a = new l(poiNearbySearchOption.mPageNum, poiNearbySearchOption.mPageCapacity);
        this.a.a(new g(this));
        this.a.a(SearchType.POI_NEAR_BY_SEARCH);
        return a(new m(poiNearbySearchOption));
    }
}
