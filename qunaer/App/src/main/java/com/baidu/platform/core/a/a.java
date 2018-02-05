package com.baidu.platform.core.a;

import com.baidu.mapapi.search.district.DistrictSearchOption;
import com.baidu.platform.base.g;
import com.baidu.platform.domain.b;

public class a extends g {
    a(DistrictSearchOption districtSearchOption) {
        a(districtSearchOption);
    }

    private void a(DistrictSearchOption districtSearchOption) {
        if (districtSearchOption != null) {
            this.a.a("qt", "con");
            this.a.a("rp_format", "json");
            this.a.a("rp_filter", "mobile");
            this.a.a("area_res", "true");
            this.a.a("addr_identify", "1");
            this.a.a("ie", "utf-8");
            this.a.a("pn", "0");
            this.a.a("rn", "10");
            this.a.a("c", districtSearchOption.mCityName);
            if (districtSearchOption.mDistrictName == null || districtSearchOption.mDistrictName.equals("")) {
                this.a.a("wd", districtSearchOption.mCityName);
            } else {
                this.a.a("wd", districtSearchOption.mDistrictName);
            }
        }
    }

    public String a(b bVar) {
        return bVar.n();
    }
}
