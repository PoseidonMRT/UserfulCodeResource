package ctrip.android.oauth.component;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import ctrip.android.oauth.model.auth.SendAuthRequest;

public class CtripAuthRequestParam extends CtripBrowserRequestParamBase {
    public static final String EXTRA_KEY_AUTHINFO = "key_authinfo";
    public static final String EXTRA_KEY_PACKAGE_NAME = "key_package_name";
    private SendAuthRequest mAuthRequest;
    private String mPackageName;

    public CtripAuthRequestParam(Context context) {
        super(context);
    }

    protected void onSetupRequestParam(Bundle bundle) {
        Bundle bundle2 = bundle.getBundle(EXTRA_KEY_AUTHINFO);
        if (bundle2 != null) {
            this.mAuthRequest = new SendAuthRequest(bundle2);
        }
        this.mPackageName = bundle.getString(EXTRA_KEY_PACKAGE_NAME);
    }

    public void onCreateRequestParamBundle(Bundle bundle) {
        if (this.mAuthRequest != null) {
            Bundle bundle2 = new Bundle();
            this.mAuthRequest.toBundle(bundle2);
            bundle.putBundle(EXTRA_KEY_AUTHINFO, bundle2);
            bundle.putString(EXTRA_KEY_PACKAGE_NAME, this.mPackageName);
        }
    }

    public void execRequest(Activity activity, int i) {
    }

    public SendAuthRequest getAuthRequest() {
        return this.mAuthRequest;
    }

    public void setAuthInfo(SendAuthRequest sendAuthRequest) {
        this.mAuthRequest = sendAuthRequest;
    }

    public String getPackageName() {
        return this.mPackageName;
    }

    public void setPackageName(String str) {
        this.mPackageName = str;
    }
}
