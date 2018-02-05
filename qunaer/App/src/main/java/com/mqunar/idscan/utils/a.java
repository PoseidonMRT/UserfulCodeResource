package com.mqunar.idscan.utils;

import android.util.TypedValue;

public final class a {
    public static int a(float f) {
        return (int) (c(f) + 0.5f);
    }

    public static float b(float f) {
        return c(f);
    }

    private static float c(float f) {
        return TypedValue.applyDimension(1, f, com.mqunar.idscan.a.a().getResources().getDisplayMetrics());
    }
}
