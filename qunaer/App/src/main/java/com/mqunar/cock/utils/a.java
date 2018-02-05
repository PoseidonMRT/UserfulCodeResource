package com.mqunar.cock.utils;

import android.annotation.TargetApi;
import android.os.Build;

final class a {
    a() {
    }

    @TargetApi(9)
    public String a() {
        return Build.SERIAL;
    }
}
