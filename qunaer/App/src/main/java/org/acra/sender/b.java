package org.acra.sender;

import android.content.Context;
import android.content.pm.PackageManager.NameNotFoundException;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import java.io.File;
import java.util.LinkedList;
import java.util.List;
import org.acra.ACRA;
import org.acra.collector.CrashReportData;
import org.acra.config.ACRAConfiguration;
import org.acra.config.d;
import org.acra.config.d.a;
import org.acra.file.c;

public class b {
    private final Context a;
    private final ACRAConfiguration b;
    private final List<ReportSender> c;

    b(@NonNull Context context, @NonNull ACRAConfiguration aCRAConfiguration, @NonNull List<ReportSender> list) {
        this.a = context;
        this.b = aCRAConfiguration;
        this.c = list;
    }

    public void a(@NonNull File file, @NonNull String str, int i) {
        ACRA.f.c(ACRA.e, "Sending report " + file);
        try {
            a(new c().a(file), file, str, i);
        } catch (Throwable e) {
            ACRA.f.c(ACRA.e, "Failed to send crash reports for " + file, e);
        } catch (Throwable e2) {
            ACRA.f.c(ACRA.e, "Failed to load crash report for " + file, e2);
        } catch (Throwable e22) {
            ACRA.f.c(ACRA.e, "Failed to send crash report for " + file, e22);
        }
    }

    private void a(@NonNull CrashReportData crashReportData, File file, @NonNull String str, int i) {
        if (!b() || this.b.sendReportsInDevMode()) {
            List<a> linkedList = new LinkedList();
            for (ReportSender reportSender : this.c) {
                try {
                    ACRA.f.b(ACRA.e, "Sending report using " + reportSender.getClass().getName());
                    reportSender.send(this.a, crashReportData, this.b, file, str, i);
                    ACRA.f.b(ACRA.e, "Sent report using " + reportSender.getClass().getName());
                } catch (ReportSenderException e) {
                    linkedList.add(new a(reportSender, e));
                }
            }
            if (linkedList.isEmpty()) {
                ACRA.f.b(ACRA.e, "Report was sent by all senders");
            } else if (a().a(this.c, linkedList)) {
                throw new ReportSenderException("Policy marked this task as incomplete. ACRA will try to send this report again.", ((a) linkedList.get(0)).b());
            } else {
                StringBuilder stringBuilder = new StringBuilder("ReportSenders of classes [");
                for (a a : linkedList) {
                    stringBuilder.append(a.a().getClass().getName());
                    stringBuilder.append(", ");
                }
                stringBuilder.append("] failed, but Policy marked this task as complete. ACRA will not send this report again.");
                ACRA.f.d(ACRA.e, stringBuilder.toString());
            }
        }
    }

    private d a() {
        try {
            return (d) this.b.retryPolicyClass().newInstance();
        } catch (Throwable e) {
            ACRA.f.c(ACRA.e, "Failed to create policy instance of class " + this.b.retryPolicyClass().getName(), e);
        } catch (Throwable e2) {
            ACRA.f.c(ACRA.e, "Failed to create policy instance of class " + this.b.retryPolicyClass().getName(), e2);
        }
        return new org.acra.config.c();
    }

    public static void a(@Nullable File file) {
        if (file != null && file.exists() && !file.delete()) {
            ACRA.f.d(ACRA.e, "Could not delete error report : " + file);
        }
    }

    private boolean b() {
        try {
            if ((this.a.getPackageManager().getApplicationInfo(this.a.getPackageName(), 0).flags & 2) > 0) {
                return true;
            }
            return false;
        } catch (NameNotFoundException e) {
            return false;
        }
    }
}
