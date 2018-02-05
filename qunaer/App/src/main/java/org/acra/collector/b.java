package org.acra.collector;

import android.content.Context;
import android.content.res.Configuration;
import android.support.annotation.NonNull;
import android.util.SparseArray;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;
import org.acra.ACRA;

public final class b {
    private final Map<String, SparseArray<String>> a = new HashMap();

    private b() {
        SparseArray sparseArray = new SparseArray();
        SparseArray sparseArray2 = new SparseArray();
        SparseArray sparseArray3 = new SparseArray();
        SparseArray sparseArray4 = new SparseArray();
        SparseArray sparseArray5 = new SparseArray();
        SparseArray sparseArray6 = new SparseArray();
        SparseArray sparseArray7 = new SparseArray();
        SparseArray sparseArray8 = new SparseArray();
        SparseArray sparseArray9 = new SparseArray();
        for (Field field : Configuration.class.getFields()) {
            if (Modifier.isStatic(field.getModifiers()) && Modifier.isFinal(field.getModifiers())) {
                String name = field.getName();
                try {
                    if (name.startsWith("HARDKEYBOARDHIDDEN_")) {
                        sparseArray.put(field.getInt(null), name);
                    } else if (name.startsWith("KEYBOARD_")) {
                        sparseArray2.put(field.getInt(null), name);
                    } else if (name.startsWith("KEYBOARDHIDDEN_")) {
                        sparseArray3.put(field.getInt(null), name);
                    } else if (name.startsWith("NAVIGATION_")) {
                        sparseArray4.put(field.getInt(null), name);
                    } else if (name.startsWith("NAVIGATIONHIDDEN_")) {
                        sparseArray5.put(field.getInt(null), name);
                    } else if (name.startsWith("ORIENTATION_")) {
                        sparseArray6.put(field.getInt(null), name);
                    } else if (name.startsWith("SCREENLAYOUT_")) {
                        sparseArray7.put(field.getInt(null), name);
                    } else if (name.startsWith("TOUCHSCREEN_")) {
                        sparseArray8.put(field.getInt(null), name);
                    } else if (name.startsWith("UI_MODE_")) {
                        sparseArray9.put(field.getInt(null), name);
                    }
                } catch (Throwable e) {
                    ACRA.f.b(ACRA.e, "Error while inspecting device configuration: ", e);
                } catch (Throwable e2) {
                    ACRA.f.b(ACRA.e, "Error while inspecting device configuration: ", e2);
                }
            }
        }
        this.a.put("HARDKEYBOARDHIDDEN_", sparseArray);
        this.a.put("KEYBOARD_", sparseArray2);
        this.a.put("KEYBOARDHIDDEN_", sparseArray3);
        this.a.put("NAVIGATION_", sparseArray4);
        this.a.put("NAVIGATIONHIDDEN_", sparseArray5);
        this.a.put("ORIENTATION_", sparseArray6);
        this.a.put("SCREENLAYOUT_", sparseArray7);
        this.a.put("TOUCHSCREEN_", sparseArray8);
        this.a.put("UI_MODE_", sparseArray9);
    }

    @NonNull
    private String a(@NonNull Configuration configuration) {
        StringBuilder stringBuilder = new StringBuilder();
        for (Field field : configuration.getClass().getFields()) {
            try {
                if (!Modifier.isStatic(field.getModifiers())) {
                    stringBuilder.append(field.getName()).append('=');
                    if (field.getType().equals(Integer.TYPE)) {
                        stringBuilder.append(a(configuration, field));
                    } else if (field.get(configuration) != null) {
                        stringBuilder.append(field.get(configuration).toString());
                    }
                    stringBuilder.append('\n');
                }
            } catch (Throwable e) {
                ACRA.f.c(ACRA.e, "Error while inspecting device configuration: ", e);
            } catch (Throwable e2) {
                ACRA.f.c(ACRA.e, "Error while inspecting device configuration: ", e2);
            }
        }
        return stringBuilder.toString();
    }

    private String a(@NonNull Configuration configuration, @NonNull Field field) {
        String name = field.getName();
        if (name.equals("mcc") || name.equals("mnc")) {
            return Integer.toString(field.getInt(configuration));
        }
        if (name.equals("uiMode")) {
            return a((SparseArray) this.a.get("UI_MODE_"), field.getInt(configuration));
        }
        if (name.equals("screenLayout")) {
            return a((SparseArray) this.a.get("SCREENLAYOUT_"), field.getInt(configuration));
        }
        SparseArray sparseArray = (SparseArray) this.a.get(name.toUpperCase() + '_');
        if (sparseArray == null) {
            return Integer.toString(field.getInt(configuration));
        }
        name = (String) sparseArray.get(field.getInt(configuration));
        if (name == null) {
            return Integer.toString(field.getInt(configuration));
        }
        return name;
    }

    @NonNull
    private static String a(@NonNull SparseArray<String> sparseArray, int i) {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i2 = 0; i2 < sparseArray.size(); i2++) {
            int keyAt = sparseArray.keyAt(i2);
            if (((String) sparseArray.get(keyAt)).endsWith("_MASK")) {
                int i3 = i & keyAt;
                if (i3 > 0) {
                    if (stringBuilder.length() > 0) {
                        stringBuilder.append('+');
                    }
                    stringBuilder.append((String) sparseArray.get(i3));
                }
            }
        }
        return stringBuilder.toString();
    }

    @NonNull
    public static String a(@NonNull Context context) {
        try {
            return new b().a(context.getResources().getConfiguration());
        } catch (Throwable e) {
            ACRA.f.b(ACRA.e, "Couldn't retrieve CrashConfiguration for : " + context.getPackageName(), e);
            return "Couldn't retrieve crash config";
        }
    }
}
