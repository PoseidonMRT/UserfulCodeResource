package com.huawei.hwid.openapi.a;

import com.huawei.hwid.openapi.out.OutConst;
import com.huawei.hwid.openapi.quicklogin.d.b.d;

public class b {
    public static final String a = ("HwID_OpenSDK_LOG[" + a() + "/" + OutConst.version + "]");

    private static String a() {
        try {
            return OutConst.getIterfaceVersion();
        } catch (Throwable th) {
            d.b(a, th.getMessage(), th);
            return "1.005";
        }
    }
}
