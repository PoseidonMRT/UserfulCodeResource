package com.baidu.platform.core.d;

import com.baidu.mapapi.search.route.DrivingRouteResult;
import com.baidu.platform.base.e;

class o implements e<DrivingRouteResult> {
    final /* synthetic */ j a;

    o(j jVar) {
        this.a = jVar;
    }

    public void a(DrivingRouteResult drivingRouteResult) {
        if (this.a.b != null) {
            this.a.b.onGetDrivingRouteResult(drivingRouteResult);
        }
    }
}
