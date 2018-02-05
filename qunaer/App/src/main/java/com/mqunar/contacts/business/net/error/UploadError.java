package com.mqunar.contacts.business.net.error;

public class UploadError {
    public static final int TYPE_EMPTY_PARAM_FAILURE = 3;
    public static final int TYPE_LICENSES_FAILURE = 1;
    public static final int TYPE_UNKNOWN = -1;
    public static final int TYPE_VALIDATE_FAILURE = 2;
    public String error;
    public int type;

    public UploadError(int i, String str) {
        this.type = 0;
        this.type = i;
        this.error = str;
    }

    public UploadError(String str) {
        this(-1, str);
    }
}
