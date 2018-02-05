package com.mqunar.hy.res;

import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import com.mqunar.hy.res.Listener.QpDownloadListener;
import com.mqunar.hy.res.logger.Timber;
import com.mqunar.hy.res.model.HybridInfo;
import com.mqunar.hy.res.model.HybridParam;
import com.mqunar.hy.res.model.HybridResult;
import com.mqunar.hy.res.model.HybridResult.MgData;
import com.mqunar.hy.res.upgrade.HyResDownloadTask;
import com.mqunar.hy.res.upgrade.HyResUpdateRequestTask;
import com.mqunar.hy.res.utils.DownloadManager;
import com.mqunar.hy.res.utils.SpCahceUtil;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ManualDownloadControler {
    private DownloadTaskResult<HybridInfo> downloadTaskResult = null;
    private HybridInfo hybridInfo = null;
    private List<HybridInfo> infoList = null;
    private Handler mHandler = new Handler(Looper.getMainLooper());
    private HybridInfo newHybridInfo = null;
    private QpDownloadListener qpDownloadListener = null;

    class DownloadCallback implements DownloadTaskResult<HybridInfo> {
        private DownloadCallback() {
        }

        public void sucess(HybridInfo hybridInfo) {
            Timber.i("Hyres ManualDownloadControler sucess = " + hybridInfo.toJsonString(), new Object[0]);
            ManualDownloadControler.this.mHandler.post(new Runnable() {
                public void run() {
                    if (ManualDownloadControler.this.qpDownloadListener != null) {
                        ManualDownloadControler.this.qpDownloadListener.onQpDownLoaded();
                    }
                }
            });
        }

        public void error(HybridInfo hybridInfo, int i, String str) {
            Timber.i("Hyres ManualDownloadControler HybridInfo = " + hybridInfo.toJsonString() + "; errorcode = " + i + "; errorMessage = " + str, new Object[0]);
            ManualDownloadControler.this.mHandler.post(new Runnable() {
                public void run() {
                    if (ManualDownloadControler.this.qpDownloadListener != null) {
                        ManualDownloadControler.this.qpDownloadListener.onMessageError();
                    }
                }
            });
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
                    ArrayList arrayList = mgData.hlist;
                    if (arrayList == null) {
                        return;
                    }
                    if (arrayList.size() <= 0) {
                        ManualDownloadControler.this.mHandler.post(new Runnable() {
                            public void run() {
                                if (ManualDownloadControler.this.qpDownloadListener != null) {
                                    ManualDownloadControler.this.qpDownloadListener.requestResultNoQp();
                                }
                            }
                        });
                        return;
                    }
                    if (this.info != null) {
                        Iterator it = arrayList.iterator();
                        while (it.hasNext()) {
                            HybridInfo hybridInfo = (HybridInfo) it.next();
                            hybridInfo.QpRequestType = this.info.QpRequestType;
                            if (this.info.hybridId.equals(hybridInfo.hybridId)) {
                                ManualDownloadControler.this.newHybridInfo = hybridInfo;
                            }
                        }
                    }
                    ManualDownloadControler.this.mHandler.post(new Runnable() {
                        public void run() {
                            if (ManualDownloadControler.this.qpDownloadListener == null) {
                                return;
                            }
                            if (ManualDownloadControler.this.newHybridInfo != null) {
                                ManualDownloadControler.this.qpDownloadListener.requestResultHasQp();
                            } else {
                                ManualDownloadControler.this.qpDownloadListener.requestResultNoQp();
                            }
                        }
                    });
                }
            }
        }

        public void error(int i, String str) {
            ManualDownloadControler.this.mHandler.post(new Runnable() {
                public void run() {
                    if (ManualDownloadControler.this.qpDownloadListener != null) {
                        ManualDownloadControler.this.qpDownloadListener.onMessageError();
                    }
                }
            });
            Timber.i("Hyres ManualDownloadControler errorcode = " + i + "; errorMessage = " + str, new Object[0]);
        }
    }

    public void startUpdateRequest(String str, QpDownloadListener qpDownloadListener) {
        if (TextUtils.isEmpty(str)) {
            throw new NullPointerException("hybridId can not null");
        }
        this.qpDownloadListener = qpDownloadListener;
        this.infoList = HybridManager.getInstance().getHybridInfos();
        this.hybridInfo = HybridManager.getInstance().getCustomerHybridInfo(str);
        HyResUpdateRequestTask.getInstance().run(this.infoList, new RequestCallback(this.hybridInfo), this.hybridInfo);
    }

    public void unregister() {
        if (this.downloadTaskResult != null) {
            DownloadManager.getInstance().removeDownloadTaskResult(this.hybridInfo, this.downloadTaskResult);
        }
    }

    public void startDownload() {
        if (this.newHybridInfo == null) {
            this.mHandler.post(new Runnable() {
                public void run() {
                    if (ManualDownloadControler.this.qpDownloadListener != null) {
                        ManualDownloadControler.this.qpDownloadListener.onMessageError();
                    }
                }
            });
            return;
        }
        HybridInfo cacheHybridInfo = SpCahceUtil.getInstance().getCacheHybridInfo(this.hybridInfo.hybridId);
        if (cacheHybridInfo == null || cacheHybridInfo.version < this.newHybridInfo.version) {
            HybridParam hybridParam;
            List arrayList;
            String str = this.newHybridInfo.hybridId;
            for (HybridInfo cacheHybridInfo2 : this.infoList) {
                if (str.equals(cacheHybridInfo2.hybridId)) {
                    if (cacheHybridInfo2.version >= this.newHybridInfo.version) {
                        this.mHandler.post(new Runnable() {
                            public void run() {
                                if (ManualDownloadControler.this.qpDownloadListener != null) {
                                    ManualDownloadControler.this.qpDownloadListener.onQpDownLoaded();
                                }
                            }
                        });
                        return;
                    }
                    hybridParam = new HybridParam(this.infoList);
                    arrayList = new ArrayList();
                    arrayList.add(this.newHybridInfo);
                    new HyResDownloadTask(HyResInitializer.getContext(), hybridParam).run(arrayList, new DownloadCallback());
                    return;
                }
            }
            hybridParam = new HybridParam(this.infoList);
            arrayList = new ArrayList();
            arrayList.add(this.newHybridInfo);
            new HyResDownloadTask(HyResInitializer.getContext(), hybridParam).run(arrayList, new DownloadCallback());
            return;
        }
        this.mHandler.post(new Runnable() {
            public void run() {
                if (ManualDownloadControler.this.qpDownloadListener != null) {
                    ManualDownloadControler.this.qpDownloadListener.onQpDownLoaded();
                }
            }
        });
    }

    public void forceReplace() {
        if (this.newHybridInfo != null) {
            HybridInfo cacheHybridInfo = SpCahceUtil.getInstance().getCacheHybridInfo(this.newHybridInfo.hybridId);
            if (cacheHybridInfo != null) {
                cacheHybridInfo = HybridManager.getInstance().moveToWorkspace(cacheHybridInfo);
                if (cacheHybridInfo != null) {
                    HybridManager.getInstance().addNewModule(cacheHybridInfo.path, cacheHybridInfo.getMd5());
                }
                SpCahceUtil.getInstance().deleteCacheHyInfo(this.newHybridInfo.hybridId);
            }
        }
    }
}
