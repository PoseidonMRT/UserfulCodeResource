package qunar.lego.utils;

import android.content.Context;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.util.Pair;
import com.iflytek.aiui.AIUIConstant;
import com.mqunar.necro.agent.instrumentation.HttpInstrumentation;
import com.mqunar.necro.agent.instrumentation.Instrumented;
import com.mqunar.network.NetRequestManager;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.Proxy;
import java.net.Proxy.Type;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.AbstractMap.SimpleEntry;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;

@Instrumented
public class Pitcher {
    private static String DEFAULT_PROXY_URL;
    private static boolean IS_URL_LOCK = false;
    private static final Uri PREFERRED_APN_URI = Uri.parse("content://telephony/carriers/preferapn");
    private HttpURLConnection conn;
    private final byte[] content;
    private final Context context;
    private String proxyUrl = DEFAULT_PROXY_URL;
    private final HttpHeader reqHeader;
    private final String url;

    public Pitcher(Context context, String str, List<FormPart> list, HttpHeader httpHeader) {
        this.context = context;
        this.url = str;
        Pair protocol = getProtocol(str, (List) list, httpHeader);
        this.content = (byte[]) protocol.second;
        this.reqHeader = (HttpHeader) protocol.first;
    }

    public Pitcher(Context context, String str, byte[] bArr, HttpHeader httpHeader) {
        this.context = context;
        this.url = str;
        Pair protocol = getProtocol(str, bArr, httpHeader);
        this.content = (byte[]) protocol.second;
        this.reqHeader = (HttpHeader) protocol.first;
    }

    public String getProxyUrl() {
        return this.proxyUrl;
    }

    public byte[] getContent() {
        return this.content;
    }

    public String getUrl() {
        return this.url;
    }

    public HttpHeader getReqHeader() {
        return this.reqHeader;
    }

    public PitcherResponse request() {
        PitcherResponse pitcherResponse = new PitcherResponse();
        this.conn = null;
        OutputStream outputStream;
        try {
            if (this.context == null || isEmpty(this.url)) {
                throw new IllegalArgumentException("context or url illegal");
            }
            Proxy proxy = setupProxy(this.context, this.url);
            if (isEmpty(this.proxyUrl)) {
                throw new MalformedURLException("pitcher url must not be empty");
            }
            if (proxy != null) {
                this.conn = (HttpURLConnection) HttpInstrumentation.openConnectionWithProxy(new URL(this.proxyUrl).openConnection(proxy));
            } else {
                this.conn = (HttpURLConnection) HttpInstrumentation.openConnection(new URL(this.proxyUrl).openConnection());
            }
            this.conn.setConnectTimeout(80000);
            this.conn.setReadTimeout(80000);
            this.conn.setRequestMethod(HttpPost.METHOD_NAME);
            this.conn.setUseCaches(false);
            Iterator it = this.reqHeader.iterator();
            while (it.hasNext()) {
                SimpleEntry simpleEntry = (SimpleEntry) it.next();
                this.conn.addRequestProperty((String) simpleEntry.getKey(), (String) simpleEntry.getValue());
            }
            this.conn.setDoOutput(true);
            this.conn.setDoInput(true);
            outputStream = this.conn.getOutputStream();
            outputStream.write(this.content);
            outputStream.flush();
            try {
                outputStream.close();
            } catch (IOException e) {
            }
            pitcherResponse.respcode = this.conn.getResponseCode();
            pitcherResponse.respHeader = new HttpHeader(this.conn.getHeaderFields());
            if (pitcherResponse.respcode < HttpStatus.SC_BAD_REQUEST) {
                InputStream inputStream = this.conn.getInputStream();
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                byte[] bArr = new byte[4096];
                while (true) {
                    int read = inputStream.read(bArr);
                    if (read == -1) {
                        break;
                    }
                    byteArrayOutputStream.write(bArr, 0, read);
                }
                byteArrayOutputStream.flush();
                pitcherResponse.content = buildResult(byteArrayOutputStream.toByteArray());
            }
            if (this.conn != null) {
                this.conn.disconnect();
            }
            return pitcherResponse;
        } catch (Exception e2) {
            pitcherResponse.e = e2;
            if (this.conn != null) {
                this.conn.disconnect();
            }
        } catch (Throwable th) {
            if (this.conn != null) {
                this.conn.disconnect();
            }
        }
    }

