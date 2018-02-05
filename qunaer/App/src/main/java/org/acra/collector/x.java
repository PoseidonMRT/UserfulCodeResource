package org.acra.collector;

import android.support.annotation.NonNull;

final class x {
    @NonNull
    static String a() {
        String property = System.getProperty("java.vm.version");
        if (property.charAt(0) - 48 < 2) {
            return property + "  (Dalvik)";
        }
        return property + "  (ART)";
    }
}
