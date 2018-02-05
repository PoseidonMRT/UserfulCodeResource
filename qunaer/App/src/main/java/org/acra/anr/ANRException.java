package org.acra.anr;

import android.support.annotation.NonNull;

public class ANRException extends RuntimeException {
    public String ANRInfo = "";
    public String mainThreadInfo;
    public int monitorMode;
    public String tracesFilePath = "";

    ANRException(@NonNull String str) {
        super("ANRException");
        this.ANRInfo = str;
    }
}
