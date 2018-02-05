package org.apache.commons.io.input;

import com.baidu.location.BDLocation;
import com.iflytek.speech.VoiceWakeuperAidl;
import com.mqunar.necro.agent.instrumentation.HttpInstrumentation;
import com.mqunar.necro.agent.instrumentation.Instrumented;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.text.MessageFormat;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.io.ByteOrderMark;

@Instrumented
public class XmlStreamReader extends Reader {
    private static final ByteOrderMark[] BOMS = new ByteOrderMark[]{ByteOrderMark.UTF_8, ByteOrderMark.UTF_16BE, ByteOrderMark.UTF_16LE, ByteOrderMark.UTF_32BE, ByteOrderMark.UTF_32LE};
    private static final int BUFFER_SIZE = 4096;
    private static final Pattern CHARSET_PATTERN = Pattern.compile("charset=[\"']?([.[^; \"']]*)[\"']?");
    private static final String EBCDIC = "CP1047";
    public static final Pattern ENCODING_PATTERN = Pattern.compile("<\\?xml.*encoding[\\s]*=[\\s]*((?:\".[^\"]*\")|(?:'.[^']*'))", 8);
    private static final String HTTP_EX_1 = "Invalid encoding, CT-MIME [{0}] CT-Enc [{1}] BOM [{2}] XML guess [{3}] XML prolog [{4}], BOM must be NULL";
    private static final String HTTP_EX_2 = "Invalid encoding, CT-MIME [{0}] CT-Enc [{1}] BOM [{2}] XML guess [{3}] XML prolog [{4}], encoding mismatch";
    private static final String HTTP_EX_3 = "Invalid encoding, CT-MIME [{0}] CT-Enc [{1}] BOM [{2}] XML guess [{3}] XML prolog [{4}], Invalid MIME";
    private static final String RAW_EX_1 = "Invalid encoding, BOM [{0}] XML guess [{1}] XML prolog [{2}] encoding mismatch";
    private static final String RAW_EX_2 = "Invalid encoding, BOM [{0}] XML guess [{1}] XML prolog [{2}] unknown BOM";
    private static final String US_ASCII = "US-ASCII";
    private static final String UTF_16 = "UTF-16";
    private static final String UTF_16BE = "UTF-16BE";
    private static final String UTF_16LE = "UTF-16LE";
    private static final String UTF_32 = "UTF-32";
    private static final String UTF_32BE = "UTF-32BE";
    private static final String UTF_32LE = "UTF-32LE";
    private static final String UTF_8 = "UTF-8";
    private static final ByteOrderMark[] XML_GUESS_BYTES = new ByteOrderMark[]{new ByteOrderMark("UTF-8", 60, 63, 120, 109), new ByteOrderMark("UTF-16BE", 0, 60, 0, 63), new ByteOrderMark("UTF-16LE", 60, 0, 63, 0), new ByteOrderMark(UTF_32BE, 0, 0, 0, 60, 0, 0, 0, 63, 0, 0, 0, 120, 0, 0, 0, 109), new ByteOrderMark(UTF_32LE, 60, 0, 0, 0, 63, 0, 0, 0, 120, 0, 0, 0, 109, 0, 0, 0), new ByteOrderMark(EBCDIC, 76, 111, BDLocation.TypeServerError, 148)};
    private final String defaultEncoding;
    private final String encoding;
    private final Reader reader;

    public String getDefaultEncoding() {
        return this.defaultEncoding;
    }

    public XmlStreamReader(File file) {
        this(new FileInputStream(file));
    }

    public XmlStreamReader(InputStream inputStream) {
        this(inputStream, true);
    }

    public XmlStreamReader(InputStream inputStream, boolean z) {
        this(inputStream, z, null);
    }

