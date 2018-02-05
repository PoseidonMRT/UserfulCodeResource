package org.acra;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Process;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.iflytek.aiui.AIUIConstant;
import com.iflytek.cloud.SpeechConstant;
import java.lang.Thread.UncaughtExceptionHandler;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import org.acra.builder.LastActivityManager;
import org.acra.builder.ReportBuilder;
import org.acra.builder.ReportPrimer;
import org.acra.collector.b;
import org.acra.collector.c;
import org.acra.config.ACRAConfiguration;
import org.acra.util.PackageManagerWrapper;
import org.acra.util.e;
import org.acra.util.j;

public class ErrorReporter implements UncaughtExceptionHandler {
    @NonNull
    private final ACRAConfiguration config;
    private final Application context;
    @NonNull
    private final c crashReportDataFactory;
    private boolean errorReporterEnable;
    @NonNull
    private volatile b exceptionHandlerInitializer;
    private final List<BeforeWriteCrashAdder> mBeforeWriteCrashAdders;
    public LastActivityManager mLastActivityManager;
    @NonNull
    private final org.acra.builder.c reportExecutor;
    private final boolean supportedAndroidVersion;

    ErrorReporter() {
        this.mBeforeWriteCrashAdders = new ArrayList();
        this.exceptionHandlerInitializer = new b(this) {
            final /* synthetic */ ErrorReporter a;

            {
                this.a = r1;
            }

            public void a(ErrorReporter errorReporter) {
            }
        };
        this.errorReporterEnable = true;
        this.errorReporterEnable = false;
        this.supportedAndroidVersion = false;
        this.context = null;
        this.config = null;
        this.crashReportDataFactory = null;
        this.reportExecutor = null;
    }

    ErrorReporter(@NonNull Application application, @NonNull ACRAConfiguration aCRAConfiguration, @NonNull SharedPreferences sharedPreferences, boolean z, boolean z2, boolean z3) {
        String a;
        UncaughtExceptionHandler defaultUncaughtExceptionHandler;
        this.mBeforeWriteCrashAdders = new ArrayList();
        this.exceptionHandlerInitializer = /* anonymous class already generated */;
        this.errorReporterEnable = true;
        this.context = application;
        this.config = aCRAConfiguration;
        this.supportedAndroidVersion = z2;
        if (aCRAConfiguration.getReportFields().contains(ReportField.INITIAL_CONFIGURATION)) {
            a = b.a(this.context);
        } else {
            a = null;
        }
        Calendar gregorianCalendar = new GregorianCalendar();
        this.mLastActivityManager = new LastActivityManager(this.context, aCRAConfiguration);
        this.crashReportDataFactory = new c(this.context, aCRAConfiguration, sharedPreferences, gregorianCalendar, a, this.mLastActivityManager);
        if (z3) {
            defaultUncaughtExceptionHandler = Thread.getDefaultUncaughtExceptionHandler();
            Thread.setDefaultUncaughtExceptionHandler(this);
        } else {
            defaultUncaughtExceptionHandler = null;
        }
        Context context = application;
        ACRAConfiguration aCRAConfiguration2 = aCRAConfiguration;
        this.reportExecutor = new org.acra.builder.c(context, aCRAConfiguration2, this.crashReportDataFactory, this.mLastActivityManager, defaultUncaughtExceptionHandler, getReportPrimer(aCRAConfiguration));
        this.reportExecutor.a(z);
    }

    @Deprecated
    public void addCustomData(@NonNull String str, String str2) {
        if (this.errorReporterEnable) {
            putCustomData(str, str2);
        }
    }

    public String putCustomData(@NonNull String str, String str2) {
        if (this.errorReporterEnable) {
            return this.crashReportDataFactory.a(str, str2);
        }
        return "";
    }

