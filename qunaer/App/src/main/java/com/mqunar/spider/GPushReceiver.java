package com.mqunar.spider;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import com.mqunar.core.QSpider;
import java.util.ArrayList;
import java.util.List;

public class GPushReceiver extends BroadcastReceiver {
    private static Object a = new Object();
    private static List<Intent> b;

    public synchronized void onReceive(Context context, Intent intent) {
        if (!QSpider.loadDone) {
            synchronized (a) {
                if (b == null) {
                    b = new ArrayList();
                }
                b.add(intent);
            }
        } else if (TextUtils.isEmpty(QSpider.crashTouchUrl)) {
            BroadcastReceiver broadcastReceiver;
            check(context);
            try {
                broadcastReceiver = (BroadcastReceiver) Class.forName("com.mqunar.atom.push.GPushReceiver").newInstance();
            } catch (Exception e) {
                e.printStackTrace();
                broadcastReceiver = null;
            }
            if (broadcastReceiver != null) {
                broadcastReceiver.onReceive(context, intent);
            }
        } else {
            throw new RuntimeException("init fail!");
        }
    }

    public static void check(Context context) {
        synchronized (a) {
            List list;
            if (b == null || b.isEmpty()) {
                list = null;
            } else {
                list = new ArrayList(b);
                b.clear();
                b = null;
            }
        }
        if (r0 != null && !r0.isEmpty()) {
            for (Intent intent : r0) {
                BroadcastReceiver broadcastReceiver;
                try {
                    broadcastReceiver = (BroadcastReceiver) Class.forName("com.mqunar.atom.push.GPushReceiver").newInstance();
                } catch (Exception e) {
                    e.printStackTrace();
                    broadcastReceiver = null;
                }
                if (broadcastReceiver != null) {
                    broadcastReceiver.onReceive(context, intent);
                }
            }
        }
    }
}
