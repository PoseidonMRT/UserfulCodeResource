package com.huawei.hwid.openapi.quicklogin.d.b;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class k {
    private static String a(char c, int i) {
        StringBuffer stringBuffer = new StringBuffer(i);
        for (int i2 = 0; i2 < i; i2++) {
            stringBuffer.append(c);
        }
        return stringBuffer.toString();
    }

    public static String a(Object obj) {
        return a(String.valueOf(obj));
    }

    public static String a(String str) {
        if (TextUtils.isEmpty(str)) {
            return "";
        }
        int ceil = (int) Math.ceil(((double) (str.length() * 30)) / 100.0d);
        return a('*', ceil) + str.substring(ceil);
    }

    public static String a(Bundle bundle) {
        if (bundle == null) {
            return "";
        }
        Set<String> keySet = bundle.keySet();
        StringBuffer stringBuffer = new StringBuffer();
        for (String str : keySet) {
            stringBuffer.append(str).append("=").append(a(bundle.get(str))).append(" ");
        }
        return stringBuffer.toString();
    }

    public static String a(Intent intent) {
        if (intent == null) {
            return "";
        }
        StringBuffer stringBuffer = new StringBuffer();
        if (!TextUtils.isEmpty(intent.getAction())) {
            stringBuffer.append("act:" + intent.getAction()).append(" ");
        }
        stringBuffer.append(" flag:" + intent.getFlags()).append(" ");
        if (intent.getExtras() != null) {
            stringBuffer.append(a(intent.getExtras()));
        }
        return stringBuffer.toString();
    }

    public static String a(Map map) {
        if (map == null || map.size() == 0) {
            return "";
        }
        StringBuffer stringBuffer = new StringBuffer();
        for (Entry entry : map.entrySet()) {
            stringBuffer.append(entry.getKey()).append("=").append(a(String.valueOf(entry.getValue()))).append(" ");
        }
        return stringBuffer.toString();
    }

    public static String a(String str, boolean z) {
        return j.a(str);
    }

    public static String b(String str) {
        if (TextUtils.isEmpty(str)) {
            return "";
        }
        int indexOf = str.indexOf("=");
        if (indexOf > 0) {
            return str.substring(0, indexOf) + a(str, true).substring(indexOf);
        }
        return str;
    }

    public static String a() {
        return "BccB";
    }

    public static String b() {
        return "mgsI";
    }
}
