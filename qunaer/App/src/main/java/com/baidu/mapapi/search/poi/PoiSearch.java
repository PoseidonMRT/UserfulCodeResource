package com.baidu.mapapi.search.poi;

import com.baidu.mapapi.BMapManager;
import com.baidu.mapapi.search.core.i;
import com.baidu.platform.core.c.a;
import com.baidu.platform.core.c.f;

public class PoiSearch extends i {
    private a a = new f();
    private boolean b = false;

    PoiSearch() {
    }

    public static PoiSearch newInstance() {
        BMapManager.init();
        return new PoiSearch();
    }

    public void destroy() {
        if (!this.b) {
            this.b = true;
            this.a.a();
            BMapManager.destroy();
        }
    }

    public boolean searchInBound(PoiBoundSearchOption poiBoundSearchOption) {
        if (this.a == null) {
            throw new IllegalStateException("searcher is null, please call newInstance first.");
        } else if (poiBoundSearchOption != null && poiBoundSearchOption.mBound != null && poiBoundSearchOption.mKeyword != null) {
            return this.a.a(poiBoundSearchOption);
        } else {
            throw new IllegalArgumentException("option or bound or keyworld can not be null");
        }
    }

    public boolean searchInCity(PoiCitySearchOption poiCitySearchOption) {
        if (this.a == null) {
            throw new IllegalStateException("searcher is null, please call newInstance first.");
        } else if (poiCitySearchOption != null && poiCitySearchOption.mCity != null && poiCitySearchOption.mKeyword != null) {
            return this.a.a(poiCitySearchOption);
        } else {
            throw new IllegalArgumentException("option or city or keyworld can not be null");
        }
    }

    public boolean searchNearby(PoiNearbySearchOption poiNearbySearchOption) {
        if (this.a == null) {
            throw new IllegalStateException("searcher is null, please call newInstance first.");
        } else if (poiNearbySearchOption != null && poiNearbySearchOption.mLocation != null && poiNearbySearchOption.mKeyword != null) {
            return poiNearbySearchOption.mRadius <= 0 ? false : this.a.a(poiNearbySearchOption);
        } else {
            throw new IllegalArgumentException("option or location or keyworld can not be null");
        }
    }

    public boolean searchPoiDetail(PoiDetailSearchOption poiDetailSearchOption) {
        if (this.a == null) {
            throw new IllegalStateException("searcher is null, please call newInstance first.");
        } else if (poiDetailSearchOption != null && poiDetailSearchOption.mUid != null) {
            return this.a.a(poiDetailSearchOption);
        } else {
            throw new IllegalArgumentException("option or uid can not be null");
        }
    }

    public boolean searchPoiIndoor(PoiIndoorOption poiIndoorOption) {
        if (this.a == null) {
            throw new IllegalStateException("searcher is null, please call newInstance first.");
        } else if (poiIndoorOption != null && poiIndoorOption.bid != null && poiIndoorOption.wd != null) {
            return this.a.a(poiIndoorOption);
        } else {
            throw new IllegalArgumentException("option or indoor bid or keyword can not be null");
        }
    }

    public void setOnGetPoiSearchResultListener(OnGetPoiSearchResultListener onGetPoiSearchResultListener) {
        if (this.a == null) {
            throw new IllegalStateException("searcher is null, please call newInstance first.");
        } else if (onGetPoiSearchResultListener == null) {
            throw new IllegalArgumentException("listener can not be null");
        } else {
            this.a.a(onGetPoiSearchResultListener);
        }
    }
}
