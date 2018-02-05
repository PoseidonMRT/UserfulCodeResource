package com.baidu.platform.core.d;

import com.baidu.mapapi.CoordType;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.route.MassTransitRoutePlanOption;
import com.baidu.platform.base.g;
import com.baidu.platform.comapi.util.CoordTrans;
import com.baidu.platform.domain.b;

public class i extends g {
    public i(MassTransitRoutePlanOption massTransitRoutePlanOption) {
        a(massTransitRoutePlanOption);
    }

    private void a(MassTransitRoutePlanOption massTransitRoutePlanOption) {
        LatLng location = massTransitRoutePlanOption.mFrom.getLocation();
        if (location != null) {
            if (SDKInitializer.getCoordType() == CoordType.GCJ02) {
                location = CoordTrans.gcjToBaidu(location);
            }
            this.a.a("origin", location.latitude + "," + location.longitude);
        } else {
            this.a.a("origin", massTransitRoutePlanOption.mFrom.getName());
        }
        if (massTransitRoutePlanOption.mFrom.getCity() != null) {
            this.a.a("origin_region", massTransitRoutePlanOption.mFrom.getCity());
        }
        location = massTransitRoutePlanOption.mTo.getLocation();
        if (location != null) {
            if (SDKInitializer.getCoordType() == CoordType.GCJ02) {
                location = CoordTrans.gcjToBaidu(location);
            }
            this.a.a("destination", location.latitude + "," + location.longitude);
        } else {
            this.a.a("destination", massTransitRoutePlanOption.mTo.getName());
        }
        if (massTransitRoutePlanOption.mTo.getCity() != null) {
            this.a.a("destination_region", massTransitRoutePlanOption.mTo.getCity());
        }
        this.a.a("tactics_incity", massTransitRoutePlanOption.mTacticsIncity.getInt() + "");
        this.a.a("tactics_intercity", massTransitRoutePlanOption.mTacticsIntercity.getInt() + "");
        this.a.a("trans_type_intercity", massTransitRoutePlanOption.mTransTypeIntercity.getInt() + "");
        this.a.a("page_index", massTransitRoutePlanOption.mPageIndex + "");
        this.a.a("page_size", massTransitRoutePlanOption.mPageSize + "");
        this.a.a("coord_type", massTransitRoutePlanOption.mCoordType);
        this.a.a("output", "json");
        this.a.a("from", "android_map_sdk");
    }

    public String a(b bVar) {
        return bVar.g();
    }
}
