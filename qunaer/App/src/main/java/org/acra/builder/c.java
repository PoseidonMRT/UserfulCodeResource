package org.acra.builder;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Looper;
import android.os.Process;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat.Builder;
import java.io.File;
import java.lang.Thread.UncaughtExceptionHandler;
import java.util.Date;
import org.acra.ACRA;
import org.acra.ReportField;
import org.acra.ReportingInteractionMode;
import org.acra.collector.CrashReportData;
import org.acra.config.ACRAConfiguration;
import org.acra.file.e;
import org.acra.prefs.b;
import org.acra.util.k;

public final class c {
    private static int h = 0;
    private final Context a;
    private final ACRAConfiguration b;
    private final org.acra.collector.c c;
    private final LastActivityManager d;
    private final UncaughtExceptionHandler e;
    private final ReportPrimer f;
    private boolean g = false;

    class a {
        private Long a;

        private a() {
        }

        public void a(long j) {
            this.a = Long.valueOf(j);
        }

        public long a() {
            return this.a == null ? 0 : System.currentTimeMillis() - this.a.longValue();
        }
    }

    public c(@NonNull Context context, @NonNull ACRAConfiguration aCRAConfiguration, @NonNull org.acra.collector.c cVar, @NonNull LastActivityManager lastActivityManager, @Nullable UncaughtExceptionHandler uncaughtExceptionHandler, @NonNull ReportPrimer reportPrimer) {
        this.a = context;
        this.b = aCRAConfiguration;
        this.c = cVar;
        this.d = lastActivityManager;
        this.e = uncaughtExceptionHandler;
        this.f = reportPrimer;
    }

    public void a(@Nullable Thread thread, @NonNull Throwable th) {
        if (this.e != null) {
            ACRA.f.c(ACRA.e, "ACRA is disabled for " + this.a.getPackageName() + " - forwarding uncaught Exception on to default ExceptionHandler");
            this.e.uncaughtException(thread, th);
            return;
        }
        ACRA.f.e(ACRA.e, "ACRA is disabled for " + this.a.getPackageName() + " - no default ExceptionHandler");
        ACRA.f.c(ACRA.e, "ACRA caught a " + th.getClass().getSimpleName() + " for " + this.a.getPackageName(), th);
    }

    public boolean a() {
        return this.g;
    }

    public void a(boolean z) {
        this.g = z;
    }

    public void a(@NonNull ReportBuilder reportBuilder) {
        boolean z = true;
        if (this.g) {
            ReportingInteractionMode reportingInteractionMode;
            boolean z2;
            boolean z3;
            this.f.primeReport(this.a, reportBuilder);
            if (reportBuilder.isSendSilently()) {
                reportingInteractionMode = ReportingInteractionMode.SILENT;
                if (this.b.mode() != ReportingInteractionMode.SILENT) {
                    z2 = true;
                } else {
                    z2 = false;
                }
            } else {
                reportingInteractionMode = this.b.mode();
                z2 = false;
            }
            if (reportingInteractionMode == ReportingInteractionMode.TOAST || (this.b.resToastText() != 0 && (reportingInteractionMode == ReportingInteractionMode.NOTIFICATION || reportingInteractionMode == ReportingInteractionMode.DIALOG))) {
                z3 = true;
            } else {
                z3 = false;
            }
            final a aVar = new a();
            if (z3) {
                new Thread(this) {
                    final /* synthetic */ c b;

                    public void run() {
                        Looper.prepare();
                        k.a(this.b.a, this.b.b.resToastText(), 1);
                        aVar.a(System.currentTimeMillis());
                        Looper.loop();
                    }
                }.start();
            }
            CrashReportData a = this.c.a(reportBuilder);
            final File a2 = a(a);
            a(a2, a);
            ACRA.getErrorReporter().clearCustomData();
            SharedPreferences a3 = new b(this.a, this.b).a();
            if (reportingInteractionMode == ReportingInteractionMode.SILENT || reportingInteractionMode == ReportingInteractionMode.TOAST || a3.getBoolean("acra.alwaysaccept", false)) {
                b(z2);
                if (reportingInteractionMode == ReportingInteractionMode.SILENT && !reportBuilder.isEndApplication()) {
                    return;
                }
            } else if (reportingInteractionMode == ReportingInteractionMode.NOTIFICATION) {
                ACRA.f.b(ACRA.e, "Creating Notification.");
                a(a2, reportBuilder);
            }
            if (reportingInteractionMode != ReportingInteractionMode.DIALOG || a3.getBoolean("acra.alwaysaccept", false)) {
                z = false;
            }
            if (z3) {
                final ReportBuilder reportBuilder2 = reportBuilder;
                new Thread(this) {
                    final /* synthetic */ c e;

                    public void run() {
                        ACRA.f.b(ACRA.e, "Waiting for 2000 millis from " + aVar.a + " currentMillis=" + System.currentTimeMillis());
                        long a = 2000 - aVar.a();
                        if (a > 0) {
                            try {
                                Thread.sleep(a);
                            } catch (Throwable e) {
                                ACRA.f.a(ACRA.e, "Interrupted while waiting for Toast to end.", e);
                            }
                        }
                        ACRA.f.b(ACRA.e, "Finished waiting for Toast");
                        this.e.a(reportBuilder2, a2, z);
                    }
                }.start();
                return;
            }
            a(reportBuilder, a2, z);
            return;
        }
        ACRA.f.a(ACRA.e, "ACRA is disabled. Report not sent.");
    }

