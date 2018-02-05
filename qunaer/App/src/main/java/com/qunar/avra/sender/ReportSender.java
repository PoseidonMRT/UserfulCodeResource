package com.qunar.avra.sender;

import android.support.annotation.NonNull;
import com.qunar.avra.violation.Violation;

public interface ReportSender {
    void send(@NonNull Violation violation);
}
