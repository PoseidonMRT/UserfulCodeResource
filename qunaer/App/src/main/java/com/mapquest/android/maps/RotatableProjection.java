package com.mapquest.android.maps;

import android.graphics.Matrix;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.RectF;

class RotatableProjection implements ProjectionX {
    private static final String TAG = RotatableProjection.class.getSimpleName();
    Point focalPointOnMap = null;
    private float[] mapPointOut = new float[2];
    private MapView mapView;
    private ProjectionX projection;
    private RectF rectF = new RectF();
    private float[] reverseMapPointOut = new float[2];
    private Matrix reverseRotateMatrix;
    private Matrix rotateMatrix;

    public RotatableProjection(MapView mapView, Projection projection) {
        this.mapView = mapView;
        this.projection = (ProjectionX) projection;
        this.rotateMatrix = new Matrix();
        this.reverseRotateMatrix = new Matrix();
    }

    public void setRotate(float f, int i, int i2) {
        Matrix matrix = this.rotateMatrix;
        Matrix matrix2 = this.reverseRotateMatrix;
        if (f == 0.0f) {
            matrix.reset();
            matrix2.reset();
            this.focalPointOnMap = null;
            return;
        }
        matrix2.reset();
        matrix2.setRotate(-f, (float) i, (float) i2);
        matrix.reset();
        matrix2.invert(matrix);
    }

    private void arrayToPoint(float[] fArr, Point point) {
        point.set((int) fArr[0], (int) fArr[1]);
    }

    public void rotateMapRect(Rect rect) {
        this.rectF.set(rect);
        this.reverseRotateMatrix.mapRect(this.rectF);
        this.rectF.round(rect);
    }

    public Point mapPoint(int i, int i2, Point point) {
        if (point == null) {
            point = new Point(i, i2);
        } else {
            point.set(i, i2);
        }
        if (this.mapView.getMapRotation() != 0.0f) {
            this.mapPointOut[0] = (float) i;
            this.mapPointOut[1] = (float) i2;
            this.reverseRotateMatrix.mapPoints(this.mapPointOut);
            arrayToPoint(this.mapPointOut, point);
        }
        return offsetFromFocalPoint(point.x, point.y, point);
    }

    public Point reverseMapPoint(int i, int i2, Point point) {
        if (point == null) {
            point = new Point(i, i2);
        }
        Point offsetToFocalPoint = offsetToFocalPoint(i, i2, point);
        if (this.mapView.getMapRotation() != 0.0f) {
            this.reverseMapPointOut[0] = (float) offsetToFocalPoint.x;
            this.reverseMapPointOut[1] = (float) offsetToFocalPoint.y;
            this.rotateMatrix.mapPoints(this.reverseMapPointOut);
            arrayToPoint(this.reverseMapPointOut, offsetToFocalPoint);
        }
        return offsetToFocalPoint;
    }

    Point offsetFromFocalPoint(int i, int i2, Point point) {
        if (point == null) {
            point = new Point(i, i2);
        } else {
            point.set(i, i2);
        }
        MapView mapView = this.mapView;
        Point point2 = mapView.focalPoint;
        if (point2 != null) {
            point.x = ((mapView.getMapWidth() >> 1) - point2.x) + i;
            point.y = ((mapView.getMapHeight() >> 1) - point2.y) + i2;
        }
        return point;
    }

    Point offsetToFocalPoint(int i, int i2, Point point) {
        if (point == null) {
            point = new Point(i, i2);
        } else {
            point.set(i, i2);
        }
        MapView mapView = this.mapView;
        Point point2 = mapView.focalPoint;
        if (point2 != null) {
            point.x = (point2.x - (mapView.getMapWidth() >> 1)) + i;
            point.y = (point2.y - (mapView.getMapHeight() >> 1)) + i2;
        }
        return point;
    }

    Rect offsetToFocalPoint(Rect rect) {
        MapView mapView = this.mapView;
        Point point = mapView.focalPoint;
        if (point != null) {
            rect.offset(point.x - (mapView.getMapWidth() >> 1), point.y - (mapView.getMapHeight() >> 1));
        }
        return rect;
    }

    public Point deScalePoint(int i, int i2, Point point) {
        if (point == null) {
            point = new Point(i, i2);
        } else {
            point.set(i, i2);
        }
        Point point2 = this.mapView.scalePoint;
        point.x = (int) ((((float) point2.x) + (((float) (i - point2.x)) / this.mapView.currentScale)) + 0.5f);
        point.y = (int) (((((float) (i2 - point2.y)) / this.mapView.currentScale) + ((float) point2.y)) + 0.5f);
        return point;
    }

    public Point scalePoint(int i, int i2, Point point) {
        if (point == null) {
            point = new Point(i, i2);
        } else {
            point.set(i, i2);
        }
        Point point2 = this.mapView.scalePoint;
        point.x = (int) ((((float) point2.x) + (((float) (i - point2.x)) * this.mapView.currentScale)) + 0.5f);
        point.y = (int) (((((float) (i2 - point2.y)) * this.mapView.currentScale) + ((float) point2.y)) + 0.5f);
        return point;
    }

    public int getScaleFactor(int i) {
        return this.projection.getScaleFactor(i);
    }

    public int calculateZoomLevel(BoundingBox boundingBox) {
        return this.projection.calculateZoomLevel(boundingBox);
    }

    public int calculateZoomLevel(BoundingBox boundingBox, int i, int i2) {
        return this.projection.calculateZoomLevel(boundingBox, i, i2);
    }

    public Point getScreenFromGlobal(Point point) {
        Point screenFromGlobal = this.projection.getScreenFromGlobal(point);
        return reverseMapPoint(screenFromGlobal.x, screenFromGlobal.y, screenFromGlobal);
    }

    public GeoPoint fromPixels(int i, int i2) {
        Point point = new Point(i, i2);
        point = mapPoint(point.x, point.y, point);
        return this.projection.fromPixels(point.x, point.y);
    }

    public float metersToEquatorPixels(float f) {
        return this.projection.metersToEquatorPixels(f);
    }

    public Point toPixels(GeoPoint geoPoint, Point point) {
        Point toPixels = this.projection.toPixels(geoPoint, point);
        return reverseMapPoint(toPixels.x, toPixels.y, toPixels);
    }

    public Projection getProjection() {
        return this.projection;
    }
}
