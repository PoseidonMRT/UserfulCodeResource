package com.baidu.platform.core.d;

import com.baidu.mapapi.search.route.TransitRoutePlanOption;
import com.baidu.platform.base.g;
import com.baidu.platform.domain.b;
import org.apache.http.cookie.ClientCookie;

public class s extends g {
    public s(TransitRoutePlanOption transitRoutePlanOption) {
        a(transitRoutePlanOption);
    }

    private void a(TransitRoutePlanOption transitRoutePlanOption) {
        this.a.a("qt", "bus");
        this.a.a("sy", transitRoutePlanOption.mPolicy.getInt() + "");
        this.a.a("ie", "utf-8");
        this.a.a("lrn", "20");
        this.a.a(ClientCookie.VERSION_ATTR, "3");
        this.a.a("rp_format", "json");
        this.a.a("rp_filter", "mobile");
        this.a.a("ic_info", "2");
        this.a.a("sn", a(transitRoutePlanOption.mFrom));
        this.a.a("en", a(transitRoutePlanOption.mTo));
        if (transitRoutePlanOption.mCityName != null) {
            this.a.a("c", transitRoutePlanOption.mCityName);
        }
    }

    public String a(b bVar) {
        return bVar.h();
    }
}
