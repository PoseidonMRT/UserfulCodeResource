package com.baidu.platform.core.busline;

import com.baidu.mapapi.search.busline.BusLineResult;
import com.baidu.platform.base.e;

class d implements e<BusLineResult> {
    final /* synthetic */ c a;

    d(c cVar) {
        this.a = cVar;
    }

    public void a(BusLineResult busLineResult) {
        if (this.a.b != null) {
            this.a.b.onGetBusLineResult(busLineResult);
        }
    }
}
