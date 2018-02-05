package com.mqunar.libtask;

import java.io.Serializable;

public class FormPart implements Serializable {
    private static final long serialVersionUID = 1;
    final String file;
    final HttpHeader header;
    final String metaData;
    final String name;
    final String value;

    public FormPart(String str, String str2) {
        this.name = str;
        this.value = str2;
        this.file = null;
        this.metaData = null;
        this.header = new HttpHeader();
    }

    public FormPart(String str, String str2, String str3) {
        this.name = str;
        this.value = null;
        this.file = str2;
        this.metaData = str3;
        this.header = new HttpHeader();
    }

    public void addHeader(String str, String str2) {
        if (str == null) {
            throw new IllegalArgumentException("Header name may not be null");
        }
        this.header.addHeader(str, str2);
    }
}
