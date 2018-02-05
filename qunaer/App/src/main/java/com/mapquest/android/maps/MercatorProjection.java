package com.mapquest.android.maps;

import android.graphics.Point;
import com.baidu.mapapi.map.WeightedLatLng;
import qunar.sdk.mapapi.utils.projectTransform.CoordConvertLocal;

public class MercatorProjection implements ProjectionX {
    private static final int EARTH_RADIUS_METERS = 6378137;
    private static final String LOG_TAG = MercatorProjection.class.getSimpleName();
    public static final double MAX_LAT = 85.05112877980659d;
    private static final int[] SCALE_FACTORS = new int[]{0, 0, 110936008, 55468004, 27734002, 13867001, 6933500, 3466750, 1733375, 866687, 433343, 216671, 108335, 54167, 27083, 13541, 6770, 3385, 1692};
    private final int TILE_SIZE = 256;
    private int centerLat;
    private int centerLng;
    private int centerXPix;
    private int centerYPix;
    private MapView mapView;
    private int zoomLevel;

    public MercatorProjection(MapView mapView) {
        this.mapView = mapView;
    }

    private void check() {
        Object obj = 1;
        Object obj2 = null;
        if (this.mapView.getZoomLevel() != this.zoomLevel) {
            obj2 = 1;
        }
        GeoPoint geoPoint = this.mapView.centerGeoPoint;
        if (geoPoint != null) {
            if (geoPoint.getLatitudeE6() != this.centerLat) {
                obj2 = 1;
            }
            if (geoPoint.getLongitudeE6() == this.centerLng) {
                obj = obj2;
            }
            if (obj != null && obj != null) {
                synchronized (this) {
                    this.zoomLevel = this.mapView.getZoomLevel();
                    this.centerLat = geoPoint.getLatitudeE6();
                    this.centerLng = geoPoint.getLongitudeE6();
                    Point toGlobalPixels = toGlobalPixels(geoPoint, new Point());
                    this.centerXPix = toGlobalPixels.x;
                    this.centerYPix = toGlobalPixels.y;
                }
            }
        }
    }

    public int getScaleFactor(int i) {
        return SCALE_FACTORS[i];
    }

    private GeoPoint fromGlobalPixels(int i, int i2) {
        double d = MAX_LAT;
        int zoomLevel = this.mapView.getZoomLevel();
        int i3 = 256 << zoomLevel;
        if (i3 == 0) {
            i3 = 0;
        } else {
            i3 = (i3 + i) % i3;
        }
        double pixelYToLatitude = pixelYToLatitude((double) i2, zoomLevel);
        if (pixelYToLatitude <= MAX_LAT) {
            if (pixelYToLatitude < -85.05112877980659d) {
                d = -85.05112877980659d;
            } else {
                d = pixelYToLatitude;
            }
        }
        return new GeoPoint(Util.to1E6(d), Util.to1E6(pixelXToLongitude((double) i3, zoomLevel)));
    }

    private float metersPerPixel(double d, int i) {
        return (float) ((((Math.cos((d * CoordConvertLocal.REAL_PI) / 180.0d) * 2.0d) * CoordConvertLocal.REAL_PI) * 6378137.0d) / ((double) (256 << i)));
    }

    public float metersToEquatorPixels(float f) {
        check();
        return f / metersPerPixel(Util.from1E6(this.mapView.getMapCenter().getLatitudeE6()), this.mapView.getZoomLevel());
    }

    Point toGlobalPixels(GeoPoint geoPoint, Point point) {
        if (point == null) {
            point = new Point();
        }
        int zoomLevel = this.mapView.getZoomLevel();
        point.x = (int) (((Util.from1E6(geoPoint.getLongitudeE6()) + 180.0d) / 360.0d) * ((double) (256 << zoomLevel)));
        double sin = Math.sin(Math.toRadians(Util.from1E6(geoPoint.getLatitudeE6())));
        point.y = (int) ((0.5d - (Math.log((WeightedLatLng.DEFAULT_INTENSITY + sin) / (WeightedLatLng.DEFAULT_INTENSITY - sin)) / 12.566370614359172d)) * ((double) (256 << zoomLevel)));
        return point;
    }

