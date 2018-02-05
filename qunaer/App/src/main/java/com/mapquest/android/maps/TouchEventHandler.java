package com.mapquest.android.maps;

import android.graphics.Point;
import android.graphics.PointF;
import android.os.Message;
import android.support.v4.internal.view.SupportMenu;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import com.iflytek.cloud.SpeechEvent;
import java.util.ArrayList;
import java.util.Iterator;

class TouchEventHandler implements MapTouchEventHandler {
    static final int ACTION = 0;
    private static final String TAG = TouchEventHandler.class.getName();
    private boolean firedMoveStart = false;
    private boolean firedTouch = false;
    private GestureDetector gestureDetector;
    private MapView mapView;
    private SimpleMultiTouchDetector multiTouchDetector;
    MultiTouchDoubleTapHandler multiTouchDoubleTapHandler = null;

    public interface MultiTouchHandler {
        boolean onTouch(MotionEvent motionEvent, SimpleMultiTouchDetector simpleMultiTouchDetector, PointF[] pointFArr);

        boolean onTouchEnd(MotionEvent motionEvent, SimpleMultiTouchDetector simpleMultiTouchDetector, PointF[] pointFArr);

        boolean reset();
    }

    public abstract class AbstractMultiTouchHandler implements MultiTouchHandler {
        public AbstractMultiTouchHandler(MapView mapView) {
        }

        protected void copy(ArrayList<PointF> arrayList, PointF[] pointFArr) {
            int size;
            for (size = arrayList.size(); size < pointFArr.length; size++) {
                arrayList.add(new PointF());
            }
            for (int i = 0; i < pointFArr.length; i++) {
                ((PointF) arrayList.get(i)).x = pointFArr[i].x;
                ((PointF) arrayList.get(i)).y = pointFArr[i].y;
            }
            for (size = arrayList.size() - 1; size >= pointFArr.length; size--) {
                arrayList.remove(size);
            }
        }

        public PointF getCenterPoint(ArrayList<PointF> arrayList) {
            Iterator it = arrayList.iterator();
            float f = 0.0f;
            float f2 = 0.0f;
            float f3 = 0.0f;
            float f4 = 0.0f;
            while (it.hasNext()) {
                PointF pointF = (PointF) it.next();
                if (f4 > pointF.x || f4 == 0.0f) {
                    f4 = pointF.x;
                }
                if (f3 < pointF.x || f3 == 0.0f) {
                    f3 = pointF.x;
                }
                if (f2 > pointF.y || f2 == 0.0f) {
                    f2 = pointF.y;
                }
                if (f < pointF.y || f == 0.0f) {
                    f = pointF.y;
                }
            }
            return new PointF((f3 + f4) / 2.0f, (f + f2) / 2.0f);
        }
    }

    class GestureListener extends SimpleOnGestureListener {
        private GestureListener() {
        }

        public void onLongPress(MotionEvent motionEvent) {
            if (!TouchEventHandler.this.multiTouchDetector.isInProgress()) {
                Message obtain = Message.obtain();
                obtain.what = 4;
                obtain.getData().putParcelable(SpeechEvent.KEY_EVENT_RECORD_DATA, MotionEvent.obtain(motionEvent));
                EventDispatcher.sendMessage(obtain);
                super.onLongPress(motionEvent);
            }
        }

        public boolean onDown(MotionEvent motionEvent) {
            TouchEventHandler.this.mapView.getController().stopPanning();
            return true;
        }

        public boolean onDoubleTap(MotionEvent motionEvent) {
            if (!TouchEventHandler.this.multiTouchDoubleTapHandler.isInProgress()) {
                TouchEventHandler.this.mapView.getController().zoomInFixing((int) motionEvent.getX(), (int) motionEvent.getY());
            }
            return true;
        }

