package com.baidu.platform.core.c;

import com.baidu.mapapi.model.CoordUtil;
import com.baidu.mapapi.search.core.CityInfo;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.core.PoiInfo.POITYPE;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.core.SearchResult.ERRORNO;
import com.baidu.mapapi.search.poi.PoiAddrInfo;
import com.baidu.mapapi.search.poi.PoiResult;
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

public class l extends f {
    int c;
    int d;

    l(int i, int i2) {
        this.c = i;
        this.d = i2;
    }

    private boolean a(String str, PoiResult poiResult) {
        if (str == null || str.equals("")) {
            return false;
        }
        try {
            JSONObject jSONObject = new JSONObject(str);
            JSONObject optJSONObject = jSONObject.optJSONObject(SpeechUtility.TAG_RESOURCE_RESULT);
            if (optJSONObject == null || optJSONObject.optInt(ParamStr.RET_RES_ERROR) != 0) {
                return false;
            }
            JSONObject optJSONObject2 = jSONObject.optJSONObject("poi_result");
            if (optJSONObject2 == null) {
                return false;
            }
            jSONObject = optJSONObject2.optJSONObject("option");
            JSONArray optJSONArray = optJSONObject2.optJSONArray("contents");
            if (jSONObject == null || optJSONArray == null) {
                return false;
            }
            int length = optJSONArray.length();
            if (length <= 0) {
                return false;
            }
            int optInt = jSONObject.optInt("total");
            poiResult.setTotalPoiNum(optInt);
            poiResult.setCurrentPageCapacity(length);
            poiResult.setCurrentPageNum(this.c);
            if (length != 0) {
                poiResult.setTotalPageNum((optInt % this.d > 0 ? 1 : 0) + (optInt / this.d));
            }
            optJSONObject = optJSONObject2.optJSONObject("current_city");
            String str2 = null;
            if (optJSONObject != null) {
                str2 = optJSONObject.optString(AIUIConstant.KEY_NAME);
            }
            Object arrayList = new ArrayList();
            for (int i = 0; i < length; i++) {
                JSONObject optJSONObject3 = optJSONArray.optJSONObject(i);
                PoiInfo poiInfo = new PoiInfo();
                if (optJSONObject3 != null) {
                    poiInfo.name = optJSONObject3.optString(AIUIConstant.KEY_NAME);
                    poiInfo.address = optJSONObject3.optString("addr");
                    poiInfo.uid = optJSONObject3.optString(AIUIConstant.KEY_UID);
                    poiInfo.phoneNum = optJSONObject3.optString("tel");
                    poiInfo.type = POITYPE.fromInt(optJSONObject3.optInt("poiType"));
                    poiInfo.isPano = optJSONObject3.optInt("pano") == 1;
                    if (!(poiInfo.type == POITYPE.BUS_LINE || poiInfo.type == POITYPE.SUBWAY_LINE)) {
                        poiInfo.location = CoordUtil.decodeLocation(optJSONObject3.optString("geo"));
                    }
                    poiInfo.city = str2;
                    optJSONObject = optJSONObject3.optJSONObject("ext");
                    if (optJSONObject != null) {
                        if ("cater".equals(optJSONObject.optString("src_name")) && optJSONObject3.optJSONObject("detail_info") != null) {
                            poiInfo.hasCaterDetails = true;
                        }
                    }
                    arrayList.add(poiInfo);
                }
            }
            if (arrayList.size() > 0) {
                poiResult.setPoiInfo(arrayList);
            }
            JSONArray optJSONArray2 = optJSONObject2.optJSONArray("addrs");
            Object arrayList2 = new ArrayList();
            if (optJSONArray2 != null) {
                for (optInt = 0; optInt < optJSONArray2.length(); optInt++) {
                    optJSONObject2 = optJSONArray2.optJSONObject(optInt);
                    PoiAddrInfo poiAddrInfo = new PoiAddrInfo();
                    if (optJSONObject2 != null) {
                        poiAddrInfo.name = optJSONObject2.optString(AIUIConstant.KEY_NAME);
                        poiAddrInfo.address = optJSONObject2.optString("addr");
                        poiAddrInfo.location = CoordUtil.decodeLocation(optJSONObject2.optString("geo"));
                        arrayList2.add(poiAddrInfo);
                    }
                }
            }
            if (arrayList2.size() > 0) {
                poiResult.setAddrInfo(arrayList2);
                poiResult.setHasAddrInfo(true);
            }
            if (arrayList2.size() <= 0 && arrayList.size() <= 0) {
                return false;
            }
            poiResult.error = ERRORNO.NO_ERROR;
            return true;
        } catch (JSONException e) {
            e.printStackTrace();
            return false;
        }
    }

