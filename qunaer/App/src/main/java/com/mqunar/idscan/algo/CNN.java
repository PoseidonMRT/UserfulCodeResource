package com.mqunar.idscan.algo;

import com.mqunar.idscan.a;
import com.mqunar.qav.uelog.QAVLog;
import com.mqunar.tools.log.QLog;

public class CNN {
    public static boolean a;

    static {
        a = true;
        try {
            System.loadLibrary("cnn");
            init(f.a);
            a = true;
        } catch (Throwable e) {
            a = false;
            QAVLog.getInstance(a.a()).log("CNN", "load cnn_v1_0_0 failed");
            QLog.e(e);
        }
    }

    private static native void init(String str);

    public static native int predict(int[] iArr);
}
