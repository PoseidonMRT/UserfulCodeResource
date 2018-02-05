package com.mqunar.network;

import android.content.Context;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.Proxy;
import android.net.Uri;
import android.os.Handler.Callback;
import android.os.Message;
import android.text.TextUtils;
import com.iflytek.aiui.AIUIConstant;
import com.iflytek.cloud.SpeechEvent;
import com.mqunar.libtask.ProgressType;
import com.mqunar.necro.agent.instrumentation.HttpInstrumentation;
import com.mqunar.necro.agent.instrumentation.Instrumented;
import com.mqunar.network.Headers.Builder;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Pattern;
import okhttp3.internal.http.StatusLine;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.HttpVersion;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.conn.params.ConnManagerPNames;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.conn.params.ConnPerRoute;
import org.apache.http.conn.params.ConnPerRouteBean;
import org.apache.http.conn.params.ConnRouteParams;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.scheme.SocketFactory;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.entity.mime.FormBodyPart;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.DefaultHttpRequestRetryHandler;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

@Instrumented
public class NetRequestManager {
    public static final int BUFFER_SIZE = 4096;
    public static final String CTWAP_APN_NAME_1 = "#777";
    public static final String CTWAP_APN_NAME_2 = "ctwap";
    public static final Uri PREFERRED_APN_URI = Uri.parse("content://telephony/carriers/preferapn");
    private static NetRequestManager g;
    private int a = 80000;
    private int b = 80000;
    private int c = 80000;
    private DefaultHttpClient d;
    private String e;
    private int f;
    private Map<String, String> h = new HashMap();
    private Map<String, List<String>> i = new HashMap();

