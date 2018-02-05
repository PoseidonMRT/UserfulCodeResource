package com.mapquest.android.maps;

import android.view.MotionEvent;

interface MapTouchEventHandler {
    boolean handleTouchEvent(MotionEvent motionEvent);
}
