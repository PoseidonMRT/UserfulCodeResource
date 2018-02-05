package com.huawei.hwid.openapi.e.b;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import com.huawei.hwid.openapi.a.b;
import com.huawei.hwid.openapi.quicklogin.d.b.d;
import com.huawei.hwid.openapi.quicklogin.d.b.k;
import com.mqunar.necro.agent.instrumentation.HttpInstrumentation;
import com.mqunar.necro.agent.instrumentation.Instrumented;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import javax.net.ssl.SSLException;
import javax.net.ssl.SSLHandshakeException;
import javax.net.ssl.SSLPeerUnverifiedException;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.message.BasicNameValuePair;

@Instrumented
public class a {
    private static final String a = b.a;
    private static HttpClient b = null;

    private static synchronized HttpClient a(Context context) {
        HttpClient httpClient;
        synchronized (a.class) {
            if (b == null) {
                b = new com.huawei.hwid.openapi.f.b().a(context);
            }
            httpClient = b;
        }
        return httpClient;
    }

    public static HttpResponse a(Context context, HttpUriRequest httpUriRequest) {
        try {
            a(context);
            d.a(a, "direct connect start! req:" + k.a(a(httpUriRequest), true));
            HttpClient httpClient = b;
            return !(httpClient instanceof HttpClient) ? httpClient.execute(httpUriRequest) : HttpInstrumentation.execute(httpClient, httpUriRequest);
        } catch (RuntimeException e) {
            if (e instanceof IllegalArgumentException) {
                throw new IllegalArgumentException("IllegalArgumentException[don't set proxy]:" + e.getMessage());
            } else if (e instanceof IllegalStateException) {
                throw new IllegalStateException("IllegalStateException[don't set proxy]:" + e.getMessage());
            } else {
                throw new UnknownHostException("don't set proxy");
            }
        } catch (Exception e2) {
            if (e2 instanceof UnsupportedEncodingException) {
                throw new UnsupportedEncodingException("UnsupportedEncodingException[don't set proxy]:" + e2.getMessage());
            } else if (e2 instanceof SSLHandshakeException) {
                throw new SSLHandshakeException("SSLHandshakeException[don't set proxy]:" + e2.getMessage());
            } else if (e2 instanceof SSLPeerUnverifiedException) {
                throw new SSLPeerUnverifiedException("SSLPeerUnverifiedException[don't set proxy]:" + e2.getMessage());
            } else if (e2 instanceof SSLException) {
                throw new SSLException("SSLException[don't set proxy]:" + e2.getMessage());
            } else {
                throw new IOException("IOException[don't set proxy]:" + e2.getMessage());
            }
        }
    }

    public static UrlEncodedFormEntity a(HashMap hashMap) {
        if (hashMap != null) {
            try {
                if (hashMap.size() != 0) {
                    List arrayList = new ArrayList();
                    for (Entry entry : hashMap.entrySet()) {
                        arrayList.add(new BasicNameValuePair((String) entry.getKey(), (String) entry.getValue()));
                    }
                    return new UrlEncodedFormEntity(arrayList, "UTF-8");
                }
            } catch (Throwable e) {
                d.b(a, e.getMessage(), e);
                return null;
            }
        }
        return null;
    }

    public static String a(Bundle bundle) {
        if (bundle == null || bundle.isEmpty()) {
            return null;
        }
        StringBuilder stringBuilder = new StringBuilder();
        Object obj = 1;
        for (String str : bundle.keySet()) {
            Object obj2 = bundle.get(str);
            if (obj2 != null) {
                obj2 = String.valueOf(obj2);
                if (!TextUtils.isEmpty(obj2)) {
                    if (obj != null) {
                        obj = null;
                    } else {
                        stringBuilder.append("&");
                    }
                    stringBuilder.append(URLEncoder.encode(str)).append("=").append(URLEncoder.encode(obj2));
                }
            }
        }
        return stringBuilder.toString();
    }

    private static String a(HttpUriRequest httpUriRequest) {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(httpUriRequest.getMethod()).append(" ").append(httpUriRequest.getProtocolVersion());
        stringBuffer.append(" head:");
        for (Header header : httpUriRequest.getAllHeaders()) {
            stringBuffer.append(header.getName()).append("=").append(header.getValue());
        }
        stringBuffer.append(" reqLine:" + httpUriRequest.getRequestLine());
        return stringBuffer.toString();
    }
}
