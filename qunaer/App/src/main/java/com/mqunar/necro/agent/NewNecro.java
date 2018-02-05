package com.mqunar.necro.agent;

import android.content.Context;
import android.text.TextUtils;
import com.mqunar.necro.agent.logging.AgentLog;
import com.mqunar.necro.agent.logging.AgentLogManager;
import com.mqunar.necro.agent.logging.AndroidAgentLog;
import com.mqunar.necro.agent.logging.NullAgentLog;
import com.mqunar.necro.agent.task.DefaultSender;
import com.mqunar.necro.agent.task.IHttpSender;
import com.mqunar.necro.agent.util.Config;

public class NewNecro {
    private static final AgentLog log = AgentLogManager.getAgentLog();
    private IHttpSender sender = new DefaultSender();
    private boolean started = false;

    private boolean isInstrumented() {
        return true;
    }

    private NewNecro(String str) {
    }

    public static NewNecro withAppToken(String str) {
        return new NewNecro(str);
    }

    public NewNecro withLogEnabled(boolean z) {
        AgentLogManager.setAgentLog(z ? new AndroidAgentLog() : new NullAgentLog());
        return this;
    }

    public NewNecro withPid(String str) {
        Config.pid = str;
        return this;
    }

    public NewNecro withCid(String str) {
        Config.cid = str;
        return this;
    }

    public NewNecro withLogLevel(int i) {
        AgentLogManager.getAgentLog().setLevel(i);
        return this;
    }

    public NewNecro withRequest(String str, String str2) {
        Config.pitcher = str;
        Config.t = str2;
        return this;
    }

    void withSender(IHttpSender iHttpSender) {
        this.sender = iHttpSender;
    }

    public void start(Context context) {
        if (TextUtils.isEmpty(Config.pid)) {
            throw new RuntimeException("qnecro agent pid must be not null");
        } else if (this.started) {
            log.debug("NewNecro is already running.");
        } else if (isInstrumented()) {
            Agent.initAndroidAgent(context, this.sender);
            Agent.start();
            this.started = true;
        } else {
            log.error("Failed to detect New Necro instrumentation.");
        }
    }

    @Deprecated
    public void shutdown() {
        if (this.started) {
            try {
                Agent.stop();
            } finally {
                this.started = false;
            }
        }
    }

    public static void startApp() {
        Agent.startApp();
    }
}
