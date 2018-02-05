package com.mqunar.core.res.compat;

import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.os.Build;
import android.os.Build.VERSION;
import android.util.SparseArray;
import com.mqunar.contacts.basis.model.Contact;
import com.mqunar.core.QunarApkLoader;
import com.mqunar.core.ReflectUtils;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

public class WebViewCompat {
    private static boolean a = "ONEPLUS A3000".equals(Build.MODEL);
    private static Set<String> b;
    private static StringBuilder c = new StringBuilder();
    public static boolean isC106 = true;
    public static boolean isCustomization;
    public static boolean isCustomization2 = true;

    static {
        HashSet hashSet;
        if (VERSION.SDK_INT == 22) {
            hashSet = new HashSet();
            hashSet.addAll(Arrays.asList(new String[]{"OPPO A53m", "OPPO A53", "R7Plusm", "OPPO R7sm", "OPPO A33", "OPPO A33m", "OPPO A33t", "OPPO R7sPlus", "A51", "A51kc", "OPPO A30", "OPPO A53t"}));
            isCustomization = hashSet.contains(Build.MODEL);
        } else if (VERSION.SDK_INT == 23) {
            hashSet = new HashSet();
            hashSet.addAll(Arrays.asList(new String[]{"OPPO R6091"}));
            isCustomization = hashSet.contains(Build.MODEL);
        } else if (VERSION.SDK_INT == 26) {
            hashSet = new HashSet();
            hashSet.addAll(Arrays.asList(new String[]{"G8142"}));
            isCustomization = hashSet.contains(Build.MODEL);
        } else {
            isCustomization = false;
        }
        if ("C106-9".equals(Build.MODEL) && "6.0.1".equals(VERSION.RELEASE)) {
        }
        hashSet = new HashSet();
        hashSet.addAll(Arrays.asList(new String[]{"OPPO R9 Plusm A", "OPPO R9m", "OPPO R7s", "OPPO R9tm", "OPPO R9t", "OPPO A59m", "OPPO A37m", "OPPO R9km", "OPPO R9 Plustm A", "OPPO A37t", "OPPO R7", "OPPO R9 Plust A"}));
        if (hashSet.contains(Build.MODEL) && VERSION.SDK_INT == 22) {
        }
    }

    private static boolean a(SparseArray sparseArray) {
        int i = 0;
        if (VERSION.SDK_INT < 23) {
            return false;
        }
        HashSet hashSet = new HashSet(sparseArray.size());
        while (i < sparseArray.size()) {
            hashSet.add(sparseArray.valueAt(i));
            i++;
        }
        return hashSet.contains("com.le.android.webview");
    }

    private static boolean b(SparseArray sparseArray) {
        int i = 0;
        if (VERSION.SDK_INT < 22) {
            return false;
        }
        HashSet hashSet = new HashSet(sparseArray.size());
        while (i < sparseArray.size()) {
            hashSet.add(sparseArray.valueAt(i));
            i++;
        }
        return hashSet.contains("androidhwext");
    }

    public static void append(String str) {
        if (c != null) {
            c.append(str).append(Contact.NUMBER);
        }
    }

    public static String getLog() {
        return c == null ? "" : c.toString();
    }

    public static boolean check(Context context, AssetManager assetManager, Method method) {
        if (b == null) {
            synchronized (WebViewCompat.class) {
                if (b == null) {
                    append("model:" + Build.MODEL);
                    append("Build.VERSION.SDK_INT:" + VERSION.SDK_INT);
                    SparseArray sparseArray = (SparseArray) ReflectUtils.invokeMethod("getAssignedPackageIdentifiers", context.getAssets(), new Class[0], new Object[0]);
                    Set hashSet = new HashSet();
                    if (sparseArray != null && sparseArray.size() > 0) {
                        int i;
                        int i2;
                        String str;
                        PackageManager packageManager = context.getPackageManager();
                        Set linkedHashSet = new LinkedHashSet();
                        for (i = 0; i < sparseArray.size(); i++) {
                            String str2 = (String) sparseArray.valueAt(i);
                            linkedHashSet.add(str2 + ":" + QunarApkLoader.getSourceDir(packageManager, str2));
                        }
                        append("sa:" + linkedHashSet);
                        SparseArray sparseArray2 = (SparseArray) ReflectUtils.invokeMethod("getAssignedPackageIdentifiers", assetManager, new Class[0], new Object[0]);
                        Set linkedHashSet2 = new LinkedHashSet();
                        for (i2 = 0; i2 < sparseArray2.size(); i2++) {
                            str = (String) sparseArray2.valueAt(i2);
                            linkedHashSet2.add(str + ":" + QunarApkLoader.getSourceDir(packageManager, str));
                        }
                        append("mySa:" + linkedHashSet2);
                        for (i = 0; i < sparseArray2.size(); i++) {
                            sparseArray.remove(sparseArray2.keyAt(i));
                        }
                        linkedHashSet2 = new LinkedHashSet();
                        for (i2 = 0; i2 < sparseArray.size(); i2++) {
                            str = (String) sparseArray.valueAt(i2);
                            linkedHashSet2.add(str + ":" + QunarApkLoader.getSourceDir(packageManager, str));
                        }
                        append("result:" + linkedHashSet2);
                        if (a || isCustomization || a(sparseArray) || b(sparseArray2)) {
                            for (i = 0; i < sparseArray.size(); i++) {
                                hashSet.add(QunarApkLoader.getSourceDir(packageManager, (String) sparseArray.valueAt(i)));
                            }
                        }
                    }
                    b = hashSet;
                    append("final:" + b);
                }
            }
        }
        if (b.isEmpty()) {
            return false;
        }
        return QunarApkLoader.addWebViewRes(assetManager, method, b);
    }
}
