package com.mqunar.atomenv;

import com.mqunar.tools.log.QLog;
import java.lang.reflect.Field;

public class BuildConfigBridge {
    public static String getBuildConfigByPackageName(String str, String str2) {
        String str3 = "";
        try {
            Field declaredField = Class.forName(str + ".BuildConfig").getDeclaredField(str2);
            declaredField.setAccessible(true);
            str3 = declaredField.get(null).toString();
        } catch (Throwable th) {
            QLog.d("env", "packageName %s found BuildConfig error", th.getMessage());
        }
        return str3;
    }
}
