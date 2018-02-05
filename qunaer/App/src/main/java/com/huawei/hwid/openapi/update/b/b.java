package com.huawei.hwid.openapi.update.b;

import com.huawei.hwid.openapi.quicklogin.d.a.c;

public class b {
    private static void a(Class cls, Class[] clsArr, Object[] objArr) {
        if (cls == null) {
            throw new Exception("class is null in staticFun");
        } else if (clsArr == null) {
            if (objArr != null) {
                throw new Exception("paramsType is null, but params is not null");
            }
        } else if (objArr == null) {
            throw new Exception("paramsType or params should be same");
        } else if (clsArr.length != objArr.length) {
            throw new Exception("paramsType len:" + clsArr.length + " should equal params.len:" + objArr.length);
        }
    }

    public static Object a(Class cls, String str, Class[] clsArr, Object[] objArr) {
        Object obj = null;
        a(cls, clsArr, objArr);
        try {
            try {
                obj = cls.getMethod(str, clsArr).invoke(null, objArr);
            } catch (Throwable e) {
                c.c("HwInvoke", e.getMessage(), e);
            } catch (Throwable e2) {
                c.c("HwInvoke", e2.getMessage(), e2);
            } catch (Throwable e22) {
                c.c("HwInvoke", e22.getMessage(), e22);
            }
        } catch (Throwable e222) {
            c.c("HwInvoke", e222.getMessage(), e222);
        } catch (Throwable e2222) {
            c.b("HwInvoke", e2222.getMessage(), e2222);
        }
        return obj;
    }

    public static Object a(String str, String str2, Class[] clsArr, Object[] objArr) {
        try {
            return a(Class.forName(str), str2, clsArr, objArr);
        } catch (Throwable e) {
            c.c("HwInvoke", e.getMessage(), e);
            return null;
        } catch (Throwable e2) {
            c.c("HwInvoke", e2.getMessage(), e2);
            return null;
        } catch (Throwable e22) {
            c.c("HwInvoke", e22.getMessage(), e22);
            return null;
        }
    }
}
