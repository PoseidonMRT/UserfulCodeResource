package ctrip.android.oauth;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import ctrip.android.oauth.component.CtripAuthRequestParam;
import ctrip.android.oauth.component.CtripWebSdkBrowser;
import ctrip.android.oauth.model.auth.SendAuthRequest;
import ctrip.android.oauth.net.CtripParameters;
import ctrip.android.oauth.utils.LogUtils;

class CtripWebAuthHandler {
    private static final String TAG = CtripWebAuthHandler.class.getName();
    private Context mContext;
    private SendAuthRequest mRequest;

    public CtripWebAuthHandler(Context context, SendAuthRequest sendAuthRequest) {
        this.mContext = context;
        this.mRequest = sendAuthRequest;
    }

    public void anthorize() {
        CtripParameters ctripParameters = new CtripParameters(this.mRequest.getAppId());
        ctripParameters.put("client_id", this.mRequest.getAppId());
        ctripParameters.put("redirect_uri", this.mRequest.getRedirectUrl());
        ctripParameters.put("response_type", "code");
        ctripParameters.put("display", "mobile");
        ctripParameters.put("scope", this.mRequest.getScope());
        ctripParameters.put("scenetype", this.mRequest.getSceneType());
        ctripParameters.put("state", this.mRequest.getState());
        ctripParameters.put("pretoken", this.mRequest.getPretoken());
        ctripParameters.put("pretokentype", this.mRequest.getPretokenType());
        ctripParameters.put("thirdcode", this.mRequest.getThirdCode());
        String str = "";
        if (CtripAPIFactory.ENV_CODE == 1) {
            str = BuildConfig.TEST_AUTHORIZE_URL;
        } else if (CtripAPIFactory.ENV_CODE == 2) {
            str = BuildConfig.PRD_AUTHORIZE_URL;
        }
        str = str + ctripParameters.encodeUrl();
        LogUtils.e("authorizeUrl", str);
        CtripAuthRequestParam ctripAuthRequestParam = new CtripAuthRequestParam(this.mContext);
        ctripAuthRequestParam.setAuthInfo(this.mRequest);
        ctripAuthRequestParam.setPackageName(this.mContext.getPackageName());
        ctripAuthRequestParam.setUrl(str);
        Bundle createRequestParamBundle = ctripAuthRequestParam.createRequestParamBundle();
        Intent intent = new Intent(this.mContext, CtripWebSdkBrowser.class);
        intent.putExtras(createRequestParamBundle);
        this.mContext.startActivity(intent);
    }
}