    public void cancel() {
        if (this.conn != null) {
            this.conn.disconnect();
        }
    }

    public Pitcher setProxyUrl(String str) {
        this.proxyUrl = str;
        return this;
    }

    private static Pair<HttpHeader, byte[]> getProtocol(String str, byte[] bArr, HttpHeader httpHeader) {
        if (bArr == null) {
            bArr = new byte[0];
        }
        HttpHeader httpHeader2 = new HttpHeader();
        if (!httpHeader2.hasHeader("Content-Type")) {
            httpHeader2.setHeader("Content-Type", URLEncodedUtils.CONTENT_TYPE);
        }
        Object buildRequest = buildRequest(bArr);
        httpHeader2.setHeader("Pitcher-Url", str);
        if (httpHeader != null) {
            httpHeader2.addHeaders(httpHeader);
        }
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS Z z");
        simpleDateFormat.setLenient(false);
        httpHeader2.setHeader("L-Date", simpleDateFormat.format(new Date()));
        return new Pair(httpHeader2, buildRequest);
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static android.util.Pair<qunar.lego.utils.HttpHeader, byte[]> getProtocol(java.lang.String r5, java.util.List<qunar.lego.utils.FormPart> r6, qunar.lego.utils.HttpHeader r7) {
        /*
        r4 = 0;
        r1 = new qunar.lego.utils.MultipartEntity;
        r1.<init>(r6);
        r2 = new java.io.ByteArrayOutputStream;
        r2.<init>();
        r1.writeTo(r2);	 Catch:{ IOException -> 0x0051 }
        r0 = r2.toByteArray();	 Catch:{ IOException -> 0x0051 }
        r2.close();	 Catch:{ IOException -> 0x0060 }
    L_0x0015:
        r1 = r1.getContentType();
        r2 = new qunar.lego.utils.HttpHeader;
        r2.<init>();
        if (r1 == 0) goto L_0x0025;
    L_0x0020:
        r3 = "Content-Type";
        r2.setHeader(r3, r1);
    L_0x0025:
        r0 = buildRequest(r0);
        r1 = "Pitcher-Url";
        r2.setHeader(r1, r5);
        if (r7 == 0) goto L_0x0033;
    L_0x0030:
        r2.addHeaders(r7);
    L_0x0033:
        r1 = new java.text.SimpleDateFormat;
        r3 = "yyyy-MM-dd HH:mm:ss.SSS Z z";
        r1.<init>(r3);
        r1.setLenient(r4);
        r3 = new java.util.Date;
        r3.<init>();
        r1 = r1.format(r3);
        r3 = "L-Date";
        r2.setHeader(r3, r1);
        r1 = new android.util.Pair;
        r1.<init>(r2, r0);
        return r1;
    L_0x0051:
        r0 = move-exception;
        r0 = 0;
        r0 = new byte[r0];	 Catch:{ all -> 0x005b }
        r2.close();	 Catch:{ IOException -> 0x0059 }
        goto L_0x0015;
    L_0x0059:
        r2 = move-exception;
        goto L_0x0015;
    L_0x005b:
        r0 = move-exception;
        r2.close();	 Catch:{ IOException -> 0x0062 }
    L_0x005f:
        throw r0;
    L_0x0060:
        r2 = move-exception;
        goto L_0x0015;
    L_0x0062:
        r1 = move-exception;
        goto L_0x005f;
        */
        throw new UnsupportedOperationException("Method not decompiled: qunar.lego.utils.Pitcher.getProtocol(java.lang.String, java.util.List, qunar.lego.utils.HttpHeader):android.util.Pair<qunar.lego.utils.HttpHeader, byte[]>");
    }

    private static boolean isEmpty(Object obj) {
        if (obj == null) {
            return true;
        }
        if (obj instanceof Collection) {
            return ((Collection) obj).isEmpty();
        }
        if (obj instanceof Map) {
            return ((Map) obj).isEmpty();
        }
        if (!(obj instanceof CharSequence)) {
            if (obj.getClass().isArray()) {
                if (obj instanceof Object[]) {
                    if (((Object[]) obj).length != 0) {
                        return false;
                    }
                    return true;
                } else if (obj instanceof int[]) {
                    if (((int[]) obj).length != 0) {
                        return false;
                    }
                    return true;
                } else if (obj instanceof long[]) {
                    if (((long[]) obj).length != 0) {
                        return false;
                    }
                    return true;
                } else if (obj instanceof short[]) {
                    if (((short[]) obj).length != 0) {
                        return false;
                    }
                    return true;
                } else if (obj instanceof double[]) {
                    if (((double[]) obj).length != 0) {
                        return false;
                    }
                    return true;
                } else if (obj instanceof float[]) {
                    if (((float[]) obj).length != 0) {
                        return false;
                    }
                    return true;
                } else if (obj instanceof boolean[]) {
                    if (((boolean[]) obj).length != 0) {
                        return false;
                    }
                    return true;
                } else if (obj instanceof char[]) {
                    if (((char[]) obj).length != 0) {
                        return false;
                    }
                    return true;
                } else if (obj instanceof byte[]) {
                    if (((byte[]) obj).length != 0) {
                        return false;
                    }
                    return true;
                }
            }
            return false;
        } else if (((CharSequence) obj).length() != 0) {
            return false;
        } else {
            return true;
        }
    }

    private static Proxy setupProxy(Context context, String str) {
        NetworkInfo activeNetworkInfo;
        boolean z;
        int proxyPort;
        String str2;
        try {
            activeNetworkInfo = ((ConnectivityManager) context.getSystemService("connectivity")).getActiveNetworkInfo();
        } catch (Exception e) {
            activeNetworkInfo = null;
        }
        if (activeNetworkInfo == null || activeNetworkInfo.getType() != 1) {
            z = false;
        } else {
            z = true;
        }
        String proxyHost;
        if (z) {
            proxyHost = getProxyHost(false, context);
            proxyPort = getProxyPort(false, context);
            str2 = proxyHost;
        } else {
            proxyHost = getProxyHost(true, context);
            proxyPort = getProxyPort(true, context);
            str2 = proxyHost;
        }
        if (str.toLowerCase().startsWith("https://")) {
            String str3;
            String str4 = "";
            try {
                Cursor query = context.getContentResolver().query(PREFERRED_APN_URI, new String[]{"_id", "apn", "type"}, null, null, null);
                if (query != null) {
                    query.moveToFirst();
                    if (query.getCount() == 0 || query.isAfterLast()) {
                        str3 = str4;
                    } else {
                        str3 = query.getString(query.getColumnIndex("apn"));
                    }
                    query.close();
                } else {
                    query = context.getContentResolver().query(PREFERRED_APN_URI, null, null, null, null);
                    if (query != null) {
                        query.moveToFirst();
                        str3 = query.getString(query.getColumnIndex(AIUIConstant.USER));
                        query.close();
                    } else {
                        str3 = str4;
                    }
                }
            } catch (Exception e2) {
                try {
                    str3 = ((ConnectivityManager) context.getSystemService("connectivity")).getActiveNetworkInfo().getExtraInfo();
                } catch (Exception e3) {
                    str3 = "";
                }
            }
            if (NetRequestManager.CTWAP_APN_NAME_1.equalsIgnoreCase(str3) || NetRequestManager.CTWAP_APN_NAME_2.equalsIgnoreCase(str3)) {
                str2 = null;
            }
        }
        if (str2 == null || proxyPort == -1) {
            return null;
        }
        return new Proxy(Type.HTTP, new InetSocketAddress(str2, proxyPort));
    }

    private static int getProxyPort(boolean z, Context context) {
        if (!z) {
            try {
                return android.net.Proxy.getPort(context);
            } catch (Throwable th) {
            }
        }
        return android.net.Proxy.getDefaultPort();
    }

    private static String getProxyHost(boolean z, Context context) {
        if (!z) {
            try {
                return android.net.Proxy.getHost(context);
            } catch (Throwable th) {
            }
        }
        return android.net.Proxy.getDefaultHost();
    }

    public static void setDefaultProxyUrl(String str) {
        if (!IS_URL_LOCK) {
            DEFAULT_PROXY_URL = str;
        }
    }

    public static void lockUrl() {
        IS_URL_LOCK = true;
    }

    private static byte[] buildRequest(byte[] bArr) {
        if (bArr == null) {
            bArr = new byte[0];
        }
        return Goblin.sand(bArr);
    }

    public static byte[] buildResult(byte[] bArr) {
        return isEmpty(bArr) ? new byte[0] : Goblin.drift(bArr);
    }
}
