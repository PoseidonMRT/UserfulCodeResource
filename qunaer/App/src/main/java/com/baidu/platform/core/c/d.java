package com.baidu.platform.core.c;

import android.net.http.Headers;
import com.baidu.mapapi.CoordType;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.SearchResult.ERRORNO;
import com.baidu.mapapi.search.poi.PoiDetailResult;
import com.baidu.platform.base.f;
import com.baidu.platform.comapi.util.CoordTrans;
import com.iflytek.aiui.AIUIConstant;
import com.iflytek.cloud.SpeechUtility;
import org.json.JSONException;
import org.json.JSONObject;

public class d extends f {
    private boolean a(String str, PoiDetailResult poiDetailResult) {
        try {
            JSONObject jSONObject = new JSONObject(str);
            if (jSONObject.optInt("status") != 0) {
                return false;
            }
            jSONObject = jSONObject.optJSONObject(SpeechUtility.TAG_RESOURCE_RESULT);
            if (jSONObject == null) {
                return false;
            }
            poiDetailResult.name = jSONObject.optString(AIUIConstant.KEY_NAME);
            JSONObject optJSONObject = jSONObject.optJSONObject(Headers.LOCATION);
            if (optJSONObject != null) {
                double optDouble = optJSONObject.optDouble("lat");
                double optDouble2 = optJSONObject.optDouble("lng");
                if (SDKInitializer.getCoordType() == CoordType.GCJ02) {
                    poiDetailResult.location = CoordTrans.baiduToGcj(new LatLng(optDouble, optDouble2));
                } else {
                    poiDetailResult.location = new LatLng(optDouble, optDouble2);
                }
            }
            poiDetailResult.address = jSONObject.optString("address");
            poiDetailResult.telephone = jSONObject.optString("telephone");
            poiDetailResult.uid = jSONObject.optString(AIUIConstant.KEY_UID);
            jSONObject = jSONObject.optJSONObject("detail_info");
            if (jSONObject != null) {
                poiDetailResult.tag = jSONObject.optString(AIUIConstant.KEY_TAG);
                poiDetailResult.detailUrl = jSONObject.optString("detail_url");
                poiDetailResult.type = jSONObject.optString("type");
                poiDetailResult.price = jSONObject.optDouble("price", 0.0d);
                poiDetailResult.overallRating = jSONObject.optDouble("overall_rating", 0.0d);
                poiDetailResult.tasteRating = jSONObject.optDouble("taste_rating", 0.0d);
                poiDetailResult.serviceRating = jSONObject.optDouble("service_rating", 0.0d);
                poiDetailResult.environmentRating = jSONObject.optDouble("environment_rating", 0.0d);
                poiDetailResult.facilityRating = jSONObject.optDouble("facility_rating", 0.0d);
                poiDetailResult.hygieneRating = jSONObject.optDouble("hygiene_rating", 0.0d);
                poiDetailResult.technologyRating = jSONObject.optDouble("technology_rating", 0.0d);
                poiDetailResult.imageNum = jSONObject.optInt("image_num");
                poiDetailResult.grouponNum = jSONObject.optInt("groupon_num");
                poiDetailResult.commentNum = jSONObject.optInt("comment_num");
                poiDetailResult.favoriteNum = jSONObject.optInt("favorite_num");
                poiDetailResult.checkinNum = jSONObject.optInt("checkin_num");
                poiDetailResult.shopHours = jSONObject.optString("shop_hours");
            }
            poiDetailResult.error = ERRORNO.NO_ERROR;
            return true;
        } catch (JSONException e) {
            e.printStackTrace();
            return false;
        }
    }

    public void a(String str) {
        PoiDetailResult poiDetailResult = new PoiDetailResult();
        if (str == null || str.equals("")) {
            poiDetailResult.error = ERRORNO.RESULT_NOT_FOUND;
            this.a.a(poiDetailResult);
            return;
        }
        try {
            JSONObject jSONObject = new JSONObject(str);
            if (jSONObject.has("SDK_InnerError")) {
                jSONObject = jSONObject.optJSONObject("SDK_InnerError");
                if (jSONObject.has("PermissionCheckError")) {
                    poiDetailResult.error = ERRORNO.PERMISSION_UNFINISHED;
                    this.a.a(poiDetailResult);
                    return;
                } else if (jSONObject.has("httpStateError")) {
                    String optString = jSONObject.optString("httpStateError");
                    if (optString.equals("NETWORK_ERROR")) {
                        poiDetailResult.error = ERRORNO.NETWORK_ERROR;
                    } else if (optString.equals("REQUEST_ERROR")) {
                        poiDetailResult.error = ERRORNO.REQUEST_ERROR;
                    } else {
                        poiDetailResult.error = ERRORNO.SEARCH_SERVER_INTERNAL_ERROR;
                    }
                    this.a.a(poiDetailResult);
                    return;
                }
            }
            if (!a(str, poiDetailResult)) {
                poiDetailResult.error = ERRORNO.RESULT_NOT_FOUND;
            }
            this.a.a(poiDetailResult);
        } catch (Exception e) {
            poiDetailResult.error = ERRORNO.RESULT_NOT_FOUND;
            this.a.a(poiDetailResult);
        }
    }
}
