package com.mqunar.network;

import android.net.http.Headers;
import android.os.Handler.Callback;
import android.os.Looper;
import android.text.TextUtils;
import java.io.File;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.mime.FormBodyPart;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.protocol.HTTP;

public class NetRequest {
    byte[] a;
    InputStream b;
    HttpUriRequest c;
    public String contentType;
    List<FormBodyPart> d;
    public Callback handler;
    public Map<String, String> header;
    public int id;
    public long total;
    public String url;

    private void a() {
        if (this.header == null) {
            this.header = new HashMap();
            this.header.put(HTTP.USER_AGENT, "QSpiderAndroid");
        }
    }

    public NetRequest(String str, byte[] bArr, Callback callback) {
        this.url = str;
        this.a = bArr;
        this.handler = callback;
        a();
        b();
    }

    public NetRequest(String str, InputStream inputStream, Callback callback) {
        this.url = str;
        this.b = inputStream;
        this.handler = callback;
        a();
        b();
    }

    public void cancel() {
        if (this.c == null) {
            return;
        }
        if (Looper.myLooper() == Looper.getMainLooper()) {
            new d(this).start();
            return;
        }
        try {
            this.c.abort();
        } catch (Throwable th) {
        }
    }

    @Deprecated
    public NetRequest multipart() {
        this.d = new ArrayList();
        return this;
    }

    private void b() {
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

    public NetRequest addFormDataPart(String str, String str2) {
        return addFormDataPart(str, str2, null);
    }

    public NetRequest addFormDataPart(String str, String str2, List<SimpleEntry<String, String>> list) {
        if (str == null) {
            throw new NullPointerException("name == null");
        }
        if (this.d == null) {
            this.d = new ArrayList();
        }
        try {
            FormBodyPart formBodyPart = new FormBodyPart(str, new StringBody(str2, Charset.forName("UTF-8")));
            if (!(list == null || list.isEmpty())) {
                for (SimpleEntry simpleEntry : list) {
                    formBodyPart.addField((String) simpleEntry.getKey(), (String) simpleEntry.getValue());
                }
            }
            this.d.add(formBodyPart);
        } catch (UnsupportedEncodingException e) {
        }
        return this;
    }

    public NetRequest addFormDataPart(String str, File file, String str2) {
        return addFormDataPart(str, file, str2, null);
    }

    public NetRequest addFormDataPart(String str, File file, String str2, List<SimpleEntry<String, String>> list) {
        if (str == null) {
            throw new NullPointerException("name == null");
        }
        if (this.d == null) {
            this.d = new ArrayList();
        }
        if (TextUtils.isEmpty(str2)) {
            str2 = "image/png";
        }
        FormBodyPart formBodyPart = new FormBodyPart(str, new FileBody(file, str2, "UTF-8"));
        if (!(list == null || list.isEmpty())) {
            for (SimpleEntry simpleEntry : list) {
                formBodyPart.addField((String) simpleEntry.getKey(), (String) simpleEntry.getValue());
            }
        }
        this.d.add(formBodyPart);
        return this;
    }
}
