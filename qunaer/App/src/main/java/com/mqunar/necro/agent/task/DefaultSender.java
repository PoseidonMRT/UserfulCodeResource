package com.mqunar.necro.agent.task;

import android.content.Context;
import com.mqunar.necro.agent.Agent;
import com.mqunar.necro.agent.util.Config;

public class DefaultSender implements IHttpSender {
    public void send(Context context, String str) {
        NecroSenderProxy necroSenderProxy = new NecroSenderProxy(Config.t, Config.pitcher, Agent.getCparam(), String.valueOf(System.currentTimeMillis()));
    }
}
