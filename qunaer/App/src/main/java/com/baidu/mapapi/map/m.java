package com.baidu.mapapi.map;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;

class m extends AnimatorListenerAdapter {
    final /* synthetic */ SwipeDismissTouchListener a;

    m(SwipeDismissTouchListener swipeDismissTouchListener) {
        this.a = swipeDismissTouchListener;
    }

    public void onAnimationEnd(Animator animator) {
        this.a.a();
    }
}
