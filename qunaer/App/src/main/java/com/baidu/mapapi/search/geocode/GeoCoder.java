package com.baidu.mapapi.search.geocode;

import com.baidu.mapapi.BMapManager;
import com.baidu.mapapi.search.core.i;
import com.baidu.platform.core.b.a;
import com.baidu.platform.core.b.f;

public class GeoCoder extends i {
    private f a = new a();
    private boolean b;

    private GeoCoder() {
    }

    public static GeoCoder newInstance() {
        BMapManager.init();
        return new GeoCoder();
    }

    public void destroy() {
        if (!this.b) {
            this.b = true;
            this.a.a();
            BMapManager.destroy();
        }
    }

    public boolean geocode(GeoCodeOption geoCodeOption) {
        if (this.a == null) {
            throw new IllegalStateException("GeoCoder is null, please call newInstance() first.");
        } else if (geoCodeOption != null && geoCodeOption.mAddress != null && geoCodeOption.mCity != null) {
            return this.a.a(geoCodeOption);
        } else {
            throw new IllegalArgumentException("option or address or city can not be null");
        }
    }

    public boolean reverseGeoCode(ReverseGeoCodeOption reverseGeoCodeOption) {
        if (this.a == null) {
            throw new IllegalStateException("GeoCoder is null, please call newInstance() first.");
        } else if (reverseGeoCodeOption != null && reverseGeoCodeOption.mLocation != null) {
            return this.a.a(reverseGeoCodeOption);
        } else {
            throw new IllegalArgumentException("option or mLocation can not be null");
        }
    }

    public void setOnGetGeoCodeResultListener(OnGetGeoCoderResultListener onGetGeoCoderResultListener) {
        if (this.a == null) {
            throw new IllegalStateException("GeoCoder is null, please call newInstance() first.");
        } else if (onGetGeoCoderResultListener == null) {
            throw new IllegalArgumentException("listener can not be null");
        } else {
            this.a.a(onGetGeoCoderResultListener);
        }
    }
}
