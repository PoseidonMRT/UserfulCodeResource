package com.huawei.hwid.openapi.e;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import java.util.ArrayList;
import java.util.HashMap;

public class c {
    private static final Object f = new Object();
    private static c g;
    private final Context a;
    private final HashMap b = new HashMap();
    private final HashMap c = new HashMap();
    private final ArrayList d = new ArrayList();
    private final Handler e;

    public static c a(Context context) {
        c cVar;
        synchronized (f) {
            if (g == null) {
                g = new c(context.getApplicationContext());
            }
            cVar = g;
        }
        return cVar;
    }

    private c(Context context) {
        this.a = context;
        this.e = new d(this, context.getMainLooper());
    }

    public void a(BroadcastReceiver broadcastReceiver, IntentFilter intentFilter) {
        synchronized (this.b) {
            f fVar = new f(intentFilter, broadcastReceiver);
            ArrayList arrayList = (ArrayList) this.b.get(broadcastReceiver);
            if (arrayList == null) {
                arrayList = new ArrayList(1);
                this.b.put(broadcastReceiver, arrayList);
            }
            arrayList.add(intentFilter);
            for (int i = 0; i < intentFilter.countActions(); i++) {
                String action = intentFilter.getAction(i);
                arrayList = (ArrayList) this.c.get(action);
                if (arrayList == null) {
                    arrayList = new ArrayList(1);
                    this.c.put(action, arrayList);
                }
                arrayList.add(fVar);
            }
        }
    }

