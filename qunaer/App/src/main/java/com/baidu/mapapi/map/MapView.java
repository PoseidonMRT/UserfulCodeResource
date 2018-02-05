package com.baidu.mapapi.map;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.NinePatch;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.NinePatchDrawable;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.util.SparseArray;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.BMapManager;
import com.baidu.mapapi.common.SysOSUtil;
import com.baidu.mapapi.map.MapViewLayoutParams.ELayoutMode;
import com.baidu.mapapi.model.CoordUtil;
import com.baidu.platform.comapi.commonutils.a;
import com.baidu.platform.comapi.map.C0009i;
import com.baidu.platform.comapi.map.C0010j;
import com.baidu.platform.comapi.map.N;
import com.baidu.platform.comapi.map.l;
import com.mqunar.libtask.ProgressType;
import java.io.File;

public final class MapView extends ViewGroup {
    private static final String a = MapView.class.getSimpleName();
    private static String b;
    private static final SparseArray<Integer> n = new SparseArray();
    private C0010j c;
    private BaiduMap d;
    private ImageView e;
    private Bitmap f;
    private N g;
    private Point h;
    private Point i;
    private RelativeLayout j;
    private TextView k;
    private TextView l;
    private ImageView m;
    private int o = LogoPosition.logoPostionleftBottom.ordinal();
    private boolean p = true;
    private boolean q = true;
    private float r;
    private l s;
    private int t;
    private int u;
    private int v;
    private int w;
    private int x;
    private int y;

    static {
        n.append(3, Integer.valueOf(2000000));
        n.append(4, Integer.valueOf(1000000));
        n.append(5, Integer.valueOf(500000));
        n.append(6, Integer.valueOf(200000));
        n.append(7, Integer.valueOf(100000));
        n.append(8, Integer.valueOf(50000));
        n.append(9, Integer.valueOf(25000));
        n.append(10, Integer.valueOf(20000));
        n.append(11, Integer.valueOf(LocationClientOption.MIN_AUTO_NOTIFY_INTERVAL));
        n.append(12, Integer.valueOf(5000));
        n.append(13, Integer.valueOf(2000));
        n.append(14, Integer.valueOf(1000));
        n.append(15, Integer.valueOf(500));
        n.append(16, Integer.valueOf(200));
        n.append(17, Integer.valueOf(100));
        n.append(18, Integer.valueOf(50));
        n.append(19, Integer.valueOf(20));
        n.append(20, Integer.valueOf(10));
        n.append(21, Integer.valueOf(5));
        n.append(22, Integer.valueOf(2));
    }

    public MapView(Context context) {
        super(context);
        a(context, null);
    }

