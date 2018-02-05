package com.huawei.hwid.openapi.quicklogin.d.b;

import java.lang.reflect.Field;

public class g {
    private static f a = f.MODE_SUPPORT_UNKNOWN;
    private static e b;

    public static e a() {
        b();
        if (a == f.MODE_SUPPORT_MTK_GEMINI) {
            b = i.b();
        } else {
            b = h.b();
        }
        return b;
    }

    public static boolean b() {
        boolean z = true;
        if (a == f.MODE_SUPPORT_UNKNOWN) {
            try {
                if (d() || e()) {
                    a = f.MODE_SUPPORT_MTK_GEMINI;
                } else if (c()) {
                    a = f.MODE_SUPPORT_HW_GEMINI;
                } else {
                    a = f.MODE_NOT_SUPPORT_GEMINI;
                    z = false;
                }
                return z;
            } catch (Throwable e) {
                d.b("mutiCardFactory", e.getMessage(), e);
                return false;
            } catch (Throwable e2) {
                d.b("mutiCardFactory", e2.toString(), e2);
                return false;
            }
        } else if (a == f.MODE_SUPPORT_HW_GEMINI || a == f.MODE_SUPPORT_MTK_GEMINI) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean c() {
        try {
            boolean booleanValue;
            Object c = h.c();
            if (c != null) {
                booleanValue = ((Boolean) c.getClass().getMethod("isMultiSimEnabled", new Class[0]).invoke(c, new Object[0])).booleanValue();
            } else {
                booleanValue = false;
            }
            return booleanValue;
        } catch (Throwable e) {
            d.b("mutiCardFactory", e.getMessage(), e);
            return false;
        } catch (Throwable e2) {
            d.b("mutiCardFactory", e2.getMessage(), e2);
            return false;
        }
    }

    private static boolean d() {
        boolean z = false;
        try {
            Field declaredField = Class.forName("com.mediatek.common.featureoption.FeatureOption").getDeclaredField("MTK_GEMINI_SUPPORT");
            declaredField.setAccessible(true);
            z = declaredField.getBoolean(null);
        } catch (Exception e) {
            d.d("mutiCardFactory", e.getMessage());
        } catch (Error e2) {
            d.d("mutiCardFactory", e2.toString());
        }
        return z;
    }

    private static boolean e() {
        try {
            boolean equals;
            Class cls = Class.forName("android.os.SystemProperties");
            Object newInstance = cls.newInstance();
            Object invoke = cls.getMethod("get", new Class[]{String.class}).invoke(newInstance, new Object[]{"ro.mtk_gemini_support"});
            if (invoke instanceof String) {
                equals = ((String) invoke).equals("1");
            } else {
                equals = false;
            }
            return equals;
        } catch (Exception e) {
            d.d("mutiCardFactory", e.getMessage());
            return false;
        } catch (Error e2) {
            d.d("mutiCardFactory", e2.toString());
            return false;
        }
    }
}
