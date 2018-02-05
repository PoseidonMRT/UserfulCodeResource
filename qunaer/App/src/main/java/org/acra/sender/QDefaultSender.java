package org.acra.sender;

import android.content.Context;
import android.support.annotation.NonNull;
import java.io.File;
import org.acra.collector.CrashReportData;
import org.acra.config.ACRAConfiguration;

public class QDefaultSender implements ReportSender {
    public void send(@NonNull Context context, @NonNull CrashReportData crashReportData, @NonNull ACRAConfiguration aCRAConfiguration, @NonNull File file, @NonNull String str, int i) {
        String str2 = "http://mwhale.corp.qunar.com/api/log/androidErrorLog";
        str2 = "http://miniclient.qunar.com/pitcher-proxy";
        new QSenderProxy("http://mwhale.corp.qunar.com/api/log/androidErrorLog", "http://miniclient.qunar.com/pitcher-proxy").send(context, crashReportData, aCRAConfiguration, file, str, i);
    }
}
