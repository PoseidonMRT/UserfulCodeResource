package com.mapquest.android.maps;

import android.os.Handler;
import android.os.Message;
import android.view.MotionEvent;
import android.view.ViewConfiguration;

public class TrackballGestureDetector {
    private static final int DOUBLE_TAP_TIMEOUT = ViewConfiguration.getDoubleTapTimeout();
    private static final int EVENT_EXPIRY = (((LONG_PRESS_TIMEOUT + DOUBLE_TAP_TIMEOUT) + TAP_TIMEOUT) * 2);
    private static final int LONG_PRESS = 0;
    private static final int LONG_PRESS_TIMEOUT = ViewConfiguration.getLongPressTimeout();
    private static final int RESET = 2;
    private static final int TAP = 1;
    private static final int TAP_TIMEOUT = (ViewConfiguration.getTapTimeout() * 2);
    private float currentDownX;
    private float currentDownY;
    private boolean doubleTap = false;
    EventDispatcher eventDispatcher = new EventDispatcher();
    private float firstDownX;
    private float firstDownY;
    private long lastKeyDownTime = 0;
    private long lastKeyUpTime = 0;
    private boolean longPress = false;
    private Runnable longPressRunnable = null;
    private boolean scroll = false;
    private float scrollX;
    private float scrollY;
    private boolean tap = false;
    private Runnable tapRunnable = null;

    class EventDispatcher extends Handler {
        private EventDispatcher() {
        }

        public void handleMessage(Message message) {
            switch (message.what) {
                case 0:
                    TrackballGestureDetector.this.longPress = true;
                    if (TrackballGestureDetector.this.longPressRunnable != null) {
                        TrackballGestureDetector.this.longPressRunnable.run();
                        return;
                    }
                    return;
                case 1:
                    TrackballGestureDetector.this.tap = false;
                    if (TrackballGestureDetector.this.tapRunnable != null) {
                        TrackballGestureDetector.this.tapRunnable.run();
                        return;
                    }
                    return;
                case 2:
                    TrackballGestureDetector.this.reset();
                    return;
                default:
                    return;
            }
        }
    }

    public void analyze(MotionEvent motionEvent) {
        int action = motionEvent.getAction();
        float x = motionEvent.getX() * 100.0f;
        float y = motionEvent.getY() * 100.0f;
        this.eventDispatcher.removeMessages(2);
        switch (action) {
            case 0:
                this.currentDownX = x;
                this.currentDownY = y;
                this.lastKeyDownTime = motionEvent.getDownTime();
                if (Math.abs(motionEvent.getDownTime() - this.lastKeyUpTime) >= ((long) DOUBLE_TAP_TIMEOUT)) {
                    this.eventDispatcher.removeMessages(0);
                    this.eventDispatcher.sendEmptyMessageAtTime(0, (motionEvent.getDownTime() + ((long) TAP_TIMEOUT)) + ((long) LONG_PRESS_TIMEOUT));
                    this.firstDownX = x;
                    this.firstDownY = y;
                    break;
                }
                this.eventDispatcher.removeMessages(0);
                this.eventDispatcher.removeMessages(1);
                this.tap = false;
                this.doubleTap = true;
                this.scroll = false;
                this.longPress = false;
                break;
            case 1:
                if ((this.lastKeyUpTime == 0 || Math.abs(this.lastKeyDownTime - this.lastKeyUpTime) > ((long) DOUBLE_TAP_TIMEOUT)) && Math.abs(this.lastKeyDownTime - motionEvent.getEventTime()) < ((long) TAP_TIMEOUT)) {
                    this.eventDispatcher.removeMessages(0);
                    this.eventDispatcher.sendEmptyMessageDelayed(1, (long) DOUBLE_TAP_TIMEOUT);
                    this.tap = true;
                }
                this.doubleTap = false;
                this.scroll = false;
                this.longPress = false;
                this.lastKeyUpTime = motionEvent.getEventTime();
                break;
            case 2:
                if (!this.doubleTap && !this.tap && !this.longPress) {
                    this.scrollX = x;
                    this.scrollY = y;
                    if (Math.abs(this.scrollX) >= 1.0f || Math.abs(this.scrollY) >= 1.0f) {
                        this.eventDispatcher.removeMessages(0);
                        this.scroll = true;
                        this.tap = false;
                        this.longPress = false;
                        break;
                    }
                }
                this.doubleTap = false;
                this.tap = false;
                break;
                break;
            case 3:
                this.eventDispatcher.removeMessages(0);
                this.tap = false;
                break;
        }
        this.eventDispatcher.sendEmptyMessageDelayed(2, (long) EVENT_EXPIRY);
    }

    public void registerLongPressCallback(Runnable runnable) {
        this.longPressRunnable = runnable;
    }

    public void registerTapCallback(Runnable runnable) {
        this.tapRunnable = runnable;
    }

    public float getCurrentDownX() {
        return this.currentDownX;
    }

    public float getCurrentDownY() {
        return this.currentDownY;
    }

    public float getFirstDownX() {
        return this.firstDownX;
    }

    public float getFirstDownY() {
        return this.firstDownY;
    }

    public float scrollX() {
        return this.scrollX;
    }

    public float scrollY() {
        return this.scrollY;
    }

    public boolean isDoubleTap() {
        return this.doubleTap;
    }

    public boolean isScroll() {
        return this.scroll;
    }

    public boolean isTap() {
        return this.tap;
    }

    private void reset() {
        this.tap = false;
        this.doubleTap = false;
        this.scroll = false;
        this.longPress = false;
        this.scrollX = 0.0f;
        this.scrollY = 0.0f;
        this.currentDownX = 0.0f;
        this.currentDownY = 0.0f;
        this.firstDownY = 0.0f;
        this.firstDownY = 0.0f;
        this.lastKeyUpTime = 0;
    }
}
