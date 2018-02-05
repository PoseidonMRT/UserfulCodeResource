package com.huawei.hwid.openapi.b;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import com.huawei.hwid.openapi.a.b;
import com.huawei.hwid.openapi.quicklogin.d.b.k;
import com.mqunar.tools.DateTimeUtils;

public class d {
    private static final String a = b.a;

    public static String a(Context context, String str, String str2, Bundle bundle) {
        if (str2 == null) {
            str2 = "";
        }
        Object obj = "";
        try {
            obj = com.huawei.hwid.openapi.e.b.b(context, str + "_" + str2, "");
        } catch (Throwable e) {
            com.huawei.hwid.openapi.quicklogin.d.b.d.b(a, e.getMessage(), e);
        } catch (Throwable e2) {
            com.huawei.hwid.openapi.quicklogin.d.b.d.b(a, e2.getMessage(), e2);
        }
        if (TextUtils.isEmpty(obj)) {
            com.huawei.hwid.openapi.quicklogin.d.b.d.d(a, "token is not exist for:" + k.a(str) + "_" + str2);
        }
        return obj;
    }

    public static void b(Context context, String str, String str2, Bundle bundle) {
        if (str != null) {
            if (str2 == null) {
                str2 = "";
            }
            try {
                com.huawei.hwid.openapi.e.b.a(context, str + "_" + str2);
            } catch (Throwable e) {
                com.huawei.hwid.openapi.quicklogin.d.b.d.b(a, e.getMessage(), e);
            }
        }
    }

    public static boolean a(Context context, String str, String str2, String str3, Bundle bundle) {
        try {
            if (TextUtils.isEmpty(str3) || TextUtils.isEmpty(str)) {
                com.huawei.hwid.openapi.quicklogin.d.b.d.b(a, "in store Token token:" + k.a(str3) + " clientId:" + k.a(str) + " is invalid", null);
                return false;
            }
            if (str2 == null) {
                str2 = "";
            }
            com.huawei.hwid.openapi.quicklogin.d.b.d.a(a, "storeToken");
            com.huawei.hwid.openapi.e.b.a(context, str + "_" + str2, str3);
            return true;
        } catch (Throwable e) {
            com.huawei.hwid.openapi.quicklogin.d.b.d.b(a, "storeToken InvalidKeyException: " + e.getMessage(), e);
            return false;
        } catch (Throwable e2) {
            com.huawei.hwid.openapi.quicklogin.d.b.d.b(a, "storeToken BadPaddingException: " + e2.getMessage(), e2);
            return false;
        } catch (Throwable e22) {
            com.huawei.hwid.openapi.quicklogin.d.b.d.b(a, "storeToken IllegalBlockSizeException: " + e22.getMessage(), e22);
            return false;
        } catch (Throwable e222) {
            com.huawei.hwid.openapi.quicklogin.d.b.d.b(a, "storeToken NoSuchAlgorithmException: " + e222.getMessage(), e222);
            return false;
        } catch (Throwable e2222) {
            com.huawei.hwid.openapi.quicklogin.d.b.d.b(a, "storeToken NoSuchPaddingException: " + e2222.getMessage(), e2222);
            return false;
        } catch (Throwable e22222) {
            com.huawei.hwid.openapi.quicklogin.d.b.d.b(a, "storeToken Exception: " + e22222.getMessage(), e22222);
            return false;
        }
    }

