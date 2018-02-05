package qunar.sdk.mapapi.gaodeMapImp;

import android.view.View;
import java.util.List;
import qunar.sdk.location.QLocation;
import qunar.sdk.mapapi.QunarInfoWindow;
import qunar.sdk.mapapi.QunarMap;
import qunar.sdk.mapapi.entity.QMarker;
import qunar.sdk.mapapi.listener.CustomMapCallback;
import qunar.sdk.mapapi.listener.MapClickListener;
import qunar.sdk.mapapi.listener.MapLoadedCallback;
import qunar.sdk.mapapi.listener.MapLongClickListener;
import qunar.sdk.mapapi.listener.MapStatusChangeListener;
import qunar.sdk.mapapi.listener.MarkerClickListener;
import qunar.sdk.mapapi.listener.MyLocationClickListener;
import qunar.sdk.mapapi.utils.MapConstant.TEMPTOKEN;

public class GaodeMapStrategy implements QunarMap {
    public void setOnMapClickListener(MapClickListener mapClickListener) {
    }

    public void setOnMapLongClickListener(MapLongClickListener mapLongClickListener) {
    }

    public void setOnMarkerClickListener(MarkerClickListener markerClickListener) {
    }

    public void addMarker(QMarker qMarker) {
    }

    public void isAutoMarkersZoom(boolean z) {
    }

    public void addMarkers(List<QMarker> list, boolean z) {
    }

    public void removeMarker(QMarker qMarker) {
    }

    public void removeMarkers(List<QMarker> list) {
    }

    public void hideInfoWindow() {
    }

    public void showInfoWindow(QunarInfoWindow qunarInfoWindow) {
    }

    public void showCustomInfoWindow(View view, QLocation qLocation, int i) {
    }

    public void setOnMapLoadedCallback(MapLoadedCallback mapLoadedCallback) {
    }

    public void setOnMapStatusChangeListener(MapStatusChangeListener mapStatusChangeListener) {
    }

    public boolean isMapLoaded() {
        return false;
    }

    public void addMyLocationData(QLocation qLocation) {
    }

    public void clear() {
    }

    public QLocation getMapCenter() {
        return null;
    }

    public float getZoomLevel() {
        return 3.0f;
    }

    public void twoPoint2Line(QMarker qMarker, QMarker qMarker2, int i, int i2) {
    }

    public void drawPolyline(List<QLocation> list, int i, int i2, QMarker qMarker, QMarker qMarker2) {
    }

    public void drawPolyline(List<QLocation> list, List<QMarker> list2, int i, int i2, int i3, int i4, CustomMapCallback... customMapCallbackArr) {
    }

    public void drawPolygon(List<QLocation> list, int i, int i2, int i3, String str) {
    }

    public void updatePolygon(int i, int i2, int i3, String str) {
    }

    public void removePolygon(String str) {
    }

    public void mapBound(List<QLocation> list, int i, int i2, CustomMapCallback... customMapCallbackArr) {
    }

    public void addMarkers(List<QMarker> list, boolean z, int i, int i2) {
    }

    public void mapCenterTo(int i, int i2) {
    }

    public void reSetMarkPosition(QMarker qMarker, QLocation qLocation) {
    }

    public void reSetMarkRotate(QMarker qMarker, float f) {
    }

    public void setOnMyLocationClickListener(MyLocationClickListener myLocationClickListener) {
    }

    public void setViewPadding(int i, int i2, int i3, int i4) {
    }

    @Deprecated
    public void destroy(String str) {
        if (str != null && !TEMPTOKEN.MAP_DESTROY_TOKEN.equals(str)) {
        }
    }
}
