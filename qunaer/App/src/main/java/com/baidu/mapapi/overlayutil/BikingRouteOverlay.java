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
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.route.BikingRouteLine;
import com.baidu.mapapi.search.route.BikingRouteLine.BikingStep;
import java.util.ArrayList;
import java.util.List;

public class BikingRouteOverlay extends OverlayManager {
    private BikingRouteLine mRouteLine = null;

    public BikingRouteOverlay(BaiduMap baiduMap) {
        super(baiduMap);
    }

    public void setData(BikingRouteLine bikingRouteLine) {
        this.mRouteLine = bikingRouteLine;
    }

    public final List<OverlayOptions> getOverlayOptions() {
        LatLng latLng = null;
        if (this.mRouteLine == null) {
            return null;
        }
        List<OverlayOptions> arrayList = new ArrayList();
        if (this.mRouteLine.getAllStep() != null && this.mRouteLine.getAllStep().size() > 0) {
            for (BikingStep bikingStep : this.mRouteLine.getAllStep()) {
                Bundle bundle = new Bundle();
                bundle.putInt("index", this.mRouteLine.getAllStep().indexOf(bikingStep));
                if (bikingStep.getEntrance() != null) {
                    arrayList.add(new MarkerOptions().position(bikingStep.getEntrance().getLocation()).rotate((float) (360 - bikingStep.getDirection())).zIndex(10).anchor(0.5f, 0.5f).extraInfo(bundle).icon(BitmapDescriptorFactory.fromAssetWithDpi("Icon_line_node.png")));
                }
                if (this.mRouteLine.getAllStep().indexOf(bikingStep) == this.mRouteLine.getAllStep().size() - 1 && bikingStep.getExit() != null) {
                    arrayList.add(new MarkerOptions().position(bikingStep.getExit().getLocation()).anchor(0.5f, 0.5f).zIndex(10).icon(BitmapDescriptorFactory.fromAssetWithDpi("Icon_line_node.png")));
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
            for (BikingStep bikingStep2 : this.mRouteLine.getAllStep()) {
                LatLng latLng2;
                List wayPoints = bikingStep2.getWayPoints();
                if (wayPoints != null) {
                    List arrayList2 = new ArrayList();
                    if (latLng != null) {
                        arrayList2.add(latLng);
                    }
                    arrayList2.addAll(wayPoints);
                    arrayList.add(new PolylineOptions().points(arrayList2).width(10).color(getLineColor() != 0 ? getLineColor() : Color.argb(178, 0, 78, 255)).zIndex(0));
                    latLng2 = (LatLng) wayPoints.get(wayPoints.size() - 1);
                } else {
                    latLng2 = latLng;
                }
                latLng = latLng2;
            }
        }
        return arrayList;
    }

    public BitmapDescriptor getStartMarker() {
        return null;
    }

    public int getLineColor() {
        return 0;
    }

    public BitmapDescriptor getTerminalMarker() {
        return null;
    }

    public boolean onRouteNodeClick(int i) {
        if (!(this.mRouteLine.getAllStep() == null || this.mRouteLine.getAllStep().get(i) == null)) {
            Log.i("baidumapsdk", "BikingRouteOverlay onRouteNodeClick");
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
