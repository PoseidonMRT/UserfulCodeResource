package org.acra.collector;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;
import org.acra.ACRA;
import org.acra.config.ACRAConfiguration;
import org.apache.commons.io.FilenameUtils;

final class t {
    private final Context a;
    private final ACRAConfiguration b;

    t(@NonNull Context context, @NonNull ACRAConfiguration aCRAConfiguration) {
        this.a = context;
        this.b = aCRAConfiguration;
    }

    @NonNull
    public String a() {
        StringBuilder stringBuilder = new StringBuilder();
        Map treeMap = new TreeMap();
        SharedPreferences defaultSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this.a);
        Editor editor = null;
        if (defaultSharedPreferences != null) {
            if (defaultSharedPreferences.contains("acra.acraLastVersion")) {
                if (null == null) {
                    editor = defaultSharedPreferences.edit();
                }
                editor.remove("acra.acraLastVersion");
            }
            if (defaultSharedPreferences.contains("acra.lastVersionNr")) {
                if (editor == null) {
                    editor = defaultSharedPreferences.edit();
                }
                editor.remove("acra.lastVersionNr");
            }
            if (editor != null) {
                editor.commit();
            }
        }
        treeMap.put("default", defaultSharedPreferences);
        treeMap.put("data_acra", this.a.getSharedPreferences("data_acra", 0));
        Iterator it = this.b.additionalSharedPreferences().iterator();
        while (it.hasNext()) {
            String str = (String) it.next();
            treeMap.put(str, this.a.getSharedPreferences(str, 0));
        }
        for (Entry entry : treeMap.entrySet()) {
            String str2 = (String) entry.getKey();
            Map all = ((SharedPreferences) entry.getValue()).getAll();
            if (all.isEmpty()) {
                stringBuilder.append(str2).append('=').append("empty\n");
            } else {
                for (Entry entry2 : all.entrySet()) {
                    if (a((String) entry2.getKey())) {
                        ACRA.f.b(ACRA.e, "Filtered out sharedPreference=" + str2 + "  key=" + ((String) entry2.getKey()) + " due to filtering rule");
                    } else {
                        Object value = entry2.getValue();
                        stringBuilder.append(str2).append(FilenameUtils.EXTENSION_SEPARATOR).append((String) entry2.getKey()).append('=');
                        stringBuilder.append(value == null ? "null" : value.toString());
                        stringBuilder.append('\n');
                    }
                }
                stringBuilder.append('\n');
            }
        }
        return stringBuilder.toString();
    }

    private boolean a(@NonNull String str) {
        Iterator it = this.b.excludeMatchingSharedPreferencesKeys().iterator();
        while (it.hasNext()) {
            if (str.matches((String) it.next())) {
                return true;
            }
        }
        return false;
    }
}
