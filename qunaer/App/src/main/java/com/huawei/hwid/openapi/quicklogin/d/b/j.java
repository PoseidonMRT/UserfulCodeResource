package com.huawei.hwid.openapi.quicklogin.d.b;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class j {
    public static String a(String str) {
        if (str != null) {
            try {
                if (!str.trim().equals("")) {
                    char[] toCharArray = str.toCharArray();
                    int i = 0;
                    while (i < toCharArray.length) {
                        if (!("0123456789".contains(String.valueOf(toCharArray[i])) || "{:=@}/#?%\"(),/\\<>| &".contains(String.valueOf(toCharArray[i])))) {
                            toCharArray[i] = '*';
                        }
                        i += 2;
                    }
                    str = String.valueOf(toCharArray);
                    Matcher matcher = Pattern.compile("[0-9]{7,}").matcher(str);
                    while (matcher.find()) {
                        String group = matcher.group();
                        if (group == null) {
                            break;
                        }
                        char[] toCharArray2 = group.toCharArray();
                        for (i = 0; i < toCharArray2.length; i += 2) {
                            if (!"{:=@}/#?%\"(),/\\<>| &".contains(String.valueOf(toCharArray2[i]))) {
                                toCharArray2[i] = '*';
                            }
                        }
                        str = str.replaceAll(group, String.valueOf(toCharArray2));
                    }
                }
            } catch (Throwable e) {
                d.b("MyProguard", e.getMessage(), e);
            }
        }
        return str;
    }
}
