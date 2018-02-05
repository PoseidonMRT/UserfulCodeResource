package com.baidu.mapapi.map;

import android.view.View;
import android.view.View.OnClickListener;
import com.baidu.platform.comapi.map.E;

class r implements OnClickListener {
    final /* synthetic */ TextureMapView a;

    r(TextureMapView textureMapView) {
        this.a = textureMapView;
    }

    public void onClick(View view) {
        E D = this.a.b.b().D();
        D.a -= 1.0f;
        this.a.b.b().a(D, 300);
    }
}
