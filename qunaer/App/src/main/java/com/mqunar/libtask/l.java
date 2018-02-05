package com.mqunar.libtask;

class l implements Runnable {
    final /* synthetic */ TaskTrain a;
    final /* synthetic */ ChiefGuard b;

    l(ChiefGuard chiefGuard, TaskTrain taskTrain) {
        this.b = chiefGuard;
        this.a = taskTrain;
    }

    public void run() {
        this.a.a.findCache(true);
    }
}
