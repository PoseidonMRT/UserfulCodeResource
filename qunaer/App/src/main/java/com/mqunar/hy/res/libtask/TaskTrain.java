package com.mqunar.hy.res.libtask;

import android.content.Context;
import android.os.AsyncTask;
import com.mqunar.libtask.ProgressType;

class TaskTrain extends AsyncTask<Void, Integer, Void> {
    final AbsConductor conductor;
    public final Context context;
    private final Ticket ticket;
    private final Trumpet trumpet;

    public TaskTrain(Context context, AbsConductor absConductor, Ticket ticket, Trumpet trumpet) {
        this.context = context;
        this.conductor = absConductor;
        this.ticket = ticket;
        this.trumpet = trumpet;
        absConductor.setTaskTrain(this);
    }

    protected Void doInBackground(Void... voidArr) {
        try {
            this.conductor.doingTask();
        } catch (Exception e) {
            e.printStackTrace();
        }
        publishProgress(new Integer[]{Integer.valueOf(ProgressType.PRO_END)});
        return null;
    }

    protected void onProgressUpdate(Integer... numArr) {
        this.conductor.onProgressUpdate(numArr);
    }

    final boolean cancel() {
        if (!isCancelable()) {
            return false;
        }
        cancel(true);
        return true;
    }

    protected void onCancelled(Void voidR) {
        if (this.trumpet != null) {
            this.trumpet.cancel(this);
        }
    }

    protected void onPostExecute(Void voidR) {
        if (this.trumpet != null) {
            this.trumpet.ok(this);
        }
    }

    public boolean isCancelable() {
        return this.ticket.cancelable;
    }

    public int cacheType() {
        return this.ticket.cacheType;
    }

    public Ticket getTicket() {
        return this.ticket;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        TaskTrain taskTrain = (TaskTrain) obj;
        if (this.conductor != null) {
            if (this.conductor.equals(taskTrain.conductor)) {
                return true;
            }
        } else if (taskTrain.conductor == null) {
            return true;
        }
        return false;
    }

    public int hashCode() {
        return this.conductor != null ? this.conductor.hashCode() : 0;
    }
}
