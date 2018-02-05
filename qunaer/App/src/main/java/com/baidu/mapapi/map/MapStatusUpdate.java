package com.baidu.mapapi.map;

import android.graphics.Point;
import com.baidu.mapapi.model.CoordUtil;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.model.LatLngBounds;
import com.baidu.mapapi.model.inner.GeoPoint;
import com.baidu.platform.comapi.map.C0005e;

public final class MapStatusUpdate {
    private static final String l = MapStatusUpdate.class.getSimpleName();
    int a;
    MapStatus b;
    LatLng c;
    LatLngBounds d;
    int e;
    int f;
    float g;
    int h;
    int i;
    float j;
    Point k;

    MapStatusUpdate() {
    }

    MapStatusUpdate(int i) {
        this.a = i;
    }

    MapStatus a(C0005e c0005e, MapStatus mapStatus) {
        GeoPoint ll2mc;
        GeoPoint ll2mc2;
        float a;
        switch (this.a) {
            case 1:
                return this.b;
            case 2:
                return new MapStatus(mapStatus.rotate, this.c, mapStatus.overlook, mapStatus.zoom, mapStatus.targetScreen, null);
            case 3:
                ll2mc = CoordUtil.ll2mc(this.d.southwest);
                ll2mc2 = CoordUtil.ll2mc(this.d.northeast);
                double longitudeE6 = ll2mc.getLongitudeE6();
                double latitudeE6 = ll2mc2.getLatitudeE6();
                double longitudeE62 = ll2mc2.getLongitudeE6();
                a = c0005e.a((int) longitudeE6, (int) latitudeE6, (int) longitudeE62, (int) ll2mc.getLatitudeE6(), mapStatus.a.j.right - mapStatus.a.j.left, mapStatus.a.j.bottom - mapStatus.a.j.top);
                return new MapStatus(mapStatus.rotate, this.d.getCenter(), mapStatus.overlook, a, mapStatus.targetScreen, null);
            case 4:
                return new MapStatus(mapStatus.rotate, this.c, mapStatus.overlook, this.g, mapStatus.targetScreen, null);
            case 5:
                c0005e.F();
                GeoPoint b = c0005e.b((c0005e.F() / 2) + this.h, (c0005e.G() / 2) + this.i);
                return new MapStatus(mapStatus.rotate, CoordUtil.mc2ll(b), mapStatus.overlook, mapStatus.zoom, mapStatus.targetScreen, b.getLongitudeE6(), b.getLatitudeE6(), null);
            case 6:
                return new MapStatus(mapStatus.rotate, mapStatus.target, mapStatus.overlook, mapStatus.zoom + this.j, mapStatus.targetScreen, mapStatus.a(), mapStatus.b(), null);
            case 7:
                return new MapStatus(mapStatus.rotate, CoordUtil.mc2ll(c0005e.b(this.k.x, this.k.y)), mapStatus.overlook, mapStatus.zoom + this.j, this.k, null);
            case 8:
                return new MapStatus(mapStatus.rotate, mapStatus.target, mapStatus.overlook, this.g, mapStatus.targetScreen, mapStatus.a(), mapStatus.b(), null);
            case 9:
                ll2mc = CoordUtil.ll2mc(this.d.southwest);
                ll2mc2 = CoordUtil.ll2mc(this.d.northeast);
                C0005e c0005e2 = c0005e;
                a = c0005e2.a((int) ll2mc.getLongitudeE6(), (int) ll2mc2.getLatitudeE6(), (int) ll2mc2.getLongitudeE6(), (int) ll2mc.getLatitudeE6(), this.e, this.f);
                return new MapStatus(mapStatus.rotate, this.d.getCenter(), mapStatus.overlook, a, mapStatus.targetScreen, null);
            default:
                return null;
        }
    }
}
