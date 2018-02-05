package org.acra.security;

import android.content.Context;
import android.support.annotation.NonNull;
import java.io.FileInputStream;
import java.io.InputStream;
import org.acra.ACRA;

final class b extends BaseKeyStoreFactory {
    private final String a;

    b(String str, String str2) {
        super(str);
        this.a = str2;
    }

    public InputStream a(@NonNull Context context) {
        try {
            return new FileInputStream(this.a);
        } catch (Throwable e) {
            ACRA.f.c(ACRA.e, "Could not find File " + this.a, e);
            return null;
        }
    }
}
