package com.mqunar.qutui;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;
import android.text.TextUtils;
import com.mqunar.qutui.model.Caf;
import com.mqunar.tools.log.QLog;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class l implements Runnable {
    final /* synthetic */ List a;
    final /* synthetic */ Map b;
    final /* synthetic */ QutuiLog c;

    l(QutuiLog qutuiLog, List list, Map map) {
        this.c = qutuiLog;
        this.a = list;
        this.b = map;
    }

    public void run() {
        List a = i.a();
        Map hashMap = new HashMap();
        List list = a;
        for (Caf caf : this.a) {
            if (TextUtils.isEmpty(caf.blackBrand) || !caf.blackBrand.equalsIgnoreCase(Build.BRAND)) {
                try {
                    boolean z = list.size() > 1 && !list.contains(caf.processName);
                    Intent intent = new Intent(caf.actionName);
                    intent.setPackage(caf.packageName);
                    intent.putExtra("source", caf.source);
                    if (caf.startNow) {
                        QLog.i("try to startService: " + caf.actionName + ", " + caf.packageName + ", " + caf.source, new Object[0]);
                        this.c.l.startService(intent);
                    }
                    if (caf.intervalTime > 0) {
                        PendingIntent service = PendingIntent.getService(this.c.l, 0, intent, 0);
                        AlarmManager alarmManager = (AlarmManager) this.c.l.getSystemService("alarm");
                        if (alarmManager != null) {
                            alarmManager.setRepeating(0, System.currentTimeMillis() + ((long) caf.intervalTime), (long) caf.intervalTime, service);
                        }
                        QutuiLog.m.put(caf.packageName, service);
                    }
                    if (caf.startNow && list.size() > 1) {
                        boolean z2;
                        if (z) {
                            list = i.a();
                            z2 = !list.contains(caf.processName);
                        } else {
                            z2 = z;
                        }
                        hashMap.put(caf.packageName, Boolean.valueOf(z2));
                    }
                    a = list;
                } catch (Throwable th) {
                    Throwable th2 = th;
                    a = list;
                    QLog.e(th2);
                }
                list = a;
            } else {
                QLog.i("ignore board: " + Build.BRAND, new Object[0]);
            }
        }
        QutuiLog.getInstance(this.c.l).a(this.b, hashMap);
    }
}
