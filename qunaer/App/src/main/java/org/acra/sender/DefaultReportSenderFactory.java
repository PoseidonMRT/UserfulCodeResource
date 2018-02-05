package org.acra.sender;

import android.content.Context;
import android.support.annotation.NonNull;
import org.acra.ACRA;
import org.acra.config.ACRAConfiguration;
import org.acra.util.PackageManagerWrapper;

public final class DefaultReportSenderFactory implements ReportSenderFactory {
    @NonNull
    public ReportSender create(@NonNull Context context, @NonNull ACRAConfiguration aCRAConfiguration) {
        if (new PackageManagerWrapper(context).hasPermission("android.permission.INTERNET")) {
            ACRA.f.b(ACRA.e, context.getPackageName() + " reports will be sent by Http.");
            return new QDefaultSender();
        }
        ACRA.f.e(ACRA.e, context.getPackageName() + " should be granted permission " + "android.permission.INTERNET" + " if you want your crash reports to be sent. If you don't want to add this permission to your application you can also enable sending reports by email. If this is your will then provide your email address in @AcraConfig(mailTo=\"your.account@domain.com\"");
        return new NullSender();
    }
}
