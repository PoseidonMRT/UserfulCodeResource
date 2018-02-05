package com.huawei.hwid.openapi.e.a;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public final class a {
    public static byte[] a(byte[] bArr, int i, byte[] bArr2, int i2) {
        return a(bArr, i, bArr2, i2, 0);
    }

    public static byte[] b(byte[] bArr, int i, byte[] bArr2, int i2) {
        return a(bArr, i, bArr2, i2, 1);
    }

    private static byte[] a(byte[] bArr, int i, byte[] bArr2, int i2, int i3) {
        if (bArr == null || bArr2 == null) {
            return new byte[0];
        }
        int length;
        int i4;
        if (i <= 0 || i > bArr.length) {
            i = bArr.length;
        }
        if (i2 <= 0 || i2 > bArr2.length) {
            length = bArr2.length;
        } else {
            length = i2;
        }
        if (length > 16) {
            length = 16;
        }
        byte[] bArr3 = new byte[16];
        for (i4 = 0; i4 < 16; i4++) {
            bArr3[i4] = (byte) 0;
        }
        for (i4 = 0; i4 < length; i4++) {
            bArr3[i4] = bArr2[i4];
        }
        Cipher instance = Cipher.getInstance("AES/ECB/PKCS5Padding");
        if (i3 == 0) {
            length = 1;
        } else {
            length = 2;
        }
        instance.init(length, new SecretKeySpec(bArr3, 0, 16, "AES"));
        return instance.doFinal(bArr, 0, i);
    }
}
