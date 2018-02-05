package com.mapquest.android.maps;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.http.Headers;
import android.os.Bundle;
import com.mapquest.android.maps.MapView.LayoutParams;
import com.mapquest.android.maps.MapView.MapViewEventListener;
import java.util.Date;
import java.util.List;

public class MyLocationOverlay extends Overlay implements SensorListener, LocationListener {
    private static final int GPS_TIME_OUT = 10000;
    private static final String LOG_TAG = MyLocationOverlay.class.getSimpleName();
    private int HALO_COLOR = -11460275;
    private Sensor accelerometer;
    int animating;
    int animationFrequency = 2000;
    double animationLength = 500.0d;
    double animationRadius = 50.0d;
    private Paint beaconAnimationFillPaint;
    private Paint beaconAnimationOutlinePaint;
    private float bearing = 0.0f;
    private CompassView cv;
    private MapViewEventListener defaultListener = new MapViewEventListener() {
        public void zoomStart(MapView mapView) {
        }

        public void zoomEnd(MapView mapView) {
        }

        public void touch(MapView mapView) {
        }

        public void mapLoaded(MapView mapView) {
        }

        public void moveStart(MapView mapView) {
            if (MyLocationOverlay.this.lastFix != null && MyLocationOverlay.this.lastFix.getAccuracy() <= 32.0f) {
                MyLocationOverlay.this.followingFlag = false;
            }
        }

        public void moveEnd(MapView mapView) {
            if (MyLocationOverlay.this.userFollowingFlag && !MyLocationOverlay.this.followingFlag && getChangeDistance(mapView).floatValue() <= 15.0f) {
                MyLocationOverlay.this.followingFlag = true;
            }
        }

        private Float getChangeDistance(MapView mapView) {
            if (MyLocationOverlay.this.lastFixGeoPoint == null) {
                return Float.valueOf(0.0f);
            }
            Projection projection = mapView.getProjection();
            return Float.valueOf(Util.distance(projection.toPixels(mapView.getMapCenter(), new Point()), projection.toPixels(MyLocationOverlay.this.lastFixGeoPoint, new Point())));
        }

        public void move(MapView mapView) {
        }

        public void longTouch(MapView mapView, Point point) {
        }
    };
    private boolean enableCompass = false;
    private boolean enableMyLocation = false;
    private boolean followingFlag = false;
    private Location lastFix = null;
    private GeoPoint lastFixGeoPoint = null;
    private long lastGpsFixTime = 0;
    private Location lastNetworkFix = null;
    private MapViewEventListener listener = null;
    private LocationManager locationManager = null;
    private final SensorEventListener mListener = new SensorEventListener() {
        float[] geomagnetic;
        float[] gravity;
        float[] r = new float[16];

        private void reset(float[] fArr) {
            if (fArr != null) {
                for (int i = 0; i < fArr.length; i++) {
                    fArr[i] = 0.0f;
                }
            }
        }

        public void onSensorChanged(SensorEvent sensorEvent) {
            if (sensorEvent.sensor.getType() == 1) {
                if (this.gravity == null || sensorEvent.values.length != this.gravity.length) {
                    this.gravity = new float[sensorEvent.values.length];
                }
                System.arraycopy(sensorEvent.values, 0, this.gravity, 0, this.gravity.length);
            }
            if (sensorEvent.sensor.getType() == 2) {
                this.geomagnetic = (float[]) sensorEvent.values.clone();
                if (this.geomagnetic == null || sensorEvent.values.length != this.geomagnetic.length) {
                    this.geomagnetic = new float[sensorEvent.values.length];
                }
                System.arraycopy(sensorEvent.values, 0, this.geomagnetic, 0, this.geomagnetic.length);
            }
            if (this.gravity != null && this.geomagnetic != null) {
                reset(this.r);
                if (SensorManager.getRotationMatrix(this.r, (float[]) null, this.gravity, this.geomagnetic)) {
                    r0 = new float[3];
                    SensorManager.getOrientation(this.r, r0);
                    r0[0] = (float) Math.toDegrees((double) r0[0]);
                    r0[1] = (float) Math.toDegrees((double) r0[1]);
                    r0[2] = (float) Math.toDegrees((double) r0[2]);
                    MyLocationOverlay.this.onSensorChanged(3, r0);
                }
            }
        }

        public void onAccuracyChanged(Sensor sensor, int i) {
            MyLocationOverlay.this.onAccuracyChanged(3, i);
        }
    };
    private Sensor magnetometer;
    private MapView mapView;
    private Drawable marker = null;
    private int markerAlignment = 3;
    int minFingerSize;
    private boolean networkAvailable;
    private Paint paint = null;
    private Rect rect = new Rect();
    private Point reusable = new Point();
    private Runnable runnable;
    private SensorManager sensorManager = null;
    long startTime;
    private boolean userFollowingFlag = false;

