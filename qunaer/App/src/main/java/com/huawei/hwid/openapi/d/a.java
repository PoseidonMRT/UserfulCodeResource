package com.huawei.hwid.openapi.d;

import android.os.Bundle;
import android.text.TextUtils;
import com.baidu.location.LocationClientOption;
import com.huawei.hwid.openapi.a.b;
import com.huawei.hwid.openapi.out.OutReturn;
import com.huawei.hwid.openapi.out.OutReturn.ParamStr;
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

public abstract class a {
    protected static final String a = b.a;

    public abstract HttpEntity a();

    public abstract String b();

    public abstract b c();

    public HttpUriRequest d() {
        if (c() == b.URLType) {
            return e();
        }
        return f();
    }

    public Bundle a(HttpResponse httpResponse) {
        Bundle bundle = new Bundle();
        if (httpResponse == null) {
            d.b(a, "rsp null!");
        } else {
            d.b(a, "rsp status code:" + httpResponse.getStatusLine().getStatusCode());
            bundle.putInt(ParamStr.RET_RES_CODE, httpResponse.getStatusLine().getStatusCode());
            bundle.putString(ParamStr.RET_RES_CONTENT, b(httpResponse));
            Header[] allHeaders = httpResponse.getAllHeaders();
            Bundle bundle2 = new Bundle();
            for (Header header : allHeaders) {
                bundle2.putString(header.getName(), header.getValue());
            }
            bundle.putBundle(ParamStr.RET_RES_HEAD, bundle2);
        }
        return bundle;
    }

    public boolean a(Bundle bundle, String str) {
        if (OutReturn.getRetResCode(bundle) != 200) {
            return false;
        }
        if (str.contains("https://login.vmall.com")) {
            String accessToken = OutReturn.getAccessToken(bundle);
            if (accessToken == null || "".equals(accessToken.trim())) {
                return false;
            }
        }
        CharSequence nspstatus = OutReturn.getNSPSTATUS(bundle);
        if (TextUtils.isEmpty(nspstatus) || "0".equals(nspstatus)) {
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
        if (b.URLType.equals(c())) {
            httpUriRequest.addHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
        } else {
            httpUriRequest.addHeader("Content-Type", "text/html; charset=UTF-8");
        }
        httpUriRequest.getParams().setIntParameter("http.socket.timeout", LocationClientOption.MIN_AUTO_NOTIFY_INTERVAL);
        httpUriRequest.getParams().setIntParameter("http.connection.timeout", LocationClientOption.MIN_AUTO_NOTIFY_INTERVAL);
        HttpClientParams.setRedirecting(httpUriRequest.getParams(), false);
        return httpUriRequest;
    }

    private HttpGet e() {
        HttpUriRequest httpGet = new HttpGet(b());
        a(httpGet);
        return httpGet;
    }

    private HttpPost f() {
        try {
            d.b(a, "the post request URI is:" + k.b(b()));
            HttpUriRequest httpPost = new HttpPost(b());
            a(httpPost);
            HttpEntity a = a();
            if (a == null) {
                return httpPost;
            }
            httpPost.setEntity(a);
            return httpPost;
        } catch (Throwable e) {
            d.b(a, e.getMessage(), e);
            return null;
        }
    }
}
