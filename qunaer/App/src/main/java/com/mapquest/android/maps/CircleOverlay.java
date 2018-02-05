package com.mapquest.android.maps;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.view.MotionEvent;

public class CircleOverlay extends Overlay {
    private GeoPoint center;
    private Paint paint;
    private double radiusMeters;
    private int radiusPixels;

    public CircleOverlay(GeoPoint geoPoint, int i, Paint paint) {
        this.center = geoPoint;
        this.radiusPixels = i;
        this.paint = paint;
    }

    public CircleOverlay(GeoPoint geoPoint, double d, Paint paint) {
        this.center = geoPoint;
        this.radiusMeters = d;
        this.paint = paint;
    }

    public void setCenter(GeoPoint geoPoint) {
        this.center = geoPoint;
    }

    public void setPaint(Paint paint) {
        this.paint = paint;
    }

    public void setRadiusPixels(int i) {
        this.radiusPixels = i;
        this.radiusMeters = 0.0d;
    }

    public void setRadiusMeters(double d) {
        this.radiusMeters = d;
        this.radiusPixels = 0;
    }

    public void draw(Canvas canvas, MapView mapView, boolean z) {
        int metersToEquatorPixels;
        Point toPixels = mapView.getProjection().toPixels(this.center, (Point) null);
        int i = toPixels.x;
        int i2 = toPixels.y;
        Rect clipBounds = canvas.getClipBounds();
        if (this.radiusMeters > 0.0d) {
            metersToEquatorPixels = (int) mapView.getProjection().metersToEquatorPixels((float) this.radiusMeters);
        } else {
            metersToEquatorPixels = this.radiusPixels;
        }
        Rect rect = new Rect(i - metersToEquatorPixels, i2 - metersToEquatorPixels, i + metersToEquatorPixels, i2 + metersToEquatorPixels);
        int strokeWidth = ((int) this.paint.getStrokeWidth()) / 2;
        rect.inset(-strokeWidth, -strokeWidth);
        if (rect.intersect(clipBounds)) {
            canvas.drawCircle((float) i, (float) i2, (float) metersToEquatorPixels, this.paint);
        }
    }

    public boolean onTap(GeoPoint geoPoint, MapView mapView) {
        if (this.tapListener == null || !contains(mapView.getProjection().toPixels(geoPoint, (Point) null), mapView)) {
            return false;
        }
        this.tapListener.onTap(geoPoint, mapView);
        return true;
    }

    public boolean onTouchEvent(MotionEvent motionEvent, MapView mapView) {
        if (this.touchListener == null || !contains(new Point((int) motionEvent.getX(), (int) motionEvent.getY()), mapView)) {
            return false;
        }
        this.touchListener.onTouch(motionEvent, mapView);
        return true;
    }

    public boolean onTrackballEvent(MotionEvent motionEvent, MapView mapView) {
        if (this.trackballListener == null || !contains(new Point((int) motionEvent.getX(), (int) motionEvent.getY()), mapView)) {
            return false;
        }
        this.trackballListener.onTrackballEvent(motionEvent, mapView);
        return true;
    }

    private boolean contains(Point point, MapView mapView) {
        int metersToEquatorPixels;
        Point toPixels = mapView.getProjection().toPixels(this.center, (Point) null);
        if (this.radiusMeters > 0.0d) {
            metersToEquatorPixels = (int) mapView.getProjection().metersToEquatorPixels((float) this.radiusMeters);
        } else {
            metersToEquatorPixels = this.radiusPixels;
        }
        int i = toPixels.x - point.x;
        int i2 = toPixels.y - point.y;
        if (Math.sqrt((double) ((i2 * i2) + (i * i))) <= ((double) metersToEquatorPixels)) {
            return true;
        }
        return false;
    }

    public void destroy() {
        this.paint = null;
    }
}
