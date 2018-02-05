package com.baidu.mapapi.map;

import android.view.View;
import android.view.View.OnClickListener;
import com.baidu.platform.comapi.map.E;

class v implements OnClickListener {
    final /* synthetic */ WearMapView a;

    v(WearMapView wearMapView) {
        this.a = wearMapView;
    }

    public void onClick(View view) {
        E D = this.a.d.a().D();
        D.a -= 1.0f;
        this.a.d.a().a(D, 300);
    }
}
