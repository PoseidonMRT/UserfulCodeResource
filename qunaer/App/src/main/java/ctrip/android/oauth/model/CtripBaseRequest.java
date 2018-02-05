package ctrip.android.oauth.model;

import android.os.Bundle;

public abstract class CtripBaseRequest {
    public abstract boolean checkArgs();

    public abstract int getType();

    public void toBundle(Bundle bundle) {
        bundle.putInt("_ctripapi_command_type", getType());
    }

    public void fromBundle(Bundle bundle) {
    }
}
