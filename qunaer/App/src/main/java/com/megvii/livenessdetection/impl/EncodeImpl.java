package com.megvii.livenessdetection.impl;

import android.util.Base64;

public class EncodeImpl {
    private static native String nativeEncode(byte[] bArr, boolean z, boolean z2, int i, String str);

    public static byte[] a(byte[] bArr, boolean z, boolean z2, int i, String str) {
        if (bArr == null || bArr.length == 0) {
            return null;
        }
        if (!z && !z2 && str == null) {
            return bArr;
        }
        if (i < 0) {
            return null;
        }
        String nativeEncode = nativeEncode(bArr, z, z2, i, str);
        if (nativeEncode == null) {
            return null;
        }
        return Base64.decode(nativeEncode, 0);
    }

    public static byte[] a(byte[] bArr, boolean z, boolean z2, int i) {
        return a(bArr, z, z2, i, null);
    }
}
