package com.mqunar.splash;

public class SplashUtils {
    private static String crashTouchUrl;
    private static volatile boolean loadDone;
    private static SplashMonitor splashMonitor;

    private SplashUtils() {
    }

    public static void setLoadDone(boolean z) {
        loadDone = z;
    }

    public static boolean isLoadDone() {
        return loadDone;
    }

    public static void setCrashTouchUrl(String str) {
        crashTouchUrl = str;
    }

    public static String getCrashTouchUrl() {
        return crashTouchUrl;
    }

    public static void setSplashMonitor(SplashMonitor splashMonitor) {
        splashMonitor = splashMonitor;
    }

    public static SplashMonitor getSplashMonitor() {
        return splashMonitor;
    }
}
