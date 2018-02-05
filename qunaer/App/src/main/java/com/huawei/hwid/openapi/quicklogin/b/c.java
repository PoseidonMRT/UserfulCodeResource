package com.huawei.hwid.openapi.quicklogin.b;

import android.os.Bundle;
import android.text.TextUtils;
import com.baidu.location.LocationClientOption;
import com.huawei.hwid.openapi.out.OutReturn.ParamStr;
import com.huawei.hwid.openapi.quicklogin.a.a;
import com.huawei.hwid.openapi.quicklogin.b.a.b;
import com.huawei.hwid.openapi.quicklogin.d.b.d;
import com.huawei.hwid.openapi.quicklogin.d.b.k;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.params.HttpClientParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

public abstract class c {
    protected static final String a = a.b;
    protected String b = "2090";

    public abstract b a(String str);

    public abstract HttpEntity a();

    public abstract String b();

    public abstract d c();

    public HttpUriRequest d() {
        if (c() == d.URLType) {
            return f();
        }
        return e();
    }

    public Bundle a(HttpResponse httpResponse) {
        Bundle bundle = new Bundle();
        if (httpResponse != null) {
            bundle.putInt(ParamStr.RET_RES_CODE, httpResponse.getStatusLine().getStatusCode());
            bundle.putString(ParamStr.RET_RES_CONTENT, b(httpResponse));
            d.a("Request", k.a(bundle.getString(ParamStr.RET_RES_CONTENT), true));
            Header[] allHeaders = httpResponse.getAllHeaders();
            Bundle bundle2 = new Bundle();
            for (Header header : allHeaders) {
                bundle2.putString(header.getName(), header.getValue());
            }
            bundle.putBundle(ParamStr.RET_RES_HEAD, bundle2);
        }
        return bundle;
    }

    public boolean a(Bundle bundle) {
        if (com.huawei.hwid.openapi.quicklogin.b.b.c.a.a(bundle) != 200) {
            return false;
        }
        CharSequence c = com.huawei.hwid.openapi.quicklogin.b.b.c.a.c(bundle);
        if (TextUtils.isEmpty(c) || "0".equals(c)) {
            return true;
        }
        return false;
    }

    public String b(HttpResponse httpResponse) {
        try {
            return EntityUtils.toString(httpResponse.getEntity(), "UTF-8");
        } catch (Throwable e) {
            d.b(a, e.getMessage(), e);
            return null;
        }
    }

    private final HttpUriRequest a(HttpUriRequest httpUriRequest) {
        httpUriRequest.addHeader(HTTP.CONN_DIRECTIVE, HTTP.CONN_KEEP_ALIVE);
        if (d.URLType.equals(c())) {
            httpUriRequest.addHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
        } else {
            httpUriRequest.addHeader("Content-Type", "text/html; charset=UTF-8");
        }
        httpUriRequest.getParams().setIntParameter("http.socket.timeout", LocationClientOption.MIN_AUTO_NOTIFY_INTERVAL);
        httpUriRequest.getParams().setIntParameter("http.connection.timeout", LocationClientOption.MIN_AUTO_NOTIFY_INTERVAL);
        HttpClientParams.setRedirecting(httpUriRequest.getParams(), false);
        return httpUriRequest;
    }

    private HttpGet f() {
        HttpUriRequest httpGet = new HttpGet(b());
        a(httpGet);
        return httpGet;
    }

    protected HttpPost e() {
        try {
            d.b(a, k.a("the post request URI is:" + b()));
            HttpUriRequest httpPost = new HttpPost(b());
            a(httpPost);
            HttpEntity a = a();
            if (a == null) {
                return httpPost;
            }
            httpPost.setEntity(a);
            return httpPost;
        } catch (Throwable e) {
            d.b(a, k.a(e.toString()), e);
            return null;
        }
    }
}
