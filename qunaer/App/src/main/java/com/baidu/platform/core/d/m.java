package com.baidu.platform.core.d;

import com.baidu.mapapi.search.route.WalkingRouteResult;
import com.baidu.platform.base.e;

class m implements e<WalkingRouteResult> {
    final /* synthetic */ j a;

    m(j jVar) {
        this.a = jVar;
    }

    public void a(WalkingRouteResult walkingRouteResult) {
        if (this.a.b != null) {
            this.a.b.onGetWalkingRouteResult(walkingRouteResult);
        }
    }
}
