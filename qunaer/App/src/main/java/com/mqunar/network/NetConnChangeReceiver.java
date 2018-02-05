package com.mqunar.network;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.NetworkInfo.State;
import android.telephony.TelephonyManager;
import android.widget.Toast;
import java.util.List;

public class NetConnChangeReceiver extends BroadcastReceiver {
    public static boolean netGetted;
    public static boolean wifi;

    public static boolean isNetworkAvailable(Context context) {
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

    public static void dealNetworkInfo(Context context) {
        NetworkInfo activeNetworkInfo;
        try {
            activeNetworkInfo = ((ConnectivityManager) context.getSystemService("connectivity")).getActiveNetworkInfo();
        } catch (Exception e) {
            activeNetworkInfo = null;
        }
        if (activeNetworkInfo != null) {
            boolean z;
            if (activeNetworkInfo.getType() == 1) {
                z = true;
            } else {
                z = false;
            }
            wifi = z;
        } else {
            wifi = false;
        }
        netGetted = true;
    }

    public static boolean isNetwork2GClass(Context context) {
        NetworkInfo activeNetworkInfo;
        try {
            activeNetworkInfo = ((ConnectivityManager) context.getSystemService("connectivity")).getActiveNetworkInfo();
        } catch (Exception e) {
            activeNetworkInfo = null;
        }
        if (!(activeNetworkInfo == null || activeNetworkInfo.getType() == 1)) {
            TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService("phone");
            if (telephonyManager != null) {
                if (a(telephonyManager.getNetworkType())) {
                    return true;
                }
                return false;
            }
        }
        return false;
    }

    private static boolean a(int i) {
        switch (i) {
            case 1:
            case 2:
            case 4:
            case 7:
            case 11:
                return true;
            default:
                return false;
        }
    }

    public void onReceive(Context context, Intent intent) {
        dealNetworkInfo(context);
        ActivityManager activityManager = (ActivityManager) context.getSystemService("activity");
        if (activityManager != null) {
            List runningTasks;
            try {
                runningTasks = activityManager.getRunningTasks(1);
            } catch (SecurityException e) {
                runningTasks = null;
            }
            if (r0 != null && r0.size() > 0) {
                int i;
                for (RunningTaskInfo runningTaskInfo : r0) {
                    if (!runningTaskInfo.topActivity.getPackageName().equals(context.getPackageName())) {
                        if (runningTaskInfo.baseActivity.getPackageName().equals(context.getPackageName())) {
                        }
                    }
                    i = 1;
                }
                i = 0;
                if (i == 0) {
                    return;
                }
                if (wifi) {
                    Toast.makeText(context, "当前网络已切换至wlan模式", 0).show();
                } else {
                    Toast.makeText(context, "当前网络已切换至GPRS/3G模式", 0).show();
                }
            }
        }
    }
}
