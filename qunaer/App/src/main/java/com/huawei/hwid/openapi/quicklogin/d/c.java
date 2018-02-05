package com.huawei.hwid.openapi.quicklogin.d;

import com.huawei.hwid.openapi.quicklogin.d.b.d;
import java.util.regex.Pattern;

public class c {
    public static boolean a(String str, String str2) {
        if (b(str) || b(str2) || !Pattern.compile(str2).matcher(str).matches()) {
            return false;
        }
        return true;
    }

    public static boolean a(String str) {
        if (str == null) {
            return false;
        }
        return a(str, "^[0-9]{0,128}$");
    }

    public static boolean b(String str) {
        if (str == null || str.trim().length() < 1) {
            return true;
        }
        return false;
    }

    public static byte[] c(String str) {
        try {
            return str.getBytes("UTF-8");
        } catch (Throwable e) {
            d.b("StringUtil", e.getMessage(), e);
            return new byte[0];
        }
    }

    public static String d(String str) {
        int i = 0;
        if (b(str)) {
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