        public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent2, float f, float f2) {
            TouchEventHandler.this.multiTouchDetector.onTouchEvent(motionEvent2);
            if (!TouchEventHandler.this.multiTouchDetector.isInProgress()) {
                Projection projection = TouchEventHandler.this.mapView.getProjection();
                int i = TouchEventHandler.this.mapView.focalPoint.x + ((int) f);
                int i2 = TouchEventHandler.this.mapView.focalPoint.y + ((int) f2);
                TouchEventHandler.this.mapView.centerGeoPoint = projection.fromPixels(i, i2);
                TouchEventHandler.this.mapView.invalidate();
                if (TouchEventHandler.this.firedMoveStart) {
                    EventDispatcher.sendEmptyMessage(22);
                } else {
                    EventDispatcher.sendEmptyMessage(21);
                    TouchEventHandler.this.firedMoveStart = true;
                }
            }
            return true;
        }

        public boolean onFling(MotionEvent motionEvent, MotionEvent motionEvent2, float f, float f2) {
            if (TouchEventHandler.this.multiTouchDetector.isInProgress()) {
                return false;
            }
            TouchEventHandler.this.mapView.getController().getMapAnimator().animateFlick(new Point((int) motionEvent.getX(), (int) motionEvent.getY()), f, f2);
            return true;
        }

