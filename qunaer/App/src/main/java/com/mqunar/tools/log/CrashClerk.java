package com.mqunar.tools.log;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.os.Build;
import android.os.Build.VERSION;
import android.os.Process;
import android.util.Log;
import com.huawei.hwid.openapi.out.OutReturn.ParamStr;
import com.iflytek.aiui.AIUIConstant;
import com.iflytek.cloud.SpeechConstant;
import com.mqunar.atomenv.AtomEnvConstants;
import com.mqunar.atomenv.OwnerConstant;
import com.mqunar.storage.Storage;
import com.mqunar.tools.CheckUtils;
import com.mqunar.tools.DateTimeUtils;
import com.mqunar.tools.log.QLog.Clerk;
import com.mqunar.tools.log.QLog.GlobalContext;
import com.mqunar.tools.log.QLog.Markable;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import org.apache.commons.io.IOUtils;

public class CrashClerk implements Clerk, Markable {
    private static final ThreadLocal<String> e = new ThreadLocal();
    private Context a;
    private Storage b;
    private Storage c;
    private Storage d;

    public CrashClerk(Context context) {
        Context applicationContext;
        if (context == null) {
            try {
                Method method = Class.forName("com.mqunar.core.basectx.application.QApplication").getMethod("getContext", new Class[0]);
                method.setAccessible(true);
                context = (Context) method.invoke(null, new Object[0]);
            } catch (Throwable th) {
            }
        }
        if (context != null) {
            applicationContext = context.getApplicationContext();
        } else {
            applicationContext = null;
        }
        this.a = applicationContext;
        this.b = Storage.newStorage(this.a, "qunar_cr");
        this.c = Storage.newStorage(this.a, "qunar_ex");
        this.d = Storage.newStorage(this.a, OwnerConstant.STORAGE_OWNER_SYS);
    }

    public void v(String str, Object... objArr) {
    }

    public void d(String str, Object... objArr) {
    }

    public void i(String str, Object... objArr) {
    }

    public void w(String str, Object... objArr) {
    }

    public void e(String str, Object... objArr) {
        e(null, str, objArr);
    }

    public void e(Throwable th, String str, Object... objArr) {
        this.c.putString(b(), "##eb##" + a(th, str, objArr) + "##ee##");
    }

    public void e(Throwable th) {
        e(th, null, new Object[0]);
    }

    public void crash(Throwable th, String str) {
        this.b.putString(b(), a(th, str, new Object[0]));
    }

    public Map<String, String> getCrashes() {
        Map all = this.b.getAll();
        Map<String, String> hashMap = new HashMap();
        for (String str : all.keySet()) {
            Object obj = all.get(str);
            if (obj instanceof String) {
                hashMap.put(str, (String) obj);
            }
        }
        return hashMap;
    }

    public void removeCrash(String str) {
        this.b.remove(str);
    }

    private static String a() {
        int myPid = Process.myPid();
        for (RunningAppProcessInfo runningAppProcessInfo : ((ActivityManager) GlobalContext.getAppContext().getSystemService("activity")).getRunningAppProcesses()) {
            if (runningAppProcessInfo.pid == myPid) {
                return runningAppProcessInfo.processName;
            }
        }
        return null;
    }