    public XmlStreamReader(InputStream inputStream, boolean z, String str) {
        this.defaultEncoding = str;
        InputStream bOMInputStream = new BOMInputStream(new BufferedInputStream(inputStream, 4096), false, BOMS);
        InputStream bOMInputStream2 = new BOMInputStream(bOMInputStream, true, XML_GUESS_BYTES);
        this.encoding = doRawStream(bOMInputStream, bOMInputStream2, z);
        this.reader = new InputStreamReader(bOMInputStream2, this.encoding);
    }

    public XmlStreamReader(URL url) {
        this(HttpInstrumentation.openConnection(url.openConnection()), null);
    }

    public XmlStreamReader(URLConnection uRLConnection, String str) {
        this.defaultEncoding = str;
        String contentType = uRLConnection.getContentType();
        InputStream bOMInputStream = new BOMInputStream(new BufferedInputStream(uRLConnection.getInputStream(), 4096), false, BOMS);
        InputStream bOMInputStream2 = new BOMInputStream(bOMInputStream, true, XML_GUESS_BYTES);
        if ((uRLConnection instanceof HttpURLConnection) || contentType != null) {
            this.encoding = doHttpStream(bOMInputStream, bOMInputStream2, contentType, true);
        } else {
            this.encoding = doRawStream(bOMInputStream, bOMInputStream2, true);
        }
        this.reader = new InputStreamReader(bOMInputStream2, this.encoding);
    }

    public XmlStreamReader(InputStream inputStream, String str) {
        this(inputStream, str, true);
    }

    public XmlStreamReader(InputStream inputStream, String str, boolean z, String str2) {
        this.defaultEncoding = str2;
        InputStream bOMInputStream = new BOMInputStream(new BufferedInputStream(inputStream, 4096), false, BOMS);
        InputStream bOMInputStream2 = new BOMInputStream(bOMInputStream, true, XML_GUESS_BYTES);
        this.encoding = doHttpStream(bOMInputStream, bOMInputStream2, str, z);
        this.reader = new InputStreamReader(bOMInputStream2, this.encoding);
    }

    public XmlStreamReader(InputStream inputStream, String str, boolean z) {
        this(inputStream, str, z, null);
    }

    public String getEncoding() {
        return this.encoding;
    }

    public int read(char[] cArr, int i, int i2) {
        return this.reader.read(cArr, i, i2);
    }

    public void close() {
        this.reader.close();
    }

    private String doRawStream(BOMInputStream bOMInputStream, BOMInputStream bOMInputStream2, boolean z) {
        String bOMCharsetName = bOMInputStream.getBOMCharsetName();
        String bOMCharsetName2 = bOMInputStream2.getBOMCharsetName();
        try {
            return calculateRawEncoding(bOMCharsetName, bOMCharsetName2, getXmlProlog(bOMInputStream2, bOMCharsetName2));
        } catch (XmlStreamReaderException e) {
            if (z) {
                return doLenientDetection(null, e);
            }
            throw e;
        }
    }

    private String doHttpStream(BOMInputStream bOMInputStream, BOMInputStream bOMInputStream2, String str, boolean z) {
        String bOMCharsetName = bOMInputStream.getBOMCharsetName();
        String bOMCharsetName2 = bOMInputStream2.getBOMCharsetName();
        try {
            return calculateHttpEncoding(str, bOMCharsetName, bOMCharsetName2, getXmlProlog(bOMInputStream2, bOMCharsetName2), z);
        } catch (XmlStreamReaderException e) {
            if (z) {
                return doLenientDetection(str, e);
            }
            throw e;
        }
    }

    private String doLenientDetection(String str, XmlStreamReaderException xmlStreamReaderException) {
        if (str != null && str.startsWith("text/html")) {
            try {
                return calculateHttpEncoding("text/xml" + str.substring("text/html".length()), xmlStreamReaderException.getBomEncoding(), xmlStreamReaderException.getXmlGuessEncoding(), xmlStreamReaderException.getXmlEncoding(), true);
            } catch (XmlStreamReaderException e) {
                xmlStreamReaderException = e;
            }
        }
        String xmlEncoding = xmlStreamReaderException.getXmlEncoding();
        if (xmlEncoding == null) {
            xmlEncoding = xmlStreamReaderException.getContentTypeEncoding();
        }
        if (xmlEncoding == null) {
            return this.defaultEncoding == null ? "UTF-8" : this.defaultEncoding;
        } else {
            return xmlEncoding;
        }
    }

