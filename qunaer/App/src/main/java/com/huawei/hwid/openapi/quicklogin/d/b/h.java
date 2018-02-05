package com.huawei.hwid.openapi.quicklogin.d.b;

import com.huawei.hwid.openapi.quicklogin.a.a;

public class h implements e {
    private static h a;

    public static synchronized h b() {
        h hVar;
        synchronized (h.class) {
            if (a == null) {
                a = new h();
            }
            hVar = a;
        }
        return hVar;
    }

    public int a() {
        try {
            Object c = c();
            if (c != null) {
                return ((Integer) c.getClass().getMethod("getDefaultSubscription", new Class[0]).invoke(c, new Object[0])).intValue();
            }
            return 0;
        } catch (Throwable e) {
            d.b(a.b, e.getMessage(), e);
            return -1;
        } catch (Throwable e2) {
            d.b(a.b, e2.getMessage(), e2);
            return -1;
        }
    }

    public String a(int i) {
        String str;
        String str2 = "";
        Class[] clsArr = new Class[]{Integer.TYPE};
        Object[] objArr = new Object[]{Integer.valueOf(i)};
        try {
            Object c = c();
            if (c != null) {
                str = (String) c.getClass().getMethod("getSubscriberId", clsArr).invoke(c, objArr);
                if (str != null) {
                    return "";
                }
                return str;
            }
        } catch (Throwable e) {
            d.b(a.b, e.getMessage(), e);
            str = str2;
        } catch (Throwable e2) {
            d.b(a.b, e2.getMessage(), e2);
        }
        str = str2;
        if (str != null) {
            return str;
        }
        return "";
    }

    public int b(int i) {
        int i2;
        if (i == -1) {
            i2 = 5;
        } else {
            i2 = 0;
        }
        Class[] clsArr = new Class[]{Integer.TYPE};
        Object[] objArr = new Object[]{Integer.valueOf(i)};
        try {
            Object c = c();
            if (c != null) {
                i2 = ((Integer) c.getClass().getDeclaredMethod("getSimState", clsArr).invoke(c, objArr)).intValue();
            }
        } catch (Throwable e) {
            d.b(a.b, e.getMessage(), e);
        } catch (Throwable e2) {
            d.b(a.b, e2.getMessage(), e2);
        }
        return i2;
    }

    public static Object c() {
        Object obj = null;
        try {
            Class cls = Class.forName("android.telephony.MSimTelephonyManager");
            obj = cls.getDeclaredMethod("getDefault", new Class[0]).invoke(cls, new Object[0]);
        } catch (Throwable e) {
            d.b(a.b, e.getMessage(), e);
        }
        return obj;
    }

    public String c(int i) {
        String str;
        String str2 = "";
        Class[] clsArr = new Class[]{Integer.TYPE};
        Object[] objArr = new Object[]{Integer.valueOf(i)};
        try {
            Object c = c();
            if (c != null) {
                str = (String) c.getClass().getMethod("getSimOperator", clsArr).invoke(c, objArr);
                if (str != null) {
                    return "";
                }
                return str;
            }
        } catch (Throwable e) {
            d.b(a.b, e.getMessage(), e);
            str = str2;
        } catch (Throwable e2) {
            d.b(a.b, e2.getMessage(), e2);
        }
        str = str2;
        if (str != null) {
            return str;
        }
        return "";
    }
}
