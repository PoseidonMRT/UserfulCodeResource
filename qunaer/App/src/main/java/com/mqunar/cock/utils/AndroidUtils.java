package com.mqunar.cock.utils;

import android.content.Context;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Build.VERSION;
import android.provider.Settings.Secure;
import android.support.v4.os.EnvironmentCompat;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import com.iflytek.aiui.AIUIConstant;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.lang.reflect.Method;

public class AndroidUtils {
    public static final Uri PREFERRED_APN_URI = Uri.parse("content://telephony/carriers/preferapn");
    private static String a = null;
    private static String b = EnvironmentCompat.MEDIA_UNKNOWN;
    private static String c = "";

    public static String getIMEI(Context context) {
        if (!TextUtils.isEmpty(a)) {
            return a;
        }
        try {
            a = ((TelephonyManager) context.getSystemService("phone")).getDeviceId();
        } catch (Throwable th) {
        }
        return a;
    }

    public static String getSN() {
        if (!EnvironmentCompat.MEDIA_UNKNOWN.equals(b)) {
            return b;
        }
        try {
            if (VERSION.SDK_INT >= 9) {
                b = new a().a();
            }
            if (EnvironmentCompat.MEDIA_UNKNOWN.equals(b)) {
                try {
                    Method declaredMethod = Class.forName("android.os.SystemProperties").getDeclaredMethod("get", new Class[]{String.class, String.class});
                    b = (String) declaredMethod.invoke(null, new Object[]{"ro.serialno", EnvironmentCompat.MEDIA_UNKNOWN});
                    if (EnvironmentCompat.MEDIA_UNKNOWN.equals(b)) {
                        b = (String) declaredMethod.invoke(null, new Object[]{"gsm.device.sn", EnvironmentCompat.MEDIA_UNKNOWN});
                    }
                    if (EnvironmentCompat.MEDIA_UNKNOWN.equals(b)) {
                        b = (String) declaredMethod.invoke(null, new Object[]{"ril.serialnumber", EnvironmentCompat.MEDIA_UNKNOWN});
                    }
                } catch (Exception e) {
                }
            }
            if (EnvironmentCompat.MEDIA_UNKNOWN.equals(b)) {
                return "";
            }
        } catch (Throwable th) {
        }
        return b;
    }

    public static String getMac() {
        if (!TextUtils.isEmpty(c)) {
            return c;
        }
        String str = "";
        try {
            LineNumberReader lineNumberReader = new LineNumberReader(new InputStreamReader(Runtime.getRuntime().exec("cat /sys/class/net/wlan0/address ").getInputStream()));
            while (str != null) {
                str = lineNumberReader.readLine();
                if (str != null) {
                    c = str.trim();
                    break;
                }
            }
        } catch (Throwable th) {
        }
        return c;
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
}
