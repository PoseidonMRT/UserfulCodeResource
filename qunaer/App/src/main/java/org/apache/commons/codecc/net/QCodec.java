package org.apache.commons.codecc.net;

import android.support.v4.media.TransportMediator;
import java.util.BitSet;
import org.apache.commons.codecc.DecoderException;
import org.apache.commons.codecc.EncoderException;
import org.apache.commons.codecc.StringDecoder;
import org.apache.commons.codecc.StringEncoder;

public class QCodec extends RFC1522Codec implements StringDecoder, StringEncoder {
    private static final byte BLANK = (byte) 32;
    private static final BitSet PRINTABLE_CHARS = new BitSet(256);
    private static final byte UNDERSCORE = (byte) 95;
    private final String charset;
    private boolean encodeBlanks;

    static {
        int i;
        PRINTABLE_CHARS.set(32);
        PRINTABLE_CHARS.set(33);
        PRINTABLE_CHARS.set(34);
        PRINTABLE_CHARS.set(35);
        PRINTABLE_CHARS.set(36);
        PRINTABLE_CHARS.set(37);
        PRINTABLE_CHARS.set(38);
        PRINTABLE_CHARS.set(39);
        PRINTABLE_CHARS.set(40);
        PRINTABLE_CHARS.set(41);
        PRINTABLE_CHARS.set(42);
        PRINTABLE_CHARS.set(43);
        PRINTABLE_CHARS.set(44);
        PRINTABLE_CHARS.set(45);
        PRINTABLE_CHARS.set(46);
        PRINTABLE_CHARS.set(47);
        for (i = 48; i <= 57; i++) {
            PRINTABLE_CHARS.set(i);
        }
        PRINTABLE_CHARS.set(58);
        PRINTABLE_CHARS.set(59);
        PRINTABLE_CHARS.set(60);
        PRINTABLE_CHARS.set(62);
        PRINTABLE_CHARS.set(64);
        for (i = 65; i <= 90; i++) {
            PRINTABLE_CHARS.set(i);
        }
        PRINTABLE_CHARS.set(91);
        PRINTABLE_CHARS.set(92);
        PRINTABLE_CHARS.set(93);
        PRINTABLE_CHARS.set(94);
        PRINTABLE_CHARS.set(96);
        for (i = 97; i <= 122; i++) {
            PRINTABLE_CHARS.set(i);
        }
        PRINTABLE_CHARS.set(123);
        PRINTABLE_CHARS.set(124);
        PRINTABLE_CHARS.set(125);
        PRINTABLE_CHARS.set(TransportMediator.KEYCODE_MEDIA_PLAY);
    }

    public QCodec() {
        this("UTF-8");
    }

    public QCodec(String str) {
        this.encodeBlanks = false;
        this.charset = str;
    }

    protected String getEncoding() {
        return "Q";
    }

    protected byte[] doEncoding(byte[] bArr) {
        if (bArr == null) {
            return null;
        }
        byte[] encodeQuotedPrintable = QuotedPrintableCodec.encodeQuotedPrintable(PRINTABLE_CHARS, bArr);
        if (this.encodeBlanks) {
            for (int i = 0; i < encodeQuotedPrintable.length; i++) {
                if (encodeQuotedPrintable[i] == BLANK) {
                    encodeQuotedPrintable[i] = UNDERSCORE;
                }
            }
        }
        return encodeQuotedPrintable;
    }

    protected byte[] doDecoding(byte[] bArr) {
        int i = 0;
        if (bArr == null) {
            return null;
        }
        int i2;
        byte b;
        for (byte b2 : bArr) {
            if (b2 == UNDERSCORE) {
                i2 = 1;
                break;
            }
        }
        i2 = 0;
        if (i2 == 0) {
            return QuotedPrintableCodec.decodeQuotedPrintable(bArr);
        }
        byte[] bArr2 = new byte[bArr.length];
        while (i < bArr.length) {
            b2 = bArr[i];
            if (b2 != UNDERSCORE) {
                bArr2[i] = b2;
            } else {
                bArr2[i] = BLANK;
            }
            i++;
        }
        return QuotedPrintableCodec.decodeQuotedPrintable(bArr2);
    }

    public String encode(String str, String str2) {
        if (str == null) {
            return null;
        }
        try {
            return encodeText(str, str2);
        } catch (Throwable e) {
            throw new EncoderException(e.getMessage(), e);
        }
    }

    public String encode(String str) {
        if (str == null) {
            return null;
        }
        return encode(str, getDefaultCharset());
    }

    public String decode(String str) {
        if (str == null) {
            return null;
        }
        try {
            return decodeText(str);
        } catch (Throwable e) {
            throw new DecoderException(e.getMessage(), e);
        }
    }

    public Object encode(Object obj) {
        if (obj == null) {
            return null;
        }
        if (obj instanceof String) {
            return encode((String) obj);
        }
        throw new EncoderException("Objects of type " + obj.getClass().getName() + " cannot be encoded using Q codec");
    }

    public Object decode(Object obj) {
        if (obj == null) {
            return null;
        }
        if (obj instanceof String) {
            return decode((String) obj);
        }
        throw new DecoderException("Objects of type " + obj.getClass().getName() + " cannot be decoded using Q codec");
    }

    public String getDefaultCharset() {
        return this.charset;
    }

    public boolean isEncodeBlanks() {
        return this.encodeBlanks;
    }

    public void setEncodeBlanks(boolean z) {
        this.encodeBlanks = z;
    }
}
