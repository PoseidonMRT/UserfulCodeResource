package com.mqunar.exception;

public class AtomFileNotFoundException extends ClassNotFoundException {
    public String atomFilePath;

    public AtomFileNotFoundException(String str, Throwable th) {
        super("file not exist:" + str + "\nmessage = " + (th == null ? "null " : th.getMessage()), th);
        this.atomFilePath = str;
    }
}
