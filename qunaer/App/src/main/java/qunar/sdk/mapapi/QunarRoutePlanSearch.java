package qunar.sdk.mapapi;

import qunar.sdk.mapapi.entity.QMarker;
import qunar.sdk.mapapi.entity.QunarRouteNode;
import qunar.sdk.mapapi.listener.QunarRoutePlanCallback;

public interface QunarRoutePlanSearch {
    void appointStartEndShowMarker(QMarker qMarker, QMarker qMarker2);

    void cancelRoutePlane();

    void destory();

    void removeFromMap();

    void setRotePlanCallback(QunarRoutePlanCallback qunarRoutePlanCallback);

    void startRoutPlane(QunarRouteType qunarRouteType, QunarRouteNode qunarRouteNode, QunarRouteNode qunarRouteNode2, String str);
}
