package com.baidu.platform.core.c;

import com.baidu.mapapi.search.poi.PoiDetailResult;
import com.baidu.platform.base.e;

class j implements e<PoiDetailResult> {
    final /* synthetic */ f a;

    j(f fVar) {
        this.a = fVar;
    }

    public void a(PoiDetailResult poiDetailResult) {
        if (this.a.b != null) {
            this.a.b.onGetPoiDetailResult(poiDetailResult);
        }
    }
}
