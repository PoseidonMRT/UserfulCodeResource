package com.qunar.qwifi;

import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.os.SystemClock;
import android.text.TextUtils;
import com.huawei.hwid.openapi.out.OutReturn.ParamStr;
import com.iflytek.aiui.AIUIConstant;
import com.mqunar.libtask.AbsConductor;
import com.mqunar.libtask.ChiefGuard;
import com.mqunar.libtask.HotdogConductor;
import com.mqunar.libtask.TaskCallback;
import com.mqunar.libtask.Ticket.RequestFeature;
import java.util.Map;
import java.util.Map.Entry;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class WifiHelper {
    private static final Object a = new Object();
    private static Map<String, String> b = null;

    public static void toWifiSettings(Context context) {
        context.startActivity(new Intent("android.settings.WIFI_SETTINGS"));
    }

    public static String autoConnectWifi(Context context, String str, int i, long j) {
        if (!c.b(context, "android.permission.ACCESS_WIFI_STATE") || !c.b(context, "android.permission.CHANGE_WIFI_STATE")) {
            return "14";
        }
        if (!c.b(context, "android.permission.ACCESS_COARSE_LOCATION") || !c.b(context, "android.permission.ACCESS_FINE_LOCATION")) {
            return "15";
        }
        if (!c.d(context.getApplicationContext())) {
            return "16";
        }
        if (c.b(context, "android.permission.ACCESS_NETWORK_STATE") && !c.c(context) && !c.a(context, "")) {
            return "12";
        }
        if (!c.b(context).isWifiEnabled() && !c.b(context).setWifiEnabled(true)) {
            return "13";
        }
        AbsConductor hotdogConductor = new HotdogConductor(c());
        JSONObject jSONObject = new JSONObject();
        try {
            jSONObject.put("seqId", str);
            jSONObject.put("business", i);
            jSONObject.put("type", "android");
            jSONObject.put("actionType", "getWifiList");
        } catch (JSONException e) {
            b.a("getWifiList json failed : " + e);
        }
        hotdogConductor.setParams(a.a, "qpub_auto_wifi", jSONObject.toString());
        ChiefGuard.getInstance().addTask(context, hotdogConductor, new RequestFeature[0]);
        Map a = c.a(context);
        if (b == null) {
            synchronized (a) {
                if (b == null) {
                    c.a(a);
                }
            }
        }
        if (b == null || (b.size() == 1 && b.containsKey(ParamStr.RET_RES_ERROR) && "".equals(b.get(ParamStr.RET_RES_ERROR)))) {
            return "10";
        }
        if (b.size() == 0) {
            return "11";
        }
        WifiInfo connectionInfo = c.b(context).getConnectionInfo();
        if (connectionInfo != null && b.containsKey(connectionInfo.getSSID().replace("\"", ""))) {
            return "2";
        }
        WifiConfiguration c = c.c(context, c.b(context).getConnectionInfo().getSSID().replace("\"", ""));
        JSONObject jSONObject2 = new JSONObject();
        JSONArray jSONArray = new JSONArray();
        int i2 = 0;
        for (Entry entry : a.entrySet()) {
            int i3;
            if (b.containsKey(entry.getKey())) {
                int i4 = i2 + 1;
                a(context, (String) entry.getKey(), (String) b.get(entry.getKey()), (String) entry.getValue());
                long j2 = 0;
                while (j2 <= j && !c.a(context, (String) entry.getKey())) {
                    if (c != null && c.a(context, c.SSID.replace("\"", ""))) {
                        c.b(context).disconnect();
                        a(context, (String) entry.getKey());
                    }
                    SystemClock.sleep(200);
                    j2 += 200;
                }
                JSONObject jSONObject3;
                if (j2 <= j) {
                    jSONObject3 = new JSONObject();
                    try {
                        jSONObject3.put(AIUIConstant.KEY_NAME, entry.getKey());
                        jSONObject3.put("password", b.get(entry.getKey()));
                        jSONObject2.put("success", jSONObject3);
                    } catch (JSONException e2) {
                        b.a("successResult json failed : " + e2);
                    }
                    a(context, jSONObject2, str, i);
                    b = null;
                    return "0";
                }
                jSONObject3 = new JSONObject();
                try {
                    jSONObject3.put(AIUIConstant.KEY_NAME, entry.getKey());
                    jSONObject3.put("password", b.get(entry.getKey()));
                    jSONArray.put(jSONObject3);
                    jSONObject2.put("fail", jSONArray);
                } catch (JSONException e22) {
                    b.a("failResult json failed : " + e22);
                }
                if (i4 >= 3) {
                    a(context, jSONObject2, str, i);
                    b = null;
                    return "1";
                }
                i3 = i4;
            } else {
                i3 = i2;
            }
            i2 = i3;
        }
        a(context, jSONObject2, str, i);
        b = null;
        return "1";
    }

    private static void a(Context context, JSONObject jSONObject, String str, int i) {
        if (!TextUtils.isEmpty(jSONObject.toString())) {
            AbsConductor hotdogConductor = new HotdogConductor(new TaskCallback[0]);
            JSONObject jSONObject2 = new JSONObject();
            try {
                jSONObject2.put("seqId", str);
                jSONObject2.put("business", i);
                jSONObject2.put("type", "android");
                jSONObject2.put("actionType", "connectResult");
                jSONObject2.put("connect_re", jSONObject);
            } catch (JSONException e) {
                b.a("connect result json failed : " + e);
            }
            hotdogConductor.setParams(a.a, "qpub_auto_wifi", jSONObject2.toString());
            ChiefGuard.getInstance().addTask(context, hotdogConductor, new RequestFeature[0]);
        }
    }

    private static boolean a(Context context, String str, String str2, String str3) {
        return c.b(context).enableNetwork(c.b(context).addNetwork(c.a(context, str, str2, str3)), true);
    }

    private static boolean a(Context context, String str) {
        WifiConfiguration c = c.c(context, str);
        if (c != null) {
            c.b(context).enableNetwork(c.networkId, true);
        }
        return false;
    }

    private static TaskCallback c() {
        return new e();
    }
}
