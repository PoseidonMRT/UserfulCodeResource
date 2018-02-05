package com.mqunar.idscan.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.view.View;
import com.mqunar.idscan.R;
import com.mqunar.idscan.a.f;
import com.mqunar.idscan.utils.a;

public final class ViewfinderView extends View {
    private float a;
    private float b;
    private float c;
    private int d;
    private int e;
    private Paint f;
    private float g;
    private float h;
    private int i;
    private int j;
    private boolean k;
    private f l;
    private Context m;
    private Bitmap n;
    private boolean o = true;
    private Rect p;
    private Bitmap q;

    public ViewfinderView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.m = context;
        this.a = a.b(32.0f);
        this.c = a.b(2.0f);
        this.b = a.b(3.0f);
        this.d = a.a(118.0f);
        this.e = a.a(6.65f);
        this.i = a.a(30.0f);
        this.g = a.b(18.0f);
        this.h = a.b(22.0f);
        this.f = new Paint(1);
        this.n = ((BitmapDrawable) getResources().getDrawable(R.drawable.idscan_passport_move_line)).getBitmap();
        this.q = ((BitmapDrawable) getResources().getDrawable(R.drawable.idscan_passport)).getBitmap();
    }

    public final void a() {
        this.o = true;
        invalidate();
    }

    public final void onDraw(Canvas canvas) {
        if (this.l != null) {
            Rect e = this.l.e();
            Rect f = this.l.f();
            if (f != null && e != null) {
                if (!this.k) {
                    this.k = true;
                    this.j = f.left;
                }
                int width = canvas.getWidth();
                int height = canvas.getHeight();
                canvas.drawBitmap(this.q, null, e, this.f);
                this.j += this.e;
                if (this.j >= f.right) {
                    this.j = f.left - this.d;
                }
                if (this.p == null) {
                    this.p = new Rect();
                }
                this.p.left = this.j < f.left ? f.left : this.j;
                this.p.right = this.j + this.d > f.right ? f.right - 2 : this.j + this.d;
                this.p.top = f.top;
                this.p.bottom = f.bottom;
                canvas.drawBitmap(this.n, null, this.p, this.f);
                if (this.o) {
                    postInvalidateDelayed(2, 0, 0, width, height);
                }
            }
        }
    }

    public final void setCameraManager(f fVar) {
        this.l = fVar;
    }

    public final void setLineShouldMove(boolean z) {
        this.o = z;
    }
}
