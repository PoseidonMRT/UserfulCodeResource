package com.mqunar.qutui;

import java.util.Map.Entry;

class q implements Runnable {
    final /* synthetic */ m a;

    q(m mVar) {
        this.a = mVar;
    }

    public void run() {
        if (QutuiLog.a(this.a.b)) {
            for (Entry entry : this.a.a.getAll().entrySet()) {
                if (((String) entry.getKey()).matches("^\\d+$") && Long.parseLong((String) entry.getKey()) + 600000 <= System.currentTimeMillis()) {
                    QutuiLog.getInstance(this.a.b).sendLog((String) entry.getValue());
                    this.a.a((String) entry.getKey());
                }
            }
        }
    }
}
