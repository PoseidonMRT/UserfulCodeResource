package com.baidu.platform.core.e;

import com.baidu.mapapi.search.share.ShareUrlResult;
import com.baidu.platform.base.e;

class h implements e<ShareUrlResult> {
    final /* synthetic */ g a;

    h(g gVar) {
        this.a = gVar;
    }

    public void a(ShareUrlResult shareUrlResult) {
        if (this.a.b != null) {
            this.a.b.onGetPoiDetailShareUrlResult(shareUrlResult);
        }
    }
}
