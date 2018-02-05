package org.apache.http.io;

import org.apache.http.HttpMessage;

@Deprecated
public interface HttpMessageParser {
    HttpMessage parse();
}
