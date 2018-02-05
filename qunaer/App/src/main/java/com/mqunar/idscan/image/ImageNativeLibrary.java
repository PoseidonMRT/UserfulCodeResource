package com.mqunar.idscan.image;

import com.mqunar.idscan.a;
import com.mqunar.qav.uelog.QAVLog;
import com.mqunar.tools.log.QLog;

public class ImageNativeLibrary {
    public static boolean a;

    static {
        a = true;
        try {
            System.loadLibrary("image");
            a = true;
        } catch (Throwable e) {
            a = false;
            QAVLog.getInstance(a.a()).log("ImageNativeLibrary", "load image_v1_0_0 failed");
            QLog.e(e);
        }
    }

    public static native void AdaptiveThreshold(int[] iArr, int i, int i2, int[] iArr2, int[] iArr3);

    public static native int Otsu(int[] iArr, int[] iArr2);

    public static native void RegionYUVtoRBGA(byte[] bArr, int i, int i2, int i3, int i4, int i5, int i6, int[] iArr);

    public static native void YUVtoARBG(byte[] bArr, int i, int i2, int[] iArr);

    public static native void YUVtoRBGA(byte[] bArr, int i, int i2, int[] iArr);
}
