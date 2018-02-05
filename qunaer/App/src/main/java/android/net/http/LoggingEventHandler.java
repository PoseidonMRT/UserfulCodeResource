package android.net.http;

public class LoggingEventHandler implements EventHandler {
    public LoggingEventHandler() {
        throw new RuntimeException("Stub!");
    }

    public void requestSent() {
        throw new RuntimeException("Stub!");
    }

    public void status(int i, int i2, int i3, String str) {
        throw new RuntimeException("Stub!");
    }

    public void headers(Headers headers) {
        throw new RuntimeException("Stub!");
    }

    public void locationChanged(String str, boolean z) {
        throw new RuntimeException("Stub!");
    }

    public void data(byte[] bArr, int i) {
        throw new RuntimeException("Stub!");
    }

    public void endData() {
        throw new RuntimeException("Stub!");
    }

    public void certificate(SslCertificate sslCertificate) {
        throw new RuntimeException("Stub!");
    }

    public void error(int i, String str) {
        throw new RuntimeException("Stub!");
    }

    public boolean handleSslErrorRequest(SslError sslError) {
        throw new RuntimeException("Stub!");
    }
}
