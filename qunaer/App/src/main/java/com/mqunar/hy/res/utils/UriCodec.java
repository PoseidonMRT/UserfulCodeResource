package com.mqunar.hy.res.utils;

import android.net.Uri;
import android.text.TextUtils;
import com.mqunar.contacts.basis.model.Contact;
import com.mqunar.hy.res.logger.Timber;
import java.io.ByteArrayOutputStream;
import java.net.URISyntaxException;
import java.nio.charset.Charset;

public abstract class UriCodec {
    private static final char[] DIGITS = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z'};
    private static final char[] UPPER_CASE_DIGITS = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'};
    static final Charset UTF_8 = Charset.forName("UTF-8");

    protected abstract boolean isRetained(char c);

    public final String validate(String str, int i, int i2, String str2) {
        int i3 = i;
        while (i3 < i2) {
            char charAt = str.charAt(i3);
            if ((charAt >= 'a' && charAt <= 'z') || ((charAt >= 'A' && charAt <= 'Z') || ((charAt >= '0' && charAt <= '9') || isRetained(charAt)))) {
                i3++;
            } else if (charAt != '%') {
                throw new URISyntaxException(str, "Illegal character in " + str2, i3);
            } else if (i3 + 2 >= i2) {
                throw new URISyntaxException(str, "Incomplete % sequence in " + str2, i3);
            } else {
                int hexToInt = hexToInt(str.charAt(i3 + 1));
                int hexToInt2 = hexToInt(str.charAt(i3 + 2));
                if (hexToInt == -1 || hexToInt2 == -1) {
                    throw new URISyntaxException(str, "Invalid % sequence: " + str.substring(i3, i3 + 3) + " in " + str2, i3);
                }
                i3 += 3;
            }
        }
        return str.substring(i, i2);
    }

    public static void validateSimple(String str, String str2) {
        for (int i = 0; i < str.length(); i++) {
            char charAt = str.charAt(i);
            if ((charAt < 'a' || charAt > 'z') && ((charAt < 'A' || charAt > 'Z') && ((charAt < '0' || charAt > '9') && str2.indexOf(charAt) <= -1))) {
                throw new URISyntaxException(str, "Illegal character", i);
            }
        }
    }

    private void appendEncoded(StringBuilder stringBuilder, String str, Charset charset, boolean z) {
        if (str == null) {
            throw new NullPointerException();
        }
        int i = 0;
        int i2 = -1;
        while (i < str.length()) {
            char charAt = str.charAt(i);
            if ((charAt >= 'a' && charAt <= 'z') || ((charAt >= 'A' && charAt <= 'Z') || ((charAt >= '0' && charAt <= '9') || isRetained(charAt) || (charAt == '%' && z)))) {
                if (i2 != -1) {
                    appendHex(stringBuilder, str.substring(i2, i), charset);
                    i2 = -1;
                }
                if (charAt == '%' && z) {
                    stringBuilder.append(str, i, i + 3);
                    i += 2;
                } else if (charAt == ' ') {
                    stringBuilder.append('+');
                } else {
                    stringBuilder.append(charAt);
                }
            } else if (i2 == -1) {
                i2 = i;
            }
            i++;
        }
        if (i2 != -1) {
            appendHex(stringBuilder, str.substring(i2, str.length()), charset);
        }
    }

    public final String encode(String str, Charset charset) {
        StringBuilder stringBuilder = new StringBuilder(str.length() + 16);
        appendEncoded(stringBuilder, str, charset, false);
        return stringBuilder.toString();
    }

    public final void appendEncoded(StringBuilder stringBuilder, String str) {
        appendEncoded(stringBuilder, str, UTF_8, false);
    }

    public final void appendPartiallyEncoded(StringBuilder stringBuilder, String str) {
        appendEncoded(stringBuilder, str, UTF_8, true);
    }