    String calculateRawEncoding(String str, String str2, String str3) {
        if (str == null) {
            if (str2 == null || str3 == null) {
                String str4;
                if (this.defaultEncoding == null) {
                    str4 = "UTF-8";
                } else {
                    str4 = this.defaultEncoding;
                }
                return str4;
            } else if (str3.equals("UTF-16") && (str2.equals("UTF-16BE") || str2.equals("UTF-16LE"))) {
                return str2;
            } else {
                return str3;
            }
        } else if (str.equals("UTF-8")) {
            if (str2 != null && !str2.equals("UTF-8")) {
                throw new XmlStreamReaderException(MessageFormat.format(RAW_EX_1, new Object[]{str, str2, str3}), str, str2, str3);
            } else if (str3 == null || str3.equals("UTF-8")) {
                return str;
            } else {
                throw new XmlStreamReaderException(MessageFormat.format(RAW_EX_1, new Object[]{str, str2, str3}), str, str2, str3);
            }
        } else if (str.equals("UTF-16BE") || str.equals("UTF-16LE")) {
            if (str2 != null && !str2.equals(str)) {
                throw new XmlStreamReaderException(MessageFormat.format(RAW_EX_1, new Object[]{str, str2, str3}), str, str2, str3);
            } else if (str3 == null || str3.equals("UTF-16") || str3.equals(str)) {
                return str;
            } else {
                throw new XmlStreamReaderException(MessageFormat.format(RAW_EX_1, new Object[]{str, str2, str3}), str, str2, str3);
            }
        } else if (!str.equals(UTF_32BE) && !str.equals(UTF_32LE)) {
            throw new XmlStreamReaderException(MessageFormat.format(RAW_EX_2, new Object[]{str, str2, str3}), str, str2, str3);
        } else if (str2 != null && !str2.equals(str)) {
            throw new XmlStreamReaderException(MessageFormat.format(RAW_EX_1, new Object[]{str, str2, str3}), str, str2, str3);
        } else if (str3 == null || str3.equals(UTF_32) || str3.equals(str)) {
            return str;
        } else {
            throw new XmlStreamReaderException(MessageFormat.format(RAW_EX_1, new Object[]{str, str2, str3}), str, str2, str3);
        }
    }

    String calculateHttpEncoding(String str, String str2, String str3, String str4, boolean z) {
        if (z && str4 != null) {
            return str4;
        }
        String contentTypeMime = getContentTypeMime(str);
        String contentTypeEncoding = getContentTypeEncoding(str);
        boolean isAppXml = isAppXml(contentTypeMime);
        boolean isTextXml = isTextXml(contentTypeMime);
        if (!isAppXml && !isTextXml) {
            throw new XmlStreamReaderException(MessageFormat.format(HTTP_EX_3, new Object[]{contentTypeMime, contentTypeEncoding, str2, str3, str4}), contentTypeMime, contentTypeEncoding, str2, str3, str4);
        } else if (contentTypeEncoding == null) {
            if (isAppXml) {
                return calculateRawEncoding(str2, str3, str4);
            }
            return this.defaultEncoding == null ? "US-ASCII" : this.defaultEncoding;
        } else if (contentTypeEncoding.equals("UTF-16BE") || contentTypeEncoding.equals("UTF-16LE")) {
            if (str2 == null) {
                return contentTypeEncoding;
            }
            throw new XmlStreamReaderException(MessageFormat.format(HTTP_EX_1, new Object[]{contentTypeMime, contentTypeEncoding, str2, str3, str4}), contentTypeMime, contentTypeEncoding, str2, str3, str4);
        } else if (contentTypeEncoding.equals("UTF-16")) {
            if (str2 != null && str2.startsWith("UTF-16")) {
                return str2;
            }
            throw new XmlStreamReaderException(MessageFormat.format(HTTP_EX_2, new Object[]{contentTypeMime, contentTypeEncoding, str2, str3, str4}), contentTypeMime, contentTypeEncoding, str2, str3, str4);
        } else if (contentTypeEncoding.equals(UTF_32BE) || contentTypeEncoding.equals(UTF_32LE)) {
            if (str2 == null) {
                return contentTypeEncoding;
            }
            throw new XmlStreamReaderException(MessageFormat.format(HTTP_EX_1, new Object[]{contentTypeMime, contentTypeEncoding, str2, str3, str4}), contentTypeMime, contentTypeEncoding, str2, str3, str4);
        } else if (!contentTypeEncoding.equals(UTF_32)) {
            return contentTypeEncoding;
        } else {
            if (str2 != null && str2.startsWith(UTF_32)) {
                return str2;
            }
            throw new XmlStreamReaderException(MessageFormat.format(HTTP_EX_2, new Object[]{contentTypeMime, contentTypeEncoding, str2, str3, str4}), contentTypeMime, contentTypeEncoding, str2, str3, str4);
        }
    }

