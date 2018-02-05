package qunar.sdk.mapapi;

import android.view.View;
import java.util.List;
import qunar.sdk.location.QLocation;
import qunar.sdk.mapapi.entity.QMarker;
import qunar.sdk.mapapi.listener.CustomMapCallback;
import qunar.sdk.mapapi.listener.MapClickListener;
import qunar.sdk.mapapi.listener.MapLoadedCallback;
import qunar.sdk.mapapi.listener.MapLongClickListener;
import qunar.sdk.mapapi.listener.MapStatusChangeListener;
import qunar.sdk.mapapi.listener.MarkerClickListener;
import qunar.sdk.mapapi.listener.MyLocationClickListener;

public interface QunarMap {
    void addMarker(QMarker qMarker);

    void addMarkers(List<QMarker> list, boolean z);

    void addMarkers(List<QMarker> list, boolean z, int i, int i2);

    void addMyLocationData(QLocation qLocation);

    void clear();

    @Deprecated
    void destroy(String str);

    void drawPolygon(List<QLocation> list, int i, int i2, int i3, String str);

    void drawPolyline(List<QLocation> list, int i, int i2, QMarker qMarker, QMarker qMarker2);

    void drawPolyline(List<QLocation> list, List<QMarker> list2, int i, int i2, int i3, int i4, CustomMapCallback... customMapCallbackArr);

    QLocation getMapCenter();

    float getZoomLevel();

    void hideInfoWindow();

    void isAutoMarkersZoom(boolean z);

    boolean isMapLoaded();

    void mapBound(List<QLocation> list, int i, int i2, CustomMapCallback... customMapCallbackArr);

    void mapCenterTo(int i, int i2);

    void reSetMarkPosition(QMarker qMarker, QLocation qLocation);

    void reSetMarkRotate(QMarker qMarker, float f);

    void removeMarker(QMarker qMarker);

    void removeMarkers(List<QMarker> list);

    void removePolygon(String str);

    void setOnMapClickListener(MapClickListener mapClickListener);

    void setOnMapLoadedCallback(MapLoadedCallback mapLoadedCallback);

    void setOnMapLongClickListener(MapLongClickListener mapLongClickListener);

    void setOnMapStatusChangeListener(MapStatusChangeListener mapStatusChangeListener);

    void setOnMarkerClickListener(MarkerClickListener markerClickListener);

    void setOnMyLocationClickListener(MyLocationClickListener myLocationClickListener);

    void setViewPadding(int i, int i2, int i3, int i4);

    void showCustomInfoWindow(View view, QLocation qLocation, int i);

    void showInfoWindow(QunarInfoWindow qunarInfoWindow);

    void twoPoint2Line(QMarker qMarker, QMarker qMarker2, int i, int i2);

    void updatePolygon(int i, int i2, int i3, String str);
}
