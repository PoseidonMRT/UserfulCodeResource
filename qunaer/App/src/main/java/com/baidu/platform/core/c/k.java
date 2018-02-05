package com.baidu.platform.core.c;

import com.baidu.mapapi.search.poi.PoiIndoorResult;
import com.baidu.platform.base.e;

class k implements e<PoiIndoorResult> {
    final /* synthetic */ f a;

    k(f fVar) {
        this.a = fVar;
    }

    public void a(PoiIndoorResult poiIndoorResult) {
        if (this.a.b != null) {
            this.a.b.onGetPoiIndoorResult(poiIndoorResult);
        }
    }
}
