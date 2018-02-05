package qunar.sdk.mapapi.mapquest;

import android.content.Context;
import android.graphics.Paint;
import android.graphics.Paint.Cap;
import android.graphics.Paint.Join;
import android.graphics.Paint.Style;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import com.mapquest.android.maps.BoundingBox;
import com.mapquest.android.maps.DefaultItemizedOverlay;
import com.mapquest.android.maps.GeoPoint;
import com.mapquest.android.maps.LineOverlay;
import com.mapquest.android.maps.MapView;
import com.mapquest.android.maps.MapView.MapViewEventListener;
import com.mapquest.android.maps.Overlay;
import com.mapquest.android.maps.Overlay.OverlayTapListener;
import com.mapquest.android.maps.OverlayItem;
import com.mapquest.android.maps.Util;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import qunar.sdk.location.QLocation;
import qunar.sdk.mapapi.QunarInfoWindow;
import qunar.sdk.mapapi.QunarMap;
import qunar.sdk.mapapi.QunarMapType;
import qunar.sdk.mapapi.entity.QMarker;
import qunar.sdk.mapapi.listener.CustomMapCallback;
import qunar.sdk.mapapi.listener.MapClickListener;
import qunar.sdk.mapapi.listener.MapLoadedCallback;
import qunar.sdk.mapapi.listener.MapLongClickListener;
import qunar.sdk.mapapi.listener.MapStatusChangeListener;
import qunar.sdk.mapapi.listener.MarkerClickListener;
import qunar.sdk.mapapi.listener.MyLocationClickListener;
import qunar.sdk.mapapi.listener.QunarInfoWindowClickListener;
import qunar.sdk.mapapi.utils.MapConstant.TEMPTOKEN;
import qunar.sdk.mapapi.utils.MapHelperUtils;
import qunar.sdk.mapapi.utils.projectTransform.CoordConvertLocal;
import qunar.sdk.mapapi.utils.projectTransform.WSGPointer;

public class MapQuestStrategy implements MapViewEventListener, QunarMap {
    private static final int MAPCHANGE_FINISH = 65283;
    private static final int MAPCHANGE_FINISH_CALLBACK = 65289;
    private boolean cancleRepeatEvent = false;
    private CustomAnnotationView customAnnotationView = null;
    private Drawable defaultDrawable = null;
    private MapChangeHandler handler = null;
    private boolean isAddMapViewEventListener = false;
    private boolean isMapLoad = false;
    private boolean isZoomToBounds = true;
    private DefaultItemizedOverlay itemizedOverlay = null;
    private List<OnMapLoadListener> listeners = new LinkedList();
    private Drawable locationMarker;
    private MapClickListener mapClickListener;
    private MapLoadedCallback mapLoadedCallback;
    private MapLongClickListener mapLongClickListener;
    private MapStatusChangeListener mapStatusChangeListener;
    private MapView mapView;
    private MarkerClickListener markerClickListener;
    private MyLocationClickListener myLocationClickListener;
    private DefaultItemizedOverlay myLocationOverlay = null;
    RelativeLayout newInnerView;

    public interface OnMapLoadListener {
        void onMapLoad();
    }

    class MapChangeHandler extends Handler {
        private WeakReference<MapQuestStrategy> mapQuestWR = null;

        public MapChangeHandler(MapQuestStrategy mapQuestStrategy) {
            this.mapQuestWR = new WeakReference(mapQuestStrategy);
        }

        public void handleMessage(Message message) {
            MapQuestStrategy mapQuestStrategy = (MapQuestStrategy) this.mapQuestWR.get();
            if (mapQuestStrategy == null) {
                return;
            }
            if (message.what == MapQuestStrategy.MAPCHANGE_FINISH) {
                if (hasMessages(MapQuestStrategy.MAPCHANGE_FINISH)) {
                    removeMessages(MapQuestStrategy.MAPCHANGE_FINISH);
                }
                if (hasMessages(65289)) {
                    removeMessages(65289);
                }
                sendEmptyMessageDelayed(65289, 100);
            } else if (message.what == 65289) {
                mapQuestStrategy.mapforceReFefreshFinish();
            }
        }
    }

    public MapQuestStrategy(MapView mapView) {
        this.mapView = mapView;
    }

    public List<OnMapLoadListener> executeListener() {
        return this.listeners;
    }

    public void setOnMapClickListener(MapClickListener mapClickListener) {
        if (mapClickListener != null && this.mapView != null && this.mapView != null) {
            this.mapClickListener = mapClickListener;
            if (!this.isAddMapViewEventListener) {
                this.mapView.addMapViewEventListener(this);
                this.isAddMapViewEventListener = true;
            }
        }
    }

