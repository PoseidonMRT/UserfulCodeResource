package com.baidu.mapapi.search.busline;

import com.baidu.mapapi.BMapManager;
import com.baidu.mapapi.search.core.i;
import com.baidu.platform.core.busline.IBusLineSearch;
import com.baidu.platform.core.busline.c;

public class BusLineSearch extends i {
    IBusLineSearch a = new c();
    private boolean b = false;

    BusLineSearch() {
    }

    public static BusLineSearch newInstance() {
        BMapManager.init();
        return new BusLineSearch();
    }

    public void destroy() {
        if (!this.b) {
            this.b = true;
            this.a.a();
            BMapManager.destroy();
        }
    }

    public boolean searchBusLine(BusLineSearchOption busLineSearchOption) {
        if (this.a == null) {
            throw new IllegalStateException("searcher is null, please call newInstance first.");
        } else if (busLineSearchOption != null && busLineSearchOption.mCity != null && busLineSearchOption.mUid != null) {
            return this.a.a(busLineSearchOption);
        } else {
            throw new IllegalArgumentException("option or city or uid can not be null");
        }
    }

    public void setOnGetBusLineSearchResultListener(OnGetBusLineSearchResultListener onGetBusLineSearchResultListener) {
        if (this.a == null) {
            throw new IllegalStateException("searcher is null, please call newInstance first.");
        } else if (onGetBusLineSearchResultListener == null) {
            throw new IllegalArgumentException("listener can not be null");
        } else {
            this.a.a(onGetBusLineSearchResultListener);
        }
    }
}
