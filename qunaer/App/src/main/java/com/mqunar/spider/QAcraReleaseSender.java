package com.mqunar.spider;

import android.content.Context;
import android.support.annotation.NonNull;
import java.io.File;
import org.acra.collector.CrashReportData;
import org.acra.config.ACRAConfiguration;
import org.acra.sender.QSenderProxy;
import org.acra.sender.ReportSender;

public class QAcraReleaseSender implements ReportSender {
    public void send(@NonNull Context context, @NonNull CrashReportData crashReportData, @NonNull ACRAConfiguration aCRAConfiguration, @NonNull File file, @NonNull String str, int i) {
        new QSenderProxy("http://mloganalysts.corp.qunar.com/api/log/clientError", "http://client.qunar.com/pitcher-proxy").send(context, crashReportData, aCRAConfiguration, file, str, i);
    }
}