    public void setOnMapLongClickListener(MapLongClickListener mapLongClickListener) {
        if (mapLongClickListener != null && this.mapView != null && this.mapView != null) {
            this.mapLongClickListener = mapLongClickListener;
            if (!this.isAddMapViewEventListener) {
                this.mapView.addMapViewEventListener(this);
                this.isAddMapViewEventListener = true;
            }
        }
    }

    public void setOnMapLoadedCallback(MapLoadedCallback mapLoadedCallback) {
        if (mapLoadedCallback != null && this.mapView != null && this.mapView != null) {
            this.mapLoadedCallback = mapLoadedCallback;
            if (!this.isAddMapViewEventListener) {
                this.mapView.addMapViewEventListener(this);
                this.isAddMapViewEventListener = true;
            }
        }
    }

    public void setOnMarkerClickListener(MarkerClickListener markerClickListener) {
        if (markerClickListener != null && this.mapView != null) {
            this.markerClickListener = markerClickListener;
        }
    }

    public void setOnMapStatusChangeListener(MapStatusChangeListener mapStatusChangeListener) {
        if (mapStatusChangeListener != null && this.mapView != null) {
            this.handler = new MapChangeHandler(this);
            this.mapStatusChangeListener = mapStatusChangeListener;
            if (!this.isAddMapViewEventListener) {
                this.mapView.addMapViewEventListener(this);
                this.isAddMapViewEventListener = true;
            }
        }
    }

    public void setOnMyLocationClickListener(MyLocationClickListener myLocationClickListener) {
        if (myLocationClickListener != null && this.mapView != null) {
            this.myLocationClickListener = myLocationClickListener;
        }
    }

    public float getZoomLevel() {
        if (this.mapView != null) {
            return (float) this.mapView.getZoomLevel();
        }
        return 3.0f;
    }

    public QLocation getMapCenter() {
        if (this.mapView == null) {
            return null;
        }
        GeoPoint mapCenter = this.mapView.getMapCenter();
        return new QLocation(mapCenter.getLatitude(), mapCenter.getLongitude());
    }

    public boolean isMapLoaded() {
        return this.isMapLoad;
    }

    public void addMarker(QMarker qMarker) {
        if (qMarker != null) {
            List arrayList = new ArrayList();
            arrayList.add(qMarker);
            addMarkerOperation(arrayList, true);
        }
    }

    private boolean onMarkerClick(OverlayItem overlayItem) {
        if (this.markerClickListener == null || this.mapView == null || overlayItem == null) {
            return false;
        }
        Bundle extraInfo = overlayItem.getExtraInfo();
        GeoPoint point = overlayItem.getPoint();
        QLocation qLocation = new QLocation(point.getLatitude(), point.getLongitude());
        QMarker qMarker = new QMarker();
        qMarker.position = qLocation;
        qMarker.setExtraInfo(extraInfo);
        qMarker.setBitmapDescriptor(overlayItem);
        this.markerClickListener.onMarkerClick(qMarker);
        return true;
    }

    public void isAutoMarkersZoom(boolean z) {
        this.isZoomToBounds = z;
    }

    public void addMarkers(List<QMarker> list, boolean z) {
        if (list != null && !list.isEmpty()) {
            addMarkerOperation(list, z);
            if (list.size() == 1) {
                final QMarker qMarker = (QMarker) list.get(0);
                if (qMarker != null) {
                    synchronized (this.listeners) {
                        if (isMapLoaded()) {
                            mapCenter(qMarker.position, true, 300);
                        } else {
                            this.listeners.add(new OnMapLoadListener() {
                                public void onMapLoad() {
                                    MapQuestStrategy.this.mapCenter(qMarker.position, true, 300);
                                }
                            });
                        }
                    }
                }
            } else if (this.isZoomToBounds && this.itemizedOverlay != null) {
                BoundingBox boundingBox = this.itemizedOverlay.getBoundingBox();
                if (boundingBox != null) {
                    this.mapView.getController().zoomToSpan(boundingBox);
                }
            }
        }
    }

