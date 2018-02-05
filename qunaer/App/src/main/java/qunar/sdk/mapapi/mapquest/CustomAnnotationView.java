package qunar.sdk.mapapi.mapquest;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.text.TextUtils.TruncateAt;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.ScaleAnimation;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.mapquest.android.maps.GeoPoint;
import com.mapquest.android.maps.MapView;
import com.mapquest.android.maps.MapView.LayoutParams;
import com.mapquest.android.maps.OverlayItem;

public class CustomAnnotationView extends RelativeLayout {
    private int DEFAULT_POINTER_HEIGHT = 8;
    private int DEFAULT_POINTER_WIDTH = 22;
    private boolean animated = true;
    private ScaleAnimation animation;
    private int backgroundColor;
    private int bubbleRadius = 8;
    private Context context;
    private int customPointerHeight = 0;
    private int customPointerWidth = 0;
    private float density;
    private GeoPoint geoPoint;
    private RelativeLayout innerView;
    private int innerViewDefaultPaddingBottom = 5;
    private int innerViewDefaultPaddingLeft = 8;
    private int innerViewDefaultPaddingRight = 8;
    private int innerViewDefaultPaddingTop = 5;
    private boolean interceptTouch = false;
    private LayoutParams layoutParams;
    private MapView mapView;
    private int markerHeight = 0;
    private OverlayItem parentItem;
    private int pointerHeight = 0;
    private int pointerWidth = 0;
    private TextView snippet;
    private TextView title;
    private boolean tryToKeepBubbleOnScreen = true;

    public void setInterceptTouch(boolean z) {
        this.interceptTouch = z;
    }

    public CustomAnnotationView(MapView mapView) {
        super(mapView.getContext());
        this.context = mapView.getContext();
        this.mapView = mapView;
        init();
    }

    private void init() {
        setVisibility(8);
        this.density = getResources().getDisplayMetrics().density;
        this.bubbleRadius = (int) ((((float) this.bubbleRadius) * this.density) + 0.5f);
        int i = this.customPointerWidth > 0 ? this.customPointerWidth : this.DEFAULT_POINTER_WIDTH;
        int i2 = this.customPointerHeight > 0 ? this.customPointerHeight : this.DEFAULT_POINTER_HEIGHT;
        this.pointerWidth = (int) ((((float) i) * this.density) + 0.5f);
        this.pointerHeight = (int) ((((float) i2) * this.density) + 0.5f);
        this.innerViewDefaultPaddingLeft = (int) ((((float) this.innerViewDefaultPaddingLeft) * this.density) + 0.5f);
        this.innerViewDefaultPaddingTop = (int) ((((float) this.innerViewDefaultPaddingTop) * this.density) + 0.5f);
        this.innerViewDefaultPaddingRight = (int) ((((float) this.innerViewDefaultPaddingRight) * this.density) + 0.5f);
        this.innerViewDefaultPaddingBottom = (int) ((((float) this.innerViewDefaultPaddingBottom) * this.density) + 0.5f);
        this.backgroundColor = Color.parseColor("#E6434343");
        this.layoutParams = new LayoutParams(-2, -2, new GeoPoint(0, 0), 33);
        if (this.innerView == null) {
            createDefaultInnerView();
        }
        this.animated = true;
        this.animation = new ScaleAnimation((float) this.markerHeight, 1.0f, 0.0f, 1.0f, 1, 0.5f, 1, 1.0f);
        this.animation.setDuration(100);
        this.animation.setInterpolator(new DecelerateInterpolator());
        this.innerView.setLayoutParams(new RelativeLayout.LayoutParams(-2, -2));
        setLayoutParams(this.layoutParams);
        this.mapView.addView(this);
    }

    private void createDefaultInnerView() {
        this.innerView = new RelativeLayout(this.context);
        this.innerView.setPadding(this.innerViewDefaultPaddingLeft, this.innerViewDefaultPaddingTop, this.innerViewDefaultPaddingRight, this.innerViewDefaultPaddingBottom);
        View relativeLayout = new RelativeLayout(this.context);
        relativeLayout.setId("AnnotationViewTextHolderView".hashCode());
        ViewGroup.LayoutParams layoutParams = new RelativeLayout.LayoutParams(-2, -2);
        this.title = new TextView(this.context);
        this.title.setId("AnnotationViewTitle".hashCode());
        this.title.setTextColor(-1);
        this.title.setTypeface(Typeface.DEFAULT_BOLD);
        this.title.setTextSize((float) ((int) ((14.0f * this.density) + 0.5f)));
        this.title.setMaxEms(10);
        this.title.setSingleLine(true);
        this.title.setEllipsize(TruncateAt.END);
        this.title.setIncludeFontPadding(false);
        ViewGroup.LayoutParams layoutParams2 = new RelativeLayout.LayoutParams(-2, -2);
        layoutParams2.addRule(6);
        layoutParams2.addRule(5);
        relativeLayout.addView(this.title, layoutParams2);
        this.snippet = new TextView(this.context);
        this.snippet.setId("AnnotationViewSnippet".hashCode());
        this.snippet.setTextColor(-1);
        this.snippet.setTextSize((float) ((int) ((10.0f * this.density) + 0.5f)));
        this.snippet.setMaxEms(15);
        this.snippet.setSingleLine(true);
        this.snippet.setEllipsize(TruncateAt.END);
        this.snippet.setIncludeFontPadding(false);
        layoutParams2 = new RelativeLayout.LayoutParams(-2, -2);
        layoutParams2.addRule(3, this.title.getId());
        layoutParams2.addRule(5);
        relativeLayout.addView(this.snippet, layoutParams2);
        this.innerView.addView(relativeLayout, layoutParams);
        setPadding(0, 0, 0, this.pointerHeight);
        addView(this.innerView);
    }

