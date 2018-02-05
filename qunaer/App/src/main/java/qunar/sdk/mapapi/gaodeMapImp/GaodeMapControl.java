package qunar.sdk.mapapi.gaodeMapImp;

import java.util.List;
import qunar.sdk.location.QLocation;
import qunar.sdk.mapapi.QunarLocationConfigeration;
import qunar.sdk.mapapi.QunarMapControl;
import qunar.sdk.mapapi.utils.MapConstant.TEMPTOKEN;

public class GaodeMapControl implements QunarMapControl {
    public void setMapCenter(List<QLocation> list, boolean z, int i) {
    }

    public void setMapCenter(QLocation qLocation, boolean z, int i) {
    }

    public void mapCenterTo(int i, int i2, boolean z) {
    }

    public void setMapBounds(List<QLocation> list, boolean z, int i) {
    }

    public void setMapBounds(List<QLocation> list, int i, int i2, boolean z) {
    }

    public void setMapBounds(List<QLocation> list, int i, int i2, boolean z, int i3) {
    }

    public void setMapCenterZoom(QLocation qLocation, float f, boolean z, int i) {
    }

    public void overlook(float f, boolean z, int i) {
    }

    public void rotate(float f, boolean z, int i) {
    }

    public void zoomBy(float f, boolean z, int i) {
    }

    public void zoomIn() {
    }

    public void zoomOut() {
    }

    public void zoomTo(float f, boolean z, int i) {
    }

    public void setAllGesturesEnabled(boolean z) {
    }

    public void setCompassEnabled(boolean z) {
    }

    public void setOverlookingGesturesEnabled(boolean z) {
    }

    public void setRotateGesturesEnabled(boolean z) {
    }

    public void setScrollGesturesEnabled(boolean z) {
    }

    public void setZoomGesturesEnabled(boolean z) {
    }

    public void setMyLocationEnabled(boolean z) {
    }

    public void setMyLocationConfigeration(QunarLocationConfigeration qunarLocationConfigeration) {
    }

    @Deprecated
    public void destroy(String str) {
        if (str != null && !TEMPTOKEN.MAP_DESTROY_TOKEN.equals(str)) {
        }
    }
}
