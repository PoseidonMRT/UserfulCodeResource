package org.acra.collector;

import java.io.File;
import org.acra.ACRA;

class q {
    static String a() {
        return String.valueOf(b());
    }

    private static boolean b() {
        try {
            return new File("/system/bin/su").exists() || new File("/system/xbin/su").exists();
        } catch (Exception e) {
            ACRA.f.e(ACRA.e, "get root info error : " + e);
            return false;
        }
    }
}
