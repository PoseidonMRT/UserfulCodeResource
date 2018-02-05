package com.mqunar.hy.res.libtask;

import java.net.Socket;
import java.security.KeyStore;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;
import org.apache.http.conn.ssl.SSLSocketFactory;

class QSSLSocketFactory extends SSLSocketFactory {
    SSLContext sslContext = SSLContext.getInstance("TLS");

    public QSSLSocketFactory(KeyStore keyStore) {
        super(keyStore);
        TrustManagerFactory instance = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
        instance.init(keyStore);
        TrustManager[] trustManagers = instance.getTrustManagers();
        if (trustManagers.length == 0) {
            throw new NoSuchAlgorithmException("no trust manager found");
        }
        final X509TrustManager x509TrustManager = (X509TrustManager) trustManagers[0];
        AnonymousClass1 anonymousClass1 = new X509TrustManager() {
            public void checkClientTrusted(X509Certificate[] x509CertificateArr, String str) {
                x509TrustManager.checkClientTrusted(x509CertificateArr, str);
            }

            public void checkServerTrusted(X509Certificate[] x509CertificateArr, String str) {
                if (x509CertificateArr == null || x509CertificateArr.length != 1) {
                    x509TrustManager.checkServerTrusted(x509CertificateArr, str);
                    return;
                }
                String name = x509CertificateArr[0].getIssuerX500Principal().getName();
                if (name == null || name.contains("*.qunar.com")) {
                    x509CertificateArr[0].checkValidity();
                    return;
                }
                throw new CertificateException("host can not be trusted");
            }

            public X509Certificate[] getAcceptedIssuers() {
                return x509TrustManager.getAcceptedIssuers();
            }
        };
        this.sslContext.init(null, new TrustManager[]{anonymousClass1}, new SecureRandom());
    }

    public Socket createSocket(Socket socket, String str, int i, boolean z) {
        return this.sslContext.getSocketFactory().createSocket(socket, str, i, z);
    }

    public Socket createSocket() {
        return this.sslContext.getSocketFactory().createSocket();
    }
}