    public Point toPixels(GeoPoint geoPoint, Point point) {
        int i;
        if (point == null) {
            point = new Point();
        }
        check();
        toGlobalPixels(geoPoint, point);
        int zoomLevel = 256 << this.mapView.getZoomLevel();
        int i2 = point.x - this.centerXPix;
        int i3 = point.y - this.centerYPix;
        if (Math.abs(i2) > zoomLevel / 2) {
            i = ((i2 < 0 ? 1 : -1) * zoomLevel) + i2;
        } else {
            i = i2;
        }
        point.x = i + (this.mapView.getMapWidth() / 2);
        point.y = (this.mapView.getMapHeight() / 2) + i3;
        return point;
    }

    private double pixelXToLongitude(double d, int i) {
        return 360.0d * ((d / ((double) (256 << i))) - 0.5d);
    }

    private double pixelYToLatitude(double d, int i) {
        return 90.0d - ((Math.atan(Math.exp(((-(0.5d - (d / ((double) (256 << i))))) * 2.0d) * CoordConvertLocal.REAL_PI)) * 360.0d) / CoordConvertLocal.REAL_PI);
    }

    public Point getScreenFromGlobal(Point point) {
        check();
        int zoomLevel = 256 << this.mapView.getZoomLevel();
        int zoomLevel2 = 256 << this.mapView.getZoomLevel();
        int i = point.x - this.centerXPix;
        int i2 = point.y - this.centerYPix;
        if (Math.abs(i) > zoomLevel / 2) {
            zoomLevel2 = ((i < 0 ? 1 : -1) * zoomLevel) + i;
        } else {
            zoomLevel2 = i;
        }
        return new Point(zoomLevel2 + (this.mapView.getMapWidth() / 2), (this.mapView.getMapHeight() / 2) + i2);
    }

    protected Point getGlobalFromScreen(int i, int i2, Point point) {
        check();
        int zoomLevel = 256 << this.mapView.getZoomLevel();
        int zoomLevel2 = 256 << this.mapView.getZoomLevel();
        try {
            zoomLevel = ((this.centerXPix + zoomLevel) + (i - (this.mapView.getMapWidth() >> 1))) % zoomLevel;
        } catch (Exception e) {
            Exception exception = e;
            zoomLevel = 0;
            exception.printStackTrace();
        }
        int mapHeight = this.centerYPix + (i2 - (this.mapView.getMapHeight() >> 1));
        if (mapHeight < 0 || mapHeight > zoomLevel2) {
            return null;
        }
        if (point == null) {
            point = new Point();
        }
        point.set(zoomLevel, mapHeight);
        return point;
    }

    public GeoPoint fromPixels(int i, int i2) {
        check();
        return fromGlobalPixels((this.centerXPix - (this.mapView.getMapWidth() >> 1)) + i, (this.centerYPix - (this.mapView.getMapHeight() >> 1)) + i2);
    }

    public int calculateZoomLevel(BoundingBox boundingBox) {
        return calculateZoomLevel(boundingBox, 0, 0);
    }

    public int calculateZoomLevel(BoundingBox boundingBox, int i, int i2) {
        try {
            Point toPixels = toPixels(boundingBox.ul, (Point) null);
            Point toPixels2 = toPixels(boundingBox.lr, (Point) null);
            double max = Math.max(((double) Math.abs(toPixels.x - toPixels2.x)) / ((double) (this.mapView.getMapWidth() - i)), ((double) Math.abs(toPixels.y - toPixels2.y)) / ((double) (this.mapView.getMapHeight() - i2)));
            double log = Math.log(max) / Math.log(2.0d);
            return this.mapView.getZoomLevel() - (max > WeightedLatLng.DEFAULT_INTENSITY ? (int) Math.ceil(log) : (int) Math.ceil(log));
        } catch (Exception e) {
            return this.mapView.getZoomLevel();
        }
    }
}
