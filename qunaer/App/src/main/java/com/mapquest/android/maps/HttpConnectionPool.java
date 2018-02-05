package com.mapquest.android.maps;

import com.iflytek.cloud.SpeechConstant;
import com.mqunar.necro.agent.instrumentation.HttpInstrumentation;
import com.mqunar.necro.agent.instrumentation.Instrumented;
import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.concurrent.TimeUnit;
import org.apache.http.HeaderElement;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.ConnectionKeepAliveStrategy;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.conn.params.ConnPerRouteBean;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.scheme.SocketFactory;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.message.BasicHeaderElementIterator;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.protocol.HttpContext;

@Instrumented
final class HttpConnectionPool {
    public static final int CONNECTION_TIMEOUT = 3000;
    public static final int KEEP_ALIVE = 5000;
    public static final int READ_TIMEOUT = 5000;
    DefaultHttpClient httpClient = null;
    ThreadSafeClientConnManager manager = null;
    private IdleConnectionMonitorThread monitor = null;

    class IdleConnectionMonitorThread extends Thread {
        private final ClientConnectionManager connMgr;
        private volatile boolean shutdown;

        public IdleConnectionMonitorThread(ClientConnectionManager clientConnectionManager) {
            this.connMgr = clientConnectionManager;
        }

        public void run() {
            while (!this.shutdown) {
                try {
                    synchronized (this) {
                        wait(3000);
                        if (!this.shutdown) {
                            this.connMgr.closeExpiredConnections();
                            this.connMgr.closeIdleConnections(5000, TimeUnit.MILLISECONDS);
                        }
                    }
                } catch (InterruptedException e) {
                    return;
                }
            }
        }

        public void shutdown() {
            this.shutdown = true;
            synchronized (this) {
                notifyAll();
            }
        }
    }

    public HttpConnectionPool(int i, int i2, int i3, int i4) {
        HttpParams basicHttpParams = new BasicHttpParams();
        HttpConnectionParams.setConnectionTimeout(basicHttpParams, i3);
        HttpConnectionParams.setSoTimeout(basicHttpParams, i4);
        HttpConnectionParams.setStaleCheckingEnabled(basicHttpParams, false);
        HttpConnectionParams.setSocketBufferSize(basicHttpParams, 8192);
        HttpProtocolParams.setVersion(basicHttpParams, HttpVersion.HTTP_1_1);
        HttpProtocolParams.setContentCharset(basicHttpParams, "utf-8");
        HttpProtocolParams.setUseExpectContinue(basicHttpParams, false);
        SchemeRegistry schemeRegistry = new SchemeRegistry();
        schemeRegistry.register(new Scheme(HttpHost.DEFAULT_SCHEME_NAME, PlainSocketFactory.getSocketFactory(), 80));
        SocketFactory socketFactory = SSLSocketFactory.getSocketFactory();
        socketFactory.setHostnameVerifier(SSLSocketFactory.BROWSER_COMPATIBLE_HOSTNAME_VERIFIER);
        schemeRegistry.register(new Scheme("https", socketFactory, 443));
        ConnManagerParams.setMaxConnectionsPerRoute(basicHttpParams, new ConnPerRouteBean(i2));
        ConnManagerParams.setMaxTotalConnections(basicHttpParams, i);
        ConnManagerParams.setTimeout(basicHttpParams, 3000);
        this.manager = new ThreadSafeClientConnManager(basicHttpParams, schemeRegistry);
        this.httpClient = new DefaultHttpClient(this.manager, basicHttpParams);
        DefaultHttpClient defaultHttpClient = new DefaultHttpClient();
        this.monitor = new IdleConnectionMonitorThread(this.manager);
        this.monitor.start();
        defaultHttpClient.setKeepAliveStrategy(new ConnectionKeepAliveStrategy() {
            public long getKeepAliveDuration(HttpResponse httpResponse, HttpContext httpContext) {
                BasicHeaderElementIterator basicHeaderElementIterator = new BasicHeaderElementIterator(httpResponse.headerIterator(HTTP.CONN_KEEP_ALIVE));
                while (basicHeaderElementIterator.hasNext()) {
                    HeaderElement nextElement = basicHeaderElementIterator.nextElement();
                    String name = nextElement.getName();
                    String value = nextElement.getValue();
                    if (value != null && name.equalsIgnoreCase(SpeechConstant.NET_TIMEOUT)) {
                        try {
                            return Long.parseLong(value) * 1000;
                        } catch (NumberFormatException e) {
                        }
                    }
                }
                return 5000;
            }
        });
    }

    public byte[] get(String str) {
        HttpGet httpGet = new HttpGet(str);
        httpGet.addHeader(HTTP.CONN_DIRECTIVE, "keep-alive");
        try {
            HttpResponse execute;
            DefaultHttpClient defaultHttpClient = this.httpClient;
            if (defaultHttpClient instanceof HttpClient) {
                execute = HttpInstrumentation.execute(defaultHttpClient, httpGet);
            } else {
                execute = defaultHttpClient.execute(httpGet);
            }
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            BufferedInputStream bufferedInputStream = new BufferedInputStream(execute.getEntity().getContent(), 8192);
            byte[] bArr = new byte[8192];
            while (true) {
                int read = bufferedInputStream.read(bArr);
                if (read != -1) {
                    byteArrayOutputStream.write(bArr, 0, read);
                } else {
                    byteArrayOutputStream.flush();
                    bufferedInputStream.close();
                    byteArrayOutputStream.close();
                    return byteArrayOutputStream.toByteArray();
                }
            }
        } catch (ClientProtocolException e) {
            httpGet.abort();
            return null;
        } catch (IOException e2) {
            httpGet.abort();
            return null;
        }
    }

    public void shutdown() {
        if (this.httpClient != null) {
            this.monitor.shutdown();
            this.manager.shutdown();
            this.monitor = null;
            this.manager = null;
            this.httpClient = null;
        }
    }
}
