package com.huawei.hwid.openapi.quicklogin.d.a;

import android.content.Context;
import com.huawei.hwid.openapi.out.OutReturn.Ret_code;
import com.huawei.hwid.openapi.quicklogin.b.b;
import com.huawei.hwid.openapi.quicklogin.d.b.k;
import com.huawei.hwid.openapi.quicklogin.datatype.e;
import java.util.HashMap;
import java.util.Map;

public class d {
    public static final Object a = new Object();
    private static volatile d b;
    private Map c = new HashMap();
    private Map d = new HashMap();

    private e a(int i, e eVar) {
        e eVar2 = null;
        synchronized (a) {
            switch (i) {
                case 1000:
                    com.huawei.hwid.openapi.quicklogin.d.b.d.a("OpLogUtil", "QUERY_LOADING");
                    eVar2 = (e) this.c.get(eVar.a());
                    break;
                case 1001:
                    com.huawei.hwid.openapi.quicklogin.d.b.d.a("OpLogUtil", "QUERY_UNLOADING");
                    eVar2 = (e) this.d.get(eVar.a());
                    break;
                case 1002:
                    com.huawei.hwid.openapi.quicklogin.d.b.d.a("OpLogUtil", "REMOVE_LOADING");
                    eVar2 = (e) this.c.remove(eVar.a());
                    break;
                case 1003:
                    com.huawei.hwid.openapi.quicklogin.d.b.d.a("OpLogUtil", "REMOVE_UNLOADING");
                    eVar2 = (e) this.d.remove(eVar.a());
                    break;
                case Ret_code.ERR_UP_400_FAILED /*1004*/:
                    com.huawei.hwid.openapi.quicklogin.d.b.d.a("OpLogUtil", "CHANGE_LOADING");
                    eVar2 = (e) this.c.put(eVar.a(), eVar);
                    break;
                case 1005:
                    com.huawei.hwid.openapi.quicklogin.d.b.d.a("OpLogUtil", "CHANGE_UNLOADING");
                    eVar2 = (e) this.d.put(eVar.a(), eVar);
                    break;
            }
        }
        return eVar2;
    }

    public static synchronized d a() {
        d dVar;
        synchronized (d.class) {
            if (b == null) {
                b = new d();
            }
            dVar = b;
        }
        return dVar;
    }

    private void c(Context context, e eVar) {
        String dVar = new com.huawei.hwid.openapi.quicklogin.datatype.d(context, eVar).toString();
        com.huawei.hwid.openapi.quicklogin.d.b.d.a("OpLogUtil", "uploading:" + k.a(dVar, true));
        com.huawei.hwid.openapi.quicklogin.b.b.d.a(context, new b(dVar), new e(this, context, eVar));
    }

    private boolean a(e eVar) {
        if (eVar != null && eVar.a() != null && !"".equals(eVar.a().trim())) {
            return true;
        }
        com.huawei.hwid.openapi.quicklogin.d.b.d.a("OpLogUtil", "item illegal");
        return false;
    }

    public void a(Context context, e eVar) {
        com.huawei.hwid.openapi.quicklogin.d.b.d.a("OpLogUtil", "add2List:" + (eVar == null ? null : k.a(eVar.toString(), true)));
        if (!a(eVar)) {
            return;
        }
        if (a(1000, eVar) == null) {
            a(1005, eVar);
        } else if (eVar != null) {
            com.huawei.hwid.openapi.quicklogin.d.b.d.a("OpLogUtil", "uploading:" + k.a(eVar.a(), true) + ", return");
        }
    }

    public void b(Context context, e eVar) {
        com.huawei.hwid.openapi.quicklogin.d.b.d.a("OpLogUtil", "uploadOpLogLater:" + (eVar == null ? null : k.a(eVar.toString(), true)));
        if (!a(eVar)) {
            return;
        }
        if (a(1000, eVar) == null) {
            e a = a(1003, eVar);
            if (a != null && eVar != null && eVar.c() > a.b() + 0) {
                a.a(eVar.b());
                a.b(eVar.i());
                a.c(eVar.j());
                a.a(eVar.h());
                a((int) Ret_code.ERR_UP_400_FAILED, a);
                c(context, a(1000, eVar));
            }
        } else if (eVar != null) {
            com.huawei.hwid.openapi.quicklogin.d.b.d.a("OpLogUtil", "uploading:" + k.a(eVar.a(), true) + ", return");
        }
    }
}
