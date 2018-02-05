package org.acra.security;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.RawRes;
import java.io.InputStream;

final class f extends BaseKeyStoreFactory {
    @RawRes
    private final int a;

    f(String str, @RawRes int i) {
        super(str);
        this.a = i;
    }

    public InputStream a(@NonNull Context context) {
        return context.getResources().openRawResource(this.a);
    }
}
