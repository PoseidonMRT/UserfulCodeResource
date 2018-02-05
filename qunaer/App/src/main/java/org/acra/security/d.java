package org.acra.security;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import java.security.KeyStore;
import org.acra.ACRA;
import org.acra.config.ACRAConfiguration;

public final class d {
    @Nullable
    public static KeyStore a(@NonNull Context context, @NonNull ACRAConfiguration aCRAConfiguration) {
        KeyStore b;
        try {
            b = ((c) aCRAConfiguration.keyStoreFactoryClass().newInstance()).b(context);
        } catch (Throwable e) {
            ACRA.f.c(ACRA.e, "Could not get keystore from factory", e);
            b = null;
        } catch (Throwable e2) {
            ACRA.f.c(ACRA.e, "Could not get keystore from factory", e2);
            b = null;
        }
        if (b != null) {
            return b;
        }
        int resCertificate = aCRAConfiguration.resCertificate();
        String certificatePath = aCRAConfiguration.certificatePath();
        String certificateType = aCRAConfiguration.certificateType();
        if (resCertificate != 0) {
            return new f(certificateType, resCertificate).b(context);
        }
        if (certificatePath.equals("")) {
            return b;
        }
        if (certificatePath.startsWith("asset://")) {
            return new a(certificateType, certificatePath.substring("asset://".length())).b(context);
        }
        return new b(certificateType, certificatePath).b(context);
    }
}
