package com.baidu.mapapi.map;

import android.graphics.Bitmap;
import android.graphics.Point;
import android.view.MotionEvent;
import com.baidu.mapapi.map.BaiduMap.OnMarkerClickListener;
import com.baidu.mapapi.map.BaiduMap.OnPolylineClickListener;
import com.baidu.mapapi.map.InfoWindow.OnInfoWindowClickListener;
import com.baidu.mapapi.model.CoordUtil;
import com.baidu.mapapi.model.inner.GeoPoint;
import com.baidu.platform.comapi.map.E;
import com.baidu.platform.comapi.map.l;
import java.util.Iterator;
import javax.microedition.khronos.opengles.GL10;
import org.json.JSONException;
import org.json.JSONObject;

class b implements l {
    final /* synthetic */ BaiduMap a;

    b(BaiduMap baiduMap) {
        this.a = baiduMap;
    }

    public void a() {
    }

    public void a(Bitmap bitmap) {
        if (this.a.z != null) {
            this.a.z.onSnapshotReady(bitmap);
        }
    }

    public void a(MotionEvent motionEvent) {
        if (this.a.p != null) {
            this.a.p.onTouch(motionEvent);
        }
    }

    public void a(GeoPoint geoPoint) {
        if (this.a.q != null) {
            this.a.q.onMapClick(CoordUtil.mc2ll(geoPoint));
        }
    }

    public void a(E e) {
        if (this.a.I != null) {
            this.a.I.setVisibility(4);
        }
        if (this.a.o != null) {
            this.a.o.onMapStatusChangeStart(MapStatus.a(e));
        }
    }

