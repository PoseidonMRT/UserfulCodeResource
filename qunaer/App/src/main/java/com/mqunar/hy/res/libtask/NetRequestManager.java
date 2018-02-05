package com.mqunar.hy.res.libtask;

import android.content.Context;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.Proxy;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler.Callback;
import android.os.Message;
import android.text.TextUtils;
import com.iflytek.aiui.AIUIConstant;
import com.mqunar.hy.res.logger.Timber;
import com.mqunar.libtask.ProgressType;
import com.mqunar.necro.agent.instrumentation.HttpInstrumentation;
import com.mqunar.necro.agent.instrumentation.Instrumented;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Pattern;
import okhttp3.internal.http.StatusLine;
import org.apache.commons.io.IOUtils;
import org.apache.http.Header;
import org.apache.http.HeaderElement;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.HttpResponseInterceptor;
import org.apache.http.HttpStatus;
import org.apache.http.HttpVersion;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.conn.ClientConnectionOperator;
import org.apache.http.conn.OperatedClientConnection;
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
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.InputStreamEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.DefaultHttpRequestRetryHandler;
import org.apache.http.impl.conn.DefaultClientConnectionOperator;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.protocol.HttpContext;

@Instrumented
class NetRequestManager {
    static final int BUFFER_SIZE = 4096;
    private static final String CTWAP_APN_NAME_1 = "#777";
    private static final String CTWAP_APN_NAME_2 = "ctwap";
    private static final Uri PREFERRED_APN_URI = Uri.parse("content://telephony/carriers/preferapn");
    private static final boolean PRINTLOG = false;
    private static NetRequestManager instance;
    private DefaultHttpClient cacheClient;
    private String cacheHost;
    private int cachePort;
    private int connTimeout = 80000;
    private Map<String, String> currentDnsMap = new HashMap();
    private Map<String, List<String>> dnsMap = new HashMap();
    private int readTimeout = 80000;

    class QThreadSafeClientConnManager extends ThreadSafeClientConnManager {
        QThreadSafeClientConnManager(HttpParams httpParams, SchemeRegistry schemeRegistry) {
            super(httpParams, schemeRegistry);
        }

        protected ClientConnectionOperator createConnectionOperator(SchemeRegistry schemeRegistry) {
            return new DefaultClientConnectionOperator(schemeRegistry) {
                public void openConnection(OperatedClientConnection operatedClientConnection, HttpHost httpHost, InetAddress inetAddress, HttpContext httpContext, HttpParams httpParams) {
                    String hostName = httpHost.getHostName();
                    try {
                        super.openConnection(operatedClientConnection, new HttpHost(NetRequestManager.this.nameToIp(hostName), httpHost.getPort(), httpHost.getSchemeName()), inetAddress, httpContext, httpParams);
                    } catch (IOException e) {
                        NetRequestManager.this.onSocketError(hostName);
                        throw e;
                    }
                }

                public void updateSecureConnection(OperatedClientConnection operatedClientConnection, HttpHost httpHost, HttpContext httpContext, HttpParams httpParams) {
                    String hostName = httpHost.getHostName();
                    try {
                        super.updateSecureConnection(operatedClientConnection, new HttpHost(NetRequestManager.this.nameToIp(hostName), httpHost.getPort(), httpHost.getSchemeName()), httpContext, httpParams);
                    } catch (IOException e) {
                        NetRequestManager.this.onSocketError(hostName);
                        throw e;
                    }
                }

                protected void prepareSocket(Socket socket, HttpContext httpContext, HttpParams httpParams) {
                    super.prepareSocket(socket, httpContext, httpParams);
                }
            };
        }

        public void shutdown() {
        }

        public void superShutdown() {
            super.shutdown();
        }
    }

    private String nameToIp(String str) {
        if (!isboolIp(str)) {
            synchronized (this.dnsMap) {
                String str2 = (String) this.currentDnsMap.get(str);
                if (TextUtils.isEmpty(str2)) {
                    List list = (List) this.dnsMap.get(str);
                    if (list == null || list.isEmpty()) {
                        Object substring;
                        if (str.endsWith(".")) {
                            substring = str.substring(0, str.length() - 1);
                        } else {
                            substring = str + ".";
                        }
                        list = (List) this.dnsMap.get(substring);
                    }
                    if (!(list == null || list.isEmpty())) {
                        str2 = (String) list.get((int) (Math.random() * ((double) list.size())));
                        if (!TextUtils.isEmpty(str2)) {
                            this.currentDnsMap.put(str, str2);
                            return str2;
                        }
                    }
                }
                return str2;
            }
        }
        return str;
    }

