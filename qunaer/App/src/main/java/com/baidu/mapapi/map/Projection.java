package com.baidu.mapapi.map;

import android.graphics.Point;
import android.graphics.PointF;
import com.baidu.mapapi.model.CoordUtil;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.model.inner.GeoPoint;
import com.baidu.platform.comapi.map.C0005e;
import com.baidu.platform.comapi.map.E;
import com.baidu.platform.comapi.map.E.a;

public final class Projection {
    private C0005e a;

    Projection(C0005e c0005e) {
        this.a = c0005e;
    }

    public LatLng fromScreenLocation(Point point) {
        return (point == null || this.a == null) ? null : CoordUtil.mc2ll(this.a.b(point.x, point.y));
    }

    public float metersToEquatorPixels(float f) {
        return f <= 0.0f ? 0.0f : (float) (((double) f) / this.a.I());
    }

    public PointF toOpenGLLocation(LatLng latLng, MapStatus mapStatus) {
        if (latLng == null || mapStatus == null) {
            return null;
        }
        GeoPoint ll2mc = CoordUtil.ll2mc(latLng);
        E e = mapStatus.a;
        return new PointF((float) ((ll2mc.getLongitudeE6() - e.d) / e.n), (float) ((ll2mc.getLatitudeE6() - e.e) / e.n));
    }

    public PointF toOpenGLNormalization(LatLng latLng, MapStatus mapStatus) {
        if (latLng == null || mapStatus == null) {
            return null;
        }
        GeoPoint ll2mc = CoordUtil.ll2mc(latLng);
        a aVar = mapStatus.a.k;
        return new PointF((float) (((2.0d * (ll2mc.getLongitudeE6() - ((double) aVar.a))) / ((double) Math.abs(aVar.b - aVar.a))) - WeightedLatLng.DEFAULT_INTENSITY), (float) ((((ll2mc.getLatitudeE6() - ((double) aVar.d)) * 2.0d) / ((double) Math.abs(aVar.c - aVar.d))) - WeightedLatLng.DEFAULT_INTENSITY));
    }

    public Point toScreenLocation(LatLng latLng) {
        if (latLng == null || this.a == null) {
            return null;
        }
        return this.a.a(CoordUtil.ll2mc(latLng));
    }
}
