package com.mqunar.network;

import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import javax.net.ssl.X509TrustManager;

class n implements X509TrustManager {
    final /* synthetic */ X509TrustManager a;
    final /* synthetic */ QSSLSocketFactory b;

    n(QSSLSocketFactory qSSLSocketFactory, X509TrustManager x509TrustManager) {
        this.b = qSSLSocketFactory;
        this.a = x509TrustManager;
    }

    public void checkClientTrusted(X509Certificate[] x509CertificateArr, String str) {
        this.a.checkClientTrusted(x509CertificateArr, str);
    }

    public void checkServerTrusted(X509Certificate[] x509CertificateArr, String str) {
        if (x509CertificateArr == null || x509CertificateArr.length <= 1) {
            throw new IllegalArgumentException("certificates == null or certificates.length <= 1!");
        }
        try {
            this.a.checkServerTrusted(x509CertificateArr, str);
        } catch (CertificateException e) {
            if (!this.b.a(e)) {
                TrustCA.getInstance().checkServerTrusted(x509CertificateArr, str);
            }
        }
    }

    public X509Certificate[] getAcceptedIssuers() {
        return this.a.getAcceptedIssuers();
    }
}
