package com.mapquest.android.maps;

import android.os.Message;
import android.view.MotionEvent;

class DefaultTrackBallHandler {
    private static final String TAG = DefaultTrackBallHandler.class.getSimpleName();
    private MapView mapView;
    private boolean trackballZoomMode = false;

    public DefaultTrackBallHandler(MapView mapView) {
        this.mapView = mapView;
    }

    public boolean handleTrackballEvent(MotionEvent motionEvent) {
        int action = motionEvent.getAction() & 255;
        if (action == 0) {
            this.trackballZoomMode = !this.trackballZoomMode;
            Message obtain = Message.obtain();
            obtain.what = 6;
            obtain.getData().putBoolean("state", this.trackballZoomMode);
            EventDispatcher.sendMessage(obtain);
        } else if (action == 2) {
            if (!this.trackballZoomMode) {
                this.mapView.getController().scrollBy((int) (-(motionEvent.getX() * 100.0f)), (int) (-(motionEvent.getY() * 100.0f)));
            } else if (((double) motionEvent.getY()) != 0.0d) {
                if (0.0f < motionEvent.getY()) {
                    this.mapView.getController().zoomOut();
                } else {
                    this.mapView.getController().zoomIn();
                }
            }
        }
        return true;
    }
}
