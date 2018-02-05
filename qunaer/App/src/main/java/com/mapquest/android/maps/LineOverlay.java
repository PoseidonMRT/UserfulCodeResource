package com.mapquest.android.maps;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.view.MotionEvent;
import com.mapquest.android.maps.MapView.MapViewEventListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

public class LineOverlay extends Overlay {
    private static final int EPSILON = 9;
    private static final String LOG_TAG = LineOverlay.class.getName();
    private static final int TOUCH_TOLERANCE = 5;
    private boolean DEBUG;
    private BoundingBox boundingBox;
    private List<GeoPoint> data;
    private int epsilon;
    private Paint linePaint;
    private MapViewEventListener listener;
    private Path path;
    private Paint pointPaint;
    private ArrayList<Point> points;
    private boolean showPoints;
    private volatile List<GeoPoint> simplified;
    private SimplifierHandler simplifierHandler;
    private HandlerThread simplifierThread;
    private boolean simplify;

    class EventListener implements MapViewEventListener {
        private EventListener() {
        }

        public void longTouch(MapView mapView, Point point) {
        }

        public void move(MapView mapView) {
        }

        public void moveEnd(MapView mapView) {
        }

        public void moveStart(MapView mapView) {
        }

        public void touch(MapView mapView) {
        }

        public void zoomEnd(MapView mapView) {
            if (LineOverlay.this.simplify && LineOverlay.this.simplifierHandler != null) {
                LineOverlay.this.simplifierHandler.removeMessages(0);
                LineOverlay lineOverlay = LineOverlay.this;
                lineOverlay.getClass();
                mapView.post(new Simplifier(mapView.getProjection()));
            }
        }

        public void zoomStart(MapView mapView) {
        }

        public void mapLoaded(MapView mapView) {
        }
    }

    class Simplifier implements Runnable {
        Projection projection;

        private Simplifier(Projection projection) {
            this.projection = projection;
        }

        public void run() {
            if (LineOverlay.this.simplify) {
                int size = LineOverlay.this.data.size();
                LineOverlay.this.points.ensureCapacity(size);
                int size2 = LineOverlay.this.points.size();
                if (size2 < size) {
                    while (true) {
                        int i = size2 + 1;
                        if (size2 >= size) {
                            break;
                        }
                        LineOverlay.this.points.add(new Point());
                        size2 = i;
                    }
                }
                for (int i2 = 0; i2 < size; i2++) {
                    this.projection.toPixels((GeoPoint) LineOverlay.this.data.get(i2), (Point) LineOverlay.this.points.get(i2));
                }
                LineOverlay.this.simplifierHandler.sendEmptyMessage(0);
            }
        }
    }

    class SimplifierHandler extends Handler {
        static final int SIMPLIFY = 0;
        private MapView mapView;
        Stack<int[]> reuse = new Stack();

        public SimplifierHandler(MapView mapView, Looper looper) {
            super(looper);
            this.mapView = mapView;
        }

        public void handleMessage(Message message) {
            switch (message.what) {
                case 0:
                    List arrayList = new ArrayList();
                    simplify(LineOverlay.this.points, LineOverlay.this.data, arrayList, 0, LineOverlay.this.data.size() - 1);
                    LineOverlay.this.simplified = arrayList;
                    this.mapView.postInvalidate();
                    break;
            }
            super.handleMessage(message);
        }

        private int[] getIndices(int i, int i2) {
            if (this.reuse.isEmpty()) {
                return new int[]{i, i2};
            }
            int[] iArr = (int[]) this.reuse.pop();
            iArr[0] = i;
            iArr[1] = i2;
            return iArr;
        }

        private void simplify(ArrayList<Point> arrayList, List<GeoPoint> list, List<GeoPoint> list2, int i, int i2) {
            Stack stack = new Stack();
            stack.push(getIndices(i, i2));
            Object arrayList2 = new ArrayList();
            arrayList2.add(Integer.valueOf(i));
            arrayList2.add(Integer.valueOf(i2));
            Point point = new Point();
            while (!stack.isEmpty()) {
                int[] iArr = (int[]) stack.pop();
                int i3 = iArr[0];
                int i4 = iArr[1];
                this.reuse.push(iArr);
                if (i3 + 1 < i4) {
                    int i5 = 0;
                    int i6 = 0;
                    Point point2 = (Point) arrayList.get(i3);
                    Point point3 = (Point) arrayList.get(i4);
                    int i7 = i3 + 1;
                    while (i7 < i4) {
                        Point point4 = (Point) arrayList.get(i7);
                        Util.closestPoint(point4, point2, point3, point);
                        int distanceSquared = Util.distanceSquared(point4.x, point4.y, point.x, point.y);
                        if (distanceSquared > i5) {
                            i6 = distanceSquared;
                            distanceSquared = i7;
                        } else {
                            distanceSquared = i6;
                            i6 = i5;
                        }
                        i7++;
                        i5 = i6;
                        i6 = distanceSquared;
                    }
                    if (i5 > LineOverlay.this.epsilon) {
                        arrayList2.add(Integer.valueOf(i6));
                        Object indices = getIndices(i3, i6);
                        Object indices2 = getIndices(i6, i4);
                        stack.push(indices);
                        stack.push(indices2);
                    }
                }
            }
            Collections.sort(arrayList2);
            Iterator it = arrayList2.iterator();
            int i8 = -1;
            while (it.hasNext()) {
                int intValue = ((Integer) it.next()).intValue();
                if (intValue != i8) {
                    list2.add(list.get(intValue));
                } else {
                    intValue = i8;
                }
                i8 = intValue;
            }
        }
    }

