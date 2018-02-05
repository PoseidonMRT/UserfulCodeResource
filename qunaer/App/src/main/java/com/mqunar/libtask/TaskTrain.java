package com.mqunar.libtask;

import android.content.Context;

public class TaskTrain extends AsyncTask<Void, Integer, Void> {
    final AbsConductor a;
    private final Ticket b;
    private final Trumpet c;
    public final Context context;

    public TaskTrain(Context context, AbsConductor absConductor, Ticket ticket, Trumpet trumpet) {
        this.context = context;
        this.a = absConductor;
        this.b = ticket;
        this.c = trumpet;
        absConductor.setTaskTrain(this);
    }

    protected Void doInBackground(Void... voidArr) {
        this.a.doingTask();
        publishProgress(Integer.valueOf(ProgressType.PRO_END));
        return null;
    }

    protected void onProgressUpdate(Integer... numArr) {
        this.a.onProgressUpdate(numArr);
    }

    final boolean a() {
        if (!isCancelable()) {
            return false;
        }
        cancel(true);
        return true;
    }

    protected void onCancelled(Void voidR) {
        if (this.c != null) {
            this.c.cancel(this);
        }
    }

    protected void onPostExecute(Void voidR) {
        if (this.c != null) {
            this.c.ok(this);
        }
    }

    public boolean isCancelable() {
        return this.b.cancelable;
    }

    public int cacheType() {
        return this.b.cacheType;
    }

    public Ticket getTicket() {
        return this.b;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        TaskTrain taskTrain = (TaskTrain) obj;
        if (this.a != null) {
            if (this.a.equals(taskTrain.a)) {
                return true;
            }
        } else if (taskTrain.a == null) {
            return true;
        }
        return false;
    }

    public int hashCode() {
        return this.a != null ? this.a.hashCode() : 0;
    }
}
