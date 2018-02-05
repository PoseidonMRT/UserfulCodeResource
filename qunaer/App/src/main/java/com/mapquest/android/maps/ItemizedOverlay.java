package com.mapquest.android.maps;

import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v4.view.ViewCompat;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.ViewConfiguration;
import com.mapquest.android.maps.Overlay.Snappable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Iterator;

public abstract class ItemizedOverlay<T extends OverlayItem> extends Overlay implements Snappable {
    private static final String LOG_TAG = ItemizedOverlay.class.getName();
    private BoundingBox boundingBox;
    Rect bounds;
    private int defaultAlignment;
    private Drawable defaultMarker;
    private boolean drawFocusedItem;
    private int fingerSize;
    private int focusedIndex;
    private GestureDetector gestureDetector;
    private GestureListener gestureListener;
    ArrayList<T> items;
    private int latSpanE6;
    private OnFocusChangeListener listener;
    private int lngSpanE6;
    private int pressedIndex;
    private Integer[] rankIndex;
    private int selectedIndex;
    private TrackballGestureDetector trackballGestureDetector;

    class GestureListener extends SimpleOnGestureListener {
        private MapView mapView;

        private GestureListener() {
        }

        void setMapView(MapView mapView) {
            this.mapView = mapView;
        }

        public boolean onDown(MotionEvent motionEvent) {
            int access$100 = ItemizedOverlay.this.findItem(this.mapView, (int) motionEvent.getX(), (int) motionEvent.getY());
            if (access$100 > -1) {
                ItemizedOverlay.this.press(access$100);
            }
            return super.onDown(motionEvent);
        }

        public boolean onSingleTapUp(MotionEvent motionEvent) {
            return ItemizedOverlay.this.onTap(this.mapView.getProjection().fromPixels((int) motionEvent.getX(), (int) motionEvent.getY()), this.mapView);
        }
    }

    public interface OnFocusChangeListener {
        void onFocusChanged(ItemizedOverlay itemizedOverlay, OverlayItem overlayItem);
    }

    protected abstract T createItem(int i);

    public abstract int size();

    public ItemizedOverlay(Drawable drawable) {
        this.defaultAlignment = 33;
        this.items = new ArrayList();
        this.boundingBox = new BoundingBox();
        this.focusedIndex = -1;
        this.selectedIndex = -1;
        this.pressedIndex = -1;
        this.drawFocusedItem = true;
        this.fingerSize = ViewConfiguration.getTouchSlop();
        this.trackballGestureDetector = new TrackballGestureDetector();
        this.rankIndex = null;
        this.bounds = new Rect();
        this.defaultMarker = drawable;
        this.gestureListener = new GestureListener();
        this.gestureDetector = new GestureDetector(this.gestureListener);
    }

    public ItemizedOverlay(Drawable drawable, int i) {
        this(drawable);
        this.defaultAlignment = i;
        Overlay.setAlignment(drawable, this.defaultAlignment);
    }

    private Drawable getMarker(T t) {
        Drawable marker = t.getMarker(t.getState());
        if (marker != null) {
            return marker;
        }
        marker = this.defaultMarker;
        OverlayItem.setState(marker, t.getState());
        return marker;
    }

    void drawItem(Canvas canvas, T t, Point point, Drawable drawable, boolean z) {
        this.bounds.set(drawable.getBounds());
        this.bounds.offset(point.x, point.y);
        Rect clipBounds = canvas.getClipBounds();
        if (Rect.intersects(clipBounds, clipBounds)) {
            Overlay.drawAt(canvas, drawable, point.x, point.y, z);
        }
    }

    public void draw(Canvas canvas, MapView mapView, boolean z) {
        Projection projection = mapView.getProjection();
        if (BoundingBox.intersect(getBoundingBox(), Util.createBoundingBoxFromRect(canvas.getClipBounds(), mapView))) {
            Point point = new Point();
            for (int size = this.items.size() - 1; size >= 0; size--) {
                if (this.focusedIndex != size) {
                    OverlayItem item = getItem(size);
                    Drawable marker = getMarker(item);
                    if (item.getAlignment() != 0) {
                        Overlay.setAlignment(marker, item.getAlignment());
                    }
                    projection.toPixels(item.getPoint(), point);
                    drawItem(canvas, item, point, marker, z);
                }
            }
            if (this.drawFocusedItem && this.focusedIndex != -1) {
                OverlayItem item2 = getItem(this.focusedIndex);
                projection.toPixels(item2.getPoint(), point);
                drawItem(canvas, getItem(this.focusedIndex), point, getMarker(item2), z);
            }
        }
    }