    public LineOverlay() {
        this.epsilon = 9;
        this.showPoints = false;
        this.simplify = true;
        this.DEBUG = false;
        this.listener = null;
        this.simplifierThread = null;
        this.simplifierHandler = null;
        this.path = new Path();
    }

    public LineOverlay(Paint paint) {
        this();
        this.linePaint = paint;
    }

    public void setData(List<GeoPoint> list, BoundingBox boundingBox) {
        this.data = list;
        this.boundingBox = boundingBox;
        this.points = new ArrayList(list.size());
        this.simplified = null;
    }

    public void setData(List<GeoPoint> list, boolean z) {
        setData((List) list, z ? BoundingBox.calculateBoundingBoxGeoPoint(list) : this.boundingBox);
    }

    public void setData(List<GeoPoint> list) {
        setData((List) list, true);
    }

    public void setLinePaint(Paint paint) {
        this.linePaint = paint;
    }

    public void setShowPoints(boolean z, Paint paint) {
        this.showPoints = z;
        this.pointPaint = paint;
    }

    public void setPointPaint(Paint paint) {
        this.pointPaint = paint;
    }

    public void setBoundingBox(BoundingBox boundingBox) {
        this.boundingBox = boundingBox;
    }

    public boolean isSimplify() {
        return this.simplify;
    }

    public void setSimplify(boolean z, int i) {
        if (i > -1) {
            this.epsilon = i;
        } else {
            this.epsilon = 9;
        }
        if (this.simplify != z) {
            if (this.simplify) {
                this.points.clear();
                quitSimplifier();
                this.simplified = null;
            }
            this.simplify = z;
        }
    }

    public void draw(Canvas canvas, MapView mapView, boolean z) {
        int i = 0;
        if (this.DEBUG) {
        }
        if (this.listener == null) {
            this.listener = new EventListener();
            mapView.addMapViewEventListener(this.listener);
        }
        Projection projection = mapView.getProjection();
        Rect clipBounds = canvas.getClipBounds();
        Rect createRectFromBoundingBox = Util.createRectFromBoundingBox(this.boundingBox, mapView);
        int strokeWidth = ((int) this.linePaint.getStrokeWidth()) / 2;
        createRectFromBoundingBox.inset(-strokeWidth, -strokeWidth);
        BoundingBox createBoundingBoxFromRect = Util.createBoundingBoxFromRect(clipBounds, mapView);
        List simplify = simplify(mapView, projection, this.data);
        if (Rect.intersects(createRectFromBoundingBox, clipBounds)) {
            GeoPoint geoPoint;
            GeoPoint geoPoint2;
            System.currentTimeMillis();
            this.path.reset();
            int i2 = 0;
            Point point = null;
            while (i2 < simplify.size()) {
                Point point2;
                geoPoint = (GeoPoint) simplify.get(i2);
                if (createBoundingBoxFromRect.contains(geoPoint)) {
                    point = projection.toPixels(geoPoint, point);
                    if (this.showPoints) {
                        if (this.pointPaint == null) {
                            this.pointPaint = createPointPaint();
                        }
                        canvas.drawCircle((float) point.x, (float) point.y, this.pointPaint.getStrokeWidth(), this.pointPaint);
                    }
                    if (simplify.size() > i2 + 1) {
                        geoPoint2 = (GeoPoint) simplify.get(i2 + 1);
                    } else {
                        geoPoint2 = null;
                    }
                    if (geoPoint2 != null) {
                        drawLine(point, projection.toPixels(geoPoint2, (Point) null));
                    }
                    point2 = point;
                } else if (i2 + 1 < simplify.size()) {
                    Point toPixels = projection.toPixels(geoPoint, point);
                    geoPoint = (GeoPoint) simplify.get(i2 + 1);
                    if (createBoundingBoxFromRect.contains(geoPoint)) {
                        drawLine(projection.toPixels(geoPoint, (Point) null), toPixels);
                        point2 = toPixels;
                    } else {
                        point2 = toPixels;
                    }
                } else {
                    point2 = point;
                }
                i2++;
                point = point2;
            }
            if (this.path.isEmpty()) {
                while (i < simplify.size()) {
                    geoPoint = (GeoPoint) simplify.get(i);
                    if (simplify.size() > i + 1) {
                        geoPoint2 = (GeoPoint) simplify.get(i + 1);
                    } else {
                        geoPoint2 = null;
                    }
                    if (geoPoint2 != null) {
                        drawLine(projection.toPixels(geoPoint, (Point) null), projection.toPixels(geoPoint2, (Point) null));
                    }
                    i++;
                }
            }
            System.currentTimeMillis();
            if (this.DEBUG) {
                canvas.drawPath(this.path, this.linePaint);
            } else {
                canvas.drawPath(this.path, this.linePaint);
            }
            if (!this.DEBUG) {
            }
        }
    }

