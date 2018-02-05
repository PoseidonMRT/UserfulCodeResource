package com.mqunar.hy.res.upgrade;

import android.content.Context;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.Feature;
import com.mqunar.hy.res.TaskResult;
import com.mqunar.hy.res.libtask.AbsConductor;
import com.mqunar.hy.res.libtask.TaskCallback;
import com.mqunar.hy.res.logger.Timber;
import com.mqunar.hy.res.model.HybridParam;
import com.mqunar.hy.res.model.HybridResult;
import com.mqunar.hy.res.utils.CheckQpCompetence;
import com.mqunar.hy.res.utils.ErrorCodeAndMessage;
import com.mqunar.hy.res.utils.UpgradeInfoCache;
import java.lang.reflect.Type;

class HybridUpdateCallback implements TaskCallback {
    private Context context;
    private HybridParam param;
    private HyResUpdateRequestTask task;
    private TaskResult<HybridResult> taskResult = null;

    public HybridUpdateCallback(Context context, HyResUpdateRequestTask hyResUpdateRequestTask, HybridParam hybridParam, TaskResult<HybridResult> taskResult) {
        this.context = context;
        this.task = hyResUpdateRequestTask;
        this.param = hybridParam;
        this.taskResult = taskResult;
    }

    public void onMsgRequest(AbsConductor absConductor, boolean z) {
    }

    public void onMsgResult(AbsConductor absConductor, boolean z) {
        try {
            byte[] bArr = (byte[]) absConductor.getResult();
            if (bArr != null && bArr.length != 0) {
                try {
                    Timber.i("result = " + new String(bArr, "utf-8"), new Object[0]);
                } catch (Throwable e) {
                    Timber.e(e, new Object[0]);
                }
                HybridResult hybridResult = (HybridResult) JSON.parseObject(bArr, (Type) HybridResult.class, new Feature[0]);
                if (hybridResult == null || hybridResult.status != 0) {
                    onMsgError(absConductor, z);
                    return;
                }
                if (!this.param.isSingleRequest()) {
                    this.task.setStatus((byte) 2);
                }
                if (hybridResult.data != null) {
                    UpgradeInfoCache.getInstance().addHybridInfos(hybridResult.data.hlist);
                    if (hybridResult.data.offline_hlist != null && hybridResult.data.offline_hlist.size() > 0) {
                        CheckQpCompetence.getInstance().updateOfflineHybridInfo(hybridResult.data.offline_hlist);
                    }
                    if (this.taskResult != null) {
                        this.taskResult.sucess(hybridResult);
                    }
                }
            }
        } catch (Exception e2) {
            e2.printStackTrace();
        }
    }

    public void onMsgError(AbsConductor absConductor, boolean z) {
        this.task.setStatus((byte) 3);
        if (this.taskResult != null) {
            this.taskResult.error(100, ErrorCodeAndMessage.NETWORK_CONNECTIONS_ERROR_MESSAGE);
        }
    }

    public void onMsgProgress(AbsConductor absConductor, boolean z) {
    }

    public void onMsgStart(AbsConductor absConductor, boolean z) {
    }

    public void onMsgEnd(AbsConductor absConductor, boolean z) {
        if (this.param.isSingleRequest()) {
            this.param.current.QpRequestType = 0;
        }
    }

    public void onMsgCancel(AbsConductor absConductor, boolean z) {
        if (this.taskResult != null) {
            this.taskResult.error(101, ErrorCodeAndMessage.NETWORK_CONNECTIONS_CANCEL_MESSAGE);
        }
    }

    public void onMsgCacheHit(AbsConductor absConductor, boolean z) {
    }
}
