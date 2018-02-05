package com.mqunar.hy.res.utils;

import android.os.Handler;
import android.text.TextUtils;
import com.mqunar.hy.res.DownloadTaskResult;
import com.mqunar.hy.res.logger.Timber;
import com.mqunar.hy.res.model.HybridInfo;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class DownloadManager {
    private LinkedList<DownloadItem> downloadItems;
    private Map<String, Set<DownloadTaskResult<HybridInfo>>> downloadTaskResultMap;
    public Map<String, DownloadData> nowDownloadMap;

    class DownloadItem {
        private DownloadData downloadData;
        private String url;

        public DownloadItem(String str, DownloadData downloadData) {
            this.url = str;
            this.downloadData = downloadData;
        }

        public String getUrl() {
            return this.url;
        }

        public DownloadData getDownloadData() {
            return this.downloadData;
        }
    }

    class SingletonHolder {
        private static DownloadManager instance = new DownloadManager();

        private SingletonHolder() {
        }
    }

    private DownloadManager() {
        this.downloadItems = null;
        this.downloadTaskResultMap = new HashMap();
        this.nowDownloadMap = Collections.synchronizedMap(new HashMap());
        this.downloadItems = new LinkedList();
    }

    public static DownloadManager getInstance() {
        return SingletonHolder.instance;
    }

    public synchronized boolean isRun(String str) {
        boolean z;
        DownloadData downloadData = getDownloadData(str);
        if (downloadData == null) {
            z = false;
        } else {
            z = DownloadData.DOWNLOAD_START.equals(downloadData.getDownloadState());
        }
        return z;
    }

    public synchronized boolean hasRun() {
        boolean z;
        for (Entry entry : this.nowDownloadMap.entrySet()) {
            Timber.i("DownloadManager>nowDownloadMap-> " + ((String) entry.getKey()), new Object[0]);
            if (isRun((String) entry.getKey())) {
                z = true;
                break;
            }
        }
        z = false;
        return z;
    }

    public synchronized DownloadData add(String str, String str2, String str3, int i, Handler handler) {
        DownloadData downloadData;
        DownloadItem downloadItem = getDownloadItem(str2);
        if (downloadItem != null) {
            this.downloadItems.remove(downloadItem);
        }
        downloadData = new DownloadData(str, str2, str3, i, handler);
        this.downloadItems.addFirst(new DownloadItem(str2, downloadData));
        return downloadData;
    }

    public synchronized void cancel(String str) {
        if (!TextUtils.isEmpty(str)) {
            DownloadItem downloadItem = getDownloadItem(str);
            cancelDownloadItem(downloadItem);
            this.downloadItems.remove(downloadItem);
        }
    }

    public synchronized void cancelAll() {
        Iterator it = this.downloadItems.iterator();
        while (it.hasNext()) {
            cancelDownloadItem((DownloadItem) it.next());
        }
        this.downloadItems.clear();
    }

    private synchronized void cancelDownloadItem(DownloadItem downloadItem) {
        if (downloadItem != null) {
            DownloadData access$200 = downloadItem.downloadData;
            if (access$200 != null) {
                access$200.cancel();
                String url = downloadItem.getUrl();
                try {
                    Timber.i("暂停下载的qp:" + url + "正在下载模块的url:" + this.nowDownloadMap.keySet().toString(), new Object[0]);
                } catch (Exception e) {
                }
                if (this.nowDownloadMap.containsKey(url)) {
                    this.nowDownloadMap.remove(url);
                }
            }
        }
    }

    public synchronized void remove(String str) {
        DownloadItem downloadItem = getDownloadItem(str);
        if (downloadItem != null) {
            this.downloadItems.remove(downloadItem);
            if (this.nowDownloadMap.containsKey(str)) {
                this.nowDownloadMap.remove(str);
            }
            Timber.w("removed download ->" + str, new Object[0]);
        }
    }

    private synchronized DownloadItem getDownloadItem(String str) {
        DownloadItem downloadItem;
        Iterator it = this.downloadItems.iterator();
        while (it.hasNext()) {
            downloadItem = (DownloadItem) it.next();
            if (str.equals(downloadItem.getUrl())) {
                break;
            }
        }
        downloadItem = null;
        return downloadItem;
    }

    public synchronized boolean runNext() {
        boolean z;
        if (this.downloadItems.isEmpty()) {
            z = true;
        } else {
            Iterator it = this.downloadItems.iterator();
            if (it.hasNext()) {
                String url = ((DownloadItem) it.next()).getUrl();
                Timber.w("download url -> " + url, new Object[0]);
                start(url);
            }
            z = this.downloadItems.isEmpty();
        }
        return z;
    }

    public synchronized int size() {
        return this.downloadItems.size();
    }

    public synchronized DownloadData getDownloadData(String str) {
        DownloadItem downloadItem;
        downloadItem = getDownloadItem(str);
        return downloadItem == null ? null : downloadItem.getDownloadData();
    }

    public synchronized void start(String str) {
        DownloadData downloadData = getDownloadData(str);
        if (downloadData != null) {
            if (DownloadData.DOWNLOAD_NONE.equals(downloadData.getDownloadState())) {
                this.nowDownloadMap.put(str, downloadData);
                downloadData.run();
            } else {
                Timber.w("already started" + str, new Object[0]);
            }
        }
    }

    public synchronized void setDownloadTaskResult(HybridInfo hybridInfo, DownloadTaskResult<HybridInfo> downloadTaskResult) {
        if (hybridInfo != null) {
            Set set = (Set) this.downloadTaskResultMap.get(hybridInfo.hybridId);
            if (set == null) {
                set = new HashSet();
            }
            set.add(downloadTaskResult);
            this.downloadTaskResultMap.put(hybridInfo.hybridId, set);
        }
    }

    public synchronized Set<DownloadTaskResult<HybridInfo>> getDownloadTaskResult(HybridInfo hybridInfo) {
        Set<DownloadTaskResult<HybridInfo>> emptySet;
        if (hybridInfo == null) {
            emptySet = Collections.emptySet();
        } else {
            Set set = (Set) this.downloadTaskResultMap.get(hybridInfo.hybridId);
            if (set == null) {
                set = Collections.emptySet();
            }
            emptySet = Collections.unmodifiableSet(set);
        }
        return emptySet;
    }

    public synchronized void clearDownloadTaskResult() {
        this.downloadTaskResultMap.clear();
    }

    public synchronized boolean removeDownloadTaskResult(HybridInfo hybridInfo, DownloadTaskResult<HybridInfo> downloadTaskResult) {
        boolean z;
        if (hybridInfo == null) {
            z = false;
        } else {
            Set set = (Set) this.downloadTaskResultMap.get(hybridInfo.hybridId);
            if (set == null) {
                z = false;
            } else {
                z = set.remove(downloadTaskResult);
            }
        }
        return z;
    }
}