    private List<GeoPoint> simplify(MapView mapView, Projection projection, List<GeoPoint> list) {
        if (!this.simplify) {
            return list;
        }
        if (this.simplifierHandler == null || this.simplifierThread == null) {
            this.simplifierThread = new HandlerThread("simplifier", 1);
            this.simplifierThread.start();
            this.simplifierHandler = new SimplifierHandler(mapView, this.simplifierThread.getLooper());
        }
        if (this.simplified == null) {
            this.simplified = new ArrayList();
            mapView.post(new Simplifier(projection));
            return list;
        } else if (this.simplified.size() != 0) {
            return this.simplified;
        } else {
            return list;
        }
    }

    private void drawLine(Point point, Point point2) {
        this.path.moveTo((float) point.x, (float) point.y);
        this.path.lineTo((float) point2.x, (float) point2.y);
    }

    private Paint createPointPaint() {
        if (this.pointPaint != null) {
            return this.pointPaint;
        }
        Paint paint = new Paint(1);
        paint.setColor(this.linePaint.getColor());
        paint.setAlpha(this.linePaint.getAlpha());
        paint.setStrokeWidth(this.linePaint.getStrokeWidth());
        return paint;
    }

    public boolean onTap(GeoPoint geoPoint, MapView mapView) {
        if (this.tapListener == null || !isNearLine(geoPoint, mapView)) {
            return false;
        }
        this.tapListener.onTap(geoPoint, mapView);
        return true;
    }

    public boolean onTouchEvent(MotionEvent motionEvent, MapView mapView) {
        if (this.touchListener == null || !isNearLine(mapView.getProjection().fromPixels((int) motionEvent.getX(), (int) motionEvent.getY()), mapView)) {
            return false;
        }
        this.touchListener.onTouch(motionEvent, mapView);
        return true;
    }

    public boolean onTrackballEvent(MotionEvent motionEvent, MapView mapView) {
        if (this.trackballListener == null || !isNearLine(mapView.getProjection().fromPixels((int) motionEvent.getX(), (int) motionEvent.getY()), mapView)) {
            return false;
        }
        this.trackballListener.onTrackballEvent(motionEvent, mapView);
        return true;
    }

    private boolean isNearLine(GeoPoint geoPoint, MapView mapView) {
        return Util.distance(mapView.getProjection().toPixels(geoPoint, (Point) null), mapView.getProjection().toPixels(Util.closestPoint(geoPoint, this.data), (Point) null)) < 5.0f;
    }

    private void quitSimplifier() {
        if (this.simplifierHandler != null) {
            this.simplifierHandler.removeMessages(0);
            this.simplifierHandler.reuse.clear();
            this.simplifierHandler = null;
        }
        if (this.simplifierThread != null) {
            Looper looper = this.simplifierThread.getLooper();
            if (looper != null) {
                looper.quit();
            }
            this.simplifierThread = null;
        }
    }

    public void destroy() {
        this.data = null;
        this.points = null;
        this.simplified = null;
        this.boundingBox = null;
        this.path = null;
        this.linePaint = null;
        this.pointPaint = null;
        this.simplify = false;
        quitSimplifier();
    }

    protected void addPoint(GeoPoint geoPoint, int i) {
        if (this.data == null) {
            this.data = new LinkedList();
        }
        this.data.add(geoPoint);
        if (i > 0) {
            while (this.data.size() > i) {
                this.data.remove(0);
            }
        }
        this.boundingBox = BoundingBox.calculateBoundingBoxGeoPoint(this.data);
    }
}
