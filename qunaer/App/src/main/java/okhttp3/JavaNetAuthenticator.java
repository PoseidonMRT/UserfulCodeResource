package okhttp3;

import java.net.Authenticator;
import java.net.Authenticator.RequestorType;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.PasswordAuthentication;
import java.net.Proxy;
import java.net.Proxy.Type;
import java.util.List;
import org.apache.http.HttpStatus;
import org.apache.http.auth.AUTH;

public final class JavaNetAuthenticator implements Authenticator {
    public Request authenticate(Route route, Response response) {
        List challenges = response.challenges();
        Request request = response.request();
        HttpUrl url = request.url();
        Object obj = response.code() == HttpStatus.SC_PROXY_AUTHENTICATION_REQUIRED ? 1 : null;
        Proxy proxy = route.proxy();
        int size = challenges.size();
        for (int i = 0; i < size; i++) {
            Challenge challenge = (Challenge) challenges.get(i);
            if ("Basic".equalsIgnoreCase(challenge.scheme())) {
                PasswordAuthentication requestPasswordAuthentication;
                if (obj != null) {
                    InetSocketAddress inetSocketAddress = (InetSocketAddress) proxy.address();
                    requestPasswordAuthentication = Authenticator.requestPasswordAuthentication(inetSocketAddress.getHostName(), getConnectToInetAddress(proxy, url), inetSocketAddress.getPort(), url.scheme(), challenge.realm(), challenge.scheme(), url.url(), RequestorType.PROXY);
                } else {
                    requestPasswordAuthentication = Authenticator.requestPasswordAuthentication(url.host(), getConnectToInetAddress(proxy, url), url.port(), url.scheme(), challenge.realm(), challenge.scheme(), url.url(), RequestorType.SERVER);
                }
                if (requestPasswordAuthentication != null) {
                    return request.newBuilder().header(obj != null ? AUTH.PROXY_AUTH_RESP : AUTH.WWW_AUTH_RESP, Credentials.basic(requestPasswordAuthentication.getUserName(), new String(requestPasswordAuthentication.getPassword()))).build();
                }
            }
        }
        return null;
    }

    private InetAddress getConnectToInetAddress(Proxy proxy, HttpUrl httpUrl) {
        if (proxy == null || proxy.type() == Type.DIRECT) {
            return InetAddress.getByName(httpUrl.host());
        }
        return ((InetSocketAddress) proxy.address()).getAddress();
    }
}
