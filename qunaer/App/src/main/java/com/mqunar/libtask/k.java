package com.mqunar.libtask;

class k implements Runnable {
    final /* synthetic */ TaskTrain a;
    final /* synthetic */ ChiefGuard b;

    k(ChiefGuard chiefGuard, TaskTrain taskTrain) {
        this.b = chiefGuard;
        this.a = taskTrain;
    }

    public void run() {
        this.a.a.findCache(true);
    }
}
