package com.mqunar.qutui;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Process;
import android.text.TextUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.mqunar.cock.network.OneKeyCremation;
import com.mqunar.tools.log.QLog;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class PushManager {
    private static PushManager c = new PushManager();
    Boolean a = Boolean.valueOf(false);
    private final String b = "PushManager";
    private IQutuiInterface d;
    private Context e;
    private ArrayList<OnImMessageListener> f = new ArrayList();
    private Map<String, PushListener> g = new HashMap();
    private ServiceConnection h = new g(this);
    private OnIMMessage i = new h(this);

    public interface OnImMessageListener {
        void onClosed();

        void onConnected();

        void onImMessage(byte[] bArr);
    }

    public void registerImListner(OnImMessageListener onImMessageListener) {
        this.f.add(onImMessageListener);
    }

    public void unregisterImListener(OnImMessageListener onImMessageListener) {
        this.f.remove(onImMessageListener);
    }

    public static PushManager getInstance() {
        return c;
    }

    private PushManager() {
    }

    public void registerTopic(String str, PushListener pushListener) {
        this.g.put(str, pushListener);
    }

    public void unregisterTopic(String str) {
        this.g.remove(str);
    }

    public void dispatchMessage(String str) {
        JSONObject parseObject = JSON.parseObject(str);
        String string = parseObject.getString("topic");
        if (this.g.containsKey(string)) {
            ((PushListener) this.g.get(string)).onPush(parseObject);
        }
    }

    public void init(Context context, String str, boolean z) {
        this.e = context;
        Intent intent = new Intent(context, KActivity.class);
        intent.setFlags(268435456);
        context.startActivity(intent);
        QLog.d("PushManager", "PushManager init", new Object[0]);
        BroadcastReceiver keepLiveReceiver = new KeepLiveReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.intent.action.SCREEN_OFF");
        intentFilter.addAction("android.intent.action.SCREEN_ON");
        intentFilter.addAction("android.intent.action.USER_PRESENT");
        context.registerReceiver(keepLiveReceiver, intentFilter);
        intent = new Intent(context, NetService.class);
        intent.putExtra("yid", str);
        intent.putExtra("isRelease", z);
        context.startService(intent);
        a();
    }

    public void stopPush() {
        OneKeyCremation.getInstance().stopPush();
    }

    public void startPush() {
        OneKeyCremation.getInstance().startPush();
    }

    public void setDebugMode(boolean z) {
        this.a = Boolean.valueOf(z);
    }

    public boolean isPushProcess(Context context) {
        return "com.Qunar:qutui".equalsIgnoreCase(getCurrentProcessName(context));
    }

    public static String getCurrentProcessName(Context context) {
        String str = null;
        try {
            Class cls = Class.forName("android.ddm.DdmHandleAppName");
            str = (String) cls.getDeclaredMethod("getAppName", new Class[0]).invoke(cls, new Object[0]);
        } catch (Throwable th) {
        }
        if (TextUtils.isEmpty(str)) {
            int myPid = Process.myPid();
            for (RunningAppProcessInfo runningAppProcessInfo : ((ActivityManager) context.getSystemService("activity")).getRunningAppProcesses()) {
                if (runningAppProcessInfo.pid == myPid) {
                    str = runningAppProcessInfo.processName;
                    break;
                }
            }
        }
        QLog.d("PushManager", str, new Object[0]);
        return str;
    }

    private void a() {
        this.e.bindService(new Intent(this.e, NetService.class), this.h, 1);
    }

    public void sendImMessage(byte[] bArr) {
        try {
            this.d.sendImMessage(bArr);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean OneKeyCremation() {
        try {
            return this.d.oneKeyCremation();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
