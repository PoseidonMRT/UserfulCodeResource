package com.huawei.hwid.openapi.quicklogin.b.b.b;

import android.content.Context;
import android.os.Bundle;
import com.huawei.hwid.openapi.f.b;
import com.huawei.hwid.openapi.quicklogin.d.b.d;
import com.huawei.hwid.openapi.quicklogin.d.b.k;
import com.mqunar.necro.agent.instrumentation.HttpInstrumentation;
import com.mqunar.necro.agent.instrumentation.Instrumented;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLDecoder;
import java.net.UnknownHostException;
import javax.net.ssl.SSLHandshakeException;
import javax.net.ssl.SSLPeerUnverifiedException;
import org.apache.http.Header;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpUriRequest;

@Instrumented
public class a {
    private static final String a = com.huawei.hwid.openapi.quicklogin.a.a.b;

    public static HttpResponse a(Context context, HttpUriRequest httpUriRequest) {
        try {
            HttpClient a = new b().a(context);
            d.a(a, k.a("direct connect start! req:" + a(httpUriRequest)));
            return !(a instanceof HttpClient) ? a.execute(httpUriRequest) : HttpInstrumentation.execute(a, httpUriRequest);
        } catch (Exception e) {
            if (e instanceof UnsupportedEncodingException) {
                throw new UnsupportedEncodingException("UnsupportedEncodingException[don't set proxy]:" + e.getMessage());
            } else if (e instanceof IllegalArgumentException) {
                throw new IllegalArgumentException("IllegalArgumentException[don't set proxy]:" + e.getMessage());
            } else if (e instanceof IllegalStateException) {
                throw new IllegalStateException("IllegalStateException[don't set proxy]:" + e.getMessage());
            } else if (e instanceof SSLPeerUnverifiedException) {
                throw new SSLPeerUnverifiedException("SSLPeerUnverifiedException[don't set proxy]:" + e.getMessage());
            } else if (e instanceof SSLHandshakeException) {
                throw new SSLHandshakeException("SSLHandshakeException[don't set proxy]:" + e.getMessage());
            } else if (e instanceof IOException) {
                throw new IOException("IOException[don't set proxy]:" + e.getMessage());
            } else {
                throw new UnknownHostException("don't set proxy");
            }
        }
    }

    public static boolean a(String str) {
        if (str == null || str.trim().equals("")) {
            return true;
        }
        return false;
    }

    public static Bundle b(String str) {
        Bundle bundle = new Bundle();
        if (str != null) {
            for (String split : str.split("&")) {
                String split2;
                String[] split3 = split2.split("=");
                if (split3.length == 2) {
                    String str2 = split3[0];
                    split2 = split3[1];
                    if (!(a(str2) || a(split2))) {
                        bundle.putString(c(str2), c(split2));
                    }
                }
            }
        }
        return bundle;
    }

    public static String c(String str) {
        try {
            str = URLDecoder.decode(str, "UTF-8");
        } catch (Throwable e) {
            d.b(a, e.getMessage(), e);
        }
        return str;
    }

    public static Bundle d(String str) {
        try {
            URL url = new URL(str.replace("bdconnect", HttpHost.DEFAULT_SCHEME_NAME));
            Bundle b = b(url.getQuery());
            b.putAll(b(url.getRef()));
            return b;
        } catch (Throwable e) {
            d.b(a, e.getMessage(), e);
            return new Bundle();
        }
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