    public MapView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        a(context, null);
    }

    public MapView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        a(context, null);
    }

    public MapView(Context context, BaiduMapOptions baiduMapOptions) {
        super(context);
        a(context, baiduMapOptions);
    }

    private void a(Context context) {
        String str = "logo_h.png";
        int densityDpi = SysOSUtil.getDensityDpi();
        if (densityDpi < 180) {
            str = "logo_l.png";
        }
        Bitmap a = a.a(str, context);
        Matrix matrix;
        if (densityDpi > 480) {
            matrix = new Matrix();
            matrix.postScale(2.0f, 2.0f);
            this.f = Bitmap.createBitmap(a, 0, 0, a.getWidth(), a.getHeight(), matrix, true);
        } else if (densityDpi <= 320 || densityDpi > 480) {
            this.f = a;
        } else {
            matrix = new Matrix();
            matrix.postScale(1.5f, 1.5f);
            this.f = Bitmap.createBitmap(a, 0, 0, a.getWidth(), a.getHeight(), matrix, true);
        }
        if (this.f != null) {
            this.e = new ImageView(context);
            this.e.setImageBitmap(this.f);
            addView(this.e);
        }
    }

    private void a(Context context, BaiduMapOptions baiduMapOptions) {
        C0009i.a();
        BMapManager.init();
        a(context, baiduMapOptions, b);
        this.d = new BaiduMap(this.c);
        a(context);
        b(context);
        if (!(baiduMapOptions == null || baiduMapOptions.h)) {
            this.g.setVisibility(4);
        }
        c(context);
        if (!(baiduMapOptions == null || baiduMapOptions.i)) {
            this.j.setVisibility(4);
        }
        if (!(baiduMapOptions == null || baiduMapOptions.j == null)) {
            this.o = baiduMapOptions.j.ordinal();
        }
        if (!(baiduMapOptions == null || baiduMapOptions.l == null)) {
            this.i = baiduMapOptions.l;
        }
        if (baiduMapOptions != null && baiduMapOptions.k != null) {
            this.h = baiduMapOptions.k;
        }
    }

    private void a(Context context, BaiduMapOptions baiduMapOptions, String str) {
        if (baiduMapOptions == null) {
            this.c = new C0010j(context, null, str);
        } else {
            this.c = new C0010j(context, baiduMapOptions.a(), str);
        }
        addView(this.c);
        this.s = new i(this);
        this.c.a().a(this.s);
    }

    private void a(View view) {
        LayoutParams layoutParams = view.getLayoutParams();
        if (layoutParams == null) {
            layoutParams = new LayoutParams(-2, -2);
        }
        int i = layoutParams.width;
        i = i > 0 ? MeasureSpec.makeMeasureSpec(i, 1073741824) : MeasureSpec.makeMeasureSpec(0, 0);
        int i2 = layoutParams.height;
        view.measure(i, i2 > 0 ? MeasureSpec.makeMeasureSpec(i2, 1073741824) : MeasureSpec.makeMeasureSpec(0, 0));
    }

    private void b() {
        boolean z = false;
        float f = this.c.a().D().a;
        if (this.g.a()) {
            this.g.b(f > this.c.a().b);
            N n = this.g;
            if (f < this.c.a().a) {
                z = true;
            }
            n.a(z);
        }
    }

    private void b(Context context) {
        this.g = new N(context, false);
        if (this.g.a()) {
            this.g.b(new j(this));
            this.g.a(new k(this));
            addView(this.g);
        }
    }

    private void c(Context context) {
        this.j = new RelativeLayout(context);
        this.j.setLayoutParams(new LayoutParams(-2, -2));
        this.k = new TextView(context);
        LayoutParams layoutParams = new RelativeLayout.LayoutParams(-2, -2);
        layoutParams.addRule(14);
        this.k.setTextColor(Color.parseColor("#FFFFFF"));
        this.k.setTextSize(2, 11.0f);
        this.k.setTypeface(this.k.getTypeface(), 1);
        this.k.setLayoutParams(layoutParams);
        this.k.setId(ProgressType.PRO_END);
        this.j.addView(this.k);
        this.l = new TextView(context);
        layoutParams = new RelativeLayout.LayoutParams(-2, -2);
        layoutParams.width = -2;
        layoutParams.height = -2;
        layoutParams.addRule(14);
        this.l.setTextColor(Color.parseColor("#000000"));
        this.l.setTextSize(2, 11.0f);
        this.l.setLayoutParams(layoutParams);
        this.j.addView(this.l);
        this.m = new ImageView(context);
        layoutParams = new RelativeLayout.LayoutParams(-2, -2);
        layoutParams.width = -2;
        layoutParams.height = -2;
        layoutParams.addRule(14);
        layoutParams.addRule(3, this.k.getId());
        this.m.setLayoutParams(layoutParams);
        Bitmap a = a.a("icon_scale.9.png", context);
        byte[] ninePatchChunk = a.getNinePatchChunk();
        NinePatch.isNinePatchChunk(ninePatchChunk);
        this.m.setBackgroundDrawable(new NinePatchDrawable(a, ninePatchChunk, new Rect(), null));
        this.j.addView(this.m);
        addView(this.j);
    }

    public static void setCustomMapStylePath(String str) {
        if (str == null || str.length() == 0) {
            throw new RuntimeException("customMapStylePath String is illegal");
        } else if (new File(str).exists()) {
            b = str;
        } else {
            throw new RuntimeException("please check whether the customMapStylePath file exits");
        }
    }

    public static void setMapCustomEnable(boolean z) {
        C0009i.a(z);
    }

    public void addView(View view, LayoutParams layoutParams) {
        if (layoutParams instanceof MapViewLayoutParams) {
            super.addView(view, layoutParams);
        }
    }

    public final LogoPosition getLogoPosition() {
        switch (this.o) {
            case 1:
                return LogoPosition.logoPostionleftTop;
            case 2:
                return LogoPosition.logoPostionCenterBottom;
            case 3:
                return LogoPosition.logoPostionCenterTop;
            case 4:
                return LogoPosition.logoPostionRightBottom;
            case 5:
                return LogoPosition.logoPostionRightTop;
            default:
                return LogoPosition.logoPostionleftBottom;
        }
    }

    public final BaiduMap getMap() {
        this.d.a = this;
        return this.d;
    }

    public final int getMapLevel() {
        return ((Integer) n.get((int) this.c.a().D().a)).intValue();
    }

    public int getScaleControlViewHeight() {
        return this.x;
    }

    public int getScaleControlViewWidth() {
        return this.y;
    }

    public boolean handleMultiTouch(float f, float f2, float f3, float f4) {
        return this.c != null && this.c.a(f, f2, f3, f4);
    }

    public void handleTouchDown(float f, float f2) {
        if (this.c != null) {
            this.c.a(f, f2);
        }
    }

    public boolean handleTouchMove(float f, float f2) {
        return this.c != null && this.c.c(f, f2);
    }

    public boolean handleTouchUp(float f, float f2) {
        return this.c == null ? false : this.c.b(f, f2);
    }

    public boolean inRangeOfView(float f, float f2) {
        return this.c != null && this.c.d(f, f2);
    }

    public void onCreate(Context context, Bundle bundle) {
        if (bundle != null) {
            b = bundle.getString("customMapPath");
            if (bundle == null) {
                a(context, new BaiduMapOptions());
                return;
            }
            MapStatus mapStatus = (MapStatus) bundle.getParcelable("mapstatus");
            if (this.h != null) {
                this.h = (Point) bundle.getParcelable("scalePosition");
            }
            if (this.i != null) {
                this.i = (Point) bundle.getParcelable("zoomPosition");
            }
            this.p = bundle.getBoolean("mZoomControlEnabled");
            this.q = bundle.getBoolean("mScaleControlEnabled");
            this.o = bundle.getInt("logoPosition");
            setPadding(bundle.getInt("paddingLeft"), bundle.getInt("paddingTop"), bundle.getInt("paddingRight"), bundle.getInt("paddingBottom"));
            a(context, new BaiduMapOptions().mapStatus(mapStatus));
        }
    }

    public final void onDestroy() {
        this.c.b();
        if (!(this.f == null || this.f.isRecycled())) {
            this.f.recycle();
            this.f = null;
        }
        this.g.b();
        BMapManager.destroy();
        C0009i.b();
    }

    protected final void onLayout(boolean z, int i, int i2, int i3, int i4) {
        float f;
        float f2;
        int childCount = getChildCount();
        a(this.e);
        if (((getWidth() - this.t) - this.u) - this.e.getMeasuredWidth() <= 0 || ((getHeight() - this.v) - this.w) - this.e.getMeasuredHeight() <= 0) {
            this.t = 0;
            this.u = 0;
            this.w = 0;
            this.v = 0;
            f = 1.0f;
            f2 = 1.0f;
        } else {
            f = ((float) ((getWidth() - this.t) - this.u)) / ((float) getWidth());
            f2 = ((float) ((getHeight() - this.v) - this.w)) / ((float) getHeight());
        }
        for (int i5 = 0; i5 < childCount; i5++) {
            View childAt = getChildAt(i5);
            if (childAt != null) {
                if (childAt == this.c) {
                    this.c.layout(0, 0, getWidth(), getHeight());
                } else if (childAt == this.e) {
                    r5 = (int) (((float) this.t) + (5.0f * f));
                    r0 = (int) (((float) this.u) + (5.0f * f));
                    r4 = (int) (((float) this.v) + (5.0f * f2));
                    r3 = (int) (((float) this.w) + (5.0f * f2));
                    switch (this.o) {
                        case 1:
                            r3 = r4 + this.e.getMeasuredHeight();
                            r0 = this.e.getMeasuredWidth() + r5;
                            break;
                        case 2:
                            r3 = getHeight() - r3;
                            r4 = r3 - this.e.getMeasuredHeight();
                            r5 = (((getWidth() - this.e.getMeasuredWidth()) + this.t) - this.u) / 2;
                            r0 = (((getWidth() + this.e.getMeasuredWidth()) + this.t) - this.u) / 2;
                            break;
                        case 3:
                            r3 = r4 + this.e.getMeasuredHeight();
                            r5 = (((getWidth() - this.e.getMeasuredWidth()) + this.t) - this.u) / 2;
                            r0 = (((getWidth() + this.e.getMeasuredWidth()) + this.t) - this.u) / 2;
                            break;
                        case 4:
                            r3 = getHeight() - r3;
                            r4 = r3 - this.e.getMeasuredHeight();
                            r0 = getWidth() - r0;
                            r5 = r0 - this.e.getMeasuredWidth();
                            break;
                        case 5:
                            r3 = this.e.getMeasuredHeight() + r4;
                            r0 = getWidth() - r0;
                            r5 = r0 - this.e.getMeasuredWidth();
                            break;
                        default:
                            r3 = getHeight() - r3;
                            r0 = this.e.getMeasuredWidth() + r5;
                            r4 = r3 - this.e.getMeasuredHeight();
                            break;
                    }
                    this.e.layout(r5, r4, r0, r3);
                } else if (childAt == this.g) {
                    if (this.g.a()) {
                        a(this.g);
                        if (this.i == null) {
                            r3 = (int) ((((float) (getHeight() - 15)) * f2) + ((float) this.v));
                            r4 = (int) ((((float) (getWidth() - 15)) * f) + ((float) this.t));
                            r5 = r4 - this.g.getMeasuredWidth();
                            r0 = r3 - this.g.getMeasuredHeight();
                            if (this.o == 4) {
                                r3 -= this.e.getMeasuredHeight();
                                r0 -= this.e.getMeasuredHeight();
                            }
                            this.g.layout(r5, r0, r4, r3);
                        } else {
                            this.g.layout(this.i.x, this.i.y, this.i.x + this.g.getMeasuredWidth(), this.i.y + this.g.getMeasuredHeight());
                        }
                    }
                } else if (childAt == this.j) {
                    a(this.j);
                    if (this.h == null) {
                        r0 = (int) ((((float) this.w) + (5.0f * f2)) + 56.0f);
                        this.y = this.j.getMeasuredWidth();
                        this.x = this.j.getMeasuredHeight();
                        r3 = (int) (((float) this.t) + (5.0f * f));
                        r0 = (getHeight() - r0) - this.e.getMeasuredHeight();
                        this.j.layout(r3, r0, this.y + r3, this.x + r0);
                    } else {
                        this.j.layout(this.h.x, this.h.y, this.h.x + this.j.getMeasuredWidth(), this.h.y + this.j.getMeasuredHeight());
                    }
                } else {
                    LayoutParams layoutParams = childAt.getLayoutParams();
                    if (layoutParams == null) {
                        Log.e("test", "lp == null");
                    }
                    if (layoutParams instanceof MapViewLayoutParams) {
                        Point point;
                        MapViewLayoutParams mapViewLayoutParams = (MapViewLayoutParams) layoutParams;
                        if (mapViewLayoutParams.c == ELayoutMode.absoluteMode) {
                            point = mapViewLayoutParams.b;
                        } else {
                            point = this.c.a().a(CoordUtil.ll2mc(mapViewLayoutParams.a));
                        }
                        a(childAt);
                        r5 = childAt.getMeasuredWidth();
                        int measuredHeight = childAt.getMeasuredHeight();
                        float f3 = mapViewLayoutParams.d;
                        int i6 = (int) (((float) point.x) - (f3 * ((float) r5)));
                        r0 = mapViewLayoutParams.f + ((int) (((float) point.y) - (mapViewLayoutParams.e * ((float) measuredHeight))));
                        childAt.layout(i6, r0, i6 + r5, r0 + measuredHeight);
                    }
                }
            }
        }
    }

    public final void onPause() {
        this.c.onPause();
    }

    public final void onResume() {
        this.c.onResume();
    }

    public void onSaveInstanceState(Bundle bundle) {
        if (bundle != null && this.d != null) {
            bundle.putParcelable("mapstatus", this.d.getMapStatus());
            if (this.h != null) {
                bundle.putParcelable("scalePosition", this.h);
            }
            if (this.i != null) {
                bundle.putParcelable("zoomPosition", this.i);
            }
            bundle.putBoolean("mZoomControlEnabled", this.p);
            bundle.putBoolean("mScaleControlEnabled", this.q);
            bundle.putInt("logoPosition", this.o);
            bundle.putInt("paddingLeft", this.t);
            bundle.putInt("paddingTop", this.v);
            bundle.putInt("paddingRight", this.u);
            bundle.putInt("paddingBottom", this.w);
            bundle.putString("customMapPath", b);
        }
    }

    public void removeView(View view) {
        if (view != this.e) {
            super.removeView(view);
        }
    }

    public final void setLogoPosition(LogoPosition logoPosition) {
        if (logoPosition == null) {
            this.o = LogoPosition.logoPostionleftBottom.ordinal();
        }
        this.o = logoPosition.ordinal();
        requestLayout();
    }

    public void setPadding(int i, int i2, int i3, int i4) {
        this.t = i;
        this.v = i2;
        this.u = i3;
        this.w = i4;
    }

    public void setScaleControlPosition(Point point) {
        if (point != null && point.x >= 0 && point.y >= 0 && point.x <= getWidth() && point.y <= getHeight()) {
            this.h = point;
            requestLayout();
        }
    }

    public void setUpViewEventToMapView(MotionEvent motionEvent) {
        this.c.onTouchEvent(motionEvent);
    }

    public final void setZOrderMediaOverlay(boolean z) {
        if (this.c != null) {
            this.c.setZOrderMediaOverlay(z);
        }
    }

    public void setZoomControlsPosition(Point point) {
        if (point != null && point.x >= 0 && point.y >= 0 && point.x <= getWidth() && point.y <= getHeight()) {
            this.i = point;
            requestLayout();
        }
    }

    public void showScaleControl(boolean z) {
        this.j.setVisibility(z ? 0 : 8);
        this.q = z;
    }

    public void showZoomControls(boolean z) {
        if (this.g.a()) {
            this.g.setVisibility(z ? 0 : 8);
            this.p = z;
        }
    }
}
