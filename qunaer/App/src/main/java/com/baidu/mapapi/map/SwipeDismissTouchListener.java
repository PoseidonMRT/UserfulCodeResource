package com.baidu.mapapi.map;

import android.animation.ValueAnimator;
import android.annotation.TargetApi;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewConfiguration;
import android.view.ViewGroup.LayoutParams;

public class SwipeDismissTouchListener implements OnTouchListener {
    private int a;
    private int b;
    private int c;
    private long d;
    private View e;
    private DismissCallbacks f;
    private int g = 1;
    private float h;
    private float i;
    private boolean j;
    private int k;
    private Object l;
    private VelocityTracker m;
    private float n;
    private boolean o;
    private boolean p;

    public interface DismissCallbacks {
        boolean canDismiss(Object obj);

        void onDismiss(View view, Object obj);

        void onNotify();
    }

    public SwipeDismissTouchListener(View view, Object obj, DismissCallbacks dismissCallbacks) {
        ViewConfiguration viewConfiguration = ViewConfiguration.get(view.getContext());
        this.a = viewConfiguration.getScaledTouchSlop();
        this.b = viewConfiguration.getScaledMinimumFlingVelocity();
        this.c = viewConfiguration.getScaledMaximumFlingVelocity();
        this.d = (long) view.getContext().getResources().getInteger(17694720);
        this.e = view;
        this.e.getContext();
        this.l = obj;
        this.f = dismissCallbacks;
    }

    @TargetApi(11)
    private void a() {
        LayoutParams layoutParams = this.e.getLayoutParams();
        ValueAnimator duration = ValueAnimator.ofInt(new int[]{this.e.getHeight(), 1}).setDuration(this.d);
        duration.addListener(new n(this, layoutParams, r1));
        duration.addUpdateListener(new o(this, layoutParams));
        duration.start();
    }

    @TargetApi(12)
    public boolean onTouch(View view, MotionEvent motionEvent) {
        boolean z = true;
        motionEvent.offsetLocation(this.n, 0.0f);
        if (this.g < 2) {
            this.g = this.e.getWidth();
        }
        float rawX;
        float xVelocity;
        switch (motionEvent.getActionMasked()) {
            case 0:
                this.h = motionEvent.getRawX();
                this.i = motionEvent.getRawY();
                if (!this.f.canDismiss(this.l)) {
                    return true;
                }
                this.o = false;
                this.m = VelocityTracker.obtain();
                this.m.addMovement(motionEvent);
                return true;
            case 1:
                if (this.m != null) {
                    boolean z2;
                    rawX = motionEvent.getRawX() - this.h;
                    this.m.addMovement(motionEvent);
                    this.m.computeCurrentVelocity(1000);
                    xVelocity = this.m.getXVelocity();
                    float abs = Math.abs(xVelocity);
                    float abs2 = Math.abs(this.m.getYVelocity());
                    if (Math.abs(rawX) > ((float) (this.g / 3)) && this.j) {
                        z2 = rawX > 0.0f;
                    } else if (((float) this.b) > abs || abs > ((float) this.c) || abs2 >= abs || abs2 >= abs || !this.j) {
                        z2 = false;
                        z = false;
                    } else {
                        z2 = ((xVelocity > 0.0f ? 1 : (xVelocity == 0.0f ? 0 : -1)) < 0) == ((rawX > 0.0f ? 1 : (rawX == 0.0f ? 0 : -1)) < 0);
                        if (this.m.getXVelocity() <= 0.0f) {
                            z = false;
                        }
                        boolean z3 = z;
                        z = z2;
                        z2 = z3;
                    }
                    if (z) {
                        this.e.animate().translationX(z2 ? (float) this.g : (float) (-this.g)).setDuration(this.d).setListener(new m(this));
                    } else if (this.j) {
                        this.e.animate().translationX(0.0f).setDuration(this.d).setListener(null);
                    }
                    this.m.recycle();
                    this.m = null;
                    this.n = 0.0f;
                    this.h = 0.0f;
                    this.i = 0.0f;
                    this.j = false;
                    break;
                }
                break;
            case 2:
                if (this.m != null) {
                    this.m.addMovement(motionEvent);
                    xVelocity = motionEvent.getRawX() - this.h;
                    rawX = motionEvent.getRawY() - this.i;
                    if (Math.abs(xVelocity) > ((float) this.a) && Math.abs(rawX) < Math.abs(xVelocity) / 2.0f) {
                        this.j = true;
                        this.k = xVelocity > 0.0f ? this.a : -this.a;
                        this.e.getParent().requestDisallowInterceptTouchEvent(true);
                        if (!this.o) {
                            this.o = true;
                            this.f.onNotify();
                        }
                        if (Math.abs(xVelocity) <= ((float) (this.g / 3))) {
                            this.p = false;
                        } else if (!this.p) {
                            this.p = true;
                            this.f.onNotify();
                        }
                        MotionEvent obtain = MotionEvent.obtain(motionEvent);
                        obtain.setAction((motionEvent.getActionIndex() << 8) | 3);
                        this.e.onTouchEvent(obtain);
                        obtain.recycle();
                    }
                    if (this.j) {
                        this.n = xVelocity;
                        this.e.setTranslationX(xVelocity - ((float) this.k));
                        return true;
                    }
                }
                break;
            case 3:
                if (this.m != null) {
                    this.e.animate().translationX(0.0f).setDuration(this.d).setListener(null);
                    this.m.recycle();
                    this.m = null;
                    this.n = 0.0f;
                    this.h = 0.0f;
                    this.i = 0.0f;
                    this.j = false;
                    break;
                }
                break;
        }
        return false;
    }
}
