package org.acra.file;

import android.content.Context;
import android.support.annotation.NonNull;
import java.io.File;
import java.util.Arrays;
import org.acra.ACRA;

public final class a {
    @NonNull
    private final e a;

    public a(@NonNull Context context) {
        this.a = new e(context);
    }

    public void a(boolean z, int i) {
        Object[] f = z ? this.a.f() : this.a.b();
        Arrays.sort(f, new d());
        for (int i2 = 0; i2 < f.length - i; i2++) {
            if (!f[i2].delete()) {
                ACRA.f.d(ACRA.e, "Could not delete report : " + f[i2]);
            }
        }
    }

    public void a() {
        for (File file : this.a.h()) {
            if (!file.delete()) {
                ACRA.f.d(ACRA.e, "Could not delete dump : " + file);
            }
        }
    }

    public void b() {
        for (File file : this.a.j()) {
            if (!file.delete()) {
                ACRA.f.d(ACRA.e, "Could not delete screen shot : " + file);
            }
        }
    }
}
