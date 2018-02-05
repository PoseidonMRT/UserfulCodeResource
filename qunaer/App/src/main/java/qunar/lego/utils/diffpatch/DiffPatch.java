package qunar.lego.utils.diffpatch;

import android.content.Context;

public class DiffPatch {
    public static native int bsdiff(String str, String str2, String str3);

    public static native int bspatch(String str, String str2, String str3);

    static {
        try {
            System.loadLibrary("diffpatch");
        } catch (Throwable th) {
            th.printStackTrace();
        }
    }

    public static void bspatch(Context context, String str, String str2) {
        bspatch(context.getApplicationInfo().sourceDir, str, str2);
    }
}
