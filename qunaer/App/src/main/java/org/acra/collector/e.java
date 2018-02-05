package org.acra.collector;

import android.content.Context;
import android.graphics.Point;
import android.graphics.Rect;
import android.hardware.display.DisplayManager;
import android.os.Build.VERSION;
import android.support.annotation.NonNull;
import android.util.DisplayMetrics;
import android.util.SparseArray;
import android.view.Display;
import android.view.WindowManager;
import java.lang.reflect.Field;

final class e {
    private static final SparseArray<String> a = new SparseArray();

    @NonNull
    public static String a(@NonNull Context context) {
        StringBuilder stringBuilder = new StringBuilder();
        Display[] displayArr;
        if (VERSION.SDK_INT < 17) {
            displayArr = new Display[]{((WindowManager) context.getSystemService("window")).getDefaultDisplay()};
        } else {
            displayArr = ((DisplayManager) context.getSystemService("display")).getDisplays();
        }
        for (Display a : r0) {
            stringBuilder.append(a(a));
        }
        return stringBuilder.toString();
    }

    @NonNull
    private static Object a(@NonNull Display display) {
        display.getMetrics(new DisplayMetrics());
        return g(display) + h(display) + display.getDisplayId() + ".height=" + display.getHeight() + '\n' + j(display) + i(display) + display.getDisplayId() + ".orientation=" + display.getRotation() + '\n' + display.getDisplayId() + ".pixelFormat=" + display.getPixelFormat() + '\n' + k(display) + f(display) + d(display) + display.getDisplayId() + ".refreshRate=" + display.getRefreshRate() + '\n' + c(display) + e(display) + display.getDisplayId() + ".width=" + display.getWidth() + '\n' + b(display);
    }

    @NonNull
    private static String b(@NonNull Display display) {
        if (VERSION.SDK_INT >= 17) {
            return display.getDisplayId() + ".isValid=" + display.isValid() + '\n';
        }
        return "";
    }

    @NonNull
    private static String c(@NonNull Display display) {
        return display.getDisplayId() + ".rotation=" + a(display.getRotation()) + '\n';
    }

    @NonNull
    private static String a(int i) {
        switch (i) {
            case 0:
                return "ROTATION_0";
            case 1:
                return "ROTATION_90";
            case 2:
                return "ROTATION_180";
            case 3:
                return "ROTATION_270";
            default:
                return String.valueOf(i);
        }
    }

    @NonNull
    private static String d(@NonNull Display display) {
        if (VERSION.SDK_INT < 13) {
            return "";
        }
        Rect rect = new Rect();
        display.getRectSize(rect);
        return display.getDisplayId() + ".rectSize=[" + rect.top + ',' + rect.left + ',' + rect.width() + ',' + rect.height() + ']' + '\n';
    }

    @NonNull
    private static String e(@NonNull Display display) {
        if (VERSION.SDK_INT < 13) {
            return "";
        }
        Point point = new Point();
        display.getSize(point);
        return display.getDisplayId() + ".size=[" + point.x + ',' + point.y + ']' + '\n';
    }

    private static String f(@NonNull Display display) {
        if (VERSION.SDK_INT < 17) {
            return "";
        }
        Point point = new Point();
        display.getRealSize(point);
        return display.getDisplayId() + ".realSize=[" + point.x + ',' + point.y + ']' + '\n';
    }

    @NonNull
    private static String g(@NonNull Display display) {
        if (VERSION.SDK_INT < 16) {
            return "";
        }
        Point point = new Point();
        Point point2 = new Point();
        display.getCurrentSizeRange(point, point2);
        return display.getDisplayId() + ".currentSizeRange.smallest=[" + point.x + ',' + point.y + "]\n" + display.getDisplayId() + ".currentSizeRange.largest=[" + point2.x + ',' + point2.y + "]\n";
    }

    @NonNull
    private static String h(@NonNull Display display) {
        if (VERSION.SDK_INT < 17) {
            return "";
        }
        int flags = display.getFlags();
        for (Field field : display.getClass().getFields()) {
            if (field.getName().startsWith("FLAG_")) {
                try {
                    a.put(field.getInt(null), field.getName());
                } catch (IllegalAccessException e) {
                }
            }
        }
        return display.getDisplayId() + ".flags=" + a(a, flags) + '\n';
    }

    @NonNull
    private static String i(@NonNull Display display) {
        if (VERSION.SDK_INT >= 17) {
            return display.getDisplayId() + ".name=" + display.getName() + '\n';
        }
        return "";
    }

    @NonNull
    private static String j(@NonNull Display display) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        display.getMetrics(displayMetrics);
        return a(display.getDisplayId() + ".metrics", displayMetrics);
    }

    @NonNull
    private static String k(@NonNull Display display) {
        if (VERSION.SDK_INT < 17) {
            return "";
        }
        DisplayMetrics displayMetrics = new DisplayMetrics();
        display.getRealMetrics(displayMetrics);
        return a(display.getDisplayId() + ".realMetrics", displayMetrics);
    }

    @NonNull
    private static String a(@NonNull String str, @NonNull DisplayMetrics displayMetrics) {
        return str + ".density=" + displayMetrics.density + '\n' + str + ".densityDpi=" + displayMetrics.densityDpi + '\n' + str + ".scaledDensity=x" + displayMetrics.scaledDensity + '\n' + str + ".widthPixels=" + displayMetrics.widthPixels + '\n' + str + ".heightPixels=" + displayMetrics.heightPixels + '\n' + str + ".xdpi=" + displayMetrics.xdpi + '\n' + str + ".ydpi=" + displayMetrics.ydpi + '\n';
    }

    @NonNull
    private static String a(@NonNull SparseArray<String> sparseArray, int i) {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i2 = 0; i2 < sparseArray.size(); i2++) {
            int keyAt = sparseArray.keyAt(i2) & i;
            if (keyAt > 0) {
                if (stringBuilder.length() > 0) {
                    stringBuilder.append('+');
                }
                stringBuilder.append((String) sparseArray.get(keyAt));
            }
        }
        return stringBuilder.toString();
    }
}
