package org.apache.commons.codec.net;

import org.apache.commons.codec.StringDecoder;
import org.apache.commons.codec.StringEncoder;

@Deprecated
public class BCodec extends RFC1522Codec implements StringDecoder, StringEncoder {
    public BCodec() {
        throw new RuntimeException("Stub!");
    }

    public BCodec(String str) {
        throw new RuntimeException("Stub!");
    }

    protected String getEncoding() {
        throw new RuntimeException("Stub!");
    }

    protected byte[] doEncoding(byte[] bArr) {
        throw new RuntimeException("Stub!");
    }

    protected byte[] doDecoding(byte[] bArr) {
        throw new RuntimeException("Stub!");
    }

    public String encode(String str, String str2) {
        throw new RuntimeException("Stub!");
    }

    public String encode(String str) {
        throw new RuntimeException("Stub!");
    }

    public String decode(String str) {
        throw new RuntimeException("Stub!");
    }

    public Object encode(Object obj) {
        throw new RuntimeException("Stub!");
    }

    public Object decode(Object obj) {
        throw new RuntimeException("Stub!");
    }

    public String getDefaultCharset() {
        throw new RuntimeException("Stub!");
    }
}
