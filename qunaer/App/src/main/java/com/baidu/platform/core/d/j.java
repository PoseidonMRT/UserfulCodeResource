package com.baidu.platform.core.d;

import com.baidu.mapapi.search.route.BikingRoutePlanOption;
import com.baidu.mapapi.search.route.DrivingRoutePlanOption;
import com.baidu.mapapi.search.route.IndoorRoutePlanOption;
import com.baidu.mapapi.search.route.MassTransitRoutePlanOption;
import com.baidu.mapapi.search.route.OnGetRoutePlanResultListener;
import com.baidu.mapapi.search.route.TransitRoutePlanOption;
import com.baidu.mapapi.search.route.WalkingRoutePlanOption;
import com.baidu.platform.base.SearchType;
import com.baidu.platform.base.a;

public class j extends a implements e {
    OnGetRoutePlanResultListener b = null;

    public void a() {
        this.b = null;
    }

    public void a(OnGetRoutePlanResultListener onGetRoutePlanResultListener) {
        this.b = onGetRoutePlanResultListener;
    }

    public boolean a(BikingRoutePlanOption bikingRoutePlanOption) {
        this.a = new a();
        this.a.a(new p(this));
        this.a.a(SearchType.BIKE_ROUTE);
        return a(new b(bikingRoutePlanOption));
    }

    public boolean a(DrivingRoutePlanOption drivingRoutePlanOption) {
        this.a = new c();
        this.a.a(new o(this));
        this.a.a(SearchType.DRIVE_ROUTE);
        return a(new d(drivingRoutePlanOption));
    }

    public boolean a(IndoorRoutePlanOption indoorRoutePlanOption) {
        this.a = new f();
        this.a.a(new n(this));
        this.a.a(SearchType.INDOOR_ROUTE);
        return a(new g(indoorRoutePlanOption));
    }

    public boolean a(MassTransitRoutePlanOption massTransitRoutePlanOption) {
        this.a = new h();
        this.a.a(new l(this));
        this.a.a(SearchType.MASS_TRANSIT_ROUTE);
        return a(new i(massTransitRoutePlanOption));
    }

    public boolean a(TransitRoutePlanOption transitRoutePlanOption) {
        this.a = new r();
        this.a.a(new k(this));
        this.a.a(SearchType.TRANSIT_ROUTE);
        return a(new s(transitRoutePlanOption));
    }

    public boolean a(WalkingRoutePlanOption walkingRoutePlanOption) {
        this.a = new t();
        this.a.a(new m(this));
        this.a.a(SearchType.WALK_ROUTE);
        return a(new u(walkingRoutePlanOption));
    }
}
