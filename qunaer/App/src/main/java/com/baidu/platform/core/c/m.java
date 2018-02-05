package com.baidu.platform.core.c;

import com.baidu.mapapi.model.CoordUtil;
import com.baidu.mapapi.model.inner.Point;
import com.baidu.mapapi.search.poi.PoiBoundSearchOption;
import com.baidu.mapapi.search.poi.PoiCitySearchOption;
import com.baidu.mapapi.search.poi.PoiNearbySearchOption;
import com.baidu.mapapi.search.poi.PoiSortType;
import com.baidu.platform.base.g;
import com.baidu.platform.domain.b;
import com.iflytek.speech.VoiceWakeuperAidl;

public class m extends g {
    m(PoiBoundSearchOption poiBoundSearchOption) {
        a(poiBoundSearchOption);
    }

    m(PoiCitySearchOption poiCitySearchOption) {
        a(poiCitySearchOption);
    }

    m(PoiNearbySearchOption poiNearbySearchOption) {
        a(poiNearbySearchOption);
    }

    private void a(PoiBoundSearchOption poiBoundSearchOption) {
        this.a.a("qt", "bd2");
        this.a.a("rp_format", "json");
        this.a.a("rp_filter", "mobile");
        this.a.a("ie", "utf-8");
        this.a.a("pn", poiBoundSearchOption.mPageNum + "");
        this.a.a("rn", poiBoundSearchOption.mPageCapacity + "");
        this.a.a("wd", poiBoundSearchOption.mKeyword);
        Point ll2point = CoordUtil.ll2point(poiBoundSearchOption.mBound.northeast);
        Point ll2point2 = CoordUtil.ll2point(poiBoundSearchOption.mBound.southwest);
        this.a.a("ar", "(" + ll2point.x + "," + ll2point.y + VoiceWakeuperAidl.PARAMS_SEPARATE + ll2point2.x + "," + ll2point2.y + ")");
        this.a.a("l", "12");
        this.a.a("b", "(" + ll2point.x + "," + ll2point.y + VoiceWakeuperAidl.PARAMS_SEPARATE + ll2point2.x + "," + ll2point2.y + ")");
    }

    private void a(PoiCitySearchOption poiCitySearchOption) {
        this.a.a("qt", "con");
        this.a.a("rp_format", "json");
        this.a.a("rp_filter", "mobile");
        this.a.a("ie", "utf-8");
        if (poiCitySearchOption.mIsReturnAddr) {
            this.a.a("addr_identify", "1");
        } else {
            this.a.a("addr_identify", "0");
        }
        this.a.a("c", poiCitySearchOption.mCity);
        this.a.a("pn", poiCitySearchOption.mPageNum + "");
        this.a.a("rn", poiCitySearchOption.mPageCapacity + "");
        this.a.a("l", "12");
        this.a.a("b", "(0,0;0,0)");
        this.a.a("wd", poiCitySearchOption.mKeyword);
    }

    private void a(PoiNearbySearchOption poiNearbySearchOption) {
        this.a.a("qt", "bd2");
        this.a.a("rp_format", "json");
        this.a.a("rp_filter", "mobile");
        this.a.a("ie", "utf-8");
        this.a.a("pn", poiNearbySearchOption.mPageNum + "");
        this.a.a("rn", poiNearbySearchOption.mPageCapacity + "");
        this.a.a("wd", poiNearbySearchOption.mKeyword);
        if (poiNearbySearchOption.sortType == PoiSortType.distance_from_near_to_far) {
            this.a.a("pl_sort_type", "distance");
        }
        Point ll2point = CoordUtil.ll2point(poiNearbySearchOption.mLocation);
        Point point = new Point(ll2point.x - poiNearbySearchOption.mRadius, ll2point.y - poiNearbySearchOption.mRadius);
        Point point2 = new Point(ll2point.x + poiNearbySearchOption.mRadius, ll2point.y + poiNearbySearchOption.mRadius);
        this.a.a("ar", "(" + point.x + "," + point.y + VoiceWakeuperAidl.PARAMS_SEPARATE + point2.x + "," + point2.y + ")");
        this.a.a("l", "12");
        this.a.a("b", "(" + point.x + "," + point.y + VoiceWakeuperAidl.PARAMS_SEPARATE + point2.x + "," + point2.y + ")");
        this.a.a("distance", poiNearbySearchOption.mRadius + "");
        this.a.a("center_rank", "1");
        this.a.a("loc", "(" + ll2point.x + "," + ll2point.y + ")");
    }

    public String a(b bVar) {
        return bVar.a();
    }
}
