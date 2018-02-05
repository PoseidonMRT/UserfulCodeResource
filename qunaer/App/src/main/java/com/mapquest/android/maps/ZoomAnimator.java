package com.mapquest.android.maps;

import android.graphics.Point;

class ZoomAnimator extends Animator {
    private static final int DURATION = 400;
    private Point centerPoint;
    private float currentScale;
    private float deltaScale;
    private int endZoom;
    private float finalScale;
    private GeoPoint geoPoint;
    private float initialScale;
    private long startTime;
    private int startZoom;

    public ZoomAnimator(MapView mapView) {
        super(mapView);
        this.initialScale = 1.0f;
        this.currentScale = 1.0f;
        this.finalScale = 1.0f;
        this.startTime = 0;
    }

    public ZoomAnimator(MapView mapView, Runnable runnable) {
        super(mapView, runnable);
        this.initialScale = 1.0f;
        this.currentScale = 1.0f;
        this.finalScale = 1.0f;
        this.startTime = 0;
    }

    public ZoomAnimator(MapView mapView, int i, int i2, float f, Point point) {
        this(mapView);
        this.startZoom = i;
        this.endZoom = i2;
        this.initialScale = f;
        this.currentScale = f;
        this.centerPoint = point;
    }

    public void animate(float f, Point point) {
        this.currentScale = this.mapView.currentScale;
        this.finalScale *= f;
        setDuration(getDuration() + 400);
    }

    public void preAnimation() {
        if (getDuration() == 0) {
            setDuration(400);
        }
        if (!this.mapView.scaling) {
            EventDispatcher.sendEmptyMessage(11);
        }
        if (!(this.centerPoint.x == this.mapView.getFocalPoint().x && this.centerPoint.y == this.mapView.getFocalPoint().y)) {
            this.geoPoint = this.mapView.getProjection().fromPixels(this.centerPoint.x, this.centerPoint.y);
        }
        this.finalScale = (float) Math.pow(2.0d, (double) (this.endZoom - this.startZoom));
        this.deltaScale = this.finalScale - this.initialScale;
        this.startTime = System.currentTimeMillis();
    }

    public boolean doAnimation() {
        float currentTimeMillis = (float) (System.currentTimeMillis() - this.startTime);
        if (currentTimeMillis > ((float) getDuration()) && this.currentScale != this.finalScale) {
            currentTimeMillis = (float) getDuration();
        } else if (currentTimeMillis > ((float) getDuration())) {
            return false;
        }
        this.currentScale = ((currentTimeMillis / ((float) getDuration())) * this.deltaScale) + this.initialScale;
        this.mapView.setScale(this.currentScale, this.currentScale, (float) this.centerPoint.x, (float) this.centerPoint.y);
        return true;
    }

    public void postAnimation() {
        this.mapView.currentScale = this.currentScale;
        this.mapView.setZoomLevel(this.endZoom);
        if (this.geoPoint != null) {
            this.mapView.centerGeoPoint = this.geoPoint;
            Point focalPoint = this.mapView.getFocalPoint();
            int i = focalPoint.x + (focalPoint.x - this.centerPoint.x);
            int i2 = (focalPoint.y - this.centerPoint.y) + focalPoint.y;
            this.mapView.centerGeoPoint = this.mapView.getProjection().fromPixels(i, i2);
            this.geoPoint = null;
        }
        this.mapView.zoomEnd();
        EventDispatcher.sendEmptyMessage(12);
    }
}
