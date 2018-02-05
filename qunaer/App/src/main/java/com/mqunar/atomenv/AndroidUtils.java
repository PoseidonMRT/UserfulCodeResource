package com.mqunar.atomenv;

import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Build.VERSION;
import android.provider.Settings.Secure;
import android.support.v4.os.EnvironmentCompat;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import com.iflytek.aiui.AIUIConstant;
import com.mqunar.core.basectx.application.QApplication;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.lang.reflect.Method;

public class AndroidUtils {
    public static final Uri PREFERRED_APN_URI = Uri.parse("content://telephony/carriers/preferapn");

    public static String getIMEI() {
        try {
            return ((TelephonyManager) QApplication.getContext().getSystemService("phone")).getDeviceId();
        } catch (Throwable th) {
            return null;
        }
    }

    public static String getSN() {
        String str = EnvironmentCompat.MEDIA_UNKNOWN;
        try {
            String str2;
            if (VERSION.SDK_INT >= 9) {
                str = new a().a();
            }
            if (EnvironmentCompat.MEDIA_UNKNOWN.equals(str)) {
                try {
                    Method declaredMethod = Class.forName("android.os.SystemProperties").getDeclaredMethod("get", new Class[]{String.class, String.class});
                    str2 = (String) declaredMethod.invoke(null, new Object[]{"ro.serialno", EnvironmentCompat.MEDIA_UNKNOWN});
                    try {
                        if (EnvironmentCompat.MEDIA_UNKNOWN.equals(str2)) {
                            str = (String) declaredMethod.invoke(null, new Object[]{"gsm.device.sn", EnvironmentCompat.MEDIA_UNKNOWN});
                        } else {
                            str = str2;
                        }
                        if (EnvironmentCompat.MEDIA_UNKNOWN.equals(str)) {
                            str2 = (String) declaredMethod.invoke(null, new Object[]{"ril.serialnumber", EnvironmentCompat.MEDIA_UNKNOWN});
                            if (EnvironmentCompat.MEDIA_UNKNOWN.equals(str2)) {
                                return str2;
                            }
                            return "";
                        }
                    } catch (Exception e) {
                    }
                } catch (Exception e2) {
                    str2 = str;
                }
            }
            str2 = str;
            try {
                if (EnvironmentCompat.MEDIA_UNKNOWN.equals(str2)) {
                    return str2;
                }
                return "";
            } catch (Throwable th) {
                return str2;
            }
        } catch (Throwable th2) {
            return str;
        }
    }

    public static String getMac() {
        String str = "";
        String str2 = "";
        try {
            LineNumberReader lineNumberReader = new LineNumberReader(new InputStreamReader(Runtime.getRuntime().exec("cat /sys/class/net/wlan0/address ").getInputStream()));
            while (str2 != null) {
                str2 = lineNumberReader.readLine();
                if (str2 != null) {
                    str = str2.trim();
                    break;
                }
            }
        } catch (Throwable th) {
        }
        return str;
    }

    public static String getADID() {
        try {
            String string = Secure.getString(QApplication.getContext().getContentResolver(), "android_id");
            if ("9774d56d682e549c".equalsIgnoreCase(string) || TextUtils.isEmpty(string)) {
                return getIMEI();
            }
            return string;
        } catch (Throwable th) {
            return "";
        }
    }

    public static String getApnName() {
        String str = "";
        try {
            Cursor query = QApplication.getContext().getContentResolver().query(PREFERRED_APN_URI, new String[]{"_id", "apn", "type"}, null, null, null);
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
            query = QApplication.getContext().getContentResolver().query(PREFERRED_APN_URI, null, null, null, null);
            if (query == null) {
                return str;
            }
            query.moveToFirst();
            str2 = query.getString(query.getColumnIndex(AIUIConstant.USER));
            query.close();
            return str2;
        } catch (Exception e) {
            try {
                return ((ConnectivityManager) QApplication.getContext().getSystemService("connectivity")).getActiveNetworkInfo().getExtraInfo();
            } catch (Exception e2) {
                return "";
            }
        }
    }

    public static String getSimOperator() {
        try {
            return ((TelephonyManager) QApplication.getContext().getSystemService("phone")).getSimOperator();
        } catch (Throwable th) {
            return "";
        }
    }
}
