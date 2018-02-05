package com.mapquest.android.maps;

import android.graphics.Point;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnKeyListener;
import java.util.Queue;

public final class MapController implements OnKeyListener {
    private static final String TAG = "com.mapquest.android.maps.mapcontroller";
    private MapHandler handler;
    private MapAnimator mapAnimator;
    private MapView mapView;

    class MapHandler extends Handler {
        static final String KEY_LATITUDE = "latitude";
        static final String KEY_LONGITUDE = "longitude";
        static final String KEY_LR_LATITUDE = "lr_latitude";
        static final String KEY_LR_LONGITUDE = "lr_longitude";
        static final String KEY_MAP_ROTATION = "scale";
        static final String KEY_SCALE_POINT_X = "scale_point_x";
        static final String KEY_SCALE_POINT_Y = "scale_point_x";
        static final String KEY_UL_LATITUDE = "ul_latitude";
        static final String KEY_UL_LONGITUDE = "ul_longitude";
        static final String KEY_ZOOM_LEVEL = "zoom_level";
        static final String KEY_ZOOM_TO_SPAN = "scale";
        static final int MSG_CENTER = 0;
        static final int MSG_ROTATION = 4;
        static final int MSG_ZOOM = 1;
        static final int MSG_ZOOM_TO_SPAN = 2;
        static final int MSG_ZOOM_TO_SPAN_BBOX = 3;
        MapView mapView;

        private MapHandler(MapView mapView) {
            this.mapView = mapView;
        }

        public void handleMessage(Message message) {
            switch (message.what) {
                case 0:
                    EventDispatcher.sendEmptyMessage(21);
                    this.mapView.setMapCenter(new GeoPoint(message.getData().getInt(KEY_LATITUDE), message.getData().getInt(KEY_LONGITUDE)), this.mapView.getZoomLevel());
                    this.mapView.invalidate();
                    EventDispatcher.sendEmptyMessage(23);
                    return;
                case 1:
                    EventDispatcher.sendEmptyMessage(11);
                    this.mapView.setZoomLevel(message.getData().getInt(KEY_ZOOM_LEVEL));
                    this.mapView.invalidate();
                    EventDispatcher.sendEmptyMessage(12);
                    return;
                case 2:
                    this.mapView.zoomToSpan(message.getData().getInt(KEY_LATITUDE), message.getData().getInt(KEY_LONGITUDE));
                    this.mapView.invalidate();
                    return;
                case 3:
                    this.mapView.zoomToSpan(new BoundingBox(new GeoPoint(message.getData().getInt(KEY_UL_LATITUDE), message.getData().getInt(KEY_UL_LONGITUDE)), new GeoPoint(message.getData().getInt(KEY_LR_LATITUDE), message.getData().getInt(KEY_LR_LONGITUDE))), true);
                    this.mapView.invalidate();
                    return;
                case 4:
                    EventDispatcher.sendEmptyMessage(31);
                    this.mapView.setMapRotation(message.getData().getFloat("scale"));
                    EventDispatcher.sendEmptyMessage(32);
                    this.mapView.invalidate();
                    EventDispatcher.sendEmptyMessage(33);
                    return;
                default:
                    return;
            }
        }

        public void sendSetCenter(GeoPoint geoPoint) {
            Message obtain = Message.obtain();
            obtain.getData().putInt(KEY_LATITUDE, geoPoint.getLatitudeE6());
            obtain.getData().putInt(KEY_LONGITUDE, geoPoint.getLongitudeE6());
            obtain.what = 0;
            dispatchOrSend(obtain);
        }

        public void sendSetZoom(int i) {
            Message obtain = Message.obtain();
            obtain.getData().putInt(KEY_ZOOM_LEVEL, i);
            obtain.what = 1;
            dispatchOrSend(obtain);
        }

        public void sendSetMapRotation(float f) {
            Message obtain = Message.obtain();
            obtain.getData().putFloat("scale", f);
            obtain.what = 4;
            dispatchOrSend(obtain);
        }

        public void sendZoomToSpan(int i, int i2) {
            Message obtain = Message.obtain();
            obtain.getData().putInt(KEY_LATITUDE, i);
            obtain.getData().putInt(KEY_LONGITUDE, i2);
            obtain.what = 2;
            dispatchOrSend(obtain);
        }

