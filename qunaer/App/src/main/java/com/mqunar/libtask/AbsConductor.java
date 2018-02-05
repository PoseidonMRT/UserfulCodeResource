package com.mqunar.libtask;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

public abstract class AbsConductor {
    private boolean called;
    protected int currentLength = -1;
    protected int error = 0;
    private Map<Object, Object> extraMap;
    protected final MsgDispatcher msgd;
    protected Object[] params;
    protected int progress = Integer.MIN_VALUE;
    protected long requestTotal = -1;
    protected Object result;
    protected long resultTotal = -1;
    protected Object resultType;
    protected AtomicReference<TaskCode> status = new AtomicReference(TaskCode.TASK_NONE);
    private Ticket ticket;
    protected TaskTrain train;

    protected abstract void doingTask();

    public abstract boolean equals(Object obj);

    public abstract Object findCache(boolean z);

    protected abstract int getEmpId();

    protected abstract String getEmpName();

    public abstract int hashCode();

    public abstract <T extends AbsConductor> boolean sameAs(T t);

    protected AbsConductor(TaskCallback... taskCallbackArr) {
        this.msgd = new MsgDispatcher(this.ticket, taskCallbackArr);
    }

    public final Object putExtraData(Object obj, Object obj2) {
        if (this.extraMap == null) {
            this.extraMap = new HashMap(10);
        }
        return this.extraMap.put(obj, obj2);
    }

    public Object getExtraData(Object obj) {
        return this.extraMap == null ? null : this.extraMap.get(obj);
    }

    final void setTaskTrain(TaskTrain taskTrain) {
        this.train = taskTrain;
        if (this.called) {
            this.ticket = taskTrain.getTicket();
            checkTicket(this.ticket);
            return;
        }
        throw new IllegalStateException("you must call initParam first");
    }

    protected void checkTicket(Ticket ticket) {
    }

    public void setParams(Object... objArr) {
        initParam(objArr);
    }

    protected final void initParam(Object[] objArr) {
        this.params = objArr;
        this.called = true;
    }

    public Object[] getParams() {
        return this.params;
    }

    protected void onProgressUpdate(Integer... numArr) {
        if (numArr != null && numArr.length == 1) {
            this.progress = numArr[0].intValue();
            this.msgd.onMessage(TaskCode.TASK_PENDING, this);
        }
    }

    protected void onCancelled() {
        this.msgd.onMessage(TaskCode.TASK_CANCEL, this);
    }

    public boolean cancel() {
        return cancel(false);
    }

    public boolean cancel(boolean z) {
        if (!z && this.train != null && !this.train.isCancelable()) {
            return false;
        }
        if (!((TaskCode) this.status.getAndSet(TaskCode.TASK_CANCEL)).equals(TaskCode.TASK_CANCEL)) {
            this.status.set(TaskCode.TASK_CANCEL);
            onCancelled();
        }
        if (this.train != null) {
            this.train.a();
        }
        return true;
    }

    public TaskCode getStatus() {
        return (TaskCode) this.status.get();
    }

    public Object getResult() {
        return this.result;
    }

    public Object getResultType() {
        return this.resultType;
    }

    public int getCurrentLength() {
        return this.currentLength;
    }

    public long getRequestTotal() {
        return this.requestTotal;
    }

    public long getResultTotal() {
        return this.resultTotal;
    }

    public int getError() {
        return this.error;
    }

    public int getProgress() {
        return this.progress;
    }

    public List<TaskCallback> getCallbacks() {
        return this.msgd.getCallbacks();
    }

    public final String fullId() {
        return getClass().getName() + ":" + getEmpId() + ":" + getEmpName();
    }

    public void beforeAdd() {
    }
}
