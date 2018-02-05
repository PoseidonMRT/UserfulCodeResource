package com.baidu.mapapi.map;

import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import com.baidu.mapapi.common.SysOSUtil;
import com.baidu.mapapi.map.MapBaseIndoorMapInfo.SwitchFloorError;
import com.baidu.mapapi.map.MapStatus.Builder;
import com.baidu.mapapi.map.MapViewLayoutParams.ELayoutMode;
import com.baidu.mapapi.map.MyLocationConfiguration.LocationMode;
import com.baidu.mapapi.model.CoordUtil;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.model.LatLngBounds;
import com.baidu.mapapi.model.ParcelItem;
import com.baidu.mapapi.model.inner.GeoPoint;
import com.baidu.platform.comapi.map.C0001D;
import com.baidu.platform.comapi.map.C0005e;
import com.baidu.platform.comapi.map.C0008h;
import com.baidu.platform.comapi.map.C0010j;
import com.baidu.platform.comapi.map.E;
import com.baidu.platform.comapi.map.F;
import com.iflytek.cloud.SpeechEvent;
import com.mqunar.libtask.ProgressType;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import javax.microedition.khronos.opengles.GL10;
import org.apache.http.HttpStatus;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class BaiduMap {
    public static final int MAP_TYPE_NONE = 3;
    public static final int MAP_TYPE_NORMAL = 1;
    public static final int MAP_TYPE_SATELLITE = 2;
    private static final String e = BaiduMap.class.getSimpleName();
    private OnMapDrawFrameCallback A;
    private OnBaseIndoorMapListener B;
    private TileOverlay C;
    private HeatMap D;
    private Lock E = new ReentrantLock();
    private Lock F = new ReentrantLock();
    private InfoWindow G;
    private Marker H;
    private View I;
    private Marker J;
    private MyLocationData K;
    private MyLocationConfiguration L;
    private boolean M;
    private boolean N;
    private boolean O;
    private boolean P;
    private Point Q;
    MapView a;
    TextureMapView b;
    WearMapView c;
    C0001D d;
    private Projection f;
    private UiSettings g;
    private C0010j h;
    private C0005e i;
    private F j;
    private List<Overlay> k;
    private List<Marker> l;
    private List<Marker> m;
    private a n;
    private OnMapStatusChangeListener o;
    private OnMapTouchListener p;
    private OnMapClickListener q;
    private OnMapLoadedCallback r;
    private OnMapRenderCallback s;
    private OnMapDoubleClickListener t;
    private OnMapLongClickListener u;
    private CopyOnWriteArrayList<OnMarkerClickListener> v = new CopyOnWriteArrayList();
    private CopyOnWriteArrayList<OnPolylineClickListener> w = new CopyOnWriteArrayList();
    private OnMarkerDragListener x;
    private OnMyLocationClickListener y;
    private SnapshotReadyCallback z;

    public interface OnBaseIndoorMapListener {
        void onBaseIndoorMapMode(boolean z, MapBaseIndoorMapInfo mapBaseIndoorMapInfo);
    }

    public interface OnMapClickListener {
        void onMapClick(LatLng latLng);

        boolean onMapPoiClick(MapPoi mapPoi);
    }

    public interface OnMapDoubleClickListener {
        void onMapDoubleClick(LatLng latLng);
    }

    public interface OnMapDrawFrameCallback {
        void onMapDrawFrame(MapStatus mapStatus);

        @Deprecated
        void onMapDrawFrame(GL10 gl10, MapStatus mapStatus);
    }

    public interface OnMapLoadedCallback {
        void onMapLoaded();
    }

    public interface OnMapLongClickListener {
        void onMapLongClick(LatLng latLng);
    }

    public interface OnMapRenderCallback {
        void onMapRenderFinished();
    }

    public interface OnMapStatusChangeListener {
        void onMapStatusChange(MapStatus mapStatus);

        void onMapStatusChangeFinish(MapStatus mapStatus);

        void onMapStatusChangeStart(MapStatus mapStatus);
    }

    public interface OnMapTouchListener {
        void onTouch(MotionEvent motionEvent);
    }

    public interface OnMarkerClickListener {
        boolean onMarkerClick(Marker marker);
    }

    public interface OnMarkerDragListener {
        void onMarkerDrag(Marker marker);

        void onMarkerDragEnd(Marker marker);

        void onMarkerDragStart(Marker marker);
    }

    public interface OnMyLocationClickListener {
        boolean onMyLocationClick();
    }

    public interface OnPolylineClickListener {
        boolean onPolylineClick(Polyline polyline);
    }

    public interface SnapshotReadyCallback {
        void onSnapshotReady(Bitmap bitmap);
    }

    BaiduMap(F f) {
        this.j = f;
        this.i = this.j.b();
        this.d = C0001D.TextureView;
        c();
    }

    BaiduMap(C0010j c0010j) {
        this.h = c0010j;
        this.i = this.h.a();
        this.d = C0001D.GLSurfaceView;
        c();
    }

    private Point a(String str) {
        if (TextUtils.isEmpty(str)) {
            return null;
        }
        int i = 0;
        int i2 = 0;
        for (String replaceAll : str.replaceAll("^\\{", "").replaceAll("\\}$", "").split(",")) {
            String[] split = replaceAll.replaceAll("\"", "").split(":");
            if (MapViewConstants.ATTR_X.equals(split[0])) {
                i2 = Integer.valueOf(split[1]).intValue();
            }
            if (MapViewConstants.ATTR_Y.equals(split[0])) {
                i = Integer.valueOf(split[1]).intValue();
            }
        }
        return new Point(i2, i);
    }

    private E a(MapStatusUpdate mapStatusUpdate) {
        if (this.i == null) {
            return null;
        }
        return mapStatusUpdate.a(this.i, getMapStatus()).b(this.i.D());
    }

    private final void a(MyLocationData myLocationData, MyLocationConfiguration myLocationConfiguration) {
        if (myLocationData != null && myLocationConfiguration != null && isMyLocationEnabled()) {
            Bundle bundle;
            JSONObject jSONObject = new JSONObject();
            JSONArray jSONArray = new JSONArray();
            JSONObject jSONObject2 = new JSONObject();
            JSONObject jSONObject3 = new JSONObject();
            LatLng latLng = new LatLng(myLocationData.latitude, myLocationData.longitude);
            GeoPoint ll2mc = CoordUtil.ll2mc(latLng);
            try {
                jSONObject.put("type", 0);
                jSONObject2.put("ptx", ll2mc.getLongitudeE6());
                jSONObject2.put("pty", ll2mc.getLatitudeE6());
                jSONObject2.put("radius", (double) ((float) CoordUtil.getMCDistanceByOneLatLngAndRadius(latLng, (int) myLocationData.accuracy)));
                float f = myLocationData.direction;
                if (myLocationConfiguration.enableDirection) {
                    f = myLocationData.direction % 360.0f;
                    if (f > 180.0f) {
                        f -= 360.0f;
                    } else if (f < -180.0f) {
                        f += 360.0f;
                    }
                } else {
                    f = -1.0f;
                }
                jSONObject2.put("direction", (double) f);
                jSONObject2.put("iconarrownor", "NormalLocArrow");
                jSONObject2.put("iconarrownorid", 28);
                jSONObject2.put("iconarrowfoc", "FocusLocArrow");
                jSONObject2.put("iconarrowfocid", 29);
                jSONObject2.put("lineid", myLocationConfiguration.accuracyCircleStrokeColor);
                jSONObject2.put("areaid", myLocationConfiguration.accuracyCircleFillColor);
                jSONArray.put(jSONObject2);
                jSONObject.put(SpeechEvent.KEY_EVENT_RECORD_DATA, jSONArray);
                if (myLocationConfiguration.locationMode == LocationMode.COMPASS) {
                    jSONObject3.put("ptx", ll2mc.getLongitudeE6());
                    jSONObject3.put("pty", ll2mc.getLatitudeE6());
                    jSONObject3.put("radius", 0);
                    jSONObject3.put("direction", 0);
                    jSONObject3.put("iconarrownor", "direction_wheel");
                    jSONObject3.put("iconarrownorid", 54);
                    jSONObject3.put("iconarrowfoc", "direction_wheel");
                    jSONObject3.put("iconarrowfocid", 54);
                    jSONArray.put(jSONObject3);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (myLocationConfiguration.customMarker == null) {
                bundle = null;
            } else {
                List<BitmapDescriptor> arrayList = new ArrayList();
                arrayList.add(myLocationConfiguration.customMarker);
                Bundle bundle2 = new Bundle();
                ArrayList arrayList2 = new ArrayList();
                for (BitmapDescriptor bitmapDescriptor : arrayList) {
                    ParcelItem parcelItem = new ParcelItem();
                    Bundle bundle3 = new Bundle();
                    Bitmap bitmap = bitmapDescriptor.a;
                    Buffer allocate = ByteBuffer.allocate((bitmap.getWidth() * bitmap.getHeight()) * 4);
                    bitmap.copyPixelsToBuffer(allocate);
                    bundle3.putByteArray("imgdata", allocate.array());
                    bundle3.putInt("imgindex", bitmapDescriptor.hashCode());
                    bundle3.putInt("imgH", bitmap.getHeight());
                    bundle3.putInt("imgW", bitmap.getWidth());
                    parcelItem.setBundle(bundle3);
                    arrayList2.add(parcelItem);
                }
                if (arrayList2.size() > 0) {
                    Parcelable[] parcelableArr = new ParcelItem[arrayList2.size()];
                    for (int i = 0; i < arrayList2.size(); i++) {
                        parcelableArr[i] = (ParcelItem) arrayList2.get(i);
                    }
                    bundle2.putParcelableArray("icondata", parcelableArr);
                }
                bundle = bundle2;
            }
            if (this.i != null) {
                this.i.a(jSONObject.toString(), bundle);
            }
            switch (myLocationConfiguration.locationMode) {
                case COMPASS:
                    animateMapStatus(MapStatusUpdateFactory.newMapStatus(new Builder().rotate(myLocationData.direction).overlook(-45.0f).target(new LatLng(myLocationData.latitude, myLocationData.longitude)).targetScreen(getMapStatus().targetScreen).zoom(getMapStatus().zoom).build()));
                    return;
                case FOLLOWING:
                    animateMapStatus(MapStatusUpdateFactory.newMapStatus(new Builder().target(new LatLng(myLocationData.latitude, myLocationData.longitude)).zoom(getMapStatus().zoom).rotate(getMapStatus().rotate).overlook(getMapStatus().overlook).targetScreen(getMapStatus().targetScreen).build()));
                    return;
                case NORMAL:
                    return;
                default:
                    return;
            }
        }
    }

    private void c() {
        this.k = new CopyOnWriteArrayList();
        this.l = new CopyOnWriteArrayList();
        this.m = new CopyOnWriteArrayList();
        this.Q = new Point((int) (SysOSUtil.getDensity() * 40.0f), (int) (SysOSUtil.getDensity() * 40.0f));
        this.g = new UiSettings(this.i);
        this.n = new a(this);
        this.i.a(new b(this));
        this.i.a(new c(this));
        this.i.a(new d(this));
        this.M = this.i.B();
        this.N = this.i.C();
    }

    void a() {
        if (this.i != null) {
            this.i.s();
        }
    }

    void a(HeatMap heatMap) {
        this.E.lock();
        try {
            if (!(this.D == null || this.i == null || heatMap != this.D)) {
                this.D.b();
                this.D.c();
                this.D.a = null;
                this.i.n();
                this.D = null;
                this.i.l(false);
            }
            this.E.unlock();
        } catch (Throwable th) {
            this.E.unlock();
        }
    }

    void a(TileOverlay tileOverlay) {
        this.F.lock();
        if (tileOverlay != null) {
            try {
                if (this.C == tileOverlay) {
                    tileOverlay.b();
                    tileOverlay.a = null;
                    if (this.i != null) {
                        this.i.c(false);
                    }
                }
            } catch (Throwable th) {
                this.C = null;
                this.F.unlock();
            }
        }
        this.C = null;
        this.F.unlock();
    }

    public void addHeatMap(HeatMap heatMap) {
        if (heatMap != null) {
            this.E.lock();
            try {
                if (heatMap != this.D) {
                    if (this.D != null) {
                        this.D.b();
                        this.D.c();
                        this.D.a = null;
                        this.i.n();
                    }
                    this.D = heatMap;
                    this.D.a = this;
                    this.i.l(true);
                    this.E.unlock();
                }
            } finally {
                this.E.unlock();
            }
        }
    }

    public final Overlay addOverlay(OverlayOptions overlayOptions) {
        if (overlayOptions == null) {
            return null;
        }
        Overlay a = overlayOptions.a();
        a.listener = this.n;
        if (a instanceof Marker) {
            Marker marker = (Marker) a;
            if (!(marker.n == null || marker.n.size() == 0)) {
                this.l.add(marker);
                if (this.i != null) {
                    this.i.b(true);
                }
            }
            this.m.add(marker);
        }
        Bundle bundle = new Bundle();
        a.a(bundle);
        if (this.i != null) {
            this.i.b(bundle);
        }
        this.k.add(a);
        return a;
    }

    public final List<Overlay> addOverlays(List<OverlayOptions> list) {
        if (list == null) {
            return null;
        }
        List<Overlay> arrayList = new ArrayList();
        Bundle[] bundleArr = new Bundle[list.size()];
        int i = 0;
        for (OverlayOptions overlayOptions : list) {
            if (overlayOptions != null) {
                Bundle bundle = new Bundle();
                Overlay a = overlayOptions.a();
                a.listener = this.n;
                if (a instanceof Marker) {
                    Marker marker = (Marker) a;
                    if (!(marker.n == null || marker.n.size() == 0)) {
                        this.l.add(marker);
                        if (this.i != null) {
                            this.i.b(true);
                        }
                    }
                    this.m.add(marker);
                }
                this.k.add(a);
                arrayList.add(a);
                a.a(bundle);
                bundleArr[i] = bundle;
                i++;
            }
        }
        i = bundleArr.length / HttpStatus.SC_BAD_REQUEST;
        int i2 = 0;
        while (i2 < i + 1) {
            List arrayList2 = new ArrayList();
            int i3 = 0;
            while (i3 < HttpStatus.SC_BAD_REQUEST && (i2 * HttpStatus.SC_BAD_REQUEST) + i3 < bundleArr.length) {
                if (bundleArr[(i2 * HttpStatus.SC_BAD_REQUEST) + i3] != null) {
                    arrayList2.add(bundleArr[(i2 * HttpStatus.SC_BAD_REQUEST) + i3]);
                }
                i3++;
            }
            if (this.i != null) {
                this.i.a(arrayList2);
            }
            i2++;
        }
        return arrayList;
    }

    public TileOverlay addTileLayer(TileOverlayOptions tileOverlayOptions) {
        if (tileOverlayOptions == null) {
            return null;
        }
        if (this.C != null) {
            this.C.b();
            this.C.a = null;
        }
        if (this.i == null || !this.i.a(tileOverlayOptions.a())) {
            return null;
        }
        TileOverlay a = tileOverlayOptions.a(this);
        this.C = a;
        return a;
    }

    public final void animateMapStatus(MapStatusUpdate mapStatusUpdate) {
        animateMapStatus(mapStatusUpdate, 300);
    }

    public final void animateMapStatus(MapStatusUpdate mapStatusUpdate, int i) {
        if (mapStatusUpdate != null && i > 0) {
            E a = a(mapStatusUpdate);
            if (this.i == null) {
                return;
            }
            if (this.P) {
                this.i.a(a, i);
            } else {
                this.i.a(a);
            }
        }
    }

    boolean b() {
        return this.i == null ? false : this.i.d();
    }

    public final void clear() {
        this.k.clear();
        this.l.clear();
        this.m.clear();
        if (this.i != null) {
            this.i.b(false);
            this.i.m();
        }
        hideInfoWindow();
    }

    public final Point getCompassPosition() {
        return this.i != null ? a(this.i.g()) : null;
    }

    public MapBaseIndoorMapInfo getFocusedBaseIndoorMapInfo() {
        return this.i.o();
    }

    public final MyLocationConfiguration getLocationConfigeration() {
        return getLocationConfiguration();
    }

    public final MyLocationConfiguration getLocationConfiguration() {
        return this.L;
    }

    public final MyLocationData getLocationData() {
        return this.K;
    }

    public final MapStatus getMapStatus() {
        return this.i == null ? null : MapStatus.a(this.i.D());
    }

    public final LatLngBounds getMapStatusLimit() {
        return this.i == null ? null : this.i.E();
    }

    public final int getMapType() {
        return this.i == null ? 1 : !this.i.k() ? 3 : this.i.j() ? 2 : 1;
    }

    public List<Marker> getMarkersInBounds(LatLngBounds latLngBounds) {
        if (getMapStatus() == null) {
            return null;
        }
        List<Marker> arrayList = new ArrayList();
        if (this.m.size() == 0) {
            return null;
        }
        for (Marker marker : this.m) {
            if (latLngBounds.contains(marker.getPosition())) {
                arrayList.add(marker);
            }
        }
        return arrayList;
    }

    public final float getMaxZoomLevel() {
        return this.i == null ? 0.0f : this.i.a;
    }

    public final float getMinZoomLevel() {
        return this.i == null ? 0.0f : this.i.b;
    }

    public final Projection getProjection() {
        return this.f;
    }

    public final UiSettings getUiSettings() {
        return this.g;
    }

    public void hideInfoWindow() {
        if (this.G != null) {
            if (this.G.b != null) {
                switch (this.d) {
                    case TextureView:
                        if (this.b != null) {
                            this.b.removeView(this.I);
                            break;
                        }
                        break;
                    case GLSurfaceView:
                        if (this.h != null) {
                            this.a.removeView(this.I);
                            break;
                        }
                        break;
                }
                this.I = null;
            }
            this.G = null;
            this.H.remove();
            this.H = null;
        }
    }

    public final boolean isBaiduHeatMapEnabled() {
        return this.i == null ? false : this.i.h();
    }

    public boolean isBaseIndoorMapMode() {
        return this.i.p();
    }

    public final boolean isBuildingsEnabled() {
        return this.i == null ? false : this.i.l();
    }

    public final boolean isMyLocationEnabled() {
        return this.i == null ? false : this.i.r();
    }

    public final boolean isSupportBaiduHeatMap() {
        return this.i == null ? false : this.i.i();
    }

    public final boolean isTrafficEnabled() {
        return this.i == null ? false : this.i.f();
    }

    public final void removeMarkerClickListener(OnMarkerClickListener onMarkerClickListener) {
        if (this.v.contains(onMarkerClickListener)) {
            this.v.remove(onMarkerClickListener);
        }
    }

    public final void setBaiduHeatMapEnabled(boolean z) {
        if (this.i != null) {
            this.i.e(z);
        }
    }

    public final void setBuildingsEnabled(boolean z) {
        if (this.i != null) {
            this.i.g(z);
        }
    }

    public void setCompassIcon(Bitmap bitmap) {
        if (bitmap == null) {
            throw new IllegalArgumentException("compass's icon can not be null");
        }
        this.i.a(bitmap);
    }

    public void setCompassPosition(Point point) {
        if (this.i.a(point)) {
            this.Q = point;
        }
    }

    public final void setIndoorEnable(boolean z) {
        if (this.i != null) {
            this.O = z;
            this.i.i(z);
        }
        if (this.B != null && !z) {
            this.B.onBaseIndoorMapMode(false, null);
        }
    }

    public final void setMapStatus(MapStatusUpdate mapStatusUpdate) {
        if (mapStatusUpdate != null) {
            E a = a(mapStatusUpdate);
            if (this.i != null) {
                this.i.a(a);
                if (this.o != null) {
                    this.o.onMapStatusChange(getMapStatus());
                }
            }
        }
    }

    public final void setMapStatusLimits(LatLngBounds latLngBounds) {
        if (this.i != null) {
            this.i.a(latLngBounds);
            setMapStatus(MapStatusUpdateFactory.newLatLngBounds(latLngBounds));
        }
    }

    public final void setMapType(int i) {
        if (this.i != null) {
            switch (i) {
                case 1:
                    this.i.a(false);
                    this.i.q(this.M);
                    this.i.r(this.N);
                    this.i.d(true);
                    this.i.i(this.O);
                    break;
                case 2:
                    this.i.a(true);
                    this.i.q(this.M);
                    this.i.r(this.N);
                    this.i.d(true);
                    break;
                case 3:
                    if (this.i.B()) {
                        this.i.q(false);
                    }
                    if (this.i.C()) {
                        this.i.r(false);
                    }
                    this.i.d(false);
                    this.i.i(false);
                    break;
            }
            if (this.h != null) {
                this.h.a(i);
            }
        }
    }

    public final void setMaxAndMinZoomLevel(float f, float f2) {
        if (f <= 21.0f && f2 >= 3.0f && f >= f2 && this.i != null) {
            this.i.a(f, f2);
        }
    }

    public final void setMyLocationConfigeration(MyLocationConfiguration myLocationConfiguration) {
        setMyLocationConfiguration(myLocationConfiguration);
    }

    public final void setMyLocationConfiguration(MyLocationConfiguration myLocationConfiguration) {
        this.L = myLocationConfiguration;
        a(this.K, this.L);
    }

    public final void setMyLocationData(MyLocationData myLocationData) {
        this.K = myLocationData;
        if (this.L == null) {
            this.L = new MyLocationConfiguration(LocationMode.NORMAL, false, null);
        }
        a(myLocationData, this.L);
    }

    public final void setMyLocationEnabled(boolean z) {
        if (this.i != null) {
            this.i.k(z);
        }
    }

    public final void setOnBaseIndoorMapListener(OnBaseIndoorMapListener onBaseIndoorMapListener) {
        this.B = onBaseIndoorMapListener;
    }

    public final void setOnMapClickListener(OnMapClickListener onMapClickListener) {
        this.q = onMapClickListener;
    }

    public final void setOnMapDoubleClickListener(OnMapDoubleClickListener onMapDoubleClickListener) {
        this.t = onMapDoubleClickListener;
    }

    public final void setOnMapDrawFrameCallback(OnMapDrawFrameCallback onMapDrawFrameCallback) {
        this.A = onMapDrawFrameCallback;
    }

    public void setOnMapLoadedCallback(OnMapLoadedCallback onMapLoadedCallback) {
        this.r = onMapLoadedCallback;
    }

    public final void setOnMapLongClickListener(OnMapLongClickListener onMapLongClickListener) {
        this.u = onMapLongClickListener;
    }

    public void setOnMapRenderCallbadk(OnMapRenderCallback onMapRenderCallback) {
        this.s = onMapRenderCallback;
    }

    public final void setOnMapStatusChangeListener(OnMapStatusChangeListener onMapStatusChangeListener) {
        this.o = onMapStatusChangeListener;
    }

    public final void setOnMapTouchListener(OnMapTouchListener onMapTouchListener) {
        this.p = onMapTouchListener;
    }

    public final void setOnMarkerClickListener(OnMarkerClickListener onMarkerClickListener) {
        if (onMarkerClickListener != null && !this.v.contains(onMarkerClickListener)) {
            this.v.add(onMarkerClickListener);
        }
    }

    public final void setOnMarkerDragListener(OnMarkerDragListener onMarkerDragListener) {
        this.x = onMarkerDragListener;
    }

    public final void setOnMyLocationClickListener(OnMyLocationClickListener onMyLocationClickListener) {
        this.y = onMyLocationClickListener;
    }

    public final void setOnPolylineClickListener(OnPolylineClickListener onPolylineClickListener) {
        if (onPolylineClickListener != null) {
            this.w.add(onPolylineClickListener);
        }
    }

    @Deprecated
    public final void setPadding(int i, int i2, int i3, int i4) {
        if (i >= 0 && i2 >= 0 && i3 >= 0 && i4 >= 0 && this.i != null) {
            this.i.D();
            float width;
            float height;
            MapStatusUpdate newMapStatus;
            switch (this.d) {
                case TextureView:
                    if (this.b != null) {
                        width = ((float) ((this.b.getWidth() - i) - i3)) / ((float) this.b.getWidth());
                        height = ((float) ((this.b.getHeight() - i2) - i4)) / ((float) this.b.getHeight());
                        newMapStatus = MapStatusUpdateFactory.newMapStatus(new Builder().targetScreen(new Point(((this.b.getWidth() + i) - i3) / 2, ((this.b.getHeight() + i2) - i4) / 2)).build());
                        this.i.a(new Point((int) ((width * ((float) this.Q.x)) + ((float) i)), (int) ((height * ((float) this.Q.y)) + ((float) i2))));
                        setMapStatus(newMapStatus);
                        this.b.setPadding(i, i2, i3, i4);
                        this.b.invalidate();
                        return;
                    }
                    return;
                case GLSurfaceView:
                    if (this.a != null) {
                        width = ((float) ((this.a.getWidth() - i) - i3)) / ((float) this.a.getWidth());
                        height = ((float) ((this.a.getHeight() - i2) - i4)) / ((float) this.a.getHeight());
                        newMapStatus = MapStatusUpdateFactory.newMapStatus(new Builder().targetScreen(new Point(((this.a.getWidth() + i) - i3) / 2, ((this.a.getHeight() + i2) - i4) / 2)).build());
                        this.i.a(new Point((int) ((width * ((float) this.Q.x)) + ((float) i)), (int) ((height * ((float) this.Q.y)) + ((float) i2))));
                        setMapStatus(newMapStatus);
                        this.a.setPadding(i, i2, i3, i4);
                        this.a.invalidate();
                        return;
                    }
                    return;
                default:
                    return;
            }
        }
    }

    public final void setTrafficEnabled(boolean z) {
        if (this.i != null) {
            this.i.f(z);
        }
    }

    public final void setViewPadding(int i, int i2, int i3, int i4) {
        if (i >= 0 && i2 >= 0 && i3 >= 0 && i4 >= 0 && this.i != null) {
            float f;
            switch (this.d) {
                case TextureView:
                    if (this.b != null) {
                        f = (float) i;
                        f = (float) i2;
                        this.i.a(new Point((int) (((((float) ((this.b.getWidth() - i) - i3)) / ((float) this.b.getWidth())) * ((float) this.Q.x)) + f), (int) (((((float) ((this.b.getHeight() - i2) - i4)) / ((float) this.b.getHeight())) * ((float) this.Q.y)) + f)));
                        this.b.setPadding(i, i2, i3, i4);
                        this.b.invalidate();
                        return;
                    }
                    return;
                case GLSurfaceView:
                    if (this.a != null) {
                        f = (float) i;
                        f = (float) i2;
                        this.i.a(new Point((int) (((((float) ((this.a.getWidth() - i) - i3)) / ((float) this.a.getWidth())) * ((float) this.Q.x)) + f), (int) (((((float) ((this.a.getHeight() - i2) - i4)) / ((float) this.a.getHeight())) * ((float) this.Q.y)) + f)));
                        this.a.setPadding(i, i2, i3, i4);
                        this.a.invalidate();
                        return;
                    }
                    return;
                default:
                    return;
            }
        }
    }

    public void showInfoWindow(InfoWindow infoWindow) {
        if (infoWindow != null) {
            hideInfoWindow();
            if (infoWindow.b != null) {
                this.I = infoWindow.b;
                this.I.destroyDrawingCache();
                LayoutParams build = new MapViewLayoutParams.Builder().layoutMode(ELayoutMode.mapMode).position(infoWindow.c).yOffset(infoWindow.e).build();
                switch (this.d) {
                    case TextureView:
                        if (this.b != null) {
                            this.b.addView(this.I, build);
                            break;
                        }
                        break;
                    case GLSurfaceView:
                        if (this.h != null) {
                            this.a.addView(this.I, build);
                            break;
                        }
                        break;
                }
            }
            this.G = infoWindow;
            Overlay a = new MarkerOptions().perspective(false).icon(infoWindow.b != null ? BitmapDescriptorFactory.fromView(infoWindow.b) : infoWindow.a).position(infoWindow.c).zIndex(ProgressType.PRO_END).a(infoWindow.e).a();
            a.listener = this.n;
            a.type = C0008h.popup;
            Bundle bundle = new Bundle();
            a.a(bundle);
            if (this.i != null) {
                this.i.b(bundle);
            }
            this.k.add(a);
            this.H = (Marker) a;
        }
    }

    public final void showMapIndoorPoi(boolean z) {
        if (this.i != null) {
            this.i.r(z);
            this.N = z;
        }
    }

    public final void showMapPoi(boolean z) {
        if (this.i != null) {
            this.i.q(z);
            this.M = z;
        }
    }

    public final void snapshot(SnapshotReadyCallback snapshotReadyCallback) {
        this.z = snapshotReadyCallback;
        switch (this.d) {
            case TextureView:
                if (this.j != null) {
                    this.j.a("anything", null);
                    return;
                }
                return;
            case GLSurfaceView:
                if (this.h != null) {
                    this.h.a("anything", null);
                    return;
                }
                return;
            default:
                return;
        }
    }

    public final void snapshotScope(Rect rect, SnapshotReadyCallback snapshotReadyCallback) {
        this.z = snapshotReadyCallback;
        switch (this.d) {
            case TextureView:
                if (this.j != null) {
                    this.j.a("anything", rect);
                    return;
                }
                return;
            case GLSurfaceView:
                if (this.h != null) {
                    this.h.a("anything", rect);
                    return;
                }
                return;
            default:
                return;
        }
    }

    public SwitchFloorError switchBaseIndoorMapFloor(String str, String str2) {
        if (TextUtils.isEmpty(str) || TextUtils.isEmpty(str2)) {
            return SwitchFloorError.FLOOR_INFO_ERROR;
        }
        MapBaseIndoorMapInfo focusedBaseIndoorMapInfo = getFocusedBaseIndoorMapInfo();
        if (!str2.equals(focusedBaseIndoorMapInfo.a)) {
            return SwitchFloorError.FOCUSED_ID_ERROR;
        }
        List floors = focusedBaseIndoorMapInfo.getFloors();
        return (floors == null || !floors.contains(str)) ? SwitchFloorError.FLOOR_OVERLFLOW : this.i.a(str, str2) ? SwitchFloorError.SWITCH_OK : SwitchFloorError.SWITCH_ERROR;
    }
}
