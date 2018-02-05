package org.acra.log;

import android.util.Log;
import org.acra.ACRA;

public final class b implements a {
    public int a(String str, String str2) {
        if (ACRA.b) {
            return Log.v(str, str2);
        }
        return 0;
    }

    public int b(String str, String str2) {
        if (ACRA.b) {
            return Log.d(str, str2);
        }
        return 0;
    }

    public int a(String str, String str2, Throwable th) {
        if (ACRA.b) {
            return Log.d(str, str2, th);
        }
        return 0;
    }

    public int c(String str, String str2) {
        if (ACRA.b) {
            return Log.i(str, str2);
        }
        return 0;
    }

    public int d(String str, String str2) {
        if (ACRA.b) {
            return Log.w(str, str2);
        }
        return 0;
    }

    public int b(String str, String str2, Throwable th) {
        if (ACRA.b) {
            return Log.w(str, str2, th);
        }
        return 0;
    }

    public int e(String str, String str2) {
        if (ACRA.b) {
            return Log.e(str, str2);
        }
        return 0;
    }

    public int c(String str, String str2, Throwable th) {
        if (ACRA.b) {
            return Log.e(str, str2, th);
        }
        return 0;
    }
}
