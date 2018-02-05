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
import com.baidu.mapapi.search.route.DrivingRouteLine;
import com.baidu.mapapi.search.route.DrivingRouteLine.DrivingStep;
import java.util.ArrayList;
import java.util.List;

public class DrivingRouteOverlay extends OverlayManager {
    boolean focus = false;
    private DrivingRouteLine mRouteLine = null;

    public DrivingRouteOverlay(BaiduMap baiduMap) {
        super(baiduMap);
    }

    public final List<OverlayOptions> getOverlayOptions() {
        if (this.mRouteLine == null) {
            return null;
        }
        List<OverlayOptions> arrayList = new ArrayList();
        if (this.mRouteLine.getAllStep() != null && this.mRouteLine.getAllStep().size() > 0) {
            for (DrivingStep drivingStep : this.mRouteLine.getAllStep()) {
                Bundle bundle = new Bundle();
                bundle.putInt("index", this.mRouteLine.getAllStep().indexOf(drivingStep));
                if (drivingStep.getEntrance() != null) {
                    arrayList.add(new MarkerOptions().position(drivingStep.getEntrance().getLocation()).anchor(0.5f, 0.5f).zIndex(10).rotate((float) (360 - drivingStep.getDirection())).extraInfo(bundle).icon(BitmapDescriptorFactory.fromAssetWithDpi("Icon_line_node.png")));
                }
                if (this.mRouteLine.getAllStep().indexOf(drivingStep) == this.mRouteLine.getAllStep().size() - 1 && drivingStep.getExit() != null) {
                    arrayList.add(new MarkerOptions().position(drivingStep.getExit().getLocation()).anchor(0.5f, 0.5f).zIndex(10).icon(BitmapDescriptorFactory.fromAssetWithDpi("Icon_line_node.png")));
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
            boolean z;
            List allStep = this.mRouteLine.getAllStep();
            int size = allStep.size();
            List arrayList2 = new ArrayList();
            List arrayList3 = new ArrayList();
            int i = 0;
            int i2 = 0;
            while (i < size) {
                if (i == size - 1) {
                    arrayList2.addAll(((DrivingStep) allStep.get(i)).getWayPoints());
                } else {
                    arrayList2.addAll(((DrivingStep) allStep.get(i)).getWayPoints().subList(0, ((DrivingStep) allStep.get(i)).getWayPoints().size() - 1));
                }
                int size2 = i2 + (((DrivingStep) allStep.get(i)).getWayPoints().size() - 1);
                if (((DrivingStep) allStep.get(i)).getTrafficList() != null && ((DrivingStep) allStep.get(i)).getTrafficList().length > 0) {
                    for (int valueOf : ((DrivingStep) allStep.get(i)).getTrafficList()) {
                        arrayList3.add(Integer.valueOf(valueOf));
                    }
                }
                i++;
                i2 = size2;
            }
            if (arrayList3 == null || arrayList3.size() <= 0) {
                z = false;
            } else {
                z = true;
            }
            PolylineOptions zIndex = new PolylineOptions().points(arrayList2).textureIndex(arrayList3).width(7).dottedLine(z).focus(true).color(getLineColor() != 0 ? getLineColor() : Color.argb(178, 0, 78, 255)).zIndex(0);
            if (z) {
                zIndex.customTextureList(getCustomTextureList());
            }
            arrayList.add(zIndex);
        }
        return arrayList;
    }

    public void setData(DrivingRouteLine drivingRouteLine) {
        this.mRouteLine = drivingRouteLine;
    }

    public BitmapDescriptor getStartMarker() {
        return null;
    }

    public int getLineColor() {
        return 0;
    }

    public List<BitmapDescriptor> getCustomTextureList() {
        List arrayList = new ArrayList();
        arrayList.add(BitmapDescriptorFactory.fromAsset("Icon_road_blue_arrow.png"));
        arrayList.add(BitmapDescriptorFactory.fromAsset("Icon_road_green_arrow.png"));
        arrayList.add(BitmapDescriptorFactory.fromAsset("Icon_road_yellow_arrow.png"));
        arrayList.add(BitmapDescriptorFactory.fromAsset("Icon_road_red_arrow.png"));
        arrayList.add(BitmapDescriptorFactory.fromAsset("Icon_road_nofocus.png"));
        return arrayList;
    }

    public BitmapDescriptor getTerminalMarker() {
        return null;
    }

    public boolean onRouteNodeClick(int i) {
        if (!(this.mRouteLine.getAllStep() == null || this.mRouteLine.getAllStep().get(i) == null)) {
            Log.i("baidumapsdk", "DrivingRouteOverlay onRouteNodeClick");
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
        boolean z;
        for (Overlay overlay : this.mOverlayList) {
            if ((overlay instanceof Polyline) && overlay.equals(polyline)) {
                z = true;
                break;
            }
        }
        z = false;
        setFocus(z);
        return true;
    }

    public void setFocus(boolean z) {
        this.focus = z;
        for (Overlay overlay : this.mOverlayList) {
            if (overlay instanceof Polyline) {
                ((Polyline) overlay).setFocus(z);
                return;
            }
        }
    }
}
