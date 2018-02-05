package org.acra.sender;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import java.io.File;
import java.util.Set;
import org.acra.ReportField;
import org.acra.collections.d;
import org.acra.collector.CrashReportData;
import org.acra.config.ACRAConfiguration;

public class a implements ReportSender {
    private final ACRAConfiguration a;

    public a(@NonNull ACRAConfiguration aCRAConfiguration) {
        this.a = aCRAConfiguration;
    }

    public void send(@NonNull Context context, @NonNull CrashReportData crashReportData, @NonNull ACRAConfiguration aCRAConfiguration, @NonNull File file, @NonNull String str, int i) {
        String str2 = context.getPackageName() + " Crash Report";
        String a = a(crashReportData);
        Intent intent = new Intent("android.intent.action.SENDTO");
        intent.setData(Uri.fromParts("mailto", aCRAConfiguration.mailTo(), null));
        intent.addFlags(268435456);
        intent.putExtra("android.intent.extra.SUBJECT", str2);
        intent.putExtra("android.intent.extra.TEXT", a);
        context.startActivity(intent);
    }

    private String a(@NonNull CrashReportData crashReportData) {
        Set reportFields = this.a.getReportFields();
        if (reportFields.isEmpty()) {
            reportFields = new d(org.acra.a.b);
        }
        StringBuilder stringBuilder = new StringBuilder();
        for (ReportField reportField : r0) {
            stringBuilder.append(reportField.toString()).append('=');
            stringBuilder.append((String) crashReportData.get(reportField));
            stringBuilder.append('\n');
        }
        return stringBuilder.toString();
    }
}
