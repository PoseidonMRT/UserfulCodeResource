package com.mqunar.cock.network;

import com.mqunar.libtask.AbsConductor;
import com.mqunar.libtask.TaskCallback;

public class TaskCallbackWrapper implements TaskCallback {
    private final TaskCallback a;

    public TaskCallbackWrapper(TaskCallback taskCallback) {
        this.a = taskCallback;
    }

    public void onMsgRequest(AbsConductor absConductor, boolean z) {
        this.a.onMsgRequest(absConductor, z);
    }

    public void onMsgResult(AbsConductor absConductor, boolean z) {
        this.a.onMsgResult(absConductor, z);
    }

    public void onMsgError(AbsConductor absConductor, boolean z) {
        this.a.onMsgError(absConductor, z);
    }

    public void onMsgProgress(AbsConductor absConductor, boolean z) {
        this.a.onMsgProgress(absConductor, z);
    }

    public void onMsgStart(AbsConductor absConductor, boolean z) {
        this.a.onMsgStart(absConductor, z);
    }

    public void onMsgEnd(AbsConductor absConductor, boolean z) {
        this.a.onMsgEnd(absConductor, z);
    }

    public void onMsgCancel(AbsConductor absConductor, boolean z) {
        this.a.onMsgCancel(absConductor, z);
    }

    public void onMsgCacheHit(AbsConductor absConductor, boolean z) {
        this.a.onMsgCacheHit(absConductor, z);
    }
}
