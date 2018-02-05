package org.acra.sender;

import android.content.Context;
import android.support.annotation.NonNull;
import java.io.File;
import org.acra.collector.CrashReportData;
import org.acra.config.ACRAConfiguration;

public interface ReportSender {
    void send(@NonNull Context context, @NonNull CrashReportData crashReportData, @NonNull ACRAConfiguration aCRAConfiguration, @NonNull File file, @NonNull String str, int i);
}
