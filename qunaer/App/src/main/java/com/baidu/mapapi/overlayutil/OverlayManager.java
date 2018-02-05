package com.baidu.mapapi.overlayutil;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BaiduMap.OnMarkerClickListener;
import com.baidu.mapapi.map.BaiduMap.OnPolylineClickListener;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.Overlay;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLngBounds.Builder;
import java.util.ArrayList;
import java.util.List;

public abstract class OverlayManager implements OnMarkerClickListener, OnPolylineClickListener {
    BaiduMap mBaiduMap = null;
    List<Overlay> mOverlayList = null;
    private List<OverlayOptions> mOverlayOptionList = null;

    public abstract List<OverlayOptions> getOverlayOptions();

    public OverlayManager(BaiduMap baiduMap) {
        this.mBaiduMap = baiduMap;
        if (this.mOverlayOptionList == null) {
            this.mOverlayOptionList = new ArrayList();
        }
        if (this.mOverlayList == null) {
            this.mOverlayList = new ArrayList();
        }
    }

    public final void addToMap() {
        if (this.mBaiduMap != null) {
            removeFromMap();
            if (getOverlayOptions() != null) {
                this.mOverlayOptionList.addAll(getOverlayOptions());
            }
            for (OverlayOptions addOverlay : this.mOverlayOptionList) {
                this.mOverlayList.add(this.mBaiduMap.addOverlay(addOverlay));
            }
        }
    }

    public final void removeFromMap() {
        if (this.mBaiduMap != null) {
            for (Overlay remove : this.mOverlayList) {
                remove.remove();
            }
            this.mOverlayOptionList.clear();
            this.mOverlayList.clear();
        }
    }

    public void zoomToSpan() {
        if (this.mBaiduMap != null && this.mOverlayList.size() > 0) {
            Builder builder = new Builder();
            for (Overlay overlay : this.mOverlayList) {
                if (overlay instanceof Marker) {
                    builder.include(((Marker) overlay).getPosition());
                }
            }
            this.mBaiduMap.setMapStatus(MapStatusUpdateFactory.newLatLngBounds(builder.build()));
        }
    }
}
