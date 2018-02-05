package ctrip.android.oauth.component;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;

public abstract class CtripBrowserRequestParamBase {
    protected static final String EXTRA_KEY_URL = "key_url";
    protected Context mContext;
    protected String mUrl;

    public abstract void execRequest(Activity activity, int i);

    protected abstract void onCreateRequestParamBundle(Bundle bundle);

    protected abstract void onSetupRequestParam(Bundle bundle);

    public CtripBrowserRequestParamBase(Context context) {
        this.mContext = context.getApplicationContext();
    }

    public void setupRequestParam(Bundle bundle) {
        this.mUrl = bundle.getString(EXTRA_KEY_URL);
        onSetupRequestParam(bundle);
    }

    public Bundle createRequestParamBundle() {
        Bundle bundle = new Bundle();
        if (!TextUtils.isEmpty(this.mUrl)) {
            bundle.putString(EXTRA_KEY_URL, this.mUrl);
        }
        onCreateRequestParamBundle(bundle);
        return bundle;
    }

    public void setUrl(String str) {
        this.mUrl = str;
    }

    public String getUrl() {
        return this.mUrl;
    }
}
