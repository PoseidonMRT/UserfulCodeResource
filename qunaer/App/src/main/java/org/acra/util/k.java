package org.acra.util;

import android.content.Context;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.widget.Toast;
import org.acra.ACRA;

public final class k {
    public static void a(@NonNull Context context, @StringRes int i, @IntRange(from = 0, to = 1) int i2) {
        try {
            Toast.makeText(context, i, i2).show();
        } catch (Throwable e) {
            ACRA.f.b(ACRA.e, "Could not send crash Toast", e);
        }
    }
}
