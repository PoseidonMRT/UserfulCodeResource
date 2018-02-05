package com.baidu.platform.core.e;

import com.baidu.mapapi.search.share.ShareUrlResult;
import com.baidu.platform.base.e;

class i implements e<ShareUrlResult> {
    final /* synthetic */ g a;

    i(g gVar) {
        this.a = gVar;
    }

    public void a(ShareUrlResult shareUrlResult) {
        if (this.a.b != null) {
            this.a.b.onGetLocationShareUrlResult(shareUrlResult);
        }
    }
}
