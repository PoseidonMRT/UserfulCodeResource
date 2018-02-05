package com.mqunar.libtask;

import android.os.Process;

class b extends i<Params, Result> {
    final /* synthetic */ AsyncTask a;

    b(AsyncTask asyncTask) {
        this.a = asyncTask;
        super();
    }

    public Result call() {
        this.a.mTaskInvoked.set(true);
        Process.setThreadPriority(10);
        return this.a.postResult(this.a.doInBackground(this.b));
    }
}