    public final T getItem(int i) {
        return (OverlayItem) this.items.get(getIndexToDraw(i));
    }

    public void clear() {
        this.focusedIndex = -1;
        this.selectedIndex = -1;
        this.pressedIndex = -1;
        Iterator it = this.items.iterator();
        while (it.hasNext()) {
            getMarker((OverlayItem) it.next()).setCallback(null);
        }
        this.items.clear();
    }

    public GeoPoint getCenter() {
        if (this.items.size() > 0) {
            return getItem(0).getPoint();
        }
        return null;
    }

    public BoundingBox getBoundingBox() {
        return this.boundingBox;
    }

    public void destroy() {
        clear();
    }

    public void remove(T t) {
        if (this.items != null) {
            this.items.remove(t);
            populate();
        }
    }

    protected static Drawable boundCenter(Drawable drawable) {
        return Overlay.setAlignment(drawable, 3);
    }

    protected static Drawable boundCenterBottom(Drawable drawable) {
        return Overlay.setAlignment(drawable, 33);
    }

    public void setDrawFocusedItem(boolean z) {
        this.drawFocusedItem = z;
    }

    public final int getLastFocusedIndex() {
        return this.focusedIndex;
    }

    public T getFocus() {
        return this.focusedIndex < 0 ? null : (OverlayItem) this.items.get(this.focusedIndex);
    }

    public int getLatSpanE6() {
        return this.latSpanE6;
    }

    public int getLonSpanE6() {
        return this.lngSpanE6;
    }

    public T nextFocus(boolean z) {
        int i = this.focusedIndex;
        if (z) {
            this.focusedIndex++;
        } else {
            this.focusedIndex--;
        }
        if (this.focusedIndex >= 0 && this.focusedIndex < this.items.size()) {
            return (OverlayItem) this.items.get(this.focusedIndex);
        }
        this.focusedIndex = i;
        return null;
    }

    protected int getIndexToDraw(int i) {
        return this.rankIndex == null ? i : this.rankIndex[i].intValue();
    }

    protected final void populate() {
        int size = size();
        this.items.clear();
        this.items.ensureCapacity(size);
        int i = -90000000;
        int i2 = 90000000;
        int i3 = -180000000;
        int i4 = 180000000;
        for (int i5 = 0; i5 < size; i5++) {
            OverlayItem createItem = createItem(i5);
            GeoPoint point = createItem.getPoint();
            if (point.getLatitudeE6() > i) {
                i = point.getLatitudeE6();
            }
            if (point.getLatitudeE6() < i2) {
                i2 = point.getLatitudeE6();
            }
            if (point.getLongitudeE6() > i3) {
                i3 = point.getLongitudeE6();
            }
            if (point.getLongitudeE6() < i4) {
                i4 = point.getLongitudeE6();
            }
            createItem.getMarker(0);
            createItem.setState(0);
            this.items.add(createItem);
        }
        this.boundingBox.ul = new GeoPoint(i, i4);
        this.boundingBox.lr = new GeoPoint(i2, i3);
        this.latSpanE6 = i - i2;
        this.lngSpanE6 = i3 - i4;
        this.focusedIndex = -1;
        this.selectedIndex = -1;
        this.pressedIndex = -1;
        this.rankIndex = new Integer[size];
        for (i = 0; i < size; i++) {
            this.rankIndex[i] = Integer.valueOf(i);
        }
        Arrays.sort(this.rankIndex, new Comparator() {
            public int compare(Object obj, Object obj2) {
                return Integer.valueOf(((OverlayItem) ItemizedOverlay.this.items.get(((Integer) obj).intValue())).getPoint().getLatitudeE6()).compareTo(Integer.valueOf(((OverlayItem) ItemizedOverlay.this.items.get(((Integer) obj2).intValue())).getPoint().getLatitudeE6()));
            }
        });
    }

