package com.baidu.platform.comapi.commonutils;

import android.content.Context;
import android.net.NetworkInfo;
import android.net.Proxy;
import com.baidu.mapapi.NetworkUtil;
import com.baidu.platform.comapi.util.SysUpdateObserver;
import com.baidu.platform.comjni.engine.AppEngine;
import com.baidu.platform.comjni.map.commonmemcache.a;
import com.mqunar.network.NetRequestManager;

public class SysUpdateUtil implements SysUpdateObserver {
    static a a = new a();
    public static boolean b = false;
    public static String c = "";
    public static int d = 0;

    public void init() {
        if (a != null) {
            a.a();
            a.b();
        }
    }

    public void updateNetworkInfo(Context context) {
        NetworkUtil.updateNetworkProxy(context);
    }

    public void updateNetworkProxy(Context context) {
        NetworkInfo activeNetworkInfo = NetworkUtil.getActiveNetworkInfo(context);
        if (activeNetworkInfo != null && activeNetworkInfo.isAvailable()) {
            String toLowerCase = activeNetworkInfo.getTypeName().toLowerCase();
            if (toLowerCase.equals("wifi") && activeNetworkInfo.isConnected()) {
                AppEngine.SetProxyInfo(null, 0);
                b = false;
            } else if (toLowerCase.equals("mobile") || (toLowerCase.equals("wifi") && !NetworkUtil.isWifiConnected(activeNetworkInfo))) {
                String extraInfo = activeNetworkInfo.getExtraInfo();
                b = false;
                if (extraInfo != null) {
                    extraInfo = extraInfo.toLowerCase();
                    if (extraInfo.startsWith("cmwap") || extraInfo.startsWith("uniwap") || extraInfo.startsWith("3gwap")) {
                        c = "10.0.0.172";
                        d = 80;
                        b = true;
                    } else if (extraInfo.startsWith(NetRequestManager.CTWAP_APN_NAME_2)) {
                        c = "10.0.0.200";
                        d = 80;
                        b = true;
                    } else if (extraInfo.startsWith("cmnet") || extraInfo.startsWith("uninet") || extraInfo.startsWith("ctnet") || extraInfo.startsWith("3gnet")) {
                        b = false;
                    }
                } else {
                    extraInfo = Proxy.getDefaultHost();
                    int defaultPort = Proxy.getDefaultPort();
                    if (extraInfo != null && extraInfo.length() > 0) {
                        if ("10.0.0.172".equals(extraInfo.trim())) {
                            c = "10.0.0.172";
                            d = defaultPort;
                            b = true;
                        } else if ("10.0.0.200".equals(extraInfo.trim())) {
                            c = "10.0.0.200";
                            d = 80;
                            b = true;
                        }
                    }
                }
                if (b) {
                    AppEngine.SetProxyInfo(c, d);
                } else {
                    AppEngine.SetProxyInfo(null, 0);
                }
            }
        }
    }

    public void updatePhoneInfo() {
        if (a != null) {
            a.b();
        }
    }
}
