package com.baidu.platform.core.d;

import com.baidu.mapapi.common.Logger;
import com.baidu.mapapi.model.CoordUtil;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.model.inner.GeoPoint;
import com.baidu.mapapi.search.core.RouteNode;
import com.baidu.mapapi.search.core.SearchResult.ERRORNO;
import com.baidu.mapapi.search.core.TaxiInfo;
import com.baidu.mapapi.search.route.DrivingRouteLine;
import com.baidu.mapapi.search.route.DrivingRouteLine.DrivingStep;
import com.baidu.mapapi.search.route.DrivingRouteResult;
import com.huawei.hwid.openapi.out.OutReturn.ParamStr;
import com.iflytek.aiui.AIUIConstant;
import com.iflytek.cloud.SpeechUtility;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class c extends q {
    private RouteNode a(JSONArray jSONArray, List<RouteNode> list) {
        if (jSONArray != null) {
            int length = jSONArray.length();
            if (length > 0) {
                for (int i = 0; i < length; i++) {
                    JSONObject optJSONObject = jSONArray.optJSONObject(i);
                    if (optJSONObject != null) {
                        RouteNode a = a(optJSONObject);
                        if (i == length - 1) {
                            return a;
                        }
                        list.add(a);
                    }
                }
                return null;
            }
        }
        return null;
    }

    private RouteNode a(JSONObject jSONObject) {
        if (jSONObject == null) {
            return null;
        }
        RouteNode routeNode = new RouteNode();
        routeNode.setTitle(jSONObject.optString("wd"));
        routeNode.setUid(jSONObject.optString(AIUIConstant.KEY_UID));
        GeoPoint geoPoint = new GeoPoint(0.0d, 0.0d);
        JSONArray optJSONArray = jSONObject.optJSONArray("spt");
        if (optJSONArray != null) {
            geoPoint.setLongitudeE6((double) optJSONArray.optInt(0));
            geoPoint.setLatitudeE6((double) optJSONArray.optInt(1));
        }
        routeNode.setLocation(CoordUtil.mc2ll(geoPoint));
        return routeNode;
    }

    private List<LatLng> a(JSONArray jSONArray) {
        if (jSONArray != null) {
            int length = jSONArray.length();
            if (length >= 6) {
                List<LatLng> arrayList = new ArrayList();
                double d = 0.0d;
                double d2 = 0.0d;
                for (int i = 5; i < length; i++) {
                    if (i % 2 != 0) {
                        d += (double) jSONArray.optInt(i);
                    } else {
                        d2 += (double) jSONArray.optInt(i);
                        arrayList.add(CoordUtil.mc2ll(new GeoPoint(d2, d)));
                    }
                }
                return arrayList;
            }
        }
        return null;
    }

    private List<DrivingStep> a(JSONArray jSONArray, JSONArray jSONArray2) {
        if (jSONArray != null) {
            int length = jSONArray.length();
            if (length > 0) {
                int i;
                List<DrivingStep> arrayList;
                int i2;
                String str;
                int i3;
                JSONObject optJSONObject;
                DrivingStep drivingStep;
                String optString;
                List a;
                RouteNode routeNode;
                int i4 = 0;
                if (jSONArray2 != null) {
                    i4 = jSONArray2.length();
                    if (i4 > 0) {
                        int i5 = 1;
                        i = i4;
                        arrayList = new ArrayList();
                        i2 = 0;
                        str = "";
                        i3 = 0;
                        while (i3 < length) {
                            optJSONObject = jSONArray.optJSONObject(i3);
                            if (optJSONObject != null) {
                                i4 = i2;
                            } else {
                                drivingStep = new DrivingStep();
                                drivingStep.setDistance(optJSONObject.optInt("distance"));
                                drivingStep.setDirection(optJSONObject.optInt("direction") * 30);
                                drivingStep.setInstructions(optJSONObject.optString("instructions"));
                                optString = optJSONObject.optString("start_instructions");
                                if (optString == null) {
                                    i4 = drivingStep.getDistance();
                                    optString = i4 >= 1000 ? " - " + i4 + "米" : " - " + (((double) i4) / 1000.0d) + "公里";
                                    if (i2 <= arrayList.size()) {
                                        optString = ((DrivingStep) arrayList.get(i2 - 1)).getExitInstructions() + optString;
                                    }
                                }
                                drivingStep.setEntranceInstructions(optString);
                                drivingStep.setExitInstructions(optJSONObject.optString("end_instructions"));
                                drivingStep.setNumTurns(optJSONObject.optInt("turn"));
                                a = a(optJSONObject.optJSONArray("spath"));
                                drivingStep.setPathList(a);
                                if (a != null) {
                                    routeNode = new RouteNode();
                                    routeNode.setLocation((LatLng) a.get(0));
                                    drivingStep.setEntrance(routeNode);
                                    routeNode = new RouteNode();
                                    routeNode.setLocation((LatLng) a.get(a.size() - 1));
                                    drivingStep.setExit(routeNode);
                                }
                                if (r3 != null && i3 < r2) {
                                    drivingStep.setTrafficList(b(jSONArray2.optJSONObject(i3)));
                                }
                                i4 = i2 + 1;
                                arrayList.add(drivingStep);
                            }
                            i3++;
                            i2 = i4;
                        }
                        return arrayList;
                    }
                }
                Object obj = null;
                i = i4;
                arrayList = new ArrayList();
                i2 = 0;
                str = "";
                i3 = 0;
                while (i3 < length) {
                    optJSONObject = jSONArray.optJSONObject(i3);
                    if (optJSONObject != null) {
                        drivingStep = new DrivingStep();
                        drivingStep.setDistance(optJSONObject.optInt("distance"));
                        drivingStep.setDirection(optJSONObject.optInt("direction") * 30);
                        drivingStep.setInstructions(optJSONObject.optString("instructions"));
                        optString = optJSONObject.optString("start_instructions");
                        if (optString == null) {
                            i4 = drivingStep.getDistance();
                            if (i4 >= 1000) {
                            }
                            if (i2 <= arrayList.size()) {
                                optString = ((DrivingStep) arrayList.get(i2 - 1)).getExitInstructions() + optString;
                            }
                        }
                        drivingStep.setEntranceInstructions(optString);
                        drivingStep.setExitInstructions(optJSONObject.optString("end_instructions"));
                        drivingStep.setNumTurns(optJSONObject.optInt("turn"));
                        a = a(optJSONObject.optJSONArray("spath"));
                        drivingStep.setPathList(a);
                        if (a != null) {
                            routeNode = new RouteNode();
                            routeNode.setLocation((LatLng) a.get(0));
                            drivingStep.setEntrance(routeNode);
                            routeNode = new RouteNode();
                            routeNode.setLocation((LatLng) a.get(a.size() - 1));
                            drivingStep.setExit(routeNode);
                        }
                        drivingStep.setTrafficList(b(jSONArray2.optJSONObject(i3)));
                        i4 = i2 + 1;
                        arrayList.add(drivingStep);
                    } else {
                        i4 = i2;
                    }
                    i3++;
                    i2 = i4;
                }
                return arrayList;
            }
        }
        return null;
    }

    private List<TaxiInfo> b(String str) {
        if (str == null || str.length() <= 0) {
            return null;
        }
        List<TaxiInfo> arrayList = new ArrayList();
        try {
            JSONArray jSONArray = new JSONArray(str);
            if (jSONArray == null) {
                return null;
            }
            for (int i = 0; i < jSONArray.length(); i++) {
                JSONObject jSONObject = jSONArray.getJSONObject(i);
                if (jSONObject != null) {
                    TaxiInfo taxiInfo = new TaxiInfo();
                    String optString = jSONObject.optString("total_price");
                    if (optString == null || optString.equals("")) {
                        taxiInfo.setTotalPrice(0.0f);
                    } else {
                        taxiInfo.setTotalPrice(Float.parseFloat(optString));
                    }
                    arrayList.add(taxiInfo);
                }
            }
            return arrayList;
        } catch (JSONException e) {
            if (!Logger.debugEnable()) {
                return null;
            }
            e.printStackTrace();
            return null;
        }
    }

    private List<DrivingStep> b(JSONArray jSONArray, List<DrivingStep> list) {
        if (jSONArray != null) {
            int length = jSONArray.length();
            if (length > 0 && list != null) {
                List<DrivingStep> arrayList = new ArrayList();
                for (int i = 0; i < length; i++) {
                    JSONObject optJSONObject = jSONArray.optJSONObject(i);
                    if (optJSONObject != null) {
                        int optInt = optJSONObject.optInt("n");
                        int optInt2 = optJSONObject.optInt("s");
                        for (int i2 = 0; i2 < optInt; i2++) {
                            if (optInt2 + i2 < list.size()) {
                                arrayList.add(list.get(optInt2 + i2));
                            }
                        }
                    }
                }
                return arrayList;
            }
        }
        return null;
    }

    private boolean b(String str, DrivingRouteResult drivingRouteResult) {
        if (str == null || "".equals(str)) {
            return false;
        }
        try {
            JSONObject jSONObject = new JSONObject(str);
            if (jSONObject == null) {
                return false;
            }
            JSONObject optJSONObject = jSONObject.optJSONObject(SpeechUtility.TAG_RESOURCE_RESULT);
            if (optJSONObject == null) {
                return false;
            }
            switch (optJSONObject.optInt(ParamStr.RET_RES_ERROR)) {
                case 0:
                    JSONObject optJSONObject2 = jSONObject.optJSONObject("cars");
                    if (optJSONObject2 == null) {
                        return false;
                    }
                    jSONObject = optJSONObject2.optJSONObject("option");
                    optJSONObject = optJSONObject2.optJSONObject(AIUIConstant.KEY_CONTENT);
                    if (jSONObject == null || optJSONObject == null) {
                        return false;
                    }
                    RouteNode a = a(jSONObject.optJSONObject("start"));
                    List arrayList = new ArrayList();
                    RouteNode a2 = a(jSONObject.optJSONArray("end"), arrayList);
                    List a3 = a(optJSONObject.optJSONArray("steps"), optJSONObject.optJSONArray("stepts"));
                    List arrayList2 = new ArrayList();
                    JSONArray optJSONArray = optJSONObject.optJSONArray("routes");
                    if (optJSONArray == null) {
                        return false;
                    }
                    for (int i = 0; i < optJSONArray.length(); i++) {
                        DrivingRouteLine drivingRouteLine = new DrivingRouteLine();
                        JSONObject optJSONObject3 = optJSONArray.optJSONObject(i);
                        if (optJSONObject3 != null) {
                            JSONArray optJSONArray2 = optJSONObject3.optJSONArray("legs");
                            if (optJSONArray2 == null) {
                                return false;
                            }
                            int length = optJSONArray2.length();
                            List arrayList3 = new ArrayList();
                            int i2 = 0;
                            int i3 = 0;
                            for (int i4 = 0; i4 < length; i4++) {
                                JSONObject optJSONObject4 = optJSONArray2.optJSONObject(i4);
                                if (optJSONObject4 != null) {
                                    i3 += optJSONObject4.optInt("distance");
                                    i2 += optJSONObject4.optInt("duration");
                                    Collection b = b(optJSONObject4.optJSONArray("stepis"), a3);
                                    if (b != null) {
                                        arrayList3.addAll(b);
                                    }
                                }
                            }
                            drivingRouteLine.setStarting(a);
                            drivingRouteLine.setTerminal(a2);
                            if (arrayList.size() == 0) {
                                drivingRouteLine.setWayPoints(null);
                            } else {
                                drivingRouteLine.setWayPoints(arrayList);
                            }
                            drivingRouteLine.setDistance(i3);
                            drivingRouteLine.setDuration(i2);
                            drivingRouteLine.setCongestionDistance(optJSONObject3.optInt("congestion_length"));
                            drivingRouteLine.setLightNum(optJSONObject3.optInt("light_num"));
                            if (arrayList3.size() == 0) {
                                drivingRouteLine.setSteps(null);
                            } else {
                                drivingRouteLine.setSteps(arrayList3);
                            }
                            arrayList2.add(drivingRouteLine);
                        }
                    }
                    drivingRouteResult.setRouteLines(arrayList2);
                    drivingRouteResult.setTaxiInfos(b(optJSONObject2.optString("taxis")));
                    return true;
                case 4:
                    drivingRouteResult.error = ERRORNO.ST_EN_TOO_NEAR;
                    return true;
                default:
                    return false;
            }
        } catch (JSONException e) {
            e.printStackTrace();
            return false;
        }
    }

    private int[] b(JSONObject jSONObject) {
        int i = 0;
        if (jSONObject == null) {
            return null;
        }
        JSONArray optJSONArray = jSONObject.optJSONArray("end");
        JSONArray optJSONArray2 = jSONObject.optJSONArray("status");
        if (optJSONArray == null || optJSONArray2 == null) {
            return null;
        }
        List arrayList = new ArrayList();
        int length = optJSONArray.length();
        int length2 = optJSONArray2.length();
        int i2 = 0;
        while (i2 < length) {
            int optInt = optJSONArray.optInt(i2);
            int optInt2 = i2 < length2 ? optJSONArray2.optInt(i2) : 0;
            for (int i3 = 0; i3 < optInt; i3++) {
                arrayList.add(Integer.valueOf(optInt2));
            }
            i2++;
        }
        int[] iArr = new int[arrayList.size()];
        while (i < arrayList.size()) {
            iArr[i] = ((Integer) arrayList.get(i)).intValue();
            i++;
        }
        return iArr;
    }

    public void a(String str, DrivingRouteResult drivingRouteResult) {
        if (str == null || str.equals("")) {
            drivingRouteResult.error = ERRORNO.RESULT_NOT_FOUND;
            return;
        }
        try {
            JSONObject jSONObject = new JSONObject(str);
            if (jSONObject.has("SDK_InnerError")) {
                jSONObject = jSONObject.optJSONObject("SDK_InnerError");
                if (jSONObject.has("PermissionCheckError")) {
                    drivingRouteResult.error = ERRORNO.PERMISSION_UNFINISHED;
                    return;
                } else if (jSONObject.has("httpStateError")) {
                    String optString = jSONObject.optString("httpStateError");
                    if (optString.equals("NETWORK_ERROR")) {
                        drivingRouteResult.error = ERRORNO.NETWORK_ERROR;
                        return;
                    } else if (optString.equals("REQUEST_ERROR")) {
                        drivingRouteResult.error = ERRORNO.REQUEST_ERROR;
                        return;
                    } else {
                        drivingRouteResult.error = ERRORNO.SEARCH_SERVER_INTERNAL_ERROR;
                        return;
                    }
                }
            }
            if (!a(str, drivingRouteResult, false) && !b(str, drivingRouteResult)) {
                drivingRouteResult.error = ERRORNO.RESULT_NOT_FOUND;
            }
        } catch (Exception e) {
            drivingRouteResult.error = ERRORNO.RESULT_NOT_FOUND;
        }
    }
}
