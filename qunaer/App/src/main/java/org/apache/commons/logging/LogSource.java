package org.apache.commons.logging;

import java.lang.reflect.Constructor;
import java.util.Hashtable;

@Deprecated
public class LogSource {
    protected static boolean jdk14IsAvailable;
    protected static boolean log4jIsAvailable;
    protected static Constructor logImplctor;
    protected static Hashtable logs;

    LogSource() {
        throw new RuntimeException("Stub!");
    }

    public static void setLogImplementation(String str) {
        throw new RuntimeException("Stub!");
    }

    public static void setLogImplementation(Class cls) {
        throw new RuntimeException("Stub!");
    }

    public static Log getInstance(String str) {
        throw new RuntimeException("Stub!");
    }

    public static Log getInstance(Class cls) {
        throw new RuntimeException("Stub!");
    }

    public static Log makeNewLogInstance(String str) {
        throw new RuntimeException("Stub!");
    }

    public static String[] getLogNames() {
        throw new RuntimeException("Stub!");
    }
}
