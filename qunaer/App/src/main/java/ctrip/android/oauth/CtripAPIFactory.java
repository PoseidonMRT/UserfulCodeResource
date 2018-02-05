package ctrip.android.oauth;

import android.content.Context;
import android.util.Log;
import ctrip.android.oauth.utils.LogUtils;

public class CtripAPIFactory {
    public static int ENV_CODE = 1;
    private static final String TAG = "Ctrip.OAuth.SDK.CtripAPIFactory";

    static {
        LogUtils.sIsLogEnable = false;
        Log.e("CtripAPIFactory", "IsLogEnable : " + LogUtils.sIsLogEnable);
    }

    public static ICtripAPI createCtripAPI(Context context, String str) {
        return createCtripAPI(context, str, false);
    }

    public static ICtripAPI createCtripAPI(Context context, String str, boolean z) {
        LogUtils.d(TAG, "createCtripAPI, appId = " + str + ", checkSignature = " + z);
        return new CtripApiImplV1(context, str, z);
    }

    private CtripAPIFactory() {
        throw new RuntimeException(getClass().getSimpleName() + " should not be instantiated");
    }
}
