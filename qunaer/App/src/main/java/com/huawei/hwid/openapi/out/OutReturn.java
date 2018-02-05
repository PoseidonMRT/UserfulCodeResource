package com.huawei.hwid.openapi.out;

import android.os.Bundle;
import android.util.Log;

public class OutReturn {

    public class ParamStr {
        public static final String ACCESS_TOKEN = "access_token";
        public static final String EXPIRES_IN = "expires_in";
        public static final String Err_Info = "err_info";
        public static final String RET_CODE = "ret_code";
        public static final String RET_NSP_STATUS = "NSP_STATUS";
        public static final String RET_RES_CODE = "res_code";
        public static final String RET_RES_CONTENT = "res_content";
        public static final String RET_RES_ERROR = "error";
        public static final String RET_RES_HEAD = "res_head";
    }

    public class Ret_code {
        public static final int ACCOUNT_NON_LOGIN = 4;
        public static final int ERR_OPENGW_AT_400 = 907114002;
        public static final int ERR_OPENGW_AT_RSP_1101_FAILED = 907114008;
        public static final int ERR_OPENGW_AT_RSP_1102_FAILED = 907114009;
        public static final int ERR_OPENGW_AT_RSP_1107_FAILED = 907114010;
        public static final int ERR_OPENGW_AT_RSP_1202_FAILED = 907114011;
        public static final int ERR_OPENGW_AT_SSL_HANGSHAKE_FAILED = 907114012;
        public static final int ERR_OPENGW_AT_SSL_TIME_FAILED = 907114013;
        public static final int ERR_OPENGW_AT_TIMEOUT_FAILED = 907114005;
        public static final int ERR_OPENGW_FAILED_1201 = 907114000;
        public static final int ERR_OPENGW_FAILED_1401 = 907114001;
        public static final int ERR_OPENGW_RSP_FAILED = 2000;
        public static final int ERR_OPENGW_SSL_FAILED = 2002;
        public static final int ERR_OPENGW_ST_TIMEOUT_FAILED = 907114004;
        public static final int ERR_OPENGW_TIMEOUT_FAILED = 2003;
        public static final int ERR_OPENGW_TMPST_400 = 907114016;
        public static final int ERR_OPENGW_TMPST_SSL_FAILED = 907114007;
        public static final int ERR_OPENGW_USERINFO_400 = 907114003;
        public static final int ERR_OPENGW_USERINFO_RSP_102_FAILED = 907114020;
        public static final int ERR_OPENGW_USERINFO_RSP_105_FAILED = 907114018;
        public static final int ERR_OPENGW_USERINFO_RSP_1101_FAILED = 907114017;
        public static final int ERR_OPENGW_USERINFO_RSP_1107_FAILED = 907114019;
        public static final int ERR_OPENGW_USERINFO_SSL_HANGSHAKE_FAILED = 907114014;
        public static final int ERR_OPENGW_USERINFO_SSL_TIME_FAILED = 907114015;
        public static final int ERR_OPENGW_USERINFO_TIMEOUT_FAILED = 907114006;
        public static final int ERR_UP_400_FAILED = 1004;
        public static final int ERR_UP_FAILED = 1001;
        public static final int ERR_UP_SSL_FAILED = 1002;
        public static final int ERR_UP_SSL_TIME_FAILED = 2004;
        public static final int ERR_UP_TIMOUT_FAILED = 1003;
        public static final int FAILED = -1;
        public static final int NETWORK_ERR = 102;
        public static final int NETWORK_OPENGW_HTTP_ERR = 105;
        public static final int NETWORK_OPENGW_SSL_ERR = 104;
        public static final int NETWORK_REQUEST_TIMEOUT = 103;
        public static final int SERVER_RSP_FAILED = 1000;
        public static final int SERVICETOKEN_INVALID = 6;
        public static final int SIGNATURE_INVALID = 5;
        public static final int SUCCESS = 1;
        public static final int SYSTEM_AUTH_FAILED = 101;
        public static final int SYSTEM_ERR = 100;
        public static final int USER_CANCEL = 2;
    }

