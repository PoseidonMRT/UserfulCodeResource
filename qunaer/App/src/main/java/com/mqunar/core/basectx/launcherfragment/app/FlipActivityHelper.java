package com.mqunar.core.basectx.launcherfragment.app;

import android.app.Instrumentation;
import android.os.Bundle;
import android.support.v4.view.GestureDetectorCompat;
import android.view.GestureDetector.OnGestureListener;
import android.view.MotionEvent;
import android.view.ViewConfiguration;
import qunar.sdk.mapapi.utils.projectTransform.CoordConvertLocal;

public class FlipActivityHelper implements OnGestureListener {
    private static float SWIPE_MIN_DISTANCE = 150.0f;
    private static float SWIPE_THRESHOLD_VELOCITY = 100.0f;
    private boolean canFlip;
    private GestureDetectorCompat detector;
    private final InnerFlipActivity mActivity;
    private boolean touchOnHScroll;

    final class AnonymousClass1 extends Thread {
        final /* synthetic */ int val$KeyCode;

        AnonymousClass1(int i) {
            this.val$KeyCode = i;
        }

        public void run() {
            try {
                new Instrumentation().sendKeyDownUpSync(this.val$KeyCode);
            } catch (Exception e) {
            }
        }
    }

    public FlipActivityHelper(InnerFlipActivity innerFlipActivity) {
        this.mActivity = innerFlipActivity;
    }

    public void onCreate(Bundle bundle) {
        ViewConfiguration viewConfiguration = ViewConfiguration.get(this.mActivity);
        SWIPE_MIN_DISTANCE = this.mActivity.getResources().getDisplayMetrics().widthPixels == 0 ? SWIPE_MIN_DISTANCE : (float) ((this.mActivity.getResources().getDisplayMetrics().widthPixels * 1) / 3);
        SWIPE_THRESHOLD_VELOCITY = (float) (viewConfiguration.getScaledMinimumFlingVelocity() * 3);
        this.detector = new GestureDetectorCompat(this.mActivity, this);
        if (bundle != null) {
            setCanFlip(bundle.getBoolean("canFlip", true));
        }
    }

    public void onSaveInstanceState(Bundle bundle) {
        bundle.putBoolean("canFlip", isCanFlip());
    }

    public boolean isCanFlip() {
        return this.canFlip;
    }

    public void setCanFlip(boolean z) {
        this.canFlip = z;
    }

    public boolean onDown(MotionEvent motionEvent) {
        return false;
    }

    public void onShowPress(MotionEvent motionEvent) {
    }

    public boolean onSingleTapUp(MotionEvent motionEvent) {
        return false;
    }

    public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent2, float f, float f2) {
        return false;
    }

    public void onLongPress(MotionEvent motionEvent) {
    }

    public boolean onFling(MotionEvent motionEvent, MotionEvent motionEvent2, float f, float f2) {
        if (motionEvent == null || motionEvent2 == null) {
            return false;
        }
        float x = motionEvent2.getX() - motionEvent.getX();
        float y = motionEvent2.getY() - motionEvent.getY();
        float abs = Math.abs(x);
        y = Math.abs(y);
        int round = Math.round((float) ((Math.asin(((double) y) / Math.sqrt((double) ((abs * abs) + (y * y)))) / CoordConvertLocal.REAL_PI) * 180.0d));
        if (Math.abs(f) < SWIPE_THRESHOLD_VELOCITY || Math.abs(x) <= SWIPE_MIN_DISTANCE || round >= 30 || x <= 0.0f) {
            return false;
        }
        simulateKey(4);
        return true;
    }

    public boolean dispatchTouchEvent(MotionEvent motionEvent) {
        if (isCanFlip() && !this.touchOnHScroll && this.detector.onTouchEvent(motionEvent) && (motionEvent.getAction() & 255) == 1) {
            return true;
        }
        this.touchOnHScroll = false;
        return this.mActivity.superDispatchTouchEvent(motionEvent);
    }

    public boolean isTouchOnHScroll() {
        return this.touchOnHScroll;
    }

    public void setTouchOnHScroll(boolean z) {
        this.touchOnHScroll = z;
    }

    public static void simulateKey(int i) {
        new AnonymousClass1(i).start();
    }
}
