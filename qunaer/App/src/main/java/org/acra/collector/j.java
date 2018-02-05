package org.acra.collector;

import android.content.Context;
import android.support.annotation.NonNull;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import org.acra.ACRA;
import org.acra.util.g;

class j {
    j() {
    }

    @NonNull
    public String a(@NonNull Context context, @NonNull String str, int i) {
        return g.a(a(context, str), i);
    }

    @NonNull
    private static InputStream a(@NonNull Context context, @NonNull String str) {
        try {
            if (str.startsWith("/")) {
                return new FileInputStream(str);
            }
            if (str.contains("/")) {
                return new FileInputStream(new File(context.getFilesDir(), str));
            }
            return context.openFileInput(str);
        } catch (FileNotFoundException e) {
            ACRA.f.e(ACRA.e, "Cannot find application log file : '" + str + '\'');
            return new ByteArrayInputStream(new byte[0]);
        }
    }
}
