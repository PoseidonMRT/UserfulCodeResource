package com.baidu.platform.comapi.map;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Rect;
import android.graphics.SurfaceTexture;
import android.os.Bundle;
import android.os.Handler;
import android.view.GestureDetector;
import android.view.GestureDetector.OnDoubleTapListener;
import android.view.GestureDetector.OnGestureListener;
import android.view.MotionEvent;
import android.view.TextureView;
import android.view.TextureView.SurfaceTextureListener;
import com.baidu.mapapi.common.EnvironmentUtilities;
import com.baidu.mapapi.common.SysOSUtil;
import com.baidu.mapapi.model.inner.GeoPoint;
import com.baidu.platform.comapi.map.m.a;
import java.util.concurrent.atomic.AtomicBoolean;
import org.json.JSONException;
import org.json.JSONObject;
import qunar.sdk.mapapi.entity.QMarker;

@SuppressLint({"NewApi"})
public class F extends TextureView implements OnDoubleTapListener, OnGestureListener, SurfaceTextureListener, a {
    public static int a;
    public static int b;
    private GestureDetector c;
    private Handler d;
    private C0012m e = null;
    private C0005e f;

    public F(Context context, C c, String str) {
        super(context);
        a(context, c, str);
    }

    private void a(Context context, C c, String str) {
        setSurfaceTextureListener(this);
        if (context == null) {
            throw new RuntimeException("when you create an mapview, the context can not be null");
        }
        this.c = new GestureDetector(context, this);
        EnvironmentUtilities.initAppDirectory(context);
        if (this.f == null) {
            this.f = new C0005e(context, str);
        }
        this.f.a();
        this.f.b();
        this.f.a(c);
        f();
        this.f.a(this.d);
        this.f.e();
    }

    private void f() {
        this.d = new G(this);
    }

    public int a() {
        return this.f == null ? 0 : MapRenderer.nativeRender(this.f.h);
    }

    public void a(String str, Rect rect) {
        if (this.f != null && this.f.g != null) {
            if (rect != null) {
                int i = rect.left;
                int i2 = b < rect.bottom ? 0 : b - rect.bottom;
                int width = rect.width();
                int height = rect.height();
                if (i >= 0 && i2 >= 0 && width > 0 && height > 0) {
                    if (width > a) {
                        width = Math.abs(rect.width()) - (rect.right - a);
                    }
                    if (height > b) {
                        height = Math.abs(rect.height()) - (rect.bottom - b);
                    }
                    if (i > SysOSUtil.getScreenSizeX() || i2 > SysOSUtil.getScreenSizeY()) {
                        this.f.g.a(str, null);
                        if (this.e != null) {
                            this.e.a();
                            return;
                        }
                        return;
                    }
                    a = width;
                    b = height;
                    Bundle bundle = new Bundle();
                    bundle.putInt(MapViewConstants.ATTR_X, i);
                    bundle.putInt(MapViewConstants.ATTR_Y, i2);
                    bundle.putInt("width", width);
                    bundle.putInt(QMarker.MARKER_HEIGHT, height);
                    this.f.g.a(str, bundle);
                    if (this.e != null) {
                        this.e.a();
                        return;
                    }
                    return;
                }
                return;
            }
            this.f.g.a(str, null);
            if (this.e != null) {
                this.e.a();
            }
        }
    }

    public C0005e b() {
        return this.f;
    }

    public void c() {
        if (this.f != null && this.f.g != null) {
            for (l d : this.f.f) {
                d.d();
            }
            this.f.g.i();
            this.f.g.f();
            this.f.g.p();
            if (this.e != null) {
                this.e.a();
            }
        }
    }

    public void d() {
        if (this.f != null && this.f.g != null) {
            this.f.g.e();
            synchronized (this.f) {
                this.f.g.e();
                if (this.e != null) {
                    this.e.b();
                }
            }
        }
    }

    public void e() {
        synchronized (this.f) {
            for (l f : this.f.f) {
                f.f();
            }
            if (this.f != null) {
                this.f.b(this.d);
                this.f.M();
                this.f = null;
            }
            this.d.removeCallbacksAndMessages(null);
        }
    }

    public boolean onDoubleTap(MotionEvent motionEvent) {
        if (this.f == null || this.f.g == null || !this.f.i) {
            return true;
        }
        GeoPoint b = this.f.b((int) motionEvent.getX(), (int) motionEvent.getY());
        if (b == null) {
            return false;
        }
        for (l b2 : this.f.f) {
            b2.b(b);
        }
        if (!this.f.e) {
            return false;
        }
        E D = this.f.D();
        D.a += 1.0f;
        D.d = b.getLongitudeE6();
        D.e = b.getLatitudeE6();
        this.f.a(D, 300);
        C0005e c0005e = this.f;
        C0005e.k = System.currentTimeMillis();
        return true;
    }

    public boolean onDoubleTapEvent(MotionEvent motionEvent) {
        return false;
    }

