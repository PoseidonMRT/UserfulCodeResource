package ctrip.android.oauth.utils;

import android.util.DisplayMetrics;
import android.util.TypedValue;

public class DeviceUtils {
    public static int getPixelFromDip(DisplayMetrics displayMetrics, float f) {
        return (int) (TypedValue.applyDimension(1, f, displayMetrics) + 0.5f);
    }
}
