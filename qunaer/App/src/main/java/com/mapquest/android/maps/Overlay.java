package com.mapquest.android.maps;

import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.view.KeyEvent;
import android.view.MotionEvent;

public abstract class Overlay {
    public static final int BOTTOM = 32;
    public static final int CENTER = 3;
    public static final int CENTER_HORIZONTAL = 1;
    public static final int CENTER_VERTICAL = 2;
    public static final int LEFT = 4;
    public static final int RIGHT = 8;
    protected static final float SHADOW_X_SKEW = -0.9f;
    protected static final float SHADOW_Y_SCALE = 0.5f;
    public static final int TOP = 16;
    private String key = "";
    protected OverlayTapListener tapListener;
    protected OverlayTouchEventListener touchListener;
    protected OverlayTrackballEventListener trackballListener;
    private int zIndex = 0;

    public interface Snappable {
        boolean onSnapToItem(int i, int i2, Point point, MapView mapView);
    }

    public interface OverlayTapListener {
        void onTap(GeoPoint geoPoint, MapView mapView);
    }

    public interface OverlayTouchEventListener {
        void onTouch(MotionEvent motionEvent, MapView mapView);
    }

    public interface OverlayTrackballEventListener {
        void onTrackballEvent(MotionEvent motionEvent, MapView mapView);
    }

    public static Drawable setAlignment(Drawable drawable, int i) {
        if (drawable != null) {
            int intrinsicWidth = drawable.getIntrinsicWidth();
            int intrinsicHeight = drawable.getIntrinsicHeight();
            int i2 = -(intrinsicWidth >> 1);
            int i3 = -(intrinsicHeight >> 1);
            int i4 = i2 + intrinsicWidth;
            int i5 = i2;
            i2 = i3;
            int i6 = i3 + intrinsicHeight;
            int i7 = 0;
            while (i != 0) {
                i3 = i7 + 1;
                if (i7 >= 3) {
                    break;
                } else if ((i & 1) > 0) {
                    i4 = -(intrinsicWidth >> 1);
                    i ^= 1;
                    i5 = i4;
                    i4 += intrinsicWidth;
                    i7 = i3;
                } else if ((i & 2) > 0) {
                    i6 = -(intrinsicHeight >> 1);
                    i ^= 2;
                    i2 = i6;
                    i6 += intrinsicHeight;
                    i7 = i3;
                } else if ((i & 32) > 0) {
                    i ^= 32;
                    i6 = 0;
                    i2 = -intrinsicHeight;
                    i7 = i3;
                } else if ((i & 16) > 0) {
                    i ^= 16;
                    i7 = i3;
                    i6 = intrinsicHeight;
                    i2 = 0;
                } else if ((i & 8) > 0) {
                    i ^= 8;
                    i4 = 0;
                    i5 = -intrinsicWidth;
                    i7 = i3;
                } else if ((i & 4) > 0) {
                    i ^= 4;
                    i7 = i3;
                    i4 = intrinsicWidth;
                    i5 = 0;
                } else {
                    i7 = i3;
                }
            }
            drawable.setBounds(i5, i2, i4, i6);
        }
        return drawable;
    }

    protected static void drawAt(Canvas canvas, Drawable drawable, int i, int i2, boolean z) {
        try {
            canvas.save();
            canvas.translate((float) i, (float) i2);
            drawable.draw(canvas);
        } finally {
            canvas.restore();
        }
    }

    public void setTapListener(OverlayTapListener overlayTapListener) {
        this.tapListener = overlayTapListener;
    }

    public void setTouchEventListener(OverlayTouchEventListener overlayTouchEventListener) {
        this.touchListener = overlayTouchEventListener;
    }

    public void setTrackballEventListener(OverlayTrackballEventListener overlayTrackballEventListener) {
        this.trackballListener = overlayTrackballEventListener;
    }

    public void draw(Canvas canvas, MapView mapView, boolean z) {
    }

    public boolean draw(Canvas canvas, MapView mapView, boolean z, long j) {
        draw(canvas, mapView, z);
        return false;
    }

    public boolean onKeyDown(int i, KeyEvent keyEvent, MapView mapView) {
        return false;
    }

    public boolean onKeyUp(int i, KeyEvent keyEvent, MapView mapView) {
        return false;
    }

    public boolean onTap(GeoPoint geoPoint, MapView mapView) {
        return false;
    }

    public boolean onTouchEvent(MotionEvent motionEvent, MapView mapView) {
        return false;
    }

    public boolean onTrackballEvent(MotionEvent motionEvent, MapView mapView) {
        return false;
    }

    public void destroy() {
    }

    public int getZIndex() {
        return this.zIndex;
    }

    public void setZIndex(int i) {
        this.zIndex = i;
    }

    public String getKey() {
        return this.key;
    }

    public void setKey(String str) {
        this.key = str;
    }
}
