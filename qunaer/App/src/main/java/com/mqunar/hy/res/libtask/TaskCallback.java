package com.mqunar.hy.res.libtask;

import android.os.Handler;

public interface TaskCallback<T extends AbsConductor> {

    public class AsHandler<T extends AbsConductor> implements TaskCallback<T> {
        private Handler handler;

        public AsHandler(Handler handler) {
            this.handler = handler;
            if (handler == null) {
                throw new IllegalArgumentException("handler must not be null");
            }
        }

        public void onMsgRequest(T t, boolean z) {
            this.handler.obtainMessage(TaskCode.TASK_REQUEST.getCode(), t).sendToTarget();
        }

        public void onMsgResult(T t, boolean z) {
            this.handler.obtainMessage(TaskCode.TASK_RESULT.getCode(), t).sendToTarget();
        }

        public void onMsgError(T t, boolean z) {
            this.handler.obtainMessage(TaskCode.TASK_ERROR.getCode(), t).sendToTarget();
        }

        public void onMsgProgress(T t, boolean z) {
            this.handler.obtainMessage(TaskCode.TASK_PENDING.getCode(), t).sendToTarget();
        }

        public void onMsgStart(T t, boolean z) {
            this.handler.obtainMessage(TaskCode.TASK_PENDING.getCode(), t).sendToTarget();
        }

        public void onMsgEnd(T t, boolean z) {
            this.handler.obtainMessage(TaskCode.TASK_PENDING.getCode(), t).sendToTarget();
        }

        public void onMsgCancel(T t, boolean z) {
            this.handler.obtainMessage(TaskCode.TASK_CANCEL.getCode(), t).sendToTarget();
        }

        public void onMsgCacheHit(T t, boolean z) {
            this.handler.obtainMessage(TaskCode.TASK_CACHE_HIT.getCode(), t).sendToTarget();
        }
    }

    void onMsgCacheHit(T t, boolean z);

    void onMsgCancel(T t, boolean z);

    void onMsgEnd(T t, boolean z);

    void onMsgError(T t, boolean z);

    void onMsgProgress(T t, boolean z);

    void onMsgRequest(T t, boolean z);

    void onMsgResult(T t, boolean z);

    void onMsgStart(T t, boolean z);
}
