package org.acra.sender;

import android.content.Context;
import android.support.annotation.NonNull;
import java.io.File;
import org.acra.ACRA;
import org.acra.collector.CrashReportData;
import org.acra.config.ACRAConfiguration;

public final class NullSender implements ReportSender {
    public void send(@NonNull Context context, @NonNull CrashReportData crashReportData, @NonNull ACRAConfiguration aCRAConfiguration, @NonNull File file, @NonNull String str, int i) {
        ACRA.f.d(ACRA.e, context.getPackageName() + " reports will NOT be sent - no valid ReportSender is configured. Try setting 'formUri' or 'mailTo'");
    }
}
