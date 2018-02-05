package ctrip.android.oauth.model.auth;

import android.os.Bundle;
import ctrip.android.oauth.model.CtripBaseResponse;

public class SendAuthResponse extends CtripBaseResponse {
    private static final String TAG = "Ctrip.OAuth.SDK.SendAuthResponse";
    private static final int TYPE = 1;
    public String code;
    public String extJson;
    public String state;

    public SendAuthResponse(Bundle bundle) {
        fromBundle(bundle);
        this.extJson = SendAuthRequest.extJson;
    }

    public int getType() {
        return 1;
    }

    public void fromBundle(Bundle bundle) {
        super.fromBundle(bundle);
        this.state = bundle.getString("_ctripapi_sendauth_response_state");
        this.code = bundle.getString("_ctripapi_sendauth_response_code");
    }

    public void toBundle(Bundle bundle) {
        super.toBundle(bundle);
        bundle.putString("_ctripapi_sendauth_response_state", this.state);
        bundle.putString("_ctripapi_sendauth_response_code", this.code);
    }

    public boolean checkArgs() {
        return true;
    }

    public String toString() {
        return "errCode:" + this.errCode + ";errStr:" + this.errStr + ";code:" + this.code + ";state:" + this.state;
    }
}
