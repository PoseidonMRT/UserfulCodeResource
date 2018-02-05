package org.acra.security;

import android.content.Context;
import android.support.annotation.NonNull;
import java.io.InputStream;
import org.acra.ACRA;

final class a extends BaseKeyStoreFactory {
    private final String a;

    a(String str, String str2) {
        super(str);
        this.a = str2;
    }

    public InputStream a(@NonNull Context context) {
        try {
            return context.getAssets().open(this.a);
        } catch (Throwable e) {
            ACRA.f.c(ACRA.e, "Could not open certificate in asset://" + this.a, e);
            return null;
        }
    }
}
