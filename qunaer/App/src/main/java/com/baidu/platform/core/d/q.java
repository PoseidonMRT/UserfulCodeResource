package com.baidu.platform.core.d;

import com.baidu.mapapi.model.CoordUtil;
import com.baidu.mapapi.search.core.CityInfo;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.core.SearchResult.ERRORNO;
import com.baidu.mapapi.search.route.DrivingRouteResult;
import com.baidu.mapapi.search.route.SuggestAddrInfo;
import com.baidu.mapapi.search.route.TransitRouteResult;
import com.baidu.mapapi.search.route.WalkingRouteResult;
import com.baidu.platform.base.SearchType;
import com.baidu.platform.base.f;
import com.huawei.hwid.openapi.out.OutReturn.ParamStr;
import com.iflytek.aiui.AIUIConstant;
import com.iflytek.cloud.SpeechUtility;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class q extends f {
    SuggestAddrInfo c = null;
    protected boolean d;

    private SuggestAddrInfo a(JSONObject jSONObject) {
        int i = 0;
        SuggestAddrInfo suggestAddrInfo = null;
        if (jSONObject != null) {
            JSONObject optJSONObject = jSONObject.optJSONObject("traffic_pois");
            if (optJSONObject != null) {
                JSONObject optJSONObject2 = optJSONObject.optJSONObject("option");
                JSONObject optJSONObject3 = optJSONObject.optJSONObject(AIUIConstant.KEY_CONTENT);
                if (!(optJSONObject2 == null || optJSONObject3 == null)) {
                    String optString;
                    JSONArray optJSONArray;
                    JSONArray optJSONArray2;
                    int length;
                    boolean[] zArr;
                    boolean[] zArr2;
                    int i2;
                    int parseInt;
                    int parseInt2;
                    optJSONObject = optJSONObject2.optJSONObject("start_city");
                    String optString2 = optJSONObject != null ? optJSONObject.optString("cname") : null;
                    JSONArray optJSONArray3 = optJSONObject2.optJSONArray("end_city");
                    if (optJSONArray3 != null) {
                        optJSONObject = (JSONObject) optJSONArray3.opt(0);
                        if (optJSONObject != null) {
                            optString = optJSONObject.optString("cname");
                            optJSONArray = optJSONObject2.optJSONArray("city_list");
                            optJSONArray2 = optJSONObject2.optJSONArray("prio_flag");
                            if (!(optJSONArray == null || optJSONArray2 == null)) {
                                length = optJSONArray.length();
                                zArr = new boolean[length];
                                zArr2 = new boolean[length];
                                for (i2 = 0; i2 < length; i2++) {
                                    parseInt = Integer.parseInt(optJSONArray.optString(i2));
                                    parseInt2 = Integer.parseInt(optJSONArray2.optString(i2));
                                    zArr[i2] = parseInt != 1;
                                    zArr2[i2] = parseInt2 != 1;
                                }
                                suggestAddrInfo = new SuggestAddrInfo();
                                while (i < length) {
                                    if (!zArr2[i]) {
                                        if (zArr[i]) {
                                            if (i != 0) {
                                                suggestAddrInfo.setSuggestStartNode(a(optJSONObject3.optJSONArray("start"), optString2));
                                            } else if (i == length - 1 || i <= 0) {
                                                suggestAddrInfo.setSuggestWpNode(b(optJSONObject3, "multi_waypoints"));
                                            } else {
                                                suggestAddrInfo.setSuggestEndNode(a(optJSONObject3.optJSONArray("end"), optString));
                                            }
                                        } else if (i != 0) {
                                            suggestAddrInfo.setSuggestStartCity(a(optJSONObject3.optJSONArray("start")));
                                        } else if (i == length - 1 || i <= 0) {
                                            suggestAddrInfo.setSuggestWpCity(a(optJSONObject3, "multi_waypoints"));
                                        } else {
                                            suggestAddrInfo.setSuggestEndCity(a(optJSONObject3.optJSONArray("end")));
                                        }
                                    }
                                    i++;
                                }
                            }
                        }
                    }
                    optString = null;
                    optJSONArray = optJSONObject2.optJSONArray("city_list");
                    optJSONArray2 = optJSONObject2.optJSONArray("prio_flag");
                    length = optJSONArray.length();
                    zArr = new boolean[length];
                    zArr2 = new boolean[length];
                    for (i2 = 0; i2 < length; i2++) {
                        parseInt = Integer.parseInt(optJSONArray.optString(i2));
                        parseInt2 = Integer.parseInt(optJSONArray2.optString(i2));
                        if (parseInt != 1) {
                        }
                        zArr[i2] = parseInt != 1;
                        if (parseInt2 != 1) {
                        }
                        zArr2[i2] = parseInt2 != 1;
                    }
                    suggestAddrInfo = new SuggestAddrInfo();
                    while (i < length) {
                        if (!zArr2[i]) {
                            if (zArr[i]) {
                                if (i != 0) {
                                    if (i == length - 1) {
                                    }
                                    suggestAddrInfo.setSuggestWpNode(b(optJSONObject3, "multi_waypoints"));
                                } else {
                                    suggestAddrInfo.setSuggestStartNode(a(optJSONObject3.optJSONArray("start"), optString2));
                                }
                            } else if (i != 0) {
                                if (i == length - 1) {
                                }
                                suggestAddrInfo.setSuggestWpCity(a(optJSONObject3, "multi_waypoints"));
                            } else {
                                suggestAddrInfo.setSuggestStartCity(a(optJSONObject3.optJSONArray("start")));
                            }
                        }
                        i++;
                    }
                }
            }
        }
        return suggestAddrInfo;
    }

    private List<CityInfo> a(JSONArray jSONArray) {
        if (jSONArray == null || jSONArray.length() <= 0) {
            return null;
        }
        ArrayList arrayList = new ArrayList();
        for (int i = 0; i < jSONArray.length(); i++) {
            JSONObject jSONObject = (JSONObject) jSONArray.opt(i);
            if (jSONObject != null) {
                CityInfo cityInfo = new CityInfo();
                cityInfo.num = jSONObject.optInt("num");
                cityInfo.city = jSONObject.optString(AIUIConstant.KEY_NAME);
                arrayList.add(cityInfo);
            }
        }
        arrayList.trimToSize();
        return arrayList;
    }

    private List<PoiInfo> a(JSONArray jSONArray, String str) {
        if (jSONArray != null) {
            List<PoiInfo> arrayList = new ArrayList();
            for (int i = 0; i < jSONArray.length(); i++) {
                JSONObject jSONObject = (JSONObject) jSONArray.opt(i);
                if (jSONObject != null) {
                    PoiInfo poiInfo = new PoiInfo();
                    poiInfo.address = jSONObject.optString("addr");
                    poiInfo.uid = jSONObject.optString(AIUIConstant.KEY_UID);
                    poiInfo.name = jSONObject.optString(AIUIConstant.KEY_NAME);
                    poiInfo.location = CoordUtil.decodeLocation(jSONObject.optString("geo"));
                    poiInfo.city = str;
                    arrayList.add(poiInfo);
                }
            }
            if (arrayList.size() > 0) {
                return arrayList;
            }
        }
        return null;
    }

    private List<List<CityInfo>> a(JSONObject jSONObject, String str) {
        List<List<CityInfo>> arrayList = new ArrayList();
        if (jSONObject == null) {
            return null;
        }
        JSONArray optJSONArray = jSONObject.optJSONArray(str);
        if (optJSONArray == null) {
            return null;
        }
        for (int i = 0; i < optJSONArray.length(); i++) {
            List a = a((JSONArray) optJSONArray.opt(i));
            if (a != null) {
                arrayList.add(a);
            }
        }
        return arrayList;
    }

    private List<List<PoiInfo>> b(JSONObject jSONObject, String str) {
        List<List<PoiInfo>> arrayList = new ArrayList();
        if (jSONObject == null) {
            return null;
        }
        JSONArray optJSONArray = jSONObject.optJSONArray(str);
        if (optJSONArray == null) {
            return null;
        }
        for (int i = 0; i < optJSONArray.length(); i++) {
            List a = a((JSONArray) optJSONArray.opt(i), "");
            if (a != null) {
                arrayList.add(a);
            }
        }
        return arrayList;
    }

    private boolean b(String str) {
        if (str == null || str.length() <= 0) {
            return false;
        }
        try {
            JSONObject jSONObject = new JSONObject(str);
            if (jSONObject == null) {
                return false;
            }
            JSONObject optJSONObject = jSONObject.optJSONObject(SpeechUtility.TAG_RESOURCE_RESULT);
            if (optJSONObject == null || optJSONObject.optInt("type") != 23 || optJSONObject.optInt(ParamStr.RET_RES_ERROR) != 0) {
                return false;
            }
            this.c = a(jSONObject);
            return this.c != null;
        } catch (JSONException e) {
            e.printStackTrace();
            return false;
        }
    }

    public void a(String str) {
        SearchType a = a();
        if (b(str)) {
            this.d = true;
        } else {
            this.d = false;
        }
        switch (a) {
            case TRANSIT_ROUTE:
                TransitRouteResult transitRouteResult = new TransitRouteResult();
                if (this.d) {
                    transitRouteResult.setSuggestAddrInfo(this.c);
                    transitRouteResult.error = ERRORNO.AMBIGUOUS_ROURE_ADDR;
                } else {
                    ((r) this).a(str, transitRouteResult);
                }
                this.a.a(transitRouteResult);
                return;
            case DRIVE_ROUTE:
                DrivingRouteResult drivingRouteResult = new DrivingRouteResult();
                if (this.d) {
                    drivingRouteResult.setSuggestAddrInfo(this.c);
                    drivingRouteResult.error = ERRORNO.AMBIGUOUS_ROURE_ADDR;
                } else {
                    ((c) this).a(str, drivingRouteResult);
                }
                this.a.a(drivingRouteResult);
                return;
            case WALK_ROUTE:
                WalkingRouteResult walkingRouteResult = new WalkingRouteResult();
                if (this.d) {
                    walkingRouteResult.setSuggestAddrInfo(this.c);
                    walkingRouteResult.error = ERRORNO.AMBIGUOUS_ROURE_ADDR;
                } else {
                    ((t) this).a(str, walkingRouteResult);
                }
                this.a.a(walkingRouteResult);
                return;
            default:
                return;
        }
    }
}
