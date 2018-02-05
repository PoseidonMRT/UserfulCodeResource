package com.baidu.platform.core.c;

import com.baidu.mapapi.search.poi.PoiDetailSearchOption;
import com.baidu.platform.base.g;
import com.baidu.platform.domain.b;
import com.iflytek.aiui.AIUIConstant;

public class e extends g {
    e(PoiDetailSearchOption poiDetailSearchOption) {
        a(poiDetailSearchOption);
    }

    public String a(b bVar) {
        return bVar.b();
    }

    void a(PoiDetailSearchOption poiDetailSearchOption) {
        this.a.a(AIUIConstant.KEY_UID, poiDetailSearchOption.mUid);
        this.a.a("output", "json");
        this.a.a("scope", "2");
    }
}
