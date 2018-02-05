package com.mqunar.hy.res;

import com.mqunar.hy.res.logger.Timber;
import com.mqunar.hy.res.model.HybridInfo;
import com.mqunar.hy.res.model.HybridParam;
import com.mqunar.hy.res.model.HybridResult;
import com.mqunar.hy.res.model.HybridResult.MgData;
import com.mqunar.hy.res.upgrade.HyResDownloadTask;
import com.mqunar.hy.res.upgrade.HyResUpdateRequestTask;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class AutoDownloadControler {
    private List<HybridInfo> infoList = null;

    class DownloadCallback implements DownloadTaskResult<HybridInfo> {
        private DownloadCallback() {
        }

        public void sucess(HybridInfo hybridInfo) {
            Timber.i("Hyres AutoDownloadControler sucess = " + hybridInfo.toJsonString(), new Object[0]);
        }

        public void error(HybridInfo hybridInfo, int i, String str) {
            Timber.i("Hyres AutoDownloadControler HybridInfo = " + hybridInfo.toJsonString() + "; errorcode = " + i + "; errorMessage = " + str, new Object[0]);
        }
    }

    class RequestCallback implements TaskResult<HybridResult> {
        private HybridInfo info = null;

        public RequestCallback(HybridInfo hybridInfo) {
            this.info = hybridInfo;
        }

        public void sucess(HybridResult hybridResult) {
            if (hybridResult != null) {
                MgData mgData = hybridResult.data;
                if (mgData != null) {
                    List list = mgData.hlist;
                    if (list != null && list.size() > 0) {
                        if (this.info != null) {
                            Iterator it = list.iterator();
                            while (it.hasNext()) {
                                ((HybridInfo) it.next()).QpRequestType = this.info.QpRequestType;
                            }
                        }
                        HybridParam hybridParam = new HybridParam(AutoDownloadControler.this.infoList);
                        Collections.reverse(list);
                        new HyResDownloadTask(HyResInitializer.getContext(), hybridParam).run(list, new DownloadCallback());
                    }
                }
            }
        }

        public void error(int i, String str) {
            Timber.i("Hyres AutoDownloadControler errorcode = " + i + "; errorMessage = " + str, new Object[0]);
        }
    }

    public void startUpdateRequest() {
        HybridManager.getInstance().addCacheList();
        startUpdateRequest(null);
    }

    public void startUpdateRequest(HybridInfo hybridInfo) {
        this.infoList = HybridManager.getInstance().getHybridInfos();
        HyResUpdateRequestTask.getInstance().run(this.infoList, new RequestCallback(hybridInfo), hybridInfo);
    }
}
