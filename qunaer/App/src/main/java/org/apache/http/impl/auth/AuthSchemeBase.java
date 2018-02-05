package org.apache.http.impl.auth;

import org.apache.http.Header;
import org.apache.http.auth.AuthScheme;
import org.apache.http.util.CharArrayBuffer;

@Deprecated
public abstract class AuthSchemeBase implements AuthScheme {
    protected abstract void parseChallenge(CharArrayBuffer charArrayBuffer, int i, int i2);

    public AuthSchemeBase() {
        throw new RuntimeException("Stub!");
    }

    public void processChallenge(Header header) {
        throw new RuntimeException("Stub!");
    }

    public boolean isProxy() {
        throw new RuntimeException("Stub!");
    }
}
