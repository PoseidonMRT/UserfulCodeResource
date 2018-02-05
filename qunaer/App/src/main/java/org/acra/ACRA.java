package org.acra;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Build.VERSION;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.iflytek.cloud.SpeechConstant;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;
import org.acra.annotation.ReportsCrashes;
import org.acra.config.ACRAConfiguration;
import org.acra.log.a;
import org.acra.log.b;
import org.acra.ne.NativeExceptionHandler;
import org.acra.util.c;
import org.acra.util.e;
import org.acra.util.g;
import org.json.JSONObject;

public final class ACRA {
    public static int a = 1;
    public static boolean b = false;
    public static String c;
    public static String d;
    public static final String e = ACRA.class.getSimpleName();
    @NonNull
    public static a f = new b();
    public static String g = "";
    private static Application h;
    @Nullable
    private static ACRAConfiguration i;
    @Nullable
    private static ErrorReporter j;
    private static OnSharedPreferenceChangeListener k;
    private static ErrorReporter l;

    public final class ENV {
        public static final int BETA = 2;
        public static final int DEV = 3;
        public static final int RELEASE = 1;
    }

    private ACRA() {
    }

    public static void a(int i) {
        switch (i) {
            case 1:
                b = false;
                return;
            case 2:
                b = true;
                return;
            case 3:
                b = true;
                return;
            default:
                return;
        }
    }

    @Nullable
    private static String a(@NonNull String str) {
        try {
            JSONObject jSONObject = new JSONObject(str);
            String string = jSONObject.getString("pid");
            String string2 = jSONObject.getString(SpeechConstant.ISV_VID);
            if (!jSONObject.isNull("cid")) {
                d = jSONObject.getString("cid");
            }
            if (string == null || string.trim().equals("")) {
                return "C参数pid不符合要求 " + str;
            }
            if (string2 == null || string2.trim().equals("")) {
                return "C参数vid不符合要求 " + str;
            }
            c = string2;
            return null;
        } catch (Exception e) {
            return "C参数格式错误 " + e.toString();
        }
    }

    public static boolean init(@NonNull Application application, int i, @NonNull String str) {
        try {
            ACRAConfiguration a = new org.acra.config.b(application).a();
            String a2 = a(str);
            if (a2 != null) {
                throw new RuntimeException(a2 + "，ACRA初始化失败！！！");
            }
            if (!(new c().a(application) || a.monitorAllProcess())) {
                if (a.monitorProcess() == null) {
                    return false;
                }
                if (!Arrays.asList(a.monitorProcess()).contains(c.b(application))) {
                    return false;
                }
            }
            a = i;
            a(a);
            g = str.trim();
            if (((ReportsCrashes) application.getClass().getAnnotation(ReportsCrashes.class)) == null) {
                f.e(e, "ACRA#init(Application) called but no ReportsCrashes annotation on Application " + application.getPackageName());
                return false;
            }
            a(application, a);
            return true;
        } catch (org.acra.config.a e) {
            f.d(e, "Configuration Error - ACRA not started : " + e.getMessage());
            return false;
        }
    }

    private static void a(@NonNull Application application, @NonNull ACRAConfiguration aCRAConfiguration) {
        a(application, aCRAConfiguration, true);
        if (aCRAConfiguration.monitorNativeCrash()) {
            try {
                System.loadLibrary("native_monitor");
                new NativeExceptionHandler().a((Context) application);
            } catch (Throwable th) {
                f.e(e, "init native crash monitor is failed " + th.toString());
            }
        }
        if (aCRAConfiguration.monitorANR()) {
            try {
                new org.acra.anr.b().a((Context) application);
            } catch (Throwable th2) {
                f.e(e, "init ANR monitor is failed " + th2.toString());
            }
        }
    }

    private static void a(@NonNull Application application, @NonNull ACRAConfiguration aCRAConfiguration, boolean z) {
        boolean z2 = false;
        boolean z3 = VERSION.SDK_INT >= 11;
        if (!z3) {
            f.d(e, "ACRA 4.7.0+ requires HONEYCOMB or greater. ACRA is disabled and will NOT catch crashes or send messages.");
        }
        if (h != null) {
            f.d(e, "ACRA#init called more than once. Won't do anything more.");
            return;
        }
        h = application;
        if (aCRAConfiguration == null) {
            f.e(e, "ACRA#init called but no ACRAConfiguration provided");
            return;
        }
        i = aCRAConfiguration;
        SharedPreferences a = new org.acra.prefs.b(h, i).a();
        if (z3 && !b(a)) {
            z2 = true;
        }
        j = new ErrorReporter(h, i, a, z2, z3, true);
        if (z) {
            e eVar = new e(h, aCRAConfiguration);
            if (aCRAConfiguration.deleteOldUnsentReportsOnApplicationStart()) {
                eVar.a();
            }
            if (aCRAConfiguration.deleteUnapprovedReportsOnApplicationStart()) {
                eVar.b();
            }
            if (z2) {
                eVar.c();
            }
        }
        k = new OnSharedPreferenceChangeListener() {
            public void onSharedPreferenceChanged(@NonNull SharedPreferences sharedPreferences, String str) {
                if ("acra.disable".equals(str) || "acra.enable".equals(str)) {
                    ACRA.getErrorReporter().setEnabled(!ACRA.b(sharedPreferences));
                }
            }
        };
        a.registerOnSharedPreferenceChangeListener(k);
    }

    public static boolean isACRASenderServiceProcess() {
        String a = a();
        f.b(e, "ACRA processName='" + a + '\'');
        return a != null && a.endsWith(":acra");
    }

    @Nullable
    private static String a() {
        try {
            return g.a(new FileInputStream("/proc/self/cmdline")).trim();
        } catch (IOException e) {
            return null;
        }
    }

    @NonNull
    public static ErrorReporter getErrorReporter() {
        if (j != null) {
            return j;
        }
        if (l == null) {
            synchronized (ACRA.class) {
                if (l == null) {
                    l = new ErrorReporter();
                }
            }
        }
        return l;
    }

    private static boolean b(@NonNull SharedPreferences sharedPreferences) {
        boolean z = true;
        boolean z2 = false;
        try {
            String str = "acra.disable";
            if (sharedPreferences.getBoolean("acra.enable", true)) {
                z = false;
            }
            z2 = sharedPreferences.getBoolean(str, z);
        } catch (Exception e) {
        }
        return z2;
    }
}
