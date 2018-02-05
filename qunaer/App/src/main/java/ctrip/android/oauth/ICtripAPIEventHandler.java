package ctrip.android.oauth;

import ctrip.android.oauth.model.CtripBaseRequest;
import ctrip.android.oauth.model.CtripBaseResponse;

public interface ICtripAPIEventHandler {
    void onRequest(CtripBaseRequest ctripBaseRequest);

    void onResponse(CtripBaseResponse ctripBaseResponse);
}
