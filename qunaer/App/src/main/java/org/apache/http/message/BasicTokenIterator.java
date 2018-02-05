package org.apache.http.message;

import org.apache.http.HeaderIterator;
import org.apache.http.TokenIterator;

@Deprecated
public class BasicTokenIterator implements TokenIterator {
    public static final String HTTP_SEPARATORS = " ,;=()<>@:\\\"/[]?{}\t";
    protected String currentHeader;
    protected String currentToken;
    protected final HeaderIterator headerIt;
    protected int searchPos;

    public BasicTokenIterator(HeaderIterator headerIterator) {
        throw new RuntimeException("Stub!");
    }

    public boolean hasNext() {
        throw new RuntimeException("Stub!");
    }

    public String nextToken() {
        throw new RuntimeException("Stub!");
    }

    public final Object next() {
        throw new RuntimeException("Stub!");
    }

    public final void remove() {
        throw new RuntimeException("Stub!");
    }

    protected int findNext(int i) {
        throw new RuntimeException("Stub!");
    }

    protected String createToken(String str, int i, int i2) {
        throw new RuntimeException("Stub!");
    }

    protected int findTokenStart(int i) {
        throw new RuntimeException("Stub!");
    }

    protected int findTokenSeparator(int i) {
        throw new RuntimeException("Stub!");
    }

    protected int findTokenEnd(int i) {
        throw new RuntimeException("Stub!");
    }

    protected boolean isTokenSeparator(char c) {
        throw new RuntimeException("Stub!");
    }

    protected boolean isWhitespace(char c) {
        throw new RuntimeException("Stub!");
    }

    protected boolean isTokenChar(char c) {
        throw new RuntimeException("Stub!");
    }

    protected boolean isHttpSeparator(char c) {
        throw new RuntimeException("Stub!");
    }
}
