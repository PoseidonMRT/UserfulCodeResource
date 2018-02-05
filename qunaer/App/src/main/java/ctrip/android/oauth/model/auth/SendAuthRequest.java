package ctrip.android.oauth.model.auth;

import android.os.Bundle;
import ctrip.android.oauth.model.CtripBaseRequest;

public class SendAuthRequest extends CtripBaseRequest {
    private static final int TYPE = 1;
    public static String extJson;
    private String appId;
    private String pretoken;
    private String pretokenType;
    private String redirectUrl;
    private String sceneType;
    private String scope;
    private String state;
    private String thirdCode;

    public SendAuthRequest(String str, String str2) {
        this(str, str2, "", "", "", "", "", "", "");
    }

    public SendAuthRequest(String str, String str2, String str3) {
        this(str, str2, str3, "", "", "", "", "", "");
    }

    public SendAuthRequest(String str, String str2, String str3, String str4) {
        this(str, str2, str3, str4, "", "", "", "", "");
    }

    public SendAuthRequest(String str, String str2, String str3, String str4, String str5, String str6, String str7, String str8, String str9) {
        this.appId = str;
        this.redirectUrl = str2;
        this.scope = str3;
        this.state = str4;
        this.sceneType = str5;
        this.pretoken = str6;
        this.pretokenType = str7;
        this.thirdCode = str8;
        extJson = str9;
    }

    public SendAuthRequest(Bundle bundle) {
        fromBundle(bundle);
    }

    public String getAppId() {
        return this.appId;
    }

    public String getRedirectUrl() {
        return this.redirectUrl;
    }

    public String getScope() {
        return this.scope;
    }

    public String getState() {
        return this.state;
    }

    public String getSceneType() {
        return this.sceneType;
    }

    public String getPretoken() {
        return this.pretoken;
    }

    public String getPretokenType() {
        return this.pretokenType;
    }

    public String getThirdCode() {
        return this.thirdCode;
    }

    public int getType() {
        return 1;
    }

    public void fromBundle(Bundle bundle) {
        super.fromBundle(bundle);
        this.appId = bundle.getString("_ctripapi_sendauth_request_appid");
        this.redirectUrl = bundle.getString("_ctripapi_sendauth_request_redirect_url");
        this.scope = bundle.getString("_ctripapi_sendauth_request_scope");
        this.state = bundle.getString("_ctripapi_sendauth_request_state");
        this.sceneType = bundle.getString("_ctripapi_sendauth_request_scenetype");
        this.pretoken = bundle.getString("_ctripapi_sendauth_request_pretoken");
        this.pretokenType = bundle.getString("_ctripapi_sendauth_request_pretokentype");
        this.thirdCode = bundle.getString("_ctripapi_sendauth_request_thirdCode");
        extJson = bundle.getString("_ctripapi_sendauth_request_extJson");
    }

    public void toBundle(Bundle bundle) {
        super.toBundle(bundle);
        bundle.putString("_ctripapi_sendauth_request_appid", this.appId);
        bundle.putString("_ctripapi_sendauth_request_redirect_url", this.redirectUrl);
        bundle.putString("_ctripapi_sendauth_request_scope", this.scope);
        bundle.putString("_ctripapi_sendauth_request_state", this.state);
        bundle.putString("_ctripapi_sendauth_request_scenetype", this.sceneType);
        bundle.putString("_ctripapi_sendauth_request_pretoken", this.pretoken);
        bundle.putString("_ctripapi_sendauth_request_pretokentype", this.pretokenType);
        bundle.putString("_ctripapi_sendauth_request_thirdCode", this.thirdCode);
        bundle.putString("_ctripapi_sendauth_request_extJson", extJson);
    }

    public boolean checkArgs() {
        return true;
    }
}
