package qunar.sdk.mapapi.listener;

import java.util.ArrayList;
import java.util.List;
import qunar.sdk.mapapi.QunarRouteType;
import qunar.sdk.mapapi.entity.QunarRouteNode;

public interface QunarRoutePlanCallback {
    void onRouteNodeClick(QunarRouteNode qunarRouteNode);

    void onRoutePlanCallback(boolean z, String str, ArrayList<QunarRouteNode> arrayList, int i);

    void onRouteTimeOut(QunarRouteType qunarRouteType, String str);

    void onStartEndNodeAmbiguityCallback(QunarRouteType qunarRouteType, List<QunarRouteNode> list, List<QunarRouteNode> list2);

    void routeAnalysisEndCallback(RoutePlaneResultType routePlaneResultType);

    void routeAnalysisStartCallback();
}
