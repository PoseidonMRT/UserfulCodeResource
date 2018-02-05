package com.mqunar.core.basectx.launcherfragment.app;

import android.view.MotionEvent;
import com.mqunar.core.basectx.activity.QFragmentActivity;

public class InnerFlipActivity extends QFragmentActivity {
    public boolean superDispatchTouchEvent(MotionEvent motionEvent) {
        return super.dispatchTouchEvent(motionEvent);
    }
}
