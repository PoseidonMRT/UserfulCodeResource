package org.apache.http.message;

import org.apache.http.HeaderElement;
import org.apache.http.HeaderElementIterator;
import org.apache.http.HeaderIterator;

@Deprecated
public class BasicHeaderElementIterator implements HeaderElementIterator {
    public BasicHeaderElementIterator(HeaderIterator headerIterator, HeaderValueParser headerValueParser) {
        throw new RuntimeException("Stub!");
    }

    public BasicHeaderElementIterator(HeaderIterator headerIterator) {
        throw new RuntimeException("Stub!");
    }

    public boolean hasNext() {
        throw new RuntimeException("Stub!");
    }

    public HeaderElement nextElement() {
        throw new RuntimeException("Stub!");
    }

    public final Object next() {
        throw new RuntimeException("Stub!");
    }

    public void remove() {
        throw new RuntimeException("Stub!");
    }
}
