package qunar.sdk.mapapi;

import java.util.List;
import qunar.sdk.location.QLocation;

public interface QunarMapControl {
    @Deprecated
    void destroy(String str);

    void mapCenterTo(int i, int i2, boolean z);

    void overlook(float f, boolean z, int i);

    void rotate(float f, boolean z, int i);

    void setAllGesturesEnabled(boolean z);

    void setCompassEnabled(boolean z);

    void setMapBounds(List<QLocation> list, int i, int i2, boolean z);

    void setMapBounds(List<QLocation> list, int i, int i2, boolean z, int i3);

    void setMapBounds(List<QLocation> list, boolean z, int i);

    void setMapCenter(List<QLocation> list, boolean z, int i);

    void setMapCenter(QLocation qLocation, boolean z, int i);

    void setMapCenterZoom(QLocation qLocation, float f, boolean z, int i);

    void setMyLocationConfigeration(QunarLocationConfigeration qunarLocationConfigeration);

    void setMyLocationEnabled(boolean z);

    void setOverlookingGesturesEnabled(boolean z);

    void setRotateGesturesEnabled(boolean z);

    void setScrollGesturesEnabled(boolean z);

    void setZoomGesturesEnabled(boolean z);

    void zoomBy(float f, boolean z, int i);

    void zoomIn();

    void zoomOut();

    void zoomTo(float f, boolean z, int i);
}