    public MyLocationOverlay(Context context, MapView mapView) {
        this.mapView = mapView;
        this.minFingerSize = (int) ((context.getResources().getDisplayMetrics().density * 10.0f) + 0.5f);
        this.paint = new Paint(1);
        this.paint.setFilterBitmap(true);
        this.paint.setDither(true);
        this.sensorManager = (SensorManager) context.getSystemService("sensor");
        this.accelerometer = this.sensorManager.getDefaultSensor(1);
        this.magnetometer = this.sensorManager.getDefaultSensor(2);
        this.locationManager = (LocationManager) context.getSystemService(Headers.LOCATION);
        this.marker = Util.getDrawable(context, "location_marker_purple");
        this.beaconAnimationFillPaint = new Paint();
        this.beaconAnimationFillPaint.setColor(this.HALO_COLOR);
        this.beaconAnimationFillPaint.setAlpha(30);
        this.beaconAnimationFillPaint.setAntiAlias(true);
        this.beaconAnimationFillPaint.setStyle(Style.FILL);
        this.beaconAnimationOutlinePaint = new Paint(this.beaconAnimationFillPaint);
        this.beaconAnimationOutlinePaint.setAlpha(60);
        this.beaconAnimationOutlinePaint.setStrokeWidth(2.0f);
        this.beaconAnimationOutlinePaint.setStyle(Style.STROKE);
        this.cv = new CompassView(mapView.getContext());
        this.cv.setVisibility(4);
        mapView.addView(this.cv, new LayoutParams(-2, -2, 40, 40, 3));
        this.animating = 1;
        this.startTime = new Date().getTime();
    }

    protected MapViewEventListener setupMapViewEventListener() {
        return this.defaultListener;
    }

    public boolean isFollowing() {
        return this.userFollowingFlag;
    }

    public void setFollowing(boolean z) {
        this.userFollowingFlag = z;
        this.followingFlag = true;
        if (z) {
            this.listener = setupMapViewEventListener();
            if (this.listener != null) {
                this.mapView.addMapViewEventListener(this.listener);
            }
            this.mapView.getController().animateTo(this.lastFixGeoPoint);
            return;
        }
        this.mapView.removeMapViewEventListener(this.listener);
    }

    public void destroy() {
        if (this.enableMyLocation) {
            disableMyLocation();
            this.enableMyLocation = false;
        }
        if (this.enableCompass) {
            disableCompass();
            this.enableCompass = false;
        }
        this.locationManager = null;
        this.sensorManager = null;
        this.accelerometer = null;
        this.magnetometer = null;
        this.lastFix = null;
        this.lastFixGeoPoint = null;
        if (this.cv != null) {
            this.cv.destroyDrawingCache();
            this.cv = null;
        }
        this.mapView = null;
    }

    public boolean isCompassEnabled() {
        return this.enableCompass;
    }

    public boolean enableCompass() {
        if (!this.enableCompass) {
            this.enableCompass = true;
            this.sensorManager.registerListener(this.mListener, this.accelerometer, 3);
            this.sensorManager.registerListener(this.mListener, this.magnetometer, 3);
        }
        return this.enableCompass;
    }

    public void disableCompass() {
        if (this.enableCompass) {
            this.cv.setVisibility(4);
            this.sensorManager.unregisterListener(this);
            this.sensorManager.unregisterListener(this.mListener);
        }
    }

