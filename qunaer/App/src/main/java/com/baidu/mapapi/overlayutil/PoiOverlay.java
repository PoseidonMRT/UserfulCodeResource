package com.baidu.mapapi.overlayutil;

import android.os.Bundle;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.Polyline;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.poi.PoiResult;
import java.util.ArrayList;
import java.util.List;

public class PoiOverlay extends OverlayManager {
    private static final int MAX_POI_SIZE = 10;
    private PoiResult mPoiResult = null;

    public PoiOverlay(BaiduMap baiduMap) {
        super(baiduMap);
    }

    public void setData(PoiResult poiResult) {
        this.mPoiResult = poiResult;
    }

    public final List<OverlayOptions> getOverlayOptions() {
        if (this.mPoiResult == null || this.mPoiResult.getAllPoi() == null) {
            return null;
        }
        List<OverlayOptions> arrayList = new ArrayList();
        int i = 0;
        for (int i2 = 0; i2 < this.mPoiResult.getAllPoi().size() && i < 10; i2++) {
            if (((PoiInfo) this.mPoiResult.getAllPoi().get(i2)).location != null) {
                i++;
                Bundle bundle = new Bundle();
                bundle.putInt("index", i2);
                arrayList.add(new MarkerOptions().icon(BitmapDescriptorFactory.fromAssetWithDpi("Icon_mark" + i + ".png")).extraInfo(bundle).position(((PoiInfo) this.mPoiResult.getAllPoi().get(i2)).location));
            }
        }
        return arrayList;
    }

    public PoiResult getPoiResult() {
        return this.mPoiResult;
    }

    public boolean onPoiClick(int i) {
        return false;
    }

    public final boolean onMarkerClick(Marker marker) {
        if (this.mOverlayList.contains(marker) && marker.getExtraInfo() != null) {
            return onPoiClick(marker.getExtraInfo().getInt("index"));
        }
        return false;
    }

    public boolean onPolylineClick(Polyline polyline) {
        return false;
    }
}