    private void addMarkerOperation(List<QMarker> list, boolean z) {
        if (this.mapView != null && list != null && !list.isEmpty()) {
            Context context = this.mapView.getContext();
            if (context != null) {
                if (this.defaultDrawable == null) {
                    this.defaultDrawable = Util.getDrawable(context, "location_marker_purple");
                }
                if (this.itemizedOverlay == null) {
                    this.itemizedOverlay = new DefaultItemizedOverlay(this.defaultDrawable);
                    if (this.mapView != null) {
                        this.mapView.getOverlays().add(this.itemizedOverlay);
                        this.itemizedOverlay.setTapListener(new OverlayTapListener() {
                            public void onTap(GeoPoint geoPoint, MapView mapView) {
                                MapQuestStrategy.this.cancleRepeatEvent = true;
                                int lastFocusedIndex = MapQuestStrategy.this.itemizedOverlay.getLastFocusedIndex();
                                if (lastFocusedIndex > -1) {
                                    MapQuestStrategy.this.onMarkerClick(MapQuestStrategy.this.itemizedOverlay.getItem(lastFocusedIndex));
                                }
                            }
                        });
                    }
                }
                Drawable drawable = null;
                if (z) {
                    drawable = MapHelperUtils.createDrawable(this.mapView.getContext(), (QMarker) list.get(0));
                    Overlay.setAlignment(drawable, 33);
                }
                Drawable drawable2 = drawable;
                for (QMarker qMarker : list) {
                    QLocation qLocation = qMarker.position;
                    OverlayItem overlayItem = new OverlayItem(new GeoPoint(qLocation.getLatitude(), qLocation.getLongitude()), "", "");
                    if (!z) {
                        drawable2 = MapHelperUtils.createDrawable(context, qMarker);
                        Overlay.setAlignment(drawable2, 33);
                    }
                    if (drawable2 != null) {
                        overlayItem.setMarker(drawable2);
                        overlayItem.setExtraInfo(qMarker.getExtraInfo());
                        this.itemizedOverlay.addItem(overlayItem);
                        qMarker.setBitmapDescriptor(overlayItem);
                        qMarker.setMapType(QunarMapType.MAPQUEST);
                    }
                }
                this.mapView.invalidate();
            }
        }
    }

    private void mapCenter(QLocation qLocation, boolean z, int i) {
        if (qLocation != null && this.mapView != null) {
            if (z) {
                this.mapView.getController().animateTo(new GeoPoint(qLocation.getLatitude(), qLocation.getLongitude()));
                this.mapView.getController().setZoom(this.mapView.getZoomLevel() + 1);
                return;
            }
            this.mapView.getController().setCenter(new GeoPoint(qLocation.getLatitude(), qLocation.getLongitude()));
            this.mapView.getController().setZoom(this.mapView.getZoomLevel() + 1);
        }
    }

    public void removeMarkers(List<QMarker> list) {
        if (list != null && !list.isEmpty()) {
            for (QMarker removeMarker : list) {
                removeMarker(removeMarker);
            }
            this.mapView.invalidate();
        }
    }

    public void removeMarker(QMarker qMarker) {
        if (qMarker != null && this.itemizedOverlay != null) {
            this.itemizedOverlay.removeItem((OverlayItem) qMarker.getBitmapDescriptor());
            qMarker.recycle();
        }
    }

    public void hideInfoWindow() {
        if (this.customAnnotationView != null) {
            this.customAnnotationView.hide();
        }
        if (this.newInnerView != null) {
            this.newInnerView.destroyDrawingCache();
            this.newInnerView.removeAllViews();
            this.newInnerView.setOnClickListener(null);
        }
    }

    public void showInfoWindow(final QunarInfoWindow qunarInfoWindow) {
        hideInfoWindow();
        if (this.mapView != null && qunarInfoWindow != null) {
            Context context = this.mapView.getContext();
            View view = qunarInfoWindow.getView();
            if (context != null && view != null) {
                QLocation qLocation = qunarInfoWindow.getMarker().position;
                GeoPoint geoPoint = new GeoPoint(qLocation.getLatitude(), qLocation.getLongitude());
                if (this.customAnnotationView == null) {
                    this.customAnnotationView = new CustomAnnotationView(this.mapView);
                    this.customAnnotationView.setAnimated(true);
                }
                this.customAnnotationView.setGeoPoint(geoPoint);
                this.customAnnotationView.setInterceptTouch(true);
                if (this.newInnerView == null) {
                    this.newInnerView = new RelativeLayout(context);
                }
                this.newInnerView.addView(view, new LayoutParams(-1, -1));
                this.newInnerView.setFocusable(true);
                this.newInnerView.setClickable(true);
                this.customAnnotationView.setInnerView(this.newInnerView);
                this.customAnnotationView.setOnClickListener(new OnClickListener() {
                    public void onClick(View view) {
                        QunarInfoWindowClickListener infoWindowClickListener = qunarInfoWindow.getInfoWindowClickListener();
                        if (infoWindowClickListener != null) {
                            infoWindowClickListener.onInfoWindowClick(qunarInfoWindow.getCallbackData());
                        }
                    }
                });
                this.customAnnotationView.showAnnotationView(geoPoint, qunarInfoWindow.getOffsetPixelY());
            }
        }
    }

