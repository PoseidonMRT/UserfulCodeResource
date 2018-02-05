package com.mapquest.android.maps;

import android.graphics.Point;
import android.widget.Scroller;
import com.mqunar.libtask.ProgressType;

class FlingAnimator extends Animator {
    private static final int DURATION = 600;
    private static final float SCROLL_FRICTION = 0.25f;
    private GeoPoint point;
    Scroller scroller;
    private int startX;
    private int startY;

    public FlingAnimator(MapView mapView) {
        super(mapView);
        this.scroller = new Scroller(mapView.getContext());
    }

    public FlingAnimator(MapView mapView, Runnable runnable) {
        super(mapView, runnable);
    }

    public void animate(int i, int i2) {
        super.startAnimation();
        this.startX = i;
        this.startY = i2;
        int i3 = this.mapView.getFocalPoint().x;
        int i4 = this.mapView.getFocalPoint().y;
        if (this.scroller.isFinished()) {
            this.scroller.startScroll(this.startX, this.startY, i3 - this.startX, i4 - this.startY, DURATION);
        } else {
            this.scroller.forceFinished(false);
        }
    }

    public void animate(GeoPoint geoPoint) {
        Projection projection = this.mapView.getProjection();
        if (projection != null) {
            Point toPixels = projection.toPixels(geoPoint, (Point) null);
            this.point = geoPoint;
            animate(toPixels.x, toPixels.y);
        }
    }

    public void animate(Point point, float f, float f2) {
        super.startAnimation();
        this.point = null;
        this.startX = point.x;
        this.startY = point.y;
        if (!this.scroller.isFinished()) {
            this.scroller.forceFinished(true);
        }
        this.scroller.fling(point.x, point.y, (int) (f * SCROLL_FRICTION), (int) (SCROLL_FRICTION * f2), Integer.MIN_VALUE, ProgressType.PRO_END, Integer.MIN_VALUE, ProgressType.PRO_END);
    }

    public void preAnimation() {
        EventDispatcher.sendEmptyMessage(21);
    }

    private void move() {
        Projection projection = this.mapView.getProjection();
        int currX = this.startX + (this.mapView.getFocalPoint().x - this.scroller.getCurrX());
        int currY = this.startY + (this.mapView.getFocalPoint().y - this.scroller.getCurrY());
        this.startX = this.scroller.getCurrX();
        this.startY = this.scroller.getCurrY();
        this.mapView.centerGeoPoint = projection.fromPixels(currX, currY);
        EventDispatcher.sendEmptyMessage(22);
    }

    public boolean doAnimation() {
        if (this.scroller.computeScrollOffset()) {
            move();
            return true;
        }
        move();
        return false;
    }

    public void postAnimation() {
        if (this.point != null) {
            this.mapView.centerGeoPoint = this.point;
            this.mapView.invalidate();
        }
        this.point = null;
        finishPan();
    }

    public void stopAnimation(boolean z) {
        super.stopAnimation(z);
        if (z) {
            this.scroller.abortAnimation();
        } else {
            this.scroller.forceFinished(true);
        }
    }

    public void finishPan() {
        EventDispatcher.sendEmptyMessage(23);
    }
}
