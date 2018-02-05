package qunar.lego.utils;

import java.net.URLEncoder;
import java.security.Key;
import java.security.KeyFactory;
import java.security.spec.KeySpec;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Map;
import javax.crypto.Cipher;
import org.apache.commons.io.IOUtils;

public class Hallua {
    private static final String TAG = Hallua.class.getSimpleName();

    class Base64 {
        private static final int BASELENGTH = 128;
        private static final int EIGHTBIT = 8;
        private static final int FOURBYTE = 4;
        private static final int LOOKUPLENGTH = 64;
        private static final char PAD = '=';
        private static final int SIGN = -128;
        private static final int SIXTEENBIT = 16;
        private static final int TWENTYFOURBITGROUP = 24;
        private static final byte[] base64Alphabet = new byte[128];
        private static final char[] lookUpBase64Alphabet = new char[64];

        Base64() {
        }

        static {
            int i;
            int i2 = 0;
            for (i = 0; i < 128; i++) {
                base64Alphabet[i] = (byte) -1;
            }
            for (i = 90; i >= 65; i--) {
                base64Alphabet[i] = (byte) (i - 65);
            }
            for (i = 122; i >= 97; i--) {
                base64Alphabet[i] = (byte) ((i - 97) + 26);
            }
            for (i = 57; i >= 48; i--) {
                base64Alphabet[i] = (byte) ((i - 48) + 52);
            }
            base64Alphabet[43] = (byte) 62;
            base64Alphabet[47] = (byte) 63;
            for (i = 0; i <= 25; i++) {
                lookUpBase64Alphabet[i] = (char) (i + 65);
            }
            int i3 = 26;
            i = 0;
            while (i3 <= 51) {
                lookUpBase64Alphabet[i3] = (char) (i + 97);
                i3++;
                i++;
            }
            i = 52;
            while (i <= 61) {
                lookUpBase64Alphabet[i] = (char) (i2 + 48);
                i++;
                i2++;
            }
            lookUpBase64Alphabet[62] = '+';
            lookUpBase64Alphabet[63] = IOUtils.DIR_SEPARATOR_UNIX;
        }

        private static boolean isWhiteSpace(char c) {
            return c == ' ' || c == '\r' || c == '\n' || c == '\t';
        }

        private static boolean isPad(char c) {
            return c == PAD;
        }

        private static boolean isData(char c) {
            return c < '' && base64Alphabet[c] != (byte) -1;
        }

        public static String encode(byte[] bArr) {
            int i = 0;
            if (bArr == null) {
                return null;
            }
            int length = bArr.length * 8;
            if (length == 0) {
                return "";
            }
            int i2 = length % 24;
            int i3 = length / 24;
            char[] cArr = new char[((i2 != 0 ? i3 + 1 : i3) * 4)];
            int i4 = 0;
            int i5 = 0;
            while (i4 < i3) {
                length = i + 1;
                byte b = bArr[i];
                int i6 = length + 1;
                byte b2 = bArr[length];
                int i7 = i6 + 1;
                byte b3 = bArr[i6];
                byte b4 = (byte) (b2 & 15);
                byte b5 = (byte) (b & 3);
                if ((b & SIGN) == 0) {
                    i6 = (byte) (b >> 2);
                } else {
                    byte b6 = (byte) ((b >> 2) ^ 192);
                }
                if ((b2 & SIGN) == 0) {
                    i = (byte) (b2 >> 4);
                } else {
                    b = (byte) ((b2 >> 4) ^ 240);
                }
                if ((b3 & SIGN) == 0) {
                    length = (byte) (b3 >> 6);
                } else {
                    length = (byte) ((b3 >> 6) ^ 252);
                }
                int i8 = i5 + 1;
                cArr[i5] = lookUpBase64Alphabet[i6];
                i6 = i8 + 1;
                cArr[i8] = lookUpBase64Alphabet[i | (b5 << 4)];
                i5 = i6 + 1;
                cArr[i6] = lookUpBase64Alphabet[length | (b4 << 2)];
                i = i5 + 1;
                cArr[i5] = lookUpBase64Alphabet[b3 & 63];
                i4++;
                i5 = i;
                i = i7;
            }
            byte b7;
            byte b8;
            if (i2 == 8) {
                b7 = bArr[i];
                b8 = (byte) (b7 & 3);
                i = i5 + 1;
                cArr[i5] = lookUpBase64Alphabet[(b7 & SIGN) == 0 ? (byte) (b7 >> 2) : (byte) ((b7 >> 2) ^ 192)];
                length = i + 1;
                cArr[i] = lookUpBase64Alphabet[b8 << 4];
                i3 = length + 1;
                cArr[length] = PAD;
                length = i3 + 1;
                cArr[i3] = PAD;
            } else if (i2 == 16) {
                b7 = bArr[i];
                b = bArr[i + 1];
                b6 = (byte) (b & 15);
                byte b9 = (byte) (b7 & 3);
                if ((b7 & SIGN) == 0) {
                    i3 = (byte) (b7 >> 2);
                } else {
                    b8 = (byte) ((b7 >> 2) ^ 192);
                }
                length = (b & SIGN) == 0 ? (byte) (b >> 4) : (byte) ((b >> 4) ^ 240);
                i = i5 + 1;
                cArr[i5] = lookUpBase64Alphabet[i3];
                i3 = i + 1;
                cArr[i] = lookUpBase64Alphabet[length | (b9 << 4)];
                length = i3 + 1;
                cArr[i3] = lookUpBase64Alphabet[b6 << 2];
                i3 = length + 1;
                cArr[length] = PAD;
            }
            return new String(cArr);
        }

