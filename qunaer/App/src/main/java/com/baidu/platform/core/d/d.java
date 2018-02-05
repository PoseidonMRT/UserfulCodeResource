package com.baidu.platform.core.d;

import com.baidu.mapapi.search.route.DrivingRoutePlanOption;
import com.baidu.mapapi.search.route.PlanNode;
import com.baidu.platform.base.g;
import com.baidu.platform.domain.b;
import java.util.List;
import org.apache.http.cookie.ClientCookie;

public class d extends g {
    d(DrivingRoutePlanOption drivingRoutePlanOption) {
        a(drivingRoutePlanOption);
    }

    private void a(DrivingRoutePlanOption drivingRoutePlanOption) {
        this.a.a("qt", "cars");
        this.a.a("sy", drivingRoutePlanOption.mPolicy.getInt() + "");
        this.a.a("ie", "utf-8");
        this.a.a("lrn", "20");
        this.a.a(ClientCookie.VERSION_ATTR, "6");
        this.a.a("extinfo", "32");
        this.a.a("mrs", "1");
        this.a.a("rp_format", "json");
        this.a.a("rp_filter", "mobile");
        this.a.a("route_traffic", drivingRoutePlanOption.mtrafficPolicy.getInt() + "");
        this.a.a("sn", a(drivingRoutePlanOption.mFrom));
        this.a.a("en", a(drivingRoutePlanOption.mTo));
        if (drivingRoutePlanOption.mCityName != null) {
            this.a.a("c", drivingRoutePlanOption.mCityName);
        }
        if (drivingRoutePlanOption.mFrom != null) {
            this.a.a("sc", drivingRoutePlanOption.mFrom.getCity());
        }
        if (drivingRoutePlanOption.mTo != null) {
            this.a.a("ec", drivingRoutePlanOption.mTo.getCity());
        }
        List list = drivingRoutePlanOption.mWayPoints;
        String str = new String();
        String str2 = new String();
        if (list != null) {
            String str3 = str;
            str = str2;
            for (int i = 0; i < list.size(); i++) {
                PlanNode planNode = (PlanNode) list.get(i);
                if (planNode != null) {
                    str3 = str3 + a(planNode);
                    str = str + planNode.getCity();
                    if (i != list.size() - 1) {
                        str3 = str3 + "|";
                        str = str + "|";
                    }
                }
            }
            this.a.a("wp", str3);
            this.a.a("wpc", str);
        }
    }

    public String a(b bVar) {
        return bVar.i();
    }
}