    public void a(BroadcastReceiver broadcastReceiver) {
        synchronized (this.b) {
            ArrayList arrayList = (ArrayList) this.b.remove(broadcastReceiver);
            if (arrayList == null) {
                return;
            }
            for (int i = 0; i < arrayList.size(); i++) {
                IntentFilter intentFilter = (IntentFilter) arrayList.get(i);
                for (int i2 = 0; i2 < intentFilter.countActions(); i2++) {
                    String action = intentFilter.getAction(i2);
                    ArrayList arrayList2 = (ArrayList) this.c.get(action);
                    if (arrayList2 != null) {
                        int i3 = 0;
                        while (i3 < arrayList2.size()) {
                            int i4;
                            if (((f) arrayList2.get(i3)).b == broadcastReceiver) {
                                arrayList2.remove(i3);
                                i4 = i3 - 1;
                            } else {
                                i4 = i3;
                            }
                            i3 = i4 + 1;
                        }
                        if (arrayList2.size() <= 0) {
                            this.c.remove(action);
                        }
                    }
                }
            }
        }
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean a(android.content.Intent r16) {
        /*
        r15 = this;
        r11 = 1;
        r12 = 0;
        r14 = r15.b;
        monitor-enter(r14);
        r2 = r16.getAction();	 Catch:{ all -> 0x0063 }
        r1 = r15.a;	 Catch:{ all -> 0x0063 }
        r1 = r1.getContentResolver();	 Catch:{ all -> 0x0063 }
        r0 = r16;
        r3 = r0.resolveTypeIfNeeded(r1);	 Catch:{ all -> 0x0063 }
        r5 = r16.getData();	 Catch:{ all -> 0x0063 }
        r4 = r16.getScheme();	 Catch:{ all -> 0x0063 }
        r6 = r16.getCategories();	 Catch:{ all -> 0x0063 }
        r1 = r15.c;	 Catch:{ all -> 0x0063 }
        r7 = r16.getAction();	 Catch:{ all -> 0x0063 }
        r1 = r1.get(r7);	 Catch:{ all -> 0x0063 }
        r0 = r1;
        r0 = (java.util.ArrayList) r0;	 Catch:{ all -> 0x0063 }
        r8 = r0;
        if (r8 == 0) goto L_0x0098;
    L_0x0031:
        r10 = 0;
        r13 = r12;
    L_0x0033:
        r1 = r8.size();	 Catch:{ all -> 0x0063 }
        if (r13 >= r1) goto L_0x0066;
    L_0x0039:
        r1 = r8.get(r13);	 Catch:{ all -> 0x0063 }
        r0 = r1;
        r0 = (com.huawei.hwid.openapi.e.f) r0;	 Catch:{ all -> 0x0063 }
        r9 = r0;
        r1 = r9.c;	 Catch:{ all -> 0x0063 }
        if (r1 == 0) goto L_0x004b;
    L_0x0045:
        r1 = r10;
    L_0x0046:
        r7 = r13 + 1;
        r13 = r7;
        r10 = r1;
        goto L_0x0033;
    L_0x004b:
        r1 = r9.a;	 Catch:{ all -> 0x0063 }
        r7 = "LocalBroadcastManager";
        r1 = r1.match(r2, r3, r4, r5, r6, r7);	 Catch:{ all -> 0x0063 }
        if (r1 < 0) goto L_0x009d;
    L_0x0055:
        if (r10 != 0) goto L_0x009b;
    L_0x0057:
        r1 = new java.util.ArrayList;	 Catch:{ all -> 0x0063 }
        r1.<init>();	 Catch:{ all -> 0x0063 }
    L_0x005c:
        r1.add(r9);	 Catch:{ all -> 0x0063 }
        r7 = 1;
        r9.c = r7;	 Catch:{ all -> 0x0063 }
        goto L_0x0046;
    L_0x0063:
        r1 = move-exception;
        monitor-exit(r14);	 Catch:{ all -> 0x0063 }
        throw r1;
    L_0x0066:
        if (r10 == 0) goto L_0x0098;
    L_0x0068:
        r1 = r10.size();	 Catch:{ all -> 0x0063 }
        if (r12 >= r1) goto L_0x007a;
    L_0x006e:
        r1 = r10.get(r12);	 Catch:{ all -> 0x0063 }
        r1 = (com.huawei.hwid.openapi.e.f) r1;	 Catch:{ all -> 0x0063 }
        r2 = 0;
        r1.c = r2;	 Catch:{ all -> 0x0063 }
        r12 = r12 + 1;
        goto L_0x0068;
    L_0x007a:
        r1 = r15.d;	 Catch:{ all -> 0x0063 }
        r2 = new com.huawei.hwid.openapi.e.e;	 Catch:{ all -> 0x0063 }
        r0 = r16;
        r2.<init>(r0, r10);	 Catch:{ all -> 0x0063 }
        r1.add(r2);	 Catch:{ all -> 0x0063 }
        r1 = r15.e;	 Catch:{ all -> 0x0063 }
        r2 = 1;
        r1 = r1.hasMessages(r2);	 Catch:{ all -> 0x0063 }
        if (r1 != 0) goto L_0x0095;
    L_0x008f:
        r1 = r15.e;	 Catch:{ all -> 0x0063 }
        r2 = 1;
        r1.sendEmptyMessage(r2);	 Catch:{ all -> 0x0063 }
    L_0x0095:
        monitor-exit(r14);	 Catch:{ all -> 0x0063 }
        r1 = r11;
    L_0x0097:
        return r1;
    L_0x0098:
        monitor-exit(r14);	 Catch:{ all -> 0x0063 }
        r1 = r12;
        goto L_0x0097;
    L_0x009b:
        r1 = r10;
        goto L_0x005c;
    L_0x009d:
        r1 = r10;
        goto L_0x0046;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.huawei.hwid.openapi.e.c.a(android.content.Intent):boolean");
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void a() {
        /*
        r8 = this;
        r2 = 0;
    L_0x0001:
        r1 = r8.b;
        monitor-enter(r1);
        r0 = r8.d;	 Catch:{ all -> 0x003f }
        r0 = r0.size();	 Catch:{ all -> 0x003f }
        if (r0 > 0) goto L_0x000e;
    L_0x000c:
        monitor-exit(r1);	 Catch:{ all -> 0x003f }
        return;
    L_0x000e:
        r4 = new com.huawei.hwid.openapi.e.e[r0];	 Catch:{ all -> 0x003f }
        r0 = r8.d;	 Catch:{ all -> 0x003f }
        r0.toArray(r4);	 Catch:{ all -> 0x003f }
        r0 = r8.d;	 Catch:{ all -> 0x003f }
        r0.clear();	 Catch:{ all -> 0x003f }
        monitor-exit(r1);	 Catch:{ all -> 0x003f }
        r3 = r2;
    L_0x001c:
        r0 = r4.length;
        if (r3 >= r0) goto L_0x0001;
    L_0x001f:
        r5 = r4[r3];
        r1 = r2;
    L_0x0022:
        r0 = r5.b;
        r0 = r0.size();
        if (r1 >= r0) goto L_0x0042;
    L_0x002a:
        r0 = r5.b;
        r0 = r0.get(r1);
        r0 = (com.huawei.hwid.openapi.e.f) r0;
        r0 = r0.b;
        r6 = r8.a;
        r7 = r5.a;
        r0.onReceive(r6, r7);
        r0 = r1 + 1;
        r1 = r0;
        goto L_0x0022;
    L_0x003f:
        r0 = move-exception;
        monitor-exit(r1);	 Catch:{ all -> 0x003f }
        throw r0;
    L_0x0042:
        r0 = r3 + 1;
        r3 = r0;
        goto L_0x001c;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.huawei.hwid.openapi.e.c.a():void");
    }

    public synchronized void a(Context context, Intent intent) {
        if (!(intent == null || context == null)) {
            intent.setAction("com.huawei.cloudserive.getSTSuccess");
            g.a(intent);
        }
    }

    public synchronized void b(Context context, Intent intent) {
        if (!(intent == null || context == null)) {
            intent.setAction("com.huawei.cloudserive.getSTCancel");
            g.a(intent);
        }
    }

    public synchronized void a(Context context, Bundle bundle) {
        if (!(bundle == null || context == null)) {
            Intent intent = new Intent();
            intent.setAction("com.huawei.cloudserive.getSTCancel");
            Bundle bundle2 = new Bundle();
            bundle2.putBundle("bundle", bundle);
            intent.putExtras(bundle2);
            g.a(intent);
        }
    }
}