        public static byte[] decode(String str) {
            if (str == null) {
                return null;
            }
            char[] toCharArray = str.toCharArray();
            int removeWhiteSpace = removeWhiteSpace(toCharArray);
            if (removeWhiteSpace % 4 != 0) {
                return null;
            }
            int i = removeWhiteSpace / 4;
            if (i == 0) {
                return new byte[0];
            }
            Object obj = new byte[(i * 3)];
            int i2 = 0;
            int i3 = 0;
            int i4 = 0;
            while (i4 < i - 1) {
                int i5 = i2 + 1;
                char c = toCharArray[i2];
                if (!isData(c)) {
                    return null;
                }
                i2 = i5 + 1;
                char c2 = toCharArray[i5];
                if (!isData(c2)) {
                    return null;
                }
                int i6 = i2 + 1;
                char c3 = toCharArray[i2];
                if (!isData(c3)) {
                    return null;
                }
                i2 = i6 + 1;
                char c4 = toCharArray[i6];
                if (!isData(c4)) {
                    return null;
                }
                byte b = base64Alphabet[c];
                byte b2 = base64Alphabet[c2];
                byte b3 = base64Alphabet[c3];
                byte b4 = base64Alphabet[c4];
                int i7 = i3 + 1;
                obj[i3] = (byte) ((b << 2) | (b2 >> 4));
                int i8 = i7 + 1;
                obj[i7] = (byte) (((b2 & 15) << 4) | ((b3 >> 2) & 15));
                i3 = i8 + 1;
                obj[i8] = (byte) ((b3 << 6) | b4);
                i4++;
            }
            i = i2 + 1;
            char c5 = toCharArray[i2];
            if (!isData(c5)) {
                return null;
            }
            i5 = i + 1;
            char c6 = toCharArray[i];
            if (!isData(c6)) {
                return null;
            }
            byte b5 = base64Alphabet[c5];
            byte b6 = base64Alphabet[c6];
            i8 = i5 + 1;
            c2 = toCharArray[i5];
            i6 = i8 + 1;
            char c7 = toCharArray[i8];
            if (isData(c2) && isData(c7)) {
                byte b7 = base64Alphabet[c2];
                byte b8 = base64Alphabet[c7];
                i4 = i3 + 1;
                obj[i3] = (byte) ((b5 << 2) | (b6 >> 4));
                i2 = i4 + 1;
                obj[i4] = (byte) (((b6 & 15) << 4) | ((b7 >> 2) & 15));
                i3 = i2 + 1;
                obj[i2] = (byte) ((b7 << 6) | b8);
                return obj;
            } else if (isPad(c2) && isPad(c7)) {
                if ((b6 & 15) != 0) {
                    return null;
                }
                r0 = new byte[((i4 * 3) + 1)];
                System.arraycopy(obj, 0, r0, 0, i4 * 3);
                r0[i3] = (byte) ((b5 << 2) | (b6 >> 4));
                return r0;
            } else if (isPad(c2) || !isPad(c7)) {
                return null;
            } else {
                byte b9 = base64Alphabet[c2];
                if ((b9 & 3) != 0) {
                    return null;
                }
                r0 = new byte[((i4 * 3) + 2)];
                System.arraycopy(obj, 0, r0, 0, i4 * 3);
                removeWhiteSpace = i3 + 1;
                r0[i3] = (byte) ((b5 << 2) | (b6 >> 4));
                r0[removeWhiteSpace] = (byte) (((b6 & 15) << 4) | ((b9 >> 2) & 15));
                return r0;
            }
        }

        private static int removeWhiteSpace(char[] cArr) {
            int i = 0;
            if (cArr != null) {
                int length = cArr.length;
                int i2 = 0;
                while (i2 < length) {
                    int i3;
                    if (isWhiteSpace(cArr[i2])) {
                        i3 = i;
                    } else {
                        i3 = i + 1;
                        cArr[i] = cArr[i2];
                    }
                    i2++;
                    i = i3;
                }
            }
            return i;
        }
    }