    private boolean registerProviders() {
        boolean z;
        boolean z2 = true;
        List allProviders = this.locationManager.getAllProviders();
        if (allProviders.contains("gps")) {
            if (this.locationManager.isProviderEnabled("gps")) {
                z = true;
            } else {
                z = false;
            }
            this.locationManager.requestLocationUpdates("gps", 0, 0.0f, this);
        } else {
            z = false;
        }
        if (!allProviders.contains("network")) {
            return z;
        }
        if (!this.locationManager.isProviderEnabled("network")) {
            z2 = z;
        }
        this.locationManager.requestLocationUpdates("network", 0, 0.0f, this);
        return z2;
    }

    public boolean enableMyLocation() {
        this.enableMyLocation = true;
        return registerProviders();
    }

    public void disableMyLocation() {
        this.locationManager.removeUpdates(this);
        this.enableMyLocation = false;
    }

    protected boolean dispatchTap() {
        return false;
    }

    public boolean draw(Canvas canvas, MapView mapView, boolean z, long j) {
        if (this.enableCompass) {
            drawCompass(canvas, this.bearing);
        }
        if (!this.enableMyLocation || this.lastFix == null) {
            return true;
        }
        boolean animateBeacon = animateBeacon(canvas, mapView, z, j);
        drawMyLocation(canvas, mapView, this.lastFix, this.lastFixGeoPoint, j);
        if (animateBeacon || !this.enableCompass) {
            return animateBeacon;
        }
        return true;
    }

    protected boolean animateBeacon(Canvas canvas, MapView mapView, boolean z, long j) {
        if (this.beaconAnimationFillPaint == null && this.beaconAnimationOutlinePaint == null) {
            return false;
        }
        if (this.animating == 1) {
            this.startTime = j;
            this.animating = 2;
        }
        long j2 = (j - this.startTime) % ((long) this.animationFrequency);
        if (this.animating != 2 || ((double) j2) >= this.animationLength) {
            draw(canvas, mapView, z);
            return false;
        }
        Double valueOf = Double.valueOf((((double) j2) / this.animationLength) * this.animationRadius);
        if (valueOf.doubleValue() < 5.0d) {
            return false;
        }
        Point toPixels = mapView.getProjection().toPixels(this.lastFixGeoPoint, this.reusable);
        Double valueOf2 = Double.valueOf(255.0d - ((((double) j2) / this.animationLength) * 255.0d));
        this.beaconAnimationOutlinePaint.setAlpha(valueOf2.intValue());
        this.beaconAnimationFillPaint.setAlpha(valueOf2.intValue());
        if (this.beaconAnimationFillPaint != null) {
            canvas.drawCircle((float) toPixels.x, (float) toPixels.y, (float) valueOf.longValue(), this.beaconAnimationFillPaint);
        }
        if (this.beaconAnimationOutlinePaint != null) {
            canvas.drawCircle((float) toPixels.x, (float) toPixels.y, (float) valueOf.longValue(), this.beaconAnimationOutlinePaint);
        }
        int intValue = Double.valueOf(valueOf.doubleValue() + 3.0d).intValue();
        mapView.invalidate(new Rect(toPixels.x - intValue, toPixels.y - intValue, toPixels.x + intValue, intValue + toPixels.y));
        return true;
    }

    protected void drawCompass(Canvas canvas, float f) {
        if (this.cv.getVisibility() != 0) {
            this.cv.setVisibility(0);
            this.cv.invalidate();
        }
    }

    protected void drawMyLocation(Canvas canvas, MapView mapView, Location location, GeoPoint geoPoint, long j) {
        if (this.marker != null) {
            Point toPixels = mapView.getProjection().toPixels(this.lastFixGeoPoint, this.reusable);
            Overlay.setAlignment(this.marker, this.markerAlignment);
            this.rect.set(this.marker.getBounds());
            this.rect.offset(toPixels.x, toPixels.y);
            this.rect.inset((-this.minFingerSize) >> 1, (-this.minFingerSize) >> 1);
            Overlay.drawAt(canvas, this.marker, toPixels.x, toPixels.y, false);
            float metersToEquatorPixels = mapView.getProjection().metersToEquatorPixels(location.getAccuracy());
            if (metersToEquatorPixels > 10.0f) {
                canvas.drawCircle((float) toPixels.x, (float) toPixels.y, metersToEquatorPixels, this.beaconAnimationFillPaint);
                canvas.drawCircle((float) toPixels.x, (float) toPixels.y, metersToEquatorPixels, this.beaconAnimationOutlinePaint);
            }
        }
    }

