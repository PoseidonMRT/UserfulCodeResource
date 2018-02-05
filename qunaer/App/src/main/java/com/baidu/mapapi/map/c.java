package com.baidu.mapapi.map;

import android.os.Bundle;
import com.baidu.platform.comapi.map.q;

class c implements q {
    final /* synthetic */ BaiduMap a;

    c(BaiduMap baiduMap) {
        this.a = baiduMap;
    }

    public Bundle a(int i, int i2, int i3) {
        this.a.E.lock();
        try {
            if (this.a.D != null) {
                Tile a = this.a.D.a(i, i2, i3);
                if (a != null) {
                    Bundle toBundle = a.toBundle();
                    return toBundle;
                }
            }
            this.a.E.unlock();
            return null;
        } finally {
            this.a.E.unlock();
        }
    }
}
