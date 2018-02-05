package org.acra.anr;

import android.app.ActivityManager;
import android.app.ActivityManager.ProcessErrorStateInfo;
import android.content.Context;
import android.os.Looper;
import android.os.Process;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import java.util.List;
import org.acra.ACRA;

public class a {
    @Nullable
    private static ProcessErrorStateInfo a(@NonNull Context context, int i) {
        ActivityManager activityManager = (ActivityManager) context.getSystemService("activity");
        for (int i2 = 0; i2 < i; i2++) {
            SystemClock.sleep(200);
            List<ProcessErrorStateInfo> processesInErrorState = activityManager.getProcessesInErrorState();
            if (processesInErrorState != null) {
                for (ProcessErrorStateInfo processErrorStateInfo : processesInErrorState) {
                    if (processErrorStateInfo.condition == 2) {
                        return processErrorStateInfo;
                    }
                }
                continue;
            }
        }
        return null;
    }

    static boolean a(@NonNull Context context, int i, int i2, String str) {
        StackTraceElement[] stackTrace = Looper.getMainLooper().getThread().getStackTrace();
        ProcessErrorStateInfo a = a(context, i);
        if (a == null || Process.myPid() != a.pid) {
            return false;
        }
        Throwable aNRException = new ANRException(a.longMsg);
        aNRException.setStackTrace(stackTrace);
        aNRException.monitorMode = i2;
        aNRException.tracesFilePath = str;
        ACRA.getErrorReporter().uncaughtException(Thread.currentThread(), aNRException);
        return true;
    }
}