    private void onSocketError(String str) {
        if (!TextUtils.isEmpty(str) && !isboolIp(str)) {
            synchronized (this.dnsMap) {
                String str2 = (String) this.currentDnsMap.remove(str);
                for (String str3 : this.dnsMap.keySet()) {
                    ((List) this.dnsMap.get(str3)).remove(str2);
                }
            }
        }
    }

    private NetRequestManager() {
    }

    private static int getProxyPort(boolean z, Context context) {
        if (!z) {
            try {
                return Proxy.getPort(context);
            } catch (Throwable th) {
            }
        }
        return Proxy.getDefaultPort();
    }

    private static String getProxyHost(boolean z, Context context) {
        if (!z) {
            try {
                return Proxy.getHost(context);
            } catch (Throwable th) {
            }
        }
        return Proxy.getDefaultHost();
    }

    public static NetRequestManager getInstance() {
        if (instance == null) {
            synchronized (NetRequestManager.class) {
                if (instance == null) {
                    instance = new NetRequestManager();
                }
            }
        }
        return instance;
    }

    private static String getApnName(Context context) {
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
        return setupProxy(context, null);
    }

    private DefaultHttpClient setupProxy(Context context, String str) {
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
            if ("#777".equalsIgnoreCase(apnName) || "ctwap".equalsIgnoreCase(apnName)) {
                proxyHost = null;
            }
        }
        DefaultHttpClient httpClient = getHttpClient(proxyHost, proxyPort);
        httpClient.addResponseInterceptor(new HttpResponseInterceptor() {
            public void process(HttpResponse httpResponse, HttpContext httpContext) {
                Header contentEncoding = httpResponse.getEntity().getContentEncoding();
                if (contentEncoding != null) {
                    for (HeaderElement name : contentEncoding.getElements()) {
                        if (name.getName().equalsIgnoreCase("gzip")) {
                            httpResponse.setEntity(new GzipDecompressingEntity(httpResponse.getEntity()));
                            return;
                        }
                    }
                }
            }
        });
        httpClient.setHttpRequestRetryHandler(new DefaultHttpRequestRetryHandler(6, true));
        return httpClient;
    }

    private boolean isboolIp(String str) {
        return Pattern.compile("([1-9]|[1-9]//d|1//d{2}|2[0-4]//d|25[0-5])(//.(//d|[1-9]//d|1//d{2}|2[0-4]//d|25[0-5])){3}").matcher(str).matches();
    }

    public DefaultHttpClient getHttpClient(String str, int i) {
        if (this.cacheClient != null && equalsString(this.cacheHost, str) && this.cachePort == i) {
            return this.cacheClient;
        }
        this.cacheHost = str;
        this.cachePort = i;
        DefaultHttpClient cacheHttpClient = getCacheHttpClient(str, i);
        this.cacheClient = cacheHttpClient;
        return cacheHttpClient;
    }

    private boolean equalsString(String str, String str2) {
        if (str == null) {
            return str2 == null;
        } else {
            return str.equals(str2);
        }
    }

    private DefaultHttpClient getCacheHttpClient(String str, int i) {
        SchemeRegistry schemeRegistry = new SchemeRegistry();
        schemeRegistry.register(new Scheme(HttpHost.DEFAULT_SCHEME_NAME, PlainSocketFactory.getSocketFactory(), 80));
        try {
            SocketFactory qSSLSocketFactory = new QSSLSocketFactory(null);
            qSSLSocketFactory.setHostnameVerifier(SSLSocketFactory.STRICT_HOSTNAME_VERIFIER);
            schemeRegistry.register(new Scheme("https", qSSLSocketFactory, 443));
        } catch (Throwable e) {
            Timber.e(e, new Object[0]);
        }
        HttpParams basicHttpParams = new BasicHttpParams();
        HttpProtocolParams.setVersion(basicHttpParams, HttpVersion.HTTP_1_1);
        HttpProtocolParams.setContentCharset(basicHttpParams, "UTF-8");
        HttpConnectionParams.setConnectionTimeout(basicHttpParams, this.connTimeout);
        HttpConnectionParams.setSoTimeout(basicHttpParams, this.readTimeout);
        if (str != null && str.trim().length() > 0) {
            ConnRouteParams.setDefaultProxy(basicHttpParams, new HttpHost(str, i));
        }
        ConnManagerParams.setMaxTotalConnections(basicHttpParams, ProgressType.PRO_END);
        basicHttpParams.setIntParameter(ConnManagerPNames.MAX_TOTAL_CONNECTIONS, ProgressType.PRO_END);
        ConnPerRoute connPerRouteBean = new ConnPerRouteBean(20);
        ConnManagerParams.setMaxConnectionsPerRoute(basicHttpParams, connPerRouteBean);
        basicHttpParams.setParameter(ConnManagerPNames.MAX_CONNECTIONS_PER_ROUTE, connPerRouteBean);
        return new DefaultHttpClient(new QThreadSafeClientConnManager(basicHttpParams, schemeRegistry), basicHttpParams);
    }

    private static boolean isRedirect(int i) {
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

    NetResponse request(NetRequest netRequest, Context context) {
        NetResponse netResponse = new NetResponse();
        if (netRequest == null || netRequest.url == null || netRequest.url.length() == 0) {
            netResponse.error = 3;
            if (netRequest.handler != null) {
                sendMessageInfo(netRequest.handler, netResponse.id, netResponse.error, netResponse.e);
            }
            return netResponse;
        }
        netResponse.id = netRequest.id;
        DefaultHttpClient defaultHttpClient = setupProxy(context, netRequest.url);
        try {
            final OutputStreamWarp outputStreamWarp;
            HttpUriRequest httpUriRequest;
            HttpResponse execute;
            if (netRequest.content != null) {
                HttpPost httpPost = new HttpPost(netRequest.url);
                outputStreamWarp = new OutputStreamWarp(netRequest);
                httpPost.setEntity(new ByteArrayEntity(netRequest.content) {
                    public void writeTo(OutputStream outputStream) {
                        outputStreamWarp.setOutputStream(outputStream);
                        super.writeTo(outputStreamWarp);
                    }
                });
                httpUriRequest = httpPost;
            } else if (netRequest.stream != null) {
                HttpPost httpPost2 = new HttpPost(netRequest.url);
                outputStreamWarp = new OutputStreamWarp(netRequest);
                httpPost2.setEntity(new InputStreamEntity(netRequest.stream, -1) {
                    public void writeTo(OutputStream outputStream) {
                        outputStreamWarp.setOutputStream(outputStream);
                        super.writeTo(outputStreamWarp);
                    }
                });
                r3 = httpPost2;
            } else {
                r3 = new HttpGet(netRequest.url);
                outputStreamWarp = null;
            }
            if (netRequest.hasHeader()) {
                for (Entry entry : netRequest.header.entrySet()) {
                    httpUriRequest.addHeader((String) entry.getKey(), (String) entry.getValue());
                }
            }
            netRequest.httpRequest = httpUriRequest;
            defaultHttpClient.getCookieStore().clear();
            if (defaultHttpClient instanceof HttpClient) {
                execute = HttpInstrumentation.execute(defaultHttpClient, httpUriRequest);
            } else {
                execute = defaultHttpClient.execute(httpUriRequest);
            }
            if (outputStreamWarp != null) {
                outputStreamWarp.checkNotify();
            }
            if (execute != null) {
                netResponse.code = execute.getStatusLine().getStatusCode();
                netResponse.redirect = isRedirect(netResponse.code);
                netResponse.headers = mergeHeaders(execute);
                try {
                    ByteArrayOutputStream byteArrayOutputStream;
                    int read;
                    InputStream content = execute.getEntity().getContent();
                    long j = 0;
                    if (netRequest.handler == null) {
                        int parseInt;
                        if (netResponse.headers != null) {
                            String str = (String) netResponse.headers.get(HTTP.CONTENT_LEN);
                            if (str != null && TextUtils.isDigitsOnly(str)) {
                                parseInt = Integer.parseInt(str);
                                if (parseInt > 0) {
                                }
                                byteArrayOutputStream = new ByteArrayOutputStream(4096);
                            } else if (HTTP.CHUNK_CODING.equalsIgnoreCase((String) netResponse.headers.get(HTTP.TRANSFER_ENCODING))) {
                                parseInt = -1;
                                if (parseInt > 0 || parseInt >= 409600) {
                                    byteArrayOutputStream = new ByteArrayOutputStream(4096);
                                } else {
                                    byteArrayOutputStream = new ByteArrayOutputStream(parseInt);
                                }
                            }
                        }
                        parseInt = 0;
                        if (parseInt > 0) {
                        }
                        byteArrayOutputStream = new ByteArrayOutputStream(4096);
                    } else {
                        byteArrayOutputStream = null;
                    }
                    byte[] bArr = new byte[4096];
                    while (true) {
                        read = content.read(bArr);
                        if (read == -1) {
                            break;
                        }
                        long j2 = j + ((long) read);
                        if (netRequest.handler == null) {
                            byteArrayOutputStream.write(bArr, 0, read);
                            j = j2;
                        } else {
                            netResponse.result = bArr;
                            netResponse.resultLen = read;
                            sendMessageRead(netRequest.handler, netResponse);
                            j = j2;
                        }
                    }
                    netResponse.total = j;
                    if (netRequest.handler != null) {
                        netResponse.result = bArr;
                        netResponse.resultLen = read;
                        sendMessageRead(netRequest.handler, netResponse);
                    }
                    if (netRequest.handler == null) {
                        netResponse.result = byteArrayOutputStream.rawByteArray();
                        netResponse.resultLen = netResponse.result.length;
                        byteArrayOutputStream.close();
                    }
                    if (netResponse.headers != null) {
                        if ("gzip".equalsIgnoreCase((String) netResponse.headers.get(HTTP.CONTENT_ENCODING))) {
                            netResponse.headers.remove(HTTP.CONTENT_LEN);
                            netResponse.headers.put(HTTP.CONTENT_LEN, String.valueOf(j));
                        }
                    }
                    content.close();
                } catch (Exception e) {
                    netResponse.error = 2;
                    netResponse.e = e;
                    sendMessageInfo(netRequest.handler, netResponse.id, netResponse.error, netResponse.e);
                }
            }
            return netResponse;
        } catch (Throwable th) {
            netResponse.error = 1;
            netResponse.e = new RuntimeException(th);
            sendMessageInfo(netRequest.handler, netResponse.id, netResponse.error, netResponse.e);
            return netResponse;
        }
    }

    private static void saveToFile(long j, String str) {
        FileOutputStream fileOutputStream;
        Throwable th;
        synchronized ("22") {
            FileOutputStream fileOutputStream2 = null;
            try {
                File externalStorageDirectory;
                String str2;
                if (Environment.getExternalStorageState().equals("mounted")) {
                    externalStorageDirectory = Environment.getExternalStorageDirectory();
                } else {
                    externalStorageDirectory = new File("/data/data/com.Qunar/files");
                }
                if (!externalStorageDirectory.exists()) {
                    externalStorageDirectory.mkdirs();
                }
                File file = new File(externalStorageDirectory, "qunar_log.txt");
                String str3 = str + j + ":\n";
                if (file.exists()) {
                    str2 = str3;
                } else {
                    str2 = file.getAbsolutePath() + IOUtils.LINE_SEPARATOR_UNIX + str3;
                }
                fileOutputStream = new FileOutputStream(file, true);
                try {
                    fileOutputStream.write(str2.getBytes());
                    fileOutputStream.write("\n\n".getBytes());
                    fileOutputStream.flush();
                    if (fileOutputStream != null) {
                        try {
                            fileOutputStream.close();
                        } catch (IOException e) {
                        }
                    }
                } catch (Throwable th2) {
                    th = th2;
                    if (fileOutputStream != null) {
                        try {
                            fileOutputStream.close();
                        } catch (IOException e2) {
                        }
                    }
                    throw th;
                }
            } catch (Throwable th3) {
                Throwable th4 = th3;
                fileOutputStream = null;
                th = th4;
                if (fileOutputStream != null) {
                    fileOutputStream.close();
                }
                throw th;
            }
        }
    }

    private static String bytesToHexString(byte[] bArr) {
        StringBuilder stringBuilder = new StringBuilder("");
        if (bArr == null || bArr.length <= 0) {
            return null;
        }
        for (byte b : bArr) {
            String toHexString = Integer.toHexString(b & 255);
            if (toHexString.length() < 2) {
                stringBuilder.append(0);
            }
            stringBuilder.append(toHexString);
        }
        return stringBuilder.toString();
    }

    private Map<String, String> mergeHeaders(HttpResponse httpResponse) {
        Map<String, String> hashMap = new HashMap();
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
                    hashMap.put(name, value);
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
                    hashMap.put(header.getName(), "-1");
                } else {
                    hashMap.put(header.getName(), header.getValue());
                }
            }
        }
        return hashMap;
    }

    private void sendMessageRead(Callback callback, NetResponse netResponse) {
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
            str = (String) netResponse.headers.get(HTTP.CONTENT_LEN);
        }
        if (str != null && str.length() >= 0) {
            netResponse.total = Long.parseLong(str);
        } else if (netResponse.headers != null && ((String) netResponse.headers.get(HTTP.TRANSFER_ENCODING)).equalsIgnoreCase(HTTP.CHUNK_CODING) && netResponse.resultLen == -1) {
            netResponse.total = -1;
        }
        obtain.obj = new NetMsgObj(netResponse.id, netResponse.total, netResponse.resultLen, obj);
        callback.handleMessage(obtain);
    }

    boolean sendMessageWrite(Callback callback, NetRequest netRequest) {
        if (netRequest.handler == null) {
            return false;
        }
        Message obtain = Message.obtain();
        obtain.what = 2;
        obtain.obj = new NetMsgObj(netRequest.id, netRequest.total, 0, null);
        callback.handleMessage(obtain);
        return true;
    }

    private boolean sendMessageInfo(Callback callback, int i, int i2, Exception exception) {
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
