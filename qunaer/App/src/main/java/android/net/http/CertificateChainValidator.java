package android.net.http;

import javax.net.ssl.SSLSocket;

public class CertificateChainValidator {
    CertificateChainValidator() {
        throw new RuntimeException("Stub!");
    }

    public static CertificateChainValidator getInstance() {
        throw new RuntimeException("Stub!");
    }

    public SslError doHandshakeAndValidateServerCertificates(HttpsConnection httpsConnection, SSLSocket sSLSocket, String str) {
        throw new RuntimeException("Stub!");
    }

    public static SslError verifyServerCertificates(byte[][] bArr, String str, String str2) {
        throw new RuntimeException("Stub!");
    }

    public static void handleTrustStorageUpdate() {
        throw new RuntimeException("Stub!");
    }
}