    private boolean b(String str, PoiResult poiResult) {
        if (str == null || str.equals("") || poiResult == null) {
            return false;
        }
        try {
            JSONObject jSONObject = new JSONObject(str);
            if (jSONObject == null) {
                return false;
            }
            JSONObject optJSONObject = jSONObject.optJSONObject(SpeechUtility.TAG_RESOURCE_RESULT);
            jSONObject = jSONObject.optJSONObject("traffic_citys");
            if (optJSONObject == null || jSONObject == null || optJSONObject.optInt("type") != 7 || optJSONObject.optInt(ParamStr.RET_RES_ERROR) != 0) {
                return false;
            }
            JSONArray optJSONArray = jSONObject.optJSONArray("contents");
            if (optJSONArray == null || optJSONArray.length() <= 0) {
                return false;
            }
            List arrayList = new ArrayList();
            for (int i = 0; i < optJSONArray.length(); i++) {
                JSONObject optJSONObject2 = optJSONArray.optJSONObject(i);
                if (optJSONObject2 != null) {
                    CityInfo cityInfo = new CityInfo();
                    cityInfo.num = optJSONObject2.optInt("num");
                    cityInfo.city = optJSONObject2.optString(AIUIConstant.KEY_NAME);
                    arrayList.add(cityInfo);
                }
            }
            if (arrayList.size() <= 0) {
                return false;
            }
            poiResult.setSuggestCityList(arrayList);
            poiResult.error = ERRORNO.AMBIGUOUS_KEYWORD;
            return true;
        } catch (JSONException e) {
            e.printStackTrace();
            return false;
        }
    }

    public void a(String str) {
        boolean z = false;
        SearchResult poiResult = new PoiResult();
        if (str == null || str.equals("")) {
            poiResult.error = ERRORNO.RESULT_NOT_FOUND;
            this.a.a(poiResult);
            return;
        }
        try {
            JSONObject jSONObject = new JSONObject(str);
            if (jSONObject.has("SDK_InnerError")) {
                jSONObject = jSONObject.optJSONObject("SDK_InnerError");
                if (jSONObject.has("PermissionCheckError")) {
                    poiResult.error = ERRORNO.PERMISSION_UNFINISHED;
                    this.a.a(poiResult);
                    return;
                } else if (jSONObject.has("httpStateError")) {
                    String optString = jSONObject.optString("httpStateError");
                    if (optString.equals("NETWORK_ERROR")) {
                        poiResult.error = ERRORNO.NETWORK_ERROR;
                    } else if (optString.equals("REQUEST_ERROR")) {
                        poiResult.error = ERRORNO.REQUEST_ERROR;
                    } else {
                        poiResult.error = ERRORNO.SEARCH_SERVER_INTERNAL_ERROR;
                    }
                    this.a.a(poiResult);
                    return;
                }
            }
            SearchType searchType = this.b;
            if (SearchType.POI_IN_CITY_SEARCH == a()) {
                z = a(str, poiResult, false);
            }
            if (!(z || b(str, poiResult) || a(str, poiResult))) {
                poiResult.error = ERRORNO.RESULT_NOT_FOUND;
            }
            this.a.a(poiResult);
        } catch (Exception e) {
            poiResult.error = ERRORNO.RESULT_NOT_FOUND;
            this.a.a(poiResult);
        }
    }
}
