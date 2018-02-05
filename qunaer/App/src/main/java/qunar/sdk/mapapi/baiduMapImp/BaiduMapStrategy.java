package qunar.sdk.mapapi.baiduMapImp;

import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BaiduMap.OnMapClickListener;
import com.baidu.mapapi.map.BaiduMap.OnMapLoadedCallback;
import com.baidu.mapapi.map.BaiduMap.OnMapLongClickListener;
import com.baidu.mapapi.map.BaiduMap.OnMapStatusChangeListener;
import com.baidu.mapapi.map.BaiduMap.OnMarkerClickListener;
import com.baidu.mapapi.map.BaiduMap.OnMyLocationClickListener;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.InfoWindow.OnInfoWindowClickListener;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.Polygon;
import com.baidu.mapapi.map.PolygonOptions;
import com.baidu.mapapi.map.PolylineOptions;
import com.baidu.mapapi.map.Stroke;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.model.LatLngBounds;
import com.baidu.mapapi.model.LatLngBounds.Builder;
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

public class BaiduMapStrategy implements OnMapClickListener, OnMapLoadedCallback, OnMapLongClickListener, OnMapStatusChangeListener, OnMarkerClickListener, OnMyLocationClickListener, QunarMap {
    private static final int MAPCHANGE_FINISH = 65283;
    private static final int MAPCHANGE_FINISH_CALLBACK = 65289;
    private static final String POLYGON_EXT_INFO = "polygon_ext_info";
    private BaiduMap bdMap;
    private List<QMarker> good4recycle = new ArrayList();
    private MapChangeHandler handler = null;
    private BitmapDescriptor infoWindowBitmapDescriptor;
    private boolean isMapLoad = false;
    private boolean isZoomToBounds = true;
    private List<OnMapLoadListener> listeners = new LinkedList();
    private MapClickListener mapClickListener;
    private MapLoadedCallback mapLoadedCallback;
    private MapLongClickListener mapLongClickListener;
    private MapStatusChangeListener mapStatusChangeListener;
    private MarkerClickListener markerClickListener;
    private MyLocationClickListener myLocationClickListener;
    private List<Polygon> polygonList = new ArrayList();

    public interface OnMapLoadListener {
        void onMapLoad();
    }

    class MapChangeHandler extends Handler {
        private WeakReference<BaiduMapStrategy> bdMapWR = null;

        public MapChangeHandler(BaiduMapStrategy baiduMapStrategy) {
            this.bdMapWR = new WeakReference(baiduMapStrategy);
        }

        public void handleMessage(Message message) {
            BaiduMapStrategy baiduMapStrategy = (BaiduMapStrategy) this.bdMapWR.get();
            if (baiduMapStrategy == null) {
                return;
            }
            if (message.what == BaiduMapStrategy.MAPCHANGE_FINISH) {
                if (hasMessages(BaiduMapStrategy.MAPCHANGE_FINISH)) {
                    removeMessages(BaiduMapStrategy.MAPCHANGE_FINISH);
                }
                if (hasMessages(65289)) {
                    removeMessages(65289);
                }
                sendEmptyMessageDelayed(65289, 100);
            } else if (message.what == 65289) {
                baiduMapStrategy.mapforceReFefreshFinish();
            }
        }
    }

    public BaiduMapStrategy(BaiduMap baiduMap) {
        this.bdMap = baiduMap;
    }

    public BaiduMap getBaiduMap() {
        return this.bdMap;
    }

    public List<OnMapLoadListener> executeListener() {
        return this.listeners;
    }

    public void setOnMapClickListener(MapClickListener mapClickListener) {
        if (mapClickListener != null && this.bdMap != null && this.bdMap != null) {
            this.mapClickListener = mapClickListener;
            this.bdMap.setOnMapClickListener(this);
        }
    }

