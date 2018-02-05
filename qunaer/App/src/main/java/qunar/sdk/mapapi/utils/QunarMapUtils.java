package qunar.sdk.mapapi.utils;

import android.graphics.Point;
import android.location.Location;
import android.view.ViewGroup;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Projection;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.utils.CoordinateConverter;
import com.baidu.mapapi.utils.CoordinateConverter.CoordType;
import com.baidu.mapapi.utils.DistanceUtil;
import com.baidu.mapapi.utils.SpatialRelationUtil;
import com.mapquest.android.maps.GeoPoint;
import java.util.ArrayList;
import java.util.List;
import qunar.sdk.location.QLocation;
import qunar.sdk.mapapi.QunarMapFacade;
import qunar.sdk.mapapi.QunarMapType;
import qunar.sdk.mapapi.QunarMapView;
import qunar.sdk.mapapi.utils.projectTransform.GCJPointer;
import qunar.sdk.mapapi.utils.projectTransform.WSGPointer;

public class QunarMapUtils {
    public static final String GPOINT_CHINA_CENTER = "35.563611,103.388611";

    public static QLocation formatGpoint(String str, QunarMapType qunarMapType, boolean z) {
        if (str == null || !str.contains(",")) {
            return null;
        }
        String[] split = str.split(",");
        if (split == null || split.length < 2) {
            return null;
        }
        try {
            double parseDouble = Double.parseDouble(split[0]);
            double parseDouble2 = Double.parseDouble(split[1]);
            switch (qunarMapType) {
                case BAIDU:
                    LatLng convert = new CoordinateConverter().from(CoordType.COMMON).coord(new LatLng(parseDouble, parseDouble2)).convert();
                    return new QLocation(convert.latitude, convert.longitude);
                case GAODE:
                    return null;
                case MAPQUEST:
                    if (z) {
                        return new QLocation(parseDouble, parseDouble2);
                    }
                    WSGPointer toExactWSGPointer = new GCJPointer(parseDouble, parseDouble2).toExactWSGPointer();
                    return new QLocation(toExactWSGPointer.getLatitude(), toExactWSGPointer.getLongitude());
                default:
                    return null;
            }
        } catch (Exception e) {
            return null;
        }
    }

    @Deprecated
    public static QLocation formatGpoint(String str) {
        if (str == null || !str.contains(",")) {
            return null;
        }
        String[] split = str.split(",");
        if (split == null || split.length < 2) {
            return null;
        }
        try {
            double parseDouble = Double.parseDouble(split[0]);
            double parseDouble2 = Double.parseDouble(split[1]);
            switch (QunarMapFacade.mapType) {
                case BAIDU:
                    LatLng convert = new CoordinateConverter().from(CoordType.COMMON).coord(new LatLng(parseDouble, parseDouble2)).convert();
                    return new QLocation(convert.latitude, convert.longitude);
                case GAODE:
                    return null;
                case MAPQUEST:
                    WSGPointer toExactWSGPointer = new GCJPointer(parseDouble, parseDouble2).toExactWSGPointer();
                    return new QLocation(toExactWSGPointer.getLatitude(), toExactWSGPointer.getLongitude());
                default:
                    return null;
            }
        } catch (Exception e) {
            return null;
        }
    }

    public static QLocation OffsetLocation(QLocation qLocation, int i, int i2, QunarMapView qunarMapView) {
        QunarMapType qunarMapType = qunarMapView.getQunarMapType();
        ViewGroup displayMap = qunarMapView.getDisplayMap();
        Point toScreenLocation;
        switch (qunarMapType) {
            case BAIDU:
                if (!(displayMap instanceof MapView)) {
                    return null;
                }
                QLocation qLocation2;
                BaiduMap map = ((MapView) displayMap).getMap();
                LatLng latLng = new LatLng(qLocation.getLatitude(), qLocation.getLongitude());
                Projection projection = map.getProjection();
                if (projection != null) {
                    toScreenLocation = projection.toScreenLocation(latLng);
                    toScreenLocation.x -= i;
                    toScreenLocation.y -= i2;
                    LatLng fromScreenLocation = projection.fromScreenLocation(toScreenLocation);
                    qLocation2 = new QLocation(fromScreenLocation.latitude, fromScreenLocation.longitude);
                } else {
                    qLocation2 = null;
                }
                return qLocation2;
            case MAPQUEST:
                if (!(displayMap instanceof com.mapquest.android.maps.MapView)) {
                    return null;
                }
                com.mapquest.android.maps.Projection projection2 = ((com.mapquest.android.maps.MapView) displayMap).getProjection();
                toScreenLocation = projection2.toPixels(new GeoPoint(qLocation.getLatitude(), qLocation.getLongitude()), new Point());
                toScreenLocation.x -= i;
                toScreenLocation.y -= i2;
                GeoPoint fromPixels = projection2.fromPixels(toScreenLocation.x, toScreenLocation.y);
                return new QLocation(fromPixels.getLatitude(), fromPixels.getLongitude());
            default:
                return null;
        }
    }

