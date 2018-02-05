package com.mqunar.atomenv;

import android.content.Context;

public class SystemPropertyProxy {
    public static String get(Context context, String str) {
        String str2 = "";
        try {
            Class loadClass = context.getClassLoader().loadClass("android.os.SystemProperties");
            return (String) loadClass.getMethod("get", new Class[]{String.class}).invoke(loadClass, new Object[]{new String(str)});
        } catch (Exception e) {
            return "";
        }
    }
}
