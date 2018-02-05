package org.acra.collector;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.NetworkInfo.State;
import android.support.annotation.NonNull;
import org.acra.util.PackageManagerWrapper;

public class l {
    public static String a(@NonNull Context context) {
        if (!new PackageManagerWrapper(context).hasPermission("android.permission.ACCESS_NETWORK_STATE")) {
            return "";
        }
        switch (b(context)) {
            case 0:
                return "无网络";
            case 1:
                return "wifi";
            case 2:
                return "2G";
            case 3:
                return "3G";
            case 4:
                return "4G";
            case 5:
                return "其他移动网络";
            default:
                return "获取失败";
        }
    }

    private static int b(@NonNull Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService("connectivity");
        if (connectivityManager == null) {
            return 0;
        }
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        if (activeNetworkInfo == null || !activeNetworkInfo.isAvailable()) {
            return 0;
        }
        NetworkInfo networkInfo = connectivityManager.getNetworkInfo(1);
        if (networkInfo != null) {
            State state = networkInfo.getState();
            if (state != null && (state == State.CONNECTED || state == State.CONNECTING)) {
                return 1;
            }
        }
        NetworkInfo networkInfo2 = connectivityManager.getNetworkInfo(0);
        if (networkInfo2 != null) {
            State state2 = networkInfo2.getState();
            String subtypeName = networkInfo2.getSubtypeName();
            if (state2 != null && (state2 == State.CONNECTED || state2 == State.CONNECTING)) {
                switch (activeNetworkInfo.getSubtype()) {
                    case 1:
                    case 2:
                    case 4:
                    case 7:
                    case 11:
                        return 2;
                    case 3:
                    case 5:
                    case 6:
                    case 8:
                    case 9:
                    case 10:
                    case 12:
                    case 14:
                    case 15:
                        return 3;
                    case 13:
                        return 4;
                    default:
                        if (subtypeName.equalsIgnoreCase("TD-SCDMA") || subtypeName.equalsIgnoreCase("WCDMA") || subtypeName.equalsIgnoreCase("CDMA2000")) {
                            return 3;
                        }
                        return 5;
                }
            }
        }
        return 0;
    }
}
