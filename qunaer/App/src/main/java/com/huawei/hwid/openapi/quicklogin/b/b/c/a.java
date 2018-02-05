package com.huawei.hwid.openapi.quicklogin.b.b.c;

import android.os.Bundle;
import android.util.Log;
import com.huawei.hwid.openapi.out.OutReturn.ParamStr;

public class a {
    private static final String a = a.class.getSimpleName();

    public static final int a(Bundle bundle) {
        if (bundle != null) {
            return bundle.getInt(ParamStr.RET_RES_CODE);
        }
        Log.d(a, "Bundle is null,return -1");
        return -1;
    }

    public static final Bundle b(Bundle bundle) {
        if (bundle == null) {
            Log.d(a, "Bundle is null,return new Bundle");
            return new Bundle();
        }
        Bundle bundle2 = bundle.getBundle(ParamStr.RET_RES_HEAD);
        if (bundle2 != null) {
            return bundle2;
        }
        Log.d(a, "Heads is null,return new Bundle");
        return new Bundle();
    }

    public static final String c(Bundle bundle) {
        Bundle b = b(bundle);
        if (b.containsKey(ParamStr.RET_NSP_STATUS)) {
            return b.getString(ParamStr.RET_NSP_STATUS);
        }
        Log.d(a, "not found RET_NSP_STATUS");
        return "";
    }

    public static Bundle a(int i, String str) {
        Log.d(a, "creatReturn");
        Bundle bundle = new Bundle();
        bundle.putInt(ParamStr.RET_CODE, i);
        bundle.putString(ParamStr.Err_Info, str);
        return bundle;
    }
}
