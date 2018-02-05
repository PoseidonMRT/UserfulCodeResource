package com.iflytek.cloud.thirdparty;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Build.VERSION;
import android.support.v4.os.EnvironmentCompat;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.WindowManager;
import java.io.BufferedReader;
import java.io.FileReader;
import java.security.MessageDigest;
import java.util.Locale;
import java.util.Random;

public final class h {
    public static String a() {
        return a(Build.MODEL);
    }

    public static String a(Context context) {
        String deviceId;
        try {
            deviceId = ((TelephonyManager) context.getSystemService("phone")).getDeviceId();
        } catch (Exception e) {
            deviceId = "";
        }
        return a(deviceId);
    }

    public static String a(Context context, String str) {
        String valueOf;
        try {
            valueOf = String.valueOf(b(System.currentTimeMillis() + str + a(context) + g(context) + new Random().nextInt(10)).toCharArray(), 8, 16);
        } catch (Exception e) {
            valueOf = "";
        }
        return c(valueOf);
    }

    protected static String a(String str) {
        if (str == null) {
            return "";
        }
        if (str.length() > 901) {
            str = str.substring(0, 900);
        }
        return str.replace("\\", "").replace("|", "");
    }

    protected static boolean a(String str, int i) {
        return str != null && str.length() > i;
    }

    public static String b() {
        return a(Build.MANUFACTURER);
    }

    public static String b(Context context) {
        try {
            String a = a(context);
            if (a != null && a.length() > 6) {
                return String.valueOf(b(a).toCharArray(), 7, 18);
            }
            a = g(context);
            if (a != null && a.length() >= 9) {
                return String.valueOf(b(a).toCharArray(), 7, 18);
            }
            a = c(context);
            return (a == null || a.length() == 0) ? String.valueOf(b(a(Build.MODEL)).toCharArray(), 7, 18) : String.valueOf(b(a).toCharArray(), 7, 18);
        } catch (Exception e) {
            return "";
        }
    }