    private void focus(int i) {
        int i2 = this.focusedIndex;
        this.focusedIndex = changeState(this.focusedIndex, i, 4);
        if (this.listener != null && i2 != this.focusedIndex) {
            this.listener.onFocusChanged(this, (OverlayItem) this.items.get(this.focusedIndex));
        }
    }

    private void select(int i) {
        this.selectedIndex = changeState(this.selectedIndex, i, 1);
    }

    private void press(int i) {
        this.pressedIndex = changeState(this.pressedIndex, i, 2);
    }

    private int changeState(int i, int i2, int i3) {
        if (i == i2 || i2 <= -1 || i2 >= this.items.size()) {
            return i;
        }
        OverlayItem item;
        if (i > -1 && i < this.items.size()) {
            item = getItem(i);
            item.setState(removeStateBit(item.getState(), i3));
        }
        item = getItem(i2);
        item.setState(setStateBit(item.getState(), i3));
        return i2;
    }

    private int setStateBit(int i, int i2) {
        return i | i2;
    }

    private int removeStateBit(int i, int i2) {
        return (ViewCompat.MEASURED_SIZE_MASK ^ i2) & i;
    }

    private int findItem(MapView mapView, int i, int i2) {
        Projection projection = mapView.getProjection();
        Point point = new Point();
        if (this.focusedIndex != -1) {
            OverlayItem item = getItem(this.focusedIndex);
            point = projection.toPixels(item.getPoint(), point);
            if (hitTest(item, getMarker(item), i - point.x, i2 - point.y)) {
                return this.focusedIndex;
            }
        }
        int size = this.items.size();
        Point point2 = point;
        for (int i3 = 0; i3 < size; i3++) {
            OverlayItem item2 = getItem(i3);
            point2 = projection.toPixels(item2.getPoint(), point2);
            if (hitTest(item2, getMarker(item2), i - point2.x, i2 - point2.y)) {
                return i3;
            }
        }
        return -1;
    }

    protected boolean hitTest(T t, Drawable drawable, int i, int i2) {
        Rect copyBounds = drawable.copyBounds();
        int i3 = this.fingerSize >> 1;
        copyBounds.inset(-i3, -i3);
        return copyBounds.contains(i, i2);
    }

    protected boolean onTap(int i) {
        return false;
    }

    public boolean onKeyUp(int i, KeyEvent keyEvent, MapView mapView) {
        return ((i == 66 || i == 23) && this.focusedIndex > -1 && this.focusedIndex < this.items.size()) ? onTap(this.focusedIndex) : super.onKeyUp(i, keyEvent, mapView);
    }

    public boolean onSnapToItem(int i, int i2, Point point, MapView mapView) {
        return false;
    }

    public void setFocus(T t) {
        int i = 0;
        while (i < size()) {
            if (getItem(i) == t) {
                break;
            }
            i++;
        }
        i = 0;
        focus(i);
    }

    protected void setLastFocusedIndex(int i) {
        this.focusedIndex = i;
    }

    public boolean onTap(GeoPoint geoPoint, MapView mapView) {
        Point toPixels = mapView.getProjection().toPixels(geoPoint, (Point) null);
        int findItem = findItem(mapView, toPixels.x, toPixels.y);
        if (findItem <= -1) {
            return super.onTap(geoPoint, mapView);
        }
        focus(findItem);
        select(findItem);
        return onTap(findItem);
    }

    public boolean onTouchEvent(MotionEvent motionEvent, MapView mapView) {
        this.gestureListener.setMapView(mapView);
        return this.gestureDetector.onTouchEvent(motionEvent);
    }

    public boolean onTrackballEvent(MotionEvent motionEvent, MapView mapView) {
        this.trackballGestureDetector.analyze(motionEvent);
        if (this.trackballGestureDetector.isScroll()) {
            if (this.trackballGestureDetector.scrollX() < 0.0f) {
                focus(this.focusedIndex - 1);
            } else {
                focus(this.focusedIndex + 1);
            }
        } else if (this.trackballGestureDetector.isTap()) {
            return onTap(this.focusedIndex);
        }
        return super.onTrackballEvent(motionEvent, mapView);
    }

    public void setOnFocusChangeListener(OnFocusChangeListener onFocusChangeListener) {
        this.listener = onFocusChangeListener;
    }
}
