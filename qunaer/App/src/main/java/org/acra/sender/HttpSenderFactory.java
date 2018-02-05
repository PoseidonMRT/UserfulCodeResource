package org.acra.sender;

import android.content.Context;
import android.support.annotation.NonNull;
import org.acra.config.ACRAConfiguration;

public final class HttpSenderFactory implements ReportSenderFactory {
    @NonNull
    public ReportSender create(@NonNull Context context, @NonNull ACRAConfiguration aCRAConfiguration) {
        return new HttpSender(aCRAConfiguration, aCRAConfiguration.httpMethod(), aCRAConfiguration.reportType(), null);
    }
}
