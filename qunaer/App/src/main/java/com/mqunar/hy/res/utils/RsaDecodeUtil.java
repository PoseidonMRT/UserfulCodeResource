package com.mqunar.hy.res.utils;

import android.util.Base64;
import com.mqunar.hy.res.logger.Timber;
import java.security.KeyFactory;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;
import javax.crypto.Cipher;

public class RsaDecodeUtil {
    private static Map<String, String> map = new HashMap();
    private static RSAPublicKey publicKey = getPublicKeyFromX509("RSA", "MEcwDQYJKoZIhvcNAQEBBQADNgAwMwIsAMtB+Gi1Yrzo48WkI8SJiWMwdiHkeXrq\nbmHMSZGM56qx2BmMjyi4zuJEhP8CAwEAAQ==");
    private static final int rsaLength = 60;

    public static boolean equals(String str, String str2) {
        return decode(str).equals(decode(str2));
    }

    public static String decode(String str) {
        if (str == null || str.length() != 60) {
            return str;
        }
        String str2 = (String) map.get(str);
        if (str2 != null || publicKey == null) {
            return str2;
        }
        try {
            str2 = decryptByPublicKey(str, publicKey);
            map.put(str, str2);
            return str2;
        } catch (Throwable e) {
            Timber.e(e, "解密出错", new Object[0]);
            return str2;
        }
    }

    private static String decryptByPublicKey(String str, RSAPublicKey rSAPublicKey) {
        int i = 0;
        Cipher instance = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        instance.init(2, rSAPublicKey);
        String str2 = "";
        byte[][] splitArray = splitArray(Base64.decode(str, 0), rSAPublicKey.getModulus().bitLength() / 8);
        int length = splitArray.length;
        while (i < length) {
            str2 = str2 + new String(instance.doFinal(splitArray[i]));
            i++;
        }
        return str2;
    }

    private static RSAPublicKey getPublicKeyFromX509(String str, String str2) {
        try {
            return (RSAPublicKey) KeyFactory.getInstance(str).generatePublic(new X509EncodedKeySpec(Base64.decode(str2, 0)));
        } catch (Throwable th) {
            return null;
        }
    }

    public static String[] splitString(String str, int i) {
        int i2;
        int length = str.length() / i;
        int length2 = str.length() % i;
        if (length2 != 0) {
            i2 = 1;
        } else {
            i2 = 0;
        }
        String[] strArr = new String[(length + i2)];
        String str2 = "";
        for (int i3 = 0; i3 < length + i2; i3++) {
            String substring;
            if (i3 != (length + i2) - 1 || length2 == 0) {
                substring = str.substring(i3 * i, (i3 * i) + i);
            } else {
                substring = str.substring(i3 * i, (i3 * i) + length2);
            }
            strArr[i3] = substring;
        }
        return strArr;
    }

    public static byte[][] splitArray(byte[] bArr, int i) {
        int i2;
        int length = bArr.length / i;
        int length2 = bArr.length % i;
        if (length2 != 0) {
            i2 = 1;
        } else {
            i2 = 0;
        }
        byte[][] bArr2 = new byte[(length + i2)][];
        for (int i3 = 0; i3 < length + i2; i3++) {
            Object obj = new byte[i];
            if (i3 != (length + i2) - 1 || length2 == 0) {
                System.arraycopy(bArr, i3 * i, obj, 0, i);
            } else {
                System.arraycopy(bArr, i3 * i, obj, 0, length2);
            }
            bArr2[i3] = obj;
        }
        return bArr2;
    }
}
