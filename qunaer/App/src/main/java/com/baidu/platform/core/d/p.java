package com.baidu.platform.core.d;

import com.baidu.mapapi.search.route.BikingRouteResult;
import com.baidu.platform.base.e;

class p implements e<BikingRouteResult> {
    final /* synthetic */ j a;

    p(j jVar) {
        this.a = jVar;
    }

    public void a(BikingRouteResult bikingRouteResult) {
        if (this.a.b != null) {
            this.a.b.onGetBikingRouteResult(bikingRouteResult);
        }
    }
}
