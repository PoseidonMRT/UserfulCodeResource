package com.mqunar.necro.agent;

import android.content.Context;
import com.mqunar.necro.agent.bean.BaseData;
import com.mqunar.necro.agent.logging.AgentLog;
import com.mqunar.necro.agent.logging.AgentLogManager;
import com.mqunar.necro.agent.task.IHttpSender;

public class Agent {
    private static final AgentImpl NULL_AGENT_IMPL = new NullAgentImpl();
    public static final String VERSION = "0.0.1";
    private static AgentImpl agentImpl = NULL_AGENT_IMPL;
    public static long applicationStart = 0;
    private static Object implLock = new Object();
    private static final AgentLog log = AgentLogManager.getAgentLog();

    private Agent() {
    }

    private static void setImpl(AgentImpl agentImpl) {
        synchronized (implLock) {
            if (agentImpl == null) {
                agentImpl = NULL_AGENT_IMPL;
            } else {
                agentImpl = agentImpl;
            }
        }
    }

    private static AgentImpl getImpl() {
        AgentImpl agentImpl;
        synchronized (implLock) {
            agentImpl = agentImpl;
        }
        return agentImpl;
    }

    static void initAndroidAgent(Context context, IHttpSender iHttpSender) {
        try {
            setImpl(new AndroidAgentImpl(context, iHttpSender));
        } catch (AgentInitializationException e) {
            log.error("Failed to initialize the agent: " + e.toString());
        }
    }

    static void resetNullAgent() {
        setImpl(null);
    }

    public static String getVersion() {
        return VERSION;
    }

    static void disable() {
        getImpl().disable();
        resetNullAgent();
    }

    static boolean isDisabled() {
        return getImpl().isDisabled();
    }

    static void start() {
        getImpl().start();
    }

    static void stop() {
        getImpl().stop();
    }

    public static String getActiveNetworkCarrier() {
        return getImpl().getNetworkCarrier();
    }

    public static String getActiveNetworkWanType() {
        return getImpl().getNetworkWanType();
    }

    public static String getCparam() {
        return getImpl().getCparam();
    }

    public static void dealData(BaseData baseData) {
        getImpl().dealData(baseData);
    }

    public static void sendDataNow() {
        getImpl().sendDataNow();
    }

    public static void startApp() {
        applicationStart = System.currentTimeMillis();
    }
}
