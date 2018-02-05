package com.baidu.mapapi.map;

import android.content.Context;
import android.os.Bundle;
import com.baidu.platform.comapi.map.C0000L;

class d implements C0000L {
    final /* synthetic */ BaiduMap a;

    d(BaiduMap baiduMap) {
        this.a = baiduMap;
    }

    public Bundle a(int i, int i2, int i3, Context context) {
        this.a.F.lock();
        try {
            if (this.a.C != null) {
                Tile a = this.a.C.a(i, i2, i3);
                if (a != null) {
                    Bundle toBundle = a.toBundle();
                    return toBundle;
                }
            }
            this.a.F.unlock();
            return null;
        } finally {
            this.a.F.unlock();
        }
    }
}