    public static QLocation fromScreenLocation(Point point, ViewGroup viewGroup) {
        if (point == null || viewGroup == null) {
            return null;
        }
        if (viewGroup instanceof MapView) {
            Projection projection = ((MapView) viewGroup).getMap().getProjection();
            if (projection == null) {
                return null;
            }
            LatLng fromScreenLocation = projection.fromScreenLocation(point);
            return new QLocation(fromScreenLocation.latitude, fromScreenLocation.longitude);
        } else if (!(viewGroup instanceof com.mapquest.android.maps.MapView)) {
            return null;
        } else {
            GeoPoint fromPixels = ((com.mapquest.android.maps.MapView) viewGroup).getProjection().fromPixels(point.x, point.y);
            return new QLocation(fromPixels.getLatitude(), fromPixels.getLongitude());
        }
    }

    public static Point toScreenLocation(QLocation qLocation, ViewGroup viewGroup) {
        if (qLocation == null || viewGroup == null) {
            return null;
        }
        if (viewGroup instanceof MapView) {
            LatLng latLng = new LatLng(qLocation.getLatitude(), qLocation.getLongitude());
            Projection projection = ((MapView) viewGroup).getMap().getProjection();
            if (projection != null) {
                return projection.toScreenLocation(latLng);
            }
            return null;
        } else if (!(viewGroup instanceof com.mapquest.android.maps.MapView)) {
            return null;
        } else {
            return ((com.mapquest.android.maps.MapView) viewGroup).getProjection().toPixels(new GeoPoint(qLocation.getLatitude(), qLocation.getLongitude()), new Point());
        }
    }

    public static double getDistance(Point point, Point point2, ViewGroup viewGroup) {
        if (point == null || point2 == null) {
            return 0.0d;
        }
        switch (QunarMapFacade.mapType) {
            case BAIDU:
                if (!(viewGroup instanceof MapView)) {
                    return 0.0d;
                }
                Projection projection = ((MapView) viewGroup).getMap().getProjection();
                if (projection != null) {
                    return DistanceUtil.getDistance(projection.fromScreenLocation(point), projection.fromScreenLocation(point2));
                }
                return 0.0d;
            case GAODE:
                return 0.0d;
            default:
                return 0.0d;
        }
    }

    public static double getDistance(QLocation qLocation, QLocation qLocation2) {
        float[] fArr = new float[1];
        if (qLocation == null || qLocation2 == null) {
            return 0.0d;
        }
        Location.distanceBetween(qLocation.getLatitude(), qLocation.getLongitude(), qLocation2.getLatitude(), qLocation2.getLongitude(), fArr);
        return (double) fArr[0];
    }

    public static boolean isPolygonContainsPoint(List<QLocation> list, QLocation qLocation, QunarMapView qunarMapView) {
        if (list == null || list.size() == 0 || qLocation == null) {
            return false;
        }
        switch (qunarMapView.getQunarMapType()) {
            case BAIDU:
                List arrayList = new ArrayList();
                for (QLocation qLocation2 : list) {
                    arrayList.add(new LatLng(qLocation2.getLatitude(), qLocation2.getLongitude()));
                }
                return SpatialRelationUtil.isPolygonContainsPoint(arrayList, new LatLng(qLocation.getLatitude(), qLocation.getLongitude()));
            case GAODE:
            case MAPQUEST:
                return false;
            default:
                return false;
        }
    }
}
