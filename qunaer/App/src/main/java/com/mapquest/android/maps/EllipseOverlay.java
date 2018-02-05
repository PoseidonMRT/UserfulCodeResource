package com.mapquest.android.maps;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.RectF;
import android.view.MotionEvent;

public class EllipseOverlay extends Overlay {
    private static final String LOG_TAG = EllipseOverlay.class.getName();
    private BoundingBox boundingBox;
    private Paint paint;

    public EllipseOverlay(BoundingBox boundingBox, Paint paint) {
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
        Point toPixels = projection.toPixels(this.boundingBox.ul, (Point) null);
        Point toPixels2 = projection.toPixels(this.boundingBox.lr, (Point) null);
        RectF rectF = new RectF();
        rectF.set((float) toPixels.x, (float) toPixels.y, (float) toPixels2.x, (float) toPixels2.y);
        RectF rectF2 = new RectF();
        rectF2.set(canvas.getClipBounds());
        if (RectF.intersects(rectF, rectF2)) {
            canvas.drawOval(rectF, this.paint);
        }
    }

    public boolean onTap(GeoPoint geoPoint, MapView mapView) {
        if (this.tapListener == null || !contains(geoPoint)) {
            return false;
        }
        this.tapListener.onTap(geoPoint, mapView);
        return true;
    }

    public boolean onTouchEvent(MotionEvent motionEvent, MapView mapView) {
        if (this.touchListener == null || !contains(mapView.getProjection().fromPixels((int) motionEvent.getX(), (int) motionEvent.getY()))) {
            return false;
        }
        this.touchListener.onTouch(motionEvent, mapView);
        return true;
    }

    public boolean onTrackballEvent(MotionEvent motionEvent, MapView mapView) {
        if (this.trackballListener == null || !contains(mapView.getProjection().fromPixels((int) motionEvent.getX(), (int) motionEvent.getY()))) {
            return false;
        }
        this.trackballListener.onTrackballEvent(motionEvent, mapView);
        return true;
    }

    private boolean contains(GeoPoint geoPoint) {
        return this.boundingBox.contains(geoPoint);
    }

    public void destroy() {
        this.paint = null;
        this.boundingBox = null;
    }
}
