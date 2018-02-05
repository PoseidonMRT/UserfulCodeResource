package com.mqunar.libtask;

import com.mqunar.tools.ArrayUtils;
import com.mqunar.tools.CheckUtils;
import java.util.List;

public class MsgDispatcher {
    private final TaskCallback[] a;
    private final Ticket b;

    public MsgDispatcher(Ticket ticket, TaskCallback... taskCallbackArr) {
        this.b = ticket;
        if (CheckUtils.isContainsEmpty(taskCallbackArr)) {
            this.a = new TaskCallback[0];
        } else {
            this.a = taskCallbackArr;
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
        TaskCallback taskCallback;
        switch (n.a[taskCode.ordinal()]) {
            case 1:
                taskCallbackArr = this.a;
                length = taskCallbackArr.length;
                while (i < length) {
                    taskCallback = taskCallbackArr[i];
                    absConductor.putExtraData(Statistics.KEY_TASK_CANCEL, Long.valueOf(System.currentTimeMillis()));
                    taskCallback.onMsgCancel(absConductor, z);
                    i++;
                }
                return;
            case 2:
                taskCallbackArr = this.a;
                length = taskCallbackArr.length;
                while (i < length) {
                    taskCallback = taskCallbackArr[i];
                    absConductor.putExtraData(Statistics.KEY_TASK_REQUEST, Long.valueOf(System.currentTimeMillis()));
                    taskCallback.onMsgRequest(absConductor, z);
                    i++;
                }
                return;
            case 3:
                taskCallbackArr = this.a;
                length = taskCallbackArr.length;
                while (i < length) {
                    taskCallback = taskCallbackArr[i];
                    absConductor.putExtraData(Statistics.KEY_TASK_RESULT, Long.valueOf(System.currentTimeMillis()));
                    taskCallback.onMsgResult(absConductor, z);
                    i++;
                }
                return;
            case 4:
                taskCallbackArr = this.a;
                length = taskCallbackArr.length;
                while (i < length) {
                    taskCallback = taskCallbackArr[i];
                    if (this.b != null && this.b.progress) {
                        taskCallback.onMsgProgress(absConductor, z);
                    } else if (absConductor.getProgress() == Integer.MIN_VALUE) {
                        if (absConductor.getExtraData(Statistics.KEY_TASK_START) == null) {
                            absConductor.putExtraData(Statistics.KEY_TASK_START, Long.valueOf(System.currentTimeMillis()));
                        }
                        taskCallback.onMsgStart(absConductor, z);
                    } else if (absConductor.getProgress() == ProgressType.PRO_END) {
                        if (absConductor.getExtraData(Statistics.KEY_TASK_END) == null) {
                            absConductor.putExtraData(Statistics.KEY_TASK_END, Long.valueOf(System.currentTimeMillis()));
                        }
                        taskCallback.onMsgEnd(absConductor, z);
                    }
                    i++;
                }
                return;
            case 5:
                taskCallbackArr = this.a;
                length = taskCallbackArr.length;
                while (i < length) {
                    taskCallback = taskCallbackArr[i];
                    absConductor.putExtraData(Statistics.KEY_TASK_ERR, Long.valueOf(System.currentTimeMillis()));
                    taskCallback.onMsgError(absConductor, z);
                    i++;
                }
                return;
            case 6:
                taskCallbackArr = this.a;
                length = taskCallbackArr.length;
                while (i < length) {
                    taskCallback = taskCallbackArr[i];
                    absConductor.putExtraData(Statistics.KEY_TASK_CACAHEHIT, Long.valueOf(System.currentTimeMillis()));
                    taskCallback.onMsgCacheHit(absConductor, z);
                    i++;
                }
                return;
            default:
                return;
        }
    }

    public boolean hasCallback(TaskCallback taskCallback) {
        return ArrayUtils.asReadOnlyList(this.a).contains(taskCallback);
    }

    public List<TaskCallback> getCallbacks() {
        return ArrayUtils.asReadOnlyList(this.a);
    }
}
