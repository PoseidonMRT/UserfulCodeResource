package com.qunar.avra.violation;

import android.support.annotation.NonNull;

public class Violation extends RuntimeException {
    Violation(@NonNull String str) {
        super(str);
    }
}
