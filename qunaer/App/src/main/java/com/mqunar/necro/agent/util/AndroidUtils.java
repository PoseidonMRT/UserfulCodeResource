package com.mqunar.necro.agent.util;

import android.annotation.TargetApi;
import android.content.Context;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Build.VERSION;
import android.provider.Settings.Secure;
import android.support.v4.os.EnvironmentCompat;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import com.iflytek.aiui.AIUIConstant;
import com.mqunar.necro.agent.logging.AgentLog;
import com.mqunar.necro.agent.logging.AgentLogManager;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.lang.reflect.Method;
import java.security.MessageDigest;
import java.text.MessageFormat;
import java.util.UUID;

public class AndroidUtils {
    private static final String ANDROID = "Android";
    private static final Uri PREFERRED_APN_URI = Uri.parse("content://telephony/carriers/preferapn");
    private static String imei = null;
    private static AgentLog log = AgentLogManager.getAgentLog();
    private static String macSerial = "";
    private static String sn = EnvironmentCompat.MEDIA_UNKNOWN;

    public static String getIMEI(Context context) {
        if (!TextUtils.isEmpty(imei)) {
            return imei;
        }
        try {
            imei = ((TelephonyManager) context.getSystemService("phone")).getDeviceId();
        } catch (Throwable th) {
        }
        return imei;
    }

    public static String getSN() {
        if (!EnvironmentCompat.MEDIA_UNKNOWN.equals(sn)) {
            return sn;
        }
        try {
            if (VERSION.SDK_INT >= 9) {
                sn = new Object() {
                    @TargetApi(9)
                    public String getSerial() {
                        return Build.SERIAL;
                    }
                }.getSerial();
            }
            if (EnvironmentCompat.MEDIA_UNKNOWN.equals(sn)) {
                try {
                    Method declaredMethod = Class.forName("android.os.SystemProperties").getDeclaredMethod("get", new Class[]{String.class, String.class});
                    sn = (String) declaredMethod.invoke(null, new Object[]{"ro.serialno", EnvironmentCompat.MEDIA_UNKNOWN});
                    if (EnvironmentCompat.MEDIA_UNKNOWN.equals(sn)) {
                        sn = (String) declaredMethod.invoke(null, new Object[]{"gsm.device.sn", EnvironmentCompat.MEDIA_UNKNOWN});
                    }
                    if (EnvironmentCompat.MEDIA_UNKNOWN.equals(sn)) {
                        sn = (String) declaredMethod.invoke(null, new Object[]{"ril.serialnumber", EnvironmentCompat.MEDIA_UNKNOWN});
                    }
                } catch (Exception e) {
                }
            }
            if (EnvironmentCompat.MEDIA_UNKNOWN.equals(sn)) {
                return "";
            }
        } catch (Throwable th) {
        }
        return sn;
    }

    public static String getMac() {
        if (!TextUtils.isEmpty(macSerial)) {
            return macSerial;
        }
        String str = "";
        try {
            LineNumberReader lineNumberReader = new LineNumberReader(new InputStreamReader(Runtime.getRuntime().exec("cat /sys/class/net/wlan0/address ").getInputStream()));
            while (str != null) {
                str = lineNumberReader.readLine();
                if (str != null) {
                    macSerial = str.trim();
                    break;
                }
            }
        } catch (Throwable th) {
        }
        return macSerial;
    }

    public static String getADID(Context context) {
        try {
            String string = Secure.getString(context.getContentResolver(), "android_id");
            if ("9774d56d682e549c".equalsIgnoreCase(string) || TextUtils.isEmpty(string)) {
                return getIMEI(context);
            }
            return string;
        } catch (Throwable th) {
            return "";
        }
    }

    public static String getApnName(Context context) {
        String str = "";
        try {
            Cursor query = context.getContentResolver().query(PREFERRED_APN_URI, new String[]{"_id", "apn", "type"}, null, null, null);
            String str2;
            if (query != null) {
                query.moveToFirst();
                if (query.getCount() == 0 || query.isAfterLast()) {
                    str2 = str;
                } else {
                    str2 = query.getString(query.getColumnIndex("apn"));
                }
                query.close();
                return str2;
            }
            query = context.getContentResolver().query(PREFERRED_APN_URI, null, null, null, null);
            if (query == null) {
                return str;
            }
            query.moveToFirst();
            str2 = query.getString(query.getColumnIndex(AIUIConstant.USER));
            query.close();
            return str2;
        } catch (Exception e) {
            try {
                return ((ConnectivityManager) context.getSystemService("connectivity")).getActiveNetworkInfo().getExtraInfo();
            } catch (Exception e2) {
                return "";
            }
        }
    }

    public static String getSimOperator(Context context) {
        try {
            return ((TelephonyManager) context.getSystemService("phone")).getSimOperator();
        } catch (Throwable th) {
            return "";
        }
    }

