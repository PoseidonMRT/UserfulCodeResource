package com.huawei.hwid.openapi.e.a;

import java.util.Locale;

public final class d {
    public static String a(byte[] bArr, int i) {
        StringBuffer stringBuffer = new StringBuffer();
        if (bArr == null) {
            return null;
        }
        if (i <= 0 || i > bArr.length) {
            i = bArr.length;
        }
        for (int i2 = 0; i2 < i; i2++) {
            String toHexString = Integer.toHexString(bArr[i2] & 255);
            if (toHexString.length() == 1) {
                toHexString = "0" + toHexString;
            }
            stringBuffer.append(toHexString.toUpperCase(Locale.US));
        }
        return stringBuffer.toString();
    }

    public static byte[] a(String str) {
        if (str == null) {
            return new byte[0];
        }
        int length = str.length();
        if (length % 2 != 0) {
            return new byte[0];
        }
        String toUpperCase = str.toUpperCase(Locale.US);
        for (int i = 0; i < length; i++) {
            char charAt = toUpperCase.charAt(i);
            if (('0' > charAt || charAt > '9') && ('A' > charAt || charAt > 'F')) {
                return new byte[0];
            }
        }
        int i2 = length / 2;
        byte[] bArr = new byte[i2];
        byte[] bArr2 = new byte[2];
        length = 0;
        int i3 = 0;
        while (i3 < i2) {
            int i4 = length + 1;
            bArr2[0] = (byte) toUpperCase.charAt(length);
            int i5 = i4 + 1;
            bArr2[1] = (byte) toUpperCase.charAt(i4);
            length = 0;
            while (length < 2) {
                if ((byte) 65 > bArr2[length] || bArr2[length] > (byte) 70) {
                    bArr2[length] = (byte) (bArr2[length] - 48);
                } else {
                    bArr2[length] = (byte) (bArr2[length] - 55);
                }
                length++;
            }
            bArr[i3] = (byte) ((bArr2[0] << 4) | bArr2[1]);
            i3++;
            length = i5;
        }
        return bArr;
    }
}
