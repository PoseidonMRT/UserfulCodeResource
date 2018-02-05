package com.baidu.platform.core.a;

import com.baidu.mapapi.search.core.SearchResult.ERRORNO;
import com.baidu.mapapi.search.district.DistrictResult;

class e implements com.baidu.platform.base.e<DistrictResult> {
    final /* synthetic */ d a;

    e(d dVar) {
        this.a = dVar;
    }

    public void a(DistrictResult districtResult) {
        if ((districtResult == null || districtResult.error == ERRORNO.RESULT_NOT_FOUND) && this.a.b) {
            this.a.b = false;
            String cityName = districtResult.getCityName();
            if (!(cityName == null || cityName.equals(""))) {
                this.a.a(cityName);
                this.a.c = districtResult;
                return;
            }
        }
        if (!(this.a.b || this.a.c == null || districtResult.error != ERRORNO.NO_ERROR)) {
            districtResult.setCityCode(this.a.c.getCityCode());
            districtResult.setCenterPt(this.a.c.getCenterPt());
        }
        this.a.b = true;
        this.a.c = null;
        if (this.a.d != null) {
            this.a.d.onGetDistrictResult(districtResult);
        }
    }
}
