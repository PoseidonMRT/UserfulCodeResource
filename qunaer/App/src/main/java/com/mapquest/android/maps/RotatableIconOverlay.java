package com.mapquest.android.maps;

import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.view.MotionEvent;

public class RotatableIconOverlay extends Overlay {
    public static final int ANCHOR_BOTTOM_CENTER = 4;
    public static final int ANCHOR_CENTER = 0;
    public static final int ANCHOR_CENTER_LEFT = 1;
    public static final int ANCHOR_CENTER_RIGHT = 2;
    public static final int ANCHOR_TOP_CENTER = 3;
    private static final String LOG_TAG = RotatableIconOverlay.class.getSimpleName();
    public static final int TOUCH_TOLERANCE = 10;
    private boolean DEBUG;
    private int alignment;
    private RectF clipBounds;
    private Drawable drawable;
    private RectF imageRegion;
    private Matrix matrix;
    private Paint paint;
    private GeoPoint point;
    private boolean rotateWithMap;
    private float rotation;

    public RotatableIconOverlay(GeoPoint geoPoint, Drawable drawable) {
        this(geoPoint, drawable, 35);
    }

    public RotatableIconOverlay(GeoPoint geoPoint, Drawable drawable, int i) {
        this.DEBUG = false;
        this.imageRegion = new RectF();
        this.clipBounds = new RectF();
        this.point = geoPoint;
        this.drawable = drawable;
        this.paint = new Paint();
        this.paint.setAntiAlias(true);
        this.paint.setDither(true);
        this.paint.setFilterBitmap(true);
        this.alignment = i;
        this.matrix = new Matrix();
        Overlay.setAlignment(drawable, i);
    }

    public GeoPoint getPoint() {
        return this.point;
    }

    public void setPoint(GeoPoint geoPoint) {
        this.point = geoPoint;
    }

    public Drawable getDrawable() {
        return this.drawable;
    }

    public void setDrawable(Drawable drawable) {
        this.drawable = drawable;
        Overlay.setAlignment(drawable, this.alignment);
    }

    public void setRotation(float f) {
        this.rotation = f;
        this.matrix.setRotate(f);
    }

    public void setAlignment(int i) {
        this.alignment = i;
        if (this.drawable != null) {
            Overlay.setAlignment(this.drawable, i);
        }
    }

    public int getAligment() {
        return this.alignment;
    }

    public void draw(Canvas canvas, MapView mapView, boolean z) {
        Point toPixels;
        if (this.DEBUG) {
            toPixels = mapView.getProjection().toPixels(this.point, (Point) null);
            this.imageRegion.set(this.drawable.getBounds());
            this.imageRegion.offset((float) toPixels.x, (float) toPixels.y);
            this.clipBounds.set(canvas.getClipBounds());
        } else {
            toPixels = mapView.getProjection().toPixels(this.point, (Point) null);
            this.imageRegion.set(this.drawable.getBounds());
            this.imageRegion.offset((float) toPixels.x, (float) toPixels.y);
            this.clipBounds.set(canvas.getClipBounds());
        }
        if (RectF.intersects(this.imageRegion, this.clipBounds)) {
            try {
                canvas.save();
                float f = this.rotation % 360.0f;
                if (this.rotateWithMap) {
                    f = (f + mapView.getMapRotation()) % 360.0f;
                }
                canvas.translate((float) toPixels.x, (float) toPixels.y);
                if (f != 0.0f) {
                    canvas.rotate(f);
                }
                this.drawable.draw(canvas);
            } finally {
                canvas.restore();
            }
        } else if (!this.DEBUG) {
        }
    }

    public boolean onTap(GeoPoint geoPoint, MapView mapView) {
        if (this.tapListener == null || !intersects(geoPoint, mapView)) {
            if (this.DEBUG) {
            }
            return false;
        }
        if (this.DEBUG) {
            this.tapListener.onTap(geoPoint, mapView);
        } else {
            this.tapListener.onTap(geoPoint, mapView);
        }
        return true;
    }

    public boolean onTouchEvent(MotionEvent motionEvent, MapView mapView) {
        if (this.touchListener == null || !intersects(mapView.getProjection().fromPixels((int) motionEvent.getX(), (int) motionEvent.getY()), mapView)) {
            return false;
        }
        this.touchListener.onTouch(motionEvent, mapView);
        return true;
    }

    public boolean onTrackballEvent(MotionEvent motionEvent, MapView mapView) {
        if (this.trackballListener == null || !intersects(mapView.getProjection().fromPixels((int) motionEvent.getX(), (int) motionEvent.getY()), mapView)) {
            return false;
        }
        this.trackballListener.onTrackballEvent(motionEvent, mapView);
        return true;
    }

    private boolean intersects(GeoPoint geoPoint, MapView mapView) {
        Point toPixels = mapView.getProjection().toPixels(this.point, (Point) null);
        Point toPixels2 = mapView.getProjection().toPixels(geoPoint, (Point) null);
        this.imageRegion.set(this.drawable.getBounds());
        this.matrix.mapRect(this.imageRegion);
        this.imageRegion.offset((float) toPixels.x, (float) toPixels.y);
        return this.imageRegion.contains((float) toPixels2.x, (float) toPixels2.y);
    }

    public void destroy() {
        this.point = null;
        this.drawable = null;
    }

    public boolean isRotateWithMap() {
        return this.rotateWithMap;
    }

    public void setRotateWithMap(boolean z) {
        this.rotateWithMap = z;
    }
}
