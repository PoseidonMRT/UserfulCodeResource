package org.acra;

import android.content.Context;

public interface BeforeWriteCrashAdder {
    void add(Context context, Throwable th);
}
