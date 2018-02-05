package com.baidu.platform.core.c;

import com.baidu.mapapi.search.poi.PoiResult;
import com.baidu.platform.base.e;

class i implements e<PoiResult> {
    final /* synthetic */ f a;

    i(f fVar) {
        this.a = fVar;
    }

    public void a(PoiResult poiResult) {
        if (this.a.b != null) {
            this.a.b.onGetPoiResult(poiResult);
        }
    }
}