    public static boolean isNetworkConnected(Context context) {
        if (context != null) {
            NetworkInfo activeNetworkInfo = ((ConnectivityManager) context.getSystemService("connectivity")).getActiveNetworkInfo();
            if (activeNetworkInfo != null) {
                return activeNetworkInfo.isAvailable();
            }
        }
        return false;
    }

    public static String carrierNameFromContext(Context context) {
        try {
            NetworkInfo networkInfo = getNetworkInfo(context);
            if (!isConnected(networkInfo)) {
                return "none";
            }
            if (isWan(networkInfo)) {
                return carrierNameFromTelephonyManager(context);
            }
            if (isWifi(networkInfo)) {
                return "wifi";
            }
            log.warning(MessageFormat.format("Unknown network type: {0} [{1}]", new Object[]{networkInfo.getTypeName(), Integer.valueOf(networkInfo.getType())}));
            return EnvironmentCompat.MEDIA_UNKNOWN;
        } catch (SecurityException e) {
            return EnvironmentCompat.MEDIA_UNKNOWN;
        }
    }

    public static String wanType(Context context) {
        try {
            NetworkInfo networkInfo = getNetworkInfo(context);
            if (!isConnected(networkInfo)) {
                return "none";
            }
            if (isWifi(networkInfo)) {
                return "wifi";
            }
            return isWan(networkInfo) ? connectionNameFromNetworkSubtype(networkInfo.getSubtype()) : EnvironmentCompat.MEDIA_UNKNOWN;
        } catch (SecurityException e) {
            return EnvironmentCompat.MEDIA_UNKNOWN;
        }
    }

    private static boolean isConnected(NetworkInfo networkInfo) {
        return networkInfo != null && networkInfo.isConnected();
    }

    private static boolean isWan(NetworkInfo networkInfo) {
        switch (networkInfo.getType()) {
            case 0:
            case 2:
            case 3:
            case 4:
            case 5:
                return true;
            default:
                return false;
        }
    }

    private static boolean isWifi(NetworkInfo networkInfo) {
        switch (networkInfo.getType()) {
            case 1:
            case 6:
            case 7:
            case 9:
                return true;
            default:
                return false;
        }
    }

    private static NetworkInfo getNetworkInfo(Context context) {
        try {
            return ((ConnectivityManager) context.getSystemService("connectivity")).getActiveNetworkInfo();
        } catch (SecurityException e) {
            log.warning("Cannot determine network state. Enable android.permission.ACCESS_NETWORK_STATE in your manifest.");
            throw e;
        }
    }

    private static String carrierNameFromTelephonyManager(Context context) {
        String simOperator = ((TelephonyManager) context.getSystemService("phone")).getSimOperator();
        if (simOperator == null || simOperator.trim().isEmpty()) {
            simOperator = EnvironmentCompat.MEDIA_UNKNOWN;
        }
        Object obj = (Build.PRODUCT.equals("google_sdk") || Build.PRODUCT.equals("sdk") || Build.PRODUCT.equals("sdk_x86") || Build.FINGERPRINT.startsWith("generic")) ? 1 : null;
        return (!simOperator.equals(ANDROID) || obj == null) ? simOperator : "wifi";
    }

    private static String connectionNameFromNetworkSubtype(int i) {
        switch (i) {
            case 1:
                return "GPRS";
            case 2:
                return "EDGE";
            case 3:
                return "UMTS";
            case 4:
                return "CDMA";
            case 5:
                return "EVDO rev 0";
            case 6:
                return "EVDO rev A";
            case 7:
                return "1xRTT";
            case 8:
                return "HSDPA";
            case 9:
                return "HSUPA";
            case 10:
                return "HSPA";
            case 11:
                return "IDEN";
            case 12:
                return "EVDO rev B";
            case 13:
                return "LTE";
            case 14:
                return "HRPD";
            case 15:
                return "HSPAP";
            default:
                return EnvironmentCompat.MEDIA_UNKNOWN;
        }
    }

    public static String stringToMD5(String str) {
        try {
            byte[] digest = MessageDigest.getInstance("MD5").digest(str.getBytes("UTF-8"));
            StringBuilder stringBuilder = new StringBuilder(digest.length * 2);
            for (byte b : digest) {
                if ((b & 255) < 16) {
                    stringBuilder.append("0");
                }
                stringBuilder.append(Integer.toHexString(b & 255));
            }
            return stringBuilder.toString();
        } catch (Throwable th) {
            return "";
        }
    }

    public static String getTraceId(Context context) {
        String stringToMD5 = stringToMD5(UUID.randomUUID().toString() + getIMEI(context));
        if (stringToMD5 == null) {
            return "";
        }
        return stringToMD5;
    }
}