    public static final int getRetCode(Bundle bundle) {
        if (bundle == null) {
            return -1;
        }
        return bundle.getInt(ParamStr.RET_CODE, -1);
    }

    public static final String getErrInfo(Bundle bundle) {
        if (bundle == null) {
            return "";
        }
        return bundle.getString(ParamStr.Err_Info);
    }

    public static final String getRetResErrCode(Bundle bundle) {
        if (bundle == null) {
            return String.valueOf(-1);
        }
        return bundle.getString(ParamStr.RET_RES_ERROR);
    }

    public static final int getRetResCode(Bundle bundle) {
        if (bundle == null) {
            return -1;
        }
        return bundle.getInt(ParamStr.RET_RES_CODE);
    }

    public static final String getRetContent(Bundle bundle) {
        if (bundle == null) {
            return "";
        }
        return bundle.getString(ParamStr.RET_RES_CONTENT);
    }

    public static final Bundle getRetHeads(Bundle bundle) {
        if (bundle == null) {
            return new Bundle();
        }
        Bundle bundle2 = bundle.getBundle(ParamStr.RET_RES_HEAD);
        if (bundle2 == null) {
            return new Bundle();
        }
        return bundle2;
    }

    public static final String getNSPSTATUS(Bundle bundle) {
        Bundle retHeads = getRetHeads(bundle);
        if (retHeads.containsKey(ParamStr.RET_NSP_STATUS)) {
            return retHeads.getString(ParamStr.RET_NSP_STATUS);
        }
        return "";
    }

    public static final String getAccessToken(Bundle bundle) {
        if (bundle == null) {
            return "";
        }
        return bundle.getString(ParamStr.ACCESS_TOKEN);
    }

    public static boolean isRequestSuccess(Bundle bundle) {
        boolean z = true;
        if (bundle == null) {
            return false;
        }
        if (getRetCode(bundle) != 1) {
            z = false;
        }
        return z;
    }

    public static String getExpireIn(Bundle bundle) {
        if (bundle != null) {
            return bundle.getString(ParamStr.EXPIRES_IN, "");
        }
        return "";
    }

    public static String getErrorCode(String str) {
        if (str == null) {
            return "";
        }
        String[] split = str.split(",");
        for (int i = 0; i < split.length; i++) {
            Log.i("111", "---res_content:---strArray[i]" + split[i]);
            if (split[i].contains("\"error\":")) {
                try {
                    return split[i].split(":")[1];
                } catch (Exception e) {
                    return "";
                }
            }
        }
        return "";
    }

    public static Bundle creatReturn(int i, String str) {
        Bundle bundle = new Bundle();
        bundle.putInt(ParamStr.RET_CODE, i);
        bundle.putString(ParamStr.Err_Info, str);
        return bundle;
    }

    public static Bundle creatRunTimeErrRet(String str) {
        return creatReturn(100, str);
    }

    public static Bundle creatNetworkErrRet(String str) {
        return creatReturn(102, str);
    }

    public static Bundle addSuccessCode(Bundle bundle) {
        if (bundle == null) {
            bundle = new Bundle();
        }
        bundle.putInt(ParamStr.RET_CODE, 1);
        return bundle;
    }

    public static Bundle addFailCode(Bundle bundle, int i) {
        if (bundle == null) {
            bundle = new Bundle();
        }
        bundle.putInt(ParamStr.RET_CODE, i);
        return bundle;
    }

    public static Bundle createOpenGwSSLErr() {
        Bundle bundle = new Bundle();
        bundle.putString(ParamStr.RET_RES_ERROR, String.valueOf(104));
        return bundle;
    }

    public static Bundle createOpenGwTimeout() {
        Bundle bundle = new Bundle();
        bundle.putString(ParamStr.RET_RES_ERROR, String.valueOf(103));
        return bundle;
    }

    public static Bundle createOpenGwHttpErr() {
        Bundle bundle = new Bundle();
        bundle.putString(ParamStr.RET_RES_ERROR, String.valueOf(105));
        return bundle;
    }
}
