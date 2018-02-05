package com.mqunar.necro.agent.logging;

public class NullAgentLog implements AgentLog {
    public void debug(String str) {
    }

    public void info(String str) {
    }

    public void verbose(String str) {
    }

    public void error(String str) {
    }

    public void error(String str, Throwable th) {
    }

    public void warning(String str) {
    }

    public int getLevel() {
        return 5;
    }

    public void setLevel(int i) {
    }
}
