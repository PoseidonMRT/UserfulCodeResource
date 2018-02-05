package com.android.internal.http.multipart;

import java.io.File;
import java.io.OutputStream;

public class FilePart extends PartBase {
    public static final String DEFAULT_CHARSET = "ISO-8859-1";
    public static final String DEFAULT_CONTENT_TYPE = "application/octet-stream";
    public static final String DEFAULT_TRANSFER_ENCODING = "binary";
    protected static final String FILE_NAME = "; filename=";

    public FilePart(String str, PartSource partSource, String str2, String str3) {
        super((String) null, (String) null, (String) null, (String) null);
        throw new RuntimeException("Stub!");
    }

    public FilePart(String str, PartSource partSource) {
        super((String) null, (String) null, (String) null, (String) null);
        throw new RuntimeException("Stub!");
    }

    public FilePart(String str, File file) {
        super((String) null, (String) null, (String) null, (String) null);
        throw new RuntimeException("Stub!");
    }

    public FilePart(String str, File file, String str2, String str3) {
        super((String) null, (String) null, (String) null, (String) null);
        throw new RuntimeException("Stub!");
    }

    public FilePart(String str, String str2, File file) {
        super((String) null, (String) null, (String) null, (String) null);
        throw new RuntimeException("Stub!");
    }

    public FilePart(String str, String str2, File file, String str3, String str4) {
        super((String) null, (String) null, (String) null, (String) null);
        throw new RuntimeException("Stub!");
    }

    protected void sendDispositionHeader(OutputStream outputStream) {
        throw new RuntimeException("Stub!");
    }

    protected void sendData(OutputStream outputStream) {
        throw new RuntimeException("Stub!");
    }

    protected PartSource getSource() {
        throw new RuntimeException("Stub!");
    }

    protected long lengthOfData() {
        throw new RuntimeException("Stub!");
    }
}