    public void setOnMapLongClickListener(MapLongClickListener mapLongClickListener) {
        if (mapLongClickListener != null && this.bdMap != null && this.bdMap != null) {
            this.mapLongClickListener = mapLongClickListener;
            this.bdMap.setOnMapLongClickListener(this);
        }
    }

    public void setOnMapLoadedCallback(MapLoadedCallback mapLoadedCallback) {
        if (mapLoadedCallback != null && this.bdMap != null && this.bdMap != null) {
            this.mapLoadedCallback = mapLoadedCallback;
            this.bdMap.setOnMapLoadedCallback(this);
        }
    }

    public void setOnMarkerClickListener(MarkerClickListener markerClickListener) {
        if (markerClickListener != null && this.bdMap != null && this.bdMap != null) {
            this.markerClickListener = markerClickListener;
            this.bdMap.setOnMarkerClickListener(this);
        }
    }

    public void setOnMapStatusChangeListener(MapStatusChangeListener mapStatusChangeListener) {
        if (this.bdMap != null && this.bdMap != null) {
            OnMapStatusChangeListener onMapStatusChangeListener;
            this.handler = new MapChangeHandler(this);
            this.mapStatusChangeListener = mapStatusChangeListener;
            BaiduMap baiduMap = this.bdMap;
            if (mapStatusChangeListener == null) {
                onMapStatusChangeListener = null;
            }
            baiduMap.setOnMapStatusChangeListener(onMapStatusChangeListener);
        }
    }

    public void setOnMyLocationClickListener(MyLocationClickListener myLocationClickListener) {
        if (myLocationClickListener != null && this.bdMap != null && this.bdMap != null) {
            this.myLocationClickListener = myLocationClickListener;
            this.bdMap.setOnMyLocationClickListener(this);
        }
    }

    public void onMapClick(LatLng latLng) {
        if (this.mapClickListener != null && this.bdMap != null && this.bdMap != null) {
            this.mapClickListener.onMapClick(new QLocation(latLng.latitude, latLng.longitude));
        }
    }

    public boolean onMapPoiClick(MapPoi mapPoi) {
        return false;
    }

    public void onMapLongClick(LatLng latLng) {
        if (this.mapLongClickListener != null && this.bdMap != null) {
            this.mapLongClickListener.onMapLongClick(new QLocation(latLng.latitude, latLng.longitude));
        }
    }

    public boolean onMarkerClick(Marker marker) {
        if (this.markerClickListener == null || this.bdMap == null || marker == null) {
            return false;
        }
        Bundle extraInfo = marker.getExtraInfo();
        LatLng position = marker.getPosition();
        QLocation qLocation = new QLocation(position.latitude, position.longitude);
        QMarker qMarker = new QMarker();
        qMarker.position = qLocation;
        qMarker.setExtraInfo(extraInfo);
        this.markerClickListener.onMarkerClick(qMarker);
        return true;
    }

    public boolean onMyLocationClick() {
        if (this.myLocationClickListener != null) {
            this.myLocationClickListener.onMyLocationClick();
        }
        return false;
    }

    public void addMarker(QMarker qMarker) {
        if (qMarker != null) {
            BitmapDescriptor createBitmapDescriptor = MapHelperUtils.createBitmapDescriptor(qMarker);
            if (createBitmapDescriptor != null) {
                addMarker(qMarker, createBitmapDescriptor);
            }
        }
    }

    public void addMarker(final QMarker qMarker, final BitmapDescriptor bitmapDescriptor) {
        if (qMarker != null && bitmapDescriptor != null) {
            synchronized (this.listeners) {
                if (isMapLoaded()) {
                    addMarkerOperation(qMarker, bitmapDescriptor);
                } else {
                    this.listeners.add(new OnMapLoadListener() {
                        public void onMapLoad() {
                            BaiduMapStrategy.this.addMarkerOperation(qMarker, bitmapDescriptor);
                        }
                    });
                }
            }
        }
    }