    static String getContentTypeMime(String str) {
        if (str == null) {
            return null;
        }
        int indexOf = str.indexOf(VoiceWakeuperAidl.PARAMS_SEPARATE);
        if (indexOf >= 0) {
            str = str.substring(0, indexOf);
        }
        return str.trim();
    }

    static String getContentTypeEncoding(String str) {
        if (str == null) {
            return null;
        }
        int indexOf = str.indexOf(VoiceWakeuperAidl.PARAMS_SEPARATE);
        if (indexOf <= -1) {
            return null;
        }
        String group;
        Matcher matcher = CHARSET_PATTERN.matcher(str.substring(indexOf + 1));
        if (matcher.find()) {
            group = matcher.group(1);
        } else {
            group = null;
        }
        if (group != null) {
            return group.toUpperCase(Locale.US);
        }
        return null;
    }

    private static String getXmlProlog(InputStream inputStream, String str) {
        if (str == null) {
            return null;
        }
        byte[] bArr = new byte[4096];
        inputStream.mark(4096);
        int i = -1;
        int read = inputStream.read(bArr, 0, 4096);
        int i2 = 4096;
        int i3 = 0;
        String str2 = null;
        while (read != -1 && i == -1 && i3 < 4096) {
            i3 += read;
            i2 -= read;
            read = inputStream.read(bArr, i3, i2);
            str2 = new String(bArr, 0, i3, str);
            i = str2.indexOf(62);
        }
        if (i == -1) {
            if (read == -1) {
                throw new IOException("Unexpected end of XML stream");
            }
            throw new IOException("XML prolog or ROOT element not found on first " + i3 + " bytes");
        } else if (i3 <= 0) {
            return null;
        } else {
            inputStream.reset();
            BufferedReader bufferedReader = new BufferedReader(new StringReader(str2.substring(0, i + 1)));
            CharSequence stringBuffer = new StringBuffer();
            for (str2 = bufferedReader.readLine(); str2 != null; str2 = bufferedReader.readLine()) {
                stringBuffer.append(str2);
            }
            Matcher matcher = ENCODING_PATTERN.matcher(stringBuffer);
            if (!matcher.find()) {
                return null;
            }
            String toUpperCase = matcher.group(1).toUpperCase();
            return toUpperCase.substring(1, toUpperCase.length() - 1);
        }
    }

    static boolean isAppXml(String str) {
        return str != null && (str.equals("application/xml") || str.equals("application/xml-dtd") || str.equals("application/xml-external-parsed-entity") || (str.startsWith("application/") && str.endsWith("+xml")));
    }

    static boolean isTextXml(String str) {
        return str != null && (str.equals("text/xml") || str.equals("text/xml-external-parsed-entity") || (str.startsWith("text/") && str.endsWith("+xml")));
    }
}
