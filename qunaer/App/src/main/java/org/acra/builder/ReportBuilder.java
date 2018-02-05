package org.acra.builder;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;
import org.acra.anr.ANRException;

public final class ReportBuilder {
    private final Map<String, String> customData = new HashMap();
    private boolean endApplication = false;
    private Throwable exception;
    private String message;
    private boolean sendSilently = false;
    private Thread uncaughtExceptionThread;

    @NonNull
    public ReportBuilder message(@Nullable String str) {
        this.message = str;
        return this;
    }

    @Nullable
    public String getMessage() {
        return this.message;
    }

    @NonNull
    public ReportBuilder uncaughtExceptionThread(@Nullable Thread thread) {
        this.uncaughtExceptionThread = thread;
        return this;
    }

    @Nullable
    public Thread getUncaughtExceptionThread() {
        return this.uncaughtExceptionThread;
    }

    @NonNull
    public ReportBuilder exception(@Nullable Throwable th) {
        this.exception = th;
        return this;
    }

    @Nullable
    public Throwable getException() {
        return this.exception;
    }

    @NonNull
    public ReportBuilder customData(@NonNull Map<String, String> map) {
        this.customData.putAll(map);
        return this;
    }

    @NonNull
    public ReportBuilder customData(@NonNull String str, String str2) {
        this.customData.put(str, str2);
        return this;
    }

    @NonNull
    public Map<String, String> getCustomData() {
        return this.customData;
    }

    @NonNull
    public ReportBuilder sendSilently() {
        this.sendSilently = true;
        return this;
    }

    public boolean isSendSilently() {
        return this.sendSilently;
    }

    @NonNull
    public ReportBuilder endApplication() {
        this.endApplication = !(this.exception instanceof ANRException);
        return this;
    }

    public boolean isEndApplication() {
        return this.endApplication;
    }

    public void build(@NonNull c cVar) {
        if (this.message == null && this.exception == null) {
            this.message = "Report requested by developer";
        }
        cVar.a(this);
    }
}