    private void addMarkerOperation(QMarker qMarker, BitmapDescriptor bitmapDescriptor) {
        QLocation qLocation = qMarker.position;
        OverlayOptions zIndex = new MarkerOptions().anchor(qMarker.getAnchorX(), qMarker.getAnchorY()).extraInfo(qMarker.getExtraInfo()).perspective(qMarker.isPerspective()).position(new LatLng(qLocation.getLatitude(), qLocation.getLongitude())).rotate(qMarker.getRotate()).title(qMarker.getTitle()).visible(qMarker.isVisible()).icon(bitmapDescriptor).zIndex(qMarker.getzIndex());
        if (this.bdMap != null) {
            qMarker.setOverlayOption(this.bdMap.addOverlay(zIndex));
            qMarker.setBitmapDescriptor(bitmapDescriptor);
            qMarker.setMapType(QunarMapType.BAIDU);
        }
    }

    public void isAutoMarkersZoom(boolean z) {
        this.isZoomToBounds = z;
    }

    public void addMarkers(List<QMarker> list, boolean z) {
        if (list != null && !list.isEmpty()) {
            final QMarker qMarker;
            if (list.size() == 1) {
                qMarker = (QMarker) list.get(0);
                if (qMarker != null) {
                    addMarker(qMarker);
                    synchronized (this.listeners) {
                        if (isMapLoaded()) {
                            mapCenter(qMarker.position, true, 300);
                        } else {
                            this.listeners.add(new OnMapLoadListener() {
                                public void onMapLoad() {
                                    BaiduMapStrategy.this.mapCenter(qMarker.position, true, 300);
                                }
                            });
                        }
                    }
                    return;
                }
                return;
            }
            Builder builder = new Builder();
            QLocation qLocation;
            if (z) {
                BitmapDescriptor createBitmapDescriptor = MapHelperUtils.createBitmapDescriptor((QMarker) list.get(0));
                for (QMarker qMarker2 : list) {
                    addMarker(qMarker2, createBitmapDescriptor);
                    qLocation = qMarker2.position;
                    builder.include(new LatLng(qLocation.getLatitude(), qLocation.getLongitude()));
                }
            } else {
                for (QMarker qMarker22 : list) {
                    addMarker(qMarker22, MapHelperUtils.createBitmapDescriptor(qMarker22));
                    qLocation = qMarker22.position;
                    builder.include(new LatLng(qLocation.getLatitude(), qLocation.getLongitude()));
                }
            }
            if (this.isZoomToBounds) {
                final LatLngBounds build = builder.build();
                synchronized (this.listeners) {
                    if (isMapLoaded()) {
                        zoom2Bounds(build);
                    } else {
                        this.listeners.add(new OnMapLoadListener() {
                            public void onMapLoad() {
                                BaiduMapStrategy.this.zoom2Bounds(build);
                            }
                        });
                    }
                }
            }
        }
    }

    private void zoom2Bounds(LatLngBounds latLngBounds) {
        MapStatusUpdate newLatLngBounds = MapStatusUpdateFactory.newLatLngBounds(latLngBounds);
        if (this.bdMap != null) {
            this.bdMap.animateMapStatus(newLatLngBounds, 300);
        }
    }

    private void mapCenter(QLocation qLocation, boolean z, int i) {
        if (qLocation != null) {
            MapStatusUpdate newLatLngZoom = MapStatusUpdateFactory.newLatLngZoom(new LatLng(qLocation.getLatitude(), qLocation.getLongitude()), 15.0f);
            if (z) {
                this.bdMap.animateMapStatus(newLatLngZoom, i);
            } else {
                this.bdMap.setMapStatus(newLatLngZoom);
            }
        }
    }

    public void removeMarkers(List<QMarker> list) {
        if (list != null && list.size() != 0) {
            for (QMarker qMarker : list) {
                qMarker.remove();
                qMarker.recycle();
            }
        }
    }

