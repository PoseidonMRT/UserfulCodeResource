package com.mqunar.spider;

import android.content.Context;
import android.os.Build;
import android.os.Build.VERSION;
import com.iflytek.aiui.AIUIConstant;
import com.iflytek.cloud.SpeechConstant;
import com.mqunar.atomenv.GlobalEnv;
import com.mqunar.core.ReflectUtils;
import com.mqunar.json.JsonUtils;
import com.mqunar.necro.agent.task.IHttpSender;
import com.mqunar.necro.agent.task.NecroSenderProxy;
import java.util.HashMap;

public class QNecroSender implements IHttpSender {
    public void send(Context context, String str) {
        String str2 = (String) ReflectUtils.invokeStaticMethod("com.mqunar.qav.uelog.QAVLog", "getRequestId", null, null);
        if (GlobalEnv.getInstance().isRelease()) {
            new NecroSenderProxy("http://mloganalysts.corp.qunar.com/api/log/androidPerformanceLog", "", a(), str2).send(context, str);
        } else {
            new NecroSenderProxy("http://l-wap6.wap.beta.cn6.qunar.com:9099/api/log/androidPerformanceLog", "http://front.pitcher.beta.qunar.com/pitcher-proxy", a(), str2).send(context, str);
        }
    }

    private String a() {
        HashMap hashMap = new HashMap();
        hashMap.put("gid", GlobalEnv.getInstance().getGid());
        hashMap.put("ma", GlobalEnv.getInstance().getMac());
        hashMap.put("osVersion", VERSION.RELEASE);
        hashMap.put("pid", GlobalEnv.getInstance().getPid());
        hashMap.put(AIUIConstant.KEY_UID, GlobalEnv.getInstance().getUid());
        hashMap.put(SpeechConstant.ISV_VID, GlobalEnv.getInstance().getVid());
        hashMap.put("key", String.valueOf(System.currentTimeMillis()));
        hashMap.put("cid", GlobalEnv.getInstance().getCid());
        hashMap.put("rcid", GlobalEnv.getInstance().getRCid());
        hashMap.put("model", Build.MODEL);
        return JsonUtils.toJsonString(hashMap);
    }
}
