package com.mapquest.android.maps;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.view.MotionEvent;

public class RectangleOverlay extends Overlay {
    private static final String LOG_TAG = RectangleOverlay.class.getSimpleName();
    private BoundingBox boundingBox;
    private Paint paint;
    private Path path = new Path();

    public RectangleOverlay(BoundingBox boundingBox, Paint paint) {
        this.boundingBox = boundingBox;
        this.paint = paint;
    }

    public void setBoundingBox(BoundingBox boundingBox) {
        this.boundingBox = boundingBox;
    }

    public void setPaint(Paint paint) {
        this.paint = paint;
    }

    public void draw(Canvas canvas, MapView mapView, boolean z) {
        Projection projection = mapView.getProjection();
        this.path.reset();
        Point toPixels = projection.toPixels(this.boundingBox.ul, (Point) null);
        Point toPixels2 = projection.toPixels(new GeoPoint(this.boundingBox.ul.getLatitude(), this.boundingBox.lr.getLongitude()), (Point) null);
        Point toPixels3 = projection.toPixels(this.boundingBox.lr, (Point) null);
        Point toPixels4 = projection.toPixels(new GeoPoint(this.boundingBox.lr.getLatitude(), this.boundingBox.ul.getLongitude()), (Point) null);
        this.path.moveTo((float) toPixels.x, (float) toPixels.y);
        this.path.lineTo((float) toPixels2.x, (float) toPixels2.y);
        this.path.lineTo((float) toPixels3.x, (float) toPixels3.y);
        this.path.lineTo((float) toPixels4.x, (float) toPixels4.y);
        this.path.lineTo((float) toPixels.x, (float) toPixels.y);
        canvas.drawPath(this.path, this.paint);
    }

    public boolean onTap(GeoPoint geoPoint, MapView mapView) {
        if (this.tapListener == null || !contains(mapView, geoPoint)) {
            return false;
        }
        this.tapListener.onTap(geoPoint, mapView);
        return true;
    }

    public boolean onTouchEvent(MotionEvent motionEvent, MapView mapView) {
        if (this.touchListener == null || !contains(mapView, mapView.getProjection().fromPixels((int) motionEvent.getX(), (int) motionEvent.getY()))) {
            return false;
        }
        this.touchListener.onTouch(motionEvent, mapView);
        return true;
    }

    public boolean onTrackballEvent(MotionEvent motionEvent, MapView mapView) {
        if (this.trackballListener == null || !contains(mapView, mapView.getProjection().fromPixels((int) motionEvent.getX(), (int) motionEvent.getY()))) {
            return false;
        }
        this.trackballListener.onTrackballEvent(motionEvent, mapView);
        return true;
    }

    private boolean contains(MapView mapView, GeoPoint geoPoint) {
        return this.boundingBox.contains(geoPoint);
    }

    public void destroy() {
        this.paint = null;
        this.boundingBox = null;
    }
}
