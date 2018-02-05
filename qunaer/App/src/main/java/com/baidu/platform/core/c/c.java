package com.baidu.platform.core.c;

import com.baidu.mapapi.search.poi.PoiIndoorOption;
import com.baidu.platform.base.g;
import com.baidu.platform.domain.b;

public class c extends g {
    public c(PoiIndoorOption poiIndoorOption) {
        a(poiIndoorOption);
    }

    private void a(PoiIndoorOption poiIndoorOption) {
        this.a.a("qt", "indoor_s");
        this.a.a(MapViewConstants.ATTR_X, "0");
        this.a.a(MapViewConstants.ATTR_Y, "0");
        this.a.a("from", "android_map_sdk");
        String str = poiIndoorOption.bid;
        if (!(str == null || str.equals(""))) {
            this.a.a("bid", str);
        }
        str = poiIndoorOption.wd;
        if (!(str == null || str.equals(""))) {
            this.a.a("wd", str);
        }
        str = poiIndoorOption.floor;
        if (!(str == null || str.equals(""))) {
            this.a.a("floor", str);
        }
        this.a.a("current", poiIndoorOption.currentPage + "");
        this.a.a("pageSize", poiIndoorOption.pageSize + "");
    }

    public String a(b bVar) {
        return bVar.c();
    }
}
