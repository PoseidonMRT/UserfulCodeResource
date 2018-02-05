package com.mqunar.tools;

import android.os.Build.VERSION;
import android.util.SparseArray;
import android.view.View;

public enum CompatUtil {
    ;
    
    static final int API11 = 11;
    static final int API3 = 3;
    static final int API4 = 4;
    static final int API5 = 5;
    static final int API8 = 8;
    static int version;

    static {
        version = VERSION.SDK_INT;
    }

    public static int getSDKVersion() {
        return version;
    }

    public static boolean hasFroyo() {
        return getSDKVersion() >= 8;
    }

    public static boolean hasGingerbread() {
        return getSDKVersion() >= 9;
    }

    public static boolean hasHoneycomb() {
        return getSDKVersion() >= 11;
    }

    public static boolean hasHoneycombMR1() {
        return getSDKVersion() >= 12;
    }

    public static boolean hasJellyBean() {
        return getSDKVersion() >= 16;
    }

    public static void setObjectToTag(int i, View view, Object obj) {
        if (hasHoneycomb()) {
            view.setTag(i, obj);
            return;
        }
        Object tag = view.getTag();
        if (!SparseArray.class.isInstance(tag)) {
            tag = new SparseArray();
            view.setTag(tag);
        }
        ((SparseArray) tag).put(i, obj);
        view.setTag(tag);
    }

    public static Object getObjectFromTag(int i, View view) {
        if (hasHoneycomb()) {
            return view.getTag(i);
        }
        Object tag = view.getTag();
        if (SparseArray.class.isInstance(tag)) {
            return ((SparseArray) tag).get(i);
        }
        return null;
    }
}
