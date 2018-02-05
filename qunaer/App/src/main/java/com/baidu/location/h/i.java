package com.baidu.location.h;

import android.content.Context;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.NetworkInfo.State;
import android.os.Build.VERSION;
import android.os.Environment;
import android.os.Process;
import android.provider.Settings.Secure;
import android.provider.Settings.System;
import android.text.TextUtils;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClientOption;
import com.baidu.location.f;
import com.baidu.location.f.a;
import com.baidu.location.f.b;
import com.baidu.location.f.c;
import com.baidu.location.f.e;
import com.baidu.platform.comapi.location.CoordinateType;
import java.io.File;
import java.io.FileInputStream;
import java.lang.reflect.Method;
import java.math.BigInteger;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.security.MessageDigest;
import java.util.Calendar;
import java.util.Enumeration;
import java.util.Locale;

public class i {
    public static float A = 2.2f;
    public static float B = 2.3f;
    public static float C = 3.8f;
    public static int D = 3;
    public static int E = 10;
    public static int F = 2;
    public static int G = 7;
    public static int H = 20;
    public static int I = 70;
    public static int J = 120;
    public static float K = 2.0f;
    public static float L = 10.0f;
    public static float M = 50.0f;
    public static float N = 200.0f;
    public static int O = 16;
    public static float P = 0.9f;
    public static int Q = LocationClientOption.MIN_AUTO_NOTIFY_INTERVAL;
    public static float R = 0.5f;
    public static float S = 0.0f;
    public static float T = 0.1f;
    public static int U = 30;
    public static int V = 100;
    public static int W = 0;
    public static int X = 0;
    public static int Y = 0;
    public static int Z = 420000;
    public static boolean a = false;
    private static String aA = "http://loc.map.baidu.com/iofd.php";
    private static String aB = "https://sapi.skyhookwireless.com/wps2/location";
    private static String aC = "http://loc.map.baidu.com/wloc";
    public static boolean aa = true;
    public static boolean ab = true;
    public static int ac = 20;
    public static int ad = 300;
    public static int ae = 1000;
    public static long af = 900000;
    public static long ag = 420000;
    public static long ah = 180000;
    public static long ai = 0;
    public static long aj = 15;
    public static long ak = 300000;
    public static int al = 1000;
    public static int am = 0;
    public static int an = 30000;
    public static int ao = 30000;
    public static float ap = 10.0f;
    public static float aq = 6.0f;
    public static float ar = 10.0f;
    public static int as = 60;
    public static int at = 70;
    public static int au = 6;
    private static String av = "http://loc.map.baidu.com/sdk.php";
    private static String aw = "http://loc.map.baidu.com/user_err.php";
    private static String ax = "http://loc.map.baidu.com/oqur.php";
    private static String ay = "http://loc.map.baidu.com/tcu.php";
    private static String az = "http://loc.map.baidu.com/rtbu.php";
    public static boolean b = false;
    public static boolean c = false;
    public static int d = 0;
    public static String e = "http://loc.map.baidu.com/sdk_ep.php";
    public static String f = "no";
    public static String g = "loc.map.baidu.com";
    public static String h = "";
    public static int i = 0;
    public static boolean j = false;
    public static boolean k = false;
    public static boolean l = false;
    public static boolean m = false;
    public static boolean n = false;
    public static String o = CoordinateType.GCJ02;
    public static boolean p = true;
    public static int q = 3;
    public static double r = 0.0d;
    public static double s = 0.0d;
    public static double t = 0.0d;
    public static double u = 0.0d;
    public static int v = 0;
    public static byte[] w = null;
    public static boolean x = false;
    public static int y = 0;
    public static float z = 1.1f;

    public static int a(String str, String str2, String str3) {
        int i = Integer.MIN_VALUE;
        if (!(str == null || str.equals(""))) {
            int indexOf = str.indexOf(str2);
            if (indexOf != -1) {
                indexOf += str2.length();
                int indexOf2 = str.indexOf(str3, indexOf);
                if (indexOf2 != -1) {
                    String substring = str.substring(indexOf, indexOf2);
                    if (!(substring == null || substring.equals(""))) {
                        try {
                            i = Integer.parseInt(substring);
                        } catch (NumberFormatException e) {
                        }
                    }
                }
            }
        }
        return i;
    }

