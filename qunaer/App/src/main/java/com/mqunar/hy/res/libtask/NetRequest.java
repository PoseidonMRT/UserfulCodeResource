package com.mqunar.hy.res.libtask;

import android.net.http.Headers;
import android.os.Handler.Callback;
import android.os.Looper;
import android.text.TextUtils;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import org.apache.http.client.methods.HttpUriRequest;

class NetRequest {
    private static final String HEADER_DEFAULTVALUE_USERAGENT = "QSpiderAndroid";
    private static final String HEADER_KEY_USERAGENT = "User-Agent";
    byte[] content;
    public String contentType;
    public Callback handler;
    public Map<String, String> header;
    HttpUriRequest httpRequest;
    public int id;
    InputStream stream;
    public long total;
    public String url;

    private void addDefaultUserAgent() {
        if (this.header == null) {
            this.header = new HashMap();
            this.header.put("User-Agent", HEADER_DEFAULTVALUE_USERAGENT);
        }
    }

    public NetRequest(String str, byte[] bArr, Callback callback) {
        this.url = str;
        this.content = bArr;
        this.handler = callback;
        addDefaultUserAgent();
        generateId();
    }

    public NetRequest(String str, InputStream inputStream, Callback callback) {
        this.url = str;
        this.stream = inputStream;
        this.handler = callback;
        addDefaultUserAgent();
        generateId();
    }

    public void cancel() {
        if (this.httpRequest == null) {
            return;
        }
        if (Looper.myLooper() == Looper.getMainLooper()) {
            new Thread() {
                public void run() {
                    super.run();
                    try {
                        NetRequest.this.httpRequest.abort();
                    } catch (Throwable th) {
                    }
                }
            }.start();
            return;
        }
        try {
            this.httpRequest.abort();
        } catch (Throwable th) {
        }
    }

    private void generateId() {
        this.id = hashCode();
    }

    public void addHeader(String str, String str2) {
        if (!TextUtils.isEmpty(str) && !TextUtils.isEmpty(str2)) {
            this.header.put(str, str2);
            if (str.equalsIgnoreCase(Headers.CONTENT_TYPE)) {
                this.contentType = str2;
            }
        }
    }

    public boolean hasHeader() {
        return this.header != null;
    }
}