    public Location getLastFix() {
        return this.lastFix;
    }

    public GeoPoint getMyLocation() {
        return this.lastFixGeoPoint;
    }

    public float getOrientation() {
        return this.bearing;
    }

    public boolean isMyLocationEnabled() {
        return this.enableMyLocation;
    }

    public void onSensorChanged(int i, float[] fArr) {
        try {
            if (fArr[0] != this.bearing) {
                this.bearing = fArr[0];
                this.cv.setRotation(-this.bearing);
            }
        } catch (Exception e) {
        }
    }

    public void onAccuracyChanged(int i, int i2) {
    }

    public void onLocationChanged(Location location) {
        Object obj = 1;
        try {
            long currentTimeMillis = System.currentTimeMillis();
            String provider = location.getProvider();
            if ("gps".equals(provider)) {
                this.lastGpsFixTime = System.currentTimeMillis();
            } else if ("network".equals(provider)) {
                if (currentTimeMillis - this.lastGpsFixTime <= 10000) {
                    obj = null;
                }
                if (this.lastNetworkFix == null) {
                    this.lastNetworkFix = new Location(location);
                } else {
                    this.lastNetworkFix.set(location);
                }
                this.lastGpsFixTime = 0;
            } else {
                obj = null;
            }
            if (obj != null) {
                this.lastFixGeoPoint = new GeoPoint(Util.to1E6(location.getLatitude()), Util.to1E6(location.getLongitude()));
                if (this.lastFix == null) {
                    this.lastFix = new Location(location);
                    if (this.runnable != null) {
                        new Thread(this.runnable).start();
                        this.runnable = null;
                    }
                }
                this.lastFix.set(location);
            }
            checkMapCenter();
            this.mapView.invalidate();
        } catch (Exception e) {
        }
    }

    private void checkMapCenter() {
        if (this.userFollowingFlag && this.followingFlag) {
            this.mapView.getController().animateTo(this.lastFixGeoPoint);
        }
    }

    public void onStatusChanged(String str, int i, Bundle bundle) {
        if ("gps".equals(str)) {
            switch (i) {
                case 0:
                case 1:
                    if (this.lastNetworkFix != null && this.networkAvailable) {
                        this.lastGpsFixTime = 0;
                        onLocationChanged(this.lastNetworkFix);
                        return;
                    }
                    return;
                default:
                    return;
            }
        } else if ("network".equals(str)) {
            switch (i) {
                case 0:
                case 1:
                    this.networkAvailable = false;
                    return;
                case 2:
                    this.networkAvailable = true;
                    return;
                default:
                    return;
            }
        }
    }

    public void onProviderDisabled(String str) {
    }

    public void onProviderEnabled(String str) {
    }

    public boolean runOnFirstFix(Runnable runnable) {
        if (this.lastFix != null) {
            new Thread(runnable).start();
            return true;
        }
        this.runnable = runnable;
        return false;
    }

    public boolean onSnapToItem(int i, int i2, Point point, MapView mapView) {
        return false;
    }

    public boolean onTap(GeoPoint geoPoint, MapView mapView) {
        Point toPixels = mapView.getProjection().toPixels(geoPoint, this.reusable);
        return this.rect.contains(toPixels.x, toPixels.y) ? dispatchTap() : false;
    }

    public void shouldAnimate(boolean z) {
        if (z) {
            this.animating = 1;
        } else {
            this.animating = 0;
        }
    }

    public void setBeaconAnimationOutlinePaint(Paint paint) {
        this.beaconAnimationOutlinePaint = paint;
    }

    public void setBeaconAnimationFillPaint(Paint paint) {
        this.beaconAnimationFillPaint = paint;
    }

    public void setMarker(Drawable drawable, int i) {
        this.marker = drawable;
        if (i != 0) {
            this.markerAlignment = i;
        }
    }
}