        public boolean onSingleTapConfirmed(MotionEvent motionEvent) {
            if (TouchEventHandler.this.mapView.onTap(TouchEventHandler.this.mapView.getProjection().fromPixels((int) motionEvent.getX(), (int) motionEvent.getY()))) {
                return true;
            }
            EventDispatcher.sendEmptyMessage(3);
            return false;
        }
    }

    public class MultiTouchDoubleTapHandler extends AbstractMultiTouchHandler {
        private boolean doZoom = false;
        private MapView mapView;
        private long previousTouchTime = -1;

        public MultiTouchDoubleTapHandler(MapView mapView) {
            super(mapView);
            this.mapView = mapView;
        }

        public boolean onTouch(MotionEvent motionEvent, SimpleMultiTouchDetector simpleMultiTouchDetector, PointF[] pointFArr) {
            int action = motionEvent.getAction() & simpleMultiTouchDetector.getActionMask();
            if (action == 2) {
                this.previousTouchTime = -1;
            } else if (action == simpleMultiTouchDetector.getActionPointerDown()) {
                if (this.previousTouchTime == -1) {
                    this.previousTouchTime = System.currentTimeMillis();
                } else {
                    if (System.currentTimeMillis() - this.previousTouchTime < 1000 && pointFArr.length == 2) {
                        ArrayList arrayList = new ArrayList();
                        copy(arrayList, pointFArr);
                        PointF centerPoint = getCenterPoint(arrayList);
                        this.mapView.getController().zoomOutFixing((int) centerPoint.x, (int) centerPoint.y);
                    }
                    this.previousTouchTime = System.currentTimeMillis();
                }
            }
            return false;
        }

        public boolean isInProgress() {
            return System.currentTimeMillis() - this.previousTouchTime < 1000;
        }

        public boolean onTouchEnd(MotionEvent motionEvent, SimpleMultiTouchDetector simpleMultiTouchDetector, PointF[] pointFArr) {
            return false;
        }

        public boolean reset() {
            return false;
        }
    }

    public class PinchHandler extends AbstractMultiTouchHandler {
        PointF center = null;
        private float lastSuccessScaleFactor = 1.0f;
        private MapView mapView;
        ArrayList<PointF> previousTouchPoints = new ArrayList();
        private float scaleFactor = 1.0f;
        private boolean zoomStarted = false;

        public PinchHandler(MapView mapView) {
            super(mapView);
            this.mapView = mapView;
        }

        public boolean onTouchEnd(MotionEvent motionEvent, SimpleMultiTouchDetector simpleMultiTouchDetector, PointF[] pointFArr) {
            this.previousTouchPoints.clear();
            if (this.zoomStarted) {
                int zoomLevel = this.mapView.getZoomLevel();
                this.mapView.getController().getMapAnimator().animateZoomScaler(zoomLevel, (int) Math.round(((double) zoomLevel) + Util.log2((double) this.scaleFactor)), this.scaleFactor, new Point((int) this.center.x, (int) this.center.y), true);
                this.zoomStarted = false;
            }
            this.scaleFactor = 1.0f;
            this.lastSuccessScaleFactor = 1.0f;
            return true;
        }

        public boolean onTouch(MotionEvent motionEvent, SimpleMultiTouchDetector simpleMultiTouchDetector, PointF[] pointFArr) {
            if (this.previousTouchPoints.size() == 0) {
                copy(this.previousTouchPoints, pointFArr);
                this.scaleFactor *= this.mapView.currentScale;
                this.lastSuccessScaleFactor = this.scaleFactor;
                this.center = getCenterPoint(this.previousTouchPoints);
            } else if ((motionEvent.getAction() & simpleMultiTouchDetector.getActionMask()) == 2) {
                float distance = Util.distance((PointF) this.previousTouchPoints.get(0), (PointF) this.previousTouchPoints.get(1));
                this.scaleFactor = (Util.distance(pointFArr[0], pointFArr[1]) / distance) * this.scaleFactor;
                if (this.mapView.validateZoomLevel((int) Math.round(((double) this.mapView.getZoomLevel()) + Util.log2((double) this.scaleFactor)))) {
                    if (!this.zoomStarted) {
                        TouchEventHandler.this.fireZoomStartEvent();
                        this.zoomStarted = true;
                    }
                    this.mapView.setScale(this.scaleFactor, this.scaleFactor, this.center.x, this.center.y);
                    this.mapView.invalidate();
                    this.lastSuccessScaleFactor = this.scaleFactor;
                } else {
                    this.scaleFactor = this.lastSuccessScaleFactor;
                }
                copy(this.previousTouchPoints, pointFArr);
            }
            return true;
        }

        public boolean reset() {
            this.previousTouchPoints.clear();
            this.scaleFactor = 1.0f;
            return false;
        }
    }

    public class RotationPinchHandler extends AbstractMultiTouchHandler {
        private static final float MINIMUM_ANGLE = 0.5f;
        private static final float MINIMUM_SCALE_FACTOR = 0.03f;
        private PointF center = null;
        private float exAngle;
        private float exScaleFactor = 1.0f;
        private MapView mapView;
        ArrayList<PointF> previousTouchPoints = new ArrayList();
        private boolean rotationStarted = false;
        private float scaleFactor = 1.0f;
        private boolean zoomStarted = false;

        public RotationPinchHandler(MapView mapView) {
            super(mapView);
            this.mapView = mapView;
        }

        public boolean onTouchEnd(MotionEvent motionEvent, SimpleMultiTouchDetector simpleMultiTouchDetector, PointF[] pointFArr) {
            this.previousTouchPoints.clear();
            if (this.zoomStarted) {
                int zoomLevel = this.mapView.getZoomLevel();
                this.mapView.getController().getMapAnimator().animateZoomScaler(zoomLevel, (int) Math.round(((double) zoomLevel) + Util.log2((double) this.scaleFactor)), this.scaleFactor, new Point((int) this.center.x, (int) this.center.y), true);
            }
            if (this.rotationStarted) {
                EventDispatcher.sendEmptyMessage(33);
            }
            this.rotationStarted = false;
            this.zoomStarted = false;
            this.scaleFactor = 1.0f;
            this.exScaleFactor = 1.0f;
            return true;
        }

        public float angle(PointF pointF, PointF pointF2) {
            return angle(pointF, new PointF(pointF.x, pointF.y - Util.hypotenuse(pointF2.y - pointF.y, pointF2.x - pointF.x)), pointF2);
        }

        public float angle(PointF pointF, PointF pointF2, PointF pointF3) {
            return (float) Math.toDegrees(Math.atan2((double) ((pointF.y - pointF2.y) - (pointF.y - pointF3.y)), (double) ((pointF2.x - pointF.x) - (pointF3.x - pointF.x))) * 2.0d);
        }

        public boolean onTouch(MotionEvent motionEvent, SimpleMultiTouchDetector simpleMultiTouchDetector, PointF[] pointFArr) {
            if (this.previousTouchPoints.size() != 0) {
                float distance;
                boolean z;
                float f = 0.0f;
                float f2 = 0.0f;
                for (int i = 0; i < this.previousTouchPoints.size(); i++) {
                    float angle = angle(this.center, (PointF) this.previousTouchPoints.get(i)) - angle(this.center, pointFArr[i]);
                    distance = Util.distance((PointF) this.previousTouchPoints.get(i), this.center) - Util.distance(pointFArr[1], this.center);
                    if (Math.abs(f2) < Math.abs(angle)) {
                        f2 = angle;
                    }
                    if (f < Math.abs(distance)) {
                        f = Math.abs(distance);
                    }
                }
                distance = Util.distance((PointF) this.previousTouchPoints.get(0), (PointF) this.previousTouchPoints.get(1));
                this.scaleFactor = (Util.distance(pointFArr[0], pointFArr[1]) / distance) * this.scaleFactor;
                if (Math.abs(this.scaleFactor - this.exScaleFactor) / MINIMUM_SCALE_FACTOR > Math.abs(f2 - this.exAngle) / MINIMUM_ANGLE) {
                    if (!this.zoomStarted) {
                        this.zoomStarted = true;
                        TouchEventHandler.this.fireZoomStartEvent();
                    }
                    this.mapView.setScale(this.scaleFactor, this.scaleFactor, this.center.x, this.center.y);
                    this.mapView.invalidate();
                    z = true;
                } else if (Math.abs(f2 - this.exAngle) >= 0.3f) {
                    if (!this.rotationStarted) {
                        EventDispatcher.sendEmptyMessage(31);
                        this.rotationStarted = true;
                    }
                    this.mapView.setMapRotation(((this.mapView.getMapRotation() + 360.0f) + f2) % 360.0f);
                    this.mapView.invalidate();
                    z = true;
                } else {
                    z = false;
                }
                if (!z) {
                    return z;
                }
                this.exScaleFactor = this.scaleFactor;
                this.exAngle = f2;
                copy(this.previousTouchPoints, pointFArr);
                return z;
            }
            copy(this.previousTouchPoints, pointFArr);
            this.scaleFactor *= this.mapView.currentScale;
            this.center = getCenterPoint(this.previousTouchPoints);
            return true;
        }

        public boolean reset() {
            return false;
        }
    }

    public class SimpleMultiTouchDetector {
        static final int MAXIMUM_SUPPORTED_POINTER_COUNT = 2;
        protected int actionMask;
        private int actionPointerDown;
        private int actionPointerUp;
        ArrayList<MultiTouchHandler> handlers;
        boolean inProgress;
        boolean isMoveAfterPinch;
        boolean multiTouchSupported;
        float scaleFactor;

        public SimpleMultiTouchDetector() {
            this.inProgress = false;
            this.isMoveAfterPinch = false;
            this.multiTouchSupported = true;
            this.actionPointerDown = 0;
            this.actionPointerUp = 0;
            this.actionMask = SupportMenu.USER_MASK;
            this.handlers = new ArrayList();
            this.multiTouchSupported = checkMultiTouchSupport();
            if (this.multiTouchSupported) {
                try {
                    this.actionPointerDown = MotionEvent.class.getField("ACTION_POINTER_DOWN").getInt(null);
                    this.actionPointerUp = MotionEvent.class.getField("ACTION_POINTER_UP").getInt(null);
                    this.actionMask = MotionEvent.class.getField("ACTION_MASK").getInt(null);
                } catch (Exception e) {
                }
            }
        }

        public void registerHandler(MultiTouchHandler multiTouchHandler) {
            this.handlers.add(multiTouchHandler);
        }

        public void removeHandler(MultiTouchHandler multiTouchHandler) {
            this.handlers.remove(multiTouchHandler);
        }

        public void removeHandlers() {
            this.handlers.clear();
        }

        public boolean isInProgress() {
            return this.inProgress || this.isMoveAfterPinch;
        }

        private boolean checkMethodExists(Class<MotionEvent> cls, String str, Class[] clsArr) {
            try {
                cls.getMethod(str, clsArr);
                return true;
            } catch (Exception e) {
                return false;
            }
        }

        private boolean checkMultiTouchSupport() {
            return checkMethodExists(MotionEvent.class, "getPointerCount", (Class[]) null) && checkMethodExists(MotionEvent.class, "getX", new Class[]{Integer.TYPE}) && checkMethodExists(MotionEvent.class, "getY", new Class[]{Integer.TYPE});
        }

        public Object invoke(Object obj, String str, Class[] clsArr, Object[] objArr) {
            return obj.getClass().getMethod(str, clsArr).invoke(obj, objArr);
        }

        private float getX(MotionEvent motionEvent, int i) {
            return ((Float) invoke(motionEvent, "getX", new Class[]{Integer.TYPE}, new Object[]{Integer.valueOf(i)})).floatValue();
        }

        private float getY(MotionEvent motionEvent, int i) {
            return ((Float) invoke(motionEvent, "getY", new Class[]{Integer.TYPE}, new Object[]{Integer.valueOf(i)})).floatValue();
        }

        private int getPointerCount(MotionEvent motionEvent) {
            return ((Integer) invoke(motionEvent, "getPointerCount", (Class[]) null, (Object[]) null)).intValue();
        }

        public int getActionPointerDown() {
            return this.actionPointerDown;
        }

        public int getActionPointerUp() {
            return this.actionPointerUp;
        }

        public int getActionMask() {
            return this.actionMask;
        }

        public boolean onTouchEvent(MotionEvent motionEvent) {
            if (!this.multiTouchSupported) {
                return false;
            }
            try {
                int action = this.actionMask & motionEvent.getAction();
                if (action == 0) {
                    this.isMoveAfterPinch = false;
                }
                if (getPointerCount(motionEvent) <= 1) {
                    return false;
                }
                this.inProgress = true;
                this.isMoveAfterPinch = true;
                PointF[] pointFArr = new PointF[getPointerCount(motionEvent)];
                for (int i = 0; i < getPointerCount(motionEvent); i++) {
                    pointFArr[i] = new PointF();
                    pointFArr[i].x = getX(motionEvent, i);
                    pointFArr[i].y = getY(motionEvent, i);
                }
                Iterator it;
                if (action == this.actionPointerDown || action == 2) {
                    it = this.handlers.iterator();
                    while (it.hasNext()) {
                        if (((MultiTouchHandler) it.next()).onTouch(motionEvent, this, pointFArr)) {
                            break;
                        }
                    }
                } else if (action == this.actionPointerUp) {
                    it = this.handlers.iterator();
                    while (it.hasNext()) {
                        ((MultiTouchHandler) it.next()).onTouchEnd(motionEvent, this, pointFArr);
                    }
                    this.inProgress = false;
                }
                return true;
            } catch (Exception e) {
                return false;
            }
        }
    }

    public TouchEventHandler(MapView mapView) {
        this.mapView = mapView;
        this.multiTouchDetector = new SimpleMultiTouchDetector();
        this.gestureDetector = new GestureDetector(new GestureListener());
        this.multiTouchDoubleTapHandler = new MultiTouchDoubleTapHandler(mapView);
        disableMultitouchRotation();
    }

    public boolean handleTouchEvent(MotionEvent motionEvent) {
        if (this.multiTouchDetector.onTouchEvent(motionEvent)) {
            return true;
        }
        if ((motionEvent.getAction() & this.multiTouchDetector.actionMask) == 1) {
            if (this.firedMoveStart) {
                EventDispatcher.sendEmptyMessage(23);
                this.firedMoveStart = false;
            }
            this.firedTouch = false;
        }
        if (this.gestureDetector != null) {
            return this.gestureDetector.onTouchEvent(motionEvent);
        }
        return false;
    }

    public void destroy() {
    }

    protected void enableMultitouchRotation() {
        this.multiTouchDetector.removeHandlers();
        this.multiTouchDetector.registerHandler(this.multiTouchDoubleTapHandler);
        this.multiTouchDetector.registerHandler(new RotationPinchHandler(this.mapView));
    }

    protected void disableMultitouchRotation() {
        this.multiTouchDetector.removeHandlers();
        this.multiTouchDetector.registerHandler(this.multiTouchDoubleTapHandler);
        this.multiTouchDetector.registerHandler(new PinchHandler(this.mapView));
    }

    protected void fireZoomEndEvent() {
        EventDispatcher.sendEmptyMessage(12);
    }

    protected void fireZoomStartEvent() {
        EventDispatcher.sendEmptyMessage(11);
    }
}
