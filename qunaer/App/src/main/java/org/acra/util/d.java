package org.acra.util;

import android.support.annotation.NonNull;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class d {
    @NonNull
    public static String a(long j, @NonNull String str) {
        return new SimpleDateFormat(str, Locale.CHINA).format(Long.valueOf(j));
    }
}
