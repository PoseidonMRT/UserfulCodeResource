package com.baidu.platform.core.b;

import com.baidu.mapapi.search.geocode.GeoCodeOption;
import com.baidu.platform.base.g;
import com.baidu.platform.domain.b;

public class e extends g {
    public e(GeoCodeOption geoCodeOption) {
        a(geoCodeOption);
    }

    private void a(GeoCodeOption geoCodeOption) {
        this.a.a("qt", "gc");
        this.a.a("cn", geoCodeOption.mCity);
        this.a.a("ie", "utf-8");
        this.a.a("oue", "0");
        this.a.a("wd", geoCodeOption.mAddress);
    }

    public String a(b bVar) {
        return bVar.f();
    }
}
