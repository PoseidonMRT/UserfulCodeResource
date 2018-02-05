package com.mqunar.necro.agent.tracing;

import com.mqunar.necro.agent.Agent;
import com.mqunar.necro.agent.bean.WebViewData;
import com.mqunar.necro.agent.logging.AgentLog;
import com.mqunar.necro.agent.logging.AgentLogManager;

public class WebViewMachine {
    private static final AgentLog log = AgentLogManager.getAgentLog();
    private static WebViewData webData = null;

    public static boolean isTracingInactive() {
        return webData == null;
    }

    public static void onPageStarted(String str) {
        log.debug("WebViewMachine onPageStarted begin trace ");
        webData = new WebViewData();
        webData.url = str;
        webData.startTime = String.valueOf(System.currentTimeMillis());
    }

    public static void onPageFinished(String str) {
        log.debug("WebViewMachine onPageFinished begin trace ");
        if (!isTracingInactive() && str.equals(webData.url)) {
            webData.endTime = String.valueOf(System.currentTimeMillis());
            dealData();
        }
    }

    public static void onReceivedError(int i, String str) {
        log.debug("WebViewMachine onReceivedError begin trace ");
        if (!isTracingInactive() && str.equals(webData.url)) {
            webData.endTime = String.valueOf(System.currentTimeMillis());
            webData.errorType = String.valueOf(i);
            dealData();
        }
    }

    private static void dealData() {
        if (!isTracingInactive()) {
            Agent.dealData(webData);
        }
    }
}
