package com.baidu.mapapi.map;

import com.baidu.platform.comapi.map.C0005e;

public final class UiSettings {
    private C0005e a;

    UiSettings(C0005e c0005e) {
        this.a = c0005e;
    }

    public boolean isCompassEnabled() {
        return this.a.q();
    }

    public boolean isOverlookingGesturesEnabled() {
        return this.a.y();
    }

    public boolean isRotateGesturesEnabled() {
        return this.a.x();
    }

    public boolean isScrollGesturesEnabled() {
        return this.a.v();
    }

    public boolean isZoomGesturesEnabled() {
        return this.a.w();
    }

    public void setAllGesturesEnabled(boolean z) {
        setRotateGesturesEnabled(z);
        setScrollGesturesEnabled(z);
        setOverlookingGesturesEnabled(z);
        setZoomGesturesEnabled(z);
    }

    public void setCompassEnabled(boolean z) {
        this.a.h(z);
    }

    public void setOverlookingGesturesEnabled(boolean z) {
        this.a.p(z);
    }

    public void setRotateGesturesEnabled(boolean z) {
        this.a.o(z);
    }

    public void setScrollGesturesEnabled(boolean z) {
        this.a.m(z);
    }

    public void setZoomGesturesEnabled(boolean z) {
        this.a.n(z);
    }
}
