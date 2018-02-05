package com.huawei.hwid.openapi.e.a;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;
import com.huawei.hwid.openapi.a.b;
import com.huawei.hwid.openapi.e.h;
import com.huawei.hwid.openapi.quicklogin.d.b.d;
import java.security.SecureRandom;

public class c {
    private static final String a = b.a;

    public static String a(Context context, String str) {
        String str2 = "";
        if (TextUtils.isEmpty(str)) {
            return str2;
        }
        SharedPreferences a = com.huawei.hwid.openapi.e.b.a(context);
        str2 = a.getString("OxSb13_2d", null);
        if (str2 == null) {
            str2 = String.valueOf(new SecureRandom().nextLong());
            a.edit().putString("OxSb13_2d", str2).commit();
        }
        return b.a(h.b(str), 0, h.b(new StringBuffer().append(a("NikHweTd")).append(str2).toString()), 0);
    }

    public static String b(Context context, String str) {
        String str2 = "";
        if (TextUtils.isEmpty(str)) {
            return str2;
        }
        Object string = com.huawei.hwid.openapi.e.b.a(context).getString("OxSb13_2d", null);
        if (TextUtils.isEmpty(string)) {
            return str2;
        }
        String str3;
        try {
            byte[] a = b.a(str, h.b(new StringBuffer().append(a("NikHweTd")).append(string).toString()), 0);
            if (a != null) {
                str3 = new String(a, "UTF-8");
                return str3;
            }
        } catch (Throwable e) {
            d.b(a, "UnsupportedEncodingException:" + e.getMessage(), e);
        }
        str3 = str2;
        return str3;
    }

    private static String a(String str) {
        int i = 0;
        if (TextUtils.isEmpty(str)) {
            return "";
        }
        char[] toCharArray = str.toCharArray();
        char[] cArr = new char[toCharArray.length];
        int length = toCharArray.length;
        int i2 = 0;
        while (i < length) {
            cArr[i2] = (char) (toCharArray[i] + 2);
            i2++;
            i++;
        }
        return new String(cArr);
    }
}
