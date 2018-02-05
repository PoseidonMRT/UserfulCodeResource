package com.mapquest.android.maps;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.Region.Op;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.MotionEvent;

public class DrawableOverlay extends Overlay {
    private BoundingBox boundingBox;
    private Drawable drawable;

    public DrawableOverlay(Drawable drawable, BoundingBox boundingBox) {
        this();
        this.drawable = drawable;
        this.boundingBox = boundingBox;
    }

    public void draw(Canvas canvas, MapView mapView, boolean z) {
        if (this.boundingBox != null && this.drawable != null && !z) {
            try {
                Rect clipBounds = canvas.getClipBounds();
                canvas.save();
                if (mapView.getMapRotation() != 0.0f) {
                    Object obj = null;
                    if (!(clipBounds.width() == mapView.getWidth() && clipBounds.height() == mapView.getHeight())) {
                        canvas.save(2);
                        canvas.clipRect(0.0f, 0.0f, (float) mapView.getWidth(), (float) mapView.getHeight(), Op.REPLACE);
                        obj = 1;
                    }
                    canvas.rotate(mapView.getMapRotation(), (float) mapView.focalPoint.x, (float) mapView.focalPoint.y);
                    if (obj != null) {
                    }
                }
                Rect createOriginRectFromBoundingBox = Util.createOriginRectFromBoundingBox(this.boundingBox, mapView);
                ((RotatableProjection) mapView.getProjection()).offsetToFocalPoint(createOriginRectFromBoundingBox);
                if (Rect.intersects(createOriginRectFromBoundingBox, clipBounds)) {
                    this.drawable.setBounds(createOriginRectFromBoundingBox);
                    this.drawable.draw(canvas);
                }
                canvas.restore();
            } finally {
                canvas.restore();
            }
        }
    }

    public void setDrawable(Drawable drawable, BoundingBox boundingBox) {
        if (this.drawable != null && (this.drawable instanceof BitmapDrawable)) {
            ((BitmapDrawable) this.drawable).getBitmap().recycle();
            this.drawable = null;
        }
        if (drawable != null) {
            drawable.setDither(true);
            drawable.setFilterBitmap(true);
            this.drawable = drawable;
        }
        this.boundingBox = boundingBox;
    }

    public Drawable getDrawable() {
        return this.drawable;
    }

    public BoundingBox getBoundingBox() {
        return this.boundingBox;
    }

    public boolean onTap(MapView mapView) {
        return false;
    }

    public boolean onTap(GeoPoint geoPoint, MapView mapView) {
        if (this.boundingBox == null || !this.boundingBox.contains(geoPoint)) {
            return false;
        }
        boolean z;
        if (this.tapListener != null) {
            this.tapListener.onTap(geoPoint, mapView);
            z = true;
        } else {
            z = false;
        }
        if (z || onTap(mapView)) {
            return true;
        }
        return false;
    }

    public boolean onTouchEvent(MotionEvent motionEvent, MapView mapView) {
        if (!(this.touchListener == null || this.boundingBox == null)) {
            if (this.boundingBox.contains(mapView.getProjection().fromPixels((int) motionEvent.getX(), (int) motionEvent.getY()))) {
                this.touchListener.onTouch(motionEvent, mapView);
                return true;
            }
        }
        return false;
    }

    public void destroy() {
        if (this.drawable != null && (this.drawable instanceof BitmapDrawable)) {
            ((BitmapDrawable) this.drawable).getBitmap().recycle();
        }
        this.drawable = null;
        this.boundingBox = null;
    }
}