    public void setExceptionHandlerInitializer(@Nullable b bVar) {
        if (this.errorReporterEnable) {
            if (bVar == null) {
                bVar = new b(this) {
                    final /* synthetic */ ErrorReporter a;

                    {
                        this.a = r1;
                    }

                    public void a(ErrorReporter errorReporter) {
                    }
                };
            }
            this.exceptionHandlerInitializer = bVar;
        }
    }

    public String removeCustomData(@NonNull String str) {
        if (this.errorReporterEnable) {
            return this.crashReportDataFactory.a(str);
        }
        return "";
    }

    public void clearCustomData() {
        if (this.errorReporterEnable) {
            this.crashReportDataFactory.a();
        }
    }

    public String getCustomData(@NonNull String str) {
        if (this.errorReporterEnable) {
            return this.crashReportDataFactory.b(str);
        }
        return "";
    }

    public void addBeforeWriteCrashAdder(BeforeWriteCrashAdder beforeWriteCrashAdder) {
        if (this.errorReporterEnable) {
            this.mBeforeWriteCrashAdders.add(beforeWriteCrashAdder);
        }
    }

    private void exeAdders(@NonNull Throwable th) {
        if (this.mBeforeWriteCrashAdders != null && this.mBeforeWriteCrashAdders.size() != 0) {
            for (BeforeWriteCrashAdder beforeWriteCrashAdder : this.mBeforeWriteCrashAdders) {
                if (beforeWriteCrashAdder != null) {
                    try {
                        beforeWriteCrashAdder.add(this.context, th);
                    } catch (Throwable th2) {
                        ACRA.f.e(ACRA.e, "BeforeWriteCrashAdder.add " + th2.toString());
                    }
                }
            }
        }
    }

    private void setIdToCustomData() {
        putCustomData(SpeechConstant.ISV_VID, ACRA.c);
        if (!"".equals(ACRA.d)) {
            putCustomData("cid", ACRA.d);
        }
        if (new PackageManagerWrapper(this.context).hasPermission("android.permission.READ_PHONE_STATE")) {
            putCustomData(AIUIConstant.KEY_UID, j.a(this.context));
        }
    }

    public void uncaughtException(@Nullable Thread thread, @NonNull Throwable th) {
        if (this.errorReporterEnable) {
            if (!LastActivityManager.a) {
                dealBackgroundCrashInfo(th);
            }
            if (this.reportExecutor.a()) {
                try {
                    setIdToCustomData();
                } catch (Throwable th2) {
                    ACRA.f.e(ACRA.e, "setCParam is failed " + th2.toString());
                }
                try {
                    exeAdders(th);
                } catch (Throwable th22) {
                    ACRA.f.e(ACRA.e, "adder is failed " + th22.toString());
                }
                try {
                    ACRA.f.c(ACRA.e, "ACRA caught a " + th.getClass().getSimpleName() + " for " + this.context.getPackageName(), th);
                    ACRA.f.b(ACRA.e, "Building report");
                    performDeprecatedReportPriming();
                    new ReportBuilder().uncaughtExceptionThread(thread).exception(th).endApplication().build(this.reportExecutor);
                    return;
                } catch (Throwable th222) {
                    ACRA.f.c(ACRA.e, "ACRA failed to capture the error - handing off to native error reporter", th222);
                    this.reportExecutor.a(thread, th);
                    return;
                }
            }
            this.reportExecutor.a(thread, th);
        }
    }

    private void dealBackgroundCrashInfo(Throwable th) {
        SharedPreferences a = new org.acra.prefs.b(this.context, this.config).a();
        int i = a.getInt("acra.backgroundCrashCount", 0) + 1;
        a.edit().putInt("acra.backgroundCrashCount", i).commit();
        if (i > 5) {
            ACRA.f.c(ACRA.e, "ACRA caught a " + th.getClass().getSimpleName() + " for " + this.context.getPackageName(), th);
            ACRA.f.b(ACRA.e, "not get exception info , because backgroundCrashCount > 5");
            Process.killProcess(Process.myPid());
            System.exit(10);
        }
    }

