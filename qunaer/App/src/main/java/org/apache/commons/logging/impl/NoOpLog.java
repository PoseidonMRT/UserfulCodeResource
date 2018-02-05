package org.apache.commons.logging.impl;

import java.io.Serializable;
import org.apache.commons.logging.Log;

@Deprecated
public class NoOpLog implements Serializable, Log {
    public NoOpLog() {
        throw new RuntimeException("Stub!");
    }

    public NoOpLog(String str) {
        throw new RuntimeException("Stub!");
    }

    public void trace(Object obj) {
        throw new RuntimeException("Stub!");
    }

    public void trace(Object obj, Throwable th) {
        throw new RuntimeException("Stub!");
    }

    public void debug(Object obj) {
        throw new RuntimeException("Stub!");
    }

    public void debug(Object obj, Throwable th) {
        throw new RuntimeException("Stub!");
    }

    public void info(Object obj) {
        throw new RuntimeException("Stub!");
    }

    public void info(Object obj, Throwable th) {
        throw new RuntimeException("Stub!");
    }

    public void warn(Object obj) {
        throw new RuntimeException("Stub!");
    }

    public void warn(Object obj, Throwable th) {
        throw new RuntimeException("Stub!");
    }

    public void error(Object obj) {
        throw new RuntimeException("Stub!");
    }

    public void error(Object obj, Throwable th) {
        throw new RuntimeException("Stub!");
    }

    public void fatal(Object obj) {
        throw new RuntimeException("Stub!");
    }

    public void fatal(Object obj, Throwable th) {
        throw new RuntimeException("Stub!");
    }

    public final boolean isDebugEnabled() {
        throw new RuntimeException("Stub!");
    }

    public final boolean isErrorEnabled() {
        throw new RuntimeException("Stub!");
    }

    public final boolean isFatalEnabled() {
        throw new RuntimeException("Stub!");
    }

    public final boolean isInfoEnabled() {
        throw new RuntimeException("Stub!");
    }

    public final boolean isTraceEnabled() {
        throw new RuntimeException("Stub!");
    }

    public final boolean isWarnEnabled() {
        throw new RuntimeException("Stub!");
    }
}