    public void a(String str) {
        try {
            JSONObject jSONObject = new JSONObject(str);
            JSONObject optJSONObject = jSONObject.optJSONArray("dataset").optJSONObject(0);
            GeoPoint b = this.a.i.b(jSONObject.optInt("px"), jSONObject.optInt("py"));
            int optInt = optJSONObject.optInt("ty");
            if (optInt == 17) {
                if (this.a.q != null) {
                    MapPoi mapPoi = new MapPoi();
                    mapPoi.a(optJSONObject);
                    this.a.q.onMapPoiClick(mapPoi);
                }
            } else if (optInt == 18) {
                if (this.a.y != null) {
                    this.a.y.onMyLocationClick();
                } else {
                    a(b);
                }
            } else if (optInt == 19) {
                if (this.a.i != null) {
                    E D = this.a.i.D();
                    D.c = 0;
                    D.b = 0;
                    this.a.i.a(D, 300);
                }
            } else if (optInt == 90909) {
                String optString = optJSONObject.optString("marker_id");
                if (this.a.G == null || !optString.equals(this.a.H.p)) {
                    for (Overlay overlay : this.a.k) {
                        if ((overlay instanceof Marker) && overlay.p.equals(optString)) {
                            if (this.a.v.isEmpty()) {
                                a(b);
                            } else {
                                Iterator it = this.a.v.iterator();
                                while (it.hasNext()) {
                                    ((OnMarkerClickListener) it.next()).onMarkerClick((Marker) overlay);
                                }
                                return;
                            }
                        }
                    }
                    return;
                }
                OnInfoWindowClickListener onInfoWindowClickListener = this.a.G.d;
                if (onInfoWindowClickListener != null) {
                    onInfoWindowClickListener.onInfoWindowClick();
                } else {
                    a(b);
                }
            } else if (optInt == 90910) {
                String optString2 = optJSONObject.optString("polyline_id");
                for (Overlay overlay2 : this.a.k) {
                    if ((overlay2 instanceof Polyline) && overlay2.p.equals(optString2)) {
                        if (this.a.w.isEmpty()) {
                            a(b);
                        } else {
                            Iterator it2 = this.a.w.iterator();
                            while (it2.hasNext()) {
                                ((OnPolylineClickListener) it2.next()).onPolylineClick((Polyline) overlay2);
                            }
                        }
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void a(GL10 gl10, E e) {
        if (this.a.A != null) {
            this.a.A.onMapDrawFrame(MapStatus.a(e));
        }
    }

    public void a(boolean z) {
        if (this.a.B != null) {
            this.a.B.onBaseIndoorMapMode(z, this.a.getFocusedBaseIndoorMapInfo());
        }
    }

    public void b() {
        this.a.f = new Projection(this.a.i);
        this.a.P = true;
        if (this.a.r != null) {
            this.a.r.onMapLoaded();
        }
    }

    public void b(GeoPoint geoPoint) {
        if (this.a.t != null) {
            this.a.t.onMapDoubleClick(CoordUtil.mc2ll(geoPoint));
        }
    }

    public void b(E e) {
        if (this.a.o != null) {
            this.a.o.onMapStatusChange(MapStatus.a(e));
        }
    }

    public boolean b(String str) {
        try {
            JSONObject optJSONObject = new JSONObject(str).optJSONArray("dataset").optJSONObject(0);
            if (optJSONObject.optInt("ty") == 90909) {
                String optString = optJSONObject.optString("marker_id");
                if (this.a.H == null || !optString.equals(this.a.H.p)) {
                    for (Overlay overlay : this.a.k) {
                        if ((overlay instanceof Marker) && overlay.p.equals(optString)) {
                            Marker marker = (Marker) overlay;
                            if (marker.f) {
                                this.a.J = marker;
                                Point toScreenLocation = this.a.f.toScreenLocation(this.a.J.a);
                                this.a.J.setPosition(this.a.f.fromScreenLocation(new Point(toScreenLocation.x, toScreenLocation.y - 60)));
                                if (this.a.x != null) {
                                    this.a.x.onMarkerDragStart(this.a.J);
                                }
                                return true;
                            }
                        }
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return false;
    }

    public void c() {
        if (this.a.s != null) {
            this.a.s.onMapRenderFinished();
        }
    }

    public void c(GeoPoint geoPoint) {
        if (this.a.u != null) {
            this.a.u.onMapLongClick(CoordUtil.mc2ll(geoPoint));
        }
    }

    public void c(E e) {
        if (this.a.I != null) {
            this.a.I.setVisibility(0);
        }
        if (this.a.o != null) {
            this.a.o.onMapStatusChangeFinish(MapStatus.a(e));
        }
    }

    public void d() {
        this.a.E.lock();
        try {
            if (this.a.D != null) {
                this.a.D.a();
            }
            this.a.E.unlock();
        } catch (Throwable th) {
            this.a.E.unlock();
        }
    }

    public void d(GeoPoint geoPoint) {
        if (this.a.J != null && this.a.J.f) {
            Point toScreenLocation = this.a.f.toScreenLocation(CoordUtil.mc2ll(geoPoint));
            this.a.J.setPosition(this.a.f.fromScreenLocation(new Point(toScreenLocation.x, toScreenLocation.y - 60)));
            if (this.a.x != null && this.a.J.f) {
                this.a.x.onMarkerDrag(this.a.J);
            }
        }
    }

    public void e() {
        this.a.E.lock();
        try {
            if (this.a.D != null) {
                this.a.D.a();
                this.a.i.n();
            }
            this.a.E.unlock();
        } catch (Throwable th) {
            this.a.E.unlock();
        }
    }

    public void e(GeoPoint geoPoint) {
        if (this.a.J != null && this.a.J.f) {
            Point toScreenLocation = this.a.f.toScreenLocation(CoordUtil.mc2ll(geoPoint));
            this.a.J.setPosition(this.a.f.fromScreenLocation(new Point(toScreenLocation.x, toScreenLocation.y - 60)));
            if (this.a.x != null && this.a.J.f) {
                this.a.x.onMarkerDragEnd(this.a.J);
            }
            this.a.J = null;
        }
    }

    public void f() {
        this.a.i.b(false);
        this.a.E.lock();
        try {
            if (this.a.D != null) {
                this.a.a(this.a.D);
            }
            this.a.E.unlock();
        } catch (Throwable th) {
            this.a.E.unlock();
        }
    }
}
