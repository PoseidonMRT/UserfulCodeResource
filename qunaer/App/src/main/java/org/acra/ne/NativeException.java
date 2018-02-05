package org.acra.ne;

import android.support.annotation.NonNull;

public class NativeException extends RuntimeException {
    private String crashFileDirectory = "";
    public boolean noSendDmp = false;

    @NonNull
    public String getCrashFileDirectory() {
        return this.crashFileDirectory;
    }

    void setCrashFileDirectory(@NonNull String str) {
        this.crashFileDirectory = str;
    }

    public NativeException(@NonNull String str) {
        this.crashFileDirectory = str;
    }
}
