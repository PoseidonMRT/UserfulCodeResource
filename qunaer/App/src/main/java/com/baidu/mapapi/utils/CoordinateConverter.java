package com.baidu.mapapi.utils;

import com.baidu.mapapi.model.CoordUtil;
import com.baidu.mapapi.model.LatLng;
import com.baidu.platform.comapi.location.CoordinateType;

public class CoordinateConverter {
    private LatLng a;
    private CoordType b;

    public enum CoordType {
        GPS,
        COMMON
    }

    private static LatLng a(LatLng latLng) {
        return a(latLng, CoordinateType.WGS84);
    }

    private static LatLng a(LatLng latLng, String str) {
        return latLng == null ? null : CoordUtil.Coordinate_encryptEx((float) latLng.longitude, (float) latLng.latitude, str);
    }

    private static LatLng b(LatLng latLng) {
        return a(latLng, CoordinateType.GCJ02);
    }

    public LatLng convert() {
        if (this.a == null) {
            return null;
        }
        if (this.b == null) {
            this.b = CoordType.GPS;
        }
        switch (this.b) {
            case COMMON:
                return b(this.a);
            case GPS:
                return a(this.a);
            default:
                return null;
        }
    }

    public CoordinateConverter coord(LatLng latLng) {
        this.a = latLng;
        return this;
    }

    public CoordinateConverter from(CoordType coordType) {
        this.b = coordType;
        return this;
    }
}
