package com.mqunar.exception;

import android.content.res.Resources.NotFoundException;

public class ResNotFoundException extends NotFoundException {
    public NotFoundException notFoundException;
    public int resId;

    public ResNotFoundException(int i, NotFoundException notFoundException) {
        this(notFoundException == null ? "Resource ID #0x" + Integer.toHexString(i) : notFoundException.getMessage(), i, notFoundException);
    }

    public ResNotFoundException(String str, int i, NotFoundException notFoundException) {
        super(str);
        this.resId = i;
        this.notFoundException = notFoundException;
        if (notFoundException != null) {
            try {
                setStackTrace(notFoundException.getStackTrace());
            } catch (Exception e) {
            }
        }
    }
}
