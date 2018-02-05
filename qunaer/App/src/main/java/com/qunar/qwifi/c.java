package com.qunar.qwifi;

import android.content.Context;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.http.Headers;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.text.TextUtils;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;

class c {
    private static WifiManager a = null;

    static LinkedHashMap<String, String> a(Context context) {
        try {
            List scanResults = b(context).getScanResults();
            Collections.sort(scanResults, new d());
            LinkedHashMap<String, String> linkedHashMap = new LinkedHashMap();
            for (int i = 0; i < scanResults.size(); i++) {
                Object obj = "";
                String str = ((ScanResult) scanResults.get(i)).capabilities;
                if (!TextUtils.isEmpty(str)) {
                    if (str.contains("WPA") || str.contains("wpa")) {
                        obj = "wpa";
                    } else if (str.contains("WEP") || str.contains("wep")) {
                        r2 = "wep";
                    } else {
                        r2 = "none";
                    }
                }
                linkedHashMap.put(((ScanResult) scanResults.get(i)).SSID, obj);
            }
            return linkedHashMap;
        } catch (Throwable th) {
            b.a("getNearByWifiList failed : " + th);
            return new LinkedHashMap();
        }
    }

    static WifiConfiguration a(Context context, String str, String str2, String str3) {
        WifiConfiguration wifiConfiguration = new WifiConfiguration();
        wifiConfiguration.allowedAuthAlgorithms.clear();
        wifiConfiguration.allowedGroupCiphers.clear();
        wifiConfiguration.allowedKeyManagement.clear();
        wifiConfiguration.allowedPairwiseCiphers.clear();
        wifiConfiguration.allowedProtocols.clear();
        wifiConfiguration.SSID = "\"".concat(str).concat("\"");
        WifiConfiguration c = c(context, str);
        if (c != null) {
            b(context).removeNetwork(c.networkId);
        }
        if ("none".equals(str3) || TextUtils.isEmpty(str2)) {
            wifiConfiguration.allowedKeyManagement.set(0);
        }
        if ("wep".equals(str3)) {
            if (a(str2)) {
                wifiConfiguration.wepKeys[0] = str2;
            } else {
                wifiConfiguration.wepKeys[0] = "\"" + str2 + "\"";
            }
            wifiConfiguration.hiddenSSID = true;
            wifiConfiguration.allowedAuthAlgorithms.set(1);
            wifiConfiguration.allowedGroupCiphers.set(3);
            wifiConfiguration.allowedGroupCiphers.set(2);
            wifiConfiguration.allowedGroupCiphers.set(0);
            wifiConfiguration.allowedGroupCiphers.set(1);
            wifiConfiguration.allowedKeyManagement.set(0);
            wifiConfiguration.wepTxKeyIndex = 0;
        }
        if ("wpa".equals(str3)) {
            wifiConfiguration.preSharedKey = "\"".concat(str2).concat("\"");
            wifiConfiguration.hiddenSSID = true;
            wifiConfiguration.allowedAuthAlgorithms.set(0);
            wifiConfiguration.allowedGroupCiphers.set(2);
            wifiConfiguration.allowedKeyManagement.set(1);
            wifiConfiguration.allowedPairwiseCiphers.set(1);
            wifiConfiguration.allowedGroupCiphers.set(3);
            wifiConfiguration.allowedPairwiseCiphers.set(2);
            wifiConfiguration.status = 2;
        }
        return wifiConfiguration;
    }

    private static boolean a(String str) {
        int length = str.length();
        return (length == 10 || length == 26 || length == 58) && b(str);
    }

    private static boolean b(String str) {
        for (int length = str.length() - 1; length >= 0; length--) {
            char charAt = str.charAt(length);
            if ((charAt < '0' || charAt > '9') && ((charAt < 'A' || charAt > 'F') && (charAt < 'a' || charAt > 'f'))) {
                return false;
            }
        }
        return true;
    }

    static WifiManager b(Context context) {
        if (a == null) {
            synchronized (WifiHelper.class) {
                if (a == null) {
                    a = (WifiManager) context.getApplicationContext().getSystemService("wifi");
                }
            }
        }
        return a;
    }

    static boolean a(Context context, String str) {
        WifiInfo connectionInfo = b(context).getConnectionInfo();
        if ((connectionInfo == null || !str.equals(connectionInfo.getSSID().replace("\"", ""))) && !"".equals(str)) {
            return false;
        }
        boolean z;
        NetworkInfo networkInfo = ((ConnectivityManager) context.getSystemService("connectivity")).getNetworkInfo(1);
        if (networkInfo != null && networkInfo.isAvailable() && networkInfo.isConnected()) {
            z = true;
        } else {
            z = false;
        }
        return z;
    }

    static void a(Object obj) {
        try {
            obj.wait();
        } catch (InterruptedException e) {
            b.a(obj.getClass().getName() + "---------" + e.toString());
        }
    }

    static boolean b(Context context, String str) {
        return context.getApplicationContext().getPackageManager().checkPermission(str, context.getApplicationContext().getPackageName()) == 0;
    }

    static boolean c(Context context) {
        NetworkInfo activeNetworkInfo = ((ConnectivityManager) context.getSystemService("connectivity")).getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.getType() == 0;
    }

    static boolean d(Context context) {
        LocationManager locationManager = (LocationManager) context.getSystemService(Headers.LOCATION);
        return locationManager.isProviderEnabled("gps") || locationManager.isProviderEnabled("network");
    }

    static WifiConfiguration c(Context context, String str) {
        List<WifiConfiguration> configuredNetworks = b(context).getConfiguredNetworks();
        if (configuredNetworks != null && configuredNetworks.size() > 0) {
            for (WifiConfiguration wifiConfiguration : configuredNetworks) {
                if (wifiConfiguration.SSID.equals("\"".concat(str).concat("\""))) {
                    return wifiConfiguration;
                }
            }
        }
        return null;
    }
}
