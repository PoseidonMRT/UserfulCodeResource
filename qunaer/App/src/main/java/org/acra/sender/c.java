package org.acra.sender;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import java.util.ArrayList;
import org.acra.ACRA;
import org.acra.config.ACRAConfiguration;

public class c {
    private final Context a;
    private final ACRAConfiguration b;

    public c(@NonNull Context context, @NonNull ACRAConfiguration aCRAConfiguration) {
        this.a = context;
        this.b = aCRAConfiguration;
    }

    public void a(boolean z, boolean z2) {
        ACRA.f.b(ACRA.e, "About to start SenderService");
        Intent intent = new Intent(this.a, SenderService.class);
        intent.putExtra("onlySendSilentReports", z);
        intent.putExtra("approveReportsFirst", z2);
        intent.putExtra("reportSenderFactories", new ArrayList(this.b.reportSenderFactoryClasses()));
        intent.putExtra("acraConfig", this.b);
        intent.putExtra("packageEnv", ACRA.a);
        intent.putExtra("cParam", ACRA.g);
        this.a.startService(intent);
    }
}
