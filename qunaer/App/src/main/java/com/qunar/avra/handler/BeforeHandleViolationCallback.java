package com.qunar.avra.handler;

import android.support.annotation.NonNull;
import com.qunar.avra.violation.Violation;

public interface BeforeHandleViolationCallback {
    void handle(@NonNull Violation violation);
}
