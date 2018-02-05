package com.baidu.platform.core.d;

import android.net.http.Headers;
import com.baidu.mapapi.CoordType;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.BusInfo;
import com.baidu.mapapi.search.core.CoachInfo;
import com.baidu.mapapi.search.core.PlaneInfo;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.core.PriceInfo;
import com.baidu.mapapi.search.core.RouteNode;
import com.baidu.mapapi.search.core.SearchResult.ERRORNO;
import com.baidu.mapapi.search.core.TaxiInfo;
import com.baidu.mapapi.search.core.TransitResultNode;
import com.baidu.mapapi.search.core.k;
import com.baidu.mapapi.search.route.MassTransitRouteLine;
import com.baidu.mapapi.search.route.MassTransitRouteLine.TransitStep;
import com.baidu.mapapi.search.route.MassTransitRouteLine.TransitStep.StepVehicleInfoType;
import com.baidu.mapapi.search.route.MassTransitRouteLine.TransitStep.TrafficCondition;
import com.baidu.mapapi.search.route.MassTransitRouteResult;
import com.baidu.mapapi.search.route.SuggestAddrInfo;
import com.baidu.platform.base.f;
import com.baidu.platform.comapi.util.CoordTrans;
import com.iflytek.aiui.AIUIConstant;
import com.iflytek.cloud.SpeechUtility;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class h extends f {
    private TransitResultNode a(int i, JSONObject jSONObject) {
        LatLng latLng = null;
        if (jSONObject == null) {
            return null;
        }
        String optString = jSONObject.optString("wd");
        String optString2 = jSONObject.optString("city_name");
        int optInt = i == 1 ? jSONObject.optInt("city_code") : jSONObject.optInt("city_id");
        JSONObject optJSONObject = jSONObject.optJSONObject(Headers.LOCATION);
        if (optJSONObject != null) {
            latLng = new LatLng(optJSONObject.optDouble("lat"), optJSONObject.optDouble("lng"));
            if (SDKInitializer.getCoordType() == CoordType.GCJ02) {
                latLng = CoordTrans.baiduToGcj(latLng);
            }
        }
        return new TransitResultNode(optInt, optString2, latLng, optString);
    }

    private TransitStep a(JSONObject jSONObject) {
        if (jSONObject == null) {
            return null;
        }
        LatLng latLng;
        TransitStep transitStep = new TransitStep();
        transitStep.setDistance((int) jSONObject.optDouble("distance"));
        transitStep.setDuration((int) jSONObject.optDouble("duration"));
        transitStep.setInstructions(jSONObject.optString("instructions"));
        transitStep.setPathString(jSONObject.optString("path"));
        transitStep.setTrafficConditions(b(jSONObject.optJSONArray("traffic_condition")));
        JSONObject optJSONObject = jSONObject.optJSONObject("start_location");
        if (optJSONObject != null) {
            latLng = new LatLng(optJSONObject.optDouble("lat"), optJSONObject.optDouble("lng"));
            if (SDKInitializer.getCoordType() == CoordType.GCJ02) {
                latLng = CoordTrans.baiduToGcj(latLng);
            }
            transitStep.setStartLocation(latLng);
        }
        optJSONObject = jSONObject.optJSONObject("end_location");
        if (optJSONObject != null) {
            latLng = new LatLng(optJSONObject.optDouble("lat"), optJSONObject.optDouble("lng"));
            if (SDKInitializer.getCoordType() == CoordType.GCJ02) {
                latLng = CoordTrans.baiduToGcj(latLng);
            }
            transitStep.setEndLocation(latLng);
        }
        JSONObject optJSONObject2 = jSONObject.optJSONObject("vehicle_info");
        if (optJSONObject2 != null) {
            int optInt = optJSONObject2.optInt("type");
            optJSONObject2 = optJSONObject2.optJSONObject("detail");
            switch (optInt) {
                case 1:
                    transitStep.setVehileType(StepVehicleInfoType.ESTEP_TRAIN);
                    if (optJSONObject2 != null) {
                        k kVar = new k();
                        kVar.setName(optJSONObject2.optString(AIUIConstant.KEY_NAME));
                        kVar.a(optJSONObject2.optDouble("price"));
                        kVar.a(optJSONObject2.optString("booking"));
                        kVar.setDepartureStation(optJSONObject2.optString("departure_station"));
                        kVar.setArriveStation(optJSONObject2.optString("arrive_station"));
                        kVar.setDepartureTime(optJSONObject2.optString("departure_time"));
                        kVar.setArriveTime(optJSONObject2.optString("arrive_time"));
                        transitStep.setTrainInfo(kVar);
                        break;
                    }
                    break;
                case 2:
                    transitStep.setVehileType(StepVehicleInfoType.ESTEP_PLANE);
                    if (optJSONObject2 != null) {
                        PlaneInfo planeInfo = new PlaneInfo();
                        planeInfo.setName(optJSONObject2.optString(AIUIConstant.KEY_NAME));
                        planeInfo.setPrice(optJSONObject2.optDouble("price"));
                        planeInfo.setDiscount(optJSONObject2.optDouble("discount"));
                        planeInfo.setAirlines(optJSONObject2.optString("airlines"));
                        planeInfo.setBooking(optJSONObject2.optString("booking"));
                        planeInfo.setDepartureStation(optJSONObject2.optString("departure_station"));
                        planeInfo.setArriveStation(optJSONObject2.optString("arrive_station"));
                        planeInfo.setDepartureTime(optJSONObject2.optString("departure_time"));
                        planeInfo.setArriveTime(optJSONObject2.optString("arrive_time"));
                        transitStep.setPlaneInfo(planeInfo);
                        break;
                    }
                    break;
                case 3:
                    transitStep.setVehileType(StepVehicleInfoType.ESTEP_BUS);
                    if (optJSONObject2 != null) {
                        BusInfo busInfo = new BusInfo();
                        busInfo.setName(optJSONObject2.optString(AIUIConstant.KEY_NAME));
                        busInfo.setType(optJSONObject2.optInt("type"));
                        busInfo.setStopNum(optJSONObject2.optInt("stop_num"));
                        busInfo.setDepartureStation(optJSONObject2.optString("on_station"));
                        busInfo.setArriveStation(optJSONObject2.optString("off_station"));
                        busInfo.setDepartureTime(optJSONObject2.optString("first_time"));
                        busInfo.setArriveTime(optJSONObject2.optString("last_time"));
                        transitStep.setBusInfo(busInfo);
                        break;
                    }
                    break;
                case 4:
                    transitStep.setVehileType(StepVehicleInfoType.ESTEP_DRIVING);
                    break;
                case 5:
                    transitStep.setVehileType(StepVehicleInfoType.ESTEP_WALK);
                    break;
                case 6:
                    transitStep.setVehileType(StepVehicleInfoType.ESTEP_COACH);
                    if (optJSONObject2 != null) {
                        CoachInfo coachInfo = new CoachInfo();
                        coachInfo.setName(optJSONObject2.optString(AIUIConstant.KEY_NAME));
                        coachInfo.setPrice(optJSONObject2.optDouble("price"));
                        coachInfo.setBooking(optJSONObject2.optString("booking"));
                        coachInfo.setProviderName(optJSONObject2.optString("provider_name"));
                        coachInfo.setProviderUrl(optJSONObject2.optString("provider_url"));
                        coachInfo.setDepartureStation(optJSONObject2.optString("departure_station"));
                        coachInfo.setArriveStation(optJSONObject2.optString("arrive_station"));
                        coachInfo.setDepartureTime(optJSONObject2.optString("departure_time"));
                        coachInfo.setArriveTime(optJSONObject2.optString("arrive_time"));
                        transitStep.setCoachInfo(coachInfo);
                        break;
                    }
                    break;
            }
        }
        return transitStep;
    }

    private List<List<TransitStep>> a(JSONArray jSONArray) {
        List<List<TransitStep>> arrayList = new ArrayList();
        if (jSONArray == null || jSONArray.length() < 0) {
            return null;
        }
        for (int i = 0; i < jSONArray.length(); i++) {
            JSONArray optJSONArray = jSONArray.optJSONArray(i);
            if (optJSONArray != null && optJSONArray.length() > 0) {
                List arrayList2 = new ArrayList();
                for (int i2 = 0; i2 < optJSONArray.length(); i2++) {
                    JSONObject optJSONObject = optJSONArray.optJSONObject(i2);
                    if (optJSONObject != null) {
                        arrayList2.add(a(optJSONObject));
                    }
                }
                arrayList.add(arrayList2);
            }
        }
        return arrayList;
    }

    private TaxiInfo b(String str) {
        if (str == null || str.length() == 0) {
            return null;
        }
        JSONObject jSONObject;
        try {
            jSONObject = new JSONObject(str);
        } catch (JSONException e) {
            e.printStackTrace();
            jSONObject = null;
        }
        if (jSONObject == null) {
            return null;
        }
        TaxiInfo taxiInfo = new TaxiInfo();
        taxiInfo.setDesc(jSONObject.optString("remark"));
        taxiInfo.setDistance(jSONObject.optInt("distance"));
        taxiInfo.setDuration(jSONObject.optInt("duration"));
        taxiInfo.setTotalPrice((float) jSONObject.optDouble("total_price"));
        taxiInfo.setStartPrice((float) jSONObject.optDouble("start_price"));
        taxiInfo.setPerKMPrice((float) jSONObject.optDouble("km_price"));
        return taxiInfo;
    }

    private SuggestAddrInfo b(JSONObject jSONObject) {
        SuggestAddrInfo suggestAddrInfo = new SuggestAddrInfo();
        suggestAddrInfo.setSuggestStartNode(d(jSONObject.optJSONArray("origin_list")));
        suggestAddrInfo.setSuggestEndNode(d(jSONObject.optJSONArray("destination_list")));
        return suggestAddrInfo;
    }

    private List<TrafficCondition> b(JSONArray jSONArray) {
        if (jSONArray == null || jSONArray.length() < 0) {
            return null;
        }
        List<TrafficCondition> arrayList = new ArrayList();
        for (int i = 0; i < jSONArray.length(); i++) {
            JSONObject optJSONObject = jSONArray.optJSONObject(i);
            if (optJSONObject != null) {
                TrafficCondition trafficCondition = new TrafficCondition();
                trafficCondition.setTrafficStatus(optJSONObject.optInt("status"));
                trafficCondition.setTrafficGeoCnt(optJSONObject.optInt("geo_cnt"));
                arrayList.add(trafficCondition);
            }
        }
        return arrayList;
    }

    private List<PriceInfo> c(JSONArray jSONArray) {
        if (jSONArray == null) {
            return null;
        }
        List<PriceInfo> arrayList = new ArrayList();
        for (int i = 0; i < jSONArray.length(); i++) {
            PriceInfo priceInfo = new PriceInfo();
            JSONObject optJSONObject = jSONArray.optJSONObject(i);
            if (optJSONObject != null) {
                priceInfo.setTicketType(optJSONObject.optInt("ticket_type"));
                priceInfo.setTicketPrice(optJSONObject.optDouble("ticket_price"));
            }
            arrayList.add(priceInfo);
        }
        return arrayList;
    }

    private List<PoiInfo> d(JSONArray jSONArray) {
        if (jSONArray != null) {
            List<PoiInfo> arrayList = new ArrayList();
            for (int i = 0; i < jSONArray.length(); i++) {
                JSONObject jSONObject = (JSONObject) jSONArray.opt(i);
                if (jSONObject != null) {
                    PoiInfo poiInfo = new PoiInfo();
                    poiInfo.address = jSONObject.optString("address");
                    poiInfo.uid = jSONObject.optString(AIUIConstant.KEY_UID);
                    poiInfo.name = jSONObject.optString(AIUIConstant.KEY_NAME);
                    jSONObject = jSONObject.optJSONObject(Headers.LOCATION);
                    if (jSONObject != null) {
                        poiInfo.location = new LatLng(jSONObject.optDouble("lat"), jSONObject.optDouble("lng"));
                        if (SDKInitializer.getCoordType() == CoordType.GCJ02) {
                            poiInfo.location = CoordTrans.baiduToGcj(poiInfo.location);
                        }
                    }
                    arrayList.add(poiInfo);
                }
            }
            if (!arrayList.isEmpty()) {
                return arrayList;
            }
        }
        return null;
    }

    public void a(String str) {
        MassTransitRouteResult massTransitRouteResult = new MassTransitRouteResult();
        if (str == null || str.equals("")) {
            massTransitRouteResult.error = ERRORNO.RESULT_NOT_FOUND;
            this.a.a(massTransitRouteResult);
            return;
        }
        try {
            JSONObject jSONObject = new JSONObject(str);
            if (jSONObject.has("SDK_InnerError")) {
                jSONObject = jSONObject.optJSONObject("SDK_InnerError");
                if (jSONObject.has("PermissionCheckError")) {
                    massTransitRouteResult.error = ERRORNO.PERMISSION_UNFINISHED;
                    this.a.a(massTransitRouteResult);
                    return;
                } else if (jSONObject.has("httpStateError")) {
                    String optString = jSONObject.optString("httpStateError");
                    if (optString.equals("NETWORK_ERROR")) {
                        massTransitRouteResult.error = ERRORNO.NETWORK_ERROR;
                    } else if (optString.equals("REQUEST_ERROR")) {
                        massTransitRouteResult.error = ERRORNO.REQUEST_ERROR;
                    } else {
                        massTransitRouteResult.error = ERRORNO.SEARCH_SERVER_INTERNAL_ERROR;
                    }
                    this.a.a(massTransitRouteResult);
                    return;
                }
            }
            if (!(a(str, massTransitRouteResult, false) || a(str, massTransitRouteResult))) {
                massTransitRouteResult.error = ERRORNO.RESULT_NOT_FOUND;
            }
            this.a.a(massTransitRouteResult);
        } catch (Exception e) {
            massTransitRouteResult.error = ERRORNO.RESULT_NOT_FOUND;
            this.a.a(massTransitRouteResult);
        }
    }

    public boolean a(String str, MassTransitRouteResult massTransitRouteResult) {
        int i = 0;
        if (str == null || str.length() <= 0) {
            return false;
        }
        try {
            JSONObject jSONObject = new JSONObject(str);
            if (jSONObject == null) {
                return false;
            }
            switch (jSONObject.optInt("status_sdk")) {
                case 0:
                    int optInt = jSONObject.optInt("type");
                    jSONObject = jSONObject.optJSONObject(SpeechUtility.TAG_RESOURCE_RESULT);
                    if (jSONObject == null) {
                        return false;
                    }
                    if (optInt == 1) {
                        massTransitRouteResult.setOrigin(a(optInt, jSONObject.optJSONObject("origin_info")));
                        massTransitRouteResult.setDestination(a(optInt, jSONObject.optJSONObject("destination_info")));
                        massTransitRouteResult.setSuggestAddrInfo(b(jSONObject));
                        massTransitRouteResult.error = ERRORNO.AMBIGUOUS_ROURE_ADDR;
                    } else if (optInt == 2) {
                        TransitResultNode a = a(optInt, jSONObject.optJSONObject("origin"));
                        massTransitRouteResult.setOrigin(a);
                        TransitResultNode a2 = a(optInt, jSONObject.optJSONObject("destination"));
                        massTransitRouteResult.setDestination(a2);
                        massTransitRouteResult.setTotal(jSONObject.optInt("total"));
                        massTransitRouteResult.setTaxiInfo(b(jSONObject.optString("taxi")));
                        JSONArray optJSONArray = jSONObject.optJSONArray("routes");
                        if (optJSONArray == null || optJSONArray.length() <= 0) {
                            return false;
                        }
                        List arrayList = new ArrayList();
                        while (i < optJSONArray.length()) {
                            JSONObject optJSONObject = optJSONArray.optJSONObject(i);
                            if (optJSONObject != null) {
                                RouteNode routeNode;
                                MassTransitRouteLine massTransitRouteLine = new MassTransitRouteLine();
                                massTransitRouteLine.setDistance(optJSONObject.optInt("distance"));
                                massTransitRouteLine.setDuration(optJSONObject.optInt("duration"));
                                massTransitRouteLine.setArriveTime(optJSONObject.optString("arrive_time"));
                                massTransitRouteLine.setPrice(optJSONObject.optDouble("price"));
                                massTransitRouteLine.setPriceInfo(c(optJSONObject.optJSONArray("price_detail")));
                                if (a != null) {
                                    routeNode = new RouteNode();
                                    routeNode.setLocation(a.getLocation());
                                    massTransitRouteLine.setStarting(routeNode);
                                }
                                if (a2 != null) {
                                    routeNode = new RouteNode();
                                    routeNode.setLocation(a2.getLocation());
                                    massTransitRouteLine.setTerminal(routeNode);
                                }
                                JSONArray optJSONArray2 = optJSONObject.optJSONArray("steps");
                                if (optJSONArray2 != null && optJSONArray2.length() > 0) {
                                    massTransitRouteLine.setNewSteps(a(optJSONArray2));
                                    arrayList.add(massTransitRouteLine);
                                }
                            }
                            i++;
                        }
                        massTransitRouteResult.setRoutelines(arrayList);
                        massTransitRouteResult.error = ERRORNO.NO_ERROR;
                    }
                    return true;
                case 1:
                    massTransitRouteResult.error = ERRORNO.MASS_TRANSIT_SERVER_ERROR;
                    return true;
                case 2:
                    massTransitRouteResult.error = ERRORNO.MASS_TRANSIT_OPTION_ERROR;
                    return true;
                case 1002:
                    massTransitRouteResult.error = ERRORNO.MASS_TRANSIT_NO_POI_ERROR;
                    return true;
                default:
                    return false;
            }
        } catch (JSONException e) {
            e.printStackTrace();
            return false;
        }
    }
}
