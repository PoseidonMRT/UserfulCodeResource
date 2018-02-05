package com.mapquest.android.maps;

import android.graphics.Point;

interface ProjectionX extends Projection {
    int calculateZoomLevel(BoundingBox boundingBox);

    int calculateZoomLevel(BoundingBox boundingBox, int i, int i2);

    int getScaleFactor(int i);

    Point getScreenFromGlobal(Point point);
}
