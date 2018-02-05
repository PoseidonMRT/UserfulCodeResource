package ctrip.android.oauth;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import ctrip.android.oauth.RequestProcessor.Request;
import ctrip.android.oauth.model.CtripBaseRequest;
import ctrip.android.oauth.model.CtripBaseResponse;
import ctrip.android.oauth.model.auth.SendAuthRequest;
import ctrip.android.oauth.model.auth.SendAuthResponse;
import ctrip.android.oauth.utils.CtripApiUtils;
import ctrip.android.oauth.utils.LogUtils;
import java.security.InvalidParameterException;

final class CtripApiImplV1 implements ICtripAPI {
    private static final String CHECKSUM_EXTRA = "cTrIpOaUtH";
    private static final String CTRIP_APP_PACKAGE_NAME = "ctrip.android.view";
    private static final String TAG = "Ctrip.OAuth.SDK.CtripApiImplV1";
    private String appId;
    private boolean checkSignature = false;
    private Context context;
    private boolean detached = false;

    CtripApiImplV1(Context context, String str, boolean z) {
        this.context = context;
        this.appId = str;
        this.checkSignature = z;
    }

    public void setEnv(int i) {
        if (i == 1 || i == 2) {
            CtripAPIFactory.ENV_CODE = i;
            return;
        }
        throw new InvalidParameterException("unknown env code, env code must be EnvCode.TEST or EnvCode.PRD!");
    }

    public boolean registerApp(String str) {
        return true;
    }

    public void unregisterApp() {
    }

    public boolean handleIntent(Intent intent, ICtripAPIEventHandler iCtripAPIEventHandler) {
        if (!CtripApiUtils.isIntentFromCtrip(intent, "ctrip.android.oauth.token")) {
            LogUtils.e(TAG, "handleIntent fail, intent not from ctrip app msg");
            return false;
        } else if (this.detached) {
            throw new IllegalStateException("handleIntent fail, CtripApiImplV1 has been detached");
        } else {
            intent.getStringExtra("_mmessage_content");
            intent.getIntExtra("_mmessage_sdkVersion", 0);
            String stringExtra = intent.getStringExtra("_mmessage_appPackage");
            if (stringExtra == null || stringExtra.length() == 0) {
                LogUtils.e(TAG, "invalid argument");
                return false;
            }
            int intExtra = intent.getIntExtra("_ctripapi_command_type", 0);
            switch (intExtra) {
                case 1:
                    iCtripAPIEventHandler.onResponse(new SendAuthResponse(intent.getExtras()));
                    return true;
                default:
                    LogUtils.e(TAG, "unknown cmd = " + intExtra);
                    return false;
            }
        }
    }

    public boolean isCtripAppInstalled() {
        boolean z = false;
        if (this.detached) {
            throw new IllegalStateException("isCtripAppInstalled fail, CtripApiImplV1 has been detached");
        }
        try {
            PackageInfo packageInfo = this.context.getPackageManager().getPackageInfo(CTRIP_APP_PACKAGE_NAME, 64);
            if (packageInfo != null) {
                z = CtripApiUtils.validateAppSignature(this.context, packageInfo.signatures, this.checkSignature);
            }
        } catch (NameNotFoundException e) {
        }
        return z;
    }

    public boolean isCtripAppSupportAPI() {
        if (this.detached) {
            throw new IllegalStateException("isCtripAppSupportAPI fail, CtripApiImplV1 has been detached");
        }
        try {
            PackageInfo packageInfo = this.context.getPackageManager().getPackageInfo(CTRIP_APP_PACKAGE_NAME, 64);
            if (packageInfo == null) {
                return false;
            }
            String[] split = packageInfo.versionName.split("\\.");
            if (split.length < 2) {
                return false;
            }
            if (Integer.parseInt(split[0]) > 6) {
                return true;
            }
            if (Integer.parseInt(split[0]) != 6 || Integer.parseInt(split[1]) < 15) {
                return false;
            }
            return true;
        } catch (NameNotFoundException e) {
            return false;
        }
    }

    public boolean openCtripApp() {
        if (this.detached) {
            throw new IllegalStateException("openCtripApp fail, CtripApiImplV1 has been detached");
        } else if (!isCtripAppInstalled()) {
            return false;
        } else {
            try {
                this.context.startActivity(this.context.getPackageManager().getLaunchIntentForPackage(CTRIP_APP_PACKAGE_NAME));
                return true;
            } catch (Exception e) {
                return false;
            }
        }
    }

    public boolean sendCtripOAuthReq(CtripBaseRequest ctripBaseRequest) {
        if (this.detached) {
            throw new IllegalStateException("sendReq fail, CtripApiImplV1 has been detached");
        } else if (!CtripApiUtils.validateAppSignatureForPackage(this.context, CTRIP_APP_PACKAGE_NAME, this.checkSignature)) {
            LogUtils.e(TAG, "sendReq failed for ctrip app signature check failed");
            return false;
        } else if (ctripBaseRequest.checkArgs()) {
            LogUtils.e(TAG, "sendReq, request type = " + ctripBaseRequest.getType());
            Bundle bundle = new Bundle();
            ctripBaseRequest.toBundle(bundle);
            if ((ctripBaseRequest instanceof SendAuthRequest) && "sence_pay_card".equalsIgnoreCase(((SendAuthRequest) ctripBaseRequest).getSceneType())) {
                if (!(ctripBaseRequest instanceof SendAuthRequest)) {
                    return false;
                }
                new CtripWebAuthHandler(this.context, (SendAuthRequest) ctripBaseRequest).anthorize();
                return true;
            } else if (isCtripAppInstalled() && isCtripAppSupportAPI()) {
                Request request = new Request();
                request.bundle = bundle;
                request.messageContent = "ctrip://sendreq?appid=" + this.appId;
                request.targetPackageName = CTRIP_APP_PACKAGE_NAME;
                request.targetClassName = "ctrip.android.view.view.CtripBootActivity";
                return RequestProcessor.startActivity(this.context, request);
            } else if (!(ctripBaseRequest instanceof SendAuthRequest)) {
                return false;
            } else {
                new CtripWebAuthHandler(this.context, (SendAuthRequest) ctripBaseRequest).anthorize();
                return true;
            }
        } else {
            LogUtils.e(TAG, "sendReq checkArgs fail");
            return false;
        }
    }

    public boolean sendCtripOAuthRsp(CtripBaseResponse ctripBaseResponse) {
        return false;
    }

    public void detach() {
        this.detached = true;
        this.context = null;
        this.appId = null;
        this.checkSignature = false;
    }

    private byte[] getChecksum(String str, int i, String str2) {
        StringBuffer stringBuffer = new StringBuffer();
        if (str != null) {
            stringBuffer.append(str);
        }
        stringBuffer.append(i);
        stringBuffer.append(str2);
        stringBuffer.append(CHECKSUM_EXTRA);
        return CtripApiUtils.hexdigest(stringBuffer.toString().substring(1, 10).getBytes()).getBytes();
    }

    private boolean checkSumConsistent(byte[] bArr, byte[] bArr2) {
        if (bArr == null || bArr.length == 0 || bArr2 == null || bArr2.length == 0) {
            LogUtils.e(TAG, "checkSumConsistent fail, invalid arguments");
            return false;
        } else if (bArr.length != bArr2.length) {
            LogUtils.e(TAG, "checkSumConsistent fail, length is different");
            return false;
        } else {
            for (int i = 0; i < bArr.length; i++) {
                if (bArr[i] != bArr2[i]) {
                    return false;
                }
            }
            return true;
        }
    }
}
