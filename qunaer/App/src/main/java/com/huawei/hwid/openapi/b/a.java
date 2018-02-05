package com.huawei.hwid.openapi.b;

import com.huawei.hwid.openapi.a.b;
import com.huawei.hwid.openapi.e.h;
import com.huawei.hwid.openapi.out.OutReturn;
import com.huawei.hwid.openapi.quicklogin.d.b.d;

public class a {
    private static final String a = b.a;

    public static void a(com.huawei.hwid.openapi.a.a aVar) {
        d.a(a, "enter HwIDEntity::authorize(authTokenParam:" + aVar.a());
        if (h.a(aVar.a)) {
            com.huawei.hwid.openapi.quicklogin.a.a().a(aVar);
            if (com.huawei.hwid.openapi.e.a.a(aVar.a)) {
                d.a(a, "HwAccount apk is installed!");
                com.huawei.hwid.openapi.auth.b.b(aVar);
                return;
            }
            d.d(a, "Do not support no HwID APK");
            aVar.b.finish(OutReturn.creatReturn(Integer.parseInt("9"), "hwid is not exit"));
            return;
        }
        d.b(a, "no network");
        aVar.b.finish(OutReturn.creatNetworkErrRet("check env failed!"));
    }
}
