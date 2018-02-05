package com.baidu.mapapi.overlayutil;

import android.graphics.Color;
import android.util.Log;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.Polyline;
import com.baidu.mapapi.map.PolylineOptions;
import com.baidu.mapapi.search.busline.BusLineResult;
import com.baidu.mapapi.search.busline.BusLineResult.BusStation;
import com.baidu.mapapi.search.busline.BusLineResult.BusStep;
import java.util.ArrayList;
import java.util.List;

public class BusLineOverlay extends OverlayManager {
    private BusLineResult mBusLineResult = null;

    public BusLineOverlay(BaiduMap baiduMap) {
        super(baiduMap);
    }

    public void setData(BusLineResult busLineResult) {
        this.mBusLineResult = busLineResult;
    }

    public final List<OverlayOptions> getOverlayOptions() {
        if (this.mBusLineResult == null || this.mBusLineResult.getStations() == null) {
            return null;
        }
        List<OverlayOptions> arrayList = new ArrayList();
        for (BusStation location : this.mBusLineResult.getStations()) {
            arrayList.add(new MarkerOptions().position(location.getLocation()).zIndex(10).anchor(0.5f, 0.5f).icon(BitmapDescriptorFactory.fromAssetWithDpi("Icon_bus_station.png")));
        }
        List arrayList2 = new ArrayList();
        for (BusStep busStep : this.mBusLineResult.getSteps()) {
            if (busStep.getWayPoints() != null) {
                arrayList2.addAll(busStep.getWayPoints());
            }
        }
        if (arrayList2.size() > 0) {
            arrayList.add(new PolylineOptions().width(10).color(Color.argb(178, 0, 78, 255)).zIndex(0).points(arrayList2));
        }
        return arrayList;
    }

    public boolean onBusStationClick(int i) {
        if (!(this.mBusLineResult.getStations() == null || this.mBusLineResult.getStations().get(i) == null)) {
            Log.i("baidumapsdk", "BusLineOverlay onBusStationClick");
        }
        return false;
    }

    public final boolean onMarkerClick(Marker marker) {
        if (this.mOverlayList == null || !this.mOverlayList.contains(marker)) {
            return false;
        }
        return onBusStationClick(this.mOverlayList.indexOf(marker));
    }

    public boolean onPolylineClick(Polyline polyline) {
        return false;
    }
}
