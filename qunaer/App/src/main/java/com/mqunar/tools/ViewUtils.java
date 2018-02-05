package com.mqunar.tools;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.support.v4.view.ViewCompat;
import android.view.View;
import com.mqunar.tools.ViewSetter.Method;
import java.util.concurrent.atomic.AtomicInteger;

public class ViewUtils {
    private static final AtomicInteger a = new AtomicInteger(1);

    public static int generateViewId() {
        int i;
        int i2;
        do {
            i = a.get();
            i2 = i + 1;
            if (i2 > ViewCompat.MEASURED_SIZE_MASK) {
                i2 = 1;
            }
        } while (!a.compareAndSet(i, i2));
        return i;
    }

    public static int fakeGenId() {
        return generateViewId() | 268435456;
    }

    public static int unionGenId(int i) {
        if (i != 0) {
            return (268435455 & i) | 536870912;
        }
        throw new IllegalArgumentException("Id NOT ALLOW 0 !");
    }

    public static boolean setOrGone(View view, CharSequence charSequence) {
        return ViewSetter.getSetting(view).setOr(Method.Text, true, 8, charSequence);
    }

    public static boolean setOrHide(View view, CharSequence charSequence) {
        return ViewSetter.getSetting(view).setOr(Method.Text, true, 4, charSequence);
    }

    public static boolean setOrGone(View view, Drawable drawable) {
        return ViewSetter.getSetting(view).setOr(Method.Background, true, 8, drawable);
    }

    public static boolean setOrHide(View view, Drawable drawable) {
        return ViewSetter.getSetting(view).setOr(Method.Background, true, 4, drawable);
    }

    public static boolean setOrGone(View view, CharSequence... charSequenceArr) {
        return ViewSetter.getSetting(view).setOr(Method.Text, true, 8, (Object[]) charSequenceArr);
    }

    public static boolean setOrHide(View view, CharSequence... charSequenceArr) {
        return ViewSetter.getSetting(view).setOr(Method.Text, true, 4, (Object[]) charSequenceArr);
    }

    public static boolean setOrGone(View view, boolean z) {
        return ViewSetter.getSetting(view).setOr(Method.NotCare, z, 8, new Object[0]);
    }

    public static boolean setOrHide(View view, boolean z) {
        return ViewSetter.getSetting(view).setOr(Method.NotCare, z, 4, new Object[0]);
    }

    public static boolean setOrGone(View view, boolean z, CharSequence charSequence) {
        return ViewSetter.getSetting(view).setOr(Method.Text, z, 8, charSequence);
    }

    public static boolean setOrHide(View view, boolean z, CharSequence charSequence) {
        return ViewSetter.getSetting(view).setOr(Method.Text, z, 4, charSequence);
    }

    public static boolean setOrGone(View view, Bitmap bitmap) {
        return ViewSetter.getSetting(view).setOr(Method.Src, true, 8, bitmap);
    }

    public static boolean setOrHide(View view, Bitmap bitmap) {
        return ViewSetter.getSetting(view).setOr(Method.Src, true, 4, bitmap);
    }

    public static CharSequence joinAllowedNull(Object... objArr) {
        if (CheckUtils.isEmpty(objArr)) {
            return null;
        }
        StringBuilder stringBuilder = new StringBuilder();
        for (Object obj : objArr) {
            if (!CheckUtils.isEmpty(obj)) {
                stringBuilder.append(obj.toString());
            }
        }
        if (CheckUtils.isEmpty(stringBuilder)) {
            return null;
        }
        return stringBuilder.toString();
    }

    public static CharSequence joinNotAllowedNull(Object... objArr) {
        if (CheckUtils.isContainsEmpty(objArr)) {
            return null;
        }
        StringBuilder stringBuilder = new StringBuilder();
        for (Object obj : objArr) {
            stringBuilder.append(obj.toString());
        }
        return stringBuilder.toString();
    }

    public static StateListDrawable makeColorMask(Context context, int i) {
        return makeColorMask(context, i, 150994944);
    }

    public static StateListDrawable makeColorMask(Context context, int i, int i2) {
        Drawable colorDrawable = new ColorDrawable(i);
        Bitmap createBitmap = Bitmap.createBitmap(1, 1, Config.ARGB_8888);
        Canvas canvas = new Canvas(createBitmap);
        canvas.drawColor(i);
        canvas.drawColor(150994944);
        StateListDrawable stateListDrawable = new StateListDrawable();
        stateListDrawable.addState(new int[]{-16842919}, colorDrawable);
        stateListDrawable.addState(new int[]{16842919}, new BitmapDrawable(createBitmap));
        return stateListDrawable;
    }

    public static StateListDrawable makeColorMask(Context context, Bitmap bitmap) {
        return makeColorMask(context, bitmap, 150994944);
    }

    public static StateListDrawable makeColorMask(Context context, Bitmap bitmap, int i) {
        Bitmap copy = bitmap.copy(Config.ARGB_8888, false);
        Bitmap copy2 = bitmap.copy(Config.ARGB_8888, true);
        new Canvas(copy2).drawColor(i);
        StateListDrawable stateListDrawable = new StateListDrawable();
        stateListDrawable.addState(new int[]{-16842919}, new BitmapDrawable(copy));
        stateListDrawable.addState(new int[]{16842919}, new BitmapDrawable(copy2));
        return stateListDrawable;
    }

    public static Drawable scaleByDensity(Context context, Drawable drawable) {
        drawable.setBounds(0, 0, (int) (((float) drawable.getIntrinsicWidth()) * context.getResources().getDisplayMetrics().density), (int) (((float) drawable.getIntrinsicHeight()) * context.getResources().getDisplayMetrics().density));
        return drawable;
    }

    public static Drawable scaleByDensity(Context context, Bitmap bitmap) {
        return scaleByDensity(context, new BitmapDrawable(bitmap));
    }
}
