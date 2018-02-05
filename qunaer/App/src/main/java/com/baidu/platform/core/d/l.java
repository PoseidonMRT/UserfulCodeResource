package com.baidu.platform.core.d;

import com.baidu.mapapi.search.route.MassTransitRouteResult;
import com.baidu.platform.base.e;

class l implements e<MassTransitRouteResult> {
    final /* synthetic */ j a;

    l(j jVar) {
        this.a = jVar;
    }

    public void a(MassTransitRouteResult massTransitRouteResult) {
        if (this.a.b != null) {
            this.a.b.onGetMassTransitRouteResult(massTransitRouteResult);
        }
    }
}