    private void a(@NonNull ReportBuilder reportBuilder, @NonNull File file, boolean z) {
        if (z) {
            ACRA.f.b(ACRA.e, "Creating CrashReportDialog for " + file);
            Intent b = b(file, reportBuilder);
            b.setFlags(268435456);
            this.a.startActivity(b);
        }
        ACRA.f.b(ACRA.e, "Wait for Toast + worker ended. Kill Application ? " + reportBuilder.isEndApplication());
        if (reportBuilder.isEndApplication()) {
            b(reportBuilder.getUncaughtExceptionThread(), reportBuilder.getException());
        }
    }

    private void b(@Nullable Thread thread, Throwable th) {
        boolean alsoReportToAndroidFramework = this.b.alsoReportToAndroidFramework();
        if ((thread != null ? 1 : null) == null || !alsoReportToAndroidFramework || this.e == null) {
            final Activity lastActivity = this.d.getLastActivity();
            if (lastActivity != null) {
                ACRA.f.b(ACRA.e, "Finishing the last Activity prior to killing the Process");
                lastActivity.runOnUiThread(new Runnable(this) {
                    final /* synthetic */ c b;

                    public void run() {
                        lastActivity.finish();
                        ACRA.f.b(ACRA.e, "Finished " + lastActivity.getClass());
                    }
                });
                if (thread != lastActivity.getMainLooper().getThread()) {
                    this.d.a(100);
                }
                this.d.a();
            }
            try {
                a(this.b).primeEnding(this.a, th);
            } catch (Throwable th2) {
                ACRA.f.e(ACRA.e, "primeEnding failed :" + th2);
            }
            Process.killProcess(Process.myPid());
            System.exit(10);
            return;
        }
        ACRA.f.b(ACRA.e, "Handing Exception on to default ExceptionHandler");
        this.e.uncaughtException(thread, th);
    }

    @NonNull
    private static EndingPrimer a(@NonNull ACRAConfiguration aCRAConfiguration) {
        try {
            return (EndingPrimer) aCRAConfiguration.endingPrimerClass().newInstance();
        } catch (Throwable e) {
            ACRA.f.b(ACRA.e, "Could not construct ReportPrimer from " + aCRAConfiguration.endingPrimerClass() + " - not priming", e);
        } catch (Throwable e2) {
            ACRA.f.b(ACRA.e, "Could not construct ReportPrimer from " + aCRAConfiguration.endingPrimerClass() + " - not priming", e2);
        }
        return new a();
    }

    private void b(boolean z) {
        if (this.g) {
            new org.acra.sender.c(this.a, this.b).a(z, true);
        } else {
            ACRA.f.d(ACRA.e, "Would be sending reports, but ACRA is disabled");
        }
    }

    private void a(@NonNull File file, @NonNull ReportBuilder reportBuilder) {
        NotificationManager notificationManager = (NotificationManager) this.a.getSystemService("notification");
        int resNotifIcon = this.b.resNotifIcon();
        CharSequence text = this.a.getText(this.b.resNotifTickerText());
        long currentTimeMillis = System.currentTimeMillis();
        ACRA.f.b(ACRA.e, "Creating Notification for " + file);
        Intent b = b(file, reportBuilder);
        Context context = this.a;
        int i = h;
        h = i + 1;
        PendingIntent activity = PendingIntent.getActivity(context, i, b, 134217728);
        CharSequence text2 = this.a.getText(this.b.resNotifTitle());
        Notification build = new Builder(this.a).setSmallIcon(resNotifIcon).setTicker(text).setWhen(currentTimeMillis).setAutoCancel(true).setContentTitle(text2).setContentText(this.a.getText(this.b.resNotifText())).setContentIntent(activity).build();
        build.flags |= 16;
        Intent b2 = b(file, reportBuilder);
        b2.putExtra("FORCE_CANCEL", true);
        build.deleteIntent = PendingIntent.getActivity(this.a, -1, b2, 0);
        notificationManager.notify(666, build);
    }

    @NonNull
    private File a(@NonNull CrashReportData crashReportData) {
        Object property = crashReportData.getProperty(ReportField.USER_CRASH_DATE);
        String property2 = crashReportData.getProperty(ReportField.IS_SILENT);
        StringBuilder stringBuilder = new StringBuilder();
        if (property == null) {
            property = Long.valueOf(new Date().getTime());
        }
        return new File(new e(this.a).a(), stringBuilder.append(property).append(property2 != null ? org.acra.a.a : "").append(".stacktrace").toString());
    }

    private void a(@NonNull File file, @NonNull CrashReportData crashReportData) {
        try {
            ACRA.f.b(ACRA.e, "Writing crash report file " + file);
            new org.acra.file.c().a(crashReportData, file);
        } catch (Throwable e) {
            ACRA.f.c(ACRA.e, "An error occurred while writing the report file...", e);
        }
    }

    @NonNull
    private Intent b(@NonNull File file, @NonNull ReportBuilder reportBuilder) {
        ACRA.f.b(ACRA.e, "Creating DialogIntent for " + file + " exception=" + reportBuilder.getException());
        Intent intent = new Intent(this.a, this.b.reportDialogClass());
        intent.putExtra("REPORT_FILE", file);
        intent.putExtra("REPORT_EXCEPTION", reportBuilder.getException());
        intent.putExtra("REPORT_CONFIG", this.b);
        return intent;
    }
}
