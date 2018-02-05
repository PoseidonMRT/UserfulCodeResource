package com.baidu.platform.core.d;

import com.baidu.mapapi.search.route.IndoorRouteResult;
import com.baidu.platform.base.e;

class n implements e<IndoorRouteResult> {
    final /* synthetic */ j a;

    n(j jVar) {
        this.a = jVar;
    }

    public void a(IndoorRouteResult indoorRouteResult) {
        if (this.a.b != null) {
            this.a.b.onGetIndoorRouteResult(indoorRouteResult);
        }
    }
}
