package com.baidu.platform.core.b;

import android.net.http.Headers;
import com.baidu.mapapi.CoordType;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.platform.base.g;
import com.baidu.platform.comapi.util.CoordTrans;
import com.baidu.platform.domain.b;

public class h extends g {
    public h(ReverseGeoCodeOption reverseGeoCodeOption) {
        a(reverseGeoCodeOption);
    }

    private void a(ReverseGeoCodeOption reverseGeoCodeOption) {
        if (reverseGeoCodeOption.mLocation != null) {
            LatLng latLng = new LatLng(reverseGeoCodeOption.mLocation.latitude, reverseGeoCodeOption.mLocation.longitude);
            if (SDKInitializer.getCoordType() == CoordType.GCJ02) {
                latLng = CoordTrans.gcjToBaidu(latLng);
            }
            this.a.a(Headers.LOCATION, latLng.latitude + "," + latLng.longitude);
        }
        this.a.a("coordtype", "bd09ll");
        this.a.a("pois", "1");
        this.a.a("output", "json");
        this.a.a("from", "android_map_sdk");
    }

    public String a(b bVar) {
        return bVar.e();
    }
}