    private boolean dealSilentBackgroundCrashInfo(Throwable th) {
        SharedPreferences a = new org.acra.prefs.b(this.context, this.config).a();
        int i = a.getInt("acra.silentBackgroundCrashCount", 0) + 1;
        a.edit().putInt("acra.silentBackgroundCrashCount", i).apply();
        if (i <= 5) {
            return false;
        }
        ACRA.f.b(ACRA.e, "not handler silent exception info , because silentBackgroundCrashCount > 5 silent exception : " + th);
        return true;
    }

    public void handleSilentException(@Nullable Throwable th) {
        if (!this.errorReporterEnable) {
            return;
        }
        if (LastActivityManager.a || !dealSilentBackgroundCrashInfo(th)) {
            try {
                setIdToCustomData();
            } catch (Throwable th2) {
                ACRA.f.e(ACRA.e, "setCParam is failed " + th2.toString());
            }
            if (th != null) {
                try {
                    exeAdders(th);
                } catch (Throwable th22) {
                    ACRA.f.e(ACRA.e, "adder is failed " + th22.toString());
                }
            }
            performDeprecatedReportPriming();
            new ReportBuilder().exception(th).sendSilently().build(this.reportExecutor);
        }
    }

    public void setEnabled(boolean z) {
        if (!this.errorReporterEnable) {
            return;
        }
        if (this.supportedAndroidVersion) {
            ACRA.f.c(ACRA.e, "ACRA is " + (z ? "enabled" : "disabled") + " for " + this.context.getPackageName());
            this.reportExecutor.a(z);
            return;
        }
        ACRA.f.d(ACRA.e, "ACRA 4.7.0+ requires Froyo or greater. ACRA is disabled and will NOT catch crashes or send messages.");
    }

    public void checkReportsOnApplicationStart() {
        if (this.errorReporterEnable) {
            e eVar = new e(this.context, this.config);
            if (this.config.deleteOldUnsentReportsOnApplicationStart()) {
                eVar.a();
            }
            if (this.config.deleteUnapprovedReportsOnApplicationStart()) {
                eVar.b();
            }
            if (this.reportExecutor.a()) {
                eVar.c();
            }
        }
    }

    public void handleException(@Nullable Throwable th, boolean z) {
        if (this.errorReporterEnable) {
            try {
                setIdToCustomData();
            } catch (Throwable th2) {
                ACRA.f.e(ACRA.e, "setCParam is failed " + th2.toString());
            }
            if (th != null) {
                try {
                    exeAdders(th);
                } catch (Throwable th22) {
                    ACRA.f.e(ACRA.e, "adder is failed " + th22.toString());
                }
            }
            performDeprecatedReportPriming();
            ReportBuilder reportBuilder = new ReportBuilder();
            reportBuilder.exception(th);
            if (z) {
                reportBuilder.endApplication();
            }
            reportBuilder.build(this.reportExecutor);
        }
    }

    public void handleException(@Nullable Throwable th) {
        handleException(th, false);
    }

    private void performDeprecatedReportPriming() {
        try {
            this.exceptionHandlerInitializer.a(this);
        } catch (Throwable th) {
            ACRA.f.d(ACRA.e, "Failed to initialize " + this.exceptionHandlerInitializer + th);
        }
    }

    @NonNull
    private static ReportPrimer getReportPrimer(@NonNull ACRAConfiguration aCRAConfiguration) {
        try {
            return (ReportPrimer) aCRAConfiguration.reportPrimerClass().newInstance();
        } catch (Throwable e) {
            ACRA.f.b(ACRA.e, "Could not construct ReportPrimer from " + aCRAConfiguration.reportPrimerClass() + " - not priming", e);
        } catch (Throwable e2) {
            ACRA.f.b(ACRA.e, "Could not construct ReportPrimer from " + aCRAConfiguration.reportPrimerClass() + " - not priming", e2);
        }
        return new org.acra.builder.b();
    }
}
