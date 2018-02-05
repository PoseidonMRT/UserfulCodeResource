package com.mqunar.qutui;

import com.mqunar.cock.network.OneKeyCremation;
import com.mqunar.tools.log.QLog;

class e implements Runnable {
    final /* synthetic */ long a;
    final /* synthetic */ NotificationReceiver b;

    e(NotificationReceiver notificationReceiver, long j) {
        this.b = notificationReceiver;
        this.a = j;
    }

    public void run() {
        if (this.a > 1500) {
            QLog.d("YACCA", "time:" + this.a, new Object[0]);
            if (this.b.a()) {
                OneKeyCremation.getInstance().connect();
                return;
            }
            return;
        }
        this.b.a.postDelayed(this, 1500);
    }
}
