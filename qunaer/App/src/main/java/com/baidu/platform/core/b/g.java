package com.baidu.platform.core.b;

import android.net.http.Headers;
import com.baidu.mapapi.CoordType;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.core.SearchResult.ERRORNO;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult.AddressComponent;
import com.baidu.platform.base.f;
import com.baidu.platform.comapi.util.CoordTrans;
import com.iflytek.aiui.AIUIConstant;
import com.iflytek.cloud.SpeechUtility;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class g extends f {
    private AddressComponent a(JSONObject jSONObject, String str) {
        if (jSONObject == null || str == null || "".equals(str)) {
            return null;
        }
        JSONObject optJSONObject = jSONObject.optJSONObject(str);
        if (optJSONObject == null) {
            return null;
        }
        AddressComponent addressComponent = new AddressComponent();
        addressComponent.city = optJSONObject.optString("city");
        addressComponent.district = optJSONObject.optString("district");
        addressComponent.province = optJSONObject.optString("province");
        addressComponent.adcode = optJSONObject.optInt("adcode");
        addressComponent.street = optJSONObject.optString("street");
        addressComponent.streetNumber = optJSONObject.optString("street_number");
        addressComponent.countryName = optJSONObject.optString("country");
        addressComponent.countryCode = optJSONObject.optInt("country_code");
        return addressComponent;
    }

    private List<PoiInfo> a(JSONObject jSONObject, String str, String str2) {
        if (jSONObject == null || str == null || "".equals(str)) {
            return null;
        }
        JSONArray optJSONArray = jSONObject.optJSONArray(str);
        if (optJSONArray == null) {
            return null;
        }
        List<PoiInfo> arrayList = new ArrayList();
        for (int i = 0; i < optJSONArray.length(); i++) {
            JSONObject optJSONObject = optJSONArray.optJSONObject(i);
            if (optJSONObject != null) {
                PoiInfo poiInfo = new PoiInfo();
                poiInfo.address = optJSONObject.optString("addr");
                poiInfo.phoneNum = optJSONObject.optString("tel");
                poiInfo.uid = optJSONObject.optString(AIUIConstant.KEY_UID);
                poiInfo.postCode = optJSONObject.optString("zip");
                poiInfo.name = optJSONObject.optString(AIUIConstant.KEY_NAME);
                poiInfo.location = b(optJSONObject, "point");
                poiInfo.city = str2;
                arrayList.add(poiInfo);
            }
        }
        return arrayList;
    }

    private boolean a(String str, ReverseGeoCodeResult reverseGeoCodeResult) {
        if (str != null) {
            try {
                if (str.length() > 0) {
                    JSONObject jSONObject = new JSONObject(str);
                    if (jSONObject == null) {
                        reverseGeoCodeResult.error = ERRORNO.RESULT_NOT_FOUND;
                        return false;
                    }
                    int optInt = jSONObject.optInt("status");
                    if (optInt != 0) {
                        switch (optInt) {
                            case 1:
                                reverseGeoCodeResult.error = ERRORNO.SEARCH_SERVER_INTERNAL_ERROR;
                                return false;
                            case 2:
                                reverseGeoCodeResult.error = ERRORNO.SEARCH_OPTION_ERROR;
                                return false;
                            default:
                                reverseGeoCodeResult.error = ERRORNO.RESULT_NOT_FOUND;
                                return false;
                        }
                    } else if (a(jSONObject, reverseGeoCodeResult)) {
                        return true;
                    } else {
                        reverseGeoCodeResult.error = ERRORNO.RESULT_NOT_FOUND;
                        return false;
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
                reverseGeoCodeResult.error = ERRORNO.RESULT_NOT_FOUND;
                return false;
            }
        }
        reverseGeoCodeResult.error = ERRORNO.SEARCH_SERVER_INTERNAL_ERROR;
        return false;
    }

    private boolean a(JSONObject jSONObject, ReverseGeoCodeResult reverseGeoCodeResult) {
        if (jSONObject == null) {
            return false;
        }
        JSONObject optJSONObject = jSONObject.optJSONObject(SpeechUtility.TAG_RESOURCE_RESULT);
        if (optJSONObject == null) {
            return false;
        }
        reverseGeoCodeResult.setCityCode(optJSONObject.optInt("cityCode"));
        reverseGeoCodeResult.setAddress(optJSONObject.optString("formatted_address"));
        reverseGeoCodeResult.setBusinessCircle(optJSONObject.optString("business"));
        reverseGeoCodeResult.setAddressDetail(a(optJSONObject, "addressComponent"));
        reverseGeoCodeResult.setLocation(c(optJSONObject, Headers.LOCATION));
        String str = "";
        if (reverseGeoCodeResult.getAddressDetail() != null) {
            str = reverseGeoCodeResult.getAddressDetail().city;
        }
        reverseGeoCodeResult.setPoiList(a(optJSONObject, "pois", str));
        reverseGeoCodeResult.setSematicDescription(optJSONObject.optString("sematic_description"));
        reverseGeoCodeResult.error = ERRORNO.NO_ERROR;
        return true;
    }

    private LatLng b(JSONObject jSONObject, String str) {
        if (jSONObject == null || str == null || "".equals(str)) {
            return null;
        }
        JSONObject optJSONObject = jSONObject.optJSONObject(str);
        if (optJSONObject == null) {
            return null;
        }
        LatLng latLng = new LatLng(optJSONObject.optDouble(MapViewConstants.ATTR_Y), optJSONObject.optDouble(MapViewConstants.ATTR_X));
        return SDKInitializer.getCoordType() == CoordType.GCJ02 ? CoordTrans.baiduToGcj(latLng) : latLng;
    }

    private LatLng c(JSONObject jSONObject, String str) {
        if (jSONObject == null || str == null || "".equals(str)) {
            return null;
        }
        JSONObject optJSONObject = jSONObject.optJSONObject(str);
        if (optJSONObject == null) {
            return null;
        }
        LatLng latLng = new LatLng(optJSONObject.optDouble("lat"), optJSONObject.optDouble("lng"));
        return SDKInitializer.getCoordType() == CoordType.GCJ02 ? CoordTrans.baiduToGcj(latLng) : latLng;
    }

    public void a(String str) {
        ReverseGeoCodeResult reverseGeoCodeResult = new ReverseGeoCodeResult();
        if (str == null || str.equals("")) {
            reverseGeoCodeResult.error = ERRORNO.RESULT_NOT_FOUND;
            this.a.a(reverseGeoCodeResult);
            return;
        }
        try {
            JSONObject jSONObject = new JSONObject(str);
            if (jSONObject.has("SDK_InnerError")) {
                jSONObject = jSONObject.optJSONObject("SDK_InnerError");
                if (jSONObject.has("PermissionCheckError")) {
                    reverseGeoCodeResult.error = ERRORNO.PERMISSION_UNFINISHED;
                    this.a.a(reverseGeoCodeResult);
                    return;
                } else if (jSONObject.has("httpStateError")) {
                    String optString = jSONObject.optString("httpStateError");
                    if (optString.equals("NETWORK_ERROR")) {
                        reverseGeoCodeResult.error = ERRORNO.NETWORK_ERROR;
                    } else if (optString.equals("REQUEST_ERROR")) {
                        reverseGeoCodeResult.error = ERRORNO.REQUEST_ERROR;
                    } else {
                        reverseGeoCodeResult.error = ERRORNO.SEARCH_SERVER_INTERNAL_ERROR;
                    }
                    this.a.a(reverseGeoCodeResult);
                    return;
                }
            }
            if (!a(str, reverseGeoCodeResult, true)) {
                a(str, reverseGeoCodeResult);
            }
            this.a.a(reverseGeoCodeResult);
        } catch (Exception e) {
            reverseGeoCodeResult.error = ERRORNO.RESULT_NOT_FOUND;
            this.a.a(reverseGeoCodeResult);
        }
    }
}