    public void showCustomInfoWindow(View view, QLocation qLocation, int i) {
        hideInfoWindow();
        if (this.mapView != null && view != null) {
            GeoPoint geoPoint = new GeoPoint(qLocation.getLatitude(), qLocation.getLongitude());
            if (this.customAnnotationView == null) {
                this.customAnnotationView = new CustomAnnotationView(this.mapView);
                this.customAnnotationView.setAnimated(true);
            }
            this.customAnnotationView.setGeoPoint(geoPoint);
            this.customAnnotationView.setInterceptTouch(false);
            Context context = this.mapView.getContext();
            if (context != null) {
                if (this.newInnerView == null) {
                    this.newInnerView = new RelativeLayout(context);
                }
                this.newInnerView.addView(view, new LayoutParams(-1, -1));
                this.customAnnotationView.setInnerView(this.newInnerView);
                this.customAnnotationView.showAnnotationView(geoPoint, i);
            }
        }
    }

    public void addMyLocationData(QLocation qLocation) {
        GeoPoint geoPoint;
        Context context = null;
        if (this.mapView != null) {
            context = this.mapView.getContext();
        }
        if (this.myLocationOverlay == null) {
            this.locationMarker = Util.getDrawable(context, "location_marker_purple");
            this.myLocationOverlay = new DefaultItemizedOverlay(this.locationMarker);
            if (this.mapView != null) {
                this.mapView.getOverlays().add(this.myLocationOverlay);
                this.myLocationOverlay.setTapListener(new OverlayTapListener() {
                    public void onTap(GeoPoint geoPoint, MapView mapView) {
                        MapQuestStrategy.this.cancleRepeatEvent = true;
                        if (MapQuestStrategy.this.myLocationOverlay.getLastFocusedIndex() > -1 && MapQuestStrategy.this.myLocationClickListener != null) {
                            MapQuestStrategy.this.myLocationClickListener.onMyLocationClick();
                        }
                    }
                });
            }
        }
        this.myLocationOverlay.clear();
        if (qLocation.getIsAbroad()) {
            geoPoint = new GeoPoint(qLocation.getLatitude(), qLocation.getLongitude());
        } else {
            WSGPointer bd2mar = CoordConvertLocal.bd2mar(qLocation.getLatitude(), qLocation.getLongitude());
            geoPoint = new GeoPoint(bd2mar.getLatitude(), bd2mar.getLongitude());
        }
        this.myLocationOverlay.addItem(new OverlayItem(geoPoint, "", ""));
    }

    public void clear() {
        if (this.handler != null) {
            this.handler.removeCallbacksAndMessages(null);
        }
        if (this.mapView != null) {
            List<Overlay> overlays = this.mapView.getOverlays();
            if (overlays != null && !overlays.isEmpty()) {
                for (Overlay overlay : overlays) {
                    if (this.myLocationOverlay == null || overlay.hashCode() != this.myLocationOverlay.hashCode()) {
                        overlay.destroy();
                    }
                }
            } else {
                return;
            }
        }
        if (this.customAnnotationView != null) {
            this.customAnnotationView.hide();
        }
    }

    public void twoPoint2Line(QMarker qMarker, QMarker qMarker2, int i, int i2) {
        if (this.mapView != null) {
            List arrayList = new ArrayList();
            arrayList.add(qMarker);
            arrayList.add(qMarker2);
            addMarkerOperation(arrayList, false);
            List arrayList2 = new ArrayList();
            arrayList2.add(qMarker.position);
            arrayList2.add(qMarker2.position);
            drawPolyline(arrayList2, i, i2, qMarker, qMarker2);
        }
    }

