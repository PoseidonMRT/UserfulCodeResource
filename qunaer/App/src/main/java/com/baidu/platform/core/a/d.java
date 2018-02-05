package com.baidu.platform.core.a;

import com.baidu.mapapi.search.district.DistrictResult;
import com.baidu.mapapi.search.district.DistrictSearchOption;
import com.baidu.mapapi.search.district.OnGetDistricSearchResultListener;
import com.baidu.platform.base.SearchType;
import com.baidu.platform.base.a;

public class d extends a implements f {
    boolean b = true;
    DistrictResult c = null;
    private OnGetDistricSearchResultListener d;

    private boolean a(String str) {
        ((b) this.a).b();
        return a(new c(str));
    }

    public void a() {
        this.d = null;
    }

    public void a(OnGetDistricSearchResultListener onGetDistricSearchResultListener) {
        this.d = onGetDistricSearchResultListener;
    }

    public boolean a(DistrictSearchOption districtSearchOption) {
        this.a = new b();
        this.a.a(SearchType.DISTRICT_SEARCH);
        this.a.a(new e(this));
        return a(new a(districtSearchOption));
    }
}
