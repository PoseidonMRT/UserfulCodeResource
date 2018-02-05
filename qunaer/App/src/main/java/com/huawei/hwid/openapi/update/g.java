package com.huawei.hwid.openapi.update;

import android.content.Context;
import com.huawei.hwid.openapi.quicklogin.d.a.c;
import com.huawei.hwid.openapi.quicklogin.d.b.k;
import com.huawei.hwid.openapi.update.a.a;
import com.mqunar.necro.agent.instrumentation.HttpInstrumentation;
import com.mqunar.necro.agent.instrumentation.Instrumented;
import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.DefaultHttpClient;

@Instrumented
public class g {
    private Context a;

    public g(Context context) {
        this.a = context;
    }

    public ByteArrayOutputStream a(int i) {
        OutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        a aVar = new a(this.a);
        String str = "http://query.hicloud.com/hwid/v2/CheckEx.action";
        c.b("OtaHttpRequest", "url: " + k.a("http://query.hicloud.com/hwid/v2/CheckEx.action"));
        c.b("OtaHttpRequest", "deviceInfo: " + k.a(aVar.a(i).toString()));
        int a = a("http://query.hicloud.com/hwid/v2/CheckEx.action", new ByteArrayEntity(aVar.a(i).toString().getBytes(Charset.forName("UTF-8"))), byteArrayOutputStream);
        c.b("OtaHttpRequest", "statusCode:" + a);
        return a == 200 ? byteArrayOutputStream : null;
    }

    private int a(String str, ByteArrayEntity byteArrayEntity, OutputStream outputStream) {
        String replace;
        HttpResponse httpResponse;
        int i;
        HttpClient defaultHttpClient = new DefaultHttpClient();
        if (str.startsWith("http:")) {
            replace = str.replaceFirst("http:", "https:").replace(":8180", "");
        } else {
            replace = str;
        }
        HttpUriRequest httpPost = new HttpPost(replace);
        if (byteArrayEntity != null) {
            byteArrayEntity.setChunked(false);
            byteArrayEntity.setContentEncoding("UTF-8");
            httpPost.setEntity(byteArrayEntity);
        }
        httpPost.getParams().setIntParameter("http.socket.timeout", 30000);
        httpPost.getParams().setIntParameter("http.connection.timeout", 30000);
        try {
            HttpResponse execute;
            if (defaultHttpClient instanceof HttpClient) {
                execute = HttpInstrumentation.execute(defaultHttpClient, httpPost);
            } else {
                execute = defaultHttpClient.execute(httpPost);
            }
            httpResponse = execute;
        } catch (Exception e) {
            try {
                if (str.startsWith("https")) {
                    str = str.replaceFirst("https", HttpHost.DEFAULT_SCHEME_NAME);
                }
                httpPost = new HttpPost(str);
                defaultHttpClient = new DefaultHttpClient();
                if (byteArrayEntity != null) {
                    httpPost.setEntity(byteArrayEntity);
                }
                httpPost.getParams().setIntParameter("http.socket.timeout", 30000);
                httpPost.getParams().setIntParameter("http.connection.timeout", 30000);
                httpResponse = !(defaultHttpClient instanceof HttpClient) ? defaultHttpClient.execute(httpPost) : HttpInstrumentation.execute(defaultHttpClient, httpPost);
            } catch (Exception e2) {
                c.d("OtaHttpRequest", "http Exception: " + e2.getMessage());
                return -1;
            }
        }
        if (httpResponse == null || httpResponse.getStatusLine() == null) {
            i = -1;
        } else {
            i = httpResponse.getStatusLine().getStatusCode();
            if (outputStream != null) {
                httpResponse.getEntity().writeTo(outputStream);
            }
        }
        return i;
    }

    public ByteArrayOutputStream a(String str) {
        OutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        return a(str, null, byteArrayOutputStream) == 200 ? byteArrayOutputStream : null;
    }

    public boolean a(com.huawei.hwid.openapi.update.a.c cVar) {
        if (cVar != null && 200 == a("http://query.hicloud.com/hwid/v2/UpdateReport.action", new ByteArrayEntity(cVar.a(this.a).toString().getBytes(Charset.forName("UTF-8"))), null)) {
            return true;
        }
        return false;
    }

    public ByteArrayOutputStream b(String str) {
        OutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        return a(str, byteArrayOutputStream) == 200 ? byteArrayOutputStream : null;
    }

    private int a(String str, OutputStream outputStream) {
        String replace;
        HttpResponse httpResponse;
        int i;
        HttpClient defaultHttpClient = new DefaultHttpClient();
        if (str.startsWith("http:")) {
            replace = str.replaceFirst("http:", "https:").replace(":8180", "");
        } else {
            replace = str;
        }
        HttpUriRequest httpGet = new HttpGet(replace);
        httpGet.getParams().setIntParameter("http.socket.timeout", 30000);
        httpGet.getParams().setIntParameter("http.connection.timeout", 30000);
        try {
            HttpResponse execute;
            if (defaultHttpClient instanceof HttpClient) {
                execute = HttpInstrumentation.execute(defaultHttpClient, httpGet);
            } else {
                execute = defaultHttpClient.execute(httpGet);
            }
            httpResponse = execute;
        } catch (Exception e) {
            try {
                if (str.startsWith("https")) {
                    str = str.replaceFirst("https", HttpHost.DEFAULT_SCHEME_NAME);
                }
                HttpUriRequest httpGet2 = new HttpGet(str);
                httpGet2.getParams().setIntParameter("http.socket.timeout", 30000);
                httpGet2.getParams().setIntParameter("http.connection.timeout", 30000);
                defaultHttpClient = new DefaultHttpClient();
                httpResponse = !(defaultHttpClient instanceof HttpClient) ? defaultHttpClient.execute(httpGet2) : HttpInstrumentation.execute(defaultHttpClient, httpGet2);
            } catch (Exception e2) {
                c.d("OtaHttpRequest", "http Exception" + e2.getMessage());
                return -1;
            }
        }
        if (httpResponse == null || httpResponse.getStatusLine() == null) {
            i = -1;
        } else {
            i = httpResponse.getStatusLine().getStatusCode();
            if (outputStream != null) {
                httpResponse.getEntity().writeTo(outputStream);
            }
        }
        return i;
    }
}