    public void drawPolyline(List<QLocation> list, int i, int i2, QMarker qMarker, QMarker qMarker2) {
        if (this.mapView != null && list != null && !list.isEmpty()) {
            List arrayList = new ArrayList();
            arrayList.add(qMarker);
            arrayList.add(qMarker2);
            addMarkerOperation(arrayList, false);
            Paint paint = new Paint(1);
            paint.setColor(i);
            paint.setAlpha(100);
            paint.setStyle(Style.STROKE);
            paint.setStrokeJoin(Join.ROUND);
            paint.setStrokeCap(Cap.ROUND);
            paint.setStrokeWidth((float) i2);
            List arrayList2 = new ArrayList();
            for (QLocation qLocation : list) {
                arrayList2.add(new GeoPoint(qLocation.getLatitude(), qLocation.getLongitude()));
            }
            LineOverlay lineOverlay = new LineOverlay(paint);
            lineOverlay.setData(arrayList2, true);
            lineOverlay.setShowPoints(true, null);
            this.mapView.getOverlays().add(lineOverlay);
            this.mapView.invalidate();
            this.mapView.getController().zoomToSpan(BoundingBox.calculateBoundingBoxGeoPoint(arrayList2));
        }
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

    public void setViewPadding(int i, int i2, int i3, int i4) {
    }

    public void destroy(String str) {
        if (str != null && TEMPTOKEN.MAP_DESTROY_TOKEN.equals(str)) {
            if (this.newInnerView != null) {
                this.newInnerView.destroyDrawingCache();
                this.newInnerView.removeAllViews();
                this.newInnerView.setOnClickListener(null);
                this.newInnerView = null;
            }
            clear();
            if (this.myLocationOverlay != null) {
                this.myLocationOverlay.destroy();
            }
            if (this.mapView != null) {
                this.isAddMapViewEventListener = false;
                this.mapView.removeMapViewEventListener(this);
                this.mapView.destroyDrawingCache();
                this.mapView.destroy();
                this.mapView = null;
            }
        }
    }

    public void touch(MapView mapView) {
        if (this.mapClickListener != null && this.mapView != null && this.mapView != null) {
            this.mapClickListener.onMapClick(null);
        }
    }

    public void longTouch(MapView mapView, Point point) {
        if (this.mapLongClickListener == null || this.mapView == null || this.cancleRepeatEvent) {
            this.cancleRepeatEvent = false;
            return;
        }
        QLocation qLocation = null;
        if (!(this.mapView == null || point == null)) {
            GeoPoint fromPixels = this.mapView.getProjection().fromPixels(point.x, point.y);
            qLocation = new QLocation(fromPixels.getLatitude(), fromPixels.getLongitude());
        }
        this.mapLongClickListener.onMapLongClick(qLocation);
    }

    public void mapLoaded(MapView mapView) {
        if (this.mapLoadedCallback != null && this.mapView != null) {
            this.isMapLoad = true;
            this.mapLoadedCallback.onMapLoaded();
            synchronized (this.listeners) {
                if (!(this.listeners == null || this.listeners.isEmpty())) {
                    for (OnMapLoadListener onMapLoad : this.listeners) {
                        try {
                            onMapLoad.onMapLoad();
                        } catch (Exception e) {
                        }
                    }
                    this.listeners.clear();
                }
            }
        }
    }

    public void zoomEnd(MapView mapView) {
    }

    public void zoomStart(MapView mapView) {
    }

    private void mapforceReFefreshFinish() {
        if (this.mapView != null && this.mapStatusChangeListener != null) {
            GeoPoint mapCenter = this.mapView.getMapCenter();
            this.mapStatusChangeListener.onMapStatusChangeFinish(new QLocation(mapCenter.getLatitude(), mapCenter.getLongitude()), (float) this.mapView.getZoomLevel());
        }
    }

    public void move(MapView mapView) {
        if (this.mapStatusChangeListener != null && this.mapView != null && this.handler != null) {
            this.mapStatusChangeListener.onMapStatusChange(null, (float) this.mapView.getZoomLevel());
            if (this.handler.hasMessages(MAPCHANGE_FINISH)) {
                this.handler.removeMessages(MAPCHANGE_FINISH);
            }
            if (this.handler.hasMessages(65289)) {
                this.handler.removeMessages(65289);
            }
            this.handler.sendEmptyMessage(MAPCHANGE_FINISH);
        }
    }

    public void moveEnd(MapView mapView) {
        if (this.mapStatusChangeListener != null && this.mapView != null && this.handler != null) {
            if (this.handler.hasMessages(MAPCHANGE_FINISH)) {
                this.handler.removeMessages(MAPCHANGE_FINISH);
            }
            if (this.handler.hasMessages(65289)) {
                this.handler.removeMessages(65289);
            }
            if (this.mapStatusChangeListener != null && this.mapView != null) {
                GeoPoint mapCenter = this.mapView.getMapCenter();
                this.mapStatusChangeListener.onMapStatusChangeFinish(new QLocation(mapCenter.getLatitude(), mapCenter.getLongitude()), (float) this.mapView.getZoomLevel());
            }
        }
    }

    public void moveStart(MapView mapView) {
        if (this.mapStatusChangeListener != null && this.mapView != null && this.handler != null) {
            this.mapStatusChangeListener.onMapStatusChangeStart(null, (float) this.mapView.getZoomLevel());
        }
    }
}
