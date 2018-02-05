package com.mapquest.android.maps;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.Rect;
import android.view.MotionEvent;
import java.util.List;

public class PolygonOverlay extends Overlay {
    private static final String LOG_TAG = PolygonOverlay.class.getSimpleName();
    private boolean DEBUG = false;
    private BoundingBox boundingBox;
    private Paint linePaint;
    private Path path;
    private Paint pointPaint;
    private Polygon polygon;
    private boolean showPoints = false;

    public PolygonOverlay() {
        init((Paint) null);
    }

    public PolygonOverlay(Paint paint) {
        init(paint);
    }

    private void init(Paint paint) {
        if (paint != null) {
            this.linePaint = paint;
        }
        this.path = new Path();
        this.polygon = new Polygon();
    }

    public void setData(List<GeoPoint> list, BoundingBox boundingBox) {
        this.polygon.setData(list);
        this.boundingBox = boundingBox;
    }

    public void setData(List<GeoPoint> list, boolean z) {
        this.polygon.setData(list);
        if (z) {
            this.boundingBox = BoundingBox.calculateBoundingBoxGeoPoint(list);
        }
    }

    public void setData(List<GeoPoint> list) {
        this.polygon.setData(list);
        this.boundingBox = BoundingBox.calculateBoundingBoxGeoPoint(list);
    }

    public void setLinePaint(Paint paint) {
        this.linePaint = paint;
    }

    public void setShowPoints(boolean z, Paint paint) {
        this.showPoints = z;
        this.pointPaint = paint;
    }

    public void setPointPaint(Paint paint) {
        this.pointPaint = paint;
    }

    public void setBoundingBox(BoundingBox boundingBox) {
        this.boundingBox = boundingBox;
    }

    public void draw(Canvas canvas, MapView mapView, boolean z) {
        Projection projection;
        boolean intersect;
        List<GeoPoint> data;
        int i = 0;
        Rect clipBounds;
        Rect createRectFromBoundingBox;
        int strokeWidth;
        if (this.DEBUG) {
            projection = mapView.getProjection();
            clipBounds = canvas.getClipBounds();
            createRectFromBoundingBox = Util.createRectFromBoundingBox(this.boundingBox, mapView);
            strokeWidth = ((int) this.linePaint.getStrokeWidth()) / 2;
            createRectFromBoundingBox.inset(-strokeWidth, -strokeWidth);
            intersect = new Rect().setIntersect(createRectFromBoundingBox, clipBounds);
            data = this.polygon.getData();
        } else {
            projection = mapView.getProjection();
            clipBounds = canvas.getClipBounds();
            createRectFromBoundingBox = Util.createRectFromBoundingBox(this.boundingBox, mapView);
            strokeWidth = ((int) this.linePaint.getStrokeWidth()) / 2;
            createRectFromBoundingBox.inset(-strokeWidth, -strokeWidth);
            intersect = new Rect().setIntersect(createRectFromBoundingBox, clipBounds);
            data = this.polygon.getData();
        }
        if (intersect) {
            System.currentTimeMillis();
            this.path.reset();
            Point point = new Point();
            Point toPixels = projection.toPixels((GeoPoint) data.get(0), point);
            float f = (float) toPixels.x;
            float f2 = (float) toPixels.y;
            for (GeoPoint toPixels2 : data) {
                toPixels = projection.toPixels(toPixels2, point);
                f = (float) toPixels.x;
                f2 = (float) toPixels.y;
                if (i == 0) {
                    this.path.moveTo(f, f2);
                } else {
                    this.path.lineTo(f, f2);
                }
                if (this.showPoints) {
                    if (this.pointPaint == null) {
                        this.pointPaint = createPointPaint();
                    }
                    canvas.drawCircle(f, f2, (float) ((int) this.pointPaint.getStrokeWidth()), this.pointPaint);
                }
                i++;
            }
            System.currentTimeMillis();
            if (this.DEBUG) {
                canvas.drawPath(this.path, this.linePaint);
            } else {
                canvas.drawPath(this.path, this.linePaint);
            }
            if (!this.DEBUG) {
            }
        }
    }

    private Paint createPointPaint() {
        if (this.pointPaint != null) {
            return this.pointPaint;
        }
        Paint paint = new Paint(1);
        paint.setColor(this.linePaint.getColor());
        paint.setAlpha(this.linePaint.getAlpha());
        paint.setStrokeWidth(this.linePaint.getStrokeWidth());
        return paint;
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
        return this.polygon.contains(geoPoint);
    }

    public void destroy() {
        this.polygon = null;
        this.boundingBox = null;
        this.path = null;
        this.linePaint = null;
        this.pointPaint = null;
    }
}
