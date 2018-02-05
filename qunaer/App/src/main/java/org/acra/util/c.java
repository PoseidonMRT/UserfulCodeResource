package org.acra.util;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.Context;
import android.os.Process;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import org.acra.ACRA;

public class c {
    public boolean a(@NonNull Context context) {
        return b(context).equalsIgnoreCase(context.getPackageName());
    }

    @NonNull
    public static String b(@NonNull Context context) {
        String str = "";
        try {
            Class cls = Class.forName("android.ddm.DdmHandleAppName");
            str = (String) cls.getDeclaredMethod("getAppName", new Class[0]).invoke(cls, new Object[0]);
        } catch (Throwable th) {
            ACRA.f.e(ACRA.e, "can not find DdmHandleAppName class in getCurrentProcessName method");
        }
        if (!TextUtils.isEmpty(str)) {
            return str;
        }
        int myPid = Process.myPid();
        for (RunningAppProcessInfo runningAppProcessInfo : ((ActivityManager) context.getSystemService("activity")).getRunningAppProcesses()) {
            if (runningAppProcessInfo.pid == myPid) {
                return runningAppProcessInfo.processName;
            }
        }
        return str;
    }
}
