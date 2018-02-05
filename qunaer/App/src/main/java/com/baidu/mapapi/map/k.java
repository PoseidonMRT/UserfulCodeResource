package com.baidu.mapapi.map;

import android.view.View;
import android.view.View.OnClickListener;
import com.baidu.platform.comapi.map.E;

class k implements OnClickListener {
    final /* synthetic */ MapView a;

    k(MapView mapView) {
        this.a = mapView;
    }

    public void onClick(View view) {
        E D = this.a.c.a().D();
        D.a += 1.0f;
        this.a.c.a().a(D, 300);
    }
}