    private String a(Throwable th, String str, Object... objArr) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("\n======__=======\n");
        if (str != null && str.length() > 0) {
            try {
                if (objArr.length != 0) {
                    str = String.format(str, objArr);
                }
                stringBuilder.append(str);
            } catch (Throwable th2) {
            }
        }
        try {
            PackageInfo packageInfo = this.a.getPackageManager().getPackageInfo(this.a.getPackageName(), 0);
            stringBuilder.append(packageInfo.packageName);
            stringBuilder.append("-");
            stringBuilder.append(DateTimeUtils.printCalendarByPattern(DateTimeUtils.getCurrentDateTime(), DateTimeUtils.yyyy_MM_dd_HH_mm_ss));
            stringBuilder.append("-");
            stringBuilder.append(packageInfo.versionCode);
            stringBuilder.append("-");
            stringBuilder.append(packageInfo.versionName);
            stringBuilder.append("-");
        } catch (Throwable th3) {
            stringBuilder.append("packageInfoError-");
            stringBuilder.append(th3.getMessage());
            stringBuilder.append('-');
        }
        try {
            stringBuilder.append(this.d.getString(AtomEnvConstants.SYS_PID, "pid"));
            stringBuilder.append("-");
            stringBuilder.append(this.d.getString(AtomEnvConstants.SYS_VID, SpeechConstant.ISV_VID));
            stringBuilder.append("-");
            stringBuilder.append(this.d.getString(AtomEnvConstants.SYS_RCID, "cid"));
            stringBuilder.append(IOUtils.LINE_SEPARATOR_UNIX);
            stringBuilder.append("imei:");
            stringBuilder.append(this.d.getString(AtomEnvConstants.SYS_UID, AIUIConstant.KEY_UID));
            stringBuilder.append(IOUtils.LINE_SEPARATOR_UNIX);
        } catch (Throwable th32) {
            stringBuilder.append("platformInfoError-");
            stringBuilder.append(th32.getMessage());
            stringBuilder.append(IOUtils.LINE_SEPARATOR_UNIX);
        }
        try {
            String str2 = (String) Class.forName("com.mqunar.BuildConfig").getDeclaredField("MILESTONE").get(null);
            if (!CheckUtils.isEmpty(str2)) {
                stringBuilder.append("milestone:");
                stringBuilder.append(str2);
                stringBuilder.append(IOUtils.LINE_SEPARATOR_UNIX);
            }
        } catch (Throwable th4) {
            stringBuilder.append("milestone:");
            stringBuilder.append("notfound");
            stringBuilder.append(IOUtils.LINE_SEPARATOR_UNIX);
        }
        try {
            stringBuilder.append("atom:");
            stringBuilder.append(this.d.getString("sys_atom", "{}"));
            stringBuilder.append(IOUtils.LINE_SEPARATOR_UNIX);
        } catch (Throwable th5) {
            stringBuilder.append("atom:");
            stringBuilder.append(ParamStr.RET_RES_ERROR);
            stringBuilder.append(IOUtils.LINE_SEPARATOR_UNIX);
        }
        try {
            Locale locale = this.a.getResources().getConfiguration().locale;
            for (Field field : Build.class.getDeclaredFields()) {
                field.setAccessible(true);
                stringBuilder.append(field.getName().toLowerCase(locale));
                stringBuilder.append("=");
                stringBuilder.append(field.get(null).toString());
                stringBuilder.append(IOUtils.LINE_SEPARATOR_UNIX);
            }
            for (Field field2 : VERSION.class.getDeclaredFields()) {
                field2.setAccessible(true);
                stringBuilder.append(field2.getName().toLowerCase(locale));
                stringBuilder.append("=");
                stringBuilder.append(field2.get(null).toString());
                stringBuilder.append(IOUtils.LINE_SEPARATOR_UNIX);
            }
            stringBuilder.append("country=").append(locale.getCountry()).append(IOUtils.LINE_SEPARATOR_UNIX);
            stringBuilder.append("language=").append(locale.getLanguage()).append(IOUtils.LINE_SEPARATOR_UNIX);
        } catch (Throwable th6) {
        }
        try {
            stringBuilder.append("process=").append(a()).append(IOUtils.LINE_SEPARATOR_UNIX);
        } catch (Exception e) {
        }
        try {
            stringBuilder.append(Log.getStackTraceString(th));
        } catch (Throwable th7) {
        }
        return stringBuilder.toString();
    }

    private static String b() {
        String str = (String) e.get();
        if (str == null) {
            return String.valueOf(System.currentTimeMillis());
        }
        e.remove();
        return str;
    }

    public void tag(String str) {
        e.set(str);
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        return obj instanceof CrashClerk;
    }

    public int hashCode() {
        return getClass().getName().hashCode();
    }
}