    public boolean onDown(MotionEvent motionEvent) {
        return false;
    }

    public boolean onFling(MotionEvent motionEvent, MotionEvent motionEvent2, float f, float f2) {
        if (this.f == null || this.f.g == null || !this.f.i) {
            return true;
        }
        if (!this.f.d) {
            return false;
        }
        float sqrt = (float) Math.sqrt((double) ((f * f) + (f2 * f2)));
        if (sqrt <= 500.0f) {
            return false;
        }
        this.f.z();
        this.f.a(34, (int) (sqrt * 0.6f), (((int) motionEvent2.getY()) << 16) | ((int) motionEvent2.getX()));
        this.f.L();
        return true;
    }

    public void onLongPress(MotionEvent motionEvent) {
        if (this.f != null && this.f.g != null && this.f.i) {
            String a = this.f.g.a(-1, (int) motionEvent.getX(), (int) motionEvent.getY(), this.f.j);
            if (a == null || a.equals("")) {
                for (l c : this.f.f) {
                    c.c(this.f.b((int) motionEvent.getX(), (int) motionEvent.getY()));
                }
                return;
            }
            for (l c2 : this.f.f) {
                if (c2.b(a)) {
                    this.f.n = true;
                } else {
                    c2.c(this.f.b((int) motionEvent.getX(), (int) motionEvent.getY()));
                }
            }
        }
    }

    public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent2, float f, float f2) {
        return false;
    }

    public void onShowPress(MotionEvent motionEvent) {
    }

    public boolean onSingleTapConfirmed(MotionEvent motionEvent) {
        JSONObject jSONObject;
        JSONException e;
        if (!(this.f == null || this.f.g == null || !this.f.i)) {
            String a = this.f.g.a(-1, (int) motionEvent.getX(), (int) motionEvent.getY(), this.f.j);
            if (a == null || a.equals("")) {
                for (l a2 : this.f.f) {
                    a2.a(this.f.b((int) motionEvent.getX(), (int) motionEvent.getY()));
                }
            } else {
                JSONObject jSONObject2;
                try {
                    jSONObject2 = new JSONObject(a);
                    try {
                        jSONObject2.put("px", (int) motionEvent.getX());
                        jSONObject2.put("py", (int) motionEvent.getY());
                        jSONObject = jSONObject2;
                    } catch (JSONException e2) {
                        e = e2;
                        e.printStackTrace();
                        jSONObject = jSONObject2;
                        for (l a22 : this.f.f) {
                            if (jSONObject == null) {
                                a22.a(jSONObject.toString());
                            }
                        }
                        return true;
                    }
                } catch (JSONException e3) {
                    JSONException jSONException = e3;
                    jSONObject2 = null;
                    e = jSONException;
                    e.printStackTrace();
                    jSONObject = jSONObject2;
                    for (l a222 : this.f.f) {
                        if (jSONObject == null) {
                            a222.a(jSONObject.toString());
                        }
                    }
                    return true;
                }
                for (l a2222 : this.f.f) {
                    if (jSONObject == null) {
                        a2222.a(jSONObject.toString());
                    }
                }
            }
        }
        return true;
    }

    public boolean onSingleTapUp(MotionEvent motionEvent) {
        return false;
    }

    public void onSurfaceTextureAvailable(SurfaceTexture surfaceTexture, int i, int i2) {
        if (this.f != null) {
            this.e = new C0012m(surfaceTexture, this, new AtomicBoolean(true), this);
            this.e.start();
            a = i;
            b = i2;
            E D = this.f.D();
            if (D != null) {
                if (D.f == 0 || D.f == -1 || D.f == (D.j.left - D.j.right) / 2) {
                    D.f = -1;
                }
                if (D.g == 0 || D.g == -1 || D.g == (D.j.bottom - D.j.top) / 2) {
                    D.g = -1;
                }
                D.j.left = 0;
                D.j.top = 0;
                D.j.bottom = i2;
                D.j.right = i;
                this.f.a(D);
                this.f.a(a, b);
            }
        }
    }

    public boolean onSurfaceTextureDestroyed(SurfaceTexture surfaceTexture) {
        if (this.e != null) {
            this.e.c();
            this.e = null;
        }
        return true;
    }

    public void onSurfaceTextureSizeChanged(SurfaceTexture surfaceTexture, int i, int i2) {
        if (this.f != null) {
            a = i;
            b = i2;
            this.f.a(a, b);
            MapRenderer.nativeResize(this.f.h, i, i2);
        }
    }

    public void onSurfaceTextureUpdated(SurfaceTexture surfaceTexture) {
    }

    public boolean onTouchEvent(MotionEvent motionEvent) {
        if (this.f == null || this.f.g == null) {
            return true;
        }
        super.onTouchEvent(motionEvent);
        for (l a : this.f.f) {
            a.a(motionEvent);
        }
        return this.c.onTouchEvent(motionEvent) ? true : this.f.a(motionEvent);
    }
}
