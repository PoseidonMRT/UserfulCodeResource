package com.mqunar.spider;

import android.content.Context;
import android.support.annotation.NonNull;
import com.mqunar.atomenv.GlobalEnv;
import org.acra.config.ACRAConfiguration;
import org.acra.sender.NullSender;
import org.acra.sender.ReportSender;
import org.acra.sender.ReportSenderFactory;
import org.acra.util.PackageManagerWrapper;

public class QReportSenderFactory implements ReportSenderFactory {
    @NonNull
    public ReportSender create(@NonNull Context context, @NonNull ACRAConfiguration aCRAConfiguration) {
        if (!new PackageManagerWrapper(context).hasPermission("android.permission.INTERNET")) {
            return new NullSender();
        }
        if (GlobalEnv.getInstance().isRelease()) {
            return new QAcraReleaseSender();
        }
        return new QAcraBetaSender();
    }
}
