package com.baidu.platform.core.b;

import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.baidu.platform.base.e;

class c implements e<ReverseGeoCodeResult> {
    final /* synthetic */ a a;

    c(a aVar) {
        this.a = aVar;
    }

    public void a(ReverseGeoCodeResult reverseGeoCodeResult) {
        if (this.a.b != null) {
            this.a.b.onGetReverseGeoCodeResult(reverseGeoCodeResult);
        }
    }
}
