package com.mqunar.necro.agent.logging;

import android.util.Log;

public class AndroidAgentLog implements AgentLog {
    private static final String TAG = "com.mqunar.necro.agent";
    private int level = 3;

    public void debug(String str) {
        if (this.level == 5) {
            Log.d("com.mqunar.necro.agent", str);
        }
    }

    public void verbose(String str) {
        if (this.level >= 4) {
            Log.v("com.mqunar.necro.agent", str);
        }
    }

    public void info(String str) {
        if (this.level >= 3) {
            Log.i("com.mqunar.necro.agent", str);
        }
    }

    public void warning(String str) {
        if (this.level >= 2) {
            Log.w("com.mqunar.necro.agent", str);
        }
    }

    public void error(String str) {
        if (this.level >= 1) {
            Log.e("com.mqunar.necro.agent", str);
        }
    }

    public void error(String str, Throwable th) {
        if (this.level >= 1) {
            Log.e("com.mqunar.necro.agent", str, th);
        }
    }

    public int getLevel() {
        return this.level;
    }

    public void setLevel(int i) {
        if (i > 5 || i < 1) {
            throw new IllegalArgumentException("Log level is not between ERROR and DEBUG");
        }
        this.level = i;
    }
}
