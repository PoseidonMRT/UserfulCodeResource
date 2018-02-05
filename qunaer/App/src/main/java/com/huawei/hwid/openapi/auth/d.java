package com.huawei.hwid.openapi.auth;

import android.os.Bundle;
import android.os.Message;
import com.huawei.hwid.openapi.out.OutReturn;
import com.huawei.hwid.openapi.out.OutReturn.ParamStr;
import com.huawei.hwid.openapi.out.OutReturn.Ret_code;
import com.huawei.hwid.openapi.quicklogin.b.a.b;
import com.huawei.hwid.openapi.quicklogin.b.b.a.a;
import com.huawei.hwid.openapi.quicklogin.b.c;

final class d implements a {
    final /* synthetic */ com.huawei.hwid.openapi.a.a a;

    d(com.huawei.hwid.openapi.a.a aVar) {
        this.a = aVar;
    }

    public void a(c cVar, Bundle bundle) {
        com.huawei.hwid.openapi.quicklogin.b.a.a aVar;
        b a = cVar.a(bundle.getString(ParamStr.RET_RES_CONTENT));
        if (a instanceof com.huawei.hwid.openapi.quicklogin.b.a.a) {
            aVar = (com.huawei.hwid.openapi.quicklogin.b.a.a) a;
        } else {
            aVar = null;
        }
        Message obtain;
        if (aVar != null && aVar.b() == -1) {
            this.a.j = aVar.a();
            obtain = Message.obtain();
            obtain.what = 1;
            obtain.obj = this.a;
            b.b.sendMessage(obtain);
        } else if (aVar != null && aVar.b() == 70002016) {
            com.huawei.hwid.openapi.quicklogin.d.a.c.d(b.a, "getmErrorCode: " + aVar.b());
            obtain = Message.obtain();
            obtain.what = 2;
            obtain.obj = this.a;
            b.b.sendMessage(obtain);
        } else if (aVar != null && aVar.b() == 70001401) {
            com.huawei.hwid.openapi.quicklogin.d.a.c.d(b.a, "getmErrorCode: " + aVar.b());
            bundle.putInt(ParamStr.RET_CODE, Ret_code.ERR_OPENGW_FAILED_1401);
            this.a.b.finish(bundle);
        } else if (aVar == null || aVar.b() != 70001201) {
            bundle.putInt(ParamStr.RET_CODE, Ret_code.ERR_OPENGW_ST_TIMEOUT_FAILED);
            this.a.b.finish(bundle);
        } else {
            com.huawei.hwid.openapi.quicklogin.d.a.c.d(b.a, "getmErrorCode: " + aVar.b());
            bundle.putInt(ParamStr.RET_CODE, Ret_code.ERR_OPENGW_FAILED_1201);
            this.a.b.finish(bundle);
        }
    }

    public void b(c cVar, Bundle bundle) {
        Bundle creatReturn;
        if (bundle.getInt(ParamStr.RET_CODE) == Ret_code.ERR_UP_400_FAILED) {
            creatReturn = OutReturn.creatReturn(Ret_code.ERR_OPENGW_TMPST_400, bundle.getString(ParamStr.Err_Info, ""));
        } else if (bundle.getInt(ParamStr.RET_CODE) == 1002) {
            creatReturn = OutReturn.creatReturn(Ret_code.ERR_OPENGW_TMPST_SSL_FAILED, bundle.getString(ParamStr.Err_Info, ""));
        } else {
            creatReturn = OutReturn.creatReturn(Ret_code.ERR_OPENGW_ST_TIMEOUT_FAILED, bundle.getString(ParamStr.Err_Info, ""));
        }
        this.a.b.finish(creatReturn);
    }
}
