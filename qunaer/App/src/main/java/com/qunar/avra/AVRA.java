package com.qunar.avra;

import android.content.Context;
import android.support.annotation.NonNull;

public class AVRA {
    public static void watchLeak(Object obj) {
    }

    public static void install(@NonNull Context context) {
    }

    public static boolean isInAnalyzerProcess(@NonNull Context context) {
        return false;
    }

    @NonNull
    public static AVRAInstallBuilder watchAllProcess() {
        return new AVRAInstallBuilder(true);
    }

    @NonNull
    public static AVRAInstallBuilder watchMainProcess() {
        return new AVRAInstallBuilder(false);
    }

    public static boolean isLeakInitialised() {
        return false;
    }

    public static boolean isBlockInitialised() {
        return false;
    }

    public static boolean isStrictModeInitialised() {
        return false;
    }
}
