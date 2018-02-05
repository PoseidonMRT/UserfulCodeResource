package com.mqunar.spider;

import android.content.Context;
import org.acra.builder.EndingPrimer;

public class QCrashEndingPrimer implements EndingPrimer {
    public void primeEnding(Context context, Throwable th) {
        ((QunarApp) context.getApplicationContext()).clearStack();
    }
}
