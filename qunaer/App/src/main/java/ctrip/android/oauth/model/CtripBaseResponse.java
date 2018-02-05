package ctrip.android.oauth.model;

import android.os.Bundle;

public abstract class CtripBaseResponse {
    public int errCode;
    public String errStr;

    public abstract boolean checkArgs();

    public abstract int getType();

    public void toBundle(Bundle bundle) {
        bundle.putInt("_ctripapi_command_type", getType());
        bundle.putInt("_ctripapi_baseresponse_errcode", this.errCode);
        bundle.putString("_ctripapi_baseresponse_errstr", this.errStr);
    }

    public void fromBundle(Bundle bundle) {
        this.errCode = bundle.getInt("_ctripapi_baseresponse_errcode");
        this.errStr = bundle.getString("_ctripapi_baseresponse_errstr");
    }
}
