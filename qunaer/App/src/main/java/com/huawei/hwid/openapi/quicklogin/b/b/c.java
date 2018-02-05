package com.huawei.hwid.openapi.quicklogin.b.b;

import android.os.Build.VERSION;
import com.huawei.hwid.openapi.quicklogin.d.b.d;
import java.net.Socket;
import java.security.KeyStore;
import java.util.ArrayList;
import java.util.List;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocket;
import org.apache.http.conn.ssl.SSLSocketFactory;

class c extends SSLSocketFactory {
    private static Object b = new Object();
    SSLContext a = SSLContext.getInstance("TLS");

    public c(KeyStore keyStore) {
        super(keyStore);
        this.a.init(null, null, null);
    }

    public Socket createSocket(Socket socket, String str, int i, boolean z) {
        Socket createSocket = this.a.getSocketFactory().createSocket(socket, str, i, z);
        if (createSocket instanceof SSLSocket) {
            createSocket = (SSLSocket) createSocket;
        } else {
            createSocket = null;
        }
        d.b("MySSLSocketFactory", "host =" + str + "port=" + i + "autoclouse=" + z);
        a(createSocket);
        if (str.contains("hicloud.com")) {
            if (VERSION.SDK_INT < 22) {
                synchronized (b) {
                    getHostnameVerifier().verify(str, createSocket);
                }
            } else {
                getHostnameVerifier().verify(str, createSocket);
            }
        }
        return createSocket;
    }

    public Socket createSocket() {
        Socket createSocket = this.a.getSocketFactory().createSocket();
        if (createSocket instanceof SSLSocket) {
            createSocket = (SSLSocket) createSocket;
        } else {
            createSocket = null;
        }
        a(createSocket);
        return createSocket;
    }

    public void a(SSLSocket sSLSocket) {
        if (sSLSocket == null) {
            d.d("MySSLSocketFactory", "socket error");
            return;
        }
        d.b("MySSLSocketFactory", "enter setEnableSafeCipherSuites");
        String[] enabledCipherSuites = sSLSocket.getEnabledCipherSuites();
        d.b("MySSLSocketFactory", " current EnabledCipherSuites size" + enabledCipherSuites.length);
        List arrayList = new ArrayList();
        for (String str : enabledCipherSuites) {
            if (!(str.contains("RC4") || str.contains("DES") || str.contains("3DES") || str.contains("aNULL") || str.contains("eNULL"))) {
                arrayList.add(str);
            }
        }
        d.b("MySSLSocketFactory", "get safe EnabledCipherSuites list size =" + arrayList.size());
        String[] strArr = (String[]) arrayList.toArray(new String[arrayList.size()]);
        d.b("MySSLSocketFactory", "get safe EnabledCipherSuites Array length =" + strArr.length);
        sSLSocket.setEnabledCipherSuites(strArr);
    }
}
