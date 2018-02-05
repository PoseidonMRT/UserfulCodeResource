package org.acra.dialog;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import java.io.File;
import java.io.Serializable;
import org.acra.ACRA;
import org.acra.ReportField;
import org.acra.collector.CrashReportData;
import org.acra.config.ACRAConfiguration;
import org.acra.file.c;
import org.acra.util.k;

public abstract class a extends Activity {
    private File a;
    private ACRAConfiguration b;
    private Throwable c;

    protected final void onCreate(@Nullable Bundle bundle) {
        super.onCreate(bundle);
        ACRA.f.b(ACRA.e, "CrashReportDialog extras=" + getIntent().getExtras());
        Serializable serializableExtra = getIntent().getSerializableExtra("REPORT_CONFIG");
        Serializable serializableExtra2 = getIntent().getSerializableExtra("REPORT_FILE");
        Serializable serializableExtra3 = getIntent().getSerializableExtra("REPORT_EXCEPTION");
        if (getIntent().getBooleanExtra("FORCE_CANCEL", false)) {
            ACRA.f.b(ACRA.e, "Forced reports deletion.");
            a();
            finish();
        } else if ((serializableExtra instanceof ACRAConfiguration) && (serializableExtra2 instanceof File) && ((serializableExtra3 instanceof Throwable) || serializableExtra3 == null)) {
            this.b = (ACRAConfiguration) serializableExtra;
            this.a = (File) serializableExtra2;
            this.c = (Throwable) serializableExtra3;
            a(bundle);
        } else {
            ACRA.f.d(ACRA.e, "Illegal or incomplete call of BaseCrashReportDialog.");
            finish();
        }
    }

    protected void a(@Nullable Bundle bundle) {
    }

    protected final void a() {
        new org.acra.file.a(getApplicationContext()).a(false, 0);
    }

    protected final void a(@Nullable String str, @Nullable String str2) {
        c cVar = new c();
        try {
            ACRA.f.b(ACRA.e, "Add user comment to " + this.a);
            CrashReportData a = cVar.a(this.a);
            Enum enumR = ReportField.USER_COMMENT;
            if (str == null) {
                str = "";
            }
            a.put(enumR, str);
            enumR = ReportField.USER_EMAIL;
            if (str2 == null) {
                str2 = "";
            }
            a.put(enumR, str2);
            cVar.a(a, this.a);
        } catch (Throwable e) {
            ACRA.f.b(ACRA.e, "User comment not added: ", e);
        }
        new org.acra.sender.c(getApplicationContext(), this.b).a(false, true);
        int resDialogOkToast = this.b.resDialogOkToast();
        if (resDialogOkToast != 0) {
            k.a(getApplicationContext(), resDialogOkToast, 1);
        }
    }

    protected final ACRAConfiguration b() {
        return this.b;
    }
}
