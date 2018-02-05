package com.qunar.qwifi;

import com.huawei.hwid.openapi.out.OutReturn.ParamStr;
import com.iflytek.aiui.AIUIConstant;
import com.iflytek.cloud.SpeechEvent;
import com.mqunar.libtask.AbsConductor;
import com.mqunar.libtask.TaskCallback;
import java.util.HashMap;
import org.json.JSONArray;
import org.json.JSONObject;

final class e implements TaskCallback {
    e() {
    }

    public void onMsgRequest(AbsConductor absConductor, boolean z) {
    }

    public void onMsgResult(AbsConductor absConductor, boolean z) {
        synchronized (WifiHelper.a) {
            WifiHelper.b = new HashMap();
            try {
                JSONArray jSONArray = new JSONObject(new String((byte[]) absConductor.getResult(), "utf-8")).getJSONObject(SpeechEvent.KEY_EVENT_RECORD_DATA).getJSONArray("wifiRe");
                for (int i = 0; i < jSONArray.length(); i++) {
                    JSONObject jSONObject = jSONArray.getJSONObject(i);
                    WifiHelper.b.put(jSONObject.getString(AIUIConstant.KEY_NAME), jSONObject.getString("password"));
                }
            } catch (Throwable th) {
                b.a("getWifiInfo onMsgResult : " + th.toString());
            }
            WifiHelper.a.notify();
        }
    }

    public void onMsgError(AbsConductor absConductor, boolean z) {
        synchronized (WifiHelper.a) {
            WifiHelper.b = new HashMap();
            WifiHelper.b.put(ParamStr.RET_RES_ERROR, "");
            WifiHelper.a.notify();
        }
    }

    public void onMsgProgress(AbsConductor absConductor, boolean z) {
    }

    public void onMsgStart(AbsConductor absConductor, boolean z) {
    }

    public void onMsgEnd(AbsConductor absConductor, boolean z) {
    }

    public void onMsgCancel(AbsConductor absConductor, boolean z) {
        synchronized (WifiHelper.a) {
            WifiHelper.b = new HashMap();
            WifiHelper.b.put(ParamStr.RET_RES_ERROR, "");
            WifiHelper.a.notify();
        }
    }

    public void onMsgCacheHit(AbsConductor absConductor, boolean z) {
    }
}
