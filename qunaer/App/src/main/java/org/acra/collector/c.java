package org.acra.collector;

import android.app.ActivityManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.os.Build;
import android.os.Build.VERSION;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.UUID;
import org.acra.ACRA;
import org.acra.ReportField;
import org.acra.anr.ANRException;
import org.acra.builder.LastActivityManager;
import org.acra.builder.ReportBuilder;
import org.acra.config.ACRAConfiguration;
import org.acra.ne.NativeException;
import org.acra.util.PackageManagerWrapper;
import org.acra.util.h;
import org.acra.util.j;
import org.apache.commons.io.IOUtils;

public final class c {
    private final Context a;
    private final ACRAConfiguration b;
    private final SharedPreferences c;
    private final Map<String, String> d = new LinkedHashMap();
    private final Calendar e;
    private final String f;
    private final LastActivityManager g;

    public c(@NonNull Context context, @NonNull ACRAConfiguration aCRAConfiguration, @NonNull SharedPreferences sharedPreferences, @NonNull Calendar calendar, @Nullable String str, @NonNull LastActivityManager lastActivityManager) {
        this.a = context;
        this.b = aCRAConfiguration;
        this.c = sharedPreferences;
        this.e = calendar;
        this.f = str;
        this.g = lastActivityManager;
    }

    public String a(@NonNull String str, String str2) {
        return (String) this.d.put(str, str2);
    }

    public String a(@NonNull String str) {
        return (String) this.d.remove(str);
    }

    public void a() {
        this.d.clear();
    }

    public String b(@NonNull String str) {
        return (String) this.d.get(str);
    }