    public void removeMarker(QMarker qMarker) {
        if (qMarker != null) {
            qMarker.remove();
            qMarker.recycle();
        }
    }

    public void hideInfoWindow() {
        if (this.bdMap != null) {
            this.bdMap.hideInfoWindow();
            if (this.infoWindowBitmapDescriptor != null) {
                this.infoWindowBitmapDescriptor.recycle();
                this.infoWindowBitmapDescriptor = null;
            }
        }
    }

    public void showInfoWindow(final QunarInfoWindow qunarInfoWindow) {
        if (this.bdMap != null && qunarInfoWindow != null) {
            OnInfoWindowClickListener anonymousClass4 = new OnInfoWindowClickListener() {
                public void onInfoWindowClick() {
                    QunarInfoWindowClickListener infoWindowClickListener = qunarInfoWindow.getInfoWindowClickListener();
                    if (infoWindowClickListener != null) {
                        infoWindowClickListener.onInfoWindowClick(qunarInfoWindow.getCallbackData());
                    }
                }
            };
            QLocation qLocation = qunarInfoWindow.getMarker().position;
            LatLng latLng = new LatLng(qLocation.getLatitude(), qLocation.getLongitude());
            View view = qunarInfoWindow.getView();
            if (view != null) {
                try {
                    this.infoWindowBitmapDescriptor = BitmapDescriptorFactory.fromView(view);
                    if (this.infoWindowBitmapDescriptor != null) {
                        this.bdMap.showInfoWindow(new InfoWindow(this.infoWindowBitmapDescriptor, latLng, -qunarInfoWindow.getOffsetPixelY(), anonymousClass4));
                    }
                } catch (NullPointerException e) {
                }
            }
        }
    }

    public void showCustomInfoWindow(View view, QLocation qLocation, int i) {
        if (this.bdMap != null && view != null && qLocation != null) {
            this.bdMap.showInfoWindow(new InfoWindow(view, new LatLng(qLocation.getLatitude(), qLocation.getLongitude()), i));
        }
    }