    public static Object a(Context context, String str) {
        Object obj = null;
        if (context != null) {
            try {
                obj = context.getApplicationContext().getSystemService(str);
            } catch (Throwable th) {
            }
        }
        return obj;
    }

    public static Object a(Object obj, String str, Object... objArr) {
        Class cls = obj.getClass();
        Class[] clsArr = new Class[objArr.length];
        int length = objArr.length;
        for (int i = 0; i < length; i++) {
            clsArr[i] = objArr[i].getClass();
            if (clsArr[i] == Integer.class) {
                clsArr[i] = Integer.TYPE;
            }
        }
        Method declaredMethod = cls.getDeclaredMethod(str, clsArr);
        if (!declaredMethod.isAccessible()) {
            declaredMethod.setAccessible(true);
        }
        return declaredMethod.invoke(obj, objArr);
    }

    public static String a() {
        Calendar instance = Calendar.getInstance();
        int i = instance.get(5);
        int i2 = instance.get(1);
        int i3 = instance.get(2) + 1;
        int i4 = instance.get(11);
        int i5 = instance.get(12);
        int i6 = instance.get(13);
        return String.format(Locale.CHINA, "%d-%02d-%02d %02d:%02d:%02d", new Object[]{Integer.valueOf(i2), Integer.valueOf(i3), Integer.valueOf(i), Integer.valueOf(i4), Integer.valueOf(i5), Integer.valueOf(i6)});
    }

    public static String a(a aVar, com.baidu.location.f.i iVar, Location location, String str, int i) {
        String b;
        StringBuffer stringBuffer = new StringBuffer(1024);
        if (aVar != null) {
            b = b.a().b(aVar);
            if (b != null) {
                stringBuffer.append(b);
            }
        }
        if (iVar != null) {
            b = i == 0 ? iVar.b() : iVar.c();
            if (b != null) {
                stringBuffer.append(b);
            }
        }
        if (location != null) {
            b = (d == 0 || i == 0) ? e.b(location) : e.c(location);
            if (b != null) {
                stringBuffer.append(b);
            }
        }
        boolean z = false;
        if (i == 0) {
            z = true;
        }
        b = c.a().a(z);
        if (b != null) {
            stringBuffer.append(b);
        }
        if (str != null) {
            stringBuffer.append(str);
        }
        Object d = com.baidu.location.c.b.a().d();
        if (!TextUtils.isEmpty(d)) {
            stringBuffer.append("&bc=").append(d);
        }
        if (i == 0) {
            if (aVar != null) {
                b = c.a().a(aVar);
                if (b != null && b.length() + stringBuffer.length() < 750) {
                    stringBuffer.append(b);
                }
            }
            b = stringBuffer.toString();
            if (location != null || iVar == null) {
                q = 3;
            } else {
                try {
                    float speed = location.getSpeed();
                    int i2 = d;
                    int d2 = iVar.d();
                    int a = iVar.a();
                    boolean e = iVar.e();
                    if (speed < aq && ((i2 == 1 || i2 == 0) && (d2 < as || e))) {
                        q = 1;
                    } else if (speed >= ar || (!(i2 == 1 || i2 == 0 || i2 == 3) || (d2 >= at && a <= au))) {
                        q = 3;
                    } else {
                        q = 2;
                    }
                } catch (Exception e2) {
                    q = 3;
                }
            }
            return b;
        }
        if (aVar != null) {
            b = c.a().a(aVar);
            stringBuffer.append(b);
        }
        b = stringBuffer.toString();
        if (location != null) {
        }
        q = 3;
        return b;
    }

