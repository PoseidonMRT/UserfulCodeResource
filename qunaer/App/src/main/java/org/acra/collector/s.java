package org.acra.collector;

import android.content.Context;
import android.os.Build.VERSION;
import android.provider.Settings.Global;
import android.provider.Settings.Secure;
import android.provider.Settings.System;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import java.lang.reflect.Field;
import java.util.Iterator;
import org.acra.ACRA;
import org.acra.config.ACRAConfiguration;

final class s {
    private final Context a;
    private final ACRAConfiguration b;

    s(@NonNull Context context, @NonNull ACRAConfiguration aCRAConfiguration) {
        this.a = context;
        this.b = aCRAConfiguration;
    }

    @NonNull
    public String a() {
        StringBuilder stringBuilder = new StringBuilder();
        for (Field field : System.class.getFields()) {
            if (!field.isAnnotationPresent(Deprecated.class) && field.getType() == String.class) {
                try {
                    String string = System.getString(this.a.getContentResolver(), (String) field.get(null));
                    if (string != null) {
                        stringBuilder.append(field.getName()).append('=').append(string).append('\n');
                    }
                } catch (Throwable e) {
                    ACRA.f.b(ACRA.e, "Error: ", e);
                } catch (Throwable e2) {
                    ACRA.f.b(ACRA.e, "Error: ", e2);
                }
            }
        }
        return stringBuilder.toString();
    }

    @NonNull
    public String b() {
        StringBuilder stringBuilder = new StringBuilder();
        for (Field field : Secure.class.getFields()) {
            if (!field.isAnnotationPresent(Deprecated.class) && field.getType() == String.class && a(field)) {
                try {
                    String string = Secure.getString(this.a.getContentResolver(), (String) field.get(null));
                    if (string != null) {
                        stringBuilder.append(field.getName()).append('=').append(string).append('\n');
                    }
                } catch (Throwable e) {
                    ACRA.f.b(ACRA.e, "Error: ", e);
                } catch (Throwable e2) {
                    ACRA.f.b(ACRA.e, "Error: ", e2);
                }
            }
        }
        return stringBuilder.toString();
    }

    @NonNull
    public String c() {
        if (VERSION.SDK_INT < 17) {
            return "";
        }
        StringBuilder stringBuilder = new StringBuilder();
        for (Field field : Global.class.getFields()) {
            if (!field.isAnnotationPresent(Deprecated.class) && field.getType() == String.class && a(field)) {
                try {
                    String string = Global.getString(this.a.getContentResolver(), (String) field.get(null));
                    if (string != null) {
                        stringBuilder.append(field.getName()).append('=').append(string).append('\n');
                    }
                } catch (Throwable e) {
                    ACRA.f.b(ACRA.e, "Error: ", e);
                } catch (Throwable e2) {
                    ACRA.f.b(ACRA.e, "Error: ", e2);
                } catch (Throwable e22) {
                    ACRA.f.b(ACRA.e, "Error: ", e22);
                }
            }
        }
        return stringBuilder.toString();
    }

    private boolean a(@Nullable Field field) {
        if (field == null || field.getName().startsWith("WIFI_AP")) {
            return false;
        }
        Iterator it = this.b.excludeMatchingSettingsKeys().iterator();
        while (it.hasNext()) {
            if (field.getName().matches((String) it.next())) {
                return false;
            }
        }
        return true;
    }
}
