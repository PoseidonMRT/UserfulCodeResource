package com.baidu.platform.comapi.util;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.location.LocationManager;
import android.net.http.Headers;
import android.os.Build;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.provider.Settings.Secure;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;
import com.baidu.android.bbalbs.common.util.CommonParam;
import com.baidu.mapapi.VersionInfo;
import com.baidu.platform.comjni.util.AppMD5;
import com.baidu.platform.comjni.util.a;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import org.apache.commons.io.FilenameUtils;

public class e {
    private static final String A = e.class.getSimpleName();
    private static a B = new a();
    private static boolean C = true;
    private static int D = 0;
    private static int E = 0;
    private static Map<String, String> F = new HashMap();
    static String a = "02";
    static String b;
    static String c;
    static String d;
    static String e;
    static int f;
    static int g;
    static int h;
    static int i;
    static int j;
    static int k;
    static String l;
    static String m;
    static String n = "baidu";
    static String o = "baidu";
    static String p = "";
    static String q = "";
    static String r = "";
    static String s;
    static String t;
    static String u = "-1";
    static String v = "-1";
    public static Context w;
    public static final int x = Integer.parseInt(VERSION.SDK);
    public static float y = 1.0f;
    public static String z;

    public static void a() {
        d();
    }

    public static void a(String str) {
        l = str;
        f();
    }

    public static void a(String str, String str2) {
        u = str2;
        v = str;
        f();
    }