    public void onMapLoaded() {
        if (this.mapLoadedCallback != null && this.bdMap != null) {
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

    public boolean isMapLoaded() {
        return this.isMapLoad;
    }

    private void mapforceReFefreshFinish() {
        if (this.mapStatusChangeListener != null && this.bdMap != null) {
            MapStatus mapStatus = this.bdMap.getMapStatus();
            LatLng latLng = mapStatus.target;
            this.mapStatusChangeListener.onMapStatusChangeFinish(new QLocation(latLng.latitude, latLng.longitude), mapStatus.zoom);
        }
    }

    public void onMapStatusChange(MapStatus mapStatus) {
        if (this.mapStatusChangeListener != null && this.bdMap != null && mapStatus != null) {
            LatLng latLng = mapStatus.target;
            this.mapStatusChangeListener.onMapStatusChange(new QLocation(latLng.latitude, latLng.longitude), mapStatus.zoom);
            if (this.handler.hasMessages(MAPCHANGE_FINISH)) {
                this.handler.removeMessages(MAPCHANGE_FINISH);
            }
            if (this.handler.hasMessages(65289)) {
                this.handler.removeMessages(65289);
            }
            this.handler.sendEmptyMessage(MAPCHANGE_FINISH);
        }
    }

    public void onMapStatusChangeFinish(MapStatus mapStatus) {
        if (this.handler.hasMessages(MAPCHANGE_FINISH)) {
            this.handler.removeMessages(MAPCHANGE_FINISH);
        }
        if (this.handler.hasMessages(65289)) {
            this.handler.removeMessages(65289);
        }
        if (this.mapStatusChangeListener != null && this.bdMap != null && mapStatus != null) {
            LatLng latLng = mapStatus.target;
            this.mapStatusChangeListener.onMapStatusChangeFinish(new QLocation(latLng.latitude, latLng.longitude), mapStatus.zoom);
        }
    }

    public void onMapStatusChangeStart(MapStatus mapStatus) {
        if (this.mapStatusChangeListener != null && this.bdMap != null && mapStatus != null) {
            LatLng latLng = mapStatus.target;
            this.mapStatusChangeListener.onMapStatusChangeStart(new QLocation(latLng.latitude, latLng.longitude), mapStatus.zoom);
        }
    }

    public void addMyLocationData(final QLocation qLocation) {
        if (qLocation != null && this.bdMap != null) {
            synchronized (this.listeners) {
                if (isMapLoaded()) {
                    addLocationData(qLocation);
                } else {
                    this.listeners.add(new OnMapLoadListener() {
                        public void onMapLoad() {
                            BaiduMapStrategy.this.addLocationData(qLocation);
                        }
                    });
                }
            }
        }
    }

    private void addLocationData(QLocation qLocation) {
        this.bdMap.setMyLocationData(new MyLocationData.Builder().accuracy(qLocation.getAccuracy()).latitude(qLocation.getLatitude()).longitude(qLocation.getLongitude()).build());
    }

    public void clear() {
        for (QMarker recycle : this.good4recycle) {
            recycle.recycle();
        }
        for (Polygon remove : this.polygonList) {
            remove.remove();
        }
        if (this.handler != null) {
            this.handler.removeCallbacksAndMessages(null);
        }
        if (this.bdMap != null) {
            this.bdMap.clear();
        }
    }

    public QLocation getMapCenter() {
        if (this.bdMap == null) {
            return null;
        }
        LatLng latLng = this.bdMap.getMapStatus().target;
        return new QLocation(latLng.latitude, latLng.longitude);
    }

    public float getZoomLevel() {
        if (this.bdMap != null) {
            return this.bdMap.getMapStatus().zoom;
        }
        return 3.0f;
    }

    public void twoPoint2Line(QMarker qMarker, QMarker qMarker2, int i, int i2) {
        if (this.bdMap != null) {
            Builder builder = new Builder();
            LatLng latLng = new LatLng(qMarker.position.getLatitude(), qMarker.position.getLongitude());
            builder.include(latLng);
            LatLng latLng2 = new LatLng(qMarker2.position.getLatitude(), qMarker2.position.getLongitude());
            builder.include(latLng2);
            List arrayList = new ArrayList();
            arrayList.add(latLng);
            arrayList.add(latLng2);
            this.bdMap.addOverlay(new PolylineOptions().color(i).width(i2).points(arrayList));
            BitmapDescriptor createBitmapDescriptor = MapHelperUtils.createBitmapDescriptor(qMarker);
            if (createBitmapDescriptor != null) {
                OverlayOptions zIndex = new MarkerOptions().anchor(qMarker.getAnchorX(), qMarker.getAnchorY()).extraInfo(qMarker.getExtraInfo()).perspective(qMarker.isPerspective()).position(latLng).rotate(qMarker.getRotate()).title(qMarker.getTitle()).visible(qMarker.isVisible()).icon(createBitmapDescriptor).zIndex(qMarker.getzIndex());
                qMarker.setBitmapDescriptor(createBitmapDescriptor);
                this.bdMap.addOverlay(zIndex);
                this.good4recycle.add(qMarker);
            }
            BitmapDescriptor createBitmapDescriptor2 = MapHelperUtils.createBitmapDescriptor(qMarker2);
            if (createBitmapDescriptor2 != null) {
                this.bdMap.addOverlay(new MarkerOptions().anchor(qMarker2.getAnchorX(), qMarker2.getAnchorY()).extraInfo(qMarker2.getExtraInfo()).perspective(qMarker2.isPerspective()).position(latLng2).rotate(qMarker2.getRotate()).title(qMarker2.getTitle()).visible(qMarker2.isVisible()).icon(createBitmapDescriptor2).zIndex(qMarker2.getzIndex()));
                qMarker2.setBitmapDescriptor(createBitmapDescriptor2);
                this.good4recycle.add(qMarker2);
            }
            zoom2Bounds(builder.build());
        }
    }

    public void drawPolyline(List<QLocation> list, int i, int i2, QMarker qMarker, QMarker qMarker2) {
        if (this.bdMap != null) {
            Builder builder = new Builder();
            List arrayList = new ArrayList();
            if (list != null && list.size() > 0) {
                for (QLocation qLocation : list) {
                    arrayList.add(new LatLng(qLocation.getLatitude(), qLocation.getLongitude()));
                }
                LatLng latLng = new LatLng(((QLocation) list.get(0)).getLatitude(), ((QLocation) list.get(0)).getLongitude());
                builder.include(latLng);
                LatLng latLng2 = new LatLng(((QLocation) list.get(list.size() - 1)).getLatitude(), ((QLocation) list.get(list.size() - 1)).getLongitude());
                builder.include(latLng2);
                this.bdMap.addOverlay(new PolylineOptions().color(i).width(i2).points(arrayList));
                BitmapDescriptor createBitmapDescriptor = MapHelperUtils.createBitmapDescriptor(qMarker);
                if (createBitmapDescriptor != null) {
                    OverlayOptions zIndex = new MarkerOptions().anchor(qMarker.getAnchorX(), qMarker.getAnchorY()).extraInfo(qMarker.getExtraInfo()).perspective(qMarker.isPerspective()).position(latLng).rotate(qMarker.getRotate()).title(qMarker.getTitle()).visible(qMarker.isVisible()).icon(createBitmapDescriptor).zIndex(qMarker.getzIndex());
                    qMarker.setBitmapDescriptor(createBitmapDescriptor);
                    this.bdMap.addOverlay(zIndex);
                    this.good4recycle.add(qMarker);
                }
                createBitmapDescriptor = MapHelperUtils.createBitmapDescriptor(qMarker2);
                if (createBitmapDescriptor != null) {
                    this.bdMap.addOverlay(new MarkerOptions().anchor(qMarker2.getAnchorX(), qMarker2.getAnchorY()).extraInfo(qMarker2.getExtraInfo()).perspective(qMarker2.isPerspective()).position(latLng2).rotate(qMarker2.getRotate()).title(qMarker2.getTitle()).visible(qMarker2.isVisible()).icon(createBitmapDescriptor).zIndex(qMarker2.getzIndex()));
                    qMarker2.setBitmapDescriptor(createBitmapDescriptor);
                    this.good4recycle.add(qMarker2);
                }
                zoom2Bounds(builder.build());
            }
        }
    }

    public void drawPolyline(List<QLocation> list, List<QMarker> list2, int i, int i2, int i3, int i4, CustomMapCallback... customMapCallbackArr) {
        if (this.bdMap != null) {
            LatLng latLng;
            Builder builder = new Builder();
            List arrayList = new ArrayList();
            if (list2 != null && list2.size() > 0) {
                for (QMarker qMarker : list2) {
                    if (qMarker.position != null) {
                        latLng = new LatLng(qMarker.position.getLatitude(), qMarker.position.getLongitude());
                        builder.include(latLng);
                        BitmapDescriptor createBitmapDescriptor = MapHelperUtils.createBitmapDescriptor(qMarker);
                        OverlayOptions zIndex = new MarkerOptions().anchor(qMarker.getAnchorX(), qMarker.getAnchorY()).extraInfo(qMarker.getExtraInfo()).perspective(qMarker.isPerspective()).position(latLng).rotate(qMarker.getRotate()).title(qMarker.getTitle()).visible(qMarker.isVisible()).icon(createBitmapDescriptor).zIndex(qMarker.getzIndex());
                        qMarker.setBitmapDescriptor(createBitmapDescriptor);
                        this.bdMap.addOverlay(zIndex);
                    }
                }
            }
            if (list != null && list.size() > 0) {
                for (QLocation qLocation : list) {
                    latLng = new LatLng(qLocation.getLatitude(), qLocation.getLongitude());
                    arrayList.add(latLng);
                    builder.include(latLng);
                }
            }
            this.bdMap.addOverlay(new PolylineOptions().color(i).width(i2).points(arrayList));
            zoom2Bounds(builder.build(), i3, i4, customMapCallbackArr);
        }
    }

    public void drawPolygon(List<QLocation> list, int i, int i2, int i3, String str) {
        if (this.bdMap != null && list != null && list.size() != 0) {
            List arrayList = new ArrayList();
            for (QLocation qLocation : list) {
                arrayList.add(new LatLng(qLocation.getLatitude(), qLocation.getLongitude()));
            }
            OverlayOptions fillColor = new PolygonOptions().points(arrayList).stroke(new Stroke(i, i2)).fillColor(i3);
            this.bdMap.addOverlay(fillColor);
            Polygon polygon = (Polygon) this.bdMap.addOverlay(fillColor);
            Bundle bundle = new Bundle();
            bundle.putSerializable(POLYGON_EXT_INFO, str);
            polygon.setExtraInfo(bundle);
            this.polygonList.add(polygon);
        }
    }

    public void updatePolygon(int i, int i2, int i3, String str) {
        if (this.bdMap != null && this.polygonList != null && this.polygonList.size() != 0) {
            for (Polygon polygon : this.polygonList) {
                if (polygon.getExtraInfo() != null && polygon.getExtraInfo().getString(POLYGON_EXT_INFO).equals(str)) {
                    polygon.setStroke(new Stroke(i, i2));
                    polygon.setFillColor(i3);
                }
            }
        }
    }

    public void removePolygon(String str) {
        if (this.bdMap != null && this.polygonList != null && this.polygonList.size() != 0) {
            for (Polygon polygon : this.polygonList) {
                if (polygon.getExtraInfo() != null && polygon.getExtraInfo().getString(POLYGON_EXT_INFO).equals(str)) {
                    polygon.remove();
                }
            }
        }
    }

    public void mapBound(List<QLocation> list, int i, int i2, CustomMapCallback... customMapCallbackArr) {
        if (list != null && !list.isEmpty()) {
            Builder builder = new Builder();
            for (QLocation qLocation : list) {
                builder.include(new LatLng(qLocation.getLatitude(), qLocation.getLongitude()));
            }
            final LatLngBounds build = builder.build();
            synchronized (this.listeners) {
                if (isMapLoaded()) {
                    zoom2Bounds(build, i, i2, customMapCallbackArr);
                } else {
                    final int i3 = i;
                    final int i4 = i2;
                    final CustomMapCallback[] customMapCallbackArr2 = customMapCallbackArr;
                    this.listeners.add(new OnMapLoadListener() {
                        public void onMapLoad() {
                            BaiduMapStrategy.this.zoom2Bounds(build, i3, i4, customMapCallbackArr2);
                        }
                    });
                }
            }
        }
    }

    public void addMarkers(List<QMarker> list, boolean z, final int i, final int i2) {
        if (list != null && !list.isEmpty()) {
            final QMarker qMarker;
            if (list.size() == 1) {
                qMarker = (QMarker) list.get(0);
                if (qMarker != null) {
                    addMarker(qMarker);
                    synchronized (this.listeners) {
                        if (isMapLoaded()) {
                            mapCenter(qMarker.position, true, 300);
                        } else {
                            this.listeners.add(new OnMapLoadListener() {
                                public void onMapLoad() {
                                    BaiduMapStrategy.this.mapCenter(qMarker.position, true, 300);
                                }
                            });
                        }
                    }
                    return;
                }
                return;
            }
            Builder builder = new Builder();
            QLocation qLocation;
            if (z) {
                BitmapDescriptor createBitmapDescriptor = MapHelperUtils.createBitmapDescriptor((QMarker) list.get(0));
                for (QMarker qMarker2 : list) {
                    addMarker(qMarker2, createBitmapDescriptor);
                    qLocation = qMarker2.position;
                    builder.include(new LatLng(qLocation.getLatitude(), qLocation.getLongitude()));
                }
            } else {
                for (QMarker qMarker22 : list) {
                    addMarker(qMarker22, MapHelperUtils.createBitmapDescriptor(qMarker22));
                    qLocation = qMarker22.position;
                    builder.include(new LatLng(qLocation.getLatitude(), qLocation.getLongitude()));
                }
            }
            if (this.isZoomToBounds) {
                final LatLngBounds build = builder.build();
                synchronized (this.listeners) {
                    if (isMapLoaded()) {
                        zoom2Bounds(build, i, i2, new CustomMapCallback[0]);
                    } else {
                        this.listeners.add(new OnMapLoadListener() {
                            public void onMapLoad() {
                                BaiduMapStrategy.this.zoom2Bounds(build, i, i2, new CustomMapCallback[0]);
                            }
                        });
                    }
                }
            }
        }
    }

    public void mapCenterTo(int i, int i2) {
        if (this.bdMap != null) {
            MapStatus mapStatus = this.bdMap.getMapStatus();
            MapStatusUpdate newMapStatus = MapStatusUpdateFactory.newMapStatus(new MapStatus.Builder().overlook(mapStatus.overlook).rotate(mapStatus.rotate).targetScreen(new Point(i, i2)).build());
            if (this.bdMap != null) {
                this.bdMap.setMapStatus(newMapStatus);
            }
        }
    }

    private void zoom2Bounds(LatLngBounds latLngBounds, int i, int i2, CustomMapCallback... customMapCallbackArr) {
        MapStatusUpdate newLatLngBounds;
        if (i <= 0 || i2 <= 0) {
            newLatLngBounds = MapStatusUpdateFactory.newLatLngBounds(latLngBounds);
        } else {
            newLatLngBounds = MapStatusUpdateFactory.newLatLngBounds(latLngBounds, i, i2);
        }
        if (this.bdMap != null) {
            if (customMapCallbackArr.length > 0) {
                for (CustomMapCallback onCallback : customMapCallbackArr) {
                    onCallback.onCallback();
                }
            }
            this.bdMap.setMapStatus(newLatLngBounds);
        }
    }

    public void setViewPadding(int i, int i2, int i3, int i4) {
        if (this.bdMap != null && isMapLoaded()) {
            this.bdMap.setPadding(i, i2, i3, i4);
        }
    }

    public void reSetMarkPosition(QMarker qMarker, QLocation qLocation) {
        Object overlayOption = qMarker.getOverlayOption();
        if (overlayOption != null && qLocation != null && (overlayOption instanceof Marker)) {
            ((Marker) overlayOption).setPosition(new LatLng(qLocation.getLatitude(), qLocation.getLongitude()));
            qMarker.position = qLocation;
        }
    }

    public void reSetMarkRotate(QMarker qMarker, float f) {
        Object overlayOption = qMarker.getOverlayOption();
        if (overlayOption != null && (overlayOption instanceof Marker)) {
            ((Marker) overlayOption).setRotate(f);
            qMarker.setRotate(f);
        }
    }

    @Deprecated
    public void destroy(String str) {
        if (str != null && TEMPTOKEN.MAP_DESTROY_TOKEN.equals(str)) {
            if (this.infoWindowBitmapDescriptor != null) {
                this.infoWindowBitmapDescriptor.recycle();
                this.infoWindowBitmapDescriptor = null;
            }
            if (this.bdMap != null) {
                clear();
                this.bdMap = null;
            }
        }
    }
}
