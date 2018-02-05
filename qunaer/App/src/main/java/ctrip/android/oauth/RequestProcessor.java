package ctrip.android.oauth;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import ctrip.android.oauth.utils.LogUtils;

public class RequestProcessor {
    private static final String TAG = "RequestProcessor";

    public class Request {
        public Bundle bundle;
        public int flags = -1;
        public String messageContent;
        public String targetClassName;
        public String targetPackageName;
    }

    public static boolean startActivity(Context context, Request request) {
        if (context == null || request == null) {
            LogUtils.e(TAG, "send fail, invalid argument");
            return false;
        } else if (TextUtils.isEmpty(request.targetPackageName)) {
            LogUtils.e(TAG, "send fail, invalid targetPkgName, targetPkgName = " + request.targetPackageName);
            return false;
        } else {
            LogUtils.d(TAG, "send, targetPkgName = " + request.targetPackageName + ", targetClassName = " + request.targetClassName);
            Intent intent = new Intent();
            intent.setClassName(request.targetPackageName, request.targetClassName);
            intent.setData(Uri.parse("ctrip://oauth"));
            if (request.bundle != null) {
                intent.putExtras(request.bundle);
            }
            intent.putExtra("_mmessage_sdkVersion", 0);
            intent.putExtra("_mmessage_appPackage", context.getPackageName());
            intent.putExtra("_mmessage_content", request.messageContent);
            intent.putExtra("ctrip_token_key", "ctrip.android.oauth.token");
            intent.putExtra("_mmessage_checksum", "");
            if (request.flags == -1) {
                intent.addFlags(268435456).addFlags(134217728);
            } else {
                intent.setFlags(request.flags);
            }
            try {
                context.startActivity(intent);
                LogUtils.e(TAG, "send ctrip message, intent=" + intent);
                return true;
            } catch (Exception e) {
                LogUtils.e(TAG, "send fail" + e.getStackTrace());
                return false;
            }
        }
    }
}