    public static byte[] a(Context context) {
        try {
            return context.getPackageManager().getPackageInfo(context.getPackageName(), 64).signatures[0].toByteArray();
        } catch (NameNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Bundle b() {
        Bundle bundle = new Bundle();
        bundle.putString("cpu", p);
        bundle.putString("resid", a);
        bundle.putString("channel", n);
        bundle.putString("glr", q);
        bundle.putString("glv", r);
        bundle.putString("mb", g());
        bundle.putString("sv", i());
        bundle.putString("os", k());
        bundle.putInt("dpi_x", l());
        bundle.putInt("dpi_y", l());
        bundle.putString("net", l);
        bundle.putString("cuid", o());
        bundle.putByteArray("signature", a(w));
        bundle.putString("pcn", w.getPackageName());
        bundle.putInt("screen_x", h());
        bundle.putInt("screen_y", j());
        if (B != null) {
            B.a(bundle);
            Log.d("phoneInfo", "mAppSysOSAPI not null");
        }
        Log.d("phoneInfo", bundle.toString());
        return bundle;
    }

    public static void b(Context context) {
        w = context;
        s = context.getFilesDir().getAbsolutePath();
        t = context.getCacheDir().getAbsolutePath();
        c = Build.MODEL;
        d = "Android" + VERSION.SDK;
        b = context.getPackageName();
        c(context);
        d(context);
        e(context);
        f(context);
        try {
            LocationManager locationManager = (LocationManager) context.getSystemService(Headers.LOCATION);
            D = locationManager.isProviderEnabled("gps") ? 1 : 0;
            E = locationManager.isProviderEnabled("network") ? 1 : 0;
            F.put("resid", AppMD5.encodeUrlParamsValue(a));
            F.put("channel", AppMD5.encodeUrlParamsValue(m()));
            F.put("mb", AppMD5.encodeUrlParamsValue(g()));
            F.put("sv", AppMD5.encodeUrlParamsValue(i()));
            F.put("os", AppMD5.encodeUrlParamsValue(k()));
            F.put("dpi", AppMD5.encodeUrlParamsValue(String.format("%d,%d", new Object[]{Integer.valueOf(l()), Integer.valueOf(l())})));
            F.put("cuid", AppMD5.encodeUrlParamsValue(o()));
            F.put("pcn", AppMD5.encodeUrlParamsValue(w.getPackageName()));
            F.put("screen", AppMD5.encodeUrlParamsValue(String.format("%d,%d", new Object[]{Integer.valueOf(h()), Integer.valueOf(j())})));
        } catch (Exception e) {
            Log.w("baidumapsdk", "LocationManager error");
            F.put("resid", AppMD5.encodeUrlParamsValue(a));
            F.put("channel", AppMD5.encodeUrlParamsValue(m()));
            F.put("mb", AppMD5.encodeUrlParamsValue(g()));
            F.put("sv", AppMD5.encodeUrlParamsValue(i()));
            F.put("os", AppMD5.encodeUrlParamsValue(k()));
            F.put("dpi", AppMD5.encodeUrlParamsValue(String.format("%d,%d", new Object[]{Integer.valueOf(l()), Integer.valueOf(l())})));
            F.put("cuid", AppMD5.encodeUrlParamsValue(o()));
            F.put("pcn", AppMD5.encodeUrlParamsValue(w.getPackageName()));
            F.put("screen", AppMD5.encodeUrlParamsValue(String.format("%d,%d", new Object[]{Integer.valueOf(h()), Integer.valueOf(j())})));
        } catch (Throwable th) {
            F.put("resid", AppMD5.encodeUrlParamsValue(a));
            F.put("channel", AppMD5.encodeUrlParamsValue(m()));
            F.put("mb", AppMD5.encodeUrlParamsValue(g()));
            F.put("sv", AppMD5.encodeUrlParamsValue(i()));
            F.put("os", AppMD5.encodeUrlParamsValue(k()));
            F.put("dpi", AppMD5.encodeUrlParamsValue(String.format("%d,%d", new Object[]{Integer.valueOf(l()), Integer.valueOf(l())})));
            F.put("cuid", AppMD5.encodeUrlParamsValue(o()));
            F.put("pcn", AppMD5.encodeUrlParamsValue(w.getPackageName()));
            F.put("screen", AppMD5.encodeUrlParamsValue(String.format("%d,%d", new Object[]{Integer.valueOf(h()), Integer.valueOf(j())})));
        }
        if (B != null) {
            B.a();
        }
    }

    public static String c() {
        if (F == null) {
            return null;
        }
        Date date = new Date();
        long time = date.getTime() + ((long) (date.getSeconds() * 1000));
        double d = (((double) (time % 1000)) / 1000.0d) + ((double) (time / 1000));
        F.put("ctm", AppMD5.encodeUrlParamsValue(String.format("%f", new Object[]{Double.valueOf(d)})));
        StringBuilder stringBuilder = new StringBuilder();
        for (Entry entry : F.entrySet()) {
            stringBuilder.append("&").append((String) entry.getKey()).append("=").append((String) entry.getValue());
        }
        return stringBuilder.toString();
    }

    private static void c(Context context) {
        try {
            PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            e = VersionInfo.getApiVersion();
            if (!(e == null || e.equals(""))) {
                e = e.replace('_', FilenameUtils.EXTENSION_SEPARATOR);
            }
            f = packageInfo.versionCode;
        } catch (NameNotFoundException e) {
            e = "1.0.0";
            f = 1;
        }
    }

    public static void d() {
    }

    private static void d(Context context) {
        WindowManager windowManager = (WindowManager) context.getSystemService("window");
        DisplayMetrics displayMetrics = new DisplayMetrics();
        Display defaultDisplay = windowManager != null ? windowManager.getDefaultDisplay() : null;
        if (defaultDisplay != null) {
            g = defaultDisplay.getWidth();
            h = defaultDisplay.getHeight();
            defaultDisplay.getMetrics(displayMetrics);
        }
        y = displayMetrics.density;
        i = (int) displayMetrics.xdpi;
        j = (int) displayMetrics.ydpi;
        if (x > 3) {
            k = displayMetrics.densityDpi;
        } else {
            k = 160;
        }
        if (k == 0) {
            k = 160;
        }
    }

    public static String e() {
        return l;
    }

    private static void e(Context context) {
        m = Secure.getString(context.getContentResolver(), "android_id");
    }

    public static void f() {
        F.put("net", AppMD5.encodeUrlParamsValue(e()));
        F.put("appid", AppMD5.encodeUrlParamsValue(u));
        F.put("bduid", "");
        if (B != null) {
            Bundle bundle = new Bundle();
            bundle.putString("cpu", p);
            bundle.putString("resid", a);
            bundle.putString("channel", n);
            bundle.putString("glr", q);
            bundle.putString("glv", r);
            bundle.putString("mb", g());
            bundle.putString("sv", i());
            bundle.putString("os", k());
            bundle.putInt("dpi_x", l());
            bundle.putInt("dpi_y", l());
            bundle.putString("net", l);
            bundle.putString("cuid", o());
            bundle.putString("pcn", w.getPackageName());
            bundle.putInt("screen_x", h());
            bundle.putInt("screen_y", j());
            bundle.putString("appid", u);
            bundle.putString("duid", v);
            if (!TextUtils.isEmpty(z)) {
                bundle.putString("token", z);
            }
            B.a(bundle);
            SysUpdateObservable.getInstance().updatePhoneInfo();
        }
    }

    private static void f(Context context) {
        l = "0";
    }

    public static String g() {
        return c;
    }

    public static int h() {
        return g;
    }

    public static String i() {
        return e;
    }

    public static int j() {
        return h;
    }

    public static String k() {
        return d;
    }

    public static int l() {
        return k;
    }

    public static String m() {
        return n;
    }

    public static String n() {
        return s;
    }

    public static String o() {
        String a;
        try {
            a = CommonParam.a(w);
        } catch (Exception e) {
            a = "";
        }
        return a == null ? "" : a;
    }
}
