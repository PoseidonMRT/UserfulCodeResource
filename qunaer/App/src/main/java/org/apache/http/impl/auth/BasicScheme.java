package org.apache.http.impl.auth;

import org.apache.http.Header;
import org.apache.http.HttpRequest;
import org.apache.http.auth.Credentials;

@Deprecated
public class BasicScheme extends RFC2617Scheme {
    public BasicScheme() {
        throw new RuntimeException("Stub!");
    }

    public String getSchemeName() {
        throw new RuntimeException("Stub!");
    }

    public void processChallenge(Header header) {
        throw new RuntimeException("Stub!");
    }

    public boolean isComplete() {
        throw new RuntimeException("Stub!");
    }

    public boolean isConnectionBased() {
        throw new RuntimeException("Stub!");
    }

    public Header authenticate(Credentials credentials, HttpRequest httpRequest) {
        throw new RuntimeException("Stub!");
    }

    public static Header authenticate(Credentials credentials, String str, boolean z) {
        throw new RuntimeException("Stub!");
    }
}
