package com.mapquest.android.maps;

import com.mqunar.necro.agent.instrumentation.HttpInstrumentation;
import com.mqunar.necro.agent.instrumentation.Instrumented;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.HttpVersion;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;

@Instrumented
public final class HttpUtil {
    private static final int CONNECTION_TIMEOUT = 10000;
    private static final String LOG_TAG = HttpUtil.class.getName();
    private static final int READ_TIMEOUT = 30000;

    HttpUtil() {
    }

    public static String runPost(String str, Map<String, String> map) {
        InputStream executePostAsStream = executePostAsStream(str, map);
        if (executePostAsStream == null) {
            return null;
        }
        String convertStreamToString = convertStreamToString(executePostAsStream);
        try {
            executePostAsStream.close();
            return convertStreamToString;
        } catch (IOException e) {
            return convertStreamToString;
        }
    }

    public static String runRequest(String str, Map<String, String> map) {
        InputStream executeAsStream = executeAsStream(str, (Map) map);
        if (executeAsStream == null) {
            return null;
        }
        String convertStreamToString = convertStreamToString(executeAsStream);
        try {
            executeAsStream.close();
            return convertStreamToString;
        } catch (IOException e) {
            return convertStreamToString;
        }
    }

    public static String runRequest(String str) {
        return runRequest(str, (Map) null);
    }

    private static HttpParams getHttpConnectionParams(int i, int i2) {
        HttpParams basicHttpParams = new BasicHttpParams();
        HttpConnectionParams.setSocketBufferSize(basicHttpParams, 8192);
        HttpProtocolParams.setVersion(basicHttpParams, HttpVersion.HTTP_1_1);
        HttpProtocolParams.setContentCharset(basicHttpParams, "utf-8");
        HttpProtocolParams.setUseExpectContinue(basicHttpParams, false);
        if (i <= 0) {
            i = 10000;
        }
        HttpConnectionParams.setConnectionTimeout(basicHttpParams, i);
        if (i2 <= 0) {
            i2 = HttpConnectionPool.CONNECTION_TIMEOUT;
        }
        HttpConnectionParams.setSoTimeout(basicHttpParams, i2);
        return basicHttpParams;
    }

    public static InputStream executePostAsStream(String str, Map<String, String> map) {
        DefaultHttpClient defaultHttpClient = new DefaultHttpClient(getHttpConnectionParams(10000, HttpConnectionPool.CONNECTION_TIMEOUT));
        Object httpPost = new HttpPost(str);
        try {
            List arrayList = new ArrayList(map.size());
            for (String str2 : map.keySet()) {
                arrayList.add(new BasicNameValuePair(str2, (String) map.get(str2)));
            }
            httpPost.setEntity(new UrlEncodedFormEntity(arrayList));
            HttpResponse execute = !(defaultHttpClient instanceof HttpClient) ? defaultHttpClient.execute(httpPost) : HttpInstrumentation.execute(defaultHttpClient, httpPost);
            int statusCode = execute.getStatusLine().getStatusCode();
            if (statusCode != 200 && statusCode != HttpStatus.SC_NO_CONTENT) {
                return null;
            }
            if (statusCode == HttpStatus.SC_NO_CONTENT) {
                return null;
            }
            HttpEntity entity = execute.getEntity();
            if (entity != null) {
                return entity.getContent();
            }
            return null;
        } catch (Exception e) {
            httpPost.abort();
        }
    }

    public static InputStream executeAsStream(String str) {
        return executeAsStream(str, (Map) null);
    }

    public static InputStream executeAsStream(String str, int i) {
        return executeAsStream(str, (Map) null, i);
    }

    public static InputStream executeAsStream(String str, Map<String, String> map) {
        return executeAsStream(str, map, 10000);
    }

    public static InputStream executeAsStream(String str, Map<String, String> map, int i) {
        InputStream content;
        DefaultHttpClient defaultHttpClient = new DefaultHttpClient(getHttpConnectionParams(i, i));
        HttpUriRequest httpGet = new HttpGet(str);
        if (map != null) {
            try {
                for (String str2 : map.keySet()) {
                    httpGet.addHeader(str2, (String) map.get(str2));
                }
            } catch (Exception e) {
                httpGet.abort();
                return null;
            }
        }
        HttpEntity entity = (!(defaultHttpClient instanceof HttpClient) ? defaultHttpClient.execute(httpGet) : HttpInstrumentation.execute(defaultHttpClient, httpGet)).getEntity();
        if (entity != null) {
            content = entity.getContent();
        } else {
            content = null;
        }
        return content;
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static java.lang.String convertStreamToString(java.io.InputStream r4) {
        /*
        r0 = new java.io.BufferedReader;
        r1 = new java.io.InputStreamReader;
        r1.<init>(r4);
        r0.<init>(r1);
        r1 = new java.lang.StringBuilder;
        r1.<init>();
    L_0x000f:
        r2 = r0.readLine();	 Catch:{ IOException -> 0x002c }
        if (r2 == 0) goto L_0x0038;
    L_0x0015:
        r3 = new java.lang.StringBuilder;	 Catch:{ IOException -> 0x002c }
        r3.<init>();	 Catch:{ IOException -> 0x002c }
        r2 = r3.append(r2);	 Catch:{ IOException -> 0x002c }
        r3 = "\n";
        r2 = r2.append(r3);	 Catch:{ IOException -> 0x002c }
        r2 = r2.toString();	 Catch:{ IOException -> 0x002c }
        r1.append(r2);	 Catch:{ IOException -> 0x002c }
        goto L_0x000f;
    L_0x002c:
        r0 = move-exception;
        r0.printStackTrace();	 Catch:{ all -> 0x0046 }
        r4.close();	 Catch:{ IOException -> 0x0041 }
    L_0x0033:
        r0 = r1.toString();
        return r0;
    L_0x0038:
        r4.close();	 Catch:{ IOException -> 0x003c }
        goto L_0x0033;
    L_0x003c:
        r0 = move-exception;
        r0.printStackTrace();
        goto L_0x0033;
    L_0x0041:
        r0 = move-exception;
        r0.printStackTrace();
        goto L_0x0033;
    L_0x0046:
        r0 = move-exception;
        r4.close();	 Catch:{ IOException -> 0x004b }
    L_0x004a:
        throw r0;
    L_0x004b:
        r1 = move-exception;
        r1.printStackTrace();
        goto L_0x004a;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.mapquest.android.maps.HttpUtil.convertStreamToString(java.io.InputStream):java.lang.String");
    }
}
