package com.baidu.platform.core.b;

import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.platform.base.e;

class b implements e<GeoCodeResult> {
    final /* synthetic */ a a;

    b(a aVar) {
        this.a = aVar;
    }

    public void a(GeoCodeResult geoCodeResult) {
        if (this.a.b != null) {
            this.a.b.onGetGeoCodeResult(geoCodeResult);
        }
    }
}