    public static String a(File file) {
        if (!file.isFile()) {
            return null;
        }
        byte[] bArr = new byte[1024];
        try {
            MessageDigest instance = MessageDigest.getInstance("MD5");
            FileInputStream fileInputStream = new FileInputStream(file);
            while (true) {
                int read = fileInputStream.read(bArr, 0, 1024);
                if (read != -1) {
                    instance.update(bArr, 0, read);
                } else {
                    fileInputStream.close();
                    return new BigInteger(1, instance.digest()).toString(16);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static boolean a(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService("connectivity");
        if (connectivityManager == null) {
            return false;
        }
        NetworkInfo[] allNetworkInfo = connectivityManager.getAllNetworkInfo();
        if (allNetworkInfo == null) {
            return false;
        }
        for (NetworkInfo state : allNetworkInfo) {
            if (state.getState() == State.CONNECTED) {
                return true;
            }
        }
        return false;
    }

    public static boolean a(BDLocation bDLocation) {
        int locType = bDLocation.getLocType();
        return locType > 100 && locType < 200;
    }

    public static int b(Context context) {
        try {
            return System.getInt(context.getContentResolver(), "airplane_mode_on", 0);
        } catch (Exception e) {
            return 2;
        }
    }

    private static int b(Context context, String str) {
        int i;
        try {
            i = context.checkPermission(str, Process.myPid(), Process.myUid()) == 0 ? 1 : 0;
        } catch (Exception e) {
            i = 1;
        }
        return i == 0 ? 0 : 1;
    }

    public static int b(Object obj, String str, Object... objArr) {
        Class cls = obj.getClass();
        Class[] clsArr = new Class[objArr.length];
        int length = objArr.length;
        for (int i = 0; i < length; i++) {
            clsArr[i] = objArr[i].getClass();
            if (clsArr[i] == Integer.class) {
                clsArr[i] = Integer.TYPE;
            }
        }
        Method declaredMethod = cls.getDeclaredMethod(str, clsArr);
        if (!declaredMethod.isAccessible()) {
            declaredMethod.setAccessible(true);
        }
        return ((Integer) declaredMethod.invoke(obj, objArr)).intValue();
    }

    public static String b() {
        try {
            Enumeration networkInterfaces = NetworkInterface.getNetworkInterfaces();
            while (networkInterfaces.hasMoreElements()) {
                Enumeration inetAddresses = ((NetworkInterface) networkInterfaces.nextElement()).getInetAddresses();
                while (inetAddresses.hasMoreElements()) {
                    InetAddress inetAddress = (InetAddress) inetAddresses.nextElement();
                    if (!inetAddress.isLoopbackAddress() && (inetAddress instanceof Inet4Address)) {
                        byte[] address = inetAddress.getAddress();
                        int i = 0;
                        String str = "";
                        while (i < address.length) {
                            String toHexString = Integer.toHexString(address[i] & 255);
                            if (toHexString.length() == 1) {
                                toHexString = '0' + toHexString;
                            }
                            i++;
                            str = str + toHexString;
                        }
                        return str;
                    }
                }
            }
        } catch (Exception e) {
        }
        return null;
    }

    public static int c(Context context) {
        int i = -1;
        if (VERSION.SDK_INT < 23) {
            return -2;
        }
        try {
            return Secure.getInt(context.getContentResolver(), "location_mode", -1);
        } catch (Exception e) {
            return i;
        }
    }

    public static String c() {
        return av;
    }

    public static String d() {
        return ay;
    }

    public static String d(Context context) {
        int b = b(context, "android.permission.ACCESS_COARSE_LOCATION");
        int b2 = b(context, "android.permission.ACCESS_FINE_LOCATION");
        return "&per=" + b + "|" + b2 + "|" + b(context, "android.permission.READ_PHONE_STATE");
    }

    public static String e() {
        if (!Environment.getExternalStorageState().equals("mounted")) {
            return null;
        }
        try {
            String path = Environment.getExternalStorageDirectory().getPath();
            File file = new File(path + "/baidu/tempdata");
            if (file.exists()) {
                return path;
            }
            file.mkdirs();
            return path;
        } catch (Exception e) {
            return null;
        }
    }

    public static String e(Context context) {
        int type;
        int i = -1;
        if (context != null) {
            try {
                NetworkInfo activeNetworkInfo = ((ConnectivityManager) context.getSystemService("connectivity")).getActiveNetworkInfo();
                if (activeNetworkInfo != null && activeNetworkInfo.isAvailable()) {
                    type = activeNetworkInfo.getType();
                    i = type;
                    return "&netc=" + i;
                }
            } catch (Exception e) {
            }
        }
        type = -1;
        i = type;
        return "&netc=" + i;
    }

    public static String f() {
        String e = e();
        return e == null ? null : e + "/baidu/tempdata";
    }

    public static String g() {
        try {
            File file = new File(f.getServiceContext().getFilesDir() + File.separator + "lldt");
            if (!file.exists()) {
                file.mkdirs();
            }
            return file.getAbsolutePath();
        } catch (Exception e) {
            return null;
        }
    }
}
