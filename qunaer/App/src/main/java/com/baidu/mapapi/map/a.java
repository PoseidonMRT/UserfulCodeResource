package com.baidu.mapapi.map;

import android.os.Bundle;

class a implements a {
    final /* synthetic */ BaiduMap a;

    a(BaiduMap baiduMap) {
        this.a = baiduMap;
    }

    public void a(Overlay overlay) {
        if (overlay != null && this.a.k.contains(overlay)) {
            Bundle a = overlay.a();
            if (this.a.i != null) {
                this.a.i.d(a);
            }
            this.a.k.remove(overlay);
        }
        if (overlay != null && this.a.m.contains(overlay)) {
            this.a.m.remove(overlay);
        }
        if (overlay != null && this.a.l.contains(overlay)) {
            Marker marker = (Marker) overlay;
            if (marker.n != null) {
                this.a.l.remove(marker);
                if (this.a.l.size() == 0 && this.a.i != null) {
                    this.a.i.b(false);
                }
            }
        }
    }

    public void b(Overlay overlay) {
        if (overlay != null && this.a.k.contains(overlay)) {
            if (overlay instanceof Marker) {
                Marker marker = (Marker) overlay;
                if (!(marker.n == null || marker.n.size() == 0)) {
                    if (this.a.l.contains(marker)) {
                        this.a.l.remove(marker);
                    }
                    this.a.l.add(marker);
                    if (this.a.i != null) {
                        this.a.i.b(true);
                    }
                }
            }
            Bundle bundle = new Bundle();
            if (this.a.i != null) {
                this.a.i.c(overlay.a(bundle));
            }
        }
        if (this.a.m.contains(overlay)) {
            this.a.m.remove(overlay);
        }
        if (overlay instanceof Marker) {
            this.a.m.add((Marker) overlay);
        }
    }
}
