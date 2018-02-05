package com.huawei.hwid.openapi.quicklogin.d.b;

import android.text.TextUtils;
import com.huawei.hwid.openapi.quicklogin.a.a;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public final class i implements e {
    private static i a;

    public static synchronized i b() {
        i iVar;
        synchronized (i.class) {
            if (a == null) {
                a = new i();
            }
            iVar = a;
        }
        return iVar;
    }

    private i() {
    }

    public int a() {
        return c();
    }

    private static int c() {
        try {
            Class cls = Class.forName("android.telephony.TelephonyManager");
            Method declaredMethod = cls.getDeclaredMethod("getDefaultSim", (Class[]) null);
            Object invoke = cls.getDeclaredMethod("getDefault", (Class[]) null).invoke(null, (Object[]) null);
            declaredMethod.setAccessible(true);
            return ((Integer) declaredMethod.invoke(invoke, (Object[]) null)).intValue();
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
        CharSequence charSequence = "";
        Class[] clsArr = new Class[]{Integer.TYPE};
        Object[] objArr = new Object[]{Integer.valueOf(i)};
        try {
            Object d = d();
            if (d != null) {
                str = (String) d.getClass().getMethod("getSubscriberId", clsArr).invoke(d, objArr);
            } else {
                CharSequence charSequence2 = charSequence;
            }
            charSequence = str;
        } catch (Throwable e) {
            d.b(a.b, e.getMessage(), e);
        } catch (Throwable e2) {
            d.b(a.b, e2.getMessage(), e2);
        }
        if (TextUtils.isEmpty(charSequence)) {
            str = d(i);
        } else {
            charSequence2 = charSequence;
        }
        if (str == null) {
            return "";
        }
        return str;
    }

    private String d(int i) {
        d.b(a.b, "getMTKPlmn");
        Class[] clsArr = new Class[]{Integer.TYPE};
        try {
            String str;
            if (((Integer) Class.forName("android.telephony.SubscriptionManager").getMethod("getSlotId", clsArr).invoke(null, new Object[]{Integer.valueOf(i)})).intValue() != -1) {
                Object d = d();
                str = (String) d.getClass().getMethod("getSimOperator", clsArr).invoke(d, new Object[]{Integer.valueOf(r0)});
            } else {
                str = null;
            }
            return str;
        } catch (ClassNotFoundException e) {
            d.d(a.b, "ClassNotFoundException");
            return null;
        } catch (NoSuchMethodException e2) {
            d.d(a.b, "NoSuchMethodException");
            return null;
        } catch (InvocationTargetException e3) {
            d.d(a.b, "InvocationTargetException");
            return null;
        } catch (IllegalAccessException e4) {
            d.d(a.b, "IllegalAccessException");
            return null;
        }
    }

    public int b(int i) {
        Class[] clsArr = new Class[]{Integer.TYPE};
        Object[] objArr = new Object[]{Integer.valueOf(i)};
        try {
            int intValue;
            Object d = d();
            if (d != null) {
                intValue = ((Integer) d.getClass().getDeclaredMethod("getSimState", clsArr).invoke(d, objArr)).intValue();
            } else {
                intValue = 0;
            }
            return intValue;
        } catch (Throwable e) {
            d.b(a.b, e.getMessage(), e);
            return 0;
        } catch (Throwable e2) {
            d.b(a.b, e2.getMessage(), e2);
            return 0;
        }
    }

    public String c(int i) {
        return "";
    }

    private static Object d() {
        Object obj = null;
        try {
            Class cls = Class.forName("com.mediatek.telephony.TelephonyManagerEx");
            obj = cls.getDeclaredMethod("getDefault", new Class[0]).invoke(cls, new Object[0]);
        } catch (Throwable e) {
            d.b(a.b, e.getMessage(), e);
        }
        return obj;
    }
}