    private boolean a(Context context, String str) {
        System.currentTimeMillis();
        DefaultHttpClient b = b(context, str);
        HttpUriRequest httpGet = new HttpGet(str);
        try {
            HttpResponse execute;
            if (b instanceof HttpClient) {
                execute = HttpInstrumentation.execute(b, httpGet);
            } else {
                execute = b.execute(httpGet);
            }
            if (execute.getStatusLine().getStatusCode() == 200) {
                JSONObject jSONObject = new JSONObject(EntityUtils.toString(execute.getEntity()));
                if (jSONObject.getInt("errcode") == 0) {
                    JSONArray jSONArray = jSONObject.getJSONArray(SpeechEvent.KEY_EVENT_RECORD_DATA);
                    synchronized (this.i) {
                        for (int i = 0; i < jSONArray.length(); i++) {
                            JSONObject jSONObject2 = jSONArray.getJSONObject(i);
                            CharSequence string = jSONObject2.getString("record");
                            CharSequence string2 = jSONObject2.getString(AIUIConstant.KEY_NAME);
                            if (!(TextUtils.isEmpty(string2) || TextUtils.isEmpty(string))) {
                                List list = (List) this.i.get(string2);
                                if (list == null) {
                                    list = new ArrayList();
                                    this.i.put(string2, list);
                                }
                                if (!list.contains(string)) {
                                    list.add(string);
                                }
                            }
                        }
                    }
                    return true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    private boolean a(Context context, String str, String str2) {
        Object obj = new boolean[1];
        AtomicInteger atomicInteger = new AtomicInteger(2);
        new e(this, context, str, obj, atomicInteger).start();
        new f(this, context, str2, obj, atomicInteger).start();
        if (!obj[0]) {
            try {
                synchronized (obj) {
                    obj.wait();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return obj[0];
    }

    public void requestIpList(Context context) {
        new g(this, context).start();
    }

    private String a(String str) {
        if (!c(str)) {
            synchronized (this.i) {
                String str2 = (String) this.h.get(str);
                if (TextUtils.isEmpty(str2)) {
                    List list = (List) this.i.get(str);
                    if (list == null || list.isEmpty()) {
                        Object substring;
                        if (str.endsWith(".")) {
                            substring = str.substring(0, str.length() - 1);
                        } else {
                            substring = str + ".";
                        }
                        list = (List) this.i.get(substring);
                    }
                    if (!(list == null || list.isEmpty())) {
                        str2 = (String) list.get((int) (Math.random() * ((double) list.size())));
                        if (!TextUtils.isEmpty(str2)) {
                            this.h.put(str, str2);
                            return str2;
                        }
                    }
                }
                return str2;
            }
        }
        return str;
    }

    private void b(String str) {
        if (!TextUtils.isEmpty(str) && !c(str)) {
            synchronized (this.i) {
                String str2 = (String) this.h.remove(str);
                for (String str3 : this.i.keySet()) {
                    ((List) this.i.get(str3)).remove(str2);
                }
            }
        }
    }

    private NetRequestManager() {
    }

    public static int getProxyPort(boolean z, Context context) {
        if (!z) {
            try {
                return Proxy.getPort(context);
            } catch (Throwable th) {
            }
        }
        return Proxy.getDefaultPort();
    }

    public static String getProxyHost(boolean z, Context context) {
        if (!z) {
            try {
                return Proxy.getHost(context);
            } catch (Throwable th) {
            }
        }
        return Proxy.getDefaultHost();
    }

    public static NetRequestManager getInstance() {
        if (g == null) {
            synchronized (NetRequestManager.class) {
                if (g == null) {
                    g = new NetRequestManager();
                }
            }
        }
        return g;
    }

    public static String getApnName(Context context) {
        String str = "";
        try {
            Cursor query = context.getContentResolver().query(PREFERRED_APN_URI, new String[]{"_id", "apn", "type"}, null, null, null);
            String str2;
            if (query != null) {
                query.moveToFirst();
                if (query.getCount() == 0 || query.isAfterLast()) {
                    str2 = str;
                } else {
                    str2 = query.getString(query.getColumnIndex("apn"));
                }
                query.close();
                return str2;
            }
            query = context.getContentResolver().query(PREFERRED_APN_URI, null, null, null, null);
            if (query == null) {
                return str;
            }
            query.moveToFirst();
            str2 = query.getString(query.getColumnIndex(AIUIConstant.USER));
            query.close();
            return str2;
        } catch (Exception e) {
            try {
                return ((ConnectivityManager) context.getSystemService("connectivity")).getActiveNetworkInfo().getExtraInfo();
            } catch (Exception e2) {
                return "";
            }
        }
    }

    public DefaultHttpClient setupProxy(Context context) {
        return b(context, null);
    }

    private DefaultHttpClient b(Context context, String str) {
        String proxyHost;
        int proxyPort;
        NetConnChangeReceiver.dealNetworkInfo(context);
        if (NetConnChangeReceiver.wifi) {
            proxyHost = getProxyHost(false, context);
            proxyPort = getProxyPort(false, context);
        } else {
            proxyHost = getProxyHost(true, context);
            proxyPort = getProxyPort(true, context);
        }
        if (!TextUtils.isEmpty(str) && (str.startsWith("https://") || str.startsWith("HTTPS://"))) {
            String apnName = getApnName(context);
            if (CTWAP_APN_NAME_1.equalsIgnoreCase(apnName) || CTWAP_APN_NAME_2.equalsIgnoreCase(apnName)) {
                proxyHost = null;
            }
        }
        DefaultHttpClient httpClient = getHttpClient(proxyHost, proxyPort);
        httpClient.addResponseInterceptor(new h(this));
        httpClient.setHttpRequestRetryHandler(new DefaultHttpRequestRetryHandler(6, true));
        return httpClient;
    }

    public void setTimeout(int i, int i2, int i3) {
        this.a = i;
        this.c = i3;
        this.b = i2;
    }

    private boolean c(String str) {
        return Pattern.compile("([1-9]|[1-9]//d|1//d{2}|2[0-4]//d|25[0-5])(//.(//d|[1-9]//d|1//d{2}|2[0-4]//d|25[0-5])){3}").matcher(str).matches();
    }

    public DefaultHttpClient getHttpClient(String str, int i) {
        if (this.d != null && a(this.e, str) && this.f == i) {
            return this.d;
        }
        this.e = str;
        this.f = i;
        DefaultHttpClient a = a(str, i);
        this.d = a;
        return a;
    }

    private boolean a(String str, String str2) {
        if (str == null) {
            return str2 == null;
        } else {
            return str.equals(str2);
        }
    }

    private DefaultHttpClient a(String str, int i) {
        SchemeRegistry schemeRegistry = new SchemeRegistry();
        schemeRegistry.register(new Scheme(HttpHost.DEFAULT_SCHEME_NAME, PlainSocketFactory.getSocketFactory(), 80));
        try {
            SocketFactory qSSLSocketFactory = new QSSLSocketFactory(null);
            qSSLSocketFactory.setHostnameVerifier(SSLSocketFactory.STRICT_HOSTNAME_VERIFIER);
            schemeRegistry.register(new Scheme("https", qSSLSocketFactory, 443));
        } catch (Exception e) {
            e.printStackTrace();
        }
        HttpParams basicHttpParams = new BasicHttpParams();
        HttpProtocolParams.setVersion(basicHttpParams, HttpVersion.HTTP_1_1);
        HttpProtocolParams.setContentCharset(basicHttpParams, "UTF-8");
        HttpConnectionParams.setConnectionTimeout(basicHttpParams, this.a);
        HttpConnectionParams.setSoTimeout(basicHttpParams, this.b);
        if (str != null && str.trim().length() > 0) {
            ConnRouteParams.setDefaultProxy(basicHttpParams, new HttpHost(str, i));
        }
        ConnManagerParams.setMaxTotalConnections(basicHttpParams, ProgressType.PRO_END);
        basicHttpParams.setIntParameter(ConnManagerPNames.MAX_TOTAL_CONNECTIONS, ProgressType.PRO_END);
        ConnPerRoute connPerRouteBean = new ConnPerRouteBean(20);
        ConnManagerParams.setMaxConnectionsPerRoute(basicHttpParams, connPerRouteBean);
        basicHttpParams.setParameter(ConnManagerPNames.MAX_CONNECTIONS_PER_ROUTE, connPerRouteBean);
        return new DefaultHttpClient(new l(this, basicHttpParams, schemeRegistry), basicHttpParams);
    }

    public static boolean isRedirect(int i) {
        switch (i) {
            case 300:
            case HttpStatus.SC_MOVED_PERMANENTLY /*301*/:
            case HttpStatus.SC_MOVED_TEMPORARILY /*302*/:
            case HttpStatus.SC_SEE_OTHER /*303*/:
            case 307:
            case StatusLine.HTTP_PERM_REDIRECT /*308*/:
                return true;
            default:
                return false;
        }
    }

    public NetResponse request(NetRequest netRequest, Context context) {
        NetResponse netResponse = new NetResponse();
        if (netRequest == null || netRequest.url == null || netRequest.url.length() == 0) {
            netResponse.error = 3;
            if (netRequest.handler != null) {
                sendMessageInfo(netRequest.handler, netResponse.id, netResponse.error, netResponse.e);
            }
            return netResponse;
        }
        netResponse.id = netRequest.id;
        DefaultHttpClient b = b(context, netRequest.url);
        try {
            OutputStreamWarp outputStreamWarp;
            HttpUriRequest httpUriRequest;
            Object obj;
            HttpResponse execute;
            if (netRequest.a != null) {
                HttpPost httpPost = new HttpPost(netRequest.url);
                outputStreamWarp = new OutputStreamWarp(netRequest);
                httpPost.setEntity(new i(this, netRequest.a, outputStreamWarp));
                httpUriRequest = httpPost;
            } else if (netRequest.b != null) {
                HttpPost httpPost2 = new HttpPost(netRequest.url);
                outputStreamWarp = new OutputStreamWarp(netRequest);
                httpPost2.setEntity(new j(this, netRequest.b, -1, outputStreamWarp));
                obj = httpPost2;
            } else if (netRequest.d != null) {
                HttpPost httpPost3 = new HttpPost(netRequest.url);
                outputStreamWarp = new OutputStreamWarp(netRequest);
                HttpEntity kVar = new k(this, HttpMultipartMode.BROWSER_COMPATIBLE, outputStreamWarp);
                for (FormBodyPart addPart : netRequest.d) {
                    kVar.addPart(addPart);
                }
                httpPost3.setEntity(kVar);
                obj = httpPost3;
            } else {
                obj = new HttpGet(netRequest.url);
                outputStreamWarp = null;
            }
            if (netRequest.hasHeader()) {
                for (Entry entry : netRequest.header.entrySet()) {
                    httpUriRequest.addHeader((String) entry.getKey(), (String) entry.getValue());
                }
            }
            netRequest.c = httpUriRequest;
            NetCookieUtils.a(context, netRequest.url, httpUriRequest);
            b.getCookieStore().clear();
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS Z z");
            simpleDateFormat.setLenient(false);
            httpUriRequest.addHeader("L-Date", simpleDateFormat.format(new Date()));
            if (b instanceof HttpClient) {
                execute = HttpInstrumentation.execute(b, httpUriRequest);
            } else {
                execute = b.execute(httpUriRequest);
            }
            NetCookieUtils.a(context, netRequest.url, execute);
            if (outputStreamWarp != null) {
                outputStreamWarp.checkNotify();
            }
            if (execute != null) {
                a aVar;
                int read;
                netResponse.code = execute.getStatusLine().getStatusCode();
                netResponse.redirect = isRedirect(netResponse.code);
                netResponse.headers = a(execute);
                InputStream content = execute.getEntity().getContent();
                long j = 0;
                if (netRequest.handler == null) {
                    int i;
                    if (netResponse.headers != null) {
                        obj = netResponse.headers.get(HTTP.CONTENT_LEN);
                        if (obj == null || !TextUtils.isDigitsOnly(obj)) {
                            try {
                                if (HTTP.CHUNK_CODING.equalsIgnoreCase(netResponse.headers.get(HTTP.TRANSFER_ENCODING))) {
                                    i = -1;
                                    if (i > 0 || i >= 409600) {
                                        aVar = new a(4096);
                                    } else {
                                        aVar = new a(i);
                                    }
                                }
                            } catch (Exception e) {
                                netResponse.error = 2;
                                netResponse.e = e;
                                sendMessageInfo(netRequest.handler, netResponse.id, netResponse.error, netResponse.e);
                            }
                        } else {
                            i = Integer.parseInt(obj);
                            if (i > 0) {
                            }
                            aVar = new a(4096);
                        }
                    }
                    i = 0;
                    if (i > 0) {
                    }
                    aVar = new a(4096);
                } else {
                    aVar = null;
                }
                byte[] bArr = new byte[4096];
                while (true) {
                    read = content.read(bArr);
                    if (read == -1) {
                        break;
                    }
                    j += (long) read;
                    if (netRequest.handler == null) {
                        aVar.write(bArr, 0, read);
                    } else {
                        netResponse.result = bArr;
                        netResponse.resultLen = read;
                        sendMessageRead(netRequest.handler, netResponse);
                    }
                }
                netResponse.total = j;
                if (netRequest.handler != null) {
                    netResponse.result = bArr;
                    netResponse.resultLen = read;
                    sendMessageRead(netRequest.handler, netResponse);
                }
                if (netRequest.handler == null) {
                    netResponse.result = aVar.b();
                    netResponse.resultLen = netResponse.result.length;
                    aVar.close();
                }
                if (netResponse.headers != null) {
                    if ("gzip".equalsIgnoreCase(netResponse.headers.get(HTTP.CONTENT_ENCODING))) {
                        netResponse.headers = netResponse.headers.newBuilder().set(HTTP.CONTENT_LEN, String.valueOf(j)).build();
                    }
                }
                content.close();
            }
            return netResponse;
        } catch (Throwable th) {
            netResponse.error = 1;
            netResponse.e = new RuntimeException(th);
            sendMessageInfo(netRequest.handler, netResponse.id, netResponse.error, netResponse.e);
            return netResponse;
        }
    }

    private Headers a(HttpResponse httpResponse) {
        Builder builder = new Builder();
        Header[] allHeaders = httpResponse.getAllHeaders();
        if (allHeaders != null && allHeaders.length > 0) {
            Header header = null;
            int length = allHeaders.length;
            int i = 0;
            Object obj = null;
            while (i < length) {
                Object obj2;
                Header header2 = allHeaders[i];
                String name = header2.getName();
                if (HTTP.CONTENT_LEN.equalsIgnoreCase(name)) {
                    obj2 = obj;
                } else {
                    Object obj3;
                    String value = header2.getValue();
                    if (HTTP.CONTENT_ENCODING.equalsIgnoreCase(name) && "gzip".equalsIgnoreCase(value)) {
                        obj3 = 1;
                    } else {
                        obj3 = obj;
                    }
                    builder.add(name, value);
                    Header header3 = header;
                    obj2 = obj3;
                    header2 = header3;
                }
                i++;
                obj = obj2;
                header = header2;
            }
            if (header != null) {
                if (obj != null) {
                    builder.add(header.getName(), "-1");
                } else if (header != null) {
                    builder.add(header.getName(), header.getValue());
                }
            }
        }
        return builder.build();
    }

    public void sendMessageRead(Callback callback, NetResponse netResponse) {
        Object obj;
        String str = null;
        Message obtain = Message.obtain();
        if (netResponse.resultLen > 0) {
            obj = new byte[netResponse.resultLen];
            System.arraycopy(netResponse.result, 0, obj, 0, netResponse.resultLen);
        } else {
            obj = null;
        }
        obtain.what = 1;
        if (netResponse.headers != null) {
            str = netResponse.headers.get(HTTP.CONTENT_LEN);
        }
        if (str != null && str.length() >= 0) {
            netResponse.total = Long.parseLong(str);
        } else if (netResponse.headers != null && netResponse.headers.get(HTTP.TRANSFER_ENCODING).equalsIgnoreCase(HTTP.CHUNK_CODING) && netResponse.resultLen == -1) {
            netResponse.total = -1;
        }
        obtain.obj = new NetMsgObj(netResponse.id, netResponse.total, netResponse.resultLen, obj);
        callback.handleMessage(obtain);
    }

    public boolean sendMessageWrite(Callback callback, NetRequest netRequest) {
        if (netRequest.handler == null) {
            return false;
        }
        Message obtain = Message.obtain();
        obtain.what = 2;
        obtain.obj = new NetMsgObj(netRequest.id, netRequest.total, 0, null);
        callback.handleMessage(obtain);
        return true;
    }

    public boolean sendMessageInfo(Callback callback, int i, int i2, Exception exception) {
        if (callback == null) {
            return false;
        }
        Message obtain = Message.obtain();
        obtain.what = 3;
        obtain.obj = new NetMsgObj(i, 0, i2, exception);
        callback.handleMessage(obtain);
        return true;
    }
}
