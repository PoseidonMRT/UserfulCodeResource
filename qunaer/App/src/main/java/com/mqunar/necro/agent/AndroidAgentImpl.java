package com.mqunar.necro.agent;

import android.app.Application;
import android.content.ComponentCallbacks2;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.res.Configuration;
import android.os.Build.VERSION;
import android.os.Handler;
import android.os.HandlerThread;
import com.iflytek.aiui.AIUIConstant;
import com.iflytek.cloud.SpeechConstant;
import com.mqunar.necro.agent.bean.BaseData;
import com.mqunar.necro.agent.logging.AgentLog;
import com.mqunar.necro.agent.logging.AgentLogManager;
import com.mqunar.necro.agent.task.IHttpSender;
import com.mqunar.necro.agent.tracing.BaseTraceMachineThread;
import com.mqunar.necro.agent.tracing.ITraceMachineThread;
import com.mqunar.necro.agent.tracing.TraceMachine;
import com.mqunar.necro.agent.util.AndroidUtils;
import com.mqunar.necro.agent.util.Config;
import org.json.JSONObject;

class AndroidAgentImpl implements AgentImpl {
    private static final AgentLog log = AgentLogManager.getAgentLog();
    private static Handler mWorkHandler;
    private static HandlerThread mWorkThread;
    private boolean isStarted;
    private ComponentCallbacks2 mComponentCallbacks2;
    private final Context mContext;
    private ITraceMachineThread traceMachineThread;

    public AndroidAgentImpl(Context context, IHttpSender iHttpSender) {
        this.mContext = appContext(context);
        if (isDisabled()) {
            throw new AgentInitializationException("This version of the agent has been disabled");
        }
        this.traceMachineThread = new BaseTraceMachineThread();
        TraceMachine.setTraceMachineInterface(this.traceMachineThread);
        NecroUtils.init(this.mContext, iHttpSender);
        mWorkThread = new HandlerThread("AndroidAgentImpl");
        mWorkThread.start();
        mWorkHandler = new Handler(mWorkThread.getLooper());
    }

    private void initialize() {
        this.mComponentCallbacks2 = new ComponentCallbacks2() {
            public void onTrimMemory(int i) {
                if (i == 20) {
                    AndroidAgentImpl.this.sendDataNow();
                }
            }

            public void onConfigurationChanged(Configuration configuration) {
            }

            public void onLowMemory() {
            }
        };
        this.mContext.registerComponentCallbacks(this.mComponentCallbacks2);
    }

    private static Context appContext(Context context) {
        return context instanceof Application ? context : context.getApplicationContext();
    }

    public void start() {
        log.info("AndroidAgentImpl start start start start start");
        if (isDisabled() || this.isStarted) {
            stop(false);
            return;
        }
        initialize();
        this.isStarted = true;
    }

    public void stop() {
        stop(true);
    }

    private void stop(boolean z) {
        this.isStarted = false;
        TraceMachine.haltTracing();
        if (this.mComponentCallbacks2 != null) {
            this.mContext.unregisterComponentCallbacks(this.mComponentCallbacks2);
        }
    }

    public void disable() {
        log.warning("PERMANENTLY DISABLING AGENT V" + Agent.getVersion());
        stop(false);
    }

    public boolean isDisabled() {
        return false;
    }

    public String getNetworkCarrier() {
        return AndroidUtils.carrierNameFromContext(this.mContext);
    }

    public String getNetworkWanType() {
        return AndroidUtils.wanType(this.mContext);
    }

    public String getCparam() {
        if (this.mContext == null) {
            return "";
        }
        JSONObject jSONObject = new JSONObject();
        try {
            PackageInfo packageInfo = this.mContext.getPackageManager().getPackageInfo(this.mContext.getPackageName(), 0);
            jSONObject.put("ma", AndroidUtils.getMac());
            jSONObject.put("osVersion", VERSION.RELEASE + "_" + VERSION.SDK_INT);
            jSONObject.put("pid", Config.pid);
            jSONObject.put(AIUIConstant.KEY_UID, AndroidUtils.getIMEI(this.mContext));
            jSONObject.put(SpeechConstant.ISV_VID, packageInfo.versionCode);
            jSONObject.put("ke", String.valueOf(System.currentTimeMillis()));
        } catch (Exception e) {
        }
        return jSONObject.toString();
    }

    public void dealData(final BaseData baseData) {
        if (this.isStarted && mWorkHandler != null && AndroidUtils.isNetworkConnected(this.mContext)) {
            mWorkHandler.post(new Runnable() {
                public void run() {
                    NecroUtils.dealData(baseData);
                }
            });
        }
    }

    public void sendDataNow() {
        if (this.isStarted && mWorkHandler != null && AndroidUtils.isNetworkConnected(this.mContext)) {
            mWorkHandler.post(new Runnable() {
                public void run() {
                    NecroUtils.sendDataNow();
                }
            });
        }
    }
}
