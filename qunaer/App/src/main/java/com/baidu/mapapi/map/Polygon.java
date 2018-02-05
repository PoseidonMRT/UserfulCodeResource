package com.baidu.mapapi.map;

import android.os.Bundle;
import com.baidu.mapapi.model.CoordUtil;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.model.inner.GeoPoint;
import com.baidu.platform.comapi.map.C0008h;
import java.util.List;

public final class Polygon extends Overlay {
    Stroke a;
    int b;
    List<LatLng> c;

    Polygon() {
        this.type = C0008h.polygon;
    }

    Bundle a(Bundle bundle) {
        super.a(bundle);
        GeoPoint ll2mc = CoordUtil.ll2mc((LatLng) this.c.get(0));
        bundle.putDouble("location_x", ll2mc.getLongitudeE6());
        bundle.putDouble("location_y", ll2mc.getLatitudeE6());
        Overlay.a(this.c, bundle);
        Overlay.a(this.b, bundle);
        if (this.a == null) {
            bundle.putInt("has_stroke", 0);
        } else {
            bundle.putInt("has_stroke", 1);
            bundle.putBundle("stroke", this.a.a(new Bundle()));
        }
        return bundle;
    }

    public int getFillColor() {
        return this.b;
    }

    public List<LatLng> getPoints() {
        return this.c;
    }

    public Stroke getStroke() {
        return this.a;
    }

    public void setFillColor(int i) {
        this.b = i;
        this.listener.b(this);
    }

    public void setPoints(List<LatLng> list) {
        if (list == null) {
            throw new IllegalArgumentException("points list can not be null");
        } else if (list.size() <= 2) {
            throw new IllegalArgumentException("points count can not less than three");
        } else if (list.contains(null)) {
            throw new IllegalArgumentException("points list can not contains null");
        } else {
            for (int i = 0; i < list.size(); i++) {
                for (int i2 = i + 1; i2 < list.size(); i2++) {
                    if (((LatLng) list.get(i)) == ((LatLng) list.get(i2))) {
                        throw new IllegalArgumentException("points list can not has same points");
                    }
                }
            }
            this.c = list;
            this.listener.b(this);
        }
    }

    public void setStroke(Stroke stroke) {
        this.a = stroke;
        this.listener.b(this);
    }
}
