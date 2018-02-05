package qunar.sdk.mapapi.mapquest;

import com.mapquest.android.maps.BoundingBox;
import com.mapquest.android.maps.GeoPoint;
import com.mapquest.android.maps.MapController;
import com.mapquest.android.maps.MapView;
import java.util.ArrayList;
import java.util.List;
import qunar.sdk.location.QLocation;
import qunar.sdk.mapapi.QunarLocationConfigeration;
import qunar.sdk.mapapi.QunarMapControl;
import qunar.sdk.mapapi.utils.MapConstant.TEMPTOKEN;

public class MapQuestControl implements QunarMapControl {
    private MapController mapController;
    private MapView mapView;

    public MapQuestControl(MapView mapView) {
        if (mapView != null) {
            this.mapView = mapView;
            this.mapController = mapView.getController();
        }
    }

    public void setMapCenter(List<QLocation> list, boolean z, int i) {
    }

    public void setMapCenter(QLocation qLocation, boolean z, int i) {
        if (this.mapController == null) {
            return;
        }
        if (z) {
            this.mapController.animateTo(new GeoPoint(qLocation.getLatitude(), qLocation.getLongitude()));
        } else {
            this.mapController.setCenter(new GeoPoint(qLocation.getLatitude(), qLocation.getLongitude()));
        }
    }

    public void mapCenterTo(int i, int i2, boolean z) {
    }

    public void setMapBounds(List<QLocation> list, boolean z, int i) {
        if (this.mapController != null && list != null && !list.isEmpty()) {
            List arrayList = new ArrayList();
            for (QLocation qLocation : list) {
                arrayList.add(new GeoPoint(qLocation.getLatitude(), qLocation.getLongitude()));
            }
            BoundingBox calculateBoundingBoxGeoPoint = BoundingBox.calculateBoundingBoxGeoPoint(arrayList);
            if (calculateBoundingBoxGeoPoint != null) {
                this.mapController.zoomToSpan(calculateBoundingBoxGeoPoint);
            }
        }
    }

    public void setMapBounds(List<QLocation> list, int i, int i2, boolean z) {
    }

    public void setMapBounds(List<QLocation> list, int i, int i2, boolean z, int i3) {
    }

    public void setMapCenterZoom(QLocation qLocation, float f, boolean z, int i) {
        if (this.mapController != null) {
            if (z) {
                this.mapController.animateTo(new GeoPoint(qLocation.getLatitude(), qLocation.getLongitude()));
            } else {
                this.mapController.setCenter(new GeoPoint(qLocation.getLatitude(), qLocation.getLongitude()));
            }
            this.mapController.setZoom((int) f);
        }
    }

    public void overlook(float f, boolean z, int i) {
    }

    public void rotate(float f, boolean z, int i) {
        if (this.mapController != null) {
            this.mapController.setMapRotation(f);
        }
    }

    public void zoomBy(float f, boolean z, int i) {
        if (this.mapController != null && this.mapView != null) {
            this.mapController.setZoom(this.mapView.getZoomLevel() + ((int) f));
        }
    }

    public void zoomIn() {
        if (this.mapController != null) {
            this.mapController.zoomIn();
        }
    }

    public void zoomOut() {
        if (this.mapController != null) {
            this.mapController.zoomOut();
        }
    }

    public void zoomTo(float f, boolean z, int i) {
        if (this.mapController != null) {
            this.mapController.setZoom((int) f);
        }
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

    public void destroy(String str) {
        if (str != null && TEMPTOKEN.MAP_DESTROY_TOKEN.equals(str)) {
            if (this.mapView != null) {
                this.mapView = null;
            }
            if (this.mapController != null) {
                this.mapController = null;
            }
        }
    }
}