    @NonNull
    public CrashReportData a(@NonNull ReportBuilder reportBuilder) {
        CrashReportData crashReportData = new CrashReportData();
        Set reportFields = this.b.getReportFields();
        try {
            if (reportFields.contains(ReportField.SCREENSHOT) && !(((reportBuilder.getException() instanceof NativeException) && ((NativeException) reportBuilder.getException()).noSendDmp) || reportBuilder.getException().getClass().getSimpleName().contains("Violation"))) {
                crashReportData.put(ReportField.SCREENSHOT, new r(this.g).a(this.a));
            }
        } catch (Throwable th) {
            ACRA.f.e(ACRA.e, "screen failed: " + th);
        }
        try {
            String a;
            if (reportBuilder.getException() instanceof ANRException) {
                try {
                    switch (((ANRException) reportBuilder.getException()).monitorMode) {
                        case 1:
                            a("ANR MONITOR", "FileObserver");
                            break;
                        case 2:
                            a("ANR MONITOR", "WatchDog");
                            break;
                    }
                    a = v.a(this.a, (ANRException) reportBuilder.getException());
                    if (!"".equals(a)) {
                        crashReportData.put(ReportField.TRACES_FILE, a);
                    }
                } catch (Throwable th2) {
                    ACRA.f.c(ACRA.e, "Error while retrieving traces file", th2);
                }
            }
            if (reportBuilder.getException() instanceof NativeException) {
                try {
                    crashReportData.put(ReportField.NATIVE_CRASH, ((NativeException) reportBuilder.getException()).getCrashFileDirectory());
                } catch (Throwable th22) {
                    ACRA.f.c(ACRA.e, "Error while retrieving Native data", th22);
                }
                try {
                    crashReportData.put(ReportField.JAVA_STACK_TRACE, k.a(reportBuilder.getMessage(), reportBuilder.getException()));
                } catch (Throwable th222) {
                    ACRA.f.c(ACRA.e, "Error while retrieving JAVA_STACK_TRACE data", th222);
                }
                if (((NativeException) reportBuilder.getException()).noSendDmp) {
                    try {
                        a("no_send_dump", ((NativeException) reportBuilder.getException()).noSendDmp + "");
                    } catch (Throwable th2222) {
                        ACRA.f.c(ACRA.e, "put no send dmp file", th2222);
                    }
                }
            }
            try {
                crashReportData.put(ReportField.STACK_TRACE, a(reportBuilder.getMessage(), reportBuilder.getException()));
            } catch (Throwable th22222) {
                ACRA.f.c(ACRA.e, "Error while retrieving STACK_TRACE data", th22222);
            }
            if (reportFields.contains(ReportField.ENV)) {
                try {
                    crashReportData.put(ReportField.ENV, h.a());
                } catch (Throwable th222222) {
                    ACRA.f.c(ACRA.e, "Error while retrieving ENV data", th222222);
                }
            }
            if (reportFields.contains(ReportField.NETWORK_STATE)) {
                try {
                    CharSequence a2 = l.a(this.a);
                    if (!TextUtils.isEmpty(a2)) {
                        crashReportData.put(ReportField.NETWORK_STATE, a2);
                    }
                } catch (Throwable th2222222) {
                    ACRA.f.c(ACRA.e, "Error while retrieving network state data", th2222222);
                }
            }
            if (reportFields.contains(ReportField.ACRA_VERSION)) {
                try {
                    crashReportData.put(ReportField.ACRA_VERSION, "2.1.2");
                } catch (Throwable th22222222) {
                    ACRA.f.c(ACRA.e, "Error while retrieving ACRA——VERSION data", th22222222);
                }
            }
            if (reportFields.contains(ReportField.VM_VERSION)) {
                try {
                    crashReportData.put(ReportField.VM_VERSION, x.a());
                } catch (Throwable th222222222) {
                    ACRA.f.c(ACRA.e, "Error while retrieving VM_VERSION data", th222222222);
                }
            }
            if (reportFields.contains(ReportField.PROPERTY)) {
                try {
                    crashReportData.put(ReportField.PROPERTY, o.a());
                } catch (Throwable th2222222222) {
                    ACRA.f.c(ACRA.e, "Error while retrieving PROPERTY data", th2222222222);
                }
            }
            try {
                if (ActivityManager.isUserAMonkey()) {
                    a("monkey", "true");
                }
            } catch (Throwable th22222222222) {
                ACRA.f.c(ACRA.e, "Error while put monkey data", th22222222222);
            }
            PackageManagerWrapper packageManagerWrapper = new PackageManagerWrapper(this.a);
            try {
                boolean hasPermission;
                if (VERSION.SDK_INT >= 16) {
                    hasPermission = packageManagerWrapper.hasPermission("android.permission.READ_EXTERNAL_STORAGE");
                } else {
                    hasPermission = true;
                }
                if (hasPermission && a.a(this.a)) {
                    a("cloud", "true");
                }
            } catch (Throwable th222222222222) {
                ACRA.f.e(ACRA.e, "cannot get cloudTestInfo" + th222222222222.toString());
            }
            Object obj = (packageManagerWrapper.hasPermission("android.permission.READ_LOGS") || VERSION.SDK_INT >= 16) ? 1 : null;
            if (!this.c.getBoolean("acra.syslog.enable", true) || obj == null) {
                ACRA.f.b(ACRA.e, "READ_LOGS not allowed. ACRA will not include LogCat and DropBox data.");
            } else {
                ACRA.f.b(ACRA.e, "READ_LOGS granted! ACRA can include LogCat and DropBox data.");
                i iVar = new i();
                if (reportFields.contains(ReportField.LOGCAT)) {
                    try {
                        crashReportData.put(ReportField.LOGCAT, iVar.a(this.b, null));
                    } catch (Throwable th2222222222222) {
                        ACRA.f.c(ACRA.e, "Error while retrieving LOGCAT data", th2222222222222);
                    }
                }
                if (reportFields.contains(ReportField.EVENTSLOG)) {
                    try {
                        crashReportData.put(ReportField.EVENTSLOG, iVar.a(this.b, "events"));
                    } catch (Throwable th22222222222222) {
                        ACRA.f.c(ACRA.e, "Error while retrieving EVENTSLOG data", th22222222222222);
                    }
                }
                if (reportFields.contains(ReportField.RADIOLOG)) {
                    try {
                        crashReportData.put(ReportField.RADIOLOG, iVar.a(this.b, "radio"));
                    } catch (Throwable th222222222222222) {
                        ACRA.f.c(ACRA.e, "Error while retrieving RADIOLOG data", th222222222222222);
                    }
                }
                if (reportFields.contains(ReportField.DROPBOX)) {
                    try {
                        crashReportData.put(ReportField.DROPBOX, new f().a(this.a, this.b));
                    } catch (Throwable th2222222222222222) {
                        ACRA.f.c(ACRA.e, "Error while retrieving DROPBOX data", th2222222222222222);
                    }
                }
            }
            try {
                crashReportData.put(ReportField.USER_APP_START_DATE, j.a(this.e));
            } catch (Throwable th22222222222222222) {
                ACRA.f.c(ACRA.e, "Error while retrieving USER_APP_START_DATE data", th22222222222222222);
            }
            if (reportBuilder.isSendSilently()) {
                crashReportData.put(ReportField.IS_SILENT, "true");
            }
            try {
                crashReportData.put(ReportField.REPORT_ID, UUID.randomUUID().toString());
            } catch (Throwable th222222222222222222) {
                ACRA.f.c(ACRA.e, "Error while retrieving REPORT_ID data", th222222222222222222);
            }
            try {
                crashReportData.put(ReportField.USER_CRASH_DATE, j.a(new GregorianCalendar()));
            } catch (Throwable th2222222222222222222) {
                ACRA.f.c(ACRA.e, "Error while retrieving USER_CRASH_DATE data", th2222222222222222222);
            }
            if (reportFields.contains(ReportField.STACK_TRACE_HASH)) {
                try {
                    crashReportData.put(ReportField.STACK_TRACE_HASH, a(reportBuilder.getException()));
                } catch (Throwable th22222222222222222222) {
                    ACRA.f.c(ACRA.e, "Error while retrieving STACK_TRACE_HASH data", th22222222222222222222);
                }
            }
            if (reportFields.contains(ReportField.INSTALLATION_ID)) {
                try {
                    crashReportData.put(ReportField.INSTALLATION_ID, h.a(this.a));
                } catch (Throwable th222222222222222222222) {
                    ACRA.f.c(ACRA.e, "Error while retrieving INSTALLATION_ID data", th222222222222222222222);
                }
            }
            if (reportFields.contains(ReportField.INITIAL_CONFIGURATION)) {
                try {
                    crashReportData.put(ReportField.INITIAL_CONFIGURATION, this.f);
                } catch (Throwable th2222222222222222222222) {
                    ACRA.f.c(ACRA.e, "Error while retrieving INITIAL_CONFIGURATION data", th2222222222222222222222);
                }
            }
            if (reportFields.contains(ReportField.CRASH_CONFIGURATION)) {
                try {
                    crashReportData.put(ReportField.CRASH_CONFIGURATION, b.a(this.a));
                } catch (Throwable th22222222222222222222222) {
                    ACRA.f.c(ACRA.e, "Error while retrieving CRASH_CONFIGURATION data", th22222222222222222222222);
                }
            }
            if (!(reportBuilder.getException() instanceof OutOfMemoryError) && reportFields.contains(ReportField.DUMPSYS_MEMINFO)) {
                try {
                    crashReportData.put(ReportField.DUMPSYS_MEMINFO, g.a());
                } catch (Throwable th222222222222222222222222) {
                    ACRA.f.c(ACRA.e, "Error while retrieving DUMPSYS_MEMINFO data", th222222222222222222222222);
                }
            }
            if (reportFields.contains(ReportField.PACKAGE_NAME)) {
                try {
                    crashReportData.put(ReportField.PACKAGE_NAME, this.a.getPackageName());
                } catch (Throwable th2222222222222222222222222) {
                    ACRA.f.c(ACRA.e, "Error while retrieving PACKAGE_NAME data", th2222222222222222222222222);
                }
            }
            if (reportFields.contains(ReportField.BUILD)) {
                try {
                    crashReportData.put(ReportField.BUILD, p.b(Build.class) + p.a(VERSION.class, "VERSION"));
                } catch (Throwable th22222222222222222222222222) {
                    ACRA.f.c(ACRA.e, "Error while retrieving BUILD data", th22222222222222222222222222);
                }
            }
            if (reportFields.contains(ReportField.PHONE_MODEL)) {
                try {
                    crashReportData.put(ReportField.PHONE_MODEL, Build.MODEL);
                } catch (Throwable th222222222222222222222222222) {
                    ACRA.f.c(ACRA.e, "Error while retrieving PHONE_MODEL data", th222222222222222222222222222);
                }
            }
            if (reportFields.contains(ReportField.ANDROID_VERSION)) {
                try {
                    crashReportData.put(ReportField.ANDROID_VERSION, VERSION.RELEASE);
                } catch (Throwable th2222222222222222222222222222) {
                    ACRA.f.c(ACRA.e, "Error while retrieving ANDROID_VERSION data", th2222222222222222222222222222);
                }
            }
            if (reportFields.contains(ReportField.BRAND)) {
                try {
                    crashReportData.put(ReportField.BRAND, Build.BRAND);
                } catch (Throwable th22222222222222222222222222222) {
                    ACRA.f.c(ACRA.e, "Error while retrieving BRAND data", th22222222222222222222222222222);
                }
            }
            if (reportFields.contains(ReportField.PRODUCT)) {
                try {
                    crashReportData.put(ReportField.PRODUCT, Build.PRODUCT);
                } catch (Throwable th222222222222222222222222222222) {
                    ACRA.f.c(ACRA.e, "Error while retrieving PRODUCT data", th222222222222222222222222222222);
                }
            }
            if (reportFields.contains(ReportField.TOTAL_MEM_SIZE)) {
                try {
                    crashReportData.put(ReportField.TOTAL_MEM_SIZE, Long.toString(j.b()));
                } catch (Throwable th2222222222222222222222222222222) {
                    ACRA.f.c(ACRA.e, "Error while retrieving TOTAL_MEM_SIZE data", th2222222222222222222222222222222);
                }
            }
            if (reportFields.contains(ReportField.AVAILABLE_MEM_SIZE)) {
                try {
                    crashReportData.put(ReportField.AVAILABLE_MEM_SIZE, Long.toString(j.a()));
                } catch (Throwable th22222222222222222222222222222222) {
                    ACRA.f.c(ACRA.e, "Error while retrieving AVAILABLE_MEM_SIZE data", th22222222222222222222222222222222);
                }
            }
            if (reportFields.contains(ReportField.FILE_PATH)) {
                try {
                    crashReportData.put(ReportField.FILE_PATH, j.b(this.a));
                } catch (Throwable th222222222222222222222222222222222) {
                    ACRA.f.c(ACRA.e, "Error while retrieving FILE_PATH data", th222222222222222222222222222222222);
                }
            }
            if (reportFields.contains(ReportField.DISPLAY)) {
                try {
                    crashReportData.put(ReportField.DISPLAY, e.a(this.a));
                } catch (Throwable th2222222222222222222222222222222222) {
                    ACRA.f.c(ACRA.e, "Error while retrieving DISPLAY data", th2222222222222222222222222222222222);
                }
            }
            if (reportFields.contains(ReportField.BUILD_CONFIG)) {
                try {
                    crashReportData.put(ReportField.BUILD_CONFIG, p.b(b()));
                } catch (ClassNotFoundException e) {
                } catch (Throwable th22222222222222222222222222222222222) {
                    ACRA.f.c(ACRA.e, "Error while retrieving BUILD_CONFIG data", th22222222222222222222222222222222222);
                }
            }
            if (reportFields.contains(ReportField.USER_EMAIL)) {
                try {
                    crashReportData.put(ReportField.USER_EMAIL, this.c.getString("acra.user.email", "N/A"));
                } catch (Throwable th222222222222222222222222222222222222) {
                    ACRA.f.c(ACRA.e, "Error while retrieving USER_EMAIL data", th222222222222222222222222222222222222);
                }
            }
            if (reportFields.contains(ReportField.DEVICE_FEATURES)) {
                try {
                    crashReportData.put(ReportField.DEVICE_FEATURES, d.a(this.a));
                } catch (Throwable th2222222222222222222222222222222222222) {
                    ACRA.f.c(ACRA.e, "Error while retrieving DEVICE_FEATURES data", th2222222222222222222222222222222222222);
                }
            }
            if (reportFields.contains(ReportField.ENVIRONMENT)) {
                try {
                    crashReportData.put(ReportField.ENVIRONMENT, p.a(Environment.class));
                } catch (Throwable th22222222222222222222222222222222222222) {
                    ACRA.f.c(ACRA.e, "Error while retrieving ENVIRONMENT data", th22222222222222222222222222222222222222);
                }
            }
            s sVar = new s(this.a, this.b);
            if (reportFields.contains(ReportField.SETTINGS_SYSTEM)) {
                try {
                    crashReportData.put(ReportField.SETTINGS_SYSTEM, sVar.a());
                } catch (Throwable th222222222222222222222222222222222222222) {
                    ACRA.f.c(ACRA.e, "Error while retrieving SETTINGS_SYSTEM data", th222222222222222222222222222222222222222);
                }
            }
            if (reportFields.contains(ReportField.SETTINGS_SECURE)) {
                try {
                    crashReportData.put(ReportField.SETTINGS_SECURE, sVar.b());
                } catch (Throwable th2222222222222222222222222222222222222222) {
                    ACRA.f.c(ACRA.e, "Error while retrieving SETTINGS_SECURE data", th2222222222222222222222222222222222222222);
                }
            }
            if (reportFields.contains(ReportField.SETTINGS_GLOBAL)) {
                try {
                    crashReportData.put(ReportField.SETTINGS_GLOBAL, sVar.c());
                } catch (Throwable th22222222222222222222222222222222222222222) {
                    ACRA.f.c(ACRA.e, "Error while retrieving SETTINGS_GLOBAL data", th22222222222222222222222222222222222222222);
                }
            }
            if (reportFields.contains(ReportField.SHARED_PREFERENCES)) {
                try {
                    crashReportData.put(ReportField.SHARED_PREFERENCES, new t(this.a, this.b).a());
                } catch (Throwable th222222222222222222222222222222222222222222) {
                    ACRA.f.c(ACRA.e, "Error while retrieving SHARED_PREFERENCES data", th222222222222222222222222222222222222222222);
                }
            }
            try {
                PackageInfo packageInfo = packageManagerWrapper.getPackageInfo();
                if (packageInfo != null) {
                    if (reportFields.contains(ReportField.APP_VERSION_CODE)) {
                        crashReportData.put(ReportField.APP_VERSION_CODE, Integer.toString(packageInfo.versionCode));
                    }
                    if (reportFields.contains(ReportField.APP_VERSION_NAME)) {
                        Enum enumR = ReportField.APP_VERSION_NAME;
                        if (packageInfo.versionName != null) {
                            obj = packageInfo.versionName;
                        } else {
                            obj = "not set";
                        }
                        crashReportData.put(enumR, obj);
                    }
                } else {
                    crashReportData.put(ReportField.APP_VERSION_NAME, "Package info unavailable");
                }
            } catch (Throwable th2222222222222222222222222222222222222222222) {
                ACRA.f.c(ACRA.e, "Error while retrieving APP_VERSION_CODE and APP_VERSION_NAME data", th2222222222222222222222222222222222222222222);
            }
            if (reportFields.contains(ReportField.DEVICE_ID) && packageManagerWrapper.hasPermission("android.permission.READ_PHONE_STATE")) {
                try {
                    a = j.a(this.a);
                    if (a != null) {
                        crashReportData.put(ReportField.DEVICE_ID, a);
                    }
                } catch (Throwable th22222222222222222222222222222222222222222222) {
                    ACRA.f.c(ACRA.e, "Error while retrieving DEVICE_ID data", th22222222222222222222222222222222222222222222);
                }
            }
            if (reportFields.contains(ReportField.APPLICATION_LOG)) {
                try {
                    crashReportData.put(ReportField.APPLICATION_LOG, new j().a(this.a, this.b.applicationLogFile(), this.b.applicationLogFileLines()));
                } catch (Throwable th222222222222222222222222222222222222222222222) {
                    ACRA.f.c(ACRA.e, "Error while reading application log file " + this.b.applicationLogFile(), th222222222222222222222222222222222222222222222);
                } catch (Throwable th2222222222222222222222222222222222222222222222) {
                    ACRA.f.c(ACRA.e, "Error while retrieving APPLICATION_LOG data", th2222222222222222222222222222222222222222222222);
                }
            }
            if (reportFields.contains(ReportField.MEDIA_CODEC_LIST)) {
                try {
                    crashReportData.put(ReportField.MEDIA_CODEC_LIST, MediaCodecListCollector.a());
                } catch (Throwable th22222222222222222222222222222222222222222222222) {
                    ACRA.f.c(ACRA.e, "Error while retrieving MEDIA_CODEC_LIST data", th22222222222222222222222222222222222222222222222);
                }
            }
            if (reportFields.contains(ReportField.THREAD_DETAILS)) {
                try {
                    crashReportData.put(ReportField.THREAD_DETAILS, u.a(reportBuilder.getUncaughtExceptionThread()));
                } catch (Throwable th222222222222222222222222222222222222222222222222) {
                    ACRA.f.c(ACRA.e, "Error while retrieving THREAD_DETAILS data", th222222222222222222222222222222222222222222222222);
                }
            }
            if (reportFields.contains(ReportField.USER_IP)) {
                try {
                    crashReportData.put(ReportField.USER_IP, j.c());
                } catch (Throwable th2222222222222222222222222222222222222222222222222) {
                    ACRA.f.c(ACRA.e, "Error while retrieving USER_IP data", th2222222222222222222222222222222222222222222222222);
                }
            }
            if (reportFields.contains(ReportField.PROCESS_NAME)) {
                try {
                    crashReportData.put(ReportField.PROCESS_NAME, n.a(this.a));
                } catch (Throwable th22222222222222222222222222222222222222222222222222) {
                    ACRA.f.c(ACRA.e, "Error while retrieving process name", th22222222222222222222222222222222222222222222222222);
                }
            }
            if (reportFields.contains(ReportField.PROCESS_ID)) {
                try {
                    crashReportData.put(ReportField.PROCESS_ID, m.a());
                } catch (Throwable th222222222222222222222222222222222222222222222222222) {
                    ACRA.f.c(ACRA.e, "Error while retrieving process id", th222222222222222222222222222222222222222222222222222);
                }
            }
            if (reportFields.contains(ReportField.CUSTOM_DATA)) {
                try {
                    crashReportData.put(ReportField.CUSTOM_DATA, a(reportBuilder.getCustomData()));
                } catch (Throwable th2222222222222222222222222222222222222222222222222222) {
                    ACRA.f.c(ACRA.e, "Error while retrieving CUSTOM_DATA data", th2222222222222222222222222222222222222222222222222222);
                }
            }
            if (reportFields.contains(ReportField.IS_ROOT)) {
                try {
                    crashReportData.put(ReportField.IS_ROOT, q.a());
                } catch (Throwable th22222222222222222222222222222222222222222222222222222) {
                    ACRA.f.c(ACRA.e, "Error while retrieving root data", th22222222222222222222222222222222222222222222222222222);
                }
            }
        } catch (Throwable th222222222222222222222222222222222222222222222222222222) {
            ACRA.f.c(ACRA.e, "Error while retrieving crash data", th222222222222222222222222222222222222222222222222222222);
        }
        return crashReportData;
    }

