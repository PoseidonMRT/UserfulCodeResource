package com.baidu.mapapi.map;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.NinePatch;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.NinePatchDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.View.OnApplyWindowInsetsListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowInsets;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.BMapManager;
import com.baidu.mapapi.common.SysOSUtil;
import com.baidu.mapapi.map.MapViewLayoutParams.ELayoutMode;
import com.baidu.mapapi.model.CoordUtil;
import com.baidu.platform.comapi.map.C0009i;
import com.baidu.platform.comapi.map.C0010j;
import com.baidu.platform.comapi.map.N;
import com.baidu.platform.comapi.map.l;
import com.mqunar.libtask.ProgressType;
import java.io.File;
import java.lang.ref.WeakReference;
import java.util.Timer;
import java.util.TimerTask;

@TargetApi(20)
public class WearMapView extends ViewGroup implements OnApplyWindowInsetsListener {
    public static final int BT_INVIEW = 1;
    private static final String b = MapView.class.getSimpleName();
    private static String c;
    private static int q = 0;
    private static int r = 0;
    private static int s = 10;
    private static final SparseArray<Integer> u = new SparseArray();
    private int A;
    private int B;
    private int C;
    private int D;
    private int E;
    a a = a.ROUND;
    private C0010j d;
    private BaiduMap e;
    private ImageView f;
    private Bitmap g;
    private N h;
    private boolean i = true;
    private Point j;
    private Point k;
    private RelativeLayout l;
    private SwipeDismissView m;
    public AnimationTask mTask;
    public Timer mTimer;
    public b mTimerHandler;
    private TextView n;
    private TextView o;
    private ImageView p;
    private boolean t = true;
    private boolean v = true;
    private boolean w = true;
    private float x;
    private l y;
    private int z;

    public class AnimationTask extends TimerTask {
        final /* synthetic */ WearMapView a;

        public AnimationTask(WearMapView wearMapView) {
            this.a = wearMapView;
        }

        public void run() {
            Message message = new Message();
            message.what = 1;
            this.a.mTimerHandler.sendMessage(message);
        }
    }

    public interface OnDismissCallback {
        void onDismiss();

        void onNotify();
    }

    enum a {
        ROUND,
        RECTANGLE,
        UNDETECTED
    }

    class b extends Handler {
        final /* synthetic */ WearMapView a;
        private final WeakReference<Context> b;

        public b(WearMapView wearMapView, Context context) {
            this.a = wearMapView;
            this.b = new WeakReference(context);
        }

        public void handleMessage(Message message) {
            if (((Context) this.b.get()) != null) {
                super.handleMessage(message);
                switch (message.what) {
                    case 1:
                        if (this.a.h != null) {
                            this.a.a(true);
                            return;
                        }
                        return;
                    default:
                        return;
                }
            }
        }
    }

    static {
        u.append(3, Integer.valueOf(2000000));
        u.append(4, Integer.valueOf(1000000));
        u.append(5, Integer.valueOf(500000));
        u.append(6, Integer.valueOf(200000));
        u.append(7, Integer.valueOf(100000));
        u.append(8, Integer.valueOf(50000));
        u.append(9, Integer.valueOf(25000));
        u.append(10, Integer.valueOf(20000));
        u.append(11, Integer.valueOf(LocationClientOption.MIN_AUTO_NOTIFY_INTERVAL));
        u.append(12, Integer.valueOf(5000));
        u.append(13, Integer.valueOf(2000));
        u.append(14, Integer.valueOf(1000));
        u.append(15, Integer.valueOf(500));
        u.append(16, Integer.valueOf(200));
        u.append(17, Integer.valueOf(100));
        u.append(18, Integer.valueOf(50));
        u.append(19, Integer.valueOf(20));
        u.append(20, Integer.valueOf(10));
        u.append(21, Integer.valueOf(5));
        u.append(22, Integer.valueOf(2));
    }

    public WearMapView(Context context) {
        super(context);
        a(context, null);
    }

