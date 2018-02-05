package com.mqunar.internal;

import android.content.Context;
import com.mqunar.core.ReflectUtils;
import com.mqunar.storage.Storage;

public class SpiderPlatfromInfo {
    private static final String ASSERT_CONFIG = "config";
    private static final String ASSERT_SWITCH = "switch";

    public static void init(Context context) {
        String str = "com.mqunar.atomenv.PlatformSetting";
        byte[] openAsset = Storage.openAsset(context, ASSERT_CONFIG);
        if (openAsset != null) {
            String str2 = new String(openAsset, "UTF-8");
            ReflectUtils.invokeStaticMethod(str, "setConfig", new Class[]{String.class}, new String[]{str2});
        }
        openAsset = Storage.openAsset(context, ASSERT_SWITCH);
        if (openAsset != null) {
            str2 = new String(openAsset, "UTF-8");
            ReflectUtils.invokeStaticMethod(str, "setSwitch", new Class[]{String.class}, new String[]{str2});
        }
    }
}