    @NonNull
    private String a(@Nullable Map<String, String> map) {
        Map map2 = this.d;
        Map hashMap;
        if (map != null) {
            hashMap = new HashMap(map2);
            hashMap.putAll(map);
        } else {
            hashMap = map2;
        }
        StringBuilder stringBuilder = new StringBuilder();
        for (Entry entry : r0.entrySet()) {
            stringBuilder.append((String) entry.getKey());
            stringBuilder.append(" = ");
            String str = (String) entry.getValue();
            if (str != null) {
                stringBuilder.append(str.replaceAll(IOUtils.LINE_SEPARATOR_UNIX, "\\\\n"));
            } else {
                stringBuilder.append("null");
            }
            stringBuilder.append('\n');
        }
        return stringBuilder.toString();
    }

    @NonNull
    static String a(@Nullable String str, @Nullable Throwable th) {
        String obj;
        Writer stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);
        if (!(str == null || TextUtils.isEmpty(str))) {
            printWriter.println(str);
        }
        for (Throwable th2 = th; th2 != null; th2 = th2.getCause()) {
            th2.printStackTrace(printWriter);
        }
        if (th == null || !(th instanceof ANRException)) {
            if (th == null || !th.getClass().getName().contains("Violation")) {
                obj = stringWriter.toString();
            } else {
                obj = th.getClass().getName() + "：" + th.getMessage();
            }
        } else if (((ANRException) th).mainThreadInfo != null) {
            obj = ((ANRException) th).mainThreadInfo + ((ANRException) th).ANRInfo;
        } else {
            obj = stringWriter.toString() + ((ANRException) th).ANRInfo;
        }
        printWriter.close();
        return obj;
    }

    @NonNull
    private String a(@Nullable Throwable th) {
        StringBuilder stringBuilder = new StringBuilder();
        while (th != null) {
            for (StackTraceElement stackTraceElement : th.getStackTrace()) {
                stringBuilder.append(stackTraceElement.getClassName());
                stringBuilder.append(stackTraceElement.getMethodName());
            }
            th = th.getCause();
        }
        return Integer.toHexString(stringBuilder.toString().hashCode());
    }

    @NonNull
    private Class<?> b() {
        Class<?> buildConfigClass = this.b.buildConfigClass();
        if (buildConfigClass.equals(Object.class)) {
            String str = this.a.getPackageName() + ".BuildConfig";
            try {
                buildConfigClass = Class.forName(str);
            } catch (ClassNotFoundException e) {
                ACRA.f.e(ACRA.e, "Not adding buildConfig to log. Class Not found : " + str + ". Please configure 'buildConfigClass' in your ACRA config");
                throw e;
            }
        }
        return buildConfigClass;
    }
}
