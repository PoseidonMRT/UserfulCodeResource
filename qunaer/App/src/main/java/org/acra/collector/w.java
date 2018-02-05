package org.acra.collector;

import android.content.Context;
import android.support.annotation.NonNull;
import com.mqunar.contacts.basis.model.Contact;
import org.acra.config.ACRAConfiguration;
import org.acra.prefs.b;

public class w {
    @NonNull
    public static String[] a(@NonNull Context context, @NonNull ACRAConfiguration aCRAConfiguration, @NonNull String str) {
        return new b(context, aCRAConfiguration).a().getString(str, String.valueOf(System.currentTimeMillis() + "#0")).split(Contact.NUMBER);
    }
}
