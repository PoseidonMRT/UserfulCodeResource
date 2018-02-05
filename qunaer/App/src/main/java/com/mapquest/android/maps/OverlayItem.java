package com.mapquest.android.maps;

import android.graphics.drawable.Drawable;
import android.os.Bundle;

public class OverlayItem {
    public static final int ITEM_STATE_FOCUSED_MASK = 4;
    public static final int ITEM_STATE_PRESSED_MASK = 2;
    public static final int ITEM_STATE_SELECTED_MASK = 1;
    private Bundle extraInfo;
    protected int mAlignment;
    protected Drawable mMarker;
    protected GeoPoint mPoint;
    protected String mSnippet;
    protected String mTitle;
    private int state;

    public OverlayItem(GeoPoint geoPoint, String str, String str2) {
        this.mPoint = geoPoint;
        this.mTitle = str;
        this.mSnippet = str2;
    }

    public Drawable getMarker(int i) {
        if (this.mMarker != null) {
            setState(this.mMarker, i);
        }
        return this.mMarker;
    }

    public GeoPoint getPoint() {
        return this.mPoint;
    }

    public String getSnippet() {
        return this.mSnippet;
    }

    public void setSnippet(String str) {
        this.mSnippet = str;
    }

    public String getTitle() {
        return this.mTitle;
    }

    public void setTitle(String str) {
        this.mTitle = str;
    }

    public void setMarker(Drawable drawable) {
        this.mMarker = drawable;
    }

    public Bundle getExtraInfo() {
        return this.extraInfo;
    }

    public void setExtraInfo(Bundle bundle) {
        this.extraInfo = bundle;
    }

    public static void setState(Drawable drawable, int i) {
        int i2;
        int i3;
        int[] iArr = new int[3];
        if ((i & 2) > 0) {
            i2 = 1;
            iArr[0] = 16842919;
        } else {
            i2 = 0;
        }
        if ((i & 1) > 0) {
            i3 = i2 + 1;
            iArr[i2] = 16842913;
        } else {
            i3 = i2;
        }
        if ((i & 4) > 0) {
            i2 = i3 + 1;
            iArr[i3] = 16842908;
        }
        drawable.setState(iArr);
    }

    public int getState() {
        return this.state;
    }

    void setState(int i) {
        this.state = i;
    }

    public void setAlignment(int i) {
        this.mAlignment = i;
    }

    public int getAlignment() {
        return this.mAlignment;
    }

    public String routableAddress() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(this.mPoint.getLatitude()).append(",").append(this.mPoint.getLongitude());
        return stringBuilder.toString();
    }

    public void recycle() {
        if (this.mMarker != null) {
            this.mMarker.setCallback(null);
            this.mMarker = null;
        }
    }
}
