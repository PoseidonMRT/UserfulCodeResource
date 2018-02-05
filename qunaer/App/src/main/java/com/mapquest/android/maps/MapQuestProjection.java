package com.mapquest.android.maps;

import android.graphics.Point;

class MapQuestProjection implements ProjectionX {
    private static final String LOG_TAG = MapQuestProjection.class.getSimpleName();
    public static double PIXERSPERLATDEGREE = 3.15552459661917E8d;
    public static double PIXERSPERLNGDEGREE = 2.5034459790989706E8d;
    private static double PIXERSPERMETER = 2834.6472d;
    public static final int[] SCALES = new int[]{88011773, 29337258, 9779086, 3520471, 1504475, 701289, 324767, 154950, 74999, 36000, 18000, 9000, 4700, 2500, 1500, 1000};
    private final int TILE_SIZE = 256;
    private int centerLat;
    private int centerLng;
    private int centerXPix;
    private int centerYPix;
    private MapView mapView;
    private int zoomLevel;

    public MapQuestProjection(MapView mapView) {
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

    protected int getGlobalX(double d, int i) {
        if (i < 1) {
            i = 9;
        }
        try {
            return (int) ((PIXERSPERLNGDEGREE / ((double) SCALES[i - 1])) * (180.0d + d));
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    protected int getGlobalY(double d, int i) {
        if (i < 1) {
            i = 9;
        }
        try {
            return (int) ((PIXERSPERLATDEGREE / ((double) SCALES[i - 1])) * (90.0d + d));
        } catch (Exception e) {
            return 0;
        }
    }

    private int getScreenFromGlobalX(int i) {
        int mapWidth = this.mapView.getMapWidth();
        int globalX = getGlobalX(180.0d, this.mapView.getZoomLevel());
        int i2 = ((mapWidth / 2) - this.centerXPix) + i;
        if (Math.abs(this.centerXPix - i) <= globalX / 2) {
            return i2;
        }
        return ((this.centerXPix < i ? -1 : 1) * globalX) + i2;
    }

    private int getScreenFromGlobalY(int i) {
        return ((this.mapView.getMapHeight() / 2) + this.centerYPix) - i;
    }

    private int getGlobalFromScreenX(int i) {
        return (((-this.mapView.getMapWidth()) / 2) + this.centerXPix) + i;
    }

    private int getGlobalFromScreenY(int i) {
        return ((this.mapView.getMapHeight() / 2) + this.centerYPix) - i;
    }

    protected Point getGlobalFromScreen(int i, int i2) {
        int globalFromScreenX = getGlobalFromScreenX(i);
        int globalFromScreenY = getGlobalFromScreenY(i2);
        return (globalFromScreenY < 0 || globalFromScreenY > getGlobalY(90.0d, this.mapView.getZoomLevel())) ? null : new Point(globalFromScreenX, globalFromScreenY);
    }

    private GeoPoint fromGlobalPixels(int i, int i2) {
        double d = 90.0d;
        int zoomLevel = this.mapView.getZoomLevel();
        int globalX = getGlobalX(180.0d, zoomLevel);
        int i3 = (globalX + i) % globalX;
        double pixelYToLatitude = pixelYToLatitude(i2, zoomLevel);
        if (pixelYToLatitude <= 90.0d) {
            if (pixelYToLatitude < -90.0d) {
                d = -90.0d;
            } else {
                d = pixelYToLatitude;
            }
        }
        return new GeoPoint(Util.to1E6(d), Util.to1E6(pixelXToLongitude(i3, zoomLevel)));
    }

    private Point toGlobalPixels(GeoPoint geoPoint, Point point) {
        if (point == null) {
            point = new Point();
        }
        int zoomLevel = this.mapView.getZoomLevel();
        point.x = getGlobalX(Util.from1E6(geoPoint.getLongitudeE6()), zoomLevel);
        point.y = getGlobalY(Util.from1E6(geoPoint.getLatitudeE6()), zoomLevel);
        return point;
    }

    public GeoPoint fromPixels(int i, int i2) {
        check();
        return fromGlobalPixels(getGlobalFromScreenX(i), getGlobalFromScreenY(i2));
    }

    public float metersToEquatorPixels(float f) {
        try {
            if (this.zoomLevel <= 0) {
                this.zoomLevel = 9;
            }
            return f / ((float) (((double) SCALES[this.zoomLevel - 1]) / PIXERSPERMETER));
        } catch (Exception e) {
            return 0.0f;
        }
    }

    public Point toPixels(GeoPoint geoPoint, Point point) {
        if (point == null) {
            point = new Point();
        }
        check();
        toGlobalPixels(geoPoint, point);
        point.x = getScreenFromGlobalX(point.x);
        point.y = getScreenFromGlobalY(point.y);
        return point;
    }

    public int getScaleFactor(int i) {
        return SCALES[i - 1];
    }

    private double pixelYToLatitude(int i, int i2) {
        if (i2 < 1) {
            i2 = 9;
        }
        try {
            return (((((double) i) / (PIXERSPERLATDEGREE / ((double) SCALES[i2 - 1]))) - 90.0d) * 1000000.0d) / 1000000.0d;
        } catch (Exception e) {
            return 0.0d;
        }
    }

    private double pixelXToLongitude(int i, int i2) {
        if (i2 < 1) {
            i2 = 9;
        }
        try {
            return (((((double) i) / (PIXERSPERLNGDEGREE / ((double) SCALES[i2 - 1]))) - 180.0d) * 1000000.0d) / 1000000.0d;
        } catch (Exception e) {
            return 0.0d;
        }
    }

    public Point getScreenFromGlobal(Point point) {
        check();
        return new Point(getScreenFromGlobalX(point.x), getScreenFromGlobalY(point.y));
    }

    public int calculateZoomLevel(BoundingBox boundingBox) {
        return calculateZoomLevel(boundingBox, 0, 0);
    }

    public int calculateZoomLevel(BoundingBox boundingBox, int i, int i2) {
        try {
            GeoPoint geoPoint = boundingBox.ul;
            GeoPoint geoPoint2 = boundingBox.lr;
            int i3 = 1;
            int width = this.mapView.getWidth() - i;
            int height = this.mapView.getHeight() - i2;
            int i4 = SCALES[this.zoomLevel - 1];
            double abs = Math.abs(Util.from1E6(geoPoint2.getLongitudeE6() - geoPoint.getLongitudeE6())) * (PIXERSPERLNGDEGREE / ((double) i4));
            double max = Math.max((abs / ((double) width)) * ((double) i4), ((Math.abs(Util.from1E6(geoPoint2.getLatitudeE6() - geoPoint.getLatitudeE6())) * (PIXERSPERLATDEGREE / ((double) i4))) / ((double) height)) * ((double) i4));
            while (max < ((double) SCALES[i3]) && i3 < SCALES.length - 1) {
                i3++;
            }
            return i3;
        } catch (Exception e) {
            return 8;
        }
    }
}
