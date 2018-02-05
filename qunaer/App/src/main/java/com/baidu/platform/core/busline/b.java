package com.baidu.platform.core.busline;

import com.baidu.mapapi.search.busline.BusLineSearchOption;
import com.baidu.platform.base.g;
import com.iflytek.aiui.AIUIConstant;

public class b extends g {
    public b(BusLineSearchOption busLineSearchOption) {
        a(busLineSearchOption);
    }

    private void a(BusLineSearchOption busLineSearchOption) {
        this.a.a("qt", "bsl");
        this.a.a("rt_info", "1");
        this.a.a("ie", "utf-8");
        this.a.a("oue", "0");
        this.a.a("c", busLineSearchOption.mCity);
        this.a.a(AIUIConstant.KEY_UID, busLineSearchOption.mUid);
        this.a.a("t", System.currentTimeMillis() + "");
    }

    public String a(com.baidu.platform.domain.b bVar) {
        return bVar.m();
    }
}
