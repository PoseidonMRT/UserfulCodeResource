package com.baidu.platform.core.e;

import com.baidu.mapapi.search.core.SearchResult.ERRORNO;
import com.baidu.mapapi.search.share.ShareUrlResult;
import org.json.JSONException;
import org.json.JSONObject;

public class f extends com.baidu.platform.base.f {
    public void a(String str) {
        ShareUrlResult shareUrlResult = new ShareUrlResult();
        if (str == null || str.equals("")) {
            shareUrlResult.error = ERRORNO.RESULT_NOT_FOUND;
            this.a.a(shareUrlResult);
            return;
        }
        try {
            JSONObject jSONObject = new JSONObject(str);
            if (jSONObject.has("SDK_InnerError")) {
                jSONObject = jSONObject.optJSONObject("SDK_InnerError");
                if (jSONObject.has("PermissionCheckError")) {
                    shareUrlResult.error = ERRORNO.PERMISSION_UNFINISHED;
                    this.a.a(shareUrlResult);
                    return;
                } else if (jSONObject.has("httpStateError")) {
                    String optString = jSONObject.optString("httpStateError");
                    if (optString.equals("NETWORK_ERROR")) {
                        shareUrlResult.error = ERRORNO.NETWORK_ERROR;
                    } else if (optString.equals("REQUEST_ERROR")) {
                        shareUrlResult.error = ERRORNO.REQUEST_ERROR;
                    } else {
                        shareUrlResult.error = ERRORNO.SEARCH_SERVER_INTERNAL_ERROR;
                    }
                    this.a.a(shareUrlResult);
                    return;
                }
            }
            if (str == null) {
                shareUrlResult.error = ERRORNO.RESULT_NOT_FOUND;
            } else {
                try {
                    jSONObject = new JSONObject(str);
                    if (jSONObject != null) {
                        if (jSONObject.optString("state").equals("success")) {
                            shareUrlResult.setUrl(jSONObject.optString("url"));
                            shareUrlResult.setType(a().ordinal());
                            shareUrlResult.error = ERRORNO.NO_ERROR;
                        } else {
                            shareUrlResult.error = ERRORNO.RESULT_NOT_FOUND;
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    shareUrlResult.error = ERRORNO.RESULT_NOT_FOUND;
                }
            }
            this.a.a(shareUrlResult);
        } catch (Exception e2) {
            shareUrlResult.error = ERRORNO.RESULT_NOT_FOUND;
            this.a.a(shareUrlResult);
        }
    }
}
