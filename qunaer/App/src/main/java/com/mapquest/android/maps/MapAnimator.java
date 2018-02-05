package com.mapquest.android.maps;

import android.graphics.Point;
import android.os.Message;
import com.mapquest.android.maps.Overlay.Snappable;

class MapAnimator {
    private static final String TAG = "com.mapquest.android.maps.mapanimator";
    FlingAnimator flingAnimator;
    private MapView mapView;

    public MapAnimator(MapView mapView) {
        this.mapView = mapView;
        this.flingAnimator = new FlingAnimator(mapView);
    }

    public void animateZoomScaler(int i, int i2, float f, Point point, boolean z) {
        if (z && i < i2) {
            applySnappable(point.x, point.y, point);
        }
        this.mapView.queueAnimator(new ZoomAnimator(this.mapView, i, i2, f, point));
    }

    private void applySnappable(int i, int i2, Point point) {
        for (Overlay overlay : this.mapView.getOverlays()) {
            if ((overlay instanceof Snappable) && ((Snappable) overlay).onSnapToItem(i, i2, point, this.mapView)) {
                return;
            }
        }
    }

    public void animatePan(int i, int i2) {
        this.flingAnimator.animate(i, i2);
        this.mapView.queueAnimator(this.flingAnimator);
    }

    public void animateTo(GeoPoint geoPoint) {
        if (geoPoint != null) {
            animateTo(geoPoint, (Runnable) null);
        }
    }

    public void animateTo(GeoPoint geoPoint, int i) {
        if (geoPoint != null) {
            animateTo(geoPoint);
            this.mapView.queueAnimator(new ZoomAnimator(this.mapView, this.mapView.getZoomLevel(), i, 1.0f, new Point(this.mapView.getFocalPoint().x, this.mapView.getFocalPoint().y)));
        }
    }

    public void animateTo(GeoPoint geoPoint, Runnable runnable) {
        if (geoPoint != null) {
            this.flingAnimator.animate(geoPoint);
            this.flingAnimator.setRunnable(runnable);
            this.mapView.queueAnimator(this.flingAnimator);
        }
    }

    public void animateTo(GeoPoint geoPoint, Message message) {
        if (geoPoint != null) {
            this.flingAnimator.animate(geoPoint);
            this.flingAnimator.setMessage(message);
            this.mapView.queueAnimator(this.flingAnimator);
        }
    }

    public void animateFlick(Point point, float f, float f2) {
        if (point != null) {
            this.flingAnimator.animate(point, f, f2);
            this.mapView.queueAnimator(this.flingAnimator);
        }
    }

    public void animateRotation(float f) {
        this.mapView.queueAnimator(new RotationAnimator(this.mapView, f));
    }

    public void stopSpanning() {
        stopSpanning(false);
    }

    public void stopSpanning(boolean z) {
        this.flingAnimator.stopAnimation(z);
    }
}