        public void sendZoomToSpan(BoundingBox boundingBox) {
            Message obtain = Message.obtain();
            obtain.getData().putInt(KEY_UL_LATITUDE, boundingBox.ul.getLatitudeE6());
            obtain.getData().putInt(KEY_UL_LONGITUDE, boundingBox.ul.getLongitudeE6());
            obtain.getData().putInt(KEY_LR_LATITUDE, boundingBox.lr.getLatitudeE6());
            obtain.getData().putInt(KEY_LR_LONGITUDE, boundingBox.lr.getLongitudeE6());
            obtain.what = 3;
            dispatchOrSend(obtain);
        }

        private void dispatchOrSend(Message message) {
            if (Util.checkIfSameThread(this)) {
                dispatchMessage(message);
            } else {
                sendMessage(message);
            }
        }
    }

    MapController(MapView mapView) {
        this.mapView = mapView;
        this.mapAnimator = new MapAnimator(mapView);
        this.handler = new MapHandler(mapView);
    }

    public void animateTo(GeoPoint geoPoint) {
        this.mapAnimator.animateTo(geoPoint);
    }

    public void animateTo(GeoPoint geoPoint, Message message) {
        this.mapAnimator.animateTo(geoPoint, message);
    }

    public void animateTo(GeoPoint geoPoint, Runnable runnable) {
        this.mapAnimator.animateTo(geoPoint, runnable);
    }

    public void animateRotation(float f) {
        getMapAnimator().animateRotation((f + 360.0f) % 360.0f);
    }

    public boolean onKey(View view, int i, KeyEvent keyEvent) {
        return false;
    }

    public void scrollBy(int i, int i2) {
        this.handler.sendSetCenter(this.mapView.getProjection().fromPixels(this.mapView.getFocalPoint().x + i, this.mapView.getFocalPoint().y + i2));
    }

    public void setCenter(GeoPoint geoPoint) {
        this.handler.sendSetCenter(geoPoint);
    }

    public void setZoom(int i) {
        this.handler.sendSetZoom(i);
    }

    public void setMapRotation(float f) {
        this.handler.sendSetMapRotation(f);
    }

    public void stopAnimation(boolean z) {
        Queue queue = this.mapView.animators;
        while (!queue.isEmpty()) {
            Animator animator = (Animator) queue.element();
            if (animator.isAnimating()) {
                animator.stopAnimation(z);
            }
        }
    }

    public void stopPanning() {
        this.mapAnimator.stopSpanning(false);
    }

    public boolean zoomIn() {
        return zoomInFixing(this.mapView.getFocalPoint().x, this.mapView.getFocalPoint().y);
    }

    public boolean zoomInFixing(int i, int i2) {
        int round = (int) Math.round(((double) this.mapView.getZoomLevel()) - Util.log2((double) this.mapView.currentScale));
        Point point = new Point(i, i2);
        if (!this.mapView.validateZoomLevel(this.mapView.getZoomLevel() + 1)) {
            return false;
        }
        this.mapAnimator.animateZoomScaler(round, this.mapView.getZoomLevel() + 1, this.mapView.currentScale, point, false);
        return true;
    }

    public boolean zoomOut() {
        return zoomOutFixing(this.mapView.getFocalPoint().x, this.mapView.getFocalPoint().y);
    }

    public boolean zoomOutFixing(int i, int i2) {
        int round = (int) Math.round(((double) this.mapView.getZoomLevel()) - Util.log2((double) this.mapView.currentScale));
        Point point = new Point(i, i2);
        if (!this.mapView.validateZoomLevel(this.mapView.getZoomLevel() - 1)) {
            return false;
        }
        this.mapAnimator.animateZoomScaler(round, this.mapView.getZoomLevel() - 1, this.mapView.currentScale, point, false);
        return true;
    }

    public void zoomToSpan(int i, int i2) {
        this.handler.sendZoomToSpan(i, i2);
    }

    public void zoomToSpan(BoundingBox boundingBox) {
        this.handler.sendZoomToSpan(boundingBox);
    }

    MapAnimator getMapAnimator() {
        return this.mapAnimator;
    }

    void destroy() {
        this.mapView = null;
        this.mapAnimator = null;
    }
}
