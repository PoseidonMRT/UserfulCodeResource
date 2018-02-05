package com.mqunar.libtask;

import android.annotation.TargetApi;
import android.os.Build;

final class m {
    m() {
    }

    @TargetApi(9)
    public String a() {
        return Build.SERIAL;
    }
}