    public void showAnnotationView(OverlayItem overlayItem) {
        this.parentItem = overlayItem;
        this.geoPoint = overlayItem.getPoint();
        setLayoutParams(overlayItem);
        this.title.setText(overlayItem.getTitle());
        this.snippet.setText(overlayItem.getSnippet());
        this.markerHeight = this.parentItem.getMarker(this.parentItem.getState()).getIntrinsicHeight();
        setPadding(0, 0, 0, this.markerHeight);
        show();
    }

    public void showAnnotationView(GeoPoint geoPoint, int i) {
        this.geoPoint = geoPoint;
        setLayoutParams();
        this.title.setText("");
        this.snippet.setText("");
        this.markerHeight = i;
        setPadding(0, 0, 0, this.markerHeight);
        show();
    }

    private void setLayoutParams() {
        this.layoutParams = new LayoutParams(-2, -2, this.geoPoint, 33);
        setLayoutParams(this.layoutParams);
    }

    private void setLayoutParams(OverlayItem overlayItem) {
        this.layoutParams = new LayoutParams(-2, -2, this.geoPoint, 33);
        setLayoutParams(this.layoutParams);
    }

    public void show() {
        if (getVisibility() != 0) {
            setVisibility(0);
            bringToFront();
            if (this.animated) {
                startAnimation(this.animation);
            }
        }
    }

    public void hide() {
        setVisibility(8);
    }

    public boolean onInterceptTouchEvent(MotionEvent motionEvent) {
        return this.interceptTouch;
    }

    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
    }

    private void drawPointer(Canvas canvas) {
        int measuredWidth = getMeasuredWidth() / 2;
        int i = measuredWidth - (this.pointerWidth / 2);
        int i2 = (this.pointerWidth / 2) + measuredWidth;
        int measuredHeight = (getMeasuredHeight() - this.pointerHeight) - this.markerHeight;
        int measuredHeight2 = getMeasuredHeight() - this.markerHeight;
        new RectF().set((float) i, (float) measuredHeight, (float) i2, (float) measuredHeight2);
        Path path = new Path();
        path.moveTo((float) i, (float) measuredHeight);
        path.lineTo((float) measuredWidth, (float) measuredHeight2);
        path.lineTo((float) i2, (float) measuredHeight);
        path.close();
        Paint paint = new Paint();
        paint.setColor(this.backgroundColor);
        paint.setStyle(Style.FILL);
        paint.setAntiAlias(true);
        canvas.drawPath(path, paint);
    }

    protected void onMeasure(int i, int i2) {
        super.onMeasure(this.innerView.getMeasuredWidth(), this.innerView.getMeasuredHeight());
    }

    private int getXOffsetToKeepBubbleOnScreen() {
        if (!this.tryToKeepBubbleOnScreen) {
            return 0;
        }
        int left = getLeft();
        int measuredWidth = getMeasuredWidth() - (this.bubbleRadius * 2);
        if (left >= 0) {
            return 0;
        }
        if (Math.abs(left) < measuredWidth) {
            return getLeft() * -1;
        }
        return measuredWidth;
    }

    protected void onLayout(boolean z, int i, int i2, int i3, int i4) {
        super.onLayout(z, i, i2, i3, i4);
    }

    public boolean isTryToKeepBubbleOnScreen() {
        return this.tryToKeepBubbleOnScreen;
    }

    public void tryToKeepBubbleOnScreen(boolean z) {
        this.tryToKeepBubbleOnScreen = z;
    }

    public void setAnimated(boolean z) {
        this.animated = z;
    }

    public RelativeLayout getInnerView() {
        return this.innerView;
    }

    public void setInnerView(RelativeLayout relativeLayout) {
        this.innerViewDefaultPaddingLeft = relativeLayout.getPaddingLeft();
        this.innerViewDefaultPaddingTop = relativeLayout.getPaddingTop();
        this.innerViewDefaultPaddingRight = relativeLayout.getPaddingRight();
        this.innerViewDefaultPaddingBottom = relativeLayout.getPaddingBottom();
        removeView(this.innerView);
        this.innerView = relativeLayout;
        addView(this.innerView);
    }

    public TextView getTitle() {
        return this.title;
    }

    public void setTitle(TextView textView) {
        this.title = textView;
    }

    public void setTitle(String str) {
        this.title.setText(str);
    }

    public TextView getSnippet() {
        return this.snippet;
    }

    public void setSnippet(TextView textView) {
        this.snippet = textView;
    }

    public void setSnippet(String str) {
        this.title.setText(str);
    }

    public void setBubbleRadius(int i) {
        this.bubbleRadius = i;
    }

    public void setBackgroundColor(int i) {
        this.backgroundColor = i;
    }

    public GeoPoint getGeoPoint() {
        return this.geoPoint;
    }

    public void setGeoPoint(GeoPoint geoPoint) {
        this.geoPoint = geoPoint;
    }

    public OverlayItem getOverlayItem() {
        return this.parentItem;
    }

    public void setCustomPointerSize(int i, int i2) {
        this.customPointerWidth = i;
        this.customPointerHeight = i2;
        this.pointerWidth = (int) ((((float) this.customPointerWidth) * this.density) + 0.5f);
        this.pointerHeight = (int) ((((float) this.customPointerHeight) * this.density) + 0.5f);
    }
}
