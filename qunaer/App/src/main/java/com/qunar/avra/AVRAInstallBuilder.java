package com.qunar.avra;

import android.content.Context;
import android.support.annotation.NonNull;
import com.qunar.avra.handler.BeforeHandleViolationCallback;
import com.qunar.avra.sender.ReportSender;

public class AVRAInstallBuilder {
    AVRAInstallBuilder(boolean z) {
    }

    public void buildAndInstall(@NonNull Context context) {
    }

    @NonNull
    public AVRAInstallBuilder detectLeakedActivity() {
        return this;
    }

    @NonNull
    public AVRAInstallBuilder detectBlock(int i, int i2) {
        return this;
    }

    @NonNull
    public AVRAInstallBuilder detectCleartextNetwork() {
        return this;
    }

    @NonNull
    public AVRAInstallBuilder detectFileUriExposure() {
        return this;
    }

    @NonNull
    public AVRAInstallBuilder detectLeakedClosableObjects() {
        return this;
    }

    @NonNull
    public AVRAInstallBuilder detectLeakedSqlLiteObjects() {
        return this;
    }

    @NonNull
    public AVRAInstallBuilder detectLeakedRegistrationObjects() {
        return this;
    }

    @NonNull
    public AVRAInstallBuilder detectResourceMismatch() {
        return this;
    }

    @NonNull
    public <T extends ReportSender> AVRAInstallBuilder setSenderClass(@NonNull Class<T> cls) {
        return this;
    }

    @NonNull
    public <T extends BeforeHandleViolationCallback> AVRAInstallBuilder registerBeforeHandleViolationCallbackClass(@NonNull Class<T> cls) {
        return this;
    }
}