    private static String b(String str) {
        int i = 0;
        try {
            MessageDigest instance = MessageDigest.getInstance("MD5");
            char[] toCharArray = str.toCharArray();
            byte[] bArr = new byte[toCharArray.length];
            for (int i2 = 0; i2 < toCharArray.length; i2++) {
                bArr[i2] = (byte) toCharArray[i2];
            }
            byte[] digest = instance.digest(bArr);
            StringBuffer stringBuffer = new StringBuffer();
            while (i < digest.length) {
                int i3 = digest[i] & 255;
                if (i3 < 16) {
                    stringBuffer.append("0");
                }
                stringBuffer.append(Integer.toHexString(i3));
                i++;
            }
            return stringBuffer.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    private static boolean b(Context context, String str) {
        try {
            return context.getPackageManager().checkPermission(str, context.getPackageName()) == 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static String c() {
        return a("android " + VERSION.RELEASE);
    }

    public static String c(Context context) {
        String subscriberId;
        String str = "";
        try {
            TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService("phone");
            if (telephonyManager != null) {
                subscriberId = telephonyManager.getSubscriberId();
                subscriberId = subscriberId != null ? subscriberId.trim() : str;
                c.a("MobileUtils", subscriberId);
            } else {
                subscriberId = str;
            }
        } catch (Exception e) {
            subscriberId = "";
            Log.i("MobileUtils", "can't not read imsi");
        }
        return c(subscriberId);
    }

    private static String c(String str) {
        return TextUtils.isEmpty(str) ? "" : str;
    }

    public static int d(Context context) {
        try {
            DisplayMetrics displayMetrics = new DisplayMetrics();
            ((WindowManager) context.getSystemService("window")).getDefaultDisplay().getMetrics(displayMetrics);
            return displayMetrics.widthPixels;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    public static String d() {
        String country;
        try {
            country = Locale.getDefault().getCountry();
        } catch (Exception e) {
            country = "";
        }
        return c(country);
    }

    public static int e(Context context) {
        try {
            DisplayMetrics displayMetrics = new DisplayMetrics();
            ((WindowManager) context.getSystemService("window")).getDefaultDisplay().getMetrics(displayMetrics);
            return displayMetrics.heightPixels;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    public static String e() {
        String language;
        try {
            language = Locale.getDefault().getLanguage();
        } catch (Exception e) {
            language = "";
        }
        return c(language);
    }

    public static int f(Context context) {
        try {
            DisplayMetrics displayMetrics = new DisplayMetrics();
            ((WindowManager) context.getSystemService("window")).getDefaultDisplay().getMetrics(displayMetrics);
            return displayMetrics.densityDpi;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static java.lang.String f() {
        /*
        r0 = "";
        r1 = 0;
        r2 = 2;
        r2 = new java.lang.String[r2];	 Catch:{ Exception -> 0x0047 }
        r3 = 0;
        r4 = "/system/bin/cat";
        r2[r3] = r4;	 Catch:{ Exception -> 0x0047 }
        r3 = 1;
        r4 = "/sys/devices/system/cpu/cpu0/cpufreq/cpuinfo_max_freq";
        r2[r3] = r4;	 Catch:{ Exception -> 0x0047 }
        r3 = new java.lang.ProcessBuilder;	 Catch:{ Exception -> 0x0047 }
        r3.<init>(r2);	 Catch:{ Exception -> 0x0047 }
        r2 = r3.start();	 Catch:{ Exception -> 0x0047 }
        r1 = r2.getInputStream();	 Catch:{ Exception -> 0x0047 }
        r2 = 24;
        r2 = new byte[r2];	 Catch:{ Exception -> 0x0047 }
    L_0x0021:
        r3 = r1.read(r2);	 Catch:{ Exception -> 0x0047 }
        r4 = -1;
        if (r3 != r4) goto L_0x0030;
    L_0x0028:
        r1.close();	 Catch:{ Exception -> 0x0060 }
    L_0x002b:
        r0 = r0.trim();
        return r0;
    L_0x0030:
        r3 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x0047 }
        r0 = java.lang.String.valueOf(r0);	 Catch:{ Exception -> 0x0047 }
        r3.<init>(r0);	 Catch:{ Exception -> 0x0047 }
        r0 = new java.lang.String;	 Catch:{ Exception -> 0x0047 }
        r0.<init>(r2);	 Catch:{ Exception -> 0x0047 }
        r0 = r3.append(r0);	 Catch:{ Exception -> 0x0047 }
        r0 = r0.toString();	 Catch:{ Exception -> 0x0047 }
        goto L_0x0021;
    L_0x0047:
        r0 = move-exception;
        r0.printStackTrace();	 Catch:{ all -> 0x0056 }
        r0 = "";
        r1.close();	 Catch:{ Exception -> 0x0051 }
        goto L_0x002b;
    L_0x0051:
        r1 = move-exception;
        r1.printStackTrace();
        goto L_0x002b;
    L_0x0056:
        r0 = move-exception;
        r1.close();	 Catch:{ Exception -> 0x005b }
    L_0x005a:
        throw r0;
    L_0x005b:
        r1 = move-exception;
        r1.printStackTrace();
        goto L_0x005a;
    L_0x0060:
        r1 = move-exception;
        r1.printStackTrace();
        goto L_0x002b;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.iflytek.cloud.thirdparty.h.f():java.lang.String");
    }

    public static String g() {
        Exception e;
        Throwable th;
        FileReader fileReader;
        try {
            fileReader = new FileReader("/sys/devices/system/cpu/cpu0/cpufreq/scaling_cur_freq");
            try {
                String trim = new BufferedReader(fileReader).readLine().trim();
                try {
                    fileReader.close();
                    return trim;
                } catch (Exception e2) {
                    e2.printStackTrace();
                    return trim;
                }
            } catch (Exception e3) {
                e = e3;
                try {
                    e.printStackTrace();
                    try {
                        fileReader.close();
                    } catch (Exception e4) {
                        e4.printStackTrace();
                    }
                    return "";
                } catch (Throwable th2) {
                    th = th2;
                    try {
                        fileReader.close();
                    } catch (Exception e22) {
                        e22.printStackTrace();
                    }
                    throw th;
                }
            }
        } catch (Exception e5) {
            e4 = e5;
            fileReader = null;
            e4.printStackTrace();
            fileReader.close();
            return "";
        } catch (Throwable th3) {
            th = th3;
            fileReader = null;
            fileReader.close();
            throw th;
        }
    }

    public static String g(Context context) {
        try {
            String macAddress;
            if (b(context, "android.permission.ACCESS_WIFI_STATE")) {
                macAddress = ((WifiManager) context.getSystemService("wifi")).getConnectionInfo().getMacAddress();
                if (macAddress == null || macAddress.equals("")) {
                    macAddress = EnvironmentCompat.MEDIA_UNKNOWN;
                }
            } else {
                Log.w("MobileUtils", "Could not read MAC, forget to include ACCESS_WIFI_STATE permission?");
                macAddress = EnvironmentCompat.MEDIA_UNKNOWN;
            }
            return c(macAddress);
        } catch (Throwable e) {
            Log.w("MobileUtils", "Could not read MAC, forget to include ACCESS_WIFI_STATE permission?", e);
            return EnvironmentCompat.MEDIA_UNKNOWN;
        }
    }

    public static String h() {
        FileReader fileReader;
        BufferedReader bufferedReader;
        Exception e;
        Throwable th;
        FileReader fileReader2 = null;
        try {
            fileReader = new FileReader("/proc/meminfo");
            try {
                bufferedReader = new BufferedReader(fileReader, 8192);
                try {
                    String readLine = bufferedReader.readLine();
                    if (readLine != null) {
                        readLine = readLine.substring(readLine.indexOf(":") + 2, readLine.indexOf("k") - 1).trim();
                        c.a("MobileUtils", readLine);
                        readLine = c(readLine);
                        try {
                            fileReader.close();
                            bufferedReader.close();
                            return readLine;
                        } catch (Exception e2) {
                            e2.printStackTrace();
                            return readLine;
                        }
                    }
                    try {
                        fileReader.close();
                        bufferedReader.close();
                    } catch (Exception e3) {
                        e3.printStackTrace();
                    }
                    return "";
                } catch (Exception e4) {
                    e3 = e4;
                    fileReader2 = fileReader;
                    try {
                        e3.printStackTrace();
                        try {
                            fileReader2.close();
                            bufferedReader.close();
                        } catch (Exception e32) {
                            e32.printStackTrace();
                        }
                        return "";
                    } catch (Throwable th2) {
                        th = th2;
                        fileReader = fileReader2;
                        try {
                            fileReader.close();
                            bufferedReader.close();
                        } catch (Exception e22) {
                            e22.printStackTrace();
                        }
                        throw th;
                    }
                } catch (Throwable th3) {
                    th = th3;
                    fileReader.close();
                    bufferedReader.close();
                    throw th;
                }
            } catch (Exception e5) {
                e32 = e5;
                bufferedReader = null;
                fileReader2 = fileReader;
                e32.printStackTrace();
                fileReader2.close();
                bufferedReader.close();
                return "";
            } catch (Throwable th4) {
                th = th4;
                bufferedReader = null;
                fileReader.close();
                bufferedReader.close();
                throw th;
            }
        } catch (Exception e6) {
            e32 = e6;
            bufferedReader = null;
            e32.printStackTrace();
            fileReader2.close();
            bufferedReader.close();
            return "";
        } catch (Throwable th5) {
            th = th5;
            bufferedReader = null;
            fileReader = null;
            fileReader.close();
            bufferedReader.close();
            throw th;
        }
    }

    public static String h(Context context) {
        try {
            Object string = context.getSharedPreferences("lxdMoblieAgent_sys_config", 0).getString("MOBILE_APPKEY", "");
            if (!string.equals("")) {
                return string;
            }
            ApplicationInfo applicationInfo = context.getPackageManager().getApplicationInfo(context.getPackageName(), 128);
            if (applicationInfo != null) {
                string = applicationInfo.metaData.getString("MOBILE_APPKEY");
            }
            if (TextUtils.isEmpty(string)) {
                Log.e("MobileUtils", "the appkey is empty,please init datau.sdk");
            }
            return TextUtils.isEmpty(string) ? "00000" : string;
        } catch (Exception e) {
            e.printStackTrace();
            return "00000";
        }
    }

    public static String i(Context context) {
        try {
            String string = context.getSharedPreferences("lxdMoblieAgent_sys_config", 0).getString("MOBILE_CHANNEL", "");
            if (!string.equals("")) {
                return a(string);
            }
            ApplicationInfo applicationInfo = context.getPackageManager().getApplicationInfo(context.getPackageName(), 128);
            if (applicationInfo != null) {
                string = applicationInfo.metaData.getString("MOBILE_CHANNEL");
                if (string == null) {
                    Log.w("MobileUtils", "Could not read MOBILE_CHANNEL meta-data from AndroidManifest.xml.");
                    string = "";
                }
            }
            return a(string);
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public static String j(Context context) {
        String packageName;
        try {
            packageName = context.getPackageName();
        } catch (Exception e) {
            packageName = "";
        }
        return c(packageName);
    }

    public static String k(Context context) {
        String networkOperatorName;
        try {
            networkOperatorName = ((TelephonyManager) context.getSystemService("phone")).getNetworkOperatorName();
        } catch (Exception e) {
            e.printStackTrace();
            networkOperatorName = "";
        }
        return c(networkOperatorName);
    }

    public static String l(Context context) {
        try {
            if (!b(context, "android.permission.ACCESS_NETWORK_STATE")) {
                return EnvironmentCompat.MEDIA_UNKNOWN;
            }
            NetworkInfo activeNetworkInfo = ((ConnectivityManager) context.getSystemService("connectivity")).getActiveNetworkInfo();
            if (activeNetworkInfo == null) {
                return EnvironmentCompat.MEDIA_UNKNOWN;
            }
            if (activeNetworkInfo.getType() == 1) {
                return "wifi";
            }
            String extraInfo = activeNetworkInfo.getExtraInfo();
            if (extraInfo == null) {
                return EnvironmentCompat.MEDIA_UNKNOWN;
            }
            c.a("MobileUtils", "net type:" + extraInfo);
            return extraInfo.trim();
        } catch (Throwable e) {
            Log.w("MobileUtils", "Could not read ACCESSPOINT, forget to include ACCESS_NETSTATE_STATE permission?", e);
            return EnvironmentCompat.MEDIA_UNKNOWN;
        }
    }

    public static int m(Context context) {
        try {
            return context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionCode;
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    public static String n(Context context) {
        String str;
        try {
            str = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
        } catch (Exception e) {
            e.printStackTrace();
            str = "";
        }
        return c(str);
    }

    public static long[] o(Context context) {
        BufferedReader bufferedReader;
        long j;
        FileReader fileReader;
        FileReader fileReader2;
        BufferedReader bufferedReader2;
        long parseLong;
        long[] jArr;
        Throwable th;
        long j2 = 0;
        BufferedReader bufferedReader3 = null;
        int q = q(context);
        String str = "proc/uid_stat/" + q + "/tcp_rcv";
        String str2 = "proc/uid_stat/" + q + "/tcp_snd";
        FileReader fileReader3;
        try {
            fileReader3 = new FileReader(str);
            try {
                bufferedReader = new BufferedReader(fileReader3, 500);
                try {
                    long parseLong2 = Long.parseLong(bufferedReader.readLine().toString().trim());
                    try {
                        fileReader3.close();
                        bufferedReader.close();
                        bufferedReader = null;
                        j = parseLong2;
                    } catch (Exception e) {
                        e.printStackTrace();
                        fileReader = fileReader3;
                        j = parseLong2;
                    }
                } catch (Exception e2) {
                    if (fileReader3 != null) {
                        try {
                            fileReader3.close();
                            bufferedReader.close();
                            j = 0;
                            bufferedReader = null;
                        } catch (Exception e3) {
                            e3.printStackTrace();
                            fileReader = fileReader3;
                            j = 0;
                        }
                    } else {
                        fileReader = fileReader3;
                        j = 0;
                    }
                    fileReader2 = new FileReader(str2);
                    try {
                        bufferedReader2 = new BufferedReader(fileReader2, 500);
                        parseLong = Long.parseLong(bufferedReader2.readLine().toString().trim());
                        try {
                            fileReader2.close();
                            bufferedReader2.close();
                        } catch (Exception e4) {
                            e4.printStackTrace();
                        }
                    } catch (Exception e5) {
                        fileReader = fileReader2;
                        if (fileReader != null) {
                            parseLong = 0;
                        } else {
                            try {
                                fileReader.close();
                                bufferedReader.close();
                                parseLong = 0;
                            } catch (Exception e6) {
                                e6.printStackTrace();
                                parseLong = 0;
                            }
                        }
                        c.a("MobileUtils", "indicate flow: " + j + "  &  " + parseLong);
                        jArr = new long[2];
                        if (j < 0) {
                            j = 0;
                        }
                        jArr[0] = j;
                        if (parseLong >= 0) {
                            j2 = parseLong;
                        }
                        jArr[1] = j2;
                        return jArr;
                    } catch (Throwable th2) {
                        bufferedReader2 = bufferedReader;
                        th = th2;
                        if (fileReader2 != null) {
                            try {
                                fileReader2.close();
                                bufferedReader2.close();
                            } catch (Exception e32) {
                                e32.printStackTrace();
                            }
                        }
                        throw th;
                    }
                    c.a("MobileUtils", "indicate flow: " + j + "  &  " + parseLong);
                    jArr = new long[2];
                    if (j < 0) {
                        j = 0;
                    }
                    jArr[0] = j;
                    if (parseLong >= 0) {
                        j2 = parseLong;
                    }
                    jArr[1] = j2;
                    return jArr;
                } catch (Throwable th22) {
                    Throwable th3 = th22;
                    bufferedReader3 = bufferedReader;
                    th = th3;
                    if (fileReader3 != null) {
                        try {
                            fileReader3.close();
                            bufferedReader3.close();
                        } catch (Exception e322) {
                            e322.printStackTrace();
                        }
                    }
                    throw th;
                }
            } catch (Exception e7) {
                bufferedReader = null;
                if (fileReader3 != null) {
                    fileReader = fileReader3;
                    j = 0;
                } else {
                    fileReader3.close();
                    bufferedReader.close();
                    j = 0;
                    bufferedReader = null;
                }
                fileReader2 = new FileReader(str2);
                bufferedReader2 = new BufferedReader(fileReader2, 500);
                parseLong = Long.parseLong(bufferedReader2.readLine().toString().trim());
                fileReader2.close();
                bufferedReader2.close();
                c.a("MobileUtils", "indicate flow: " + j + "  &  " + parseLong);
                jArr = new long[2];
                if (j < 0) {
                    j = 0;
                }
                jArr[0] = j;
                if (parseLong >= 0) {
                    j2 = parseLong;
                }
                jArr[1] = j2;
                return jArr;
            } catch (Throwable th4) {
                th = th4;
                if (fileReader3 != null) {
                    fileReader3.close();
                    bufferedReader3.close();
                }
                throw th;
            }
        } catch (Exception e8) {
            bufferedReader = null;
            fileReader3 = null;
            if (fileReader3 != null) {
                fileReader3.close();
                bufferedReader.close();
                j = 0;
                bufferedReader = null;
            } else {
                fileReader = fileReader3;
                j = 0;
            }
            fileReader2 = new FileReader(str2);
            bufferedReader2 = new BufferedReader(fileReader2, 500);
            parseLong = Long.parseLong(bufferedReader2.readLine().toString().trim());
            fileReader2.close();
            bufferedReader2.close();
            c.a("MobileUtils", "indicate flow: " + j + "  &  " + parseLong);
            jArr = new long[2];
            if (j < 0) {
                j = 0;
            }
            jArr[0] = j;
            if (parseLong >= 0) {
                j2 = parseLong;
            }
            jArr[1] = j2;
            return jArr;
        } catch (Throwable th5) {
            th = th5;
            fileReader3 = null;
            if (fileReader3 != null) {
                fileReader3.close();
                bufferedReader3.close();
            }
            throw th;
        }
        try {
            fileReader2 = new FileReader(str2);
            bufferedReader2 = new BufferedReader(fileReader2, 500);
            try {
                parseLong = Long.parseLong(bufferedReader2.readLine().toString().trim());
                fileReader2.close();
                bufferedReader2.close();
            } catch (Exception e9) {
                bufferedReader = bufferedReader2;
                fileReader = fileReader2;
                if (fileReader != null) {
                    fileReader.close();
                    bufferedReader.close();
                    parseLong = 0;
                } else {
                    parseLong = 0;
                }
                c.a("MobileUtils", "indicate flow: " + j + "  &  " + parseLong);
                jArr = new long[2];
                if (j < 0) {
                    j = 0;
                }
                jArr[0] = j;
                if (parseLong >= 0) {
                    j2 = parseLong;
                }
                jArr[1] = j2;
                return jArr;
            } catch (Throwable th6) {
                th = th6;
                if (fileReader2 != null) {
                    fileReader2.close();
                    bufferedReader2.close();
                }
                throw th;
            }
        } catch (Exception e10) {
            if (fileReader != null) {
                fileReader.close();
                bufferedReader.close();
                parseLong = 0;
            } else {
                parseLong = 0;
            }
            c.a("MobileUtils", "indicate flow: " + j + "  &  " + parseLong);
            jArr = new long[2];
            if (j < 0) {
                j = 0;
            }
            jArr[0] = j;
            if (parseLong >= 0) {
                j2 = parseLong;
            }
            jArr[1] = j2;
            return jArr;
        } catch (Throwable th7) {
            bufferedReader2 = bufferedReader;
            fileReader2 = fileReader;
            th = th7;
            if (fileReader2 != null) {
                fileReader2.close();
                bufferedReader2.close();
            }
            throw th;
        }
        c.a("MobileUtils", "indicate flow: " + j + "  &  " + parseLong);
        jArr = new long[2];
        if (j < 0) {
            j = 0;
        }
        jArr[0] = j;
        if (parseLong >= 0) {
            j2 = parseLong;
        }
        jArr[1] = j2;
        return jArr;
    }

    public static String p(Context context) {
        String str = "";
        try {
            NetworkInfo activeNetworkInfo = ((ConnectivityManager) context.getSystemService("connectivity")).getActiveNetworkInfo();
            if (activeNetworkInfo == null || !activeNetworkInfo.isConnected()) {
                return "";
            }
            String typeName = activeNetworkInfo.getTypeName();
            return typeName.equalsIgnoreCase("WIFI") ? "WIFI" : typeName.equalsIgnoreCase("MOBILE") ? new StringBuilder(String.valueOf(((TelephonyManager) context.getSystemService("phone")).getNetworkType())).toString() : str;
        } catch (Exception e) {
            return "";
        }
    }

    private static int q(Context context) {
        try {
            return context.getPackageManager().getApplicationInfo(context.getPackageName(), 1).uid;
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }
}
