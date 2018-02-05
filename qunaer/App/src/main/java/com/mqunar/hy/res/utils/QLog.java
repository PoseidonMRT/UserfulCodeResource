package com.mqunar.hy.res.utils;

import android.content.Context;
import java.util.Calendar;

@Deprecated
public final class QLog {
    private static final Calendar _20120101 = Calendar.getInstance();

    public static int v(String str, String str2, Object... objArr) {
        return 0;
    }

    public static int d(String str, String str2, Object... objArr) {
        return 0;
    }

    public static int i(String str, String str2, Object... objArr) {
        return 0;
    }

    public static int i(Context context, String str) {
        return 0;
    }

    public static int w(String str, String str2, Object... objArr) {
        return 0;
    }

    public static int w(String str, Throwable th) {
        return 0;
    }

    public static int e(String str, String str2, Throwable th) {
        return 0;
    }

    public static int e(String str, String str2) {
        return 0;
    }

    public static int println(int i, String str, String str2) {
        return 0;
    }

    public static long debugDuration(long j) {
        long currentTimeMillis = System.currentTimeMillis();
        StackTraceElement stackTraceElement = Thread.currentThread().getStackTrace()[3];
        d("Performance", stackTraceElement.getFileName() + "_" + stackTraceElement.getLineNumber() + ":" + (currentTimeMillis - j), new Object[0]);
        return currentTimeMillis;
    }

    public static int printLogPos(String str) {
        StackTraceElement stackTraceElement = Thread.currentThread().getStackTrace()[3];
        return d(str, stackTraceElement.getClassName() + ":" + stackTraceElement.getLineNumber() + "::" + stackTraceElement.getMethodName(), new Object[0]);
    }

    public static String getLogPos(int i) {
        return Thread.currentThread().getStackTrace()[i].getClassName();
    }

    public static String getLogPos() {
        return getLogPos(4);
    }

    public static long getSecond(Calendar calendar) {
        return (System.currentTimeMillis() - calendar.getTimeInMillis()) / 1000;
    }

    static {
        _20120101.set(2012, 0, 0, 0, 0, 0);
    }

    private static void log(int i, Throwable th, String str, Object... objArr) {
    }

    public static void v(String str, Object... objArr) {
    }

    public static void d(String str, Object... objArr) {
    }

    public static void i(String str, Object... objArr) {
    }

    public static void w(String str, Object... objArr) {
    }

    public static void e(Throwable th) {
    }

    public static void e(String str, Object... objArr) {
    }

    public static void e(Throwable th, String str, Object... objArr) {
    }
}
