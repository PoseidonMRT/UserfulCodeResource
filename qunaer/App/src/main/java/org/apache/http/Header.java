package org.apache.http;

@Deprecated
public interface Header {
    HeaderElement[] getElements();

    String getName();

    String getValue();
}