    public static boolean a(Context context, String str, String str2, String str3) {
        if (TextUtils.isEmpty(str)) {
            com.huawei.hwid.openapi.quicklogin.d.b.d.b(a, "in saveTokenExpireTime: clientId:" + k.a(str) + " is invalid", null);
            return false;
        }
        long currentTimeMillis = System.currentTimeMillis();
        if (TextUtils.isEmpty(str3)) {
            currentTimeMillis = a(context, str2, str);
        } else {
            com.huawei.hwid.openapi.quicklogin.d.b.d.a(a, "exp is not empty");
            currentTimeMillis = (currentTimeMillis + (Long.parseLong(str3) * 1000)) - DateTimeUtils.ONE_HOUR;
        }
        try {
            String valueOf = String.valueOf(currentTimeMillis);
            com.huawei.hwid.openapi.quicklogin.d.b.d.a(a, "saveTokenExpireTime");
            com.huawei.hwid.openapi.e.b.a(context, str + "_" + str2, valueOf);
            return true;
        } catch (Throwable e) {
            com.huawei.hwid.openapi.quicklogin.d.b.d.b(a, "storeToken InvalidKeyException: " + e.getMessage(), e);
            return false;
        } catch (Throwable e2) {
            com.huawei.hwid.openapi.quicklogin.d.b.d.b(a, "storeToken BadPaddingException: " + e2.getMessage(), e2);
            return false;
        } catch (Throwable e22) {
            com.huawei.hwid.openapi.quicklogin.d.b.d.b(a, "storeToken IllegalBlockSizeException: " + e22.getMessage(), e22);
            return false;
        } catch (Throwable e222) {
            com.huawei.hwid.openapi.quicklogin.d.b.d.b(a, "storeToken NoSuchAlgorithmException: " + e222.getMessage(), e222);
            return false;
        } catch (Throwable e2222) {
            com.huawei.hwid.openapi.quicklogin.d.b.d.b(a, "storeToken NoSuchPaddingException: " + e2222.getMessage(), e2222);
            return false;
        } catch (Throwable e22222) {
            com.huawei.hwid.openapi.quicklogin.d.b.d.b(a, "storeToken Exception: " + e22222.getMessage(), e22222);
            return false;
        }
    }

    public static long a(Context context, String str, String str2) {
        long currentTimeMillis = System.currentTimeMillis();
        try {
            Object b = com.huawei.hwid.openapi.e.b.b(context, str2 + "_" + str, "");
            if (!TextUtils.isEmpty(b)) {
                currentTimeMillis = Long.parseLong(b);
            }
        } catch (Throwable e) {
            com.huawei.hwid.openapi.quicklogin.d.b.d.b(a, e.toString(), e);
        } catch (Throwable e2) {
            com.huawei.hwid.openapi.quicklogin.d.b.d.b(a, e2.getMessage(), e2);
        }
        return currentTimeMillis;
    }

    public static boolean b(Context context, String str, String str2, String str3) {
        try {
            if (TextUtils.isEmpty(str)) {
                com.huawei.hwid.openapi.quicklogin.d.b.d.b(a, "in saveTokenExpireTime: clientId:" + k.a(str) + " is invalid", null);
                return false;
            }
            com.huawei.hwid.openapi.quicklogin.d.b.d.a(a, "saveDefaultTokenExpireTime");
            com.huawei.hwid.openapi.e.b.a(context, str + "_" + str2, str3);
            return true;
        } catch (Throwable e) {
            com.huawei.hwid.openapi.quicklogin.d.b.d.b(a, "storeToken InvalidKeyException: " + e.getMessage(), e);
            return false;
        } catch (Throwable e2) {
            com.huawei.hwid.openapi.quicklogin.d.b.d.b(a, "storeToken BadPaddingException: " + e2.getMessage(), e2);
            return false;
        } catch (Throwable e22) {
            com.huawei.hwid.openapi.quicklogin.d.b.d.b(a, "storeToken IllegalBlockSizeException: " + e22.getMessage(), e22);
            return false;
        } catch (Throwable e222) {
            com.huawei.hwid.openapi.quicklogin.d.b.d.b(a, "storeToken NoSuchAlgorithmException: " + e222.getMessage(), e222);
            return false;
        } catch (Throwable e2222) {
            com.huawei.hwid.openapi.quicklogin.d.b.d.b(a, "storeToken NoSuchPaddingException: " + e2222.getMessage(), e2222);
            return false;
        } catch (Throwable e22222) {
            com.huawei.hwid.openapi.quicklogin.d.b.d.b(a, "storeToken Exception: " + e22222.getMessage(), e22222);
            return false;
        }
    }
}
