package com.baidu.mapapi.overlayutil;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.Overlay;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.Polyline;
import com.baidu.mapapi.map.PolylineOptions;
import com.baidu.mapapi.search.route.TransitRouteLine;
import com.baidu.mapapi.search.route.TransitRouteLine.TransitStep;
import com.baidu.mapapi.search.route.TransitRouteLine.TransitStep.TransitRouteStepType;
import java.util.ArrayList;
import java.util.List;

public class TransitRouteOverlay extends OverlayManager {
    private TransitRouteLine mRouteLine = null;

    public TransitRouteOverlay(BaiduMap baiduMap) {
        super(baiduMap);
    }

    public final List<OverlayOptions> getOverlayOptions() {
        if (this.mRouteLine == null) {
            return null;
        }
        List<OverlayOptions> arrayList = new ArrayList();
        if (this.mRouteLine.getAllStep() != null && this.mRouteLine.getAllStep().size() > 0) {
            for (TransitStep transitStep : this.mRouteLine.getAllStep()) {
                Bundle bundle = new Bundle();
                bundle.putInt("index", this.mRouteLine.getAllStep().indexOf(transitStep));
                if (transitStep.getEntrance() != null) {
                    arrayList.add(new MarkerOptions().position(transitStep.getEntrance().getLocation()).anchor(0.5f, 0.5f).zIndex(10).extraInfo(bundle).icon(getIconForStep(transitStep)));
                }
                if (this.mRouteLine.getAllStep().indexOf(transitStep) == this.mRouteLine.getAllStep().size() - 1 && transitStep.getExit() != null) {
                    arrayList.add(new MarkerOptions().position(transitStep.getExit().getLocation()).anchor(0.5f, 0.5f).zIndex(10).icon(getIconForStep(transitStep)));
                }
            }
        }
        if (this.mRouteLine.getStarting() != null) {
            arrayList.add(new MarkerOptions().position(this.mRouteLine.getStarting().getLocation()).icon(getStartMarker() != null ? getStartMarker() : BitmapDescriptorFactory.fromAssetWithDpi("Icon_start.png")).zIndex(10));
        }
        if (this.mRouteLine.getTerminal() != null) {
            BitmapDescriptor terminalMarker;
            MarkerOptions position = new MarkerOptions().position(this.mRouteLine.getTerminal().getLocation());
            if (getTerminalMarker() != null) {
                terminalMarker = getTerminalMarker();
            } else {
                terminalMarker = BitmapDescriptorFactory.fromAssetWithDpi("Icon_end.png");
            }
            arrayList.add(position.icon(terminalMarker).zIndex(10));
        }
        if (this.mRouteLine.getAllStep() != null && this.mRouteLine.getAllStep().size() > 0) {
            for (TransitStep transitStep2 : this.mRouteLine.getAllStep()) {
                if (transitStep2.getWayPoints() != null) {
                    int lineColor = transitStep2.getStepType() != TransitRouteStepType.WAKLING ? getLineColor() != 0 ? getLineColor() : Color.argb(178, 0, 78, 255) : getLineColor() != 0 ? getLineColor() : Color.argb(178, 88, 208, 0);
                    arrayList.add(new PolylineOptions().points(transitStep2.getWayPoints()).width(10).color(lineColor).zIndex(0));
                }
            }
        }
        return arrayList;
    }

    private BitmapDescriptor getIconForStep(TransitStep transitStep) {
        switch (transitStep.getStepType()) {
            case BUSLINE:
                return BitmapDescriptorFactory.fromAssetWithDpi("Icon_bus_station.png");
            case SUBWAY:
                return BitmapDescriptorFactory.fromAssetWithDpi("Icon_subway_station.png");
            case WAKLING:
                return BitmapDescriptorFactory.fromAssetWithDpi("Icon_walk_route.png");
            default:
                return null;
        }
    }

    public void setData(TransitRouteLine transitRouteLine) {
        this.mRouteLine = transitRouteLine;
    }

    public BitmapDescriptor getStartMarker() {
        return null;
    }

    public BitmapDescriptor getTerminalMarker() {
        return null;
    }

    public int getLineColor() {
        return 0;
    }

    public boolean onRouteNodeClick(int i) {
        if (!(this.mRouteLine.getAllStep() == null || this.mRouteLine.getAllStep().get(i) == null)) {
            Log.i("baidumapsdk", "TransitRouteOverlay onRouteNodeClick");
        }
        return false;
    }

    public final boolean onMarkerClick(Marker marker) {
        for (Overlay overlay : this.mOverlayList) {
            if ((overlay instanceof Marker) && overlay.equals(marker) && marker.getExtraInfo() != null) {
                onRouteNodeClick(marker.getExtraInfo().getInt("index"));
            }
        }
        return true;
    }

    public boolean onPolylineClick(Polyline polyline) {
        return false;
    }
}
