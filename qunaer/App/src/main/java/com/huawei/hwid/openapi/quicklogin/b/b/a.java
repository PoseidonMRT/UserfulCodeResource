package com.huawei.hwid.openapi.quicklogin.b.b;

import com.huawei.hwid.openapi.quicklogin.d.b.d;
import org.apache.http.HttpHost;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.params.ConnManagerPNames;
import org.apache.http.conn.params.ConnPerRoute;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.scheme.SocketFactory;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;

public class a {
    private static ClientConnectionManager a;
    private static final ConnPerRoute b = new b();

    public static ClientConnectionManager a() {
        if (a == null) {
            SocketFactory cVar;
            SchemeRegistry schemeRegistry = new SchemeRegistry();
            try {
                cVar = new c(null);
            } catch (Throwable e) {
                d.b("HttpClientConnectManager", "getConnectionManager Exception KeyManagementException", e);
                cVar = null;
            } catch (Throwable e2) {
                d.b("HttpClientConnectManager", "getConnectionManager Exception NoSuchAlgorithmException", e2);
                cVar = null;
            } catch (Throwable e22) {
                d.b("HttpClientConnectManager", "getConnectionManager Exception KeyStoreException", e22);
                cVar = null;
            } catch (Throwable e222) {
                d.b("HttpClientConnectManager", "getConnectionManager Exception UnrecoverableKeyException", e222);
                cVar = null;
            }
            if (cVar != null) {
                d.a("HttpClientConnectManager", "mysslSocketFactory is not null");
                cVar.setHostnameVerifier(SSLSocketFactory.BROWSER_COMPATIBLE_HOSTNAME_VERIFIER);
                schemeRegistry.register(new Scheme("https", cVar, 443));
            }
            schemeRegistry.register(new Scheme(HttpHost.DEFAULT_SCHEME_NAME, PlainSocketFactory.getSocketFactory(), 80));
            HttpParams basicHttpParams = new BasicHttpParams();
            basicHttpParams.setIntParameter(ConnManagerPNames.MAX_TOTAL_CONNECTIONS, 25);
            basicHttpParams.setParameter(ConnManagerPNames.MAX_CONNECTIONS_PER_ROUTE, b);
            a(new ThreadSafeClientConnManager(basicHttpParams, schemeRegistry));
        } else {
            d.a("HttpClientConnectManager", "sClientConnectionManager is not null");
        }
        return a;
    }

    private static synchronized void a(ClientConnectionManager clientConnectionManager) {
        synchronized (a.class) {
            a = clientConnectionManager;
        }
    }
}