    public static native Map<String, byte[]> runLua(String str, String str2, Map<String, byte[]> map);

    static {
        try {
            System.loadLibrary("lua_v1_0_4");
        } catch (UnsatisfiedLinkError e) {
        }
    }

    public static byte[] rsa(byte[] bArr, byte[] bArr2, int i) {
        if (i == 2) {
            try {
                return encryptByPublicKey(bArr, new String(bArr2, "UTF-8"));
            } catch (Throwable th) {
                return null;
            }
        } else if (i == 3) {
            return encryptByPrivateKey(bArr, new String(bArr2, "UTF-8"));
        } else {
            if (i == 0) {
                return decryptByPublicKey(bArr, new String(bArr2, "UTF-8"));
            }
            if (i == 1) {
                return decryptByPrivateKey(bArr, new String(bArr2, "UTF-8"));
            }
            return null;
        }
    }

    private static byte[] subArray(byte[] bArr, int i, int i2) {
        if (bArr == null) {
            return null;
        }
        if (i < 0) {
            i = 0;
        }
        if (i2 > bArr.length) {
            i2 = bArr.length;
        }
        int i3 = i2 - i;
        if (i3 <= 0) {
            return new byte[0];
        }
        Object obj = new byte[i3];
        System.arraycopy(bArr, i, obj, 0, i3);
        return obj;
    }

    private static byte[] addAll(byte[] bArr, byte... bArr2) {
        if (bArr == null) {
            return clone(bArr2);
        }
        if (bArr2 == null) {
            return clone(bArr);
        }
        byte[] bArr3 = new byte[(bArr.length + bArr2.length)];
        System.arraycopy(bArr, 0, bArr3, 0, bArr.length);
        System.arraycopy(bArr2, 0, bArr3, bArr.length, bArr2.length);
        return bArr3;
    }

    public static byte[] clone(byte[] bArr) {
        if (bArr == null) {
            return null;
        }
        return (byte[]) bArr.clone();
    }

    private static byte[] encryptByPublicKey(byte[] bArr, String str) {
        try {
            Key generatePublic = KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(Base64.decode(str)));
            byte[] bArr2 = new byte[0];
            Cipher instance = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            instance.init(1, generatePublic);
            for (int i = 0; i < bArr.length; i += 100) {
                bArr2 = addAll(bArr2, instance.doFinal(subArray(bArr, i, i + 100)));
            }
            return uEncode(Base64.encode(bArr2)).getBytes();
        } catch (Exception e) {
            return null;
        }
    }

    private static byte[] encryptByPrivateKey(byte[] bArr, String str) {
        try {
            KeySpec pKCS8EncodedKeySpec = new PKCS8EncodedKeySpec(Base64.decode(str));
            KeyFactory instance = KeyFactory.getInstance("RSA");
            Key generatePrivate = instance.generatePrivate(pKCS8EncodedKeySpec);
            Cipher instance2 = Cipher.getInstance(instance.getAlgorithm());
            instance2.init(1, generatePrivate);
            return uEncode(Base64.encode(instance2.doFinal(bArr))).getBytes();
        } catch (Exception e) {
            return null;
        }
    }

    private static byte[] decryptByPublicKey(byte[] bArr, String str) {
        try {
            KeySpec x509EncodedKeySpec = new X509EncodedKeySpec(Base64.decode(str));
            KeyFactory instance = KeyFactory.getInstance("RSA");
            Key generatePublic = instance.generatePublic(x509EncodedKeySpec);
            Cipher instance2 = Cipher.getInstance(instance.getAlgorithm());
            instance2.init(2, generatePublic);
            return uEncode(Base64.encode(instance2.doFinal(bArr))).getBytes();
        } catch (Exception e) {
            return null;
        }
    }

    private static byte[] decryptByPrivateKey(byte[] bArr, String str) {
        try {
            KeySpec pKCS8EncodedKeySpec = new PKCS8EncodedKeySpec(Base64.decode(str));
            KeyFactory instance = KeyFactory.getInstance("RSA");
            Key generatePrivate = instance.generatePrivate(pKCS8EncodedKeySpec);
            Cipher instance2 = Cipher.getInstance(instance.getAlgorithm());
            instance2.init(2, generatePrivate);
            return uEncode(Base64.encode(instance2.doFinal(bArr))).getBytes();
        } catch (Exception e) {
            return null;
        }
    }

    private static String uEncode(String str) {
        try {
            return URLEncoder.encode(str, "utf-8");
        } catch (Exception e) {
            return null;
        }
    }
}
