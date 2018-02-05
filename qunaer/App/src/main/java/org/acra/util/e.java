package org.acra.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageInfo;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import java.io.File;
import org.acra.ACRA;
import org.acra.ReportingInteractionMode;
import org.acra.config.ACRAConfiguration;
import org.acra.ne.NativeException;
import org.acra.ne.NativeExceptionHandler;
import org.acra.prefs.a;
import org.acra.prefs.b;
import org.acra.sender.c;

public final class e {
    private final Context a;
    private final ACRAConfiguration b;

    public e(@NonNull Context context, @NonNull ACRAConfiguration aCRAConfiguration) {
        this.a = context;
        this.b = aCRAConfiguration;
    }

    public void a() {
        SharedPreferences a = new b(this.a, this.b).a();
        long j = (long) a.getInt("acra.lastVersionNr", 0);
        int d = d();
        if (((long) d) > j) {
            if (10 > a.getInt("acra.acraLastVersion", 0)) {
                a.a(a.edit().putInt("acra.acraLastVersion", 10));
            }
            org.acra.file.a aVar = new org.acra.file.a(this.a);
            aVar.a(true, 0);
            aVar.a(false, 0);
            aVar.a();
            aVar.b();
            Editor edit = a.edit();
            edit.putInt("acra.lastVersionNr", d);
            a.a(edit);
        }
    }

    public void b() {
        new org.acra.file.a(this.a).a(false, 1);
    }

    public void c() {
        org.acra.file.e eVar = new org.acra.file.e(this.a);
        File[] f = eVar.f();
        if (f.length == 0) {
            b(eVar);
            a(eVar);
            a(this.b, eVar);
            c(eVar);
            return;
        }
        if (this.b.mode() == ReportingInteractionMode.TOAST && a(f)) {
            k.a(this.a, this.b.resToastText(), 1);
        }
        new c(this.a, this.b).a(false, false);
    }

    private int d() {
        PackageInfo packageInfo = new PackageManagerWrapper(this.a).getPackageInfo();
        return packageInfo == null ? 0 : packageInfo.versionCode;
    }

    private boolean a(File[] fileArr) {
        org.acra.file.b bVar = new org.acra.file.b();
        for (File name : fileArr) {
            if (!bVar.a(name.getName())) {
                return true;
            }
        }
        return false;
    }

    private void a(ACRAConfiguration aCRAConfiguration, org.acra.file.e eVar) {
        final File[] h = eVar.h();
        if (h.length <= 0) {
            return;
        }
        if (aCRAConfiguration.sendFailedDump()) {
            ACRA.f.e(ACRA.e, "there is " + h.length + " no_send_dump");
            new Thread(this) {
                final /* synthetic */ e b;

                public void run() {
                    SystemClock.sleep(2000);
                    int i = 0;
                    while (i < h.length && i < 2) {
                        if (h[i] != null) {
                            NativeException nativeException = new NativeException(h[i].getPath());
                            nativeException.noSendDmp = true;
                            NativeExceptionHandler.a(nativeException);
                        }
                        i++;
                    }
                }
            }.start();
            return;
        }
        ACRA.f.e(ACRA.e, "there is " + h.length + " no_send_dump");
        for (File file : h) {
            if (file != null && file.exists() && file.delete()) {
                ACRA.f.e(ACRA.e, "delete no send dump");
            }
        }
    }

    private void a(org.acra.file.e eVar) {
        for (File delete : eVar.i()) {
            if (delete.delete()) {
                ACRA.f.e("ACRA", "delete residual dmp.gz file");
            }
        }
    }

    private void b(org.acra.file.e eVar) {
        for (File delete : eVar.g()) {
            if (delete.delete()) {
                ACRA.f.e("ACRA", "delete residual acrajava.gz file");
            }
        }
    }

    private void c(org.acra.file.e eVar) {
        for (File delete : eVar.j()) {
            if (delete.delete()) {
                ACRA.f.e("ACRA", "delete residual screen shot file");
            }
        }
    }
}
