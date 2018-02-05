package com.mqunar.network;

import java.net.Socket;
import java.security.KeyStore;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;
import org.apache.http.conn.ssl.SSLSocketFactory;

public class QSSLSocketFactory extends SSLSocketFactory {
    SSLContext a = SSLContext.getInstance("TLS");

    public QSSLSocketFactory(KeyStore keyStore) {
        super(keyStore);
        TrustManagerFactory instance = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
        instance.init(keyStore);
        TrustManager[] trustManagers = instance.getTrustManagers();
        if (trustManagers.length == 0) {
            throw new NoSuchAlgorithmException("no trust manager found");
        }
        n nVar = new n(this, (X509TrustManager) trustManagers[0]);
        this.a.init(null, new TrustManager[]{nVar}, new SecureRandom());
    }

    private boolean a(CertificateException certificateException) {
        if (certificateException != null) {
            try {
                if (certificateException.getCause() != null) {
                    String message = certificateException.getCause().getMessage();
                    String name = certificateException.getCause().getClass().getName();
                    if (message != null && message.contains("GMT+") && "com.android.org.bouncycastle.jce.exception.ExtCertPathValidatorException".equals(name)) {
                        return true;
                    }
                }
            } catch (Throwable th) {
            }
        }
        return false;
    }

    public Socket createSocket(Socket socket, String str, int i, boolean z) {
        return this.a.getSocketFactory().createSocket(socket, str, i, z);
    }

    public Socket createSocket() {
        return this.a.getSocketFactory().createSocket();
    }
}
