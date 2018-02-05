package com.mqunar.hy.res.libtask;

import com.mqunar.hy.res.utils.ArrayUtils;
import com.mqunar.libtask.ProgressType;
import java.util.List;

class MsgDispatcher {
    private final TaskCallback[] callbacks;
    private final Ticket ticket;

    public MsgDispatcher(Ticket ticket, TaskCallback... taskCallbackArr) {
        this.ticket = ticket;
        if (taskCallbackArr == null || taskCallbackArr.length == 0) {
            this.callbacks = new TaskCallback[0];
        } else {
            this.callbacks = taskCallbackArr;
        }
    }

    public void onMessage(TaskCode taskCode, AbsConductor absConductor) {
        int i = 0;
        if (taskCode == null) {
            throw new IllegalArgumentException("taskCode must not be null");
        }
        boolean z = !Thread.currentThread().getName().contains("QTask #");
        TaskCallback[] taskCallbackArr;
        int length;
        switch (taskCode) {
            case TASK_CANCEL:
                taskCallbackArr = this.callbacks;
                length = taskCallbackArr.length;
                while (i < length) {
                    taskCallbackArr[i].onMsgCancel(absConductor, z);
                    i++;
                }
                return;
            case TASK_REQUEST:
                taskCallbackArr = this.callbacks;
                length = taskCallbackArr.length;
                while (i < length) {
                    taskCallbackArr[i].onMsgRequest(absConductor, z);
                    i++;
                }
                return;
            case TASK_RESULT:
                taskCallbackArr = this.callbacks;
                length = taskCallbackArr.length;
                while (i < length) {
                    taskCallbackArr[i].onMsgResult(absConductor, z);
                    i++;
                }
                return;
            case TASK_PENDING:
                taskCallbackArr = this.callbacks;
                length = taskCallbackArr.length;
                while (i < length) {
                    TaskCallback taskCallback = taskCallbackArr[i];
                    if (this.ticket != null && this.ticket.progress) {
                        taskCallback.onMsgProgress(absConductor, z);
                    } else if (absConductor.getProgress() == Integer.MIN_VALUE) {
                        taskCallback.onMsgStart(absConductor, z);
                    } else if (absConductor.getProgress() == ProgressType.PRO_END) {
                        taskCallback.onMsgEnd(absConductor, z);
                    }
                    i++;
                }
                return;
            case TASK_ERROR:
                taskCallbackArr = this.callbacks;
                length = taskCallbackArr.length;
                while (i < length) {
                    taskCallbackArr[i].onMsgError(absConductor, z);
                    i++;
                }
                return;
            case TASK_CACHE_HIT:
                taskCallbackArr = this.callbacks;
                length = taskCallbackArr.length;
                while (i < length) {
                    taskCallbackArr[i].onMsgCacheHit(absConductor, z);
                    i++;
                }
                return;
            default:
                return;
        }
    }

    public boolean hasCallback(TaskCallback taskCallback) {
        return ArrayUtils.asReadOnlyList(this.callbacks).contains(taskCallback);
    }

    public List<TaskCallback> getCallbacks() {
        return ArrayUtils.asReadOnlyList(this.callbacks);
    }
}
