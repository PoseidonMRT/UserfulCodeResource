package org.acra.ne;

import android.content.Context;
import android.support.annotation.NonNull;
import java.io.File;
import org.acra.ACRA;
import org.acra.file.e;
import org.acra.util.b;

public class NativeExceptionHandler {
    public static native void endApplication();

    public static native void setDumpDir(String str, int i);

    public void a(@NonNull Context context) {
        setDumpDir(new e(context).d().getAbsolutePath(), b.a());
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static void handleCrash(org.acra.ne.NativeException r5) {
        /*
        r0 = org.acra.ACRA.f;
        r1 = org.acra.ACRA.e;
        r2 = "handle native Crash";
        r0.e(r1, r2);
        r0 = r5.getCrashFileDirectory();	 Catch:{ Exception -> 0x0057 }
        r1 = new java.io.File;	 Catch:{ Exception -> 0x0057 }
        r1.<init>(r0);	 Catch:{ Exception -> 0x0057 }
        r2 = r1.exists();	 Catch:{ Exception -> 0x0057 }
        if (r2 == 0) goto L_0x004d;
    L_0x0018:
        r2 = "";
        r0 = r0.equals(r2);	 Catch:{ Exception -> 0x0057 }
        if (r0 != 0) goto L_0x004d;
    L_0x0020:
        r0 = org.acra.ACRA.getErrorReporter();	 Catch:{ Exception -> 0x0057 }
        r1 = r1.getAbsolutePath();	 Catch:{ Exception -> 0x0057 }
        r5.setCrashFileDirectory(r1);	 Catch:{ Exception -> 0x0057 }
        r1 = java.lang.Thread.currentThread();	 Catch:{ Exception -> 0x0057 }
        r0.uncaughtException(r1, r5);	 Catch:{ Exception -> 0x0057 }
        r0 = org.acra.ACRA.f;	 Catch:{ Exception -> 0x0057 }
        r1 = org.acra.ACRA.e;	 Catch:{ Exception -> 0x0057 }
        r2 = "handleNativeException 等待Java代码中的线程";
        r0.e(r1, r2);	 Catch:{ Exception -> 0x0057 }
        r0 = 1000; // 0x3e8 float:1.401E-42 double:4.94E-321;
        android.os.SystemClock.sleep(r0);	 Catch:{ Exception -> 0x0057 }
    L_0x0040:
        r0 = org.acra.ACRA.f;
        r1 = org.acra.ACRA.e;
        r2 = "handleNativeException 结束程序";
        r0.e(r1, r2);
        endApplication();
    L_0x004c:
        return;
    L_0x004d:
        r0 = org.acra.ACRA.f;	 Catch:{ Exception -> 0x0057 }
        r1 = org.acra.ACRA.e;	 Catch:{ Exception -> 0x0057 }
        r2 = "dmp not found";
        r0.e(r1, r2);	 Catch:{ Exception -> 0x0057 }
        goto L_0x0040;
    L_0x0057:
        r0 = move-exception;
        r1 = org.acra.ACRA.f;	 Catch:{ all -> 0x0083 }
        r2 = org.acra.ACRA.e;	 Catch:{ all -> 0x0083 }
        r3 = new java.lang.StringBuilder;	 Catch:{ all -> 0x0083 }
        r3.<init>();	 Catch:{ all -> 0x0083 }
        r4 = "handleNativeHandler error ";
        r3 = r3.append(r4);	 Catch:{ all -> 0x0083 }
        r0 = r0.toString();	 Catch:{ all -> 0x0083 }
        r0 = r3.append(r0);	 Catch:{ all -> 0x0083 }
        r0 = r0.toString();	 Catch:{ all -> 0x0083 }
        r1.e(r2, r0);	 Catch:{ all -> 0x0083 }
        r0 = org.acra.ACRA.f;
        r1 = org.acra.ACRA.e;
        r2 = "handleNativeException 结束程序";
        r0.e(r1, r2);
        endApplication();
        goto L_0x004c;
    L_0x0083:
        r0 = move-exception;
        r1 = org.acra.ACRA.f;
        r2 = org.acra.ACRA.e;
        r3 = "handleNativeException 结束程序";
        r1.e(r2, r3);
        endApplication();
        throw r0;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.acra.ne.NativeExceptionHandler.handleCrash(org.acra.ne.NativeException):void");
    }

    public static void a(@NonNull NativeException nativeException) {
        String crashFileDirectory = nativeException.getCrashFileDirectory();
        if (new File(crashFileDirectory).exists() && !crashFileDirectory.equals("")) {
            ACRA.getErrorReporter().handleException(nativeException);
        }
    }
}
