package org.acra.util;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Base64;
import com.mqunar.necro.agent.instrumentation.HttpInstrumentation;
import com.mqunar.necro.agent.instrumentation.Instrumented;
import java.io.BufferedOutputStream;
import java.io.Closeable;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Map;
import java.util.Map.Entry;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;
import org.acra.ACRA;
import org.acra.config.ACRAConfiguration;
import org.acra.security.d;
import org.acra.sender.HttpSender.Method;
import org.acra.sender.HttpSender.Type;
import org.apache.http.HttpStatus;
import org.apache.http.auth.AUTH;
import org.apache.http.protocol.HTTP;

@Instrumented
public final class f {
    private final ACRAConfiguration a;
    private String b;
    private String c;
    private int d = HttpConnectionPool.CONNECTION_TIMEOUT;
    private int e = HttpConnectionPool.CONNECTION_TIMEOUT;
    private Map<String, String> f;

    public f(@NonNull ACRAConfiguration aCRAConfiguration) {
        this.a = aCRAConfiguration;
    }

    public void a(@Nullable String str) {
        this.b = str;
    }

    public void b(@Nullable String str) {
        this.c = str;
    }

    public void a(int i) {
        this.d = i;
    }

    public void b(int i) {
        this.e = i;
    }

    public void a(@Nullable Map<String, String> map) {
        this.f = map;
    }

    public void a(@NonNull Context context, @NonNull URL url, @NonNull Method method, @NonNull String str, @NonNull Type type) {
        HttpURLConnection httpURLConnection = (HttpURLConnection) HttpInstrumentation.openConnection(url.openConnection());
        if (httpURLConnection instanceof HttpsURLConnection) {
            try {
                HttpsURLConnection httpsURLConnection = (HttpsURLConnection) httpURLConnection;
                TrustManagerFactory instance = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
                instance.init(d.a(context, this.a));
                SSLContext instance2 = SSLContext.getInstance("TLS");
                instance2.init(null, instance.getTrustManagers(), null);
                httpsURLConnection.setSSLSocketFactory(instance2.getSocketFactory());
            } catch (Throwable e) {
                ACRA.f.c(ACRA.e, "Could not configure SSL for ACRA request to " + url, e);
            }
        }
        if (!(this.b == null || this.c == null)) {
            httpURLConnection.setRequestProperty(AUTH.WWW_AUTH_RESP, "Basic " + new String(Base64.encode((this.b + ':' + this.c).getBytes("UTF-8"), 2), "UTF-8"));
        }
        httpURLConnection.setConnectTimeout(this.d);
        httpURLConnection.setReadTimeout(this.e);
        httpURLConnection.setRequestProperty(HTTP.USER_AGENT, String.format("Android ACRA %1$s", new Object[]{"2.1.2"}));
        httpURLConnection.setRequestProperty("Accept", "text/html,application/xml,application/json,application/xhtml+xml,text/html;q=0.9,text/plain;q=0.8,image/png,*/*;q=0.5");
        httpURLConnection.setRequestProperty("Content-Type", type.getContentType());
        if (this.f != null) {
            for (Entry entry : this.f.entrySet()) {
                httpURLConnection.setRequestProperty((String) entry.getKey(), (String) entry.getValue());
            }
        }
        byte[] bytes = str.getBytes("UTF-8");
        httpURLConnection.setRequestMethod(method.name());
        httpURLConnection.setDoOutput(true);
        httpURLConnection.setFixedLengthStreamingMode(bytes.length);
        System.setProperty("http.keepAlive", "false");
        httpURLConnection.connect();
        Closeable bufferedOutputStream = new BufferedOutputStream(httpURLConnection.getOutputStream());
        try {
            bufferedOutputStream.write(bytes);
            bufferedOutputStream.flush();
            ACRA.f.b(ACRA.e, "Sending request to " + url);
            ACRA.f.b(ACRA.e, "Http " + method.name() + " content : ");
            ACRA.f.b(ACRA.e, str);
            int responseCode = httpURLConnection.getResponseCode();
            ACRA.f.b(ACRA.e, "Request response : " + responseCode + " : " + httpURLConnection.getResponseMessage());
            if (responseCode >= 200 && responseCode < 300) {
                ACRA.f.c(ACRA.e, "Request received by server");
            } else if (responseCode == HttpStatus.SC_UNAUTHORIZED) {
                ACRA.f.d(ACRA.e, "401: Login validation error on server - request will be discarded");
            } else if (responseCode == HttpStatus.SC_FORBIDDEN) {
                ACRA.f.d(ACRA.e, "403: Data validation error on server - request will be discarded");
            } else if (responseCode == HttpStatus.SC_METHOD_NOT_ALLOWED) {
                ACRA.f.d(ACRA.e, "405: Server rejected Http " + method + " - request will be discarded");
            } else if (responseCode == HttpStatus.SC_CONFLICT) {
                ACRA.f.d(ACRA.e, "409: Server has already received this post - request will be discarded");
            } else if (responseCode < HttpStatus.SC_BAD_REQUEST || responseCode >= 600) {
                ACRA.f.d(ACRA.e, "Could not send ACRA Post - request will be discarded. responseCode=" + responseCode + " message=" + httpURLConnection.getResponseMessage());
            } else {
                ACRA.f.d(ACRA.e, "Could not send ACRA Post responseCode=" + responseCode + " message=" + httpURLConnection.getResponseMessage());
                throw new IOException("Host returned error code " + responseCode);
            }
            httpURLConnection.disconnect();
        } finally {
            g.a(bufferedOutputStream);
        }
    }

    @NonNull
    public static String b(@NonNull Map<?, ?> map) {
        StringBuilder stringBuilder = new StringBuilder();
        for (Entry entry : map.entrySet()) {
            if (stringBuilder.length() != 0) {
                stringBuilder.append('&');
            }
            Object value = entry.getValue();
            if (value == null) {
                value = "";
            }
            stringBuilder.append(URLEncoder.encode(entry.getKey().toString(), "UTF-8"));
            stringBuilder.append('=');
            stringBuilder.append(URLEncoder.encode(value.toString(), "UTF-8"));
        }
        return stringBuilder.toString();
    }
}
