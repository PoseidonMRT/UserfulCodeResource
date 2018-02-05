package com.baidu.mapapi.map;

import android.graphics.Bitmap;
import android.view.MotionEvent;
import com.baidu.mapapi.model.inner.GeoPoint;
import com.baidu.platform.comapi.map.E;
import com.baidu.platform.comapi.map.l;
import javax.microedition.khronos.opengles.GL10;

class i implements l {
    final /* synthetic */ MapView a;

    i(MapView mapView) {
        this.a = mapView;
    }

    public void a() {
        if (this.a.c != null && this.a.c.a() != null) {
            float f = this.a.c.a().D().a;
            if (this.a.r != f) {
                CharSequence format;
                int intValue = ((Integer) MapView.n.get((int) f)).intValue();
                int i = (int) (((double) intValue) / this.a.c.a().D().m);
                this.a.m.setPadding(i / 2, 0, i / 2, 0);
                if (intValue >= 1000) {
                    format = String.format(" %d公里 ", new Object[]{Integer.valueOf(intValue / 1000)});
                } else {
                    format = String.format(" %d米 ", new Object[]{Integer.valueOf(intValue)});
                }
                this.a.k.setText(format);
                this.a.l.setText(format);
                this.a.r = f;
            }
            this.a.b();
            this.a.requestLayout();
        }
    }

    public void a(Bitmap bitmap) {
    }

    public void a(MotionEvent motionEvent) {
    }

    public void a(GeoPoint geoPoint) {
    }

    public void a(E e) {
    }

    public void a(String str) {
    }

    public void a(GL10 gl10, E e) {
    }

    public void a(boolean z) {
    }

    public void b() {
    }

    public void b(GeoPoint geoPoint) {
    }

    public void b(E e) {
    }

    public boolean b(String str) {
        return false;
    }

    public void c() {
    }

    public void c(GeoPoint geoPoint) {
    }

    public void c(E e) {
    }

    public void d() {
    }

    public void d(GeoPoint geoPoint) {
    }

    public void e() {
    }

    public void e(GeoPoint geoPoint) {
    }

    public void f() {
    }
}
