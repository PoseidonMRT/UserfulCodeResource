package com.mqunar.contacts.basis.db;

public class ReadContactsError {
    public static final int ERROR_EMPTY_DATA = 2;
    public static final int ERROR_LOGIN_FAILURE = 1;
    public static final int ERROR_NONE = 0;
    public Exception e;
    public int type;

    public ReadContactsError(int i, Exception exception) {
        this.type = -1;
        this.type = i;
        this.e = exception;
    }

    public ReadContactsError(Exception exception) {
        this.type = -1;
        this.e = exception;
    }

    public ReadContactsError(int i, String str) {
        this(i, new RuntimeException(str));
    }

    public ReadContactsError(String str) {
        this(0, new RuntimeException(str));
    }
}
