package com.mqunar.hy.res.libtask;

import android.annotation.TargetApi;
import android.content.Context;
import com.mqunar.hy.res.libtask.Ticket.RequestFeature;
import com.mqunar.libtask.ProgressType;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;

@TargetApi(9)
public final class ChiefGuard {
    private static ChiefGuard instance;
    final List<TaskTrain> running = new LinkedList();
    final Trumpet trumpet = new Trumpet() {
        public void ok(TaskTrain taskTrain) {
            synchronized (ChiefGuard.this.running) {
                ChiefGuard.this.running.remove(taskTrain);
            }
            ChiefGuard.this.checkTasks();
        }

        public void cancel(TaskTrain taskTrain) {
            synchronized (ChiefGuard.this.running) {
                ChiefGuard.this.running.remove(taskTrain);
            }
            ChiefGuard.this.checkTasks();
        }
    };
    final BlockingDeque<TaskTrain> waiting = new LinkedBlockingDeque(ProgressType.PRO_END);

    public static ChiefGuard getInstance() {
        if (instance == null) {
            synchronized (ChiefGuard.class) {
                if (instance == null) {
                    instance = new ChiefGuard();
                }
            }
        }
        return instance;
    }

    private ChiefGuard() {
    }

    public void addTask(Context context, AbsConductor absConductor, RequestFeature... requestFeatureArr) {
        addTask(context, absConductor, new Ticket(requestFeatureArr));
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void addTask(android.content.Context r8, com.mqunar.hy.res.libtask.AbsConductor r9, com.mqunar.hy.res.libtask.Ticket r10) {
        /*
        r7 = this;
        r0 = r9.getStatus();
        r1 = com.mqunar.hy.res.libtask.TaskCode.TASK_NONE;
        if (r0 == r1) goto L_0x0009;
    L_0x0008:
        return;
    L_0x0009:
        r1 = new com.mqunar.hy.res.libtask.TaskTrain;
        r0 = r7.trumpet;
        r1.<init>(r8, r9, r10, r0);
        r2 = r7.waiting;
        monitor-enter(r2);
        r0 = r7.waiting;	 Catch:{ all -> 0x001d }
        r0 = r0.contains(r1);	 Catch:{ all -> 0x001d }
        if (r0 == 0) goto L_0x0020;
    L_0x001b:
        monitor-exit(r2);	 Catch:{ all -> 0x001d }
        goto L_0x0008;
    L_0x001d:
        r0 = move-exception;
        monitor-exit(r2);	 Catch:{ all -> 0x001d }
        throw r0;
    L_0x0020:
        monitor-exit(r2);	 Catch:{ all -> 0x001d }
        r2 = r7.running;
        monitor-enter(r2);
        r0 = r7.running;	 Catch:{ all -> 0x002e }
        r0 = r0.contains(r1);	 Catch:{ all -> 0x002e }
        if (r0 == 0) goto L_0x0031;
    L_0x002c:
        monitor-exit(r2);	 Catch:{ all -> 0x002e }
        goto L_0x0008;
    L_0x002e:
        r0 = move-exception;
        monitor-exit(r2);	 Catch:{ all -> 0x002e }
        throw r0;
    L_0x0031:
        monitor-exit(r2);	 Catch:{ all -> 0x002e }
        r9.beforeAdd();
        r0 = r9.status;
        r2 = com.mqunar.hy.res.libtask.TaskCode.TASK_PENDING;
        r0.set(r2);
        r0 = -2147483648; // 0xffffffff80000000 float:-0.0 double:NaN;
        r9.progress = r0;
        r0 = r9.msgd;
        r2 = com.mqunar.hy.res.libtask.TaskCode.TASK_PENDING;
        r0.onMessage(r2, r9);
        r0 = r10.addType;
        switch(r0) {
            case 0: goto L_0x0050;
            case 1: goto L_0x005d;
            case 2: goto L_0x004c;
            case 3: goto L_0x006a;
            default: goto L_0x004c;
        };
    L_0x004c:
        r7.checkTasks();
        goto L_0x0008;
    L_0x0050:
        r2 = r7.waiting;
        monitor-enter(r2);
        r0 = r7.waiting;	 Catch:{ all -> 0x005a }
        r0.add(r1);	 Catch:{ all -> 0x005a }
        monitor-exit(r2);	 Catch:{ all -> 0x005a }
        goto L_0x004c;
    L_0x005a:
        r0 = move-exception;
        monitor-exit(r2);	 Catch:{ all -> 0x005a }
        throw r0;
    L_0x005d:
        r2 = r7.waiting;
        monitor-enter(r2);
        r0 = r7.waiting;	 Catch:{ all -> 0x0067 }
        r0.addFirst(r1);	 Catch:{ all -> 0x0067 }
        monitor-exit(r2);	 Catch:{ all -> 0x0067 }
        goto L_0x004c;
    L_0x0067:
        r0 = move-exception;
        monitor-exit(r2);	 Catch:{ all -> 0x0067 }
        throw r0;
    L_0x006a:
        r2 = r7.running;
        monitor-enter(r2);
        r3 = new java.util.ArrayList;	 Catch:{ all -> 0x0098 }
        r3.<init>();	 Catch:{ all -> 0x0098 }
        r0 = r7.running;	 Catch:{ all -> 0x0098 }
        r4 = r0.iterator();	 Catch:{ all -> 0x0098 }
    L_0x0078:
        r0 = r4.hasNext();	 Catch:{ all -> 0x0098 }
        if (r0 == 0) goto L_0x009b;
    L_0x007e:
        r0 = r4.next();	 Catch:{ all -> 0x0098 }
        r0 = (com.mqunar.hy.res.libtask.TaskTrain) r0;	 Catch:{ all -> 0x0098 }
        r5 = r0.conductor;	 Catch:{ all -> 0x0098 }
        r6 = r1.conductor;	 Catch:{ all -> 0x0098 }
        r5 = r5.sameAs(r6);	 Catch:{ all -> 0x0098 }
        if (r5 == 0) goto L_0x0078;
    L_0x008e:
        r5 = r0.isCancelable();	 Catch:{ all -> 0x0098 }
        if (r5 == 0) goto L_0x0078;
    L_0x0094:
        r3.add(r0);	 Catch:{ all -> 0x0098 }
        goto L_0x0078;
    L_0x0098:
        r0 = move-exception;
        monitor-exit(r2);	 Catch:{ all -> 0x0098 }
        throw r0;
    L_0x009b:
        r0 = r7.running;	 Catch:{ all -> 0x0098 }
        r0.removeAll(r3);	 Catch:{ all -> 0x0098 }
        r3 = r3.iterator();	 Catch:{ all -> 0x0098 }
    L_0x00a4:
        r0 = r3.hasNext();	 Catch:{ all -> 0x0098 }
        if (r0 == 0) goto L_0x00b7;
    L_0x00aa:
        r0 = r3.next();	 Catch:{ all -> 0x0098 }
        r0 = (com.mqunar.hy.res.libtask.TaskTrain) r0;	 Catch:{ all -> 0x0098 }
        r0 = r0.conductor;	 Catch:{ all -> 0x0098 }
        r4 = 0;
        r0.cancel(r4);	 Catch:{ all -> 0x0098 }
        goto L_0x00a4;
    L_0x00b7:
        monitor-exit(r2);	 Catch:{ all -> 0x0098 }
        r2 = r7.waiting;
        monitor-enter(r2);
        r3 = new java.util.ArrayList;	 Catch:{ all -> 0x00e6 }
        r3.<init>();	 Catch:{ all -> 0x00e6 }
        r0 = r7.waiting;	 Catch:{ all -> 0x00e6 }
        r4 = r0.iterator();	 Catch:{ all -> 0x00e6 }
    L_0x00c6:
        r0 = r4.hasNext();	 Catch:{ all -> 0x00e6 }
        if (r0 == 0) goto L_0x00e9;
    L_0x00cc:
        r0 = r4.next();	 Catch:{ all -> 0x00e6 }
        r0 = (com.mqunar.hy.res.libtask.TaskTrain) r0;	 Catch:{ all -> 0x00e6 }
        r5 = r0.isCancelable();	 Catch:{ all -> 0x00e6 }
        if (r5 == 0) goto L_0x00c6;
    L_0x00d8:
        r5 = r0.conductor;	 Catch:{ all -> 0x00e6 }
        r6 = r1.conductor;	 Catch:{ all -> 0x00e6 }
        r5 = r5.sameAs(r6);	 Catch:{ all -> 0x00e6 }
        if (r5 == 0) goto L_0x00c6;
    L_0x00e2:
        r3.add(r0);	 Catch:{ all -> 0x00e6 }
        goto L_0x00c6;
    L_0x00e6:
        r0 = move-exception;
        monitor-exit(r2);	 Catch:{ all -> 0x00e6 }
        throw r0;
    L_0x00e9:
        r0 = r7.waiting;	 Catch:{ all -> 0x00e6 }
        r0.removeAll(r3);	 Catch:{ all -> 0x00e6 }
        r0 = r7.waiting;	 Catch:{ all -> 0x00e6 }
        r0.add(r1);	 Catch:{ all -> 0x00e6 }
        monitor-exit(r2);	 Catch:{ all -> 0x00e6 }
        goto L_0x004c;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.mqunar.hy.res.libtask.ChiefGuard.addTask(android.content.Context, com.mqunar.hy.res.libtask.AbsConductor, com.mqunar.hy.res.libtask.Ticket):void");
    }

    public void checkTasks() {
        if (this.waiting.size() != 0) {
            synchronized (this.waiting) {
                Iterator it = this.waiting.iterator();
                while (it.hasNext()) {
                    final TaskTrain taskTrain = (TaskTrain) it.next();
                    synchronized (this.running) {
                        this.running.add(taskTrain);
                    }
                    it.remove();
                    if (!taskTrain.isCancelled()) {
                        switch (taskTrain.cacheType()) {
                            case 0:
                            case 2:
                                taskTrain.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new Void[0]);
                                break;
                            case 1:
                                AsyncTask.CACHE_THREAD_EXECUTOR.execute(new Runnable() {
                                    public void run() {
                                        taskTrain.conductor.findCache(true);
                                    }
                                });
                                taskTrain.conductor.progress = ProgressType.PRO_END;
                                taskTrain.conductor.msgd.onMessage(TaskCode.TASK_PENDING, taskTrain.conductor);
                                this.trumpet.ok(taskTrain);
                                break;
                            case 3:
                                AsyncTask.CACHE_THREAD_EXECUTOR.execute(new Runnable() {
                                    public void run() {
                                        taskTrain.conductor.findCache(true);
                                    }
                                });
                                taskTrain.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new Void[0]);
                                break;
                            default:
                                break;
                        }
                    }
                    this.trumpet.cancel(taskTrain);
                }
            }
        }
    }

    public void cancelTaskByCallback(TaskCallback taskCallback) {
        cancelTaskByCallback(taskCallback, false);
    }

    public void cancelTaskByCallback(TaskCallback taskCallback, boolean z) {
        synchronized (this.waiting) {
            Iterator it = this.waiting.iterator();
            while (it.hasNext()) {
                TaskTrain taskTrain = (TaskTrain) it.next();
                if (taskTrain.conductor.msgd.hasCallback(taskCallback) && taskTrain.isCancelable()) {
                    it.remove();
                }
            }
        }
        synchronized (this.running) {
            Object arrayList = new ArrayList();
            for (TaskTrain taskTrain2 : this.running) {
                if (taskTrain2.conductor.msgd.hasCallback(taskCallback) && (z || taskTrain2.isCancelable())) {
                    arrayList.add(taskTrain2);
                }
            }
            this.running.removeAll(arrayList);
            it = arrayList.iterator();
            while (it.hasNext()) {
                ((TaskTrain) it.next()).conductor.cancel(z);
            }
        }
    }

    public static void destroy() {
        if (instance != null) {
            instance.cancelAll();
        }
        instance = null;
    }

    private void cancelAll() {
        synchronized (this.waiting) {
            this.waiting.clear();
        }
        synchronized (this.running) {
            for (TaskTrain taskTrain : this.running) {
                taskTrain.conductor.cancel(true);
            }
            this.running.clear();
        }
    }
}
