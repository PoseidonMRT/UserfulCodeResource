package org.acra.prefs;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import org.acra.config.ACRAConfiguration;

public class b {
    private final Context a;
    private final ACRAConfiguration b;

    public b(@NonNull Context context, @NonNull ACRAConfiguration aCRAConfiguration) {
        this.a = context;
        this.b = aCRAConfiguration;
    }

    @NonNull
    public SharedPreferences a() {
        if (this.a == null) {
            throw new IllegalStateException("Cannot call ACRA.getACRASharedPreferences() before ACRA.init().");
        } else if ("".equals(this.b.sharedPreferencesName())) {
            return PreferenceManager.getDefaultSharedPreferences(this.a);
        } else {
            return this.a.getSharedPreferences(this.b.sharedPreferencesName(), this.b.sharedPreferencesMode());
        }
    }
}
