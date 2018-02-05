package ctrip.android.oauth;

import android.content.Intent;
import ctrip.android.oauth.model.CtripBaseRequest;
import ctrip.android.oauth.model.CtripBaseResponse;

public interface ICtripAPI {
    void detach();

    boolean handleIntent(Intent intent, ICtripAPIEventHandler iCtripAPIEventHandler);

    boolean isCtripAppInstalled();

    boolean isCtripAppSupportAPI();

    boolean openCtripApp();

    boolean registerApp(String str);

    boolean sendCtripOAuthReq(CtripBaseRequest ctripBaseRequest);

    boolean sendCtripOAuthRsp(CtripBaseResponse ctripBaseResponse);

    void setEnv(int i);

    void unregisterApp();
}
