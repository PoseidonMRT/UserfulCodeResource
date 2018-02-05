package com.baidu.platform.core.d;

import com.baidu.mapapi.search.route.TransitRouteResult;
import com.baidu.platform.base.e;

class k implements e<TransitRouteResult> {
    final /* synthetic */ j a;

    k(j jVar) {
        this.a = jVar;
    }

    public void a(TransitRouteResult transitRouteResult) {
        if (this.a.b != null) {
            this.a.b.onGetTransitRouteResult(transitRouteResult);
        }
    }
}
