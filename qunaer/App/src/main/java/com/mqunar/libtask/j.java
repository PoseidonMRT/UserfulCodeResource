package com.mqunar.libtask;

class j implements Trumpet {
    final /* synthetic */ ChiefGuard a;

    j(ChiefGuard chiefGuard) {
        this.a = chiefGuard;
    }

    public void ok(TaskTrain taskTrain) {
        synchronized (this.a.c) {
            this.a.c.remove(taskTrain);
        }
        this.a.checkTasks();
    }

    public void cancel(TaskTrain taskTrain) {
        synchronized (this.a.c) {
            this.a.c.remove(taskTrain);
        }
        this.a.checkTasks();
    }
}