    public static String decode(String str, boolean z, Charset charset, boolean z2) {
        if (str.indexOf(37) == -1 && (!z || str.indexOf(43) == -1)) {
            return str;
        }
        StringBuilder stringBuilder = new StringBuilder(str.length());
        try {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            int i = 0;
            while (i < str.length()) {
                int i2;
                char charAt = str.charAt(i);
                if (charAt == '%') {
                    i2 = i;
                    do {
                        if (i2 + 2 < str.length()) {
                            i = hexToInt(str.charAt(i2 + 1));
                            if (i != -1) {
                                int hexToInt = hexToInt(str.charAt(i2 + 2));
                                if (hexToInt != -1) {
                                    byteArrayOutputStream.write((byte) ((i << 4) + hexToInt));
                                    i2 += 3;
                                    if (i2 < str.length()) {
                                        break;
                                    }
                                }
                            }
                        }
                        if (z2) {
                            throw new IllegalArgumentException("Invalid % sequence at " + i2 + ": " + str);
                        }
                        byte[] bytes = "�".getBytes(charset.name());
                        byteArrayOutputStream.write(bytes, 0, bytes.length);
                        i2 += 3;
                        if (i2 < str.length()) {
                            break;
                        }
                    } while (str.charAt(i2) == '%');
                    stringBuilder.append(new String(byteArrayOutputStream.toByteArray(), charset.name()));
                    byteArrayOutputStream.reset();
                } else {
                    if (z && charAt == '+') {
                        charAt = ' ';
                    }
                    stringBuilder.append(charAt);
                    i2 = i + 1;
                }
                i = i2;
            }
        } catch (Throwable e) {
            Timber.e(e, new Object[0]);
        }
        return stringBuilder.toString();
    }

    private static int hexToInt(char c) {
        if ('0' <= c && c <= '9') {
            return c - 48;
        }
        if ('a' <= c && c <= 'f') {
            return (c + 10) - 97;
        }
        if ('A' > c || c > 'F') {
            return -1;
        }
        return (c + 10) - 65;
    }

    public static String decode(String str) {
        return decode(str, false, UTF_8, true);
    }

    private static void appendHex(StringBuilder stringBuilder, String str, Charset charset) {
        for (byte appendHex : str.getBytes(charset.name())) {
            appendHex(stringBuilder, appendHex);
        }
    }

    private static void appendHex(StringBuilder stringBuilder, byte b) {
        stringBuilder.append('%');
        stringBuilder.append(byteToHexString(b, true));
    }

    public static String byteToHexString(byte b, boolean z) {
        char[] cArr = z ? UPPER_CASE_DIGITS : DIGITS;
        return new String(new char[]{cArr[(b >> 4) & 15], cArr[b & 15]}, 0, 2);
    }

    public static String getUrlWithOutQueryAndHash(String str) {
        int i = 268435455;
        if (str == null) {
            return null;
        }
        String trim = str.trim();
        if (!trim.contains("//")) {
            return trim;
        }
        String substring;
        int indexOf = !trim.contains("?") ? 268435455 : trim.indexOf("?");
        if (trim.contains(Contact.NUMBER)) {
            i = trim.indexOf(Contact.NUMBER);
        }
        if (indexOf >= i) {
            indexOf = i;
        }
        if (indexOf < trim.length()) {
            substring = trim.substring(0, indexOf);
        } else {
            substring = trim;
        }
        if (substring.endsWith("/")) {
            return substring.substring(0, substring.length() - 1);
        }
        return substring;
    }

    public static boolean compareWithoutQuery(Uri uri, String str) {
        if (uri == null || uri.isOpaque() || TextUtils.isEmpty(str)) {
            return false;
        }
        Uri parse = Uri.parse(str);
        if (parse.isOpaque()) {
            return false;
        }
        if (TextUtils.isEmpty(uri.getScheme())) {
            if (!TextUtils.isEmpty(parse.getScheme())) {
                return false;
            }
        } else if (!uri.getScheme().equals(parse.getScheme())) {
            return false;
        }
        if (TextUtils.isEmpty(uri.getAuthority())) {
            if (!TextUtils.isEmpty(parse.getAuthority())) {
                return false;
            }
        } else if (!uri.getAuthority().equals(parse.getAuthority())) {
            return false;
        }
        String path = uri.getPath();
        if (path == null || path.equals("/")) {
            path = "";
        }
        Object path2 = parse.getPath();
        if (path2 == null || path2.equals("/")) {
            path2 = "";
        }
        return path.equals(path2);
    }
}
