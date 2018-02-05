package com.baidu.platform.core.b;

import com.baidu.mapapi.model.CoordUtil;
import com.baidu.mapapi.model.inner.GeoPoint;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.core.SearchResult.ERRORNO;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.platform.base.f;
import com.huawei.hwid.openapi.out.OutReturn.ParamStr;
import com.iflytek.aiui.AIUIConstant;
import com.iflytek.cloud.SpeechUtility;
import org.json.JSONException;
import org.json.JSONObject;

public class d extends f {
    private boolean a(String str, GeoCodeResult geoCodeResult) {
        try {
            JSONObject jSONObject = new JSONObject(str);
            if (jSONObject == null) {
                return false;
            }
            JSONObject optJSONObject = jSONObject.optJSONObject(SpeechUtility.TAG_RESOURCE_RESULT);
            if (optJSONObject == null || optJSONObject.optInt(ParamStr.RET_RES_ERROR) != 0) {
                return false;
            }
            jSONObject = jSONObject.optJSONObject(AIUIConstant.KEY_CONTENT);
            if (jSONObject == null) {
                return false;
            }
            optJSONObject = jSONObject.optJSONObject("coord");
            if (optJSONObject == null) {
                return false;
            }
            geoCodeResult.setLocation(CoordUtil.mc2ll(new GeoPoint((double) optJSONObject.optInt(MapViewConstants.ATTR_Y), (double) optJSONObject.optInt(MapViewConstants.ATTR_X))));
            geoCodeResult.setAddress(jSONObject.optString("wd"));
            geoCodeResult.error = ERRORNO.NO_ERROR;
            return true;
        } catch (JSONException e) {
            geoCodeResult.error = ERRORNO.RESULT_NOT_FOUND;
            e.printStackTrace();
        }
    }

    public void a(String str) {
        SearchResult geoCodeResult = new GeoCodeResult();
        if (str == null || str.equals("")) {
            geoCodeResult.error = ERRORNO.RESULT_NOT_FOUND;
            this.a.a(geoCodeResult);
            return;
        }
        try {
            JSONObject jSONObject = new JSONObject(str);
            if (jSONObject.has("SDK_InnerError")) {
                jSONObject = jSONObject.optJSONObject("SDK_InnerError");
                if (jSONObject.has("PermissionCheckError")) {
                    geoCodeResult.error = ERRORNO.PERMISSION_UNFINISHED;
                    this.a.a(geoCodeResult);
                    return;
                } else if (jSONObject.has("httpStateError")) {
                    String optString = jSONObject.optString("httpStateError");
                    if (optString.equals("NETWORK_ERROR")) {
                        geoCodeResult.error = ERRORNO.NETWORK_ERROR;
                    } else if (optString.equals("REQUEST_ERROR")) {
                        geoCodeResult.error = ERRORNO.REQUEST_ERROR;
                    } else {
                        geoCodeResult.error = ERRORNO.SEARCH_SERVER_INTERNAL_ERROR;
                    }
                    this.a.a(geoCodeResult);
                    return;
                }
            }
            if (!(a(str, geoCodeResult, false) || a(str, geoCodeResult))) {
                geoCodeResult.error = ERRORNO.RESULT_NOT_FOUND;
            }
            this.a.a(geoCodeResult);
        } catch (Exception e) {
            geoCodeResult.error = ERRORNO.RESULT_NOT_FOUND;
            this.a.a(geoCodeResult);
        }
    }
}
