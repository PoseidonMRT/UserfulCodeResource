package com.mapquest.android.maps;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.view.View;

class CompassView extends View {
    private BitmapDrawable bg;
    private Context context;
    private BitmapDrawable needle;
    private float rotation;

    public CompassView(Context context) {
        super(context);
        init(context);
    }

    public CompassView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        init(context);
    }

    protected void onDraw(Canvas canvas) {
        int width = getWidth();
        int height = getHeight();
        try {
            canvas.save();
            canvas.translate((float) (width >> 1), (float) (height >> 1));
            this.bg.draw(canvas);
            canvas.rotate(this.rotation);
            this.needle.draw(canvas);
        } finally {
            canvas.restore();
        }
    }

    protected void onMeasure(int i, int i2) {
        setMeasuredDimension(Math.max(this.bg.getIntrinsicWidth(), this.needle.getIntrinsicWidth()), Math.max(this.bg.getIntrinsicHeight(), this.needle.getIntrinsicHeight()));
    }

    private void init(Context context) {
        this.needle = Util.getDrawable(context, "icn_compass_needle");
        this.bg = Util.getDrawable(context, "icn_compass_bg");
        Overlay.setAlignment(this.needle, 3);
        Overlay.setAlignment(this.bg, 3);
    }

    public void setRotation(float f) {
        this.rotation = f;
        if (getVisibility() == 0) {
            invalidate();
        }
    }
}
