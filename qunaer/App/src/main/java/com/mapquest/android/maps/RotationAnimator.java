package com.mapquest.android.maps;

class RotationAnimator extends Animator {
    private static final int ANIMATION_DURATION = 900;
    private static final String TAG = RotationAnimator.class.getSimpleName();
    private float currentDeg;
    private float deltaDeg;
    private long startTime;
    private float toDeg;

    public RotationAnimator(MapView mapView) {
        super(mapView);
        this.toDeg = 0.0f;
        this.currentDeg = 0.0f;
        this.deltaDeg = 0.0f;
        this.startTime = 0;
    }

    public RotationAnimator(MapView mapView, Runnable runnable) {
        super(mapView, runnable);
        this.toDeg = 0.0f;
        this.currentDeg = 0.0f;
        this.deltaDeg = 0.0f;
        this.startTime = 0;
    }

    public RotationAnimator(MapView mapView, float f) {
        this(mapView);
        this.toDeg = f;
    }

    public RotationAnimator(MapView mapView, Runnable runnable, float f) {
        this(mapView, runnable);
        this.toDeg = f;
    }

    public void preAnimation() {
        if (getDuration() == 0) {
            setDuration(ANIMATION_DURATION);
        }
        this.currentDeg = this.mapView.getMapRotation();
        this.toDeg %= 360.0f;
        this.startTime = System.currentTimeMillis();
        EventDispatcher.sendEmptyMessage(31);
    }

    public boolean doAnimation() {
        long currentTimeMillis = System.currentTimeMillis() - this.startTime;
        if (currentTimeMillis >= ((long) getDuration()) && ((double) Math.abs(this.currentDeg - this.toDeg)) >= 0.01d) {
            currentTimeMillis = (long) getDuration();
        }
        if (this.currentDeg == this.toDeg) {
            return false;
        }
        if (currentTimeMillis > ((long) getDuration())) {
            return false;
        }
        this.deltaDeg = this.toDeg - this.currentDeg;
        if (Math.abs(this.deltaDeg) > 180.0f) {
            this.deltaDeg = 360.0f - Math.abs(this.deltaDeg);
            if (this.toDeg > this.currentDeg) {
                this.deltaDeg *= -1.0f;
                this.currentDeg += 360.0f;
            }
        }
        this.currentDeg = ((((((float) currentTimeMillis) * 1.0f) / ((float) getDuration())) * this.deltaDeg) + this.currentDeg) % 360.0f;
        this.mapView.setMapRotation(this.currentDeg);
        EventDispatcher.sendEmptyMessage(32);
        return true;
    }

    public void postAnimation() {
        EventDispatcher.sendEmptyMessage(33);
    }
}
