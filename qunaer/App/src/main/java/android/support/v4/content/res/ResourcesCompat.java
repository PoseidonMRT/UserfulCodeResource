package android.support.v4.content.res;

import android.content.res.Resources;
import android.content.res.Resources.Theme;
import android.graphics.drawable.Drawable;
import android.os.Build.VERSION;

public class ResourcesCompat {
    public static Drawable getDrawable(Resources resources, int i, Theme theme) {
        if (VERSION.SDK_INT >= 21) {
            return ResourcesCompatApi21.getDrawable(resources, i, theme);
        }
        return resources.getDrawable(i);
    }

    public static Drawable getDrawableForDensity(Resources resources, int i, int i2, Theme theme) {
        int i3 = VERSION.SDK_INT;
        if (i3 >= 21) {
            return ResourcesCompatApi21.getDrawableForDensity(resources, i, i2, theme);
        }
        if (i3 >= 15) {
            return ResourcesCompatIcsMr1.getDrawableForDensity(resources, i, i2);
        }
        return resources.getDrawable(i);
    }
}
