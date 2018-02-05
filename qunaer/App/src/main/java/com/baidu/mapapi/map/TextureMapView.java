package com.baidu.mapapi.map;

import android.annotation.SuppressLint;
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
import android.util.SparseArray;
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
import com.baidu.platform.comapi.map.F;
import com.baidu.platform.comapi.map.N;
import com.baidu.platform.comapi.map.l;
import com.mqunar.libtask.ProgressType;
import java.io.File;

public final class TextureMapView extends ViewGroup {
    private static final String a = TextureMapView.class.getSimpleName();
    private static String i;
    private static final SparseArray<Integer> n = new SparseArray();
    private F b;
    private BaiduMap c;
    private ImageView d;
    private Bitmap e;
    private N f;
    private Point g;
    private Point h;
    private RelativeLayout j;
    private TextView k;
    private TextView l;
    private ImageView m;
    private float o;
    private l p;
    private int q = LogoPosition.logoPostionleftBottom.ordinal();
    private boolean r = true;
    private boolean s = true;
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

    public TextureMapView(Context context) {
        super(context);
        a(context, null);
    }

    public TextureMapView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        a(context, null);
    }

    public TextureMapView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        a(context, null);
    }

    public TextureMapView(Context context, BaiduMapOptions baiduMapOptions) {
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
            this.e = Bitmap.createBitmap(a, 0, 0, a.getWidth(), a.getHeight(), matrix, true);
        } else if (densityDpi <= 320 || densityDpi > 480) {
            this.e = a;
        } else {
            matrix = new Matrix();
            matrix.postScale(1.5f, 1.5f);
            this.e = Bitmap.createBitmap(a, 0, 0, a.getWidth(), a.getHeight(), matrix, true);
        }
        if (this.e != null) {
            this.d = new ImageView(context);
            this.d.setImageBitmap(this.e);
            addView(this.d);
        }
    }

    private void a(Context context, BaiduMapOptions baiduMapOptions) {
        setBackgroundColor(-1);
        C0009i.a();
        BMapManager.init();
        a(context, baiduMapOptions, i);
        this.c = new BaiduMap(this.b);
        a(context);
        b(context);
        if (!(baiduMapOptions == null || baiduMapOptions.h)) {
            this.f.setVisibility(4);
        }
        c(context);
        if (!(baiduMapOptions == null || baiduMapOptions.i)) {
            this.j.setVisibility(4);
        }
        if (!(baiduMapOptions == null || baiduMapOptions.j == null)) {
            this.q = baiduMapOptions.j.ordinal();
        }
        if (!(baiduMapOptions == null || baiduMapOptions.l == null)) {
            this.h = baiduMapOptions.l;
        }
        if (baiduMapOptions != null && baiduMapOptions.k != null) {
            this.g = baiduMapOptions.k;
        }
    }

    private void a(Context context, BaiduMapOptions baiduMapOptions, String str) {
        i = str;
        if (baiduMapOptions == null) {
            this.b = new F(context, null, str);
        } else {
            this.b = new F(context, baiduMapOptions.a(), str);
        }
        addView(this.b);
        this.p = new q(this);
        this.b.b().a(this.p);
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
        float f = this.b.b().D().a;
        if (this.f.a()) {
            this.f.b(f > this.b.b().b);
            N n = this.f;
            if (f < this.b.b().a) {
                z = true;
            }
            n.a(z);
        }
    }

    private void b(Context context) {
        this.f = new N(context);
        if (this.f.a()) {
            this.f.b(new r(this));
            this.f.a(new s(this));
            addView(this.f);
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
            i = str;
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
        switch (this.q) {
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
        this.c.b = this;
        return this.c;
    }

    public final int getMapLevel() {
        return ((Integer) n.get((int) this.b.b().D().a)).intValue();
    }

    public int getScaleControlViewHeight() {
        return this.y;
    }

    public int getScaleControlViewWidth() {
        return this.y;
    }

    public void onCreate(Context context, Bundle bundle) {
        if (bundle != null) {
            i = bundle.getString("customMapPath");
            if (bundle == null) {
                a(context, new BaiduMapOptions());
                return;
            }
            MapStatus mapStatus = (MapStatus) bundle.getParcelable("mapstatus");
            if (this.g != null) {
                this.g = (Point) bundle.getParcelable("scalePosition");
            }
            if (this.h != null) {
                this.h = (Point) bundle.getParcelable("zoomPosition");
            }
            this.r = bundle.getBoolean("mZoomControlEnabled");
            this.s = bundle.getBoolean("mScaleControlEnabled");
            this.q = bundle.getInt("logoPosition");
            setPadding(bundle.getInt("paddingLeft"), bundle.getInt("paddingTop"), bundle.getInt("paddingRight"), bundle.getInt("paddingBottom"));
            a(context, new BaiduMapOptions().mapStatus(mapStatus));
        }
    }

    public final void onDestroy() {
        this.b.e();
        if (!(this.e == null || this.e.isRecycled())) {
            this.e.recycle();
        }
        this.f.b();
        BMapManager.destroy();
        C0009i.b();
    }

    @SuppressLint({"NewApi"})
    protected final void onLayout(boolean z, int i, int i2, int i3, int i4) {
        float f;
        float f2;
        int childCount = getChildCount();
        a(this.d);
        if (((getWidth() - this.t) - this.u) - this.d.getMeasuredWidth() <= 0 || ((getHeight() - this.v) - this.w) - this.d.getMeasuredHeight() <= 0) {
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
            if (childAt == this.b) {
                this.b.layout(0, 0, getWidth(), getHeight());
            } else if (childAt == this.d) {
                r5 = (int) (((float) this.t) + (5.0f * f));
                r0 = (int) (((float) this.u) + (5.0f * f));
                r4 = (int) (((float) this.v) + (5.0f * f2));
                r3 = (int) (((float) this.w) + (5.0f * f2));
                switch (this.q) {
                    case 1:
                        r3 = r4 + this.d.getMeasuredHeight();
                        r0 = this.d.getMeasuredWidth() + r5;
                        break;
                    case 2:
                        r3 = getHeight() - r3;
                        r4 = r3 - this.d.getMeasuredHeight();
                        r5 = (((getWidth() - this.d.getMeasuredWidth()) + this.t) - this.u) / 2;
                        r0 = (((getWidth() + this.d.getMeasuredWidth()) + this.t) - this.u) / 2;
                        break;
                    case 3:
                        r3 = r4 + this.d.getMeasuredHeight();
                        r5 = (((getWidth() - this.d.getMeasuredWidth()) + this.t) - this.u) / 2;
                        r0 = (((getWidth() + this.d.getMeasuredWidth()) + this.t) - this.u) / 2;
                        break;
                    case 4:
                        r3 = getHeight() - r3;
                        r4 = r3 - this.d.getMeasuredHeight();
                        r0 = getWidth() - r0;
                        r5 = r0 - this.d.getMeasuredWidth();
                        break;
                    case 5:
                        r3 = this.d.getMeasuredHeight() + r4;
                        r0 = getWidth() - r0;
                        r5 = r0 - this.d.getMeasuredWidth();
                        break;
                    default:
                        r3 = getHeight() - r3;
                        r0 = this.d.getMeasuredWidth() + r5;
                        r4 = r3 - this.d.getMeasuredHeight();
                        break;
                }
                this.d.layout(r5, r4, r0, r3);
            } else if (childAt == this.f) {
                if (this.f.a()) {
                    a(this.f);
                    if (this.h == null) {
                        r3 = (int) ((((float) (getHeight() - 15)) * f2) + ((float) this.v));
                        r4 = (int) ((((float) (getWidth() - 15)) * f) + ((float) this.t));
                        r5 = r4 - this.f.getMeasuredWidth();
                        r0 = r3 - this.f.getMeasuredHeight();
                        if (this.q == 4) {
                            r3 -= this.d.getMeasuredHeight();
                            r0 -= this.d.getMeasuredHeight();
                        }
                        this.f.layout(r5, r0, r4, r3);
                    } else {
                        this.f.layout(this.h.x, this.h.y, this.h.x + this.f.getMeasuredWidth(), this.h.y + this.f.getMeasuredHeight());
                    }
                }
            } else if (childAt == this.j) {
                a(this.j);
                if (this.g == null) {
                    r0 = (int) ((((float) this.w) + (5.0f * f2)) + 56.0f);
                    this.y = this.j.getMeasuredWidth();
                    this.x = this.j.getMeasuredHeight();
                    r3 = (int) (((float) this.t) + (5.0f * f));
                    r0 = (getHeight() - r0) - this.d.getMeasuredHeight();
                    this.j.layout(r3, r0, this.y + r3, this.x + r0);
                } else {
                    this.j.layout(this.g.x, this.g.y, this.g.x + this.j.getMeasuredWidth(), this.g.y + this.j.getMeasuredHeight());
                }
            } else {
                LayoutParams layoutParams = childAt.getLayoutParams();
                if (layoutParams instanceof MapViewLayoutParams) {
                    Point point;
                    MapViewLayoutParams mapViewLayoutParams = (MapViewLayoutParams) layoutParams;
                    if (mapViewLayoutParams.c == ELayoutMode.absoluteMode) {
                        point = mapViewLayoutParams.b;
                    } else {
                        point = this.b.b().a(CoordUtil.ll2mc(mapViewLayoutParams.a));
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

    public final void onPause() {
        this.b.d();
    }

    public final void onResume() {
        this.b.c();
    }

    public void onSaveInstanceState(Bundle bundle) {
        if (bundle != null && this.c != null) {
            bundle.putParcelable("mapstatus", this.c.getMapStatus());
            if (this.g != null) {
                bundle.putParcelable("scalePosition", this.g);
            }
            if (this.h != null) {
                bundle.putParcelable("zoomPosition", this.h);
            }
            bundle.putBoolean("mZoomControlEnabled", this.r);
            bundle.putBoolean("mScaleControlEnabled", this.s);
            bundle.putInt("logoPosition", this.q);
            bundle.putInt("paddingLeft", this.t);
            bundle.putInt("paddingTop", this.v);
            bundle.putInt("paddingRight", this.u);
            bundle.putInt("paddingBottom", this.w);
            bundle.putString("customMapPath", i);
        }
    }

    public void removeView(View view) {
        if (view != this.d) {
            super.removeView(view);
        }
    }

    public final void setLogoPosition(LogoPosition logoPosition) {
        if (logoPosition == null) {
            this.q = LogoPosition.logoPostionleftBottom.ordinal();
        }
        this.q = logoPosition.ordinal();
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
            this.g = point;
            requestLayout();
        }
    }

    public void setZoomControlsPosition(Point point) {
        if (point != null && point.x >= 0 && point.y >= 0 && point.x <= getWidth() && point.y <= getHeight()) {
            this.h = point;
            requestLayout();
        }
    }

    public void showScaleControl(boolean z) {
        this.j.setVisibility(z ? 0 : 8);
        this.s = z;
    }

    public void showZoomControls(boolean z) {
        if (this.f.a()) {
            this.f.setVisibility(z ? 0 : 8);
            this.r = z;
        }
    }
}
