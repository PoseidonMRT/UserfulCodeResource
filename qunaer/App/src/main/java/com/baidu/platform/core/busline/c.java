package com.baidu.platform.core.busline;

import com.baidu.mapapi.search.busline.BusLineSearchOption;
import com.baidu.mapapi.search.busline.OnGetBusLineSearchResultListener;
import com.baidu.platform.base.SearchType;
import com.baidu.platform.base.a;

public class c extends a implements IBusLineSearch {
    OnGetBusLineSearchResultListener b = null;

    public void a() {
        this.b = null;
    }

    public void a(OnGetBusLineSearchResultListener onGetBusLineSearchResultListener) {
        this.b = onGetBusLineSearchResultListener;
    }

    public boolean a(BusLineSearchOption busLineSearchOption) {
        this.a = new a();
        this.a.a(SearchType.BUS_LINE_DETAIL);
        this.a.a(new d(this));
        return a(new b(busLineSearchOption));
    }
}
