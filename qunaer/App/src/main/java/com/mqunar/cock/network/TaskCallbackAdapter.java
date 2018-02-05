package com.mqunar.cock.network;

import com.mqunar.libtask.AbsConductor;
import com.mqunar.libtask.TaskCallback;
import com.mqunar.tools.log.QLog;

public class TaskCallbackAdapter implements TaskCallback {
    ServiceMap b;

    public void onMsgRequest(AbsConductor absConductor, boolean z) {
    }

    public void onMsgResult(AbsConductor absConductor, boolean z) {
    }

    public void onMsgError(AbsConductor absConductor, boolean z) {
        QLog.d("onMsgError", new Object[0]);
    }

    public void onMsgProgress(AbsConductor absConductor, boolean z) {
    }

    public void onMsgStart(AbsConductor absConductor, boolean z) {
        QLog.d("onMsgStart", new Object[0]);
    }

    public void onMsgEnd(AbsConductor absConductor, boolean z) {
        QLog.d("onMsgEnd", new Object[0]);
    }

    public void onMsgCancel(AbsConductor absConductor, boolean z) {
        QLog.d("onMsgCancel", new Object[0]);
    }

    public void onMsgCacheHit(AbsConductor absConductor, boolean z) {
    }
}
