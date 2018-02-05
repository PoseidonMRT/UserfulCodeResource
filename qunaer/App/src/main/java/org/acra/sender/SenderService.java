package org.acra.sender;

import android.app.IntentService;
import android.content.Intent;
import android.os.Process;
import android.support.annotation.NonNull;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import org.acra.ACRA;
import org.acra.config.ACRAConfiguration;
import org.acra.file.b;
import org.acra.file.e;

public class SenderService extends IntentService {
    private int a;
    private final e b = new e(this);

    public SenderService() {
        super("ACRA SenderService");
    }

    protected void onHandleIntent(@NonNull Intent intent) {
        boolean booleanExtra = intent.getBooleanExtra("onlySendSilentReports", false);
        boolean booleanExtra2 = intent.getBooleanExtra("approveReportsFirst", false);
        List list = (List) intent.getSerializableExtra("reportSenderFactories");
        ACRAConfiguration aCRAConfiguration = (ACRAConfiguration) intent.getSerializableExtra("acraConfig");
        this.a = intent.getIntExtra("packageEnv", 1);
        ACRA.a(this.a);
        String stringExtra = intent.getStringExtra("cParam");
        ACRA.f.b(ACRA.e, "About to start sending reports from SenderService");
        try {
            list = a(aCRAConfiguration, list);
            if (booleanExtra2) {
                a();
            }
            File[] f = this.b.f();
            b bVar = new b(this, aCRAConfiguration, list);
            b bVar2 = new b();
            int i = 0;
            for (File file : f) {
                if (!booleanExtra || bVar2.a(file.getName())) {
                    if (i >= 5) {
                        break;
                    }
                    bVar.a(file, stringExtra, this.a);
                    i++;
                }
            }
        } catch (Throwable e) {
            ACRA.f.c(ACRA.e, "", e);
        }
        ACRA.f.b(ACRA.e, "Finished sending reports from SenderService");
    }

    @NonNull
    private List<ReportSender> a(@NonNull ACRAConfiguration aCRAConfiguration, @NonNull List<Class<? extends ReportSenderFactory>> list) {
        List<ReportSender> arrayList = new ArrayList();
        for (Class cls : list) {
            try {
                arrayList.add(((ReportSenderFactory) cls.newInstance()).create(getApplication(), aCRAConfiguration));
            } catch (Throwable e) {
                ACRA.f.b(ACRA.e, "Could not construct ReportSender from " + cls, e);
            } catch (Throwable e2) {
                ACRA.f.b(ACRA.e, "Could not construct ReportSender from " + cls, e2);
            }
        }
        return arrayList;
    }

    private void a() {
        ACRA.f.b(ACRA.e, "Mark all pending reports as approved.");
        for (File file : this.b.b()) {
            File file2 = new File(this.b.c(), file.getName());
            if (!file.renameTo(file2)) {
                ACRA.f.d(ACRA.e, "Could not rename approved report from " + file + " to " + file2);
            }
        }
    }

    public void onDestroy() {
        super.onDestroy();
        if (this.a == 1) {
            Process.killProcess(Process.myPid());
        }
    }
}
