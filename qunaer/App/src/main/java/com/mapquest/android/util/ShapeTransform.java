package com.mapquest.android.util;

import com.mapquest.android.maps.GeoPoint;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class ShapeTransform {
    private final int binaryChunkSize;
    private List<Integer> culledShapeOffsets;
    private final int minASCII;
    private boolean optimizeShape;
    private int precision;

    public ShapeTransform() {
        this(5);
    }

    public ShapeTransform(int i) {
        this.binaryChunkSize = 5;
        this.minASCII = 63;
        this.precision = 5;
        this.precision = i;
    }

    public String encodeCompressed(ArrayList<GeoPoint> arrayList) {
        int size = arrayList.size();
        StringBuilder stringBuilder = new StringBuilder();
        double pow = Math.pow(10.0d, (double) this.precision);
        int i = 0;
        int i2 = 0;
        int i3 = 0;
        while (i < size) {
            GeoPoint geoPoint = (GeoPoint) arrayList.get(i);
            int latitudeE6 = (int) (((double) geoPoint.getLatitudeE6()) * pow);
            int longitudeE6 = (int) (((double) geoPoint.getLongitudeE6()) * pow);
            stringBuilder.append(encodeSignedNumber(latitudeE6 - i3));
            stringBuilder.append(encodeSignedNumber(longitudeE6 - i2));
            i += 2;
            i2 = longitudeE6;
            i3 = latitudeE6;
        }
        return stringBuilder.toString();
    }

    private String encodeSignedNumber(int i) {
        int i2 = i << 1;
        if (i < 0) {
            i2 ^= -1;
        }
        return encodeNumber(i2);
    }

    private String encodeNumber(int i) {
        StringBuilder stringBuilder = new StringBuilder();
        while (i >= 32) {
            stringBuilder.append((char) (((i & 31) | 32) + 63));
            i >>= 5;
        }
        stringBuilder.append((char) (i + 63));
        return stringBuilder.toString();
    }

    public List<GeoPoint> decodeCompressed(String str) {
        int i = 9999;
        int i2 = 0;
        if (str == null) {
            return null;
        }
        ArrayList arrayList = new ArrayList();
        if (this.optimizeShape) {
            this.culledShapeOffsets = new ArrayList();
        }
        int length = str.length();
        double pow = Math.pow(10.0d, (double) (-this.precision));
        AtomicInteger atomicInteger = new AtomicInteger(0);
        int i3 = 9999;
        int i4 = 0;
        int i5 = 0;
        while (atomicInteger.get() < length) {
            i5 += decodePoint(str, atomicInteger);
            i4 += decodePoint(str, atomicInteger);
            if (this.optimizeShape && i5 == r2 && i4 == r1) {
                this.culledShapeOffsets.add(new Integer(i2));
            } else {
                arrayList.add(new GeoPoint(((double) i5) * pow, ((double) i4) * pow));
                i = i4;
                i3 = i5;
            }
            i2++;
        }
        this.culledShapeOffsets.add(new Integer(i2));
        return arrayList;
    }

    private int decodePoint(String str, AtomicInteger atomicInteger) {
        int i;
        int i2 = atomicInteger.get();
        int i3 = 0;
        int i4 = 0;
        while (true) {
            i = i2 + 1;
            int charAt = str.charAt(i2) - 63;
            i2 = ((charAt & 31) << i4) | i3;
            i3 = i4 + 5;
            if (charAt < 32) {
                break;
            }
            i4 = i3;
            i3 = i2;
            i2 = i;
        }
        i2 = (i2 & 1) > 0 ? (i2 >> 1) ^ -1 : i2 >> 1;
        atomicInteger.getAndSet(i);
        return i2;
    }

    public boolean isOptimizedShape() {
        return this.optimizeShape;
    }

    public void setOptimizeShape(boolean z) {
        this.optimizeShape = z;
    }

    public List<Integer> getCulledShapeOffsets() {
        return this.culledShapeOffsets;
    }
}