    public WearMapView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        a(context, null);
    }

    public WearMapView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        a(context, null);
    }

    public WearMapView(Context context, BaiduMapOptions baiduMapOptions) {
        super(context);
        a(context, baiduMapOptions);
    }

    private int a(int i, int i2) {
        return i - ((int) Math.sqrt(Math.pow((double) i, 2.0d) - Math.pow((double) i2, 2.0d)));
    }

    private void a(int i) {
        if (this.d != null) {
            switch (i) {
                case 0:
                    this.d.onPause();
                    b();
                    return;
                case 1:
                    this.d.onResume();
                    c();
                    return;
                default:
                    return;
            }
        }
    }

    private static void a(Context context) {
        Display defaultDisplay = ((WindowManager) context.getSystemService("window")).getDefaultDisplay();
        Point point = new Point();
        defaultDisplay.getSize(point);
        q = point.x;
        r = point.y;
    }

    private void a(Context context, BaiduMapOptions baiduMapOptions) {
        a(context);
        setOnApplyWindowInsetsListener(this);
        this.mTimerHandler = new b(this, context);
        this.mTimer = new Timer();
        if (!(this.mTimer == null || this.mTask == null)) {
            this.mTask.cancel();
        }
        this.mTask = new AnimationTask(this);
        this.mTimer.schedule(this.mTask, 5000);
        C0009i.a();
        BMapManager.init();
        a(context, baiduMapOptions, c);
        this.e = new BaiduMap(this.d);
        this.d.a().p(false);
        this.d.a().o(false);
        c(context);
        d(context);
        b(context);
        if (!(baiduMapOptions == null || baiduMapOptions.h)) {
            this.h.setVisibility(4);
        }
        e(context);
        if (!(baiduMapOptions == null || baiduMapOptions.i)) {
            this.l.setVisibility(4);
        }
        if (!(baiduMapOptions == null || baiduMapOptions.l == null)) {
            this.k = baiduMapOptions.l;
        }
        if (baiduMapOptions != null && baiduMapOptions.k != null) {
            this.j = baiduMapOptions.k;
        }
    }

    private void a(Context context, BaiduMapOptions baiduMapOptions, String str) {
        if (baiduMapOptions == null) {
            this.d = new C0010j(context, null, str);
        } else {
            this.d = new C0010j(context, baiduMapOptions.a(), str);
        }
        addView(this.d);
        this.y = new u(this);
        this.d.a().a(this.y);
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

    private void a(View view, boolean z) {
        if (z) {
            AnimatorSet animatorSet = new AnimatorSet();
            ObjectAnimator ofFloat = ObjectAnimator.ofFloat(view, "TranslationY", new float[]{0.0f, -50.0f});
            ObjectAnimator ofFloat2 = ObjectAnimator.ofFloat(view, "alpha", new float[]{1.0f, 0.0f});
            animatorSet.playTogether(new Animator[]{ofFloat, ofFloat2});
            animatorSet.addListener(new x(this, view));
            animatorSet.setDuration(1200);
            animatorSet.start();
            return;
        }
        view.setVisibility(0);
        animatorSet = new AnimatorSet();
        ofFloat = ObjectAnimator.ofFloat(view, "TranslationY", new float[]{-50.0f, 0.0f});
        ofFloat2 = ObjectAnimator.ofFloat(view, "alpha", new float[]{0.0f, 1.0f});
        animatorSet.playTogether(new Animator[]{ofFloat, ofFloat2});
        animatorSet.setDuration(1200);
        animatorSet.start();
    }

    private void a(boolean z) {
        if (this.i) {
            a(this.h, z);
        }
    }

    private void b() {
        if (this.d != null && !this.t) {
            d();
            this.t = true;
        }
    }

    private void b(Context context) {
        this.m = new SwipeDismissView(context, this);
        LayoutParams layoutParams = new LayoutParams((int) ((context.getResources().getDisplayMetrics().density * 34.0f) + 0.5f), r);
        this.m.setBackgroundColor(Color.argb(0, 0, 0, 0));
        this.m.setLayoutParams(layoutParams);
        addView(this.m);
    }

    private void c() {
        if (this.d != null && this.t) {
            e();
            this.t = false;
        }
    }

    private void c(Context context) {
        String str = "logo_h.png";
        int densityDpi = SysOSUtil.getDensityDpi();
        if (densityDpi < 180) {
            str = "logo_l.png";
        }
        Bitmap a = com.baidu.platform.comapi.commonutils.a.a(str, context);
        Matrix matrix;
        if (densityDpi > 480) {
            matrix = new Matrix();
            matrix.postScale(2.0f, 2.0f);
            this.g = Bitmap.createBitmap(a, 0, 0, a.getWidth(), a.getHeight(), matrix, true);
        } else if (densityDpi <= 320 || densityDpi > 480) {
            this.g = a;
        } else {
            matrix = new Matrix();
            matrix.postScale(1.5f, 1.5f);
            this.g = Bitmap.createBitmap(a, 0, 0, a.getWidth(), a.getHeight(), matrix, true);
        }
        if (this.g != null) {
            this.f = new ImageView(context);
            this.f.setImageBitmap(this.g);
            addView(this.f);
        }
    }

    private void d() {
        if (this.d != null) {
            this.d.c();
        }
    }

    private void d(Context context) {
        this.h = new N(context, true);
        if (this.h.a()) {
            this.h.b(new v(this));
            this.h.a(new w(this));
            addView(this.h);
        }
    }

    private void e() {
        if (this.d != null) {
            this.d.d();
        }
    }

    private void e(Context context) {
        this.l = new RelativeLayout(context);
        this.l.setLayoutParams(new LayoutParams(-2, -2));
        this.n = new TextView(context);
        LayoutParams layoutParams = new RelativeLayout.LayoutParams(-2, -2);
        layoutParams.addRule(14);
        this.n.setTextColor(Color.parseColor("#FFFFFF"));
        this.n.setTextSize(2, 11.0f);
        this.n.setTypeface(this.n.getTypeface(), 1);
        this.n.setLayoutParams(layoutParams);
        this.n.setId(ProgressType.PRO_END);
        this.l.addView(this.n);
        this.o = new TextView(context);
        layoutParams = new RelativeLayout.LayoutParams(-2, -2);
        layoutParams.width = -2;
        layoutParams.height = -2;
        layoutParams.addRule(14);
        this.o.setTextColor(Color.parseColor("#000000"));
        this.o.setTextSize(2, 11.0f);
        this.o.setLayoutParams(layoutParams);
        this.l.addView(this.o);
        this.p = new ImageView(context);
        layoutParams = new RelativeLayout.LayoutParams(-2, -2);
        layoutParams.width = -2;
        layoutParams.height = -2;
        layoutParams.addRule(14);
        layoutParams.addRule(3, this.n.getId());
        this.p.setLayoutParams(layoutParams);
        Bitmap a = com.baidu.platform.comapi.commonutils.a.a("icon_scale.9.png", context);
        byte[] ninePatchChunk = a.getNinePatchChunk();
        NinePatch.isNinePatchChunk(ninePatchChunk);
        this.p.setBackgroundDrawable(new NinePatchDrawable(a, ninePatchChunk, new Rect(), null));
        this.l.addView(this.p);
        addView(this.l);
    }

    public static void setCustomMapStylePath(String str) {
        if (str == null || str.length() == 0) {
            throw new RuntimeException("customMapStylePath String is illegal");
        } else if (new File(str).exists()) {
            c = str;
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

    public final BaiduMap getMap() {
        this.e.c = this;
        return this.e;
    }

    public final int getMapLevel() {
        return ((Integer) u.get((int) this.d.a().D().a)).intValue();
    }

    public int getScaleControlViewHeight() {
        return this.D;
    }

    public int getScaleControlViewWidth() {
        return this.E;
    }

    public WindowInsets onApplyWindowInsets(View view, WindowInsets windowInsets) {
        if (windowInsets.isRound()) {
            this.a = a.ROUND;
        } else {
            this.a = a.RECTANGLE;
        }
        return windowInsets;
    }

    public void onCreate(Context context, Bundle bundle) {
        if (bundle != null) {
            c = bundle.getString("customMapPath");
            if (bundle == null) {
                a(context, new BaiduMapOptions());
                return;
            }
            MapStatus mapStatus = (MapStatus) bundle.getParcelable("mapstatus");
            if (this.j != null) {
                this.j = (Point) bundle.getParcelable("scalePosition");
            }
            if (this.k != null) {
                this.k = (Point) bundle.getParcelable("zoomPosition");
            }
            this.v = bundle.getBoolean("mZoomControlEnabled");
            this.w = bundle.getBoolean("mScaleControlEnabled");
            setPadding(bundle.getInt("paddingLeft"), bundle.getInt("paddingTop"), bundle.getInt("paddingRight"), bundle.getInt("paddingBottom"));
            a(context, new BaiduMapOptions().mapStatus(mapStatus));
        }
    }

    public final void onDestroy() {
        this.d.b();
        if (!(this.g == null || this.g.isRecycled())) {
            this.g.recycle();
            this.g = null;
        }
        this.h.b();
        BMapManager.destroy();
        C0009i.b();
        if (this.mTask != null) {
            this.mTask.cancel();
        }
    }

    public final void onDismiss() {
        removeAllViews();
    }

    public final void onEnterAmbient(Bundle bundle) {
        a(0);
    }

    public void onExitAmbient() {
        a(1);
    }

    public boolean onInterceptTouchEvent(MotionEvent motionEvent) {
        switch (motionEvent.getAction()) {
            case 0:
                if (this.h.getVisibility() != 0) {
                    if (this.h.getVisibility() == 4) {
                        if (this.mTimer != null) {
                            if (this.mTask != null) {
                                this.mTask.cancel();
                            }
                            this.mTimer.cancel();
                            this.mTask = null;
                            this.mTimer = null;
                        }
                        a(false);
                        break;
                    }
                } else if (this.mTimer != null) {
                    if (this.mTask != null) {
                        this.mTimer.cancel();
                        this.mTask.cancel();
                    }
                    this.mTimer = null;
                    this.mTask = null;
                    break;
                }
                break;
            case 1:
                this.mTimer = new Timer();
                if (!(this.mTimer == null || this.mTask == null)) {
                    this.mTask.cancel();
                }
                this.mTask = new AnimationTask(this);
                this.mTimer.schedule(this.mTask, 5000);
                break;
        }
        return super.onInterceptTouchEvent(motionEvent);
    }

    @TargetApi(20)
    protected final void onLayout(boolean z, int i, int i2, int i3, int i4) {
        float f;
        float f2;
        int childCount = getChildCount();
        a(this.f);
        if (((getWidth() - this.z) - this.A) - this.f.getMeasuredWidth() <= 0 || ((getHeight() - this.B) - this.C) - this.f.getMeasuredHeight() <= 0) {
            this.z = 0;
            this.A = 0;
            this.C = 0;
            this.B = 0;
            f = 1.0f;
            f2 = 1.0f;
        } else {
            f = ((float) ((getWidth() - this.z) - this.A)) / ((float) getWidth());
            f2 = ((float) ((getHeight() - this.B) - this.C)) / ((float) getHeight());
        }
        for (int i5 = 0; i5 < childCount; i5++) {
            View childAt = getChildAt(i5);
            if (childAt == this.d) {
                this.d.layout(0, 0, getWidth(), getHeight());
            } else if (childAt == this.f) {
                r6 = (int) (((float) this.C) + (12.0f * f2));
                r3 = 0;
                r0 = 0;
                if (this.a == a.ROUND) {
                    a(this.h);
                    r3 = q / 2;
                    r0 = a(r3, this.h.getMeasuredWidth() / 2);
                    r3 = ((q / 2) - a(r3, r3 - r0)) + s;
                }
                r0 = (r - r0) - r6;
                r3 = q - r3;
                r7 = r3 - this.f.getMeasuredWidth();
                this.f.layout(r7, r0 - this.f.getMeasuredHeight(), r3, r0);
            } else if (childAt == this.h) {
                if (this.h.a()) {
                    a(this.h);
                    if (this.k == null) {
                        r0 = 0;
                        if (this.a == a.ROUND) {
                            r0 = a(r / 2, this.h.getMeasuredWidth() / 2);
                        }
                        r0 = (int) (((float) r0) + ((12.0f * f2) + ((float) this.B)));
                        r3 = (q - this.h.getMeasuredWidth()) / 2;
                        this.h.layout(r3, r0, this.h.getMeasuredWidth() + r3, this.h.getMeasuredHeight() + r0);
                    } else {
                        this.h.layout(this.k.x, this.k.y, this.k.x + this.h.getMeasuredWidth(), this.k.y + this.h.getMeasuredHeight());
                    }
                }
            } else if (childAt == this.l) {
                r3 = 0;
                r0 = 0;
                if (this.a == a.ROUND) {
                    a(this.h);
                    r3 = q / 2;
                    r0 = a(r3, this.h.getMeasuredWidth() / 2);
                    r3 = ((q / 2) - a(r3, r3 - r0)) + s;
                }
                a(this.l);
                if (this.j == null) {
                    r6 = (int) (((float) this.C) + (12.0f * f2));
                    this.E = this.l.getMeasuredWidth();
                    this.D = this.l.getMeasuredHeight();
                    r3 = (int) (((float) r3) + (((float) this.z) + (5.0f * f)));
                    r0 = (r - r6) - r0;
                    r7 = r0 - this.l.getMeasuredHeight();
                    this.l.layout(r3, r7, this.E + r3, r0);
                } else {
                    this.l.layout(this.j.x, this.j.y, this.j.x + this.l.getMeasuredWidth(), this.j.y + this.l.getMeasuredHeight());
                }
            } else if (childAt == this.m) {
                a(this.m);
                this.m.layout(0, 0, this.m.getMeasuredWidth(), r);
            } else {
                LayoutParams layoutParams = childAt.getLayoutParams();
                if (layoutParams instanceof MapViewLayoutParams) {
                    Point point;
                    MapViewLayoutParams mapViewLayoutParams = (MapViewLayoutParams) layoutParams;
                    if (mapViewLayoutParams.c == ELayoutMode.absoluteMode) {
                        point = mapViewLayoutParams.b;
                    } else {
                        point = this.d.a().a(CoordUtil.ll2mc(mapViewLayoutParams.a));
                    }
                    a(childAt);
                    r7 = childAt.getMeasuredWidth();
                    int measuredHeight = childAt.getMeasuredHeight();
                    float f3 = mapViewLayoutParams.d;
                    int i6 = (int) (((float) point.x) - (f3 * ((float) r7)));
                    r0 = mapViewLayoutParams.f + ((int) (((float) point.y) - (mapViewLayoutParams.e * ((float) measuredHeight))));
                    childAt.layout(i6, r0, i6 + r7, r0 + measuredHeight);
                }
            }
        }
    }

    public void onSaveInstanceState(Bundle bundle) {
        if (bundle != null && this.e != null) {
            bundle.putParcelable("mapstatus", this.e.getMapStatus());
            if (this.j != null) {
                bundle.putParcelable("scalePosition", this.j);
            }
            if (this.k != null) {
                bundle.putParcelable("zoomPosition", this.k);
            }
            bundle.putBoolean("mZoomControlEnabled", this.v);
            bundle.putBoolean("mScaleControlEnabled", this.w);
            bundle.putInt("paddingLeft", this.z);
            bundle.putInt("paddingTop", this.B);
            bundle.putInt("paddingRight", this.A);
            bundle.putInt("paddingBottom", this.C);
            bundle.putString("customMapPath", c);
        }
    }

    public void removeView(View view) {
        if (view != this.f) {
            super.removeView(view);
        }
    }

    public void setOnDismissCallbackListener(OnDismissCallback onDismissCallback) {
        if (this.m != null) {
            this.m.setCallback(onDismissCallback);
        }
    }

    public void setPadding(int i, int i2, int i3, int i4) {
        this.z = i;
        this.B = i2;
        this.A = i3;
        this.C = i4;
    }

    public void setScaleControlPosition(Point point) {
        if (point != null && point.x >= 0 && point.y >= 0 && point.x <= getWidth() && point.y <= getHeight()) {
            this.j = point;
            requestLayout();
        }
    }

    public void setShape(a aVar) {
        this.a = aVar;
    }

    public void setViewAnimitionEnable(boolean z) {
        this.i = z;
    }

    public void setZoomControlsPosition(Point point) {
        if (point != null && point.x >= 0 && point.y >= 0 && point.x <= getWidth() && point.y <= getHeight()) {
            this.k = point;
            requestLayout();
        }
    }

    public void showScaleControl(boolean z) {
        this.l.setVisibility(z ? 0 : 8);
        this.w = z;
    }

    public void showZoomControls(boolean z) {
        if (this.h.a()) {
            this.h.setVisibility(z ? 0 : 8);
            this.v = z;
        }
    }
}
