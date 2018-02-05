package com.mqunar.imp;

import android.util.Log;

public class Imp {
    public static native byte[] d(byte[] bArr);

    public static native byte[] e(byte[] bArr, byte[] bArr2);

    public static native byte[] version();

    static {
        try {
            System.loadLibrary("imp_1_0");
        } catch (Throwable e) {
            Log.e("JNI", e.getMessage(), e);
            Log.d("JNI", "failed first");
        }
    }
}
